package com.intrbiz.gerald.polyakov.transport;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

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
import org.apache.log4j.Logger;

import com.intrbiz.gerald.polyakov.Parcel;
import com.intrbiz.gerald.polyakov.Transport;
import com.intrbiz.util.Option;

@SuppressWarnings("deprecation")
public abstract class AbstractHTTPTransport implements Transport
{
    private Logger logger = Logger.getLogger(AbstractHTTPTransport.class);
    
    private String to;

    private HttpClient httpClient;

    public AbstractHTTPTransport()
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

    @SuppressWarnings("unused")
    @Override
    public void courier(Parcel parcel)
    {
        String url = this.to;
        //
        HttpPost post = new HttpPost(url);
        // headers
        this.defaultHeaders(parcel, post);
        // body
        try
        {
            logger.debug("Posting to " + url);
            post.setEntity(this.createEntity(parcel));
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
        httpRequest.setHeader(new BasicHeader("X-Polyakov-Version", "1.0.0"));
    }
    
    protected abstract HttpEntity createEntity(Parcel parcel) throws IOException;
}
