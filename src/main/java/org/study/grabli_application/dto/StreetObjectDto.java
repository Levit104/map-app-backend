package org.study.grabli_application.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@JsonInclude(Include.NON_NULL)
public class StreetObjectDto {

    private Long id;

    private Long idCreator;

    private Long idStreetObjectType;

    private Coordinate coordinate;

    private String comment;
}
