//
// ========================================================================
// Copyright (c) 1995-2020 Mort Bay Consulting Pty Ltd and others.
//
// This program and the accompanying materials are made available under
// the terms of the Eclipse Public License 2.0 which is available at
// https://www.eclipse.org/legal/epl-2.0
//
// This Source Code may also be made available under the following
// Secondary Licenses when the conditions for such availability set
// forth in the Eclipse Public License, v. 2.0 are satisfied:
// the Apache License v2.0 which is available at
// https://www.apache.org/licenses/LICENSE-2.0
//
// SPDX-License-Identifier: EPL-2.0 OR Apache-2.0
// ========================================================================
//

package org.eclipse.jetty.websocket.util.messages;

import java.io.IOException;
import java.lang.invoke.MethodHandle;
import java.nio.ByteBuffer;

import org.eclipse.jetty.util.Callback;
import org.eclipse.jetty.util.Utf8StringBuilder;
import org.eclipse.jetty.websocket.core.CoreSession;
import org.eclipse.jetty.websocket.core.Frame;
import org.eclipse.jetty.websocket.core.exception.MessageTooLargeException;

public class StringMessageSink extends AbstractMessageSink
{
    private static final int BUFFER_SIZE = 1024;
    private Utf8StringBuilder out;
    private int size;

    public StringMessageSink(CoreSession session, MethodHandle methodHandle)
    {
        super(session, methodHandle);
        this.size = 0;
    }

    @Override
    public void accept(Frame frame, Callback callback)
    {
        try
        {
            // If we are fin and out has not been created we don't need to aggregate.
            if (frame.isFin() && (out == null))
            {
                if (frame.hasPayload())
                    methodHandle.invoke(frame.getPayloadAsUTF8());
                else
                    methodHandle.invoke("");

                callback.succeeded();
                return;
            }

            aggregatePayload(frame);
            if (frame.isFin())
                methodHandle.invoke(out.toString());

            callback.succeeded();
        }
        catch (Throwable t)
        {
            callback.failed(t);
        }
        finally
        {
            if (frame.isFin())
            {
                // reset
                size = 0;
                out.reset();
                out = null;
            }
        }
    }

    private void aggregatePayload(Frame frame) throws IOException
    {
        if (frame.hasPayload())
        {
            ByteBuffer payload = frame.getPayload();
            size += frame.getPayloadLength();
            long maxTextMessageSize = session.getMaxTextMessageSize();
            if (maxTextMessageSize > 0 && size > maxTextMessageSize)
            {
                throw new MessageTooLargeException(String.format("Text message too large: (actual) %,d > (configured max text message size) %,d",
                    size, maxTextMessageSize));
            }

            if (out == null)
                out = new Utf8StringBuilder(BUFFER_SIZE);

            // allow for fast fail of BAD utf (incomplete utf will trigger on messageComplete)
            out.append(payload);
        }
    }
}
