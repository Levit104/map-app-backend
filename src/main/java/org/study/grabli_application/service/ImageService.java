package org.study.grabli_application.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.study.grabli_application.exceptions.ImageNotFoundException;
import org.study.grabli_application.exceptions.ImageStorageException;
import org.study.grabli_application.exceptions.ImageUploadException;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.UUID;

@Service
public class ImageService {
    private final Path uploadPath;

    public ImageService(@Value("${application.image-upload-path}") String imageUploadPath) throws IOException {
        uploadPath = Path.of(imageUploadPath); // TODO .toAbsolutePath().normalize() ???
        Files.createDirectories(uploadPath);
    }

    public String save(MultipartFile file) {
        try {
            validate(file);
            String newFileName = generateUniqueName(file);
            Path newFilePath = uploadPath.resolve(newFileName);
            Files.write(newFilePath, file.getBytes());
            return newFilePath.toString();
        } catch (IOException e) {
            throw new ImageStorageException("Ошибка при сохранении файла");
        }
    }

    // TODO делать запрос к БД? "select s from StreetObject s where s.image LIKE %:image%"
    public Resource load(String fileName) {
        try {
            Path path = uploadPath.resolve(fileName);

            if (!Files.exists(path)) {
                throw new ImageNotFoundException("Файл " + fileName + " не найден");
            }

            return new UrlResource(path.toUri());
        } catch (MalformedURLException e) {
            throw new ImageStorageException("Ошибка при загрузке файла " + fileName);
        }
    }

    public String getFileName(String filePath) {
        return Path.of(filePath).getFileName().toString();
    }

    public String getContentType(String fileName) {
        try {
            return Files.probeContentType(uploadPath.resolve(fileName));
        } catch (IOException e) {
            throw new ImageStorageException("Ошибка при загрузке типа файла");
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
