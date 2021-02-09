package org.eclipse.jetty.http3.qpack;

import java.nio.ByteBuffer;

import org.eclipse.jetty.util.BufferUtil;

/**
 * Decode instructions coming from the remote Encoder as a sequence of unframed instructions.
 */
public class IncomingEncoderStream
{
    private static final int SECTION_ACKNOWLEDGEMENT_PREFIX = 7;
    private static final int STREAM_CANCELLATION_PREFIX = 6;
    private static final int INSERT_COUNT_INCREMENT_PREFIX = 6;

    public void parse(ByteBuffer buffer)
    {
        // Get first byte without incrementing the buffers position.
        byte firstByte = buffer.slice().get();

        if ((firstByte & 0x80) != 0)
            decodeSectionAcknowledgment(buffer);
        else if ((firstByte & 0x40) != 0)
            decodeStreamCancellation(buffer);
        else
            decodeInsertCountIncrement(buffer);
    }

    private void decodeSectionAcknowledgment(ByteBuffer buffer)
    {
        System.err.println("decodeSectionAcknowledgment() " + BufferUtil.toDetailString(buffer));

        int streamId = NBitInteger.decode(buffer, SECTION_ACKNOWLEDGEMENT_PREFIX);
        System.err.println("decoded stream ID: " + streamId);
    }

    private void decodeStreamCancellation(ByteBuffer buffer)
    {
        System.err.println("decodeStreamCancellation() " + BufferUtil.toDetailString(buffer));
    }

    private void decodeInsertCountIncrement(ByteBuffer buffer)
    {
        System.err.println("decodeInsertCountIncrement() " + BufferUtil.toDetailString(buffer));
    }
}
