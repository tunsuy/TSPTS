package com.sangfor.moa.cch.socket;

import org.apache.jmeter.config.Arguments;  
import org.apache.jmeter.protocol.java.sampler.AbstractJavaSamplerClient;  
import org.apache.jmeter.protocol.java.sampler.JavaSamplerContext;  
import org.apache.jmeter.samplers.SampleResult;  
    

import com.google.gson.Gson;
import com.sangfor.moa.cch.message.JsonDataUtil;
import com.sangfor.moa.cch.message.Messagebuild;
import com.sangfor.moa.protobuf.PB_AthAuthReq;
import com.sangfor.moa.protobuf.PB_AthLoginReq;
import com.sangfor.moa.protobuf.PB_ClientDevType;
import com.squareup.wire.ByteString;
import com.squareup.wire.Message;
/** 
 * 
 * @author cch
 * 
 */
public class JmeterSampler extends AbstractJavaSamplerClient{
	
	private Messagebuild message;
	private byte[] message_byte;
	private Messagebuild auth;
	private byte[] auth_byte;
	private Messagebuild login;
	private byte[] login_byte;
	
	private Socketbuilder sb;
	private JsonDataUtil bd;
	
	//输入的参数
	private String ip;
	private int port;
	private String user;
	private String psw;
	private String test_data_path;
	private int run_count;
	private int need_login; 
	private String requestStr;
	     
	private static String label = "PBsocket";  
	       
	 // 测试结果  
	private SampleResult sr;
	 
	 /** 
	  * 初始化 
	  */  
	public void setupTest(JavaSamplerContext arg0) {    
//	    System.out.println("setupTest"); 
	    
	}
	 
	 /** 
	  * 设置请求的参数 
	  */  
	 public Arguments getDefaultParameters() {  
        Arguments params = new Arguments();  
        params.addArgument("ip", "200.200.169.53");  
        params.addArgument("port", "6800");
        params.addArgument("user", "12345678901");  
        params.addArgument("psw", "12345");
        params.addArgument("test_data_path", "data.json");
        params.addArgument("run_count", "1");
        params.addArgument("need_login", "1");
        return params;
	 }  
	 
	@SuppressWarnings("deprecation")
	@Override
	public SampleResult runTest(JavaSamplerContext parm) {
		// TODO Auto-generated method stub
		sr = new SampleResult();
        sr.setSampleLabel(label);
        sr.sampleStart(); //记录程序执行时间，以及执行结果 
		try{
			initParm(parm);//初始化，获取输入的参数
			initMessage();//初始化message
			sr.setSamplerData("请求数据:"+requestStr);
            //发送数据
            if(1 == need_login){
            	login();
            }
            Message rsp = sendData();
            sr.setDataEncoding("UTF-8");
            Gson gson=new Gson();
            String obj = gson.toJson(rsp);
            obj = JsonDataUtil.formatJson(obj);
            sr.setResponseData("响应数据:\n" +obj.toString());
            sr.setSuccessful(true);
        }catch(Exception e){
        	sr.setResponseData("run failed:\n"+e.toString() + "\n"+e.getMessage());
            sr.setSuccessful(false);
        }finally{
            sr.sampleEnd();
        }
		sb.close_socket();
        return sr;
	}
	
	private void initParm(JavaSamplerContext parm) throws Exception{
		ip = parm.getParameter("ip");
		if(ip.isEmpty()){
			throw new Exception("please input correct ip!\n");
		}
		try {
			port = Integer.parseInt(parm.getParameter("port"));
		} catch (Exception e) {
			throw new Exception("port is not num!\n"+e.toString()+"\n"+e.getMessage());
		}
        
        user = parm.getParameter("user");
        if(user.isEmpty()){
			throw new Exception("please input correct user!\n");
		}
        psw = parm.getParameter("psw");
        if(psw.isEmpty()){
			throw new Exception("please input correct psw!\n");
		}
        test_data_path = parm.getParameter("test_data_path");
        if(test_data_path.isEmpty()){
			throw new Exception("please input correct test_data_path!\n");
		}
        
        try {
        	run_count = Integer.parseInt(parm.getParameter("run_count"));
		} catch (NumberFormatException e) {
			throw new Exception("run_count is not num!\n"+e.toString()+"\n"+e.getMessage());
		}
        
        try {
        	need_login = Integer.parseInt(parm.getParameter("need_login"));
		} catch (NumberFormatException e) {
			throw new Exception("need_login is not num!\n"+e.toString()+"\n"+e.getMessage());
		}
        
	}
	
	
	private void initMessage() throws Exception{
		bd = new JsonDataUtil();
		bd.init(test_data_path);
		
		requestStr = "\n ip:"+ip+"\n port:"+port+"\n user:"+user+"\n psw:"+psw+"\n path:"+test_data_path+"\n count:"+run_count+"\n need_login:"+need_login+"\n data:"+bd.toFormatString();
		short srv_code = (short)Integer.parseInt(bd.getSrvCode().replace("0x",""), 16);
		short inf_code = (short)Integer.parseInt(bd.getInfCode());
		
		sb = new Socketbuilder(ip, port);
		message = new Messagebuild(bd.getSrvName(),srv_code,bd.getInfName(),inf_code,bd.getParm(),bd.getInfRsp());
		message_byte = message.get_customer_message();
		
		PB_AthLoginReq login_pb = new PB_AthLoginReq();
		login_pb.domain_name = "cch";
		login_pb.login_account = this.user;
		login_pb.client_version = "9.9.9";
		login_pb.cdt = PB_ClientDevType.PBCDT_SERVER;//这里比较关键，设置为server就能够同时登录多个用户
		login = new Messagebuild("auth",(short)2,"PB_AthLoginReq",(short)1,login_pb,"PB_AthLoginRsp");
		login_byte = login.get_message();
		
		PB_AthAuthReq auth_pb = new PB_AthAuthReq();
		auth_pb.login_account = this.user;
		auth_pb.encrypt_data = ByteString.of(this.psw.getBytes());
		auth = new Messagebuild("auth",(short)2,"PB_AthAuthReq",(short)3,auth_pb,"PB_AthAuthRsp");
		auth_byte = auth.get_message();
	}
	
	private void login() throws Exception{
		sb.sendPB(login_byte,"PB_AthLoginRsp",1);
		sb.sendPB(auth_byte,"PB_AthAuthRsp",1);
	}
	
	private Message sendData() throws Exception{
		return sb.sendPB(message_byte,bd.getInfRsp(),run_count);
	}
	
	/** 
	  * 结束 
	  */  
	public void teardownTest(JavaSamplerContext arg0) {
		
	}
	
}
