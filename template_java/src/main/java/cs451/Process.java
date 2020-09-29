package cs451;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.List;


/**
 * Class name: Process.java
 * Created by: George Fotiadis
 * Date: 25/09/2020 at 21:06
 **/
public class Process extends Thread
{
    private DatagramSocket socket;
    private InetAddress ip;
    private int port;
    private int processId;
    private HashMap<Integer, Host> hosts = new HashMap<>();;
    private Listener myListener;
    private static int messageId;

    public Process(InetAddress ip, int port, int processId, HashMap<Integer, Host> hosts)
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

        myListener = new Listener(this.socket);
        myListener.start();
    }

    public void sendMessage(String message, int receiverId)
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

        Message message1 = new Message(message, receiver.getPort(), receiverIp, this.port, this.ip, this.messageId, this.socket);

        System.out.println("SENDING MESSAGE TO " + receiverId);

        message1.send(this.socket);

        this.messageId += 1;
    }
}
