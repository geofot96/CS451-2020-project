package cs451.utils;

import cs451.Message;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

/**
 * Class name: Consumer.java
 * Created by: George Fotiadis
 * Date: 08/10/2020 at 18:29
 **/
public class Consumer extends Thread
{
    private Deliverer deliverer;
    private DatagramSocket socket;

    public Consumer(Deliverer deliverer, int port)
    {
        this.deliverer = deliverer;
        try
        {
            this.socket = new DatagramSocket(port);
        } catch (SocketException e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public void run()
    {
        DatagramSocket socket = this.socket;
        byte[] compressedMessage = new byte[65535];
        DatagramPacket receivedPacket = null;
        while (true)
        {
            receivedPacket = new DatagramPacket(compressedMessage, compressedMessage.length);

            try
            {
                socket.receive(receivedPacket);
                byte[] messageBytes = receivedPacket.getData();

                Message message = null;
                ByteArrayInputStream bis = new ByteArrayInputStream(messageBytes);
                ObjectInputStream ois = null;
                ois = new ObjectInputStream(bis);
                message = (Message) ois.readObject();
                this.deliverer.deliver(message);
            } catch (IOException e)
            {
                e.printStackTrace();
            } catch (ClassNotFoundException e)
            {
                e.printStackTrace();
            }
        }
    }
}
