package com.intrbiz.gerald.witchcraft;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.CopyOnWriteArrayList;

import com.codahale.metrics.MetricRegistry;
import com.intrbiz.gerald.source.DefaultIntelligenceSource;
import com.intrbiz.gerald.source.IntelligenceSource;


/**
 * Witchcraft is a collection of intelligence sources which should be 
 * exposed by Gerald
 */
public class Witchcraft
{
    private static final Witchcraft US = new Witchcraft();
    
    public static final Witchcraft get()
    {
        return US;
    }
    
    private final ConcurrentMap<String, IntelligenceSource> sources = new ConcurrentHashMap<String, IntelligenceSource>();
    
    private final List<WitchcraftListener> registerListeners = new CopyOnWriteArrayList<WitchcraftListener>();
    
    private Witchcraft()
    {
        super();
    }
    
    /**
     * Listen to registration events
     */
    public void addRegisterListener(WitchcraftListener listener)
    {
        this.registerListeners.add(listener);
    }
    
    /**
     * Stop listening to registration events
     */
    public void removeRegisterListener(WitchcraftListener listener)
    {
        this.registerListeners.remove(listener);
    }
    
    /**
     * Get all the registered sources
     */
    public Collection<IntelligenceSource> getSources()
    {
        return Collections.unmodifiableCollection(this.sources.values());
    }
    
    /**
     * Register the given source.  An IllegalArgumentException will be raised 
     * if the source already exists.
     */
    public Witchcraft register(IntelligenceSource source)
    {
        IntelligenceSource existing = this.sources.putIfAbsent(source.getName(), source);
        if (existing != null) throw new IllegalArgumentException("The source " + source.getName() + " already exists!");
        if (existing == null)
        {
            for (WitchcraftListener listener : this.registerListeners)
            {
                listener.onAdded(source);
            }
        }
        return this;
    }
    
    /**
     * Get or create a default source of the given name
     */
    public IntelligenceSource source(String name)
    {
        return this.source(new DefaultIntelligenceSource(name));
    }
    
    @SuppressWarnings("unchecked")
    public <T extends IntelligenceSource> T source(T source)
    {
        IntelligenceSource existing = this.sources.putIfAbsent(source.getName(), source);
        if (existing == null)
        {
            for (WitchcraftListener listener : this.registerListeners)
            {
                listener.onAdded(source);
            }
            return source;
        }
        return (T) existing;
    }
    
    /**
     * Get the source with the given name
     */
    public IntelligenceSource get(String name)
    {
        return this.sources.get(name);
    }
    
    /**
     * Does the given source exists
     */
    public boolean contains(String name)
    {
        return this.sources.containsKey(name);
    }
    
    /**
     * Remove the given source
     */
    public void remove(String name)
    {
        this.sources.remove(name);
    }
    
    // naming helpers
    
    public static String scoped(Class<?> klass, String name, String scope)
    {
        return name(klass, name, "[" + scope + "]");
    }
    
    public static String name(Class<?> klass, String... names)
    {
        return MetricRegistry.name(klass, names);
    }
    
    public static String name(String name, String... names)
    {
        return MetricRegistry.name(name, names);
    }
}
