package org.study.grabli_application.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.study.grabli_application.util.ValidationUtils;

@Data
public class StreetObjectDtoUpdate {
    @NotBlank(message = ValidationUtils.BLANK_FIELD)
    private String title;

    @NotBlank(message = ValidationUtils.BLANK_FIELD)
    private String description;
}
