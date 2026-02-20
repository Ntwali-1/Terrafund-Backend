package com.services.land_service.security;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

public class UserAuthenticationToken extends UsernamePasswordAuthenticationToken {

    private final Long userId;

    public UserAuthenticationToken(
            Object principal,
            Long userId,
            Object credentials,
            Collection<? extends GrantedAuthority> authorities
    ) {
        super(principal, credentials, authorities);
        this.userId = userId;
    }

    public Long getUserId() {
        return userId;
    }
}