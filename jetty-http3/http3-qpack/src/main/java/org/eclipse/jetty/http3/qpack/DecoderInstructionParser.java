package org.eclipse.jetty.http3.qpack;

import java.nio.ByteBuffer;

import org.eclipse.jetty.util.BufferUtil;

/**
 * Receives instructions coming from the remote Encoder as a sequence of unframed instructions.
 */
public class DecoderInstructionParser
{
    private static final int SECTION_ACKNOWLEDGEMENT_PREFIX = 7;
    private static final int STREAM_CANCELLATION_PREFIX = 6;
    private static final int INSERT_COUNT_INCREMENT_PREFIX = 6;

    // TODO: this should be able to handle incomplete instructions in the buffer.
    public void parse(ByteBuffer buffer)
    {
        // Get first byte without incrementing the buffers position.
        byte firstByte = buffer.slice().get();

        if ((firstByte & 0x80) != 0)
            parseSectionAcknowledgment(buffer);
        else if ((firstByte & 0x40) != 0)
            parseStreamCancellation(buffer);
        else
            parseInsertCountIncrement(buffer);
    }

    private void parseSectionAcknowledgment(ByteBuffer buffer)
    {
        System.err.println("decodeSectionAcknowledgment() " + BufferUtil.toDetailString(buffer));

        int streamId = decode(buffer, SECTION_ACKNOWLEDGEMENT_PREFIX);
        System.err.println("decoded stream ID: " + streamId);
    }

    private void parseStreamCancellation(ByteBuffer buffer)
    {
        System.err.println("decodeStreamCancellation() " + BufferUtil.toDetailString(buffer));

        int streamId = decode(buffer, STREAM_CANCELLATION_PREFIX);
        System.err.println("decoded stream ID: " + streamId);
    }

    private void parseInsertCountIncrement(ByteBuffer buffer)
    {
        System.err.println("decodeInsertCountIncrement() " + BufferUtil.toDetailString(buffer));

        int increment = decode(buffer, INSERT_COUNT_INCREMENT_PREFIX);
        System.err.println("increment: " + increment);
    }

    private static int decode(ByteBuffer buffer, int prefixLen)
    {
        // TODO: why do we need to do this???
        if (prefixLen != 8)
            buffer.position(buffer.position() + 1);
        return NBitInteger.decode(buffer, prefixLen);
    }
}
