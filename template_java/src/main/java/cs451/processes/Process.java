package cs451.processes;
import cs451.Host;
import cs451.utils.Message;
import cs451.broadcasts.BestEffortBroadcast;
import cs451.utils.Deliverer;

import java.io.*;
import java.util.*;


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
    private BestEffortBroadcast bestEffortBroadcast;
    private String outputPath;
    private List<String> logs;
    private HashSet<Integer> delivered;

    public Process(int processId, List<Host> hosts, String outputPath)
    {
        this.processId = processId;
        this.hosts = hosts;
        getMyHost();
        this.bestEffortBroadcast = new BestEffortBroadcast(this, this.hosts, myHost.getPort());
        System.out.println("My port is " + myHost.getPort());
        this.outputPath = outputPath;
        this.logs = new ArrayList<>();
        this.delivered = new HashSet<>();
    }

    public void broadcast(Message message)
    {
        this.logs.add("b " + message.getMessageId());
        this.bestEffortBroadcast.broadcast(message);
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
            logs.add("d " + message.getSenderId() + " " + message.getMessageId());
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
