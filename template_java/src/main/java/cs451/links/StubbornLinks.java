package cs451.links;

import cs451.Host;
import cs451.Message;
import cs451.utils.Deliverer;
import cs451.utils.Tuple;

import java.util.HashSet;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Class name: StubbornLinks.java
 * Created by: George Fotiadis
 * Date: 13/10/2020 at 11:03
 **/
public class StubbornLinks implements Deliverer, Link
{
    private Deliverer deliverer;
    private Timer timer;
    private FairLossLinks fairLossLinks;
    private Set<Tuple<Message, Host>> sent;
    private final static int timeout = 500;

    public StubbornLinks(Deliverer deliverer, int port)
    {
        this.deliverer = deliverer;
        this.timer = new Timer();
        this.fairLossLinks = new FairLossLinks(this, port);
        this.sent = ConcurrentHashMap.newKeySet();

        timer.schedule(new TimerTask()
        {
            @Override
            public void run()
            {
                reTransmit();
            }
        }, timeout, timeout);

    }

    @Override
    public void start()
    {

    }

    @Override
    public void send(Message message, Host host)
    {
        this.fairLossLinks.send(message, host);
        this.sent.add(new Tuple<>(message, host));
    }

    @Override
    public void deliver(Message message)
    {
        this.deliverer.deliver(message);
    }

    private void reTransmit()
    {
        for (Tuple<Message, Host> tuple : this.sent)
        {
            send(tuple.first, tuple.second);
        }
    }
}
