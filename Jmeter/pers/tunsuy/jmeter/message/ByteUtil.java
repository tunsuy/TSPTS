package com.sangfor.moa.cch.message;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/**
 * 
 * @author cch
 *
 */

public class ByteUtil {
	
	public static byte[] short2Bytes(short x,ByteOrder byteOrder) {  
        ByteBuffer buffer = ByteBuffer.allocate(2);  
        buffer.order(byteOrder);  
        buffer.putShort(x);  
        return buffer.array();
    }
	
    public static byte[] short2Bytes(short x) {  
        ByteBuffer buffer = ByteBuffer.allocate(2);  
        buffer.order(ByteOrder.BIG_ENDIAN);  
        buffer.putShort(x);	
        return buffer.array();
    }
    
    public static byte[] int2Bytes(int x,ByteOrder byteOrder) {  
        ByteBuffer buffer = ByteBuffer.allocate(4);  
        buffer.order(byteOrder);  
        buffer.putInt(x);
        return buffer.array();  
    }
    
    public static byte[] long2Bytes(long x,ByteOrder byteOrder) {  
        ByteBuffer buffer = ByteBuffer.allocate(8);
        buffer.order(byteOrder);
        buffer.putLong(x);
        return buffer.array();
    }
    
    public static short bytes2Short(byte[] src,ByteOrder byteOrder){  
        ByteBuffer buffer = ByteBuffer.wrap(src);
        buffer.order(byteOrder);
        return buffer.getShort();
    }
    
    public static int bytes2Int(byte[] src,ByteOrder byteOrder){  
        ByteBuffer buffer = ByteBuffer.wrap(src);
        buffer.order(byteOrder);
        return buffer.getInt();
    }
    
    public static long bytes2Long(byte[] src,ByteOrder byteOrder){  
        ByteBuffer buffer = ByteBuffer.wrap(src);
        buffer.order(byteOrder);
        return buffer.getLong();
    }

}
