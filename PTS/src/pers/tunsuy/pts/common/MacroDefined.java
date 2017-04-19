package pers.tunsuy.pts.common;

public class MacroDefined {
	/**
	* ��ϢЭ�飺MsgHead + PBSrvHead + OperationalData
	*
	* MsgHead(��Ϣ��ʾ��): server(16λ) + flag(16λ) + len(32λ)
	* ����server��ʾ�����룬ռ��2���ֽڣ�
	* ����flag��ʾһЩ��־λ��ռ��2���ֽڣ�
	* ����len��ʾ�������ĳ��ȣ�ռ��4���ֽڡ�
	*
	* PBSrvHead(·�ɷַ���): len(32λ) + srvop(32λ) + ...
	* ����len��ʾsrvhead�ĳ��ȣ�ռ��4���ֽڣ�
	* ����srvop��ʾ��������룬ռ��4���ֽڡ�
	*
	* OperationalData(ҵ���)
	*/

	/** MsgHead */
	public static final int MSGHEAD_SRVCODE_LENGTH = 2;
	public static final int MSGHEAD_FLAG_LENGTH = 2;
	public static final int MSGHEAD_LEN_LENGTH = 4;
	
	public static final int MSGHEAD_LEN = MSGHEAD_SRVCODE_LENGTH + MSGHEAD_FLAG_LENGTH + MSGHEAD_LEN_LENGTH;

	public static final int MSGHEAD_FLAG_DEFAULT = 1;

	/** PBSrvHead */
	public static final int SRVHEAD_LEN_LENGTH = 4;
	public static final int SRVHEAD_SRVOP_LENGTH = 4;	

	//������С���ȣ����ٰ�����Ϣͷ�б�����ֶ�
	public static final int PACKET_LENGTH_MIN = 2 + 2 + 4 + 4;
	public static final int PACKET_LEN_START_INDEX = 4;
	public static final int PACKET_LEN_LENGTH = 4;
	public static final int SRVHEAD_LEN_START_INDEX = 2 + 2 + 4;

	public static final int BUFFER_CAP = 5 * 1024;

	public static final String MOA_PROTO_PKG = "com.sangfor.moa.protobuf";
	
	
}
