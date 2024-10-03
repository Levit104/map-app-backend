package org.study.grabli_application.service.impl;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.PathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.study.grabli_application.exceptions.ImageNotFoundException;
import org.study.grabli_application.exceptions.ImageStorageException;
import org.study.grabli_application.exceptions.ImageUploadException;
import org.study.grabli_application.service.ImageService;
import org.study.grabli_application.util.ImageContainer;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.UUID;

@Service
public class ImageServiceImpl implements ImageService {
    private final Path uploadPath;

    public ImageServiceImpl(@Value("${application.image-upload-path}") String imageUploadPath) throws IOException {
        uploadPath = Path.of(imageUploadPath);
        // org.springframework.util.FileSystemUtils.deleteRecursively(uploadPath);
        createUploadDirectory();
    }

    @Override
    public String save(MultipartFile file) {
        validate(file);
        String fileName = generateUniqueName(file);
        Path path = getFilePath(fileName);
        writeToFileSystem(path, file);
        return fileName;
    }

    @Override
    public ImageContainer load(String fileName) {
        Path path = getFilePath(fileName);
        checkExistence(path);
        Resource resource = createResource(path);
        String contentType = getContentType(path);
        return new ImageContainer(contentType, resource);
    }

    @Override
    public void delete(String fileName) {
        Path path = getFilePath(fileName);
        deleteFromFileSystem(path);
    }

    private void validate(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new ImageUploadException("Пустой файл");
        }

        if (file.getContentType() == null || !file.getContentType().startsWith("image/")) {
            throw new ImageUploadException("Файл не является изображением");
        }
    }

    private String generateUniqueName(MultipartFile file) {
        return UUID.randomUUID() + "." + StringUtils.getFilenameExtension(file.getOriginalFilename());
    }

    private Path getFilePath(String fileName) {
        return uploadPath.resolve(fileName);
    }

    private void createUploadDirectory() throws IOException {
        Files.createDirectories(uploadPath);
    }

    private void writeToFileSystem(Path path, MultipartFile file) {
        try {
            Files.write(path, file.getBytes());
        } catch (IOException e) {
            throw new ImageStorageException("Ошибка при сохранении файла " + path.getFileName());
        }
    }

    private void deleteFromFileSystem(Path path) {
        try {
            Files.deleteIfExists(path);
        } catch (IOException e) {
            throw new ImageStorageException("Ошибка при удалении файла " + path.getFileName());
        }
    }

    private void checkExistence(Path path) {
        if (Files.notExists(path)) {
            throw new ImageNotFoundException("Файл " + path.getFileName() + " не найден");
        }
    }

    private String getContentType(Path path) {
        try {
            return Files.probeContentType(path);
        } catch (IOException e) {
            throw new ImageStorageException("Ошибка при загрузке типа файла " + path.getFileName());
        }
    }

    private Resource createResource(Path path) {
        return new PathResource(path);
    }
}
