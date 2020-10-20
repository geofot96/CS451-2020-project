package cs451.utils;
import cs451.Host;
import cs451.broadcasts.*;
import cs451.utils.Message;
import cs451.utils.Deliverer;

import java.io.*;
import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;


/**
 * Class name: Process.java
 * Created by: George Fotiadis
 * Date: 25/09/2020 at 21:06
 **/
public class Process extends Thread implements Deliverer
{
    private int processId;
    private List<Host> hosts;
    private Host myHost;
    private Broadcast broadcast;
    private String outputPath;
    private ConcurrentLinkedQueue<String> logs;

    private int countDelivered;

    public Process(int processId, List<Host> hosts, String outputPath)
    {
        this.processId = processId;
        this.hosts = hosts;
        getMyHost();
        this.broadcast = new UniformReliableBroadcast(this, this.hosts, myHost.getPort());
        this.outputPath = outputPath;
        this.logs = new ConcurrentLinkedQueue<>();
        this.countDelivered += 1;
    }

    public void broadcast(Message message)
    {
        this.logs.add("b " + message.getMessageId());
        System.out.println("Broadcasting message " + message.getMessageId());

        this.broadcast.broadcast(message);
    }

    private void getMyHost()
    {
        for(Host host: hosts)
        {
            if(host.getId() == this.processId)
            {
                this.myHost = host;
                return;
            }
        }
    }

    @Override
    public void deliver(Message message)
    {
        System.out.println("Delivering message " + message.getMessageId());
        logs.add("d " + message.getSenderId() + " " + message.getMessageId());
        countDelivered += 1;
        System.out.println("Delivered "+ (countDelivered - 1) + "th message");
        if(countDelivered == 1000)
        {
            System.out.println("DONE DELIVERING");
        }
    }

    public void writeOutput() throws IOException
    {
        File fout = new File(outputPath);
        FileOutputStream fos = new FileOutputStream(fout);

        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos));

        for (String str : logs) {
            bw.write(str);
            bw.newLine();
        }

        bw.close();
    }

}

















/*
private DatagramSocket socket;
    private InetAddress ip;
    private int port;
    private int processId;
    private HashMap<Integer, Host> hosts = new HashMap<>();;
    private Listener myListener;
    private static int messageId;

    public Process(InetAddress ip, int port, int processId, HashMap<Integer, Host> hosts, String outputFilePath)
    {
        // Initialize the variables
        this.ip = ip;
        this.port = port;
        this.processId = processId;
        try
        {
            this.socket = new DatagramSocket(this.port, this.ip);
        } catch (SocketException e)
        {
            e.printStackTrace();
        }
        this.hosts = hosts;
        this.messageId = 1;

        myListener = new Listener(this.socket, outputFilePath);

        System.out.println("Hosts on the system: " + this.hosts.values());

        myListener.start();
    }

    public void sendMessage(String message, int receiverId, int messageId)
    {
        Host receiver = this.hosts.get(receiverId);

        InetAddress receiverIp = null;

        try
        {
            receiverIp = InetAddress.getByName(receiver.getIp());
        } catch (UnknownHostException e)
        {
            e.printStackTrace();
        }

        double hostsThatNeedToAck =this.hosts.values().size() / 2.0;

        Message message1 = new Message(message,
                receiver.getPort(),
                receiverIp,
                receiverId,
                this.port,
                this.ip,
                this.processId,
                messageId,
                false,
                hostsThatNeedToAck);

        System.out.println("SENDING MESSAGE TO " + receiverId);

        message1.send(this.socket);

        this.messageId += 1;
    }
 */
