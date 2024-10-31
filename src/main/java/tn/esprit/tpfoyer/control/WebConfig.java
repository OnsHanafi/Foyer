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
                        .allowedOrigins("*")  // Allow all origins
                        .allowedMethods("*")  // Allow all HTTP methods
                        .allowedHeaders("*")   // Allow all headers
                        .allowCredentials(false);  // Allow credentials if needed

            }
        };

    }
}