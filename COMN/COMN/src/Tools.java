import java.nio.ByteBuffer;


public class Tools {
	
	public static byte[] intToByteArray(short i) {
		ByteBuffer buffer = ByteBuffer.allocate(2);
		buffer.putShort(i);
		return buffer.array();
	}
	
	public static int byteArrayToInt(byte[] data) {
		ByteBuffer bb = ByteBuffer.wrap(data);
	    return bb.getShort();
	}

}
