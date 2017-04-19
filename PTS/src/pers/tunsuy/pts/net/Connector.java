package pers.tunsuy.pts.net;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.channels.SocketChannel;

import javax.crypto.Mac;

import pers.tunsuy.pts.common.MacroDefined;
import pers.tunsuy.pts.model.UserData;
import pers.tunsuy.pts.util.ByteUtils;

public class Connector {
	public Connector(String ip, int port) {
		this.ip = ip;
		this.port = port;
		this.recvData = new byte[MacroDefined.BUFFER_CAP];
		this.recvDataIndex = 0;
	}
	
	public void openSocketChannel(UserData userData) throws IOException, InterruptedException {
		if (null == userData) {
			return;
		}
		
		this.client = SocketChannel.open();
		this.client.connect(new InetSocketAddress(this.ip, this.port));
		
		//非阻塞模式
//		this.client.configureBlocking(false);
//		while(! this.client.finishConnect() ){
//		    //wait, or do something else...    
//		}
		
		writeDataToNet(userData);
	}
	
	public void closeSocketChannel() throws IOException {
		this.client.close();
	}
	
	private void writeDataToNet(UserData userData) throws IOException, InterruptedException {
		System.out.println("start write data to server");
		
		if (null == userData) {
			return;
		}
		
		byte[] packetBytes = PacketStructure.getPacketBytes(userData);
		if (null == packetBytes) {
			return;
		}
		
		ByteBuffer writeBuffer = ByteBuffer.allocate(packetBytes.length);
		writeBuffer.clear();  //pos = 0, limit = cap
		writeBuffer.put(packetBytes);
		writeBuffer.flip();  //limit = pos, pos = 0
		
		while (writeBuffer.hasRemaining()) {
			this.client.write(writeBuffer);
		}
		
		readDataFromNet();
		
	}
	
	private void readDataFromNet() throws IOException, InterruptedException {
		System.out.println("start read data from server");
		
		ByteBuffer readBuffer = ByteBuffer.allocate(this.recvData.length);
		
		
		while (true) {
			readBuffer.clear();
			int byteRead = this.client.read(readBuffer);
			System.out.println("byteRead: " + byteRead);
			
			if (byteRead == -1) {
				return;
			}
			
			if (! readBuffer.hasArray()) {
				return;
			}
			
			byte[] data = readBuffer.array();	
			
			//将该次接收到的data组装到recvData的后面
			System.arraycopy(data, 0, this.recvData, this.recvDataIndex, byteRead);
			this.recvDataIndex += byteRead;
			
			while (true) {
				if (this.recvDataIndex < MacroDefined.PACKET_LENGTH_MIN) {  //不是一个完整的包
					return;
				}
				
				int packetLen = getLenOfPacketFromRecvData(this.recvData);
				System.out.println("packet len: " + packetLen);
				if (this.recvDataIndex < packetLen) { //不是一个完整的包
					return;
				}
				
				//收到一个完整的包
				byte[] packetBytes = new byte[packetLen];
				System.arraycopy(this.recvData, 0, packetBytes, 0, packetLen); //提取出完整的包
				//从recvData中去掉已提取的完整的包
				System.arraycopy(new byte[packetLen], 0, this.recvData, 0, packetLen);
				System.arraycopy(this.recvData, packetLen, this.recvData, 0, this.recvData.length - packetLen);
				this.recvDataIndex -= packetLen;
				
				analysisRecvPacketBytes(packetBytes);
			}
		}
		
	}
	
	private int getLenOfPacketFromRecvData(byte[] recvData) {
		System.out.println("get len of packet from recv data");
		if (null == recvData) {
			return 0;
		}
		
		//从recvData中提取出len字段: msgHead中的len字段
		byte[] lenBytes = new byte[MacroDefined.MSGHEAD_LEN_LENGTH];
		int len_index_start = MacroDefined.MSGHEAD_SRVCODE_LENGTH + MacroDefined.MSGHEAD_FLAG_LENGTH;
		System.arraycopy(recvData, len_index_start, lenBytes, 0, MacroDefined.MSGHEAD_LEN_LENGTH);
		
		int len = ByteUtils.bytes2Int(lenBytes, ByteOrder.BIG_ENDIAN);
		
		return len;
	}
	
	private void analysisRecvPacketBytes(byte[] packetBytes) {
		System.out.println("analysis recv packet");
		if (null == packetBytes) {
			return;
		}
		for (int i = 0; i < packetBytes.length; i++) {
			System.out.printf("0x%02X", packetBytes[i]);
		}
		//从packetBytes中提取出len字段：srvHead的长度
		byte[] lenBytes = new byte[MacroDefined.SRVHEAD_LEN_LENGTH];
		System.arraycopy(packetBytes, MacroDefined.SRVHEAD_LEN_START_INDEX, lenBytes, 0, MacroDefined.SRVHEAD_LEN_LENGTH);
		int len = ByteUtils.bytes2Int(lenBytes, ByteOrder.BIG_ENDIAN);
		System.out.println("len: " + len);
		
		//提取操作数据
		int operationDataLen = packetBytes.length - MacroDefined.PACKET_LENGTH_MIN - len;
		byte[] operationData = new byte[operationDataLen];
		System.arraycopy(packetBytes, packetBytes.length-operationDataLen, operationData, 0, operationDataLen);
		System.out.println("operationData: " + operationData.length);
		
		PacketStructure.showRspForRecvPkg(operationData);
	}
	
	private String ip;
	private int port;
	
	private SocketChannel client;
	
	private byte[] recvData;
	private int recvDataIndex;
	
}
