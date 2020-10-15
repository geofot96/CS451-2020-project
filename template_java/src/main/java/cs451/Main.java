package cs451;

import cs451.processes.Process;
import cs451.utils.Message;

import java.io.IOException;

public class Main
{
    private static Process process;

    private static void handleSignal()
    {
        //immediately stop network packet processing
        System.out.println("Immediately stopping network packet processing.");

        //write/flush output file if necessary
        System.out.println("Writing output.");
        try
        {
            process.writeOutput();
        } catch (IOException e)
        {
            e.printStackTrace();
        }
        System.out.println("Done.");
    }

    private static void initSignalHandlers()
    {
        Runtime.getRuntime().addShutdownHook(new Thread()
        {
            @Override
            public void run()
            {
                handleSignal();
            }
        });
    }

    public static void main(String[] args) throws InterruptedException
    {
        Parser parser = new Parser(args);
        parser.parse();

        initSignalHandlers();

        // example
        long pid = ProcessHandle.current().pid();
        System.out.println("My PID is " + pid + ".");
        System.out.println("Use 'kill -SIGINT " + pid + " ' or 'kill -SIGTERM " + pid + " ' to stop processing packets.");

        System.out.println("My id is " + parser.myId() + ".");
        System.out.println("List of hosts is:");
        for (Host host : parser.hosts())
        {
            System.out.println(host.getId() + ", " + host.getIp() + ", " + host.getPort());
        }

        System.out.println("Barrier: " + parser.barrierIp() + ":" + parser.barrierPort());
        System.out.println("Signal: " + parser.signalIp() + ":" + parser.signalPort());
        System.out.println("Output: " + parser.output());
        // if config is defined; always check before parser.config()
        if (parser.hasConfig())
        {
            System.out.println("Config: " + parser.config());
        }


        Coordinator coordinator = new Coordinator(parser.myId(), parser.barrierIp(), parser.barrierPort(), parser.signalIp(), parser.signalPort());

        System.out.println("Waiting for all processes for finish initialization");
        coordinator.waitOnBarrier();

        System.out.println("Broadcasting messages...");
        ///////////////// My stuff //////////////////

        Message[] messages = new Message[2];
        messages[0] = new Message("message 1", 1, parser.myId());
        messages[1] = new Message("message 2", 2, parser.myId());

        process = new Process(parser.myId(), parser.hosts(), parser.output());
        System.out.println("MY ID IS " + parser.myId());

        for(Message message: messages)
        {
            process.broadcast(message);
        }

        ////////////////////////////////////////////
        System.out.println("Signaling end of broadcasting messages");
        coordinator.finishedBroadcasting();

        while (true)
        {
            // Sleep for 1 hour
            Thread.sleep(60 * 60 * 1000);
        }
    }
}
