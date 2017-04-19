package pers.tunsuy.pts.net;

import java.nio.ByteOrder;

import com.google.protobuf.AbstractMessage;
import com.google.protobuf.AbstractParser;
import com.google.protobuf.InvalidProtocolBufferException;
import com.google.protobuf.Extension.MessageType;
import com.google.protobuf.MessageLite;
import com.sangfor.moa.protobuf.Auth.PB_AthLoginRsp;
import com.sangfor.moa.protobuf.Srvhead;

import pers.tunsuy.pts.common.MacroDefined;
import pers.tunsuy.pts.util.ByteUtils;
import pers.tunsuy.pts.util.StringUtils;
import pers.tunsuy.pts.model.UserData;
import pers.tunsuy.pts.model.UserDataBuilder;

public class PacketStructure {

	/**
	 * ���� ��Ϣ��ʾ�� 
	 * @param userData UserData����
	 * @return byte[] ����msgHead�ֽڴ�
	 */
	private static byte[] getMsgHeadBytesForPacket(UserData userData){
		if (null == userData) {
			return null;
		}
		
		int	srvcode_length = MacroDefined.MSGHEAD_SRVCODE_LENGTH;
		int	flag_length = MacroDefined.MSGHEAD_FLAG_LENGTH;
		int	len_length = MacroDefined.MSGHEAD_LEN_LENGTH;

		int msgHead_len = MacroDefined.MSGHEAD_LEN;
		
		//server�ֶ�����
		short srv_code = userData.getSrvCode();
		//flag�ֶ�����
		short flag = MacroDefined.MSGHEAD_FLAG_DEFAULT;
		//len�ֶ�����
		byte[] srvHead_bytes = getSrvHeadBytesForPacket(userData);
		int srvHead_len_len = getLenBytesOfPBSrvHeadForPacket(srvHead_bytes).length;
		int pkg_len = srvHead_len_len + msgHead_len;
		
		byte[] operateBytes = getOperationDataBytesForPacket(userData);
		if (null != operateBytes) {
			pkg_len += operateBytes.length;
		}
		
		byte[] pbSrvHeadBytes = getSrvHeadBytesForPacket(userData);
		if (null != pbSrvHeadBytes) {
			pkg_len += pbSrvHeadBytes.length;
		}

		//��װmsghead�ֽ�
		byte[] srv_code_bytes = ByteUtils.short2Bytes(srv_code,ByteOrder.BIG_ENDIAN);
		byte[] flag_bytes = ByteUtils.short2Bytes(flag,ByteOrder.BIG_ENDIAN);
		byte[] packet_len_bytes = ByteUtils.int2Bytes(pkg_len,ByteOrder.BIG_ENDIAN);

		byte[] msgHead_bytes = new byte[msgHead_len];

	    System.arraycopy(srv_code_bytes, 0, msgHead_bytes, 0, srvcode_length);  
	    System.arraycopy(flag_bytes, 0, msgHead_bytes, srvcode_length, flag_length);  
	    System.arraycopy(packet_len_bytes, 0, msgHead_bytes, srvcode_length+flag_length, len_length); 
	    
	    return msgHead_bytes;
	}

	/**
	 * ���� PBSrvHead 
	 * @param userData UserData����
	 * @return byte[] ����PBSrvHead�ֽڴ�
	 */
	private static byte[] getSrvHeadBytesForPacket(UserData userData) {
		//srvop�ֶ�����
		int srvop = userData.getSrvop();
		
		int srvop_code = srvopMark(userData.getSrvCode(), srvop);
		System.out.println("srvop_code: " + srvop_code);

		Srvhead.PB_SrvHead.Builder srvheadBuilder = Srvhead.PB_SrvHead.newBuilder();
        srvheadBuilder.setSrvop(srvop_code);
        
        Srvhead.PB_SrvHead srvHead = srvheadBuilder.build();
        byte[] srvHead_bytes = srvHead.toByteArray();

		return srvHead_bytes;
	}
	
    public static final int srvopMark(int srv, int op) {
        return (((srv) << 8) | op);
    }
	
	/**
	 * ���� PBSrvHead �ĳ���len
	 * @param PBSrvHead_bytes PBSrvHead�ֽڴ�
	 * @return byte[] ����PBSrvHead�ĳ����ֽڴ�
	 */
	private static byte[] getLenBytesOfPBSrvHeadForPacket(byte[] PBSrvHead_bytes) {
		if (null == PBSrvHead_bytes) {
			return null;
		}
		int PBSrvHead_len = PBSrvHead_bytes.length;
		byte[] PBSrvHead_len_bytes = ByteUtils.int2Bytes(PBSrvHead_len, ByteOrder.BIG_ENDIAN);
		
		return PBSrvHead_len_bytes;
	}

	/**
	 * ���� ҵ���
	 * @param userData UserData����
	 * @return byte[] ���ز��������ֽڴ�
	 */
	private static byte[] getOperationDataBytesForPacket(UserData userData) {
		byte[] operateBytes = UserDataBuilder.buildForOperationDataBytes(userData);
		return operateBytes;
	}
	
	/**
	 * ��ȡ�����������ݰ����ֽڴ�
	 * @param userData UserData����
	 * @return byte[] ���ط��Ͱ��ֽڴ�
	 */
	public static byte[] getPacketBytes(UserData userData) {
		if (null == userData) {
			return null;
		}
		
		byte[] operationDataBytes = getOperationDataBytesForPacket(userData);
        byte[] pbSrvHeadBytes = getSrvHeadBytesForPacket(userData);
        byte[] lenOfPbSrvHead = getLenBytesOfPBSrvHeadForPacket(pbSrvHeadBytes);
        byte[] msgHeadBytes = getMsgHeadBytesForPacket(userData);
        
        int pkg_len = operationDataBytes.length + pbSrvHeadBytes.length + lenOfPbSrvHead.length + msgHeadBytes.length;
        byte[] packetBytes = new byte[pkg_len];
        System.arraycopy(msgHeadBytes, 0, packetBytes, 0, msgHeadBytes.length);
        System.arraycopy(lenOfPbSrvHead, 0, packetBytes, msgHeadBytes.length, lenOfPbSrvHead.length);
        System.arraycopy(pbSrvHeadBytes, 0, packetBytes, msgHeadBytes.length+lenOfPbSrvHead.length, pbSrvHeadBytes.length);
        System.arraycopy(operationDataBytes, 0, packetBytes, msgHeadBytes.length+lenOfPbSrvHead.length+pbSrvHeadBytes.length, operationDataBytes.length);
        
        return packetBytes;
	}
	
	/**
	 * test
	 * */
	public static void showRspForRecvPkg(byte[] rspBytes) {
		if (null == rspBytes) {
			return;
		}
		
//		Class<?> InfRspClass = null;
//		try {
//			String infRspClassName = MacroDefined.MOA_PROTO_PKG + "." + StringUtils.toUpperCaseFirstOne(userData.getPbName()) + "$" + userData.getInfRspName();
//			InfRspClass = Class.forName(infRspClassName);
//		
//		} catch (ClassNotFoundException e) {
//			// TODO: handle exception
//			e.printStackTrace();
//		}
		
		try {
			PB_AthLoginRsp rsp = PB_AthLoginRsp.parseFrom(rspBytes);
			System.out.println("rsp: " + rsp);
		} catch (InvalidProtocolBufferException e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		
	}
}
