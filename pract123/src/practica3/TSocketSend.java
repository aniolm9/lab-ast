package practica3;

import ast.protocols.tcp.TCPSegment;
import utils.Channel;

public class TSocketSend extends TSocketBase {

    protected int sndMSS;       // Send maximum segment size

    public TSocketSend(Channel channel) {
        super(channel);
        //sndMSS = channel.getMSS();
        sndMSS = 3;
    }

    public void sendData(byte[] data, int offset, int length) {
        int dataLength = length;
        while (dataLength >= sndMSS) {
            this.sendSegment(this.segmentize(data, offset, sndMSS));
            offset += sndMSS;
            dataLength -= sndMSS;
        }
        if (dataLength > 0) this.sendSegment(this.segmentize(data, offset, dataLength));
    }

    protected TCPSegment segmentize(byte[] data, int offset, int length) {
        byte[] data_copy = new byte[length];
        TCPSegment segment = new TCPSegment();
        System.arraycopy(data, offset, data_copy, 0, length);
        segment.setData(data_copy, 0, length);
        return segment;
    }

    protected void sendSegment(TCPSegment segment) {
        channel.send(segment);
    }
}
