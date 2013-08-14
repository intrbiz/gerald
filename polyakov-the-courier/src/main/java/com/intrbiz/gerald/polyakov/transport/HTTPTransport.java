package com.intrbiz.gerald.polyakov.transport;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.util.UUID;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.message.BasicHeader;
import org.apache.http.params.CoreProtocolPNames;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.util.EntityUtils;

import com.intrbiz.gerald.polyakov.Parcel;
import com.intrbiz.gerald.polyakov.PolyakovKey;
import com.intrbiz.gerald.polyakov.Transport;
import com.intrbiz.util.Option;

@SuppressWarnings("deprecation")
public class HTTPTransport implements Transport
{
    private String to;

    private HttpClient httpClient;

    private UUID nodeId;

    private String nodeName;
    
    private String nodeService;
    
    private PolyakovKey key;

    public HTTPTransport()
    {
        this.httpClient = new DefaultHttpClient(new ThreadSafeClientConnManager());
        this.httpClient.getParams().setParameter(CoreProtocolPNames.USER_AGENT, "Polyakov");
    }

    @Override
    public void option(Option<String> option, String value)
    {
    }

    @Override
    public void option(Option<Integer> option, int value)
    {
    }

    @Override
    public void option(Option<Long> option, long value)
    {
    }

    @Override
    public void option(Option<Float> option, float value)
    {
    }

    @Override
    public void option(Option<Double> option, double value)
    {
    }

    @Override
    public void option(Option<Boolean> option, boolean value)
    {
    }

    @Override
    public void to(String url)
    {
        this.to = url;
    }

    @Override
    public void node(UUID id, String name, String service)
    {
        this.nodeId = id;
        this.nodeName = name;
        this.nodeService = service;
    }

    @Override
    public void key(PolyakovKey key)
    {
        this.key = key;
    }

    @SuppressWarnings("unused")
    @Override
    public void courier(Parcel parcel)
    {
        String url = this.to  + "/" + parcel.getFormat();
        //
        HttpPost post = new HttpPost(url);
        // headers
        this.defaultHeaders(parcel, post);
        // body
        try
        {
            System.out.println("Posting to " + url);
            post.setEntity(new ParcelEntity(parcel));
            HttpResponse res = this.httpClient.execute(post, new BasicHttpContext());
            int code = res.getStatusLine().getStatusCode();
            //
            HttpEntity entity = res.getEntity();
            String response = EntityUtils.toString(entity);
        }
        catch (UnsupportedEncodingException e)
        {
            post.abort();
            throw new RuntimeException(e);
        }
        catch (ClientProtocolException e)
        {
            post.abort();
            throw new RuntimeException(e);
        }
        catch (IOException e)
        {
            post.abort();
            throw new RuntimeException(e);
        }
    }
    
    protected void defaultHeaders(Parcel parcel, HttpRequest httpRequest)
    {
        // node id
        httpRequest.setHeader(new BasicHeader("X-Polyakov-Node-Id", this.nodeId.toString().toUpperCase()));
        // node name
        httpRequest.setHeader(new BasicHeader("X-Polyakov-Node-Name", this.nodeName));
        // node service
        httpRequest.setHeader(new BasicHeader("X-Polyakov-Node-Service", this.nodeService));
        // sig
        httpRequest.setHeader(new BasicHeader("X-Polyakov-Signature", this.key.sign(parcel)));
    }
    
    public static class ParcelEntity implements HttpEntity
    {
        private final Parcel entity;
        
        private final long length;
        
        public ParcelEntity(Parcel entity)
        {
            super();
            this.entity = entity;
            // calculate the length
            long l = 0;
            for (ByteBuffer bb : entity.buffers())
            {
                l += bb.capacity();
            }
            this.length = l;
        }

        @Override
        public boolean isRepeatable()
        {
            return false;
        }

        @Override
        public boolean isChunked()
        {
            return false;
        }

        @Override
        public long getContentLength()
        {
            return this.length;
        }

        @Override
        public Header getContentType()
        {
            return new BasicHeader("Content-Type", this.entity.getContentType());
        }

        @Override
        public Header getContentEncoding()
        {
            return null;
        }

        @Override
        public InputStream getContent() throws IOException, IllegalStateException
        {
            return null;
        }

        @Override
        public void writeTo(OutputStream outstream) throws IOException
        {
            for (ByteBuffer bb : this.entity.buffers())
            {
                if (bb.hasArray())
                {
                    outstream.write(bb.array(), bb.arrayOffset(), bb.capacity());
                }
                else
                {
                    while (bb.hasRemaining())
                    {
                        outstream.write(bb.get() & 0xFF);
                    }
                    bb.reset();
                }
            }
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
    }
}
