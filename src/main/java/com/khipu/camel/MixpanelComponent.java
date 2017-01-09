package com.khipu.camel;

import java.util.Map;

import org.apache.camel.CamelContext;
import org.apache.camel.Endpoint;

import org.apache.camel.impl.UriEndpointComponent;

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
        parameters.put("token",remaining);
        setProperties(endpoint, parameters);
        return endpoint;
    }
}
