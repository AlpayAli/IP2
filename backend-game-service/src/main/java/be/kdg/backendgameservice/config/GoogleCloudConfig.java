package be.kdg.backendgameservice.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.ByteArrayInputStream;
import java.io.IOException;

@Configuration
public class GoogleCloudConfig {

    @Bean
    public Storage storage() throws IOException {
        // Stel Gson in met LENIENT Strictness
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        String json = "{"
                + "\"type\": \"service_account\","
                + "\"project_id\": \"ip2-team6-442207\","
                + "\"private_key_id\": \"7969adb3b63a1aa60cbe5297f18826c8f8de6f49\","
                + "\"private_key\": \"-----BEGIN PRIVATE KEY-----\\nMIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQDp0jb2fHMwcqSO\\nVWguChyfihZ2PiGDVL4MWWlTtnl7QkTcVbNZpme1Cja0bnxgYI1X5V9lBtLAdGI5\\nkEQxO3oxGt9btFHdLXjJcQIN/DBa0P7Ku8puOzp12mVeu27eROxQvUIQkppo5iOL\\n/ftHpJBUbHay3jZ3pvVOVoBylPhlQD5C2LkQHHYar3pNFZ/L2LhcFyISZo7CXvRB\\nHx3TJ3A2dM7XpFgp7EmXd4uIQGOUAVyu/DedzOVcSfxeeVTGhUDTQ0hkumfJ5TkP\\nNutabAoiuB+r7yGB7IlRE2iF1UELCe77yhhkRYdImA8pZ6X+dHN0WIAEpBknu7zL\\nai73sg6rAgMBAAECggEATJtzJo7frp/hCkExQORPPHYhpdv1XvIE0kb5G1RNuPSd\\nWxM5D8ZVdBtEkRkh5vG7XXDpMFBr/GIlTHbX4Ml731rOk10g9TZQvK8p3SsjLJ4M\\n3J9qU3NUVAiZ7c+7I1exlUJswsFFldjRDKzKB08DeqWOvkvPzKQG+wOcUe488+7Y\\nIbndhxH7wzKHUhoXIBrjh/kokxfYtBDcBUjdSWboTNXQeAkdIgEdlomwXmurhHSL\\n34iBO9PHcLF8cDxCiwyCVNFGuzbqUzxDCt5s+iWsMEgPcdDjadAhpWWgZ/IF9JeO\\nvGmJB1hUkixkT8lmeGvduUv4Pf0bwwgG1q3LaXiwAQKBgQD4FfZcM0YlCBU0cWAq\\nf5cw7x+mIl/45G9TesYooh4iGMn9i3/qAJD8nkc4mZJtHrtRh6FoVfbg5tAH4MGD\\nCdIZcCsuzB3tcZbL3s/47BAJ7iAdNOfEJX7wzYl9Wcl5mD5rrRYNsbw8+vS2/Ndq\\nihzPC+5TRqSSboUu6nqYn0j36wKBgQDxR8HxQ0f51WOXERrVIO3uyJIvGyHqCnW7\\nN5nJGVUEP0N1L5rUqGx5VEkKWMoTpJTS1LQThWRJFEXpaA72qEy/N134ovC6Ub3y\\nQSvomaFcpzCe9hIs+J5LMKmD//OT3UTL8I9Z2jK73vxaIJKjNMqY5pUT0xvVUKGk\\nyQkfy/ZUQQKBgGCWzHBhVjIcz4S3j6V8P7VBpKTeRVd2ORHbf0NauzizRhF9OrQ0\\nvD5esYQL5sKtcGjx0Zn5vkEaHGy0ySLns/FLhsSVnCFSVE/T1E0qO6RiHgbBDmuD\\n3cSLgbhYOOUqrULnBOsYBB4J5zVHg+drFoRWNNgfbPvGI/x8NnctrQ85AoGANAr+\\nUkPo6oKUkKkn2IDZ0X+ByJdZoVnQOymrCi1koR3kNwqloukRPLppZsnwDHybfT5T\\n+UH0d67fxZCkaZ+zrisLIe5DbmzjQ7W6+tjobUvCuQbqtLiLrDw61DYJHAPGKxcg\\nVIk0GEHovKf5+SoR6RkTXstSXZiexSR3y1v7LgECgYEAg6fkwgY6XmBkuxpfzvGT\\n85uU3ezdgtYl5UG0HHLBToEYwnK4MDuflpR2vWIsBHhj2M3df5kV4bdjMuO+nMUL\\n5SD+lRCSmUbs+IQQK8E7btN9YBSv3294YzYgLF5VU1sTcmx5sUUCIZzyXgN0MIo/\\n4WHhlnv5+7/nXRSflTribOA=\\n-----END PRIVATE KEY-----\\n\","
                + "\"client_email\": \"lucasip2@ip2-team6-442207.iam.gserviceaccount.com\","
                + "\"client_id\": \"113864526235054184435\","
                + "\"auth_uri\": \"https://accounts.google.com/o/oauth2/auth\","
                + "\"token_uri\": \"https://oauth2.googleapis.com/token\","
                + "\"auth_provider_x509_cert_url\": \"https://www.googleapis.com/oauth2/v1/certs\","
                + "\"client_x509_cert_url\": \"https://www.googleapis.com/robot/v1/metadata/x509/lucasip2%40ip2-team6-442207.iam.gserviceaccount.com\","
                + "\"universe_domain\": \"googleapis.com\""
                + "}";


        // Creëer de GoogleCredentials met een ByteArrayInputStream
        GoogleCredentials credentials = GoogleCredentials.fromStream(
                new ByteArrayInputStream(json.getBytes())
        );

        // Bouw en retourneer de Google Cloud Storage client
        return StorageOptions.newBuilder()
                .setCredentials(credentials)
                .build()
                .getService();
    }
}
