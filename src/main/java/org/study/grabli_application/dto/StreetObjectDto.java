package org.study.grabli_application.dto;

import lombok.Data;

@Data
public class StreetObjectDto {
    private Long id;
    private StreetObjectTypeDto type;
    private double[] coordinates;
    private String title;
    private String description;
    private String image;
    private String creatorName;
    private String creatorContact;
    private Boolean approved;
}
