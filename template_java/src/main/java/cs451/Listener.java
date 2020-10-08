package cs451;

import java.io.*;
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
    private Message message = null;
    private String outputFilePath;

    public Listener(DatagramSocket socket, String outputFilePath)
    {
        this.socket = socket;
        this.outputFilePath = outputFilePath;
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
            this.message = message;
            //message.print();

            if(!this.message.isAck){
                System.out.println("Sending ACK for message " + this.message.messageId + " with content " + this.message.getMessage());
                sendAck();
            }
            else{
                System.out.println("Received ACK for message " + this.message.messageId + " from process " + this.message.getSourceId());
                this.message.addProcessThatDelivered(this.message.messageId);
                this.message.hostsThatHaveAcked += 1;
                if(this.message.hostsThatHaveAcked > this.message.getHostsThatNeedToAck()){
                    deliver();
                }
            }
        }
    }

    private void deliver(){
        System.out.println("DELIVERING MESSAGE " + this.message.getMessage());
        try
        {
            writeOutput();
        } catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    private void sendAck()
    {
        Message ack = new Message("ack",
                this.message.getSourcePort(),
                this.message.getSourceIp(),
                this.message.getSourceId(),
                this.message.getDestinationPort(),
                this.message.getDestinationIp(),
                this.message.getSourceId(),
                this.message.messageId,
                true,
                this.message.getHostsThatNeedToAck());
        ack.send(this.socket);
    }

    private void writeOutput() throws IOException
    {
        BufferedWriter bw = new BufferedWriter(new FileWriter(this.outputFilePath, true));

        String output = "d " + (int) this.message.getHostsThatNeedToAck() * 2 + " " + this.message.messageId;
        for(int i = 0; i < output.length(); i++){
            bw.append(output.charAt(i));
        }
        bw.newLine();

        bw.close();
    }
}
