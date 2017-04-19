package pers.tunsuy.pts.model;

import java.lang.reflect.Type;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import pers.tunsuy.pts.model.UserData;

public class UserDataDeserializer implements JsonDeserializer<UserData> {
	@Override
	public UserData deserialize(final JsonElement json, final Type typeOfT, final JsonDeserializationContext context) {
		if (json.isJsonObject() == false) {
			throw new RuntimeException("Unexpected JSON type: " + json.getClass());
		}
		
		final JsonObject jsonObject = json.getAsJsonObject();
		final String pbName = jsonObject.get("pbName").getAsString();
		final short srvCode = jsonObject.get("srvCode").getAsShort();
		final String infName = jsonObject.get("infName").getAsString();
		final String infRspName = jsonObject.get("infRspName").getAsString();
		final int srvop = jsonObject.get("srvop").getAsInt();
		final String param = jsonObject.get("param").toString();
		
		final UserData userData = new UserData();
		userData.setPbName(pbName);
		userData.setSrvCode(srvCode);
		userData.setInfName(infName);
		userData.setInfRspName(infRspName);
		userData.setSrvop(srvop);
		userData.setParam(param);
		
		return userData;
	}
}
