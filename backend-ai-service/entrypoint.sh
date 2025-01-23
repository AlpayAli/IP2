#!/bin/sh

# Start Ollama serve in the background
/bin/ollama serve &
OLLAMA_PID=$!

# Wait for Ollama to be ready using curl
echo "Waiting for Ollama to be ready..."
until curl -s http://localhost:11434 > /dev/null; do
    sleep 1
done

 #Check if the desired model is available
if /bin/ollama list | grep -q "${MODEL_NAME}"; then
    echo "Model ${MODEL_NAME} already exists."
else
    echo "No models found. Pulling model ${MODEL_NAME}..."
    /bin/ollama pull "${MODEL_NAME}"
fi

# Check if the embedding model is available
if /bin/ollama list | grep -q "${EMBED_NAME}"; then
    echo "Embedding model ${EMBED_NAME} already exists."
else
    echo "Embedding model ${EMBED_NAME} not found. Pulling model ${EMBED_NAME}..."
    /bin/ollama pull "${EMBED_NAME}"
fi

# Keep Ollama running
wait ${OLLAMA_PID}
