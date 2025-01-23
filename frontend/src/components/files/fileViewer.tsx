import { useState } from "react";

const FileViewer = () => {
    const [folder, setFolder] = useState("");
    const [file, setFile] = useState("");
    const [imageSrc, setImageSrc] = useState("");
    const [error, setError] = useState("");

    const handleFetchFile = () => {
        fetch(`http://localhost:8081/api/files/download?folder=${folder}&file=${file}`, {
            method: "GET",
        })
            .then((response) => {
                if (!response.ok) {
                    throw new Error("Failed to fetch file");
                }
                return response.blob();
            })
            .then((blob) => {
                const url = URL.createObjectURL(blob);
                setImageSrc(url);
            })
            .catch((error) => setError(error.message));
    };

    return (
        <div>
            <h1>File Viewer</h1>
            {error && <p>Error: {error}</p>}
            <div>
                <label>
                    Folder:
                    <input
                        type="text"
                        value={folder}
                        onChange={(e) => setFolder(e.target.value)}
                        placeholder="Enter folder name (e.g., playingcards-club)"
                    />
                </label>
                <br />
                <label>
                    File:
                    <input
                        type="text"
                        value={file}
                        onChange={(e) => setFile(e.target.value)}
                        placeholder="Enter file name (e.g., 1.png)"
                    />
                </label>
                <br />
                <button onClick={handleFetchFile}>Fetch File</button>
            </div>
            {imageSrc && (
                <div>
                    <h2>File Preview:</h2>
                    <img src={imageSrc} alt="Fetched File" style={{ maxWidth: "100%" }} />
                </div>
            )}
        </div>
    );
};

export default FileViewer;
