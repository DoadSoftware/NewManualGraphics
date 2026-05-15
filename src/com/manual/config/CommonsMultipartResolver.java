package com.manual.config;

import jakarta.servlet.http.HttpServletRequest;
import org.apache.commons.fileupload2.core.*;
import org.apache.commons.fileupload2.jakarta.servlet6.JakartaServletFileUpload;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.multipart.*;
import org.springframework.web.multipart.support.DefaultMultipartHttpServletRequest;

import java.nio.charset.StandardCharsets;
import java.util.*;

public class CommonsMultipartResolver implements MultipartResolver {

    private final long maxUploadSize;
    private final long maxUploadSizePerFile;

    public CommonsMultipartResolver(long maxUploadSize, long maxUploadSizePerFile) {
        this.maxUploadSize        = maxUploadSize;
        this.maxUploadSizePerFile = maxUploadSizePerFile;
    }

    @Override
    public boolean isMultipart(HttpServletRequest request) {
        return JakartaServletFileUpload.isMultipartContent(request);
    }

    @Override
    public MultipartHttpServletRequest resolveMultipart(HttpServletRequest request)
            throws MultipartException {
        try {
            DiskFileItemFactory factory = DiskFileItemFactory.builder().get();

            JakartaServletFileUpload<DiskFileItem, DiskFileItemFactory> upload =
                    new JakartaServletFileUpload<>(factory);

            if (maxUploadSize > 0)        upload.setSizeMax(maxUploadSize);
            if (maxUploadSizePerFile > 0) upload.setFileSizeMax(maxUploadSizePerFile);

            // reads raw InputStream — never calls request.getParts()
            // so Tomcat's fileCountMax limit is NEVER triggered
            List<DiskFileItem> items = upload.parseRequest(request);

            MultiValueMap<String, MultipartFile> files  = new LinkedMultiValueMap<>();
            Map<String, String[]>                params = new HashMap<>();
            Map<String, String>                  types  = new HashMap<>();

            for (DiskFileItem item : items) {
                if (item.isFormField()) {
                    String field = item.getFieldName();
                    String value = item.getString(StandardCharsets.UTF_8);
                    String[] existing = params.get(field);
                    if (existing == null) {
                        params.put(field, new String[]{ value });
                    } else {
                        String[] merged = Arrays.copyOf(existing, existing.length + 1);
                        merged[existing.length] = value;
                        params.put(field, merged);
                    }
                    if (item.getContentType() != null) types.put(field, item.getContentType());
                } else {
                    files.add(item.getFieldName(), new CommonsMultipartFile(item));
                }
            }

            return new DefaultMultipartHttpServletRequest(request, files, params, types);

        } catch (Exception ex) {
            throw new MultipartException("Failed to parse multipart request", ex);
        }
    }

    @Override
    public void cleanupMultipart(MultipartHttpServletRequest request) { }
}