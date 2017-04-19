package pers.tunsuy.pts.util;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/**
 * 
 * @author 
 *
 */

public class ByteUtils {
	
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
    
    /**
     * @param len
     * @param mark
     * @return
     */
    public static byte[] initBytes(int len,byte mark){
        if(mark==0){
            mark = (byte)0XFF;
        }
        byte[] bt = new byte[len];
        for(int i=0;i<bt.length;i++){
            bt[i] = mark;
        }
        return bt;
    }
    
    /**
     * @param bt
     * @param mark
     * @return
     */
    public static int bytesLen(byte[] bt,byte mark){
        if(mark==0){
            mark = (byte)0XFF;
        }
        int len = 0;
        if(bt!=null){
            for(int i=0;i<bt.length;i++){
                byte b = bt[i];
                if(b!=mark){
                    len ++;
                }else{
                    break;
                }
            }
        }
        return len;
    }

}

