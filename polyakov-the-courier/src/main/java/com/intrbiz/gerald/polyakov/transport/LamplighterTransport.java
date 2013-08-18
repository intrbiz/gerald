package com.intrbiz.gerald.polyakov.transport;

import java.io.IOException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpRequest;
import org.apache.http.message.BasicHeader;

import com.intrbiz.Util;
import com.intrbiz.gerald.polyakov.Parcel;
import com.intrbiz.gerald.polyakov.transport.lamplighter.LamplighterJSONEntity;
import com.intrbiz.gerald.polyakov.transport.lamplighter.SimpleBufferedHttpEntity;
import com.intrbiz.util.Option;

public class LamplighterTransport extends AbstractHTTPTransport
{
    public static final Option<String> LAMPLIGHTER_KEY = new Option<String>("lamplighter.key");
    
    private String lamplighterKey = null;
    
    public LamplighterTransport()
    {
        super();
    }

    protected HttpEntity createEntity(Parcel parcel) throws IOException
    {
        // Nginx doesn't support chunked posts, so buffer the post
        return new SimpleBufferedHttpEntity(new LamplighterJSONEntity(parcel));
    }

    @Override
    public void option(Option<String> option, String value)
    {
        super.option(option, value);
        // Lamplighter key
        if (LAMPLIGHTER_KEY.equals(option))
        {
            this.lamplighterKey = value;
        }
    }

    public String getLamplighterKey()
    {
        return lamplighterKey;
    }

    public void setLamplighterKey(String lamplighterKey)
    {
        this.lamplighterKey = lamplighterKey;
    }

    @Override
    protected void defaultHeaders(Parcel parcel, HttpRequest httpRequest)
    {
        super.defaultHeaders(parcel, httpRequest);
        // add the Lamplighter Key if we have it
        if (! Util.isEmpty(this.lamplighterKey))
        {
            httpRequest.addHeader(new BasicHeader("X-Lamplighter-Key", this.lamplighterKey));
        }
    }
}
