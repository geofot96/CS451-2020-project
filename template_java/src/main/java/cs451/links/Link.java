package cs451.links;

import cs451.Host;
import cs451.utils.Message;

/**
 * Class name: Link.java
 * Created by: George Fotiadis
 * Date: 13/10/2020 at 10:06
 **/
public interface Link
{
    public abstract void start();

    public abstract void send(Message message, Host host);
}
