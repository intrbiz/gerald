package com.intrbiz.gerald.polyakov.transport.lamplighter;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.http.Header;
import org.apache.http.HttpEntity;

public class SimpleBufferedHttpEntity implements HttpEntity
{
    private final HttpEntity entity;
    
    private final byte[] buffer;
    
    public SimpleBufferedHttpEntity(HttpEntity entity) throws IOException
    {
        super();
        this.entity = entity;
        this.buffer = buffer(entity);
    }

    @Override
    public boolean isRepeatable()
    {
        return true;
    }

    @Override
    public boolean isChunked()
    {
        return false;
    }

    @Override
    public long getContentLength()
    {
        return this.buffer.length;
    }

    @Override
    public Header getContentType()
    {
        return this.entity.getContentType();
    }

    @Override
    public Header getContentEncoding()
    {
        return this.entity.getContentEncoding();
    }

    @Override
    public InputStream getContent() throws IOException, IllegalStateException
    {
        return new ByteArrayInputStream(this.buffer);
    }

    @Override
    public void writeTo(OutputStream outstream) throws IOException
    {
        outstream.write(this.buffer);
    }

    @Override
    public boolean isStreaming()
    {
        return true;
    }

    @Override
    @Deprecated
    public void consumeContent() throws IOException
    {
    }
    
    protected static byte[] buffer(HttpEntity entity) throws IOException
    {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        entity.writeTo(baos);
        return baos.toByteArray();
    }
}
