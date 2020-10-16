package cs451;

import cs451.utils.Process;
import cs451.utils.Message;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
        List<String> config = null;
        if (parser.hasConfig())
        {
            System.out.println("Config: " + parser.config());
            try (Stream<String> stream = Files.lines(Paths.get(parser.config())))
            {
                config = stream.collect(Collectors.toUnmodifiableList());
            } catch (IOException e)
            {
                e.printStackTrace();
            }
        }


        Coordinator coordinator = new Coordinator(parser.myId(), parser.barrierIp(), parser.barrierPort(), parser.signalIp(), parser.signalPort());

        System.out.println("Waiting for all processes for finish initialization");
        coordinator.waitOnBarrier();

        System.out.println("Broadcasting messages...");
        ///////////////// My stuff //////////////////
        int messageNumber = 0;

        if(config != null)
        {
            messageNumber = Integer.parseInt(config.get(0));
        }

        Message[] messages = new Message[messageNumber];
        for (int i = 0; i < messages.length; i++)
        {
            messages[i] = new Message("a", i + 1, parser.myId(), 0);
        }

        process = new Process(parser.myId(), parser.hosts(), parser.output());

        for (Message message : messages)
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
