package org.study.grabli_application.dto;

import lombok.Data;
import org.study.grabli_application.util.ValidationUtils;

import javax.validation.constraints.NotBlank;

@Data
public class StreetObjectDtoUpdate {
    @NotBlank(message = ValidationUtils.BLANK_FIELD)
    private String title;

    @NotBlank(message = ValidationUtils.BLANK_FIELD)
    private String description;
}
