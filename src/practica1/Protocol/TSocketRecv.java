package practica1.Protocol;

import ast.protocols.tcp.TCPSegment;
import utils.Channel;

public class TSocketRecv {

    private final Channel channel;

    public TSocketRecv(Channel channel) {
        this.channel = channel;
    }

    public int receiveData(byte[] data, int offset, int length) {
        //throw new RuntimeException("Aquest mètode s'ha de completar...");
        TCPSegment segment = this.channel.receive();
        int min_length = Math.min(length, segment.getDataLength());
        System.arraycopy(segment.getData(), segment.getDataOffset(), data, offset, min_length);
        return min_length;
    }
}
