package cs451.utils;

import cs451.Message;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.*;

/**
 * Class name: UDPWrapper.java
 * Created by: George Fotiadis
 * Date: 08/10/2020 at 18:10
 **/
public class UDPWrapper
{
    private DatagramSocket socket;
    private InetAddress destinationIp;
    private int destinationPort;
    private Message message;

    public UDPWrapper(String destinationIp, int destinationPort, Message message)
    {
        try
        {
            this.destinationIp = InetAddress.getByName(destinationIp);
            this.destinationPort = destinationPort;
            this.message = message;
            this.socket = new DatagramSocket();
        } catch (UnknownHostException | SocketException e)
        {
            e.printStackTrace();
        }
    }

    public void send()
    {
        byte[] data = compressMessage();
        DatagramPacket piPacket = new DatagramPacket(data, data.length, this.destinationIp, this.destinationPort);

        // Send packet
        try
        {
            //DatagramSocket socket = new DatagramSocket(this.sourcePort, this.sourceIp);
            this.socket.send(piPacket);
        } catch (IOException e)
        {
            System.out.println(e);
            System.out.println("Unable to send message.");
        }
    }

    private byte[] compressMessage()
    {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        ObjectOutputStream objectStream = null;
        try
        {
            objectStream = new ObjectOutputStream(output);
            objectStream.writeObject(this.message);
            objectStream.flush();
        } catch (IOException e)
        {
            System.out.println("Unable to create output stream.");
        }
        return output.toByteArray();
    }
}
