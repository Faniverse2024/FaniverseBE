package fantastic.faniverse.config;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {
    private static final Dotenv dotenv = Dotenv.load();

    @Bean
    public static String getDbUrl() {
        return dotenv.get("DB_URL");
    }

    @Bean
    public static String getDbUsername() {
        return dotenv.get("DB_USERNAME");
    }

    @Bean
    public static String getDbPassword() {
        return dotenv.get("DB_PASSWORD");
    }
}

