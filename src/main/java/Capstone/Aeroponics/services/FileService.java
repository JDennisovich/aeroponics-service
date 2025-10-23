package Capstone.Aeroponics.services;

import Capstone.Aeroponics.exception.ServiceException;
import Capstone.Aeroponics.utils.ImageUtils;
import Capstone.Aeroponics.utils.MessageUtils;
import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Map;

@Service
@Slf4j
public class FileService {

    private static final String FILES = "Files";

    private static final String GITIGNORE = ".gitignore";

    @Value("${file.image.directory}")
    private String imageDirectory;

    @Value("${file.image.url}")
    private String imageBaseUrl;

    @Autowired
    private ImageUtils imageUtils;

    // Optional: configure a folder inside Cloudinary to keep your uploads organized
    @Value("${cloudinary.upload_folder:}")
    private String cloudinaryUploadFolder;

    // Cloudinary client (injected from CloudinaryConfig)
    private final Cloudinary cloudinary;

    @Autowired
    public FileService(Cloudinary cloudinary, ImageUtils imageUtils) {
        this.cloudinary = cloudinary;
        this.imageUtils = imageUtils;
    }

    /**
     * Upload file and return public URL.
     *
     * @param file the multipart file to upload
     * @return the public URL of the uploaded file
     */
    public String uploadFile(MultipartFile file) {
        try {
            if (file == null || file.isEmpty()) {
                String errorMessage = "No file provided for upload";
                log.error(errorMessage);
                throw new ServiceException(errorMessage);
            }

            // -------- Cloudinary upload starts here --------
            // Prepare upload options. You can add transformations, public_id, etc.
            Map<String, Object> uploadOptions = ObjectUtils.asMap();
            if (cloudinaryUploadFolder != null && !cloudinaryUploadFolder.isEmpty()) {
                uploadOptions.put("folder", cloudinaryUploadFolder);
            }

            // Upload file bytes to Cloudinary
            @SuppressWarnings("unchecked")
            Map<String, Object> uploadResult = cloudinary.uploader().upload(file.getBytes(), uploadOptions);

            Object secureUrl = uploadResult.get("secure_url");
            if (secureUrl != null) {
                String fileUrl = secureUrl.toString();
                log.info("File uploaded to Cloudinary successfully: {}", fileUrl);
                return fileUrl;
            } else {
                String msg = "Cloudinary upload succeeded but secure_url not returned";
                log.error(msg);
                throw new ServiceException(msg);
            }
            // -------- Cloudinary upload ends here --------

            // NOTE: The following local-save logic is intentionally removed in favor of Cloudinary.
            // If you need to keep a local copy, uncomment and adapt the code below.
            // Path uploadPath = Paths.get(imageDirectory);
            // if (!Files.exists(uploadPath)) { Files.createDirectories(uploadPath); }
            // String fileName = file.getOriginalFilename();
            // Path destination = uploadPath.resolve(fileName);
            // Files.copy(file.getInputStream(), destination, StandardCopyOption.REPLACE_EXISTING);
            // String fileUrl = imageBaseUrl + fileName;
            // return fileUrl;

        } catch (ServiceException e) {
            throw e;
        } catch (IOException e) {
            String errorMessage = "File upload failed due to I/O error";
            log.error(errorMessage, e);
            throw new ServiceException(errorMessage, e);
        } catch (Exception e) {
            String errorMessage = "Unexpected error occurred while uploading file";
            log.error(errorMessage, e);
            throw new ServiceException(errorMessage, e);
        }
    }

