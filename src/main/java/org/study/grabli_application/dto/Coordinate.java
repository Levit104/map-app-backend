package org.study.grabli_application.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Coordinate {
    private String type;
    private String[] coordinates;
}
