/*
 * Licensed to the Warcraft4J Project under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The Warcraft4J Project licenses
 * this file to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package nl.salp.warcraft4j.io.reader.http;

import nl.salp.warcraft4j.io.reader.DataReader;
import nl.salp.warcraft4j.io.datatype.DataType;
import nl.salp.warcraft4j.io.parser.DataParsingException;
import nl.salp.warcraft4j.io.reader.InputStreamDataReader;
import org.apache.http.HttpEntity;
import org.apache.http.StatusLine;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import static java.lang.String.format;

/**
 * TODO Refactor this and {@link InputStreamDataReader} since they overlap 90%.
 *
 * @author Barre Dijkstra
 */
public class HttpDataReader extends DataReader {
    private CloseableHttpClient httpClient;
    private CloseableHttpResponse response;
    private InputStream responseStream;
    private long position;
    private long length;

    public HttpDataReader(String url) throws DataParsingException {
        try {
            httpClient = HttpClients.createDefault();
            response = httpClient.execute(new HttpGet(URI.create(url)));
            responseStream = getResponseStream(url, response);
        } catch (Exception e) {
            try {
                close();
            } catch (IOException ioe) {
                // Ignore.
            }
            throw new DataParsingException(e);
        }
    }

    private InputStream getResponseStream(String url, CloseableHttpResponse response) throws IOException {
        StatusLine statusLine = response.getStatusLine();
        if (statusLine.getStatusCode() > 300) {
            throw new DataParsingException(String.format("Error opening HTTP data reader for %s: error %d: %s", url, statusLine.getStatusCode(), statusLine.getReasonPhrase()));
        }
        HttpEntity entity = response.getEntity();
        if (entity == null) {
            throw new DataParsingException(format("HTTP data reader received no response from for %s", url));
        }
        length = entity.getContentLength();
        return entity.getContent();
    }

    @Override
    public long position() {
        return position;
    }

    @Override
    public boolean hasRemaining() throws IOException {
        return length > position;
    }

    @Override
    public long remaining() throws IOException {
        return length - position;
    }

    @Override
    public long size() throws IOException {
        return length;
    }

    @Override
    public void skip(long bytes) throws IOException {
        if (bytes < 0) {
            throw new IllegalArgumentException(format("Unable to skip %d bytes.", bytes));
        }
        if (bytes > remaining()) {
            throw new IOException(format("Error skipping %d bytes from position %d with %d bytes available.", bytes, position, remaining()));
        }
        long remaining = bytes;
        while (remaining > 0) {
            remaining -= responseStream.skip(remaining);
        }
        position += bytes;
    }

    @Override
    public final <T> T readNext(DataType<T> dataType, ByteOrder byteOrder) throws IOException, DataParsingException {
        ByteBuffer buffer;
        if (dataType.isVariableLength()) {
            buffer = getVarLenBuffer(dataType, byteOrder);
        } else {
            byte[] data = new byte[dataType.getLength()];
            responseStream.read(data);
            buffer = ByteBuffer.wrap(data).order(byteOrder);
        }
        position += buffer.limit();
        buffer.rewind();
        return dataType.readNext(buffer, byteOrder);
    }

    /**
     * Get a buffer for a variable length data type.
     *
     * @param dataType  The data type.
     * @param byteOrder The byte order for the returned buffer.
     * @param <T>       The data type.
     *
     * @return The byte buffer.
     *
     * @throws IOException When creating the buffer failed.
     */
    private <T> ByteBuffer getVarLenBuffer(DataType<T> dataType, ByteOrder byteOrder) throws IOException {
        try (ByteArrayOutputStream byteOut = new ByteArrayOutputStream()) {
            boolean done = false;
            byte b;
            while (!done) {
                if ((b = (byte) responseStream.read()) != -1) {
                    byteOut.write(b);
                    done = dataType.isVariableLengthTerminator(b);
                } else {
                    done = true;
                }
            }
            return ByteBuffer.wrap(byteOut.toByteArray()).order(byteOrder);
        }
    }

    @Override
    public void close() throws IOException {
        if (this.responseStream != null) {
            try {
                this.responseStream.close();
            } catch (IOException e) {
                // Ignore.
            }
        }
        if (this.response != null) {
            try {
                this.response.close();
            } catch (IOException e) {
                // Ignore.
            }
        }
        if (this.httpClient != null) {
            try {
                this.httpClient.close();
            } catch (IOException e) {
                // Ignore.
            }
        }
    }
}
