package cs451.broadcasts;

import cs451.Host;
import cs451.utils.Deliverer;
import cs451.utils.Message;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicIntegerArray;

/**
 * Class name: FIFOBroadcast.java
 * Created by: George Fotiadis
 * Date: 16/10/2020 at 10:45
 **/
public class FIFOBroadcast implements Deliverer, Broadcast
{
    private Deliverer deliverer;
    private UniformReliableBroadcast uniformReliableBroadcast;
    private int lsn;
    private Set<Message> pending; // messages urb delivered but waiting to be fifo delivered
    private AtomicIntegerArray next; //an entry for every process p with the sequence number of the next message to be frb-delivered from sender p

    public FIFOBroadcast(Deliverer deliverer, List<Host> hosts, int myPort)
    {
        this.deliverer = deliverer;
        this.uniformReliableBroadcast = new UniformReliableBroadcast(this, hosts, myPort);
        this.lsn = 0;
        this.pending = ConcurrentHashMap.newKeySet();
        int[] tempNext = new int[hosts.size() + 1];
        Arrays.fill(tempNext, 1);
        this.next = new AtomicIntegerArray(tempNext);
    }

    @Override
    public void broadcast(Message message)
    {
        this.lsn += 1;
        message.setLsn(this.lsn);
        uniformReliableBroadcast.broadcast(message);
    }

    @Override
    public void deliver(Message message)
    {
        this.pending.add(message);
        for(Iterator<Message> i = this.pending.iterator(); i.hasNext();)
        {
            Message m = i.next();
            //System.out.println("Stuck here with message " + m.getMessageId());
            if(m.getLsn() == this.next.get(message.getSenderId()))
            {
                System.out.println("Delivering message with id" + m.getMessageId() + " from process " + m.getSenderId());
                next.set(message.getSenderId(), next.get(message.getSenderId()) + 1);
                pending.remove(m);
                this.deliverer.deliver(m);
            }
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
