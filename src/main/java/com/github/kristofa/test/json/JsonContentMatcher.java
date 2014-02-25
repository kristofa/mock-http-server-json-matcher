package com.github.kristofa.test.json;

import java.io.IOException;

import org.apache.commons.lang3.Validate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.kristofa.test.http.ContentMatcher;
import com.github.kristofa.test.http.UnexpectedContentException;

/**
 * {@link ContentMatcher} that supports matching JSON. It can deal with matching JSON streams which have a different byte
 * array representation but which are equal. For example have same properties but in different order.
 * <p/>
 * So it parses and treats the content byte array as JSON.
 * 
 * @author kristof
 */
public class JsonContentMatcher extends ContentMatcher {

    private final static ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    private byte[] content;
    private JsonNode rootNode;

    /**
     * {@inheritDoc}
     */
    @Override
    public byte[] getContent() {
        return content;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setContent(final byte[] content) throws UnexpectedContentException {
        Validate.notNull(content);
        try {
            rootNode = OBJECT_MAPPER.readTree(content);
        } catch (final JsonProcessingException e) {
            throw new UnexpectedContentException(e);
        } catch (final IOException e) {
            throw new UnexpectedContentException(e);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ContentMatcher copy() {
        final JsonContentMatcher jsonContentMatcher = new JsonContentMatcher();
        if (content != null) {
            try {
                jsonContentMatcher.setContent(content);
            } catch (final UnexpectedContentException e) {
                throw new IllegalStateException(e);
            }
        }
        return jsonContentMatcher;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        // JsonNode does not implement hashCode() se we make sure all JsonContentMatchers
        // generate same hashCode.
        final int prime = 31;
        int result = 1;
        result = prime * result + "JsonContentMatcher".hashCode();
        return result;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final JsonContentMatcher other = (JsonContentMatcher)obj;
        if (rootNode == null) {
            if (other.rootNode != null) {
                return false;
            }
        } else if (!rootNode.equals(other.rootNode)) {
            return false;
        }
        return true;
    }

}
