import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigInteger;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.BitSet;


/*
 * Milan Pavlik s1115104
 */


public class Sender1 {
	
	private int PORT;
	private String FILENAME;
	private static int PAYLOAD_SIZE = 1024;
	private static int PACKET_SIZE = 1028;	
	private static int HEADER_SIZE = 4;	// 4 bytes
	
	private DatagramSocket socket;
	private InetSocketAddress address;
	
	public Sender1(int port, File file) {
		
		try {
			socket = new DatagramSocket();
			
			address = new InetSocketAddress("localhost", port);
			
			rdt_send(file);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			System.err.println("Failed to create datagram socket. Exiting.");
			return;
		} finally {
			if (socket != null) socket.close();
		}
	}
	
	public boolean rdt_send(File file) throws FileNotFoundException {
		FileInputStream fstream = new FileInputStream(file);
		
		long fsize = file.length();
		int chunkCount = (int) Math.floor(fsize / (double) PACKET_SIZE);
		
		byte[] buffer = new byte[PACKET_SIZE];
		
		// Submit each chunk
		try {
			for (int i = 0; i < chunkCount; i++) {
			
				fstream.read(buffer, 4, PAYLOAD_SIZE);
				byte[] packet = make_pkt(buffer, i, i == chunkCount-1);
				
				udt_send(packet);
			}
			
			fstream.close();
				
		} catch (IOException e) {
			e.printStackTrace();
			System.err.println(String.format("Sending file failed."));
			return false;
		}
		return true;
	}
	
	private byte[] make_pkt(byte[] chunk, int sequenceNumber, boolean isLast) {
		
		
//		BigInteger bi = BigInteger.valueOf(sequenceNumber);
//		byte[] bytes = bi.toByteArray();
		
		// prepend header
		byte[] header = BigInteger.valueOf(sequenceNumber).toByteArray();
		
		int len = Math.min(header.length, 3);
		
		for (int i = 0; i < len; i++)
			chunk[i] = header[i];
		
		byte[] byteHeader = Arrays.copyOfRange(chunk, 0, 3);
		// Get the sequence number as an integer
		int sequenceNum = new BigInteger(byteHeader).intValue();
		System.out.printf("%d vs %d\n",sequenceNumber, sequenceNum);
		
		
		chunk[3] = isLast ? (byte) 1 : (byte) 0;
		
		if (isLast)
			System.out.printf("Chunk %d is the LAST one!\n", sequenceNumber);
		
		return chunk;
	}
	
	private boolean udt_send(byte[] data) throws IOException {
		DatagramPacket packet = new DatagramPacket(data, data.length, address);
		System.out.println("Sending packet.");
		socket.send(packet);
		return false;
	}

	public static void main(String[] args) {
		
		// Parse args
		try {
			String host = args[0];
			int port = Integer.parseInt(args[1]);
			String filename = args[2];
			
			// Initialize file 
			File file = new File(filename);
			
			System.out.println("Starting Sender1.");
			
			// Start Sender
			Sender1 sender = new Sender1(port, file);
			
			
		} catch(IndexOutOfBoundsException e) {
			e.printStackTrace();
			System.err.println("Incorect arguments. Valid arguments are <Port> <FileName> [RetryTimeout] [WindowSize]");
			return;
		} 
		
		
	}

}
