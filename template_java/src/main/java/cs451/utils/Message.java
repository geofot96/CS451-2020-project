package cs451.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.HashSet;

/**
 * Class name: Message.java
 * Created by: George Fotiadis
 * Date: 25/09/2020 at 21:11
 **/
public class Message implements Serializable
{
    private String message;
    private int messageId;
    private int senderId;
    private int lsn;

    public Message(String message, int messageId, int senderId, int lsn)
    {
        this.message = message;
        this.messageId = messageId;
        this.senderId = senderId;
        this.lsn = lsn;

    }

    public String getMessage()
    {
        return message;
    }

    public int getMessageId()
    {
        return messageId;
    }

    public int getSenderId()
    {
        return senderId;
    }

    public int getLsn()
    {
        return lsn;
    }

    public void setLsn(int lsn)
    {
        this.lsn = lsn;
    }
}

/*
private String message;
    private int destinationPort;
    private InetAddress destinationIp;
    private int destinationId;
    private int sourcePort;
    private InetAddress sourceIp;
    private int sourceId;
    public int messageId;
    public boolean isAck;
    private HashSet<Integer> processesThatDelivered;
    private double hostsThatNeedToAck;
    public static double hostsThatHaveAcked;

    public Message(String message, int destinationPort, InetAddress destinationIp, int destinationId, int sourcePort, InetAddress sourceIp, int sourceId, int messageId, boolean isAck, double hostsThatNeedToAck)
    {
        this.message = message;
        this.destinationPort = destinationPort;
        this.destinationIp = destinationIp;
        this.destinationId = destinationId;
        this.sourcePort = sourcePort;
        this.sourceIp = sourceIp;
        this.sourceId = sourceId;
        this.messageId = messageId;
        this.isAck = isAck;
        this.processesThatDelivered = new HashSet<>();
        this.hostsThatNeedToAck = hostsThatNeedToAck;
        this.hostsThatHaveAcked = 0;
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

    public void addProcessThatDelivered(int processId){
        this.processesThatDelivered.add(processId);
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

    public int getDestinationPort()
    {
        return destinationPort;
    }

    public InetAddress getDestinationIp()
    {
        return destinationIp;
    }

    public int getSourcePort()
    {
        return sourcePort;
    }

    public InetAddress getSourceIp()
    {
        return sourceIp;
    }

    public int getDestinationId()
    {
        return destinationId;
    }

    public int getSourceId()
    {
        return sourceId;
    }

    public String getMessage()
    {
        return message;
    }

    public double getHostsThatNeedToAck()
    {
        return hostsThatNeedToAck;
    }
 */