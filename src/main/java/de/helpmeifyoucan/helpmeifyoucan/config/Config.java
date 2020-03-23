package de.helpmeifyoucan.helpmeifyoucan.config;

public final class Config {

    public static final String DATABASE_PROTOCOL = "mongodb+srv";
    public static final String DATABASE_HOST = System.getenv("DATABASE_HOST");
    public static final String DATABASE_USER = "dbUser";
    public static final String DATABASE_PASSWORD = System.getenv("DATABASE_PASSWORD");
    public static final String DATABASE_NAME = "helpmeifyoucan";

    public static final String JWT_SECRET = "SecretKeyToGenJWTs";
    public static final long JWT_EXPIRATION_TIME = 864_000_000; // 10 days
    public static final String JWT_TOKEN_PREFIX = "Bearer ";
    public static final String JWT_HEADER_STRING = "Authorization";
    public static final String JWT_SIGN_UP_URL = "/auth/sign-up";

}