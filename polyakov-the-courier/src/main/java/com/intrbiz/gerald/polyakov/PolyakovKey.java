package com.intrbiz.gerald.polyakov;

import java.nio.ByteBuffer;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import org.apache.commons.codec.binary.Base64;

public class PolyakovKey
{
    public enum KeyHash {
        SHA1("SHA-1"),
        SHA256("SHA-256"),
        SHA512("SHA-512");
        
        private final String digestName;
        
        private KeyHash(String digestName)
        {
            this.digestName = digestName;
        }
        
        public final String getDigestName()
        {
            return this.digestName;
        }
    }
    
    public static final KeyHash DEFAULT_HASH = KeyHash.SHA512;
    
    private KeyHash hash = DEFAULT_HASH;
    
    private byte[] key;
    
    public PolyakovKey()
    {
        super();
    }
    
    public PolyakovKey(KeyHash hash)
    {
        super();
        this.hash = hash;
    }
    
    public PolyakovKey(byte[] key)
    {
        super();
        this.key = key;
    }
    
    public PolyakovKey(KeyHash hash, byte[] key)
    {
        super();
        this.hash = hash;
        this.key = key;
    }
    
    public PolyakovKey(String key)
    {
        super();
        this.key(key);
    }
    
    public void key(String encodedKey)
    {
        int idx = encodedKey.indexOf(":");
        if (idx <= 0) throw new RuntimeException("Invalid encoded key");
        //
        String alg = encodedKey.substring(0, idx);
        String key = encodedKey.substring(idx + 1);
        //
        this.hash = KeyHash.valueOf(alg.toUpperCase());
        this.key = Base64.decodeBase64(key);
    }

    public KeyHash getHash()
    {
        return hash;
    }

    public void setHash(KeyHash hash)
    {
        this.hash = hash;
    }

    public void setKey(byte[] key)
    {
        this.key = key;
    }
    
    public byte[] getKey()
    {
        return this.key;
    }
    
    public String sign(Parcel parcel)
    {
        try
        {
            MessageDigest md = MessageDigest.getInstance( this.hash.getDigestName() );
            //
            md.update(this.key);
            //
            for (ByteBuffer bb : parcel.buffers())
            {
                if (bb.hasArray())
                {
                    md.update(bb.array(), bb.arrayOffset(), bb.capacity());
                }
                else
                {
                    while (bb.hasRemaining())
                    {
                        md.update(bb.get());
                    }
                    bb.rewind();
                }
            }
            byte[] sig = md.digest();
            //
            return this.hash.toString() + ":" + Base64.encodeBase64String(sig);
        }
        catch (NoSuchAlgorithmException e)
        {
        }
        return "";
    }
    
    public boolean check(Parcel parcel, String sig)
    {
        String ourSig = this.sign(parcel);
        return ourSig.equals(sig);
    }
    
    public String toString()
    {
        return (this.hash == null ? "" : this.hash.toString()) + ":" + (this.key == null ? "" : Base64.encodeBase64String(this.key));
    }
    
    //
    
    /**
     * Generate a random PolyakovKey.
     * 
     * @param hash
     *     The Key Hash function
     * @param len
     *     The key length, must be between 0 and 8192
     * @return
     *     A PolyakovKey
     */
    public static PolyakovKey generate(KeyHash hash, int len)
    {
        if (hash == null) throw new IllegalArgumentException("The Key Hash function cannot be null");
        if (len <= 0 && len <= 8192) throw new IllegalArgumentException("The key length must be between 0 and 8192");
        //
        SecureRandom sr = new SecureRandom();
        byte[] b = new byte[len];
        sr.nextBytes(b);
        return new PolyakovKey(hash, b);
    }
    
    public static PolyakovKey generate(int len)
    {
        return generate(DEFAULT_HASH, len);
    }
}
