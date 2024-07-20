package org.study.grabli_application.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ObjectTypeDto {

    private Long id;

    private String ObjectName;

    private String tag;
}
