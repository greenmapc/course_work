package ru.itis.teamwork.models;

import org.springframework.security.core.GrantedAuthority;

public enum Roles implements GrantedAuthority {
    USER, ADMIN;
    @Override
    public String getAuthority() {
        return name();
    }
}
