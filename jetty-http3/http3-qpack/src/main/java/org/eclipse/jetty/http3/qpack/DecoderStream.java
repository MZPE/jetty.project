package org.eclipse.jetty.http3.qpack;

public abstract class DecoderStream
{
    abstract void sendSectionAcknowledgment(int streamId);
    abstract void sendStreamCancellation(int streamId);
    abstract void sendInsertCountIncrement(int increment);
}
