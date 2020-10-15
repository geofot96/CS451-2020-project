package cs451.utils;

import java.util.Objects;

/**
 * Class name: Tuple.java
 * Created by: George Fotiadis
 * Date: 13/10/2020 at 11:06
 **/
public class Tuple<T, S>
{
    public T first;
    public S second;

    public Tuple(T first, S second)
    {
        this.first = first;
        this.second = second;
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Tuple<?, ?> tuple = (Tuple<?, ?>) o;
        return Objects.equals(first, tuple.first) &&
                Objects.equals(second, tuple.second);
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(first, second);
    }
}
