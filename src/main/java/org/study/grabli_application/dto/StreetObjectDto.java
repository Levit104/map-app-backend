package org.study.grabli_application.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class StreetObjectDto {
    private Long id;
    private Integer typeId;
    private Coordinate coordinate;
    private String title;
    private String description;
    private String image;
    private String creatorName;
    private String creatorContact;
    private Boolean approved;

    public StreetObjectDto(Long id) {
        this.id = id;
    }
}
