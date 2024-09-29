package org.study.grabli_application.dto;

import lombok.Data;
import org.study.grabli_application.util.ValidationUtils;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
public class StreetObjectDtoCreate {
    @NotNull(message = ValidationUtils.INVALID_STREET_OBJECT_TYPE)
    private Integer typeId;

    @Size(min = 2, max = 2, message = ValidationUtils.INVALID_COORDINATES_SIZE)
    private double[] coordinates;

    @NotBlank(message = ValidationUtils.BLANK_FIELD)
    private String title;

    @NotBlank(message = ValidationUtils.BLANK_FIELD)
    private String description;

    @NotBlank(message = ValidationUtils.BLANK_FIELD)
    private String creatorName;

    @NotBlank(message = ValidationUtils.BLANK_FIELD)
    @Email(message = ValidationUtils.INVALID_EMAIL)
    private String creatorContact;
}
