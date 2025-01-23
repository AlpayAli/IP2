from datetime import datetime
import numpy as np
import sqlite3
import json
import os
from langchain_ollama import OllamaEmbeddings
from langchain_community.vectorstores import FAISS
import requests
import shutil
from langchain.text_splitter import RecursiveCharacterTextSplitter
import time
from models import load_rules, process_rules_to_documents

FAISS_PATH = "data/faiss_index"
OLLAMA_API_URL = "http://ollama:11434"
MODEL_NAME = "llama3:latest"
EMBEDDING_NAME = "nomic-embed-text:latest"
DATABASE_PATH = "data/answers_cache.db"
JSON_FILE_PATH = "data/chatbot_data.json"
SIMILARITY_THRESHOLD = 0.90

embeddings = OllamaEmbeddings(model=EMBEDDING_NAME, base_url=OLLAMA_API_URL)


def check_health(delay=60):
    """
    Continuously checks the health of the API and the availability of the model and embedding.
    Retries indefinitely with a delay between retries until the system is healthy.
    """
    # Check API connection
    response = requests.get(f"{OLLAMA_API_URL}/")
    if response.status_code != 200:
        raise ValueError("API health check failed.")
    attempt = 0
    max_attempts = 65
    while attempt < max_attempts:
        attempt += 1
        try:
            # Check model availability
            models_response = requests.get(f"{OLLAMA_API_URL}/api/tags")
            if models_response.status_code != 200:
                raise ValueError("Failed to fetch models list.")

            models_data = models_response.json()
            available_models = [model['name'] for model in models_data['models']]
            log(f"Available models: {available_models}")

            # Check if the specified model is available
            if MODEL_NAME not in available_models:
                raise ValueError(f"Model '{MODEL_NAME}' is not available.")

            if EMBEDDING_NAME not in available_models:
                raise ValueError(f"Embedding '{EMBEDDING_NAME}' is not available.")
            # Log successful health check
            log("API and model are healthy.")
            return True
        except (requests.exceptions.RequestException, ValueError) as e:
            # Log failure and retry
            log(f"Health check attempt {attempt} failed: {e}")
            time.sleep(delay)


def log(message):
    """Logs a message with a timestamp to the logfile."""
    timestamp = datetime.now().strftime("%Y-%m-%d %H:%M:%S")
    with open('data/logfile.txt', 'a') as f:
        f.write(f"[{timestamp}] {message}\n")


def create_table():
    """Creates the 'answers' table if it doesn't exist."""
    conn = sqlite3.connect(DATABASE_PATH)
    cursor = conn.cursor()
    cursor.execute("""
        CREATE TABLE IF NOT EXISTS answers (
            id INTEGER PRIMARY KEY AUTOINCREMENT,
            question TEXT UNIQUE,
            answer TEXT,
            question_embedding BLOB
        )
    """)
    conn.commit()
    conn.close()


def load_data_from_json():
    """Loads the data from the JSON file."""
    try:
        with open(JSON_FILE_PATH, "r") as file:
            data = json.load(file)
        return [(item["question"], item["answer"]) for item in data]
    except FileNotFoundError:
        return f"Error: The file {JSON_FILE_PATH} was not found."
    except json.JSONDecodeError:
        return f"Error: The file {JSON_FILE_PATH} contains invalid JSON."


def populate_data():
    log("Populating the database with data from JSON...")
    data = load_data_from_json()
    if data:
        conn = sqlite3.connect(DATABASE_PATH)
        cursor = conn.cursor()

        for question, answer in data:
            question_embedding = embeddings.embed_query(question)
            question_embedding = np.array(question_embedding, dtype=np.float32).flatten()
            cursor.execute("INSERT OR IGNORE INTO answers (question, answer, question_embedding) VALUES (?, ?, ?)",
                           (question, answer, question_embedding.tobytes()))
        conn.commit()
        conn.close()
        log("Database population completed successfully.")
    else:
        log("No data found to populate the database.")


