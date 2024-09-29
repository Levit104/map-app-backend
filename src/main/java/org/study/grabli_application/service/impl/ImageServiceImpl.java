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
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.UUID;

@Service
public class ImageServiceImpl implements ImageService {
    private final Path uploadPath;

    public ImageServiceImpl(@Value("${application.image-upload-path}") String imageUploadPath) throws IOException {
        uploadPath = Path.of(imageUploadPath);
        // org.springframework.util.FileSystemUtils.deleteRecursively(uploadPath);
        Files.createDirectories(uploadPath);
    }

    @Override
    public String save(MultipartFile file) {
        try {
            validate(file);
            String newFileName = generateUniqueName(file);
            Path newFilePath = uploadPath.resolve(newFileName);
            Files.write(newFilePath, file.getBytes());
            return newFileName;
        } catch (IOException e) {
            throw new ImageStorageException("Ошибка при сохранении файла");
        }
    }

    @Override
    public ImageContainer load(String fileName) {
        try {
            Path path = uploadPath.resolve(fileName);

            if (Files.notExists(path)) {
                throw new ImageNotFoundException("Файл " + fileName + " не найден");
            }

            Resource resource = new PathResource(path);
            String contentType = Files.probeContentType(path);

            return new ImageContainer(contentType, resource);
        } catch (MalformedURLException e) {
            throw new ImageStorageException("Ошибка при загрузке файла " + fileName);
        } catch (IOException e) {
            throw new ImageStorageException("Ошибка при загрузке типа файла " + fileName);
        }
    }

    @Override
    public void delete(String fileName) {
        try {
            Path path = uploadPath.resolve(fileName);
            Files.deleteIfExists(path);
        } catch (IOException e) {
            throw new ImageStorageException("Ошибка при удалении файла " + fileName);
        }
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
}
