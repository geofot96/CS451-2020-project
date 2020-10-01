package cs451;

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

        this.process  = new Process(myIp, myHost.getPort(), myHost.getId(), this.parser.hostsMap());
    }

    //TODO make argument a list of strings
    public void broadcast(String message)
    {
        for (Host host : this.parser.hosts())
        {
            int receiverId = host.getId();
            if(receiverId != this.myId)
            {
                this.process.sendMessage(message, receiverId);
//                this.process.sendMessage("this is a second potato from process " + this.myHost.getId(), receiverId);
            }
        }
    }
}
