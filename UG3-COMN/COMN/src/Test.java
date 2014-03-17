import java.math.BigInteger;
import java.util.Arrays;


public class Test {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		for (int i = 0; i < 2500; i++) {
			byte[] bytes = Tools.intToByteArray((short)i);
			int k = Tools.byteArrayToInt(bytes);
			
			System.out.printf("i: %d, k: %d\n", i, k);
		}

	}

}
