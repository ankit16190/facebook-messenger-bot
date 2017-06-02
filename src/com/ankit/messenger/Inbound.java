package com.ankit.messenger;

import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriInfo;

import org.primefaces.json.JSONArray;
import org.primefaces.json.JSONException;
import org.primefaces.json.JSONObject;

import javax.ws.rs.*;


@Path("chatbot")
public class Inbound {

	Outbound outbound = new Outbound();
	
	@GET	
	@Path("/webhook")
	public String bot(@Context UriInfo uriInfo, @QueryParam("hub.mode") String mode,
			@QueryParam("hub.verify_token") String verifyToken, @QueryParam("hub.challenge") String challenge) {
		String uri = uriInfo.getBaseUriBuilder().build()
				.toString();
		//System.out.println(uri);
		//System.out.println(mode);
		if(verifyToken.equals(Utils.VALIDATION_TOKEN_KEY) && challenge != null)
		return challenge;
		
		return "Wwrong Validation Token";
	}


	
	@POST 
	@Path("/bot")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.TEXT_PLAIN)
	public void bot(String request) throws JSONException {
		System.out.println("request........." + request);
		JSONObject jsonObject = new JSONObject(request.substring(request.indexOf('{')));
		if("page".equals(jsonObject.get("object"))){
			JSONArray entryArray = jsonObject.getJSONArray("entry");
			for (int i = 0; i < entryArray.length(); i++) {
				String pageId = entryArray.getJSONObject(i).getString("id");
				String time = entryArray.getJSONObject(i).getString("time");

				JSONArray messageArray = entryArray.getJSONObject(i).getJSONArray("messaging");
				for (int j = 0; j < messageArray.length(); j++) {
					JSONObject object = messageArray.getJSONObject(j);
					recievedMessage(object);
				}
			}
		}
	}

	void recievedMessage(JSONObject object) throws JSONException{
		String timestamp = object.getString("timestamp");
		String senderId = object.getJSONObject("sender").getString("id");
		String recipientId = object.getJSONObject("recipient").getString("id");
		String mid = object.getJSONObject("message").getString("mid");
		String text = object.getJSONObject("message").getString("text");
		
		outbound.createMessage(recipientId, text);
	}
}
