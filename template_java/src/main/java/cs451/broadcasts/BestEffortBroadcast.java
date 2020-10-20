package cs451.broadcasts;

import cs451.Host;
import cs451.utils.Message;
import cs451.links.PerfectLinks;
import cs451.utils.Deliverer;

import java.util.List;

/**
 * Class name: BestEffortBroadcast.java
 * Created by: George Fotiadis
 * Date: 01/10/2020 at 14:17
 **/
public class BestEffortBroadcast implements Deliverer, Broadcast
{
    private Deliverer deliverer;
    private PerfectLinks perfectLinks;
    private List<Host> hosts;

    public BestEffortBroadcast(Deliverer deliverer, List<Host> hosts, int myPort)
    {
        this.deliverer = deliverer;
        this.perfectLinks = new PerfectLinks(this, myPort);
        this.hosts = hosts;
    }

    @Override
    public void start()
    {

    }

    @Override
    public void stop()
    {

    }

    @Override
    public void broadcast(Message message)
    {
        for(Host host : hosts)
        {
            this.perfectLinks.send(message, host);
        }
    }

    @Override
    public void deliver(Message message)
    {
        this.deliverer.deliver(message);
    }
}