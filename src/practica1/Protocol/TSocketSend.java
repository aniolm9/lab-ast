package practica1.Protocol;

import ast.protocols.tcp.TCPSegment;
import utils.Channel;
import java.util.Arrays;

public class TSocketSend {
    private final Channel channel;

    public TSocketSend(Channel channel) {
        this.channel = channel;
    }

    public void sendData(byte[] data, int offset, int length) {
        //throw new RuntimeException("Aquest mètode s'ha de completar...");
        byte[] data_copy = new byte[data.length];
        TCPSegment segment = new TCPSegment();
        System.arraycopy(data, offset, data_copy, offset, length);
        segment.setData(data_copy, offset, length);
        channel.send(segment);
    }
}
