package de.helpmeifyoucan.helpmeifyoucan.utils;

public enum Roles {

    ROLE_ADMIN, ROLE_USER;

    // Prefix "ROLE_" is needed to work with Spring Security
    public static final String ROLE_NAME_ADMIN = "ROLE_ADMIN";
    public static final String ROLE_NAME_USER = "ROLE_USER";

}