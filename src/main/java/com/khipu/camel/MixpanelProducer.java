package com.khipu.camel;

import com.khipu.mixpanel.MixpanelClient;
import org.apache.camel.Exchange;
import org.apache.camel.impl.DefaultProducer;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.khipu.mixpanel.MixpanelHeaders.MIXPANEL_DISCTINCT_ID;
import static com.khipu.mixpanel.MixpanelHeaders.MIXPANEL_EVENT_NAME;

/**
 * The Mixpanel producer.
 */
public class MixpanelProducer extends DefaultProducer {
    private static final Logger log = LoggerFactory.getLogger(MixpanelProducer.class);
    private MixpanelEndpoint endpoint;
    private MixpanelClient client;


    public MixpanelProducer(MixpanelEndpoint endpoint, MixpanelClient client) {
        super(endpoint);
        this.endpoint = endpoint;
        this.client = client;
    }

    public void process(Exchange exchange) throws Exception {
        JSONObject props = new JSONObject(exchange.getIn().getBody(String.class));
        String disctincId = (String)exchange.getIn().getHeader(MIXPANEL_DISCTINCT_ID);
        String eventName = (String)exchange.getIn().getHeader(MIXPANEL_EVENT_NAME);
        log.debug("send event ["+eventName+"] to mixpanel service");
        client.sendEvent(disctincId, eventName, props);
        log.debug("event ["+eventName+"] sent to mixpanel service");
    }

}
