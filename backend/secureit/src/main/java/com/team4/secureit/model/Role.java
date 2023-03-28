package com.team4.secureit.model;

import org.springframework.security.core.authority.SimpleGrantedAuthority;

public enum Role {
    ROLE_ADMIN,
    ROLE_PROPERTY_OWNER,
    ROLE_TENANT;

    public SimpleGrantedAuthority toAuthority() {
        return new SimpleGrantedAuthority(this.name());
    }
}
