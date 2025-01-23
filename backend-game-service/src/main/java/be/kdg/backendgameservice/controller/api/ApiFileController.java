package be.kdg.backendgameservice.controller.api;

import be.kdg.backendgameservice.service.GoogleCloudStorageService;
import com.google.cloud.storage.Blob;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.nio.file.Files;
import java.nio.file.Path;

@RestController
@RequestMapping("/api/files")
@RequiredArgsConstructor
public class ApiFileController {

    private static final Logger logger = LoggerFactory.getLogger(ApiFileController.class);

    private final GoogleCloudStorageService googleCloudStorageService;

    /**
     * Test bucket accessibility.
     */
    @PostMapping("/test-bucket")
    public ResponseEntity<String> testBucket() {
        try {
            String bucketName = "image_bucket_ip2";
            if (googleCloudStorageService.isBucketAccessible(bucketName)) {
                return ResponseEntity.ok("Bucket is accessible: " + bucketName);
            } else {
                return ResponseEntity.status(404).body("Bucket not found: " + bucketName);
            }
        } catch (Exception e) {
            logger.error("Error accessing bucket", e);
            return ResponseEntity.status(403).body("Access denied: " + e.getMessage());
        }
    }

    /**
     * Upload a file to the bucket.
     */
    @PostMapping("/upload")
    public ResponseEntity<String> uploadFile(
            @RequestParam("file") MultipartFile file,
            @RequestParam("folder") String folder) {
        String bucketName = "image_bucket_ip2";
        try {
            Path tempFile = Files.createTempFile("upload-", file.getOriginalFilename());
            file.transferTo(tempFile.toFile());
            String uniqueFileName = googleCloudStorageService.uploadFileWithUniqueName(bucketName, folder, tempFile);
            Files.deleteIfExists(tempFile);
            return ResponseEntity.ok("File uploaded successfully with unique name: " + uniqueFileName);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("File upload failed: " + e.getMessage());
        }
    }


    /**
     * Generate a V4 signed URL for a file.
     */
    @GetMapping("/generate-signed-url")
    public ResponseEntity<String> generateV4SignedUrl(
            @RequestParam("folder") String folder,
            @RequestParam("file") String file) {
        try {
            String bucketName = "image_bucket_ip2";
            String filePath = folder + "/" + file;
            String signedUrl = googleCloudStorageService.generateV4SignedReadUrl(bucketName, filePath);
            logger.info("Generated V4 signed URL for file '{}': {}", filePath, signedUrl);
            return ResponseEntity.ok(signedUrl);
        } catch (Exception e) {
            logger.error("Error generating signed URL", e);
            return ResponseEntity.status(500).body("Error generating signed URL: " + e.getMessage());
        }
    }

    @GetMapping("/download")
    public ResponseEntity<InputStreamResource> downloadFile(
            @RequestParam("folder") String folder,
            @RequestParam("file") String file) {
        try {
            String bucketName = "image_bucket_ip2";
            String filePath = folder + "/" + file;

            // Ophalen van het bestand
            Blob blob = googleCloudStorageService.getFile(bucketName, filePath);

            // Zet het bestand om naar een InputStreamResource
            InputStreamResource resource = new InputStreamResource(new ByteArrayInputStream(blob.getContent()));
            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file + "\"")
                    .body(resource);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(404).body(null);
        } catch (Exception e) {
            logger.error("Error downloading file", e);
            return ResponseEntity.status(500).body(null);
        }
    }
}
