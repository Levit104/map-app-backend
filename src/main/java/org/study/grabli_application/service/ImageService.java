package org.study.grabli_application.service;

import org.springframework.web.multipart.MultipartFile;
import org.study.grabli_application.util.ImageContainer;

public interface ImageService {
    String save(MultipartFile file);

    ImageContainer load(String fileName);

    void delete(String fileName);
}
