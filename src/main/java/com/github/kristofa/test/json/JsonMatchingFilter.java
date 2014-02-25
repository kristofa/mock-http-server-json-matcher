package com.github.kristofa.test.json;

import com.github.kristofa.test.http.AbstractHttpRequestMatchingFilter;
import com.github.kristofa.test.http.HttpRequest;
import com.github.kristofa.test.http.HttpRequestImpl;
import com.github.kristofa.test.http.HttpRequestMatchingContext;
import com.github.kristofa.test.http.HttpRequestMatchingContextImpl;
import com.github.kristofa.test.http.HttpRequestMatchingFilter;
import com.github.kristofa.test.http.UnexpectedContentException;

/**
 * {@link HttpRequestMatchingFilter} that supports matching JSON content.
 * <p/>
 * If original and other request have JSON as content and they are equal but they don't have the same byte array content this
 * {@link HttpRequestMatchingFilter} will resolve this.
 * <p/>
 * Equal JSON but different byte array can occur when for example properties have different order in both requests.
 * 
 * @author kristof
 */
public class JsonMatchingFilter extends AbstractHttpRequestMatchingFilter {

    /**
     * {@inheritDoc}
     */
    @Override
    public HttpRequestMatchingContext filter(final HttpRequestMatchingContext context) {
        final HttpRequest originalRequest = context.originalRequest();
        final HttpRequest copyOriginal = copyAndSetJsonContentMatcher(originalRequest);
        if (originalRequest == copyOriginal) {
            return context;
        }

        final HttpRequest otherRequest = context.otherRequest();
        final HttpRequest copyOther = copyAndSetJsonContentMatcher(otherRequest);

        return new HttpRequestMatchingContextImpl(copyOriginal, copyOther, context.response());
    }

    private HttpRequest copyAndSetJsonContentMatcher(final HttpRequest request) {
        final HttpRequestImpl copy = new HttpRequestImpl(request);
        try {
            copy.contentMatcher(new JsonContentMatcher());
        } catch (final UnexpectedContentException e) {
            // Content is not JSON.
            return request;
        }
        return copy;
    }
}
