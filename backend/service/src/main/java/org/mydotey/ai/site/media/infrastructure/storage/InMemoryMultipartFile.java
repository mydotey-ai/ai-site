package org.mydotey.ai.site.media.infrastructure.storage;

import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * 内存中的 MultipartFile 实现
 * 用于处理已加载到内存中的文件数据
 *
 * @author AI-Site
 */
public class InMemoryMultipartFile implements MultipartFile {

    private final String name;
    private final String originalFilename;
    private final String contentType;
    private final byte[] bytes;

    public InMemoryMultipartFile(String name, String originalFilename, String contentType, byte[] bytes) {
        this.name = name;
        this.originalFilename = originalFilename;
        this.contentType = contentType;
        this.bytes = bytes;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getOriginalFilename() {
        return originalFilename;
    }

    @Override
    public String getContentType() {
        return contentType;
    }

    @Override
    public boolean isEmpty() {
        return bytes == null || bytes.length == 0;
    }

    @Override
    public long getSize() {
        return bytes != null ? bytes.length : 0;
    }

    @Override
    public byte[] getBytes() throws IOException {
        return bytes != null ? bytes : new byte[0];
    }

    @Override
    public InputStream getInputStream() throws IOException {
        return new ByteArrayInputStream(bytes != null ? bytes : new byte[0]);
    }

    @Override
    public void transferTo(java.io.File dest) throws IOException, IllegalStateException {
        new java.io.FileOutputStream(dest).write(bytes);
    }
}
