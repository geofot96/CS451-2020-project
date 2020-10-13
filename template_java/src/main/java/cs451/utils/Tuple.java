package cs451.utils;

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
}
