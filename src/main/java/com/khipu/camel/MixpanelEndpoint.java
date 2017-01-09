package com.khipu.camel;

import com.khipu.mixpanel.MixpanelClient;
import org.apache.camel.Consumer;
import org.apache.camel.Processor;
import org.apache.camel.Producer;
import org.apache.camel.impl.DefaultEndpoint;
import org.apache.camel.spi.Metadata;
import org.apache.camel.spi.UriEndpoint;
import org.apache.camel.spi.UriPath;

/**
 * Represents a Mixpanel endpoint.
 */
@UriEndpoint(scheme = "mixpanel", title = "Mixpanel", syntax="mixpanel:token", producerOnly = true, label = "Mixpanel")
public class MixpanelEndpoint extends DefaultEndpoint {
    @UriPath @Metadata(required = "true")
    private String token;

    public MixpanelEndpoint() {
    }

    public MixpanelEndpoint(String uri, MixpanelComponent component) {
        super(uri, component);

    }

    public Producer createProducer() throws Exception {
        return new MixpanelProducer(this, new MixpanelClient(token));
    }

    public Consumer createConsumer(Processor processor) throws Exception {
        throw new UnsupportedOperationException("Cannot consume from an MixpanelEndpoint: " + getEndpointUri());
    }

    public boolean isSingleton() {
        return true;
    }

    /**
     * your project token in Mixpanel
     */
    public void setToken(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }

}
