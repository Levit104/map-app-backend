package org.study.grabli_application.dto;

import lombok.Data;

@Data
public class StreetObjectDtoCreate {
    private Integer typeId;
    private String coordinates;
    // private Coordinates coordinates;
    private String title;
    private String description;
    private String image; // TODO MultipartFile???
    private String creatorName;
    private String creatorContact;
}
