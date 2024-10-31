package tn.esprit.tpfoyer.control;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig {

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {

            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry
                        .addMapping("/tpfoyer/foyer/**")  // Allow all endpoints
                        .allowedOrigins("http://192.168.1.24:80","http://192.168.1.24")  // Allow all origins
                        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")  // Allow all HTTP methods
                        .allowedHeaders("Content-Type")   // Allow all headers
                        .allowCredentials(true);  // Allow credentials if needed

            }
        };

    }
}