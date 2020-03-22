package de.helpmeifyoucan.helpmeifyoucan;

public final class Config {
    public static final String DATABASE_ADDRESS = "helpme-1rsgf.mongodb.net/test?retryWrites=true&w=majority";
    public static final String DATABASE_USER = "dbUser";
    public static final String DATABASE_PASSWORD = System.getenv("DATABASE_PASSWORD");
    public static final String DATABASE_NAME = "helpmeifyoucan";
}