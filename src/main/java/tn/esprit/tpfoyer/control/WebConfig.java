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
                .allowedMethods("*")  // Allow all HTTP methods
                .allowedHeaders("*")  // Allow all headers
                .allowedOrigins("*")  // Allow all origins
                .allowCredentials(false);  // Allow credentials if needed
    }
}