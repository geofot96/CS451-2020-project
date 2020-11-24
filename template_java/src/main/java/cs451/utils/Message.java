package cs451.utils;

import java.io.Serializable;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

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
    private Map<Integer, Integer> vClock; // for causal

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

    public int getRelaySenderId()
    {
        return relaySenderId;
    }

    public void setRelaySenderId(int relaySenderId)
    {
        this.relaySenderId = relaySenderId;
    }

    public Map<Integer, Integer> getvClock()
    {
        return vClock;
    }

    public void setvClock(Map<Integer, Integer> vClock)
    {
        this.vClock = new ConcurrentHashMap<>(vClock);
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Message message1 = (Message) o;
        return messageId == message1.messageId &&
                originalSenderId == message1.originalSenderId &&
                lsn == message1.lsn &&
                relaySenderId == message1.relaySenderId &&
                Objects.equals(message, message1.message);
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(message, messageId, originalSenderId, lsn, relaySenderId);
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
