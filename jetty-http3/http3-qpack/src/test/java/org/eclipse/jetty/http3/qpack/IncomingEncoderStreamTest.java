package org.eclipse.jetty.http3.qpack;

import java.nio.ByteBuffer;

import org.eclipse.jetty.util.BufferUtil;
import org.eclipse.jetty.util.TypeUtil;
import org.junit.jupiter.api.Test;

public class IncomingEncoderStreamTest
{
    @Test
    public void test()
    {
        DecoderInstructionParser incomingEncoderStream = new DecoderInstructionParser();

        // Example from the spec, section acknowledgement instruction with stream id 4.
        String encoded = "84";
        ByteBuffer buffer = BufferUtil.toBuffer(TypeUtil.fromHexString(encoded));
        incomingEncoderStream.parse(buffer);

        // 1111 1110 == FE is largest value we can do without continuation should be stream ID 126.
        encoded = "FE";
        buffer = BufferUtil.toBuffer(TypeUtil.fromHexString(encoded));
        incomingEncoderStream.parse(buffer);

        // 1111 1111 0000 0000 == FF00 is next value, stream id 127.
        encoded = "FF00";
        buffer = BufferUtil.toBuffer(TypeUtil.fromHexString(encoded));
        incomingEncoderStream.parse(buffer);

        // 1111 1111 0000 0001 == FF01 is next value, stream id 128.
        encoded = "FF01";
        buffer = BufferUtil.toBuffer(TypeUtil.fromHexString(encoded));
        incomingEncoderStream.parse(buffer);


        // 1111 1111 | 1100 0000 | 0000 0001 == FFC001, has continuation, stream id 128.
        // Extension bits go from least significant to most. C0 is least 01 is most so << 8
        // value should be 0140 + 2^n - 1 = 320 + 127 =
        encoded = "FFC001";
        buffer = BufferUtil.toBuffer(TypeUtil.fromHexString(encoded));
        incomingEncoderStream.parse(buffer);
    }
}
