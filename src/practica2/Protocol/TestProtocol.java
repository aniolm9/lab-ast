package practica2.Protocol;

import practica1.Protocol.QueueChannel;
import utils.Channel;

public class TestProtocol {

    public static void main(String[] args) throws InterruptedException {
        //Channel channel = new QueueChannel(4);
        Channel channel = new MonitorChannel(4);

        Sender sender = new Sender(channel, 5, 2);
        Receiver receiver = new Receiver(channel, 10);
        Thread s1 = new Thread(sender);
        Thread r1 = new Thread(receiver);
        
        s1.start();
        r1.start();
        
        s1.join();
        r1.join();
        
        System.out.println("\nSimulation end.");
    }
}
