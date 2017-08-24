package com.khipu.camel;

import com.khipu.mixpanel.MixpanelClient;
import org.apache.camel.Exchange;
import org.apache.camel.impl.DefaultProducer;
import org.json.JSONException;
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

	private static final long SECONDS_IN_DAY = 24 * 60 * 60;
	private MixpanelClient client;

	public MixpanelProducer(MixpanelEndpoint endpoint, MixpanelClient client) {
		super(endpoint);
		this.client = client;
	}

	public void process(Exchange exchange) throws Exception {
		JSONObject props = new JSONObject(exchange.getIn().getBody(String.class));
		String disctinctId = (String) exchange.getIn().getHeader(MIXPANEL_DISCTINCT_ID);
		String eventName = (String) exchange.getIn().getHeader(MIXPANEL_EVENT_NAME);
		if (isNewEvent(props)) {
			log.debug("send event [" + eventName + "] to mixpanel service");
			client.sendEvent(disctinctId, eventName, props);
			log.debug("event [" + eventName + "] sent to mixpanel service");
		} else {
			log.debug("import event [" + eventName + "] to mixpanel service");
			boolean imported = client.importEvent(disctinctId, props.getLong("time"), eventName, props);
			log.debug("event [" + eventName + "] imported to mixpanel service " + imported);
		}
	}

	private boolean isNewEvent(JSONObject properties) throws JSONException {
		if (!properties.has("time")) {
			return true;
		}
		Long eventTime = properties.getLong("time");
		long currentTime = System.currentTimeMillis() / 1000L;
		return currentTime - eventTime < SECONDS_IN_DAY;
	}
}