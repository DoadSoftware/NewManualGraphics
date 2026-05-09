package com.manual.config.core;

import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;
import com.manual.config.WebMvcConfig;

import jakarta.servlet.MultipartConfigElement;
import jakarta.servlet.ServletRegistration;
import jakarta.servlet.ServletRegistration.Dynamic;

public class SpringMvcInitializer extends AbstractAnnotationConfigDispatcherServletInitializer {

    @Override
    protected Class<?>[] getRootConfigClasses() {
        return new Class[] { WebMvcConfig.class };
    }

    @Override
    protected Class<?>[] getServletConfigClasses() {
        return null;
    }

    @Override
    protected String[] getServletMappings() {
        return new String[] { "/" };
    }
    @Override
    protected void customizeRegistration(Dynamic registration) {
    	registration.setMultipartConfig(
		    new MultipartConfigElement("",
		        10 * 1024 * 1024,   // 10MB max file size
		        20 * 1024 * 1024,   // 20MB max request size
		        5 * 1024 * 1024     // 5MB threshold
		    )
		);
    }
}