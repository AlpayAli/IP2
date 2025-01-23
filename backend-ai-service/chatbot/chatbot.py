import os
import shutil
import argparse
from langchain_community.vectorstores import FAISS
from langchain_ollama import OllamaEmbeddings
from langchain_ollama.llms import OllamaLLM
from langchain_core.prompts import ChatPromptTemplate
from langchain.text_splitter import RecursiveCharacterTextSplitter
from langchain.schema import Document
import timeit

# Define paths
FAISS_PATH = "chatbot/data/faiss_index"
DOCUMENTS_PATH = "chatbot/data"
embeddings = OllamaEmbeddings(model="llama2")

def split_text():
    """Splits documents into chunks."""
    text_splitter = RecursiveCharacterTextSplitter(
        chunk_size=1000,
        chunk_overlap=500,  # For more context
        length_function=len
    )
    return text_splitter.split_documents(load_document())

def load_document():
    """Loads all JSON documents from the specified directory."""
    documents = []
    for filename in os.listdir(DOCUMENTS_PATH):
        if filename.endswith(".json"):
            file_path = os.path.join(DOCUMENTS_PATH, filename)
            with open(file_path, "r", encoding="utf-8") as file:
                doc = file.read()
                documents.append(Document(page_content=doc))
    return documents

def save_faiss():
    """Creates and saves a FAISS index."""
    if os.path.exists(FAISS_PATH):
        shutil.rmtree(FAISS_PATH)

    documents = split_text()
    if not documents:
        raise ValueError("No documents to index.")

    # Create and save FAISS index
    db = FAISS.from_documents(documents, embeddings)
    db.save_local(FAISS_PATH)
    return db

def load_faiss():
    """Loads an existing FAISS index."""
    return FAISS.load_local(FAISS_PATH, embeddings, allow_dangerous_deserialization=True)

def main():
    parser = argparse.ArgumentParser(description="Ollama Chatbot")
    parser.add_argument("query_text", type=str, help="Query text")
    parser.add_argument("--create-index", action="store_true", help="Force create new FAISS index")
    args = parser.parse_args()

    query_text = args.query_text

    # If the --create-index flag is provided, create a new index, otherwise load the existing one
    if args.create_index:
        print("Creating a new FAISS index...")
        db = save_faiss()
    else:
        try:
            db = load_faiss()
        except FileNotFoundError:
            print("No FAISS index found. Creating a new index...")
            db = save_faiss()

    # Perform similarity search
    results = db.similarity_search_with_score(query_text, k=4)

    if not results:
        print("No relevant context found. Please refine your question or add more documents.")
        return

    # Prepare context
    context_text = "\n\n---\n\n".join([doc.page_content for doc, _score in results])

    # Initialize model
    model = OllamaLLM(model="llama2")

    # Modify the prompt template to directly answer the question
    chat_prompt_template = ChatPromptTemplate.from_template("""
    You are a helpful chatbot specializing in Texas Hold'em Poker. 
    Respond appropriately based on the following rules:
    - If the question is not about Poker, provide a relevant answer that says something like "Please ask a question about Poker." while staying helpful and polite.
    - If the question is in a language you recognize, respond in that language.
    - If the question is in an unfamiliar language, respond in English and include a list of languages you support.
    - Answer only in Text.
    - Answer concisely and to the point. 
    
    Context:
    {context}

    Question: {question}

    Answer:
    """)

    prompt = chat_prompt_template.format(
        context=context_text,
        question=query_text
    )

    # Get response from the model
    response_text = model.invoke(prompt).strip()

    # Print response
    print(response_text)

if __name__ == "__main__":
    start_time = timeit.default_timer()
    main()
    end_time = timeit.default_timer()
    print(end_time-start_time)
