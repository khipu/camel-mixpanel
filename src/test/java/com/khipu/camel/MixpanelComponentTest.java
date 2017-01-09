package com.khipu.camel;

import com.khipu.mixpanel.MixpanelHeaders;
import org.apache.camel.Endpoint;
import org.apache.camel.Exchange;
import org.apache.camel.Producer;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.json.JSONException;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.io.IOException;

public class MixpanelComponentTest extends CamelTestSupport {
    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void testMixpanelJsonExpected() throws Exception {
        thrown.expect(JSONException.class);
        thrown.expectMessage("A JSONObject text must begin with '{' at character 0");
        Endpoint mixpanel = getMandatoryEndpoint("mixpanel:token");
        Producer producer = mixpanel.createProducer();
        Exchange exchange = mixpanel.createExchange();
        exchange.getIn().setBody("");
        producer.process(exchange);

    }

    @Test
    public void testMixpanelMandatoryDistinctIdHeader() throws Exception {
        thrown.expect(IOException.class);
        thrown.expectMessage("Invalid distinctId provided");
        Endpoint mixpanel = getMandatoryEndpoint("mixpanel:token");
        Producer producer = mixpanel.createProducer();
        Exchange exchange = mixpanel.createExchange();
        exchange.getIn().setBody("{}");
        producer.process(exchange);
    }

    @Test
    public void testMixpanelMandatoryEventNameHeader() throws Exception {
        thrown.expect(IOException.class);
        thrown.expectMessage("Invalid eventName provided");
        Endpoint mixpanel = getMandatoryEndpoint("mixpanel:token");
        Producer producer = mixpanel.createProducer();
        Exchange exchange = mixpanel.createExchange();
        exchange.getIn().setBody("{}");
        exchange.getIn().setHeader(MixpanelHeaders.MIXPANEL_DISCTINCT_ID,"1234");
        producer.process(exchange);
    }

}
