package org.study.grabli_application.dto;

import lombok.Data;

@Data
public class NewStreetObjectDto {
    private Long idStreetObjectType;
    private Coordinate coordinate;
    private String comment;
}
