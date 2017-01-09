package com.khipu.mixpanel;

import com.mixpanel.mixpanelapi.ClientDelivery;
import com.mixpanel.mixpanelapi.MessageBuilder;
import com.mixpanel.mixpanelapi.MixpanelAPI;
import org.json.JSONObject;

import java.io.IOException;

public class MixpanelClient {
    private MessageBuilder messageBuilder;
    public MixpanelClient(String appToken){
        this.messageBuilder = new MessageBuilder(appToken);
    }
    private void verifyValidData(String data, String dataName) throws IOException {
        if (data==null || data.trim().length()==0){
            throw new IOException("Invalid "+dataName +" provided");
        }
    }
    public void sendEvent(String distinctId, String eventName, JSONObject props) throws IOException {
        verifyValidData(distinctId,"distinctId");
        verifyValidData(eventName,"eventName");
        JSONObject events = messageBuilder.event(distinctId, eventName, props);
        ClientDelivery delivery = new ClientDelivery();
        delivery.addMessage(events);
        MixpanelAPI mixpanelAPI = new MixpanelAPI();
        mixpanelAPI.deliver(delivery);
    }
}
