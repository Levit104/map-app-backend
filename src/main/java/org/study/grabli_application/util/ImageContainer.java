package org.study.grabli_application.util;

import org.springframework.core.io.Resource;

public record ImageContainer(String contentType, Resource resource) {
}
