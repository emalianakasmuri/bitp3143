package program22;

/*
 * Program 2.2 (file 1 of 2): CountingInputStream.java
 *
 * Program description:
 *   A decorator (wrapper) stream that counts how many times the stream
 *   beneath it is asked for data.
 *
 * What it shows:
 *   Java I/O classes are built by wrapping one stream inside another.
 *   FilterInputStream passes every call to the wrapped stream, so we only
 *   need to add a counter.
 *
 * Class description:
 *   CountingInputStream extends FilterInputStream and increments a counter
 *   on every read request that reaches the underlying source.
 */
import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;

public class CountingInputStream extends FilterInputStream {

    private int requests = 0;   // number of read requests seen

    /**
     * Wraps the given stream.
     *
     * @param source the stream that actually supplies the bytes
     */
    public CountingInputStream(InputStream source) {
        super(source);
    }

    /**
     * Reads one byte and counts the request.
     *
     * @return the byte read, or -1 at end of stream
     * @throws IOException if the underlying stream fails
     */
    @Override
    public int read() throws IOException {
        requests++;
        return super.read();
    }

    /**
     * Reads up to len bytes into buf and counts the request as one call.
     *
     * @param buf destination array
     * @param off start offset in buf
     * @param len maximum number of bytes to read
     * @return number of bytes read, or -1 at end of stream
     * @throws IOException if the underlying stream fails
     */
    @Override
    public int read(byte[] buf, int off, int len) throws IOException {
        requests++;
        return super.read(buf, off, len);
    }

    /**
     * @return how many read requests reached the underlying stream
     */
    public int getRequests() {
        return requests;
    }
}
