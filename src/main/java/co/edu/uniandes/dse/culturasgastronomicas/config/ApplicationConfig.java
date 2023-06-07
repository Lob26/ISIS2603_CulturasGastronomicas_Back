package co.edu.uniandes.dse.culturasgastronomicas.config;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration public class ApplicationConfig {
    @Value("${app.allowed-origins}") private String allowed;

    @Bean public ModelMapper modelMapper() {
        return new ModelMapper();
    }

    @Bean public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**")
                        .allowedOrigins(allowed)
                        .allowedMethods("GET", "POST", "PUT", "DELETE")
                        .maxAge(3600);
            }

        };
    }
}
