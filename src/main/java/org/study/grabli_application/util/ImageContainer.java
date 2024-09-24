package org.study.grabli_application.util;

import lombok.Data;
import org.springframework.core.io.Resource;

@Data
public class ImageContainer {
    private final String contentType;
    private final Resource resource;
}
