package cs451.utils;

import java.util.Objects;

/**
 * Class name: Triplet.java
 * Created by: George Fotiadis
 * Date: 21/10/2020 at 12:28
 **/
public class Triplet<T, S, V>
{
    public T first;
    public S second;
    public V third;

    public Triplet(T first, S second, V third)
    {
        this.first = first;
        this.second = second;
        this.third = third;
    }

    @Override
    public String toString()
    {
        return "Triplet{" +
                "first=" + first +
                ", second=" + second +
                ", third=" + third +
                '}';
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Triplet<?, ?, ?> triplet = (Triplet<?, ?, ?>) o;
        return Objects.equals(first, triplet.first) &&
                Objects.equals(second, triplet.second) &&
                Objects.equals(third, triplet.third);
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(first, second, third);
    }
}
