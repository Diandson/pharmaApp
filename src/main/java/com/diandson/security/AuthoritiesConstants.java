package com.diandson.security;

/**
 * Constants for Spring Security authorities.
 */
public final class AuthoritiesConstants {

    public static final String ADMIN = "ROLE_ADMIN";

    public static final String USER = "ROLE_USER";

    public static final String ANONYMOUS = "ROLE_ANONYMOUS";
    public static final String STRUCTURE_ADMIN = "STRUCTURE_ADMIN";
    public static final String STRUCTURE_CAISSE = "STRUCTURE_CAISSE";
    public static final String SERVEUR = "SERVEUR";
    public static final String AGENCE_ADMIN = "AGENCE_ADMIN";
    public static final String AGENCE_CAISSE = "AGENCE_CAISSE";
    public static final String MAGASIN_ADMIN = "MAGASIN_ADMIN";
    public static final String MAGASIN_CAISSE = "MAGASIN_CAISSE";

    private AuthoritiesConstants() {}
}
