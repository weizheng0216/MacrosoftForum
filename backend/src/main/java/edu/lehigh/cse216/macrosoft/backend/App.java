package edu.lehigh.cse216.macrosoft.backend;

import java.util.Map;

public class App {

    public static void main(String[] args) throws Exception {
        // Default server configurations
        int PORT = 4567;
        String STATIC_LOCATION = "/src/main/resources";
        String DATABASE_URL = "postgres://vdtksuqjtzvetb:b7ccb5e707b07d8c8bfdf7" +
                "badbae2048282884d6b2e8ad336a71ff5833b2abc3@ec2-52-22-16" +
                "1-59.compute-1.amazonaws.com:5432/d9m8d6ulhh2bbk";
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
