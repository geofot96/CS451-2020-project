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
    private static final int messageLength = 65535;

    public Listener(DatagramSocket socket)
    {
        this.socket = socket;
    }

    @Override
    public void run()
    {
        DatagramSocket socket = this.socket;
        byte[] compressedMessage = new byte[this.messageLength];
        DatagramPacket receivedPacket = null;
        System.out.println("RECEIVING MESSAGE IN LISTENER");
        while (true)
        {
            receivedPacket = new DatagramPacket(compressedMessage, compressedMessage.length);

            try
            {
                socket.receive(receivedPacket);
            } catch (IOException e)
            {
                e.printStackTrace();
            }
            byte[] messageBytes = receivedPacket.getData();

            Message message = null;
            ByteArrayInputStream bis = new ByteArrayInputStream(messageBytes);
            ObjectInputStream ois = null;
            try
            {
                ois = new ObjectInputStream(bis);
            } catch (IOException e)
            {
                e.printStackTrace();
            }
            try
            {
                message = (Message) ois.readObject();
            } catch (IOException e)
            {
                e.printStackTrace();
            } catch (ClassNotFoundException e)
            {
                e.printStackTrace();
            }
            message.print();
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
