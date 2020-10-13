package cs451.links;

import cs451.Host;
import cs451.Message;
import cs451.utils.Consumer;
import cs451.utils.Deliverer;
import cs451.utils.UDPWrapper;

import java.net.DatagramSocket;

/**
 * Class name: FairLossLinks.java
 * Created by: George Fotiadis
 * Date: 08/10/2020 at 18:07
 **/
public class FairLossLinks implements Deliverer, Link
{
    private int port;
    private Deliverer deliverer;
    private Consumer consumer;

    public FairLossLinks(Deliverer deliverer, int port)
    {
        this.port = port;
        this.deliverer = deliverer;
        this.consumer = new Consumer(this.deliverer, this.port);
        this.start();
    }

    @Override
    public void send(Message message, Host host)
    {
        UDPWrapper connection = new UDPWrapper(host.getIp(), host.getPort(), message);
        connection.send();
    }

    @Override
    public void deliver(Message message)
    {
        this.deliverer.deliver(message);
    }

    @Override
    public void start()
    {
        this.consumer.start();
    }
}