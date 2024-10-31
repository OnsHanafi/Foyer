package tn.esprit.tpfoyer.control;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebMvc
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry
                .addMapping("/**")  // Allow all endpoints
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")  // Allow all HTTP methods
                .allowedHeaders("*")  // Allow all headers
                .allowedOrigins("http://192.168.1.24")  // Allow all origins
                .allowCredentials(false);  // Allow credentials if needed
    }
}