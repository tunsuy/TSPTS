package com.sangfor.moa.cch.socket;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import org.apache.jmeter.config.Arguments;
import org.apache.jmeter.protocol.java.sampler.JavaSamplerContext;

import com.google.gson.Gson;
import com.sangfor.moa.cch.message.JsonDataUtil;
import com.sangfor.moa.cch.message.Messagebuild;
import com.sangfor.moa.protobuf.PB_AthAuthReq;
import com.sangfor.moa.protobuf.PB_AthLoginReq;
import com.sangfor.moa.protobuf.PB_ClientDevType;
import com.sangfor.moa.protobuf.PB_ImGroupMessage;
import com.sangfor.moa.protobuf.PB_ImPostGroupMsgReq;
import com.squareup.wire.ByteString;
import com.squareup.wire.Message;

public class Test {

	public static void main(String[] args) throws Exception{
		// TODO Auto-generated method stub
		JsonDataUtil bd = new JsonDataUtil();
		bd.init("test/data.json");
		short srv_code = (short)Integer.parseInt(bd.getSrvCode().replace("0x",""), 16);
		short inf_code = (short)Integer.parseInt(bd.getInfCode());
		Messagebuild message = new Messagebuild(bd.getSrvName(),srv_code,bd.getInfName(),inf_code,bd.getParm(),bd.getInfRsp());
		message.get_customer_message();
//		List<Integer> l = null;
//		Object a = new String("11");
//		System.out.print(l.getClass().getDeclaredFields());
////		System.out.println(Arrays.toString(message.get_message()));//字节数组打印
//		Socketbuilder sb = new Socketbuilder("200.200.169.53", 6800);
//		sb.sendPB(message.get_message(),bd.getInfRsp(),3);
		
//		Object login_pb = new PB_ImGroupMessage();
//		Field[] field = login_pb.getClass().getDeclaredFields();
//		for (int i = 0; i < field.length; i++) {
//			System.out.println(field[i].getName());
//			System.out.println(field[i].getGenericType().toString());
//		}
//		System.out.println();
//		Test a = new Test();
//		Field[] field1 = a.getClass().getDeclaredFields();
//		for (int i = 0; i < field1.length; i++) {
//			System.out.println(field1[i].getName());
//			System.out.println(field1[i].getGenericType().toString());
//		}
//		login_pb.domain_name = "cch";
//		login_pb.login_account = "12345678901";
//		login_pb.client_version = "9.9.0";
//		login_pb.cdt = PB_ClientDevType.PBCDT_SERVER;
//		
//		Messagebuild login = new Messagebuild("auth",(short)2,"PB_AthLoginReq",(short)1,login_pb,"PB_AthLoginRsp");
//		
//		PB_AthAuthReq auth_pb = new PB_AthAuthReq();
//		auth_pb.login_account = "12345678901";
//		auth_pb.encrypt_data = ByteString.of("123456".getBytes());
//		
//		Messagebuild auth = new Messagebuild("auth",(short)2,"PB_AthAuthReq",(short)3,auth_pb,"PB_AthAuthRsp");
//		
//		Socketbuilder sb = new Socketbuilder("200.200.169.53", 6800);
//		sb.sendPB(login.get_message(),"PB_AthLoginRsp",1);
//		Message a = sb.sendPB(auth.get_message(),"PB_AthAuthRsp",1);
//		Gson gson=new Gson();
//        String obj=gson.toJson(a);
//        System.out.println(JsonDataUtil.formatJson(obj.toString()));
//		Arguments params = new Arguments(); 
//        params.addArgument("ip", "200.200.169.53");  
//        params.addArgument("port", "6800");
//        params.addArgument("user", "12222222220");
//        params.addArgument("psw", "12345");
//        params.addArgument("test_data_path", "test/data.json");
//        params.addArgument("run_count", "1");
//        params.addArgument("need_login", "1");
//        
//        JavaSamplerContext arg0 = new JavaSamplerContext(params); 
//        JmeterSampler test = new JmeterSampler();
////        test.setupTest(arg0);
//        test.runTest(arg0);
//        test.teardownTest(arg0);
	}
	
	List<String> a = new ArrayList<>();
}
