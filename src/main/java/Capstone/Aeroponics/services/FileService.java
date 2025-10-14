package Capstone.Aeroponics.services;

import Capstone.Aeroponics.exception.ServiceException;
import Capstone.Aeroponics.utils.ImageUtils;
import Capstone.Aeroponics.utils.MessageUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

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

            // Ensure directory exists
            Path uploadPath = Paths.get(imageDirectory);
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
                log.info("Created upload directory at: {}", uploadPath);
            }

            // Get original filename
            String fileName = file.getOriginalFilename();
            Path destination = uploadPath.resolve(fileName);

            // Save file
            Files.copy(file.getInputStream(), destination, StandardCopyOption.REPLACE_EXISTING);

            // Build public URL using your configured base URL
            String fileUrl = imageBaseUrl + fileName;

            log.info("File uploaded successfully: {}", fileUrl);
            return fileUrl;

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
     * Delete file from the file system.
     *
     * @param fileUrl the URL of the file to delete
     */
    public void deleteFile(String fileUrl) {
        try {
            if (fileUrl == null || fileUrl.isEmpty()) {
                log.warn("No file URL provided for deletion");
                return;
            }

            // Extract filename from URL
            String fileName = fileUrl.replace(imageBaseUrl, "");
            Path filePath = Paths.get(imageDirectory, fileName);

            // Delete file if it exists
            if (Files.exists(filePath)) {
                Files.delete(filePath);
                log.info("File deleted successfully: {}", fileName);
            } else {
                log.warn("File not found for deletion: {}", fileName);
            }

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
     * Get image byte byte [ ].
     *
     * @param filePath the file path
     * @return the byte [ ]
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
     *
     * @param filePath the file path
     * @return the image content type
     */
    public String getImageContentType(Path filePath) {
        try {
            return Files.probeContentType(filePath);
        } catch (Exception e) {
            log.error(MessageUtils.RETRIEVE_EXCEPTION_MESSAGE, e);
            return null;
        }
    }
}