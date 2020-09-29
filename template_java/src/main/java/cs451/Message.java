package cs451;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

/**
 * Class name: Message.java
 * Created by: George Fotiadis
 * Date: 25/09/2020 at 21:11
 **/
public class Message implements Serializable
{
    private String message;
    private int destinationPort;
    private InetAddress destinationIp;
    private int sourcePort;
    private InetAddress sourceIp;
    private static int messageId;

    public Message(String message, int destinationPort, InetAddress destinationIp, int sourcePort, InetAddress sourceIp, int messageId, DatagramSocket socket)
    {
        this.message = message;
        this.destinationPort = destinationPort;
        this.destinationIp = destinationIp;
        this.sourcePort = sourcePort;
        this.sourceIp = sourceIp;
        this.messageId = messageId;
    }

    private byte[] compressMessage()
    {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        ObjectOutputStream objectStream = null;
        try
        {
            objectStream = new ObjectOutputStream(output);
            objectStream.writeObject(this);
            objectStream.flush();
        } catch (IOException e)
        {
            System.out.println("Unable to create output stream.");
        }
        return output.toByteArray();
    }

    public void send(DatagramSocket socket)
    {
        byte[] data = compressMessage();
        DatagramPacket piPacket = new DatagramPacket(data, data.length, this.destinationIp, this.destinationPort);

        // Send packet
        try
        {
            //DatagramSocket socket = new DatagramSocket(this.sourcePort, this.sourceIp);
            socket.send(piPacket);
        } catch (IOException e)
        {
            System.out.println(e);
            System.out.println("Unable to send message.");
        }
    }

    public void print()
    {
        System.out.println("Content : " + message);
        System.out.println("destinationPort : " + destinationPort);
        System.out.println("destinationIp : " + destinationIp);
        System.out.println("sourcePort : " + sourcePort);
        System.out.println("sourceIp : " + sourceIp);
        System.out.println("messageId : " + messageId);
        //System.out.println("socket : " + socket);
    }
}
