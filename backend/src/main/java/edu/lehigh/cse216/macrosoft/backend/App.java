package edu.lehigh.cse216.macrosoft.backend;

import java.util.Map;

public class App {

    public static void main(String[] args) throws Exception {
        // Default server configurations
        int PORT = 20000;
        String STATIC_LOCATION = "/src/main/resources";
        String DATABASE_URL = "";
        String CLIENT_ID = "";
        String MEMCACHED_SERVERS = "";
        String MEMCACHED_USERNAME = "";
        String MEMCACHED_PASSWORD = "";

        // Get configuration from environment
        Map<String, String> env = System.getenv();
        String val;
        if ((val = env.get("PORT")) != null)
            PORT = Integer.parseInt(val);
        if ((val = env.get("STATIC_LOCATION")) != null)
            STATIC_LOCATION = val;
        if ((val = env.get("DATABASE_URL")) != null)
            DATABASE_URL = val;
        if ((val = env.get("CLIENT_ID")) != null)
            CLIENT_ID = val;
        if ((val = env.get("MEMCACHED_SERVERS")) != null)
            MEMCACHED_SERVERS = val;
        if ((val = env.get("MEMCACHED_USERNAME")) != null)
            MEMCACHED_USERNAME = val;
        if ((val = env.get("MEMCACHED_PASSWORD")) != null)
            MEMCACHED_PASSWORD = val;

        // Run server
        BuzzServer.run(
                PORT,
                STATIC_LOCATION,
                DATABASE_URL,
                CLIENT_ID,
                MEMCACHED_SERVERS,
                MEMCACHED_USERNAME,
                MEMCACHED_PASSWORD
        );
    }

}