    /**
     * Delete file from the file system or Cloudinary.
     *
     * @param fileUrl the URL of the file to delete (can be full Cloudinary URL or stored public_id)
     */
    public void deleteFile(String fileUrl) {
        try {
            if (fileUrl == null || fileUrl.isEmpty()) {
                log.warn("No file URL provided for deletion");
                return;
            }

            // If the stored value is a full Cloudinary URL, extract public_id
            String publicId = fileUrl;
            if (fileUrl.startsWith("http")) {
                publicId = extractPublicIdFromUrl(fileUrl);
            } else if (imageBaseUrl != null && !imageBaseUrl.isEmpty() && fileUrl.startsWith(imageBaseUrl)) {
                // If you previously stored imageBaseUrl + filename, we attempt to derive a publicId.
                String fileName = fileUrl.replace(imageBaseUrl, "");
                int dot = fileName.lastIndexOf('.');
                if (dot > 0) {
                    publicId = fileName.substring(0, dot);
                } else {
                    publicId = fileName;
                }
            }

            if (publicId == null || publicId.isEmpty()) {
                log.warn("Could not determine public_id for deletion; skipping");
                return;
            }

            // Destroy resource on Cloudinary. If you stored the public_id in DB, use it directly.
            Map<?, ?> result = cloudinary.uploader().destroy(publicId, ObjectUtils.emptyMap());
            log.info("Cloudinary destroy result for {}: {}", publicId, result);

            // If you still keep local files, optionally delete local copy here. (Not doing that by default.)

        } catch (IOException e) {
            String errorMessage = "Failed to delete file due to I/O error";
            log.error(errorMessage, e);
            throw new ServiceException(errorMessage, e);
        } catch (Exception e) {
            String errorMessage = "Unexpected error occurred while deleting file";
            log.error(errorMessage, e);
            throw new ServiceException(errorMessage, e);
        }
    }

    public Path getImage(String imageName) {
        try {
            log.info(MessageUtils.retrieveSuccessMessage(imageName));
            return imageUtils.getFilePath(imageName);
        } catch (Exception e) {
            log.error(MessageUtils.RETRIEVE_EXCEPTION_MESSAGE, e);
            return null;
        }
    }

    /**
     * Get image byte byte [ ] from a local Path or remote URL if needed.
     * If you migrate fully to Cloudinary, consider returning URLs to clients instead of bytes.
     */
    public byte[] getImageByte(Path filePath) {
        try {
            log.info(MessageUtils.retrieveSuccessMessage(filePath.getFileName().toString()));
            return imageUtils.toByteArray(filePath);
        } catch (Exception e) {
            log.error(MessageUtils.RETRIEVE_EXCEPTION_MESSAGE, e);
            return null;
        }
    }

    /**
     * Gets image content type.
     */
    public String getImageContentType(Path filePath) {
        try {
            return Files.probeContentType(filePath);
        } catch (Exception e) {
            log.error(MessageUtils.RETRIEVE_EXCEPTION_MESSAGE, e);
            return null;
        }
    }

    /**
     * Extract Cloudinary public_id from a Cloudinary URL.
     * Example URL: https://res.cloudinary.com/<cloud_name>/image/upload/v12345/folder/filename.jpg
     * Returns: folder/filename
     */
    private String extractPublicIdFromUrl(String url) {
        try {
            String cleanPath = new URI(url).getPath();
            int idx = cleanPath.indexOf("/upload/");
            if (idx < 0) {
                return null;
            }
            String afterUpload = cleanPath.substring(idx + "/upload/".length());
            // Remove version like v12345/
            if (afterUpload.startsWith("v") && afterUpload.contains("/")) {
                int slashAfterVersion = afterUpload.indexOf('/');
                afterUpload = afterUpload.substring(slashAfterVersion + 1);
            }
            int dot = afterUpload.lastIndexOf('.');
            if (dot > 0) {
                return afterUpload.substring(0, dot);
            }
            return afterUpload;
        } catch (URISyntaxException e) {
            log.warn("Failed to parse URL when extracting public id: {}", url, e);
            return null;
        }
    }

    /**
     * Helper: fetch image bytes by URL (if some controllers expect to return bytes).
     * Consider returning Cloudinary URLs to clients instead of proxying bytes through your server.
     */
    public byte[] getImageBytesFromUrl(String imageUrl) {
        try {
            RestTemplate rest = new RestTemplate();
            return rest.getForObject(imageUrl, byte[].class);
        } catch (Exception e) {
            log.error("Failed to fetch image bytes from URL: {}", imageUrl, e);
            return null;
        }
    }
}