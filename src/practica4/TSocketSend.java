package practica4;

import ast.protocols.tcp.TCPSegment;

/**
 * @author AST's teachers
 */
public class TSocketSend extends TSocketBase {

    protected int sndMSS;       // Send maximum segment size

    /**
     * Create an endpoint bound to the local IP address and the given TCP port. The local IP address is determined by the
     * networking system.
     *
     * @param ch
     */
    protected TSocketSend(ProtocolSend p, int localPort, int remotePort) {
        super(p, localPort, remotePort);
        sndMSS = /*p.channel.getMMS()*/ 50 - TCPSegment.HEADER_SIZE; // IP maximum message size - TCP header size
    }

    public void sendData(byte[] data, int offset, int length) {
        int dataLength = length;
        //System.out.println("sendData length: " + length);
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
        // This is new.
        segment.setDestinationPort(remotePort);
        segment.setSourcePort(localPort);
        return segment;
    }

    protected void sendSegment(TCPSegment segment) {
        //System.out.println("Send segment: " + segment.getDataLength());
        proto.channel.send(segment);
    }
}
