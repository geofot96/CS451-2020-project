package cs451.links;

import cs451.Host;
import cs451.utils.Message;
import cs451.utils.Deliverer;
import cs451.utils.Tuple;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Class name: PerfectLinks.java
 * Created by: George Fotiadis
 * Date: 13/10/2020 at 11:16
 **/
public class PerfectLinks implements Deliverer, Link
{
    private Deliverer deliverer;
    private StubbornLinks stubbornLinks;
    private Set<Tuple<Integer, Integer>> delivered;

    public PerfectLinks(Deliverer deliverer, int port)
    {
        this.deliverer = deliverer;
        this.stubbornLinks = new StubbornLinks(this, port);
        this.delivered = ConcurrentHashMap.newKeySet();
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
        Tuple tuple = new Tuple(message.getSenderId(), message.getMessageId());
        if(!this.delivered.contains(tuple))
        {
            this.delivered.add(tuple);
            this.deliverer.deliver(message);
        }

    }
}
