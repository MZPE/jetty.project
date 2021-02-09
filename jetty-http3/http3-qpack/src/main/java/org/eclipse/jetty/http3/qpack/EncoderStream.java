package org.eclipse.jetty.http3.qpack;

public interface EncoderStream
{
    void setCapacity(int capacity);
    void insertEntry(String name, String value);
    void insertEntry(int nameRef, String value);
    void insertEntry(int ref);
}
