package org.study.grabli_application.dto;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class StreetObjectDtoCreate {
    private Integer typeId;
    private String coordinates; // JSON-строка
    private String title;
    private String description;
    private MultipartFile image;
    private String creatorName;
    private String creatorContact;
}
