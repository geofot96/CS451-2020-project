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
        this.broadcast = new FIFOBroadcast(this, this.hosts, myHost.getPort(), myHost.getId());
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
        logs.add("d " + message.getoriginalSenderId() + " " + message.getMessageId());
        countDelivered += 1;
        System.out.println("Delivered "+ (countDelivered - 1) + "th message");
        if(countDelivered - 1 == 4000)
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