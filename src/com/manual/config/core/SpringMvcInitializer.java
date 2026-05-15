package com.manual.config.core;

import com.manual.config.WebMvcConfig;
import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

public class SpringMvcInitializer extends AbstractAnnotationConfigDispatcherServletInitializer {

    @Override
    protected Class<?>[] getRootConfigClasses() { return new Class<?>[]{ WebMvcConfig.class }; }

    @Override
    protected Class<?>[] getServletConfigClasses() { return null; }

    @Override
    protected String[] getServletMappings() { return new String[]{ "/" }; }

    // customizeRegistration / MultipartConfigElement intentionally removed.
    // CommonsMultipartResolver reads raw InputStream directly —
    // no MultipartConfigElement needed, and its presence can cause conflicts.
}