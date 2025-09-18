package Capstone.Aeroponics.utils;

import Capstone.Aeroponics.models.request.ImageDataRO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Component
public class ImageUtils {
    private static final String SLASH = "/";

    private static final String DASH = "-";

    @Value("${file.home.directory}")
    private String USER_DIRECTORY;

    @Value("${file.image.directory}")
    private String IMAGE_DIRECTORY;

    @Value("${file.image.url}")
    private String IMAGE_FILE_URL;

    /**
     * Get file byte [ ].
     *
     * @param filePath the file path
     * @return the byte [ ]
     * @throws IOException the io exception
     */
    public byte[] getFile(String filePath) throws IOException {
        return Files.readAllBytes(new File(filePath).toPath());
    }

    /**
     * To byte array byte [ ].
     *
     * @param file the file
     * @return the byte [ ]
     * @throws IOException the io exception
     */
    public byte[] toByteArray(Path file) throws IOException {
        String filePath = buildFilePath(file.getFileName().toString());
        return Files.readAllBytes(new File(filePath).toPath());
    }

    /**
     * Gets file path.
     *
     * @param filePath the file path
     * @return the file path
     */
    public Path getFilePath(String filePath) {
        return new File(filePath).toPath();
    }

    /**
     * Save image string.
     *
     * @param file   the file
     * @param entity the entity
     * @return the string
     * @throws IOException the io exception
     */
    public String saveImage(MultipartFile file, String entity) throws IOException {
        StringBuilder imageFolderPath = buildImageFolderPath();
        String fileName = buildFileName(file.getOriginalFilename(), entity);
        String fileUrl = buildFileUrl(fileName);

        imageFolderPath.append(fileName);
        file.transferTo(new File(imageFolderPath.toString()));

        return fileUrl;
    }

    /**
     * Save image image data ro.
     *
     * @param file            the file
     * @param imageFolderPath the image folder path
     * @param fileName        the file name
     * @param order           the order
     * @return the image data ro
     * @throws IOException the io exception
     */
    public ImageDataRO saveImage(MultipartFile file, String imageFolderPath, String fileName, Integer order) throws IOException {
        String filePath = imageFolderPath + fileName;
        String fileUrl = buildFileUrl(fileName);

        file.transferTo(new File(filePath));

        return ImageDataRO.builder()
                .fileName(file.getOriginalFilename())
                .value(fileUrl)
                .order(order)
                .build();
    }

    public ImageDataRO saveSingleImage(MultipartFile file, String entity) throws IOException {
        StringBuilder imageFolderPath = buildImageFolderPath();
        String fileName = buildFileName(file.getOriginalFilename(), entity);

        return saveImage(file, imageFolderPath.toString(), fileName, 1);
    }

    /**
     * Save images list.
     *
     * @param files           the files
     * @param imageReferences the image references
     * @param entity          the entity
     * @return the list
     * @throws IOException the io exception
     */
    public List<ImageDataRO> saveImages(List<MultipartFile> files, List<ImageDataRO> imageReferences, String entity) throws IOException {
        List<ImageDataRO> images = new ArrayList<>();

        if (files.isEmpty()) {
            return Collections.emptyList();
        }

        for (MultipartFile file : files) {

            StringBuilder imageFolderPath = buildImageFolderPath();
            String fileName = buildFileName(file.getOriginalFilename(), entity);
            Integer order = getImageOrder(imageReferences, file.getOriginalFilename());

            images.add(saveImage(file, imageFolderPath.toString(), fileName, order));
        }

        return images;
    }

    /**
     * Save nested images list.
     *
     * @param files           the files
     * @param imageReferences the image references
     * @param entity          the entity
     * @return the list
     * @throws IOException the io exception
     */
    public List<ImageDataRO> saveNestedImages(List<MultipartFile> files, List<ImageDataRO> imageReferences, String entity) throws IOException {
        List<ImageDataRO> images = new ArrayList<>();

        if (files.isEmpty()) {
            return Collections.emptyList();
        }

        for (MultipartFile file : files) {

            List<ImageDataRO> imageReferenceList = imageReferences.stream()
                    .filter(image -> image.getValue().equals(file.getOriginalFilename())).toList();

            if (imageReferenceList.isEmpty()) {
                continue;
            }

            StringBuilder imageFolderPath = buildImageFolderPath();
            String fileName = buildFileName(file.getOriginalFilename(), entity);
            Integer order = getImageOrder(imageReferences, file.getOriginalFilename());

            images.add(saveImage(file, imageFolderPath.toString(), fileName, order));
        }

        return images;
    }

    private Integer getImageOrder(List<ImageDataRO> imageReferences, String originalFileName) {
        return imageReferences.stream()
                .filter(image -> image.getValue().equals(originalFileName))
                .map(ImageDataRO::getOrder)
                .findFirst()
                .orElse(null);
    }

    /**
     * Build image folder path string builder.
     *
     * @return the string builder
     */
    public StringBuilder buildImageFolderPath() {
        return new StringBuilder(IMAGE_DIRECTORY);
    }

    private String buildFileUrl(String fileName) {
        return IMAGE_FILE_URL + fileName;
    }

    private String buildFileName(String originalFileName, String entity) {
        return DateUtils.dateToYYYYMMDD(DateUtils.now()) + DASH + entity + DASH + originalFileName;
    }

    private String buildFilePath(String fileName) {
        return buildImageFolderPath()
                .append(fileName)
                .toString();
    }
}
