package Capstone.Aeroponics.services;

import Capstone.Aeroponics.utils.ImageUtils;
import Capstone.Aeroponics.utils.MessageUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.nio.file.Files;
import java.nio.file.Path;

@Service
@Slf4j
public class FileService {

    private static final String FILES = "Files";

    private static final String GITIGNORE = ".gitignore";

    @Autowired
    private ImageUtils imageUtils;

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