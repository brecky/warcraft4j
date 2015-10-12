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
package nl.salp.warcraft4j.io;

import org.apache.http.HttpEntity;
import org.apache.http.StatusLine;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.nio.ByteBuffer;

import static java.lang.String.format;

/**
 * TODO Refactor this and {@link InputStreamDataReader} since they overlap 90%.
 *
 * @author Barre Dijkstra
 */
public class HttpDataReader extends BaseDataReader {
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

    /**
     * {@inheritDoc}
     */
    @Override
    public long position() {
        return position;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void setPosition(long position) throws DataReadingException {
        skip(position - this.position);
    }

    /**
     * {@inheritDoc}
     *
     * @return Always returns {@code false}.
     */
    @Override
    public boolean isRandomAccessSupported() {
        return false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public long size() {
        return length;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void skip(long bytes) throws DataReadingException, UnsupportedOperationException {
        if (bytes < 0) {
            throw new UnsupportedOperationException(format("Unable to skip %d bytes.", bytes));
        }
        if (bytes > remaining()) {
            throw new DataReadingException(format("Error skipping %d bytes from position %d with %d bytes available.", bytes, position, remaining()));
        }
        try {
            long remaining = bytes;
            while (remaining > 0) {
                remaining -= responseStream.skip(remaining);
            }
            position += bytes;
        } catch (IOException e) {
            throw new DataReadingException(e);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected int readData(ByteBuffer buffer) throws DataReadingException {
        try {
            int read = 0;
            byte[] dataArray;
            if (buffer.hasArray()) {
                read = responseStream.read(buffer.array());
            } else {
                byte[] data = new byte[buffer.capacity()];
                read = responseStream.read(data);
                buffer.put(data);
            }
            return read;
        } catch (IOException e) {
            throw new DataReadingException(e);
        }
    }

    /**
     * {@inheritDoc}
     */
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