import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.util.BitSet;


/*
 * Milan Pavlik s1115104
 */


public class Sender1 {
	
	private int PORT;
	private String FILENAME;
	private static int PAYLOAD_SIZE = 1024;
	private static int PACKET_SIZE = 1028;	// 1 KB
	private static int HEADER_SIZE = 4;	// 4 bytes
	
	public Sender1(int port, File file) {
		
		
		try {
			rdt_send(file);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
	}
	
	private BufferedReader readFile(String filename) throws FileNotFoundException {
		return new BufferedReader(new FileReader(filename));
	}
	
	public boolean rdt_send(File file) throws FileNotFoundException {
		FileInputStream fstream = new FileInputStream(file);
		
		long fsize = file.length();
		int chunkCount = (int) Math.floor(fsize / (double) PACKET_SIZE);
		
		byte[] buffer = new byte[PACKET_SIZE];
		
		// Submit each chunk
		for (int i = 0; i < chunkCount; i++) {
			try {
				fstream.read(buffer, 4, PAYLOAD_SIZE);
				byte[] packet = make_pkt(buffer, i);
				
				udt_send(packet);
				
			} catch (IOException e) {
				e.printStackTrace();
				System.err.println(String.format("Reading file failed at chunk %d", i));
				return false;
			}
		}

		return true;
	}
	
	private byte[] make_pkt(byte[] chunk, int sequenceNumber) {
		// prepend header
		byte[] header = ByteBuffer.allocate(4).putInt(sequenceNumber).array();
		for (int i = 0; i < HEADER_SIZE; i++)
			chunk[i] = header[i];
		
		return chunk;
	}
	
	private boolean udt_send(byte[] data) {
//		System.out.println("Chunk size: " + data[0]);
		return false;
	}

	public static void main(String[] args) {
		
		// Parse args
		try {
			int port = Integer.parseInt(args[0]);
			String filename = args[1];
			
			// Initialize file 
			File file = new File(filename);
			
			System.out.println("Starting Sender1.");
			
			// Start Sender
			Sender1 sender = new Sender1(port, file);
			
			
		} catch(IndexOutOfBoundsException e) {
			System.err.println("Incorect arguments. Valid arguments are <Port> <FileName> [RetryTimeout] [WindowSize]");
			return;
		} 
		
		
	}

}
