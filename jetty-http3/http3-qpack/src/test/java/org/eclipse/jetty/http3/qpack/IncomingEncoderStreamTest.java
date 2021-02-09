package org.eclipse.jetty.http3.qpack;

import java.nio.ByteBuffer;

import org.eclipse.jetty.util.TypeUtil;
import org.junit.jupiter.api.Test;

public class IncomingEncoderStreamTest
{
    @Test
    public void test()
    {
        IncomingEncoderStream incomingEncoderStream = new IncomingEncoderStream();

        String encoded = "84";
        ByteBuffer buffer = ByteBuffer.wrap(TypeUtil.fromHexString(encoded));

        incomingEncoderStream.parse(buffer);
    }
}
