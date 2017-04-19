package pers.tunsuy.pts.common;

public class MacroDefined {
	/**
	* 消息协议：MsgHead + PBSrvHead + OperationalData
	*
	* MsgHead(消息表示层): server(16位) + flag(16位) + len(32位)
	* ——server表示服务码，占用2个字节；
	* ——flag表示一些标志位，占用2个字节；
	* ——len表示整个包的长度，占用4个字节。
	*
	* PBSrvHead(路由分发层): len(32位) + srvop(32位) + ...
	* ——len表示srvhead的长度，占用4个字节；
	* ——srvop表示服务操作码，占用4个字节。
	*
	* OperationalData(业务层)
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

	//包的最小长度：至少包含消息头中必须的字段
	public static final int PACKET_LENGTH_MIN = 2 + 2 + 4 + 4;
	public static final int PACKET_LEN_START_INDEX = 4;
	public static final int PACKET_LEN_LENGTH = 4;
	public static final int SRVHEAD_LEN_START_INDEX = 2 + 2 + 4;

	public static final int BUFFER_CAP = 5 * 1024;

	public static final String MOA_PROTO_PKG = "com.sangfor.moa.protobuf";
	
	
}
