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
    private Set<Integer> delivered; //sender id, message id
    private Set<Tuple<Integer, Integer>> pending; //sender id, message id
    private Map<Integer, Set<Integer>> acks; //message id, processes that have acked
    private double maxNumberOfAcks;

    public UniformReliableBroadcast(Deliverer deliverer, List<Host> hosts, int myPort)
    {
        this.deliverer = deliverer;
        this.bestEffortBroadcast = new BestEffortBroadcast(this, hosts, myPort);
        this.delivered = ConcurrentHashMap.newKeySet();
        this.maxNumberOfAcks = hosts.size();
        this.pending = ConcurrentHashMap.newKeySet();
        this.acks = new ConcurrentHashMap<>();
    }

    @Override
    public void broadcast(Message message)
    {
        Tuple tuple = new Tuple(message.getSenderId(), message.getMessageId());
        pending.add(tuple);
        this.bestEffortBroadcast.broadcast(message);
    }

    @Override
    public void deliver(Message message)
    {
        if(!this.acks.containsKey(message.getMessageId()))
        {
            this.acks.put(message.getMessageId(), ConcurrentHashMap.newKeySet());
        }
        this.acks.get(message.getMessageId()).add(message.getSenderId());
        relay(message);
       // System.out.println("Trying to deliver message with id" + message.getMessageId() + " from process " + message.getSenderId());
        if(this.acks.get(message.getMessageId()).size() > maxNumberOfAcks / 2.0)
        {
            if(!delivered.contains(message.getMessageId()))
            {
                delivered.add(message.getMessageId());
                this.deliverer.deliver(message);
            }
        }
    }

    private void relay(Message message)
    {
        Tuple tuple = new Tuple(message.getSenderId(), message.getMessageId());
        if(!pending.contains(tuple))
        {
            pending.add(tuple);
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
