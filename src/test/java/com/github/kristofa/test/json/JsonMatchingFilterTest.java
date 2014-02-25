package com.github.kristofa.test.json;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verifyNoMoreInteractions;

import org.junit.Before;
import org.junit.Test;

import com.github.kristofa.test.http.HttpRequestImpl;
import com.github.kristofa.test.http.HttpRequestMatchingContext;
import com.github.kristofa.test.http.HttpRequestMatchingContextImpl;
import com.github.kristofa.test.http.HttpResponse;
import com.github.kristofa.test.http.Method;

public class JsonMatchingFilterTest {

    private final static String PATH = "/path/";
    private final static String PATH2 = "/path/";

    private HttpResponse mockResponse;
    private final JsonMatchingFilter filter = new JsonMatchingFilter();

    @Before
    public void setup() {
        mockResponse = mock(HttpResponse.class);
    }

    @Test
    public void testFilterNoContent() {
        final HttpRequestImpl originalRequest = new HttpRequestImpl();
        originalRequest.method(Method.GET).path(PATH);

        final HttpRequestImpl otherRequest = new HttpRequestImpl();
        otherRequest.method(Method.GET).path(PATH2);

        final HttpRequestMatchingContextImpl context =
            new HttpRequestMatchingContextImpl(originalRequest, otherRequest, mockResponse);

        final HttpRequestMatchingContext returnedContext = filter.filter(context);
        assertNotSame(context, returnedContext);
        assertNotSame(originalRequest, returnedContext.originalRequest());
        assertNotSame(otherRequest, returnedContext.otherRequest());
        assertSame(mockResponse, returnedContext.response());
        verifyNoMoreInteractions(mockResponse);
    }

    @Test
    public void testFilterNoJsonContent() {
        final HttpRequestImpl originalRequest = new HttpRequestImpl();
        originalRequest.method(Method.GET).path(PATH).content(new String("No Json Content").getBytes());

        final HttpRequestImpl otherRequest = new HttpRequestImpl();
        otherRequest.method(Method.GET).path(PATH2).content(new String("No Json Content").getBytes());

        final HttpRequestMatchingContextImpl context =
            new HttpRequestMatchingContextImpl(originalRequest, otherRequest, mockResponse);

        final HttpRequestMatchingContext returnedContext = filter.filter(context);
        assertSame(context, returnedContext);
        assertSame(originalRequest, returnedContext.originalRequest());
        assertSame(otherRequest, returnedContext.otherRequest());
        assertSame(mockResponse, returnedContext.response());
        verifyNoMoreInteractions(mockResponse);

    }

    @Test
    public void testFilterSameByteArrayJsonContent() {
        final HttpRequestImpl originalRequest = new HttpRequestImpl();
        originalRequest.method(Method.GET).path(PATH).content(new String("{ \"a\": 1, \"b\": 2 }").getBytes());

        final HttpRequestImpl otherRequest = new HttpRequestImpl();
        otherRequest.method(Method.GET).path(PATH).content(new String("{ \"a\": 1, \"b\": 2 }").getBytes());

        final HttpRequestMatchingContextImpl context =
            new HttpRequestMatchingContextImpl(originalRequest, otherRequest, mockResponse);

        final HttpRequestMatchingContext returnedContext = filter.filter(context);
        assertNotSame(context, returnedContext);
        assertNotSame(originalRequest, returnedContext.originalRequest());
        assertNotSame(otherRequest, returnedContext.otherRequest());
        assertSame(mockResponse, returnedContext.response());
        assertEquals(returnedContext.originalRequest(), returnedContext.otherRequest());

        verifyNoMoreInteractions(mockResponse);

    }

    @Test
    public void testFilterDifferentByteArraySameJsonContent() {
        final HttpRequestImpl originalRequest = new HttpRequestImpl();
        originalRequest.method(Method.GET).path(PATH).content(new String("{ \"a\": 1, \"b\": 2 }").getBytes());

        final HttpRequestImpl otherRequest = new HttpRequestImpl();
        otherRequest.method(Method.GET).path(PATH).content(new String("{ \"b\": 2, \"a\": 1 }").getBytes());

        assertFalse("Content byte array does not equal so expect false.", originalRequest.equals(otherRequest));

        final HttpRequestMatchingContextImpl context =
            new HttpRequestMatchingContextImpl(originalRequest, otherRequest, mockResponse);

        final HttpRequestMatchingContext returnedContext = filter.filter(context);
        assertNotSame(context, returnedContext);
        assertNotSame(originalRequest, returnedContext.originalRequest());
        assertNotSame(otherRequest, returnedContext.otherRequest());
        assertSame(mockResponse, returnedContext.response());
        assertEquals("Expected to be equal because while byte array is not equal, JSON content is equal.",
            returnedContext.originalRequest(), returnedContext.otherRequest());

        verifyNoMoreInteractions(mockResponse);
    }

    @Test
    public void testFilterDifferentByteArrayDifferentJsonContent() {
        final HttpRequestImpl originalRequest = new HttpRequestImpl();
        originalRequest.method(Method.GET).path(PATH).content(new String("{ \"a\": 1, \"b\": 2 }").getBytes());

        final HttpRequestImpl otherRequest = new HttpRequestImpl();
        otherRequest.method(Method.GET).path(PATH).content(new String("{ \"b\": 1, \"a\": 2 }").getBytes());

        assertFalse("Content byte array does not equal so expect false.", originalRequest.equals(otherRequest));

        final HttpRequestMatchingContextImpl context =
            new HttpRequestMatchingContextImpl(originalRequest, otherRequest, mockResponse);

        final HttpRequestMatchingContext returnedContext = filter.filter(context);
        assertNotSame(context, returnedContext);
        assertNotSame(originalRequest, returnedContext.originalRequest());
        assertNotSame(otherRequest, returnedContext.otherRequest());
        assertSame(mockResponse, returnedContext.response());
        assertFalse("Expected not to be equal because values of properties a and b have swapped.", returnedContext
            .originalRequest().equals(returnedContext.otherRequest()));

        verifyNoMoreInteractions(mockResponse);

    }

}
