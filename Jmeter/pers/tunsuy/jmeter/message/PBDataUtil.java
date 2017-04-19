package com.sangfor.moa.cch.message;

import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.ByteOrder;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.squareup.wire.ByteString;
import com.squareup.wire.Message;
import com.squareup.wire.Wire;




import java.util.ArrayList;
import java.util.Iterator;
//import com.sangfor.moa.protobuf.*;
import java.util.List;
/**
 * 
 * @author cch
 *
 */
public class PBDataUtil {
	
	/**
	 * 构造通用的PB head,并且二进制化
	 * @param srvop
	 * @return
	 * @throws ClassNotFoundException
	 */
	public static byte[] buildHead(int srvop) throws ClassNotFoundException{
		Class<?> forName = Class.forName("com.sangfor.moa.protobuf.PB_SrvHead");
		JsonObject json_object = new JsonObject();
		json_object.addProperty("srvop", srvop);
		Message pb =  (Message) new Gson().fromJson(json_object,forName);
//		System.out.print(pb);
		return pb.toByteArray();
	}
	
	/**
	 * 构造通用的pb，并且二进制化
	 * @param className
	 * @param json
	 * @return
	 * @throws ClassNotFoundException
	 */
	public static byte[] buildCommonParm(String className,JsonObject json) throws ClassNotFoundException{
		if(null == className || null == json){
			return null;
		}
		Class<?> forName = Class.forName("com.sangfor.moa.protobuf."+className);
		Message pb =  (Message) new Gson().fromJson(json,forName);
//		System.out.print(pb);
		return pb.toByteArray();
	}
	
	/**
	 * 自定义构造通用的pb，并且二进制化
	 * @param className
	 * @param json
	 * @return
	 * @throws Exception
	 */
	public static byte[] customBuildCommonParm(String className,JsonObject json) throws Exception{
		if(null == className || null == json){
			return null;
		}
		Message pb = Json2Message("com.sangfor.moa.protobuf."+className,json);
//System.out.print(pb);
		return pb.toByteArray();
	}
	
	/**
	 * 把json转换为pb对象
	 * @param pb
	 * @param json
	 * @return
	 * @throws Exception
	 */
	private static Message Json2Message(String className,JsonObject json) throws Exception{
		Class<?> forName = Class.forName(className);
		Message pb = (Message) forName.newInstance();
		Field[] field = forName.getDeclaredFields(); //获取实体类的所有属性，返回Field数组  
		
		for (int i = 0; i < field.length; i++) {
			String name = field[i].getName();//获取属性的名字
			String type = field[i].getGenericType().toString();//获取属性的类型
			field[i].setAccessible(true);
			
			if(null == json.get(name)){
				continue;
			}
			if(-1 != type.indexOf("List<")){//是一个repeat类型
				
				String type_class = getClassName(type);
				List<Object> message_list = new ArrayList<Object>();
				
				JsonArray ja = json.get(name).getAsJsonArray();
				Iterator<JsonElement> it = ja.iterator();
				while(it.hasNext()){
					JsonElement je = it.next();
					Object obj = null;
					obj = getDatafromJson(type_class, je);
					if(null == obj){
//System.out.println(type_class);
			        	JsonObject childJson = je.getAsJsonObject();
			        	obj = Json2Message(type_class,childJson);
			        }
					message_list.add(obj);
				}
				field[i].set(pb, message_list);
				
			}else {//非repeat类型
				Object obj = null;
				JsonElement je = json.get(name);
				obj = getDatafromJson(type,je);
				if(null == obj){
		        	String class_name = getClassName(type);
//System.out.println(class_name);
		        	JsonObject childJson = je.getAsJsonObject();
		        	obj = Json2Message(class_name,childJson);
		        }
				field[i].set(pb, obj);
			}
			
		}
		return pb;
	}
	
	/**
	 * 对type进行字符串处理，获取类的路径
	 * @param str
	 * @return
	 */
	private static String getClassName(String str){
		String ret = "";
		if(-1 != str.indexOf("class")){
			ret = str.substring(6);
		}else{
			int start = str.indexOf("<") + 1;
			int end = str.indexOf(">");
			ret = str.substring(start,end);
		}
		return ret;
	}
	
	/**
	 * 根据类型，把json对象的数据转换到java类型
	 * @param type
	 * @param str
	 * @return
	 */
	private static Object getDatafromJson(String type,JsonElement str){
		Object obj = null;
		if(type.equals("class java.lang.String") || type.equals("java.lang.String")){   //如果type是类类型，则前面包含"class "，后面跟类名
			obj = str.getAsString();
        }else if(type.equals("class java.lang.Integer") || type.equals("java.lang.Integer")){
        	obj = new Integer(str.getAsString());
        }else if(type.equals("class java.lang.Short") || type.equals("java.lang.Short")){
        	obj = new Short(str.getAsString());
        }else if(type.equals("class java.lang.Double") || type.equals("java.lang.Double")){
        	obj = new Double(str.getAsString());
        }else if(type.equals("class java.lang.Boolean") || type.equals("java.lang.Boolean")){
        	obj = new Boolean(str.getAsString());
        }else if(type.equals("class java.lang.Float") || type.equals("java.lang.Float")){
        	obj = new Float(str.getAsString());
        }else if(type.equals("class java.lang.Long") || type.equals("java.lang.Long")){
        	obj = new Long(str.getAsString());
        }else if(type.equals("class java.lang.Byte") || type.equals("java.lang.Byte")){
        	obj =new Byte(str.getAsString());
        }else if(type.equals("class com.squareup.wire.ByteString") || type.equals("com.squareup.wire.ByteString")){
        	obj = ByteString.of(str.getAsString().getBytes());
        }else{
        	//代表是非基本数据类型
        	obj = null;
        }
		return obj;
	}
	
	/**
	 * 通过byte解析为pb对象
	 * @param className
	 * @param data
	 * @return
	 * @throws Exception 
	 */
	public static Message getCommonParmFromByte(String className,byte[] data) throws Exception{
		if(null == data || null == className){
			return null;
		}
		Wire wire = new Wire();
		Message pb = null;
		try {
			@SuppressWarnings("unchecked")
			Class<Message> forName = (Class<Message>) Class.forName("com.sangfor.moa.protobuf."+className);
			pb = wire.parseFrom(data, forName);
		} catch (ClassNotFoundException e) {
			throw new Exception("can not find class "+className + "\n"+ e.toString() + "\n" + e.getMessage());
		}catch (IOException e) {
			throw new Exception("parse byte to pb class failed:"+className + "\n"+ e.toString() + "\n" + e.getMessage());
		}
		return pb;
	}
	
	/**
	 * 
	 * @param rsp 服务端返回的数据
	 * @return
	 * 
	 */
	public static int[] analyseHeadByte(byte[] rsp){
		if(null == rsp || rsp.length < 12){
			return null;
		}
		int[] ret = new int[2];
		
		byte[] byte_m_len = new byte[4];
		System.arraycopy(rsp, 4, byte_m_len, 0, 4);
		byte[] byte_head_len = new byte[4];
		System.arraycopy(rsp, 8, byte_head_len, 0, 4);
		int head_len = ByteUtil.bytes2Int(byte_head_len,ByteOrder.BIG_ENDIAN);
		int m_len = ByteUtil.bytes2Int(byte_m_len,ByteOrder.BIG_ENDIAN);
		
		ret[0] = head_len;
		ret[1] = m_len - head_len - 12;
		return ret;
	}
}
