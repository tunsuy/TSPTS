package com.sangfor.moa.cch.message;


import java.nio.ByteOrder;

import com.google.gson.JsonObject;
import com.squareup.wire.Message;
/**
 * 
 * @author cch
 *
 */
public class Messagebuild {
	
	@SuppressWarnings("unused")
	private String srv_name;
	private short srv_code;
	private String inf_name;
	private short inf_code;
	private JsonObject json_object = null;
	@SuppressWarnings("unused")
	private String srv_rsp;
	private short flag;;
	private Message pb = null;
	
	public Messagebuild(String srv_name,short srv_code,String inf_name,short inf_code,JsonObject parm,String srv_rsp){
		this.srv_name = srv_name;
		this.srv_code = srv_code;
		this.inf_name = inf_name;
		this.inf_code = inf_code;
		this.json_object = parm;
		this.srv_rsp = srv_rsp;
		this.flag = 0;
	}
	
	public Messagebuild(String srv_name,short srv_code,String inf_name,short inf_code,Message pb,String srv_rsp){
		this.srv_name = srv_name;
		this.srv_code = srv_code;
		this.inf_name = inf_name;
		this.inf_code = inf_code;
		this.pb = pb;
		this.srv_rsp = srv_rsp;
		this.flag = 0;
	}
	
	/**
	 * 构造操作码
	 * @param srv_code
	 * @param inf_code
	 * @return
	 */
	private int get_mac(){
		return ((this.srv_code) << 8 | this.inf_code);
	}
	
	private byte[] make_first_part(int m_len){
		byte[] byte_srv_code = ByteUtil.short2Bytes((short)srv_code,ByteOrder.BIG_ENDIAN);
		byte[] byte_flag = ByteUtil.short2Bytes((short)flag,ByteOrder.BIG_ENDIAN);
		byte[] byte_message_len = ByteUtil.int2Bytes((int)m_len,ByteOrder.BIG_ENDIAN);
		
		byte[] data = new byte[8];
	    System.arraycopy(byte_srv_code, 0, data, 0, 2);  
	    System.arraycopy(byte_flag, 0, data, 2, 2);  
	    System.arraycopy(byte_message_len, 0, data, 4, 4); 
	    return data;
	}
	
	private byte[] make_second_part(int head_len){
		byte[] data = ByteUtil.int2Bytes((int)head_len,ByteOrder.BIG_ENDIAN);
	    return data; 
	}
	
	/**
	 * 构造第三部分
	 * @return
	 * @throws Exception 
	 */
	private byte[] make_third_part() throws Exception{
		byte[] re = null;
		int srv_op = get_mac();
		try {
			re = PBDataUtil.buildHead(srv_op);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			throw new Exception("build srvHead failed \n" + e.toString() + "\n"+ e.getMessage());
		}
		return re;
	}
	
	/**
	 * 构造第四部分
	 * @return
	 * @throws Exception 
	 */
	private byte[] make_forth_part() throws Exception{
		if(this.pb != null){
			return this.pb.toByteArray();
		}
		if(this.json_object != null){
			byte[] re = null;
			try {
				re = PBDataUtil.buildCommonParm(this.inf_name, this.json_object);
				return re;
			} catch (Exception e) {
				// TODO Auto-generated catch block
				throw new Exception("build "+this.inf_name+" failed \n" + e.toString() + "\n"+ e.getMessage());
			}
		}else{
			return null;
		}
	}
	
	
	/**
	 * 通过json构造message
	 * @return
	 * @throws Exception 
	 */
	public byte[] get_message() throws Exception{
		byte[] third = make_third_part();
		byte[] forth = make_forth_part();
		
		byte[] second = make_second_part(third.length);
		
		int all_len = third.length + second.length + forth.length + 8;
		byte[] first = make_first_part(all_len);
		
		byte[] message = new byte[all_len];
	    System.arraycopy(first, 0, message, 0, first.length); 
	    System.arraycopy(second, 0, message, first.length, second.length);
	    System.arraycopy(third, 0, message, first.length + second.length,third.length); 
	    System.arraycopy(forth, 0, message, first.length + second.length + third.length,forth.length);
	    return message;
	}
	
	/**
	 * 构造第四部分
	 * @return
	 * @throws Exception 
	 */
	private byte[] make_customer_forth_part() throws Exception{
		if(this.pb != null){
			return this.pb.toByteArray();
		}
		if(this.json_object != null){
			byte[] re = null;
//			try {
				re = PBDataUtil.customBuildCommonParm(this.inf_name, this.json_object);
				return re;
//			} catch (Exception e) {
//				// TODO Auto-generated catch block
//				throw new Exception("build "+this.inf_name+" failed \n" + e.toString() + "\n"+ e.getMessage());
//			}
		}else{
			return null;
		}
	}
	
	/**
	 * 通过json构造message
	 * @return
	 * @throws Exception 
	 */
	public byte[] get_customer_message() throws Exception{
		byte[] third = make_third_part();
		byte[] forth = make_customer_forth_part();
		
		byte[] second = make_second_part(third.length);
		
		int all_len = third.length + second.length + forth.length + 8;
		byte[] first = make_first_part(all_len);
		
		byte[] message = new byte[all_len];
	    System.arraycopy(first, 0, message, 0, first.length); 
	    System.arraycopy(second, 0, message, first.length, second.length);
	    System.arraycopy(third, 0, message, first.length + second.length,third.length); 
	    System.arraycopy(forth, 0, message, first.length + second.length + third.length,forth.length);
	    return message;
	}
}
