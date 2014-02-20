import java.math.BigInteger;
import java.util.Arrays;


public class Test {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		for (int i = 0; i < 2500; i++) {
			byte[] header = BigInteger.valueOf(i).toByteArray();
			
			byte[] seq;
			if (header.length == 1)
				seq = new byte[] {(byte) 0, (byte) 0, header[0]};
			else
				seq = new byte[] {(byte) 0, header[1], header[0]};
			
			for (int j = header.length -1; j >= 0; j--)
				seq[j] = header[j];
			
			int sequenceNum = new BigInteger(seq).intValue();
			
			System.out.printf("i: %d, seq: %d, bytes: %s, len: %d\n", 
					i, sequenceNum, Arrays.toString(seq), header.length);
		}

	}

}
