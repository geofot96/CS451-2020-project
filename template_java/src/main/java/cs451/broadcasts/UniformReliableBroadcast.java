package cs451.broadcasts;

import cs451.Host;
import cs451.utils.Deliverer;
import cs451.utils.Message;
import cs451.utils.Tuple;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Class name: UniformReliableBroadcast.java
 * Created by: George Fotiadis
 * Date: 15/10/2020 at 15:59
 **/
public class UniformReliableBroadcast implements Deliverer, Broadcast
{
    private Deliverer deliverer;
    private BestEffortBroadcast bestEffortBroadcast;
    private Set<Tuple<Integer, Integer>> delivered; //original sender id, message id
    private Map<Tuple<Integer, Integer>, Message> pending; //original sender id, message id, Message
    private Map<Tuple<Integer, Integer>, Set<Integer>> acks; //original sender id, message id, processes that have relayed
    private double maxNumberOfAcks;
    private int myHost;

    public UniformReliableBroadcast(Deliverer deliverer, List<Host> hosts, int myPort, int myHost)
    {
        this.deliverer = deliverer;
        this.bestEffortBroadcast = new BestEffortBroadcast(this, hosts, myPort);
        this.delivered = ConcurrentHashMap.newKeySet();
        this.maxNumberOfAcks = hosts.size();
        this.pending = new ConcurrentHashMap();
        this.acks = new ConcurrentHashMap<>();
        this.myHost = myHost;
    }

    @Override
    public void broadcast(Message message)
    {
        Tuple<Integer, Integer> tuple = new Tuple(myHost, message.getMessageId());
        pending.put(tuple, message);
        this.bestEffortBroadcast.broadcast(message);
    }

    @Override
    public void deliver(Message message)
    {
        Tuple<Integer, Integer> tuple = new Tuple(message.getoriginalSenderId(), message.getMessageId());

        if(!this.acks.containsKey(tuple))
        {
            this.acks.put(tuple, ConcurrentHashMap.newKeySet());
        }

        this.acks.get(tuple).add(message.getRelaySenderId());

        relay(message);


        for(Tuple<Integer, Integer> tup : pending.keySet())
        {
            if(canDeliver(tup) && !delivered.contains(tup))
            {
                delivered.add(tuple);
                this.deliverer.deliver(message);
            }
        }

    }

    private boolean canDeliver(Tuple<Integer, Integer> tuple)
    {
        if(this.acks.containsKey(tuple))
        {
            return this.acks.get(tuple).size() >= maxNumberOfAcks / 2.0;
        }
        else
        {
            return false;
        }
    }

    private void relay(Message message)
    {
        Tuple tuple = new Tuple(message.getoriginalSenderId(), message.getMessageId());
        if(!pending.containsKey(tuple))
        {
            pending.put(tuple, message);
            message.setRelaySenderId(myHost);
            this.bestEffortBroadcast.broadcast(message);
        }
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
