package cs451;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

/**
 * Class name: Listener.java
 * Created by: George Fotiadis
 * Date: 29/09/2020 at 11:51
 **/
public class Listener extends Thread
{
    private DatagramSocket socket;
    private int messageLength = 65535;

    public Listener(DatagramSocket socket)
    {
        this.socket = socket;
    }

    @Override
    public void run()
    {
        DatagramSocket socket = this.socket;
        byte[] receive = new byte[this.messageLength];
        DatagramPacket dpReceive = null;
        System.out.println("RECEIVING MESSAGE IN LISTENER");
        while (true)
        {
            dpReceive = new DatagramPacket(receive, receive.length);

            try
            {
                socket.receive(dpReceive);
            } catch (IOException e)
            {
                e.printStackTrace();
            }
            InetAddress senderIp = dpReceive.getAddress();
            Integer senderPort = dpReceive.getPort();
            byte[] msgBytes = dpReceive.getData();

            String message = bytesToString(msgBytes);

            System.out.println("THE MESSAGE WAS " + message);
        }
    }

    private static String bytesToString(byte[] a)
    {
        if (a == null)
            return null;
        StringBuilder ret = new StringBuilder();
        int i = 0;
        while (a[i] != 0)
        {
            ret.append((char) a[i]);
            i++;
        }
        return ret.toString();
    }
}