def initialize_database():
    """Initializes the SQLite database by creating the table and populating it with data."""
    # Log initialization start
    log("Initializing the database...")

    # Wait for health check to pass
    try:
        check_health()
    except RuntimeError as e:
        log(f"Database initialization aborted: {e}")
        raise RuntimeError(f"System initialization aborted: {e}")

    # Proceed only if the database does not exist
    if not os.path.exists(DATABASE_PATH):
        log("Database file not found. Creating database...")
        os.makedirs(os.path.dirname(DATABASE_PATH), exist_ok=True)
        create_table()
        populate_data()
        log("Database created and populated successfully.")
    else:
        log("Database already exists. Skipping creation and population.")


def get_cached_answer(query_text):
    query_embedding = embeddings.embed_query(query_text)
    query_embedding = np.array(query_embedding)

    with sqlite3.connect(DATABASE_PATH) as conn:
        cursor = conn.cursor()
        cursor.execute("SELECT answer, question_embedding FROM answers")
        results = cursor.fetchall()

    best_score = -1
    best_answer = None

    for answer, question_embedding_blob in results:
        stored_embedding = np.frombuffer(question_embedding_blob, dtype=np.float32).flatten()
        cosine_similarity = np.dot(query_embedding, stored_embedding) / (
                np.linalg.norm(query_embedding) * np.linalg.norm(stored_embedding))

        if cosine_similarity > best_score:
            best_score = cosine_similarity
            best_answer = answer

    if best_score >= SIMILARITY_THRESHOLD:
        return best_answer
    else:
        return None


def create_faiss_index():
    """Creates and returns a FAISS index for the rules."""
    if os.path.exists(FAISS_PATH):
        shutil.rmtree(FAISS_PATH)

    rules = load_rules()
    documents = process_rules_to_documents(rules)

    text_splitter = RecursiveCharacterTextSplitter(
        chunk_size=800,  # Slightly smaller chunks for better context matching
        chunk_overlap=200,  # Smaller overlap to reduce redundancy
        length_function=len
    )
    split_documents = text_splitter.split_documents(documents)

    db = FAISS.from_documents(split_documents, embeddings)
    db.save_local(FAISS_PATH)
    return db


def query_faiss_index(query_text):
    """Queries the FAISS index and retrieves relevant context."""
    if not os.path.exists(FAISS_PATH):
        raise FileNotFoundError("FAISS index not found. Please create it first.")

    db = FAISS.load_local(FAISS_PATH, embeddings, allow_dangerous_deserialization=True)
    results = db.similarity_search_with_score(query_text, k=10)  # Increased k for broader context

    if not results:
        raise ValueError("No relevant context found. Please refine your question or add more documents.")

    # Sort results by score and filter out low-quality matches
    sorted_results = sorted(results, key=lambda x: x[1], reverse=True)
    filtered_results = [doc for doc, score in sorted_results if score > 0.75]  # Adjust score threshold as needed

    if not filtered_results:
        raise ValueError("No relevant context found with sufficient confidence.")

    # Combine relevant documents into a single context
    context_text = "\n\n---\n\n".join(doc.page_content for doc in filtered_results)
    return context_text


def query_llama_model(query_text, context_text):
    """
    Queries the Llama model with the given context via HTTP to the Ollama container.
    """
    prompt = f"""
        You are a highly knowledgeable and approachable assistant with expertise in Texas Hold'em Poker. Follow these enhanced guidelines when responding to queries:

        - Provide concise, context-specific answers related to Texas Hold'em Poker.
        - For unrelated questions, inform the user politely and encourage Poker-related queries.
        - Respond in English by default, unless the query is in Dutch; in that case, respond in Dutch.
        - Maintain a professional and friendly tone.

        Context:
        {context_text}

        Question:
        {query_text}

        Answer:
    """

    data = {
        "model": MODEL_NAME,
        "prompt": prompt.strip(),
        "stream": False
    }

    try:
        response = requests.post(f"{OLLAMA_API_URL}/api/generate", json=data)
        response.raise_for_status()
        answer = response.json().get("response")
        if answer:
            return answer
    except requests.exceptions.RequestException as e:
        raise ValueError(f"Error while querying Ollama: {str(e)}")
