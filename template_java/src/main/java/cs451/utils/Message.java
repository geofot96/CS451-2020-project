package cs451.utils;

import java.io.Serializable;
/**
 * Class name: Message.java
 * Created by: George Fotiadis
 * Date: 25/09/2020 at 21:11
 **/
public class Message implements Serializable
{
    private String message;
    private int messageId;
    private int originalSenderId;
    private int lsn;
    private int relaySenderId;

    public Message(String message, int messageId, int originalSenderId, int lsn)
    {
        this.message = message;
        this.messageId = messageId;
        this.originalSenderId = originalSenderId;
        this.lsn = lsn;
        this.relaySenderId = originalSenderId;
    }

    public String getMessage()
    {
        return message;
    }

    public int getMessageId()
    {
        return messageId;
    }

    public int getoriginalSenderId()
    {
        return originalSenderId;
    }

    public int getLsn()
    {
        return lsn;
    }

    public void setLsn(int lsn)
    {
        this.lsn = lsn;
    }

    public void setoriginalSenderId(int originalSenderId)
    {
        this.originalSenderId = originalSenderId;
    }

    public int getRelaySenderId()
    {
        return relaySenderId;
    }

    public void setRelaySenderId(int relaySenderId)
    {
        this.relaySenderId = relaySenderId;
    }

    @Override
    public String toString()
    {
        return "Message{" +
                "message='" + message + '\'' +
                ", messageId=" + messageId +
                ", originalSenderId=" + originalSenderId +
                ", lsn=" + lsn +
                ", relaySenderId=" + relaySenderId +
                '}';
    }
}
