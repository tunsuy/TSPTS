package com.sangfor.moa.cch.socket;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

import com.sangfor.moa.cch.message.PBDataUtil;
import com.squareup.wire.Message;

/**
 * 用于进行socket操作
 * @author cch
 *
 */
public class Socketbuilder {
	private Socket socket = null;
	private DataInputStream is = null;
	private DataOutputStream os = null;
	private BufferedInputStream br = null;
	
	public Socketbuilder(String host,int port) throws UnknownHostException, IOException{
			//1、创建客户端Socket，指定服务器地址和端口
		this.socket = new Socket(host,port);
		this.socket.setSoTimeout(5000);
//		this.socket = new Socket();
//		socket.connect(new InetSocketAddress(host, port), 5000);

		is = new DataInputStream(socket.getInputStream()); 
		os = new DataOutputStream(socket.getOutputStream()); 
		br = new BufferedInputStream(is); 
	}
	
	/**
	 * 发送数据，忽略响应的数据
	 * @param data
	 * @param rsp_class
	 * @param count
	 * @return
	 * @throws IOException 
	 */
	public Message sendPBNoRsp(byte[] data,String rsp_class,int count) throws IOException{
		Message rsp = null;
		count = (count < 1)? 1 : count;
		for(int i=0;i<count;i++){
			os.write(data);
		}
		return rsp;
	}
	
	/**
	 * 发送数据，并且解析响应的数据
	 * @param data
	 * @param rsp_class
	 * @param count
	 * @return
	 * @throws Exception 
	 */
	public Message sendPB(byte[] data,String rsp_class,int count) throws Exception{
		Message rsp = null;
		count = (count < 1)? 1 : count;
		for(int i=0;i<count;i++){
			try {
				os.write(data);
			} catch (Exception e) {
				throw new Exception("write data failed!\n" + e.toString() + "\n" + e.getMessage());
			}
			byte[] parm = null;
			try {
				byte[] analyse_head_byte = new byte[12];
				br.read(analyse_head_byte);//获取前12个字节
				int[] parm_p = PBDataUtil.analyseHeadByte(analyse_head_byte);
				int head_len = parm_p[0];
				int parm_len = parm_p[1];
				
				byte[] head = new byte[head_len];
				parm = new byte[parm_len];
				br.read(head);
				br.read(parm);
			} catch (Exception e) {
				throw new Exception("read data failed!\n" + e.toString() + "\n" + e.getMessage());
			}
			
			rsp = PBDataUtil.getCommonParmFromByte(rsp_class,parm);
//				Message b = PBDataUtil.getCommonParmFromByte("PB_SrvHead",head);
//				System.out.println(rsp);
		}
		return rsp;
	}
	
	/**
	 * 关闭socket
	 */
	public void close_socket(){
		try {
			socket.shutdownOutput();
			is.close();
			os.close();
			br.close();
			socket.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
