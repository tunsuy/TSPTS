package pers.tunsuy.pts.model;

import pers.tunsuy.pts.common.MacroDefined;
import pers.tunsuy.pts.util.StringUtils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;

import com.google.protobuf.AbstractMessage;

import com.sangfor.moa.protobuf.Auth;;

public class UserDataBuilder {

	/** 
	 * 构造UserData对象
	 * @param jsonData json文件数据字符串
	 * @return userData 返回UserData对象
	 */
	public static UserData buildForUserData(String jsonData) {
		if (null == jsonData) {
			return null;
		}
		
		final GsonBuilder gsonBuilder = new GsonBuilder();
		gsonBuilder.registerTypeAdapter(UserData.class, new UserDataDeserializer());
		final Gson gson = gsonBuilder.create();
		
		UserData userData = null;
		try {
			userData = gson.fromJson(jsonData, UserData.class);
		} catch (JsonSyntaxException e) {
			e.printStackTrace();
		}
		
		return userData;
		
	}

	/**
	 * 构造操作数据字节
	 * @param userData UserData对象
	 * @return byte[] 返回操作类对象字节
	 */
	public static byte[] buildForOperationDataBytes(UserData userData) {
		if (null == userData) {
			return null;
		}
		
		Class<?> InfClass = null;
		try {
//			Class<?> ModuleClass = Class.forName(userData.getPbName());
			String infClassName = MacroDefined.MOA_PROTO_PKG + "." + StringUtils.toUpperCaseFirstOne(userData.getPbName()) + "$" + userData.getInfName();
			InfClass = Class.forName(infClassName);
		} catch (ClassNotFoundException e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		
		// ModuleClass.InfClass.Builder infClassBuilder = ModuleClass.InfClass.newBuilder();
		AbstractMessage infClass = (AbstractMessage)new Gson().fromJson(userData.getParam(), InfClass);

		return infClass.toByteArray();
	}
	
	/**
	 * 构造操作数据字节
	 * @param userData UserData对象
	 * @return byte[] 返回操作类对象字节
	 */
	public static String buildForInfRspName(UserData userData) {
		if (null == userData) {
			return null;
		}
		
		return userData.getInfRspName();
	}
	
}
