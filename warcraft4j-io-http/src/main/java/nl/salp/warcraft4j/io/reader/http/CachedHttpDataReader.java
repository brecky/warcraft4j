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

import nl.salp.warcraft4j.io.parser.DataParsingException;
import nl.salp.warcraft4j.io.reader.ByteArrayDataReader;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.http.HttpEntity;
import org.apache.http.StatusLine;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.net.URI;

import static java.lang.String.format;

/**
 * TODO Document class.
 *
 * @author Barre Dijkstra
 */
public class CachedHttpDataReader extends ByteArrayDataReader {

    public CachedHttpDataReader(String url) throws DataParsingException {
        super(getData(url));
    }

    public CachedHttpDataReader(String url, long offset, long length) {
        super(getData(url, offset, length));
    }

    private static byte[] getData(String url) throws DataParsingException {
        try (CloseableHttpClient httpClient = HttpClients.createDefault();
             CloseableHttpResponse response = httpClient.execute(new HttpGet(URI.create(url)))) {
            StatusLine statusLine = response.getStatusLine();
            if (statusLine.getStatusCode() > 300) {
                throw new DataParsingException(String.format("Error opening HTTP data reader for %s: error %d: %s", url, statusLine.getStatusCode(), statusLine.getReasonPhrase()));
            }
            HttpEntity entity = response.getEntity();
            if (entity == null) {
                throw new DataParsingException(format("HTTP data reader received no response from for %s", url));
            }
            byte[] data = EntityUtils.toByteArray(entity);
            EntityUtils.consume(entity);
            return data;
        } catch (IOException e) {
            throw new DataParsingException(e);
        }
    }

    private static byte[] getData(String url, long offset, long length) throws DataParsingException {
        if (offset < 0) {
            throw new DataParsingException(format("Can't create a http reader for %s with negative data block offset %d.", url, offset));
        }
        if (length < 0) {
            throw new DataParsingException(format("Can't create a http reader for %s with negative data block length %d.", url, length));
        }
        byte[] data = getData(url);
        if (offset + length > data.length) {
            throw new DataParsingException(format("Can't create a http reader for %s with %d bytes of data from offset %d with length %d.",
                    url, data.length, offset, length));
        }
        return ArrayUtils.subarray(data, (int) offset, (int) length);
    }
}
