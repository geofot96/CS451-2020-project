package cs451.broadcasts;

import cs451.Host;
import cs451.utils.Deliverer;
import cs451.utils.Message;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Class name: CausalBroadcast.java
 * Created by: George Fotiadis
 * Date: 23/11/2020 at 16:39
 **/
public class CausalBroadcast implements Deliverer, Broadcast
{
    private Deliverer deliverer;
    private UniformReliableBroadcast uniformReliableBroadcast;
    private int lsn; // number of messages that I have crb broadcasted
    private Set<Message> pending; // messages urb delivered but waiting to be fifo delivered
    private Map<Integer, Integer> vClock; // map from processId j to number of messages that it has crb-delivered from process j
    private int myHost;

    public CausalBroadcast(Deliverer deliverer, List<Host> hosts, int myPort, int myHost)
    {
        this.deliverer = deliverer;
        this.uniformReliableBroadcast = new UniformReliableBroadcast(this, hosts, myPort, myHost);
        this.lsn = 0;
        this.pending = ConcurrentHashMap.newKeySet();
        this.myHost = myHost;
        this.vClock = new ConcurrentHashMap<>();

        for(Host host : hosts)
        {
            vClock.put(host.getId(), 0);
        }
    }

    @Override
    public void broadcast(Message message)
    {
        ConcurrentHashMap<Integer, Integer> wClock = new ConcurrentHashMap<>(vClock);
        wClock.put(this.myHost, this.lsn);
        lsn += 1;
        message.setvClock(wClock);
        this.uniformReliableBroadcast.broadcast(message);
    }

    @Override
    public void deliver(Message message)
    {
        System.out.println(message.getvClock());

        this.pending.add(message);

        Iterator<Message> i = pending.iterator();

        while (i.hasNext())
        {
            Message m = i.next();

            if(isSmallerThan(m.getvClock()))
            {
                vClock.put(m.getoriginalSenderId(), vClock.get(m.getoriginalSenderId()) + 1);
                this.deliverer.deliver(m);
                i.remove();
                i = pending.iterator();
            }
        }
    }

    private boolean isSmallerThan(Map<Integer, Integer> wClock)
    {
        for(Integer i : vClock.keySet())
        {
            if(wClock.get(i) > vClock.get(i))
            {
                return false;
            }
        }
        return true;
    }

    @Override
    public void start()
    {

    }

    @Override
    public void stop()
    {

    }
}
