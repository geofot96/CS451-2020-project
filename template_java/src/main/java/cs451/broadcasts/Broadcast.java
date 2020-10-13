package cs451.broadcasts;

import cs451.Message;

/**
 * Class name: Broadcast.java
 * Created by: George Fotiadis
 * Date: 13/10/2020 at 09:51
 **/
public interface Broadcast
{
    public abstract void start();

    public abstract void stop();

    public abstract void broadcast(Message message);
}
