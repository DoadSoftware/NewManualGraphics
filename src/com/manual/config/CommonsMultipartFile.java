package com.manual.config;

import org.apache.commons.fileupload2.core.DiskFileItem;
import org.springframework.web.multipart.MultipartFile;
import java.io.*;
import java.nio.file.*;

public class CommonsMultipartFile implements MultipartFile {

    private final DiskFileItem fileItem;

    public CommonsMultipartFile(DiskFileItem fileItem) {
        this.fileItem = fileItem;
    }

    @Override public String getName() { return fileItem.getFieldName(); }

    @Override
    public String getOriginalFilename() {
        String name = fileItem.getName();
        if (name == null || name.isBlank()) return "";
        int slash  = name.lastIndexOf('/');
        int bslash = name.lastIndexOf('\\');
        return name.substring(Math.max(slash, bslash) + 1);
    }

    @Override public String  getContentType() { return fileItem.getContentType(); }
    @Override public boolean isEmpty()         { return fileItem.getSize() == 0; }
    @Override public long    getSize()         { return fileItem.getSize(); }
    @Override public byte[]  getBytes()  throws IOException { return fileItem.get(); }
    @Override public InputStream getInputStream() throws IOException { return fileItem.getInputStream(); }

    @Override
    public void transferTo(File dest) throws IOException { transferTo(dest.toPath()); }

    @Override
    public void transferTo(Path dest) throws IOException {
        try (InputStream in = fileItem.getInputStream()) {
            Files.copy(in, dest, StandardCopyOption.REPLACE_EXISTING);
        }
    }
}