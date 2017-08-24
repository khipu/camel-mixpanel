package com.khipu.mixpanel;

import com.mixpanel.mixpanelapi.ClientDelivery;
import com.mixpanel.mixpanelapi.MessageBuilder;
import com.mixpanel.mixpanelapi.MixpanelAPI;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.Base64;

public class MixpanelClient {
	private static final Logger log = LoggerFactory.getLogger(MixpanelClient.class);
	private static final int READ_TIMEOUT = 10000;
	private static final int CONNECT_TIMEOUT = 2000;
	private static final int BUFFER_SIZE = 256;
	private final String token;
	private final String apikey;

	private MessageBuilder messageBuilder;

	public MixpanelClient(String appToken, String apikey) {
		this.token = appToken;
		this.apikey = apikey;
		this.messageBuilder = new MessageBuilder(appToken);
	}

	private void verifyValidData(String data, String dataName) throws IOException {
		if (data == null || data.trim().length() == 0) {
			throw new IOException("Invalid " + dataName + " provided");
		}
	}

	public void sendEvent(String distinctId, String eventName, JSONObject props) throws IOException {
		verifyValidData(distinctId, "distinctId");
		verifyValidData(eventName, "eventName");
		JSONObject events = messageBuilder.event(distinctId, eventName, props);
		ClientDelivery delivery = new ClientDelivery();
		delivery.addMessage(events);
		MixpanelAPI mixpanelAPI = new MixpanelAPI();
		mixpanelAPI.deliver(delivery);
	}

	public boolean importEvent(String disctinctId, long time, String eventName, JSONObject properties) throws JSONException, IOException {
		properties.put("token", token);
		properties.put("distinct_id", disctinctId);
		properties.put("time", time);
		JSONObject event = new JSONObject();
		event.put("event", eventName);
		event.put("properties", properties);
		return sendData(event.toString(), apikey);
	}

	boolean sendData(String dataString, String apiKey) throws IOException {
		URL endpoint = new URL("https://api.mixpanel.com/import");
		URLConnection connection = endpoint.openConnection();
		connection.setReadTimeout(READ_TIMEOUT);
		connection.setConnectTimeout(CONNECT_TIMEOUT);
		connection.setDoOutput(true);
		connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded;charset=utf8");

		byte[] utf8data = dataString.getBytes("utf-8");
		String base64data = new String(Base64.getEncoder().encode(utf8data));
		String encodedData = URLEncoder.encode(base64data, "utf8");
		String encodedQuery = "data=" + encodedData + "&api_key=" + apiKey;
		log.debug("uploading dataString {} apiKey {} encodedData {}", dataString, apiKey, encodedData);

		try (OutputStream postStream = connection.getOutputStream()) {
			postStream.write(encodedQuery.getBytes());
		}

		try (InputStream responseStream = connection.getInputStream()) {
			String response = this.slurp(responseStream);
			log.debug("Mixpanel import response: {}", response);
			return response.equals("1");
		}
	}

	private String slurp(InputStream in) throws IOException {
		final StringBuilder out = new StringBuilder();
		InputStreamReader reader = new InputStreamReader(in, "utf8");

		char[] readBuffer = new char[BUFFER_SIZE];
		int readCount;
		do {
			readCount = reader.read(readBuffer);
			if (readCount > 0) {
				out.append(readBuffer, 0, readCount);
			}
		} while (readCount != -1);

		return out.toString();
	}
}
