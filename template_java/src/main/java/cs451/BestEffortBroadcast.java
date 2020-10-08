package cs451;

import java.io.*;
import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * Class name: BestEffortBroadcast.java
 * Created by: George Fotiadis
 * Date: 01/10/2020 at 14:17
 **/
public class BestEffortBroadcast
{
    private Process process;
    private Parser parser;
    private int myId;
    private Host myHost;
    private int messageId;

    public BestEffortBroadcast(Parser parser)
    {
        this.parser = parser;
        this.myId = this.parser.myId();
        this.myHost = this.parser.hostsMap().get(myId);
        for (Host host : this.parser.hosts())
        {
            if(host.getId() == myId){
                myHost = host;
            }
        }
        InetAddress myIp = null;

        try
        {
            myIp = InetAddress.getByName(myHost.getIp());
        } catch (UnknownHostException e)
        {
            e.printStackTrace();
        }

        this.process  = new Process(myIp, myHost.getPort(), myHost.getId(), this.parser.hostsMap(), this.parser.output());
        this.messageId = 1;
    }

    public void broadcast(String[] message)
    {
        for (Host host : this.parser.hosts())
        {
            int receiverId = host.getId();
            if(receiverId != this.myId)
            {
                for(String m : message)
                {
                    this.process.sendMessage(m, receiverId, messageId);
                    messageId += 1;
                    try
                    {
                        writeOutput(parser.output());
                    } catch (IOException e)
                    {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    private void writeOutput(String outputFilePath) throws IOException
    {
        BufferedWriter bw = new BufferedWriter(new FileWriter(outputFilePath, true));

        String output = "b " + messageId;

        for(int i = 0; i < output.length(); i++){
            bw.append(output.charAt(i));
        }
        bw.newLine();


        bw.close();
    }
}
