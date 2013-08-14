package com.intrbiz.gerald.polyakov;

import java.nio.ByteBuffer;
import java.util.LinkedList;
import java.util.List;

public class Parcel
{   
    private final String format;
    
    private final String contentType;
    
    private List<ByteBuffer> buffers = new LinkedList<ByteBuffer>();
    
    public Parcel(String format, String contentType)
    {
        super();
        this.format = format;
        this.contentType = contentType;
    }
    
    public String getFormat()
    {
        return this.format;
    }
    
    public String getContentType()
    {
        return this.contentType;
    }
    
    public void buffer(ByteBuffer buffer)
    {
        this.buffers.add(buffer);
    }
    
    public List<ByteBuffer> buffers()
    {
        return this.buffers;
    }
    
    public long getContentLength()
    {
        long l = 0;
        for (ByteBuffer buffer : this.buffers)
        {
            l += buffer.limit();
        }
        return l;
    }
}
