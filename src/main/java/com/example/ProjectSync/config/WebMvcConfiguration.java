package com.example.ProjectSync.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * WebMvcConfiguration - Configures Spring MVC for serving static resources
 * and routing the root path to index.html
 */
@Configuration
public class WebMvcConfiguration implements WebMvcConfigurer {

    /**
     * Forward root path to index.html so the frontend loads
     * This ensures that when users visit http://localhost:8080/,
     * they get the index.html page instead of an error
     */
    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/").setViewName("forward:/index.html");
    }
}
