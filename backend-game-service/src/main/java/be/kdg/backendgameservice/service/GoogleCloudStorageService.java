package be.kdg.backendgameservice.service;

import com.google.cloud.storage.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class GoogleCloudStorageService {

    private final Storage storage;


    public boolean isBucketAccessible(String bucketName) {
        Bucket bucket = storage.get(bucketName);
        return bucket != null;
    }

    public void uploadFile(String bucketName, String objectName, Path filePath) throws IOException {
        Bucket bucket = storage.get(bucketName);
        if (bucket == null) {
            throw new IllegalArgumentException("Bucket with name " + bucketName + " does not exist or is not accessible.");
        }
        byte[] bytes = Files.readAllBytes(filePath);
        bucket.create(objectName, bytes);
    }


    public byte[] downloadFile(String bucketName, String objectName) {
        Blob blob = storage.get(bucketName, objectName);
        return blob.getContent();
    }

    public void deleteFile(String bucketName, String objectName) {
        Blob blob = storage.get(bucketName, objectName);
        blob.delete();
    }

    public Blob getFile(String bucketName, String filePath) {
        Bucket bucket = storage.get(bucketName);
        if (bucket == null) {
            throw new IllegalArgumentException("Bucket not found: " + bucketName);
        }
        Blob blob = bucket.get(filePath);
        if (blob == null || !blob.exists()) {
            throw new IllegalArgumentException("File not found: " + filePath);
        }
        return blob;
    }


    public boolean folderExists(String bucketName, String folderPath) {
        Storage storage = StorageOptions.getDefaultInstance().getService();
        Blob blob = storage.get(bucketName, folderPath); // GCS treats folder names as blobs
        return blob != null && blob.isDirectory();
    }

    public void createFolder(String bucketName, String folderPath) {
        if (!folderPath.endsWith("/")) {
            folderPath += "/";
        }

        Storage storage = StorageOptions.getDefaultInstance().getService();
        BlobInfo folderBlobInfo = BlobInfo.newBuilder(bucketName, folderPath).build();

        // Upload an empty blob to create the folder
        storage.create(folderBlobInfo);
    }

    public String uploadFileWithUniqueName(String bucketName, String folder, Path filePath) throws IOException {
        Bucket bucket = storage.get(bucketName);
        if (bucket == null) {
            throw new IllegalArgumentException("Bucket with name " + bucketName + " does not exist or is not accessible.");
        }

        String originalFileName = filePath.getFileName().toString();
        String uniqueFileName = folder + "/" + System.currentTimeMillis() + "_" + originalFileName;

        byte[] bytes = Files.readAllBytes(filePath);
        bucket.create(uniqueFileName, bytes);

        return uniqueFileName;
    }

    public Blob findFileByName(String bucketName, String folder, String fileName) {
        String fullPath = folder + "/" + fileName;
        Blob blob = storage.get(bucketName, fullPath);

        if (blob == null || !blob.exists()) {
            throw new IllegalArgumentException("File not found: " + fullPath);
        }
        return blob;
    }

    public String generateV4SignedReadUrl(String bucketName, String objectName) {
        try {
            BlobId blobId = BlobId.of(bucketName, objectName);
            BlobInfo blobInfo = BlobInfo.newBuilder(blobId).build();

            URL url = storage.signUrl(
                    blobInfo,
                    1, TimeUnit.HOURS,
                    Storage.SignUrlOption.httpMethod(HttpMethod.GET),
                    Storage.SignUrlOption.withV4Signature()
            );
            return url.toString();
        } catch (Exception e) {
            throw new RuntimeException("Error generating V4 signed URL for file: " + objectName, e);
        }
    }
}

