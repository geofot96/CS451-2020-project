package cs451.links;

import cs451.Host;
import cs451.Message;
import cs451.utils.Deliverer;

import java.util.HashSet;
import java.util.Timer;

/**
 * Class name: PerfectLinks.java
 * Created by: George Fotiadis
 * Date: 13/10/2020 at 11:16
 **/
public class PerfectLinks implements Deliverer, Link
{
    Deliverer deliverer;
    StubbornLinks stubbornLinks;
    private HashSet<Integer> delivered;

    public PerfectLinks(Deliverer deliverer, int port)
    {
        this.deliverer = deliverer;
        this.stubbornLinks = new StubbornLinks(this, port);
        this.delivered = new HashSet<>();
    }

    @Override
    public void start()
    {

    }

    @Override
    public void send(Message message, Host host)
    {
        stubbornLinks.send(message, host);
    }

    @Override
    public void deliver(Message message)
    {
        if(!this.delivered.contains(message.getMessageId()))
        {
            this.delivered.add(message.getMessageId());
            this.deliverer.deliver(message);
        }

    }
}
