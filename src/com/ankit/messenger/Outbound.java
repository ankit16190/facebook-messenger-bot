package com.ankit.messenger;

import java.io.IOException;

import javax.ws.rs.core.MediaType;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.primefaces.json.JSONException;
import org.primefaces.json.JSONObject;

public class Outbound {

	void createMessage(String recipientId, String text) throws JSONException{

		JSONObject json = new JSONObject();
		JSONObject json1 = new JSONObject();
		JSONObject json2 = new JSONObject();
		json.put("id", recipientId);
		json1.put("recipient", json);
		json2.put("text", "hiii");
		json1.put("message", json2);

		sendMessage(json);
	}

	void sendMessage(JSONObject jsonObject) throws JSONException{

		final CloseableHttpClient httpClient = HttpClientBuilder.create().build();
		try {
			final HttpPost request = new HttpPost(Utils.FACEBOOK_URL);
			request.setHeader("Content-type", MediaType.APPLICATION_JSON);
			request.setHeader("access_token", Utils.ACCESS_TOKEN_KEY);
			StringEntity params = new StringEntity(jsonObject.toString());
			//System.out.println("params........" +String.valueOf(jsonObject));
			request.setEntity(params);
			HttpResponse response = httpClient.execute(request);
			//System.out.println("result........" + Stringresponse);
			String mresult = EntityUtils.toString(response.getEntity());
			//JSONObject jsonObject = new JSONObject(mresult);
			System.out.println("result........" +mresult);


		}
		catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if(httpClient != null)
			{
				try {
					httpClient.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

}
