package com.example.shareformore.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class FilePathConfig implements WebMvcConfigurer {
    @Value("${file.accessPath}")
    private String accessPath;
    @Value("${file.baseUrl}")
    private String baseUrl;
    @Value("${file.uploadFolder}")
    private String uploadFolder;

    public String getAccessPath() {
        return accessPath;
    }

    public String getBaseUrl() {
        return baseUrl;
    }

    public String getUploadFolder() {
        return uploadFolder;
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler(accessPath).addResourceLocations("file:" + uploadFolder);
    }
}
