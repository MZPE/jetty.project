package org.eclipse.jetty.http3.qpack;

import java.nio.ByteBuffer;

/**
 * Receives instructions coming from the remote Decoder as a sequence of unframed instructions.
 */
public class EncoderInstructionParser
{
    public void parse(ByteBuffer buffer)
    {
        byte firstByte = buffer.slice().get();

        if ((firstByte & 0x80) != 0)
        {
            boolean referenceDynamicTable = (firstByte & 0x40) != 0;

            parseInsertNameWithReference(buffer);
        }
        else if ((firstByte & 0x40) != 0)
            parseInsertWithLiteralName(buffer);
        else if ((firstByte & 0x20) != 0)
            parseSetDynamicTableCapacity(buffer);
        else
            parseDuplicate(buffer);
    }

    private void parseInsertNameWithReference(ByteBuffer buffer)
    {
    }

    private void parseInsertWithLiteralName(ByteBuffer buffer)
    {
    }

    private void parseSetDynamicTableCapacity(ByteBuffer buffer)
    {
    }

    private void parseDuplicate(ByteBuffer buffer)
    {
    }
}
