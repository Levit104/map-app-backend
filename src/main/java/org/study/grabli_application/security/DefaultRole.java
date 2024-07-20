package org.study.grabli_application.security;

import org.springframework.security.core.GrantedAuthority;

public enum DefaultRole implements GrantedAuthority {

    DEFAULT;

    @Override
    public String getAuthority() {
        return "USER";
    }
}
