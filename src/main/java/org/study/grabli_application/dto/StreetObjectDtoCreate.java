package org.study.grabli_application.dto;

import lombok.Data;

@Data
public class StreetObjectDtoCreate {
    private Integer typeId;
    private double[] coordinates;
    private String title;
    private String description;
    private String creatorName;
    private String creatorContact;
}
