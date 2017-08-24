package com.khipu.camel;

import org.apache.camel.CamelContext;
import org.apache.camel.Endpoint;
import org.apache.camel.impl.UriEndpointComponent;

import java.util.Map;

/**
 * Represents the component that manages {@link MixpanelEndpoint}.
 */
public class MixpanelComponent extends UriEndpointComponent {

	public MixpanelComponent() {
		super(MixpanelEndpoint.class);
	}

	public MixpanelComponent(CamelContext context) {
		super(context, MixpanelEndpoint.class);
	}

	protected Endpoint createEndpoint(String uri, String remaining, Map<String, Object> parameters) throws Exception {
		Endpoint endpoint = new MixpanelEndpoint(uri, this);
		String[] params = remaining.split("\\|");
		if (params.length > 0) {
			parameters.put("token", params[0].trim());
		}
		if (params.length > 1) {
			parameters.put("apiKey", params[1].trim());
		}
		setProperties(endpoint, parameters);
		return endpoint;
	}
}
