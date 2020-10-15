package cs451.broadcasts;

import cs451.Host;
import cs451.utils.Deliverer;
import cs451.utils.Message;
import cs451.utils.Tuple;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Class name: ReliableBroadcast.java
 * Created by: George Fotiadis
 * Date: 15/10/2020 at 15:48
 **/
public class ReliableBroadcast implements Deliverer, Broadcast
{
    private Deliverer deliverer;
    private BestEffortBroadcast bestEffortBroadcast;
    private HashSet<Tuple<Integer, Integer>> delivered;

    public ReliableBroadcast(Deliverer deliverer, List<Host> hosts, int myPort)
    {
        this.deliverer = deliverer;
        this.bestEffortBroadcast = new BestEffortBroadcast(this, hosts, myPort);
        this.delivered = new HashSet<>();
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
        this.bestEffortBroadcast.broadcast(message);
    }

    @Override
    public void deliver(Message message)
    {
        Tuple tuple = new Tuple(message.getSenderId(), message.getMessageId());
        if(!this.delivered.contains(tuple))
        {
            this.delivered.add(tuple);
            this.deliverer.deliver(message);
            bestEffortBroadcast.broadcast(message);
        }
    }
}
