package com.intrbiz.gerald.polyakov;

public final class Option<V extends Object>
{
    private final String name;

    public Option(String name)
    {
        this.name = name;
    }

    public String name()
    {
        return this.name;
    }

    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        return result;
    }

    @SuppressWarnings("rawtypes")
    @Override
    public boolean equals(Object obj)
    {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        Option other = (Option) obj;
        if (name == null)
        {
            if (other.name != null) return false;
        }
        else if (!name.equals(other.name)) return false;
        return true;
    }

    public String toString()
    {
        return this.name;
    }
}
