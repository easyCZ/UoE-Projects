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
	
	private DatagramSocket socket;
	private InetSocketAddress address;
	
	public Sender1(int port, File file) {
		
		try {
			socket = new DatagramSocket();
		} catch (IOException e) {
			System.err.println("Failed to create datagram socket. Exiting.");
			return;
		}
		
		address = new InetSocketAddress("localhost", port);
		
		System.out.println(address == null);
		
		
		try {
			rdt_send(file);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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
				byte[] packet = make_pkt(buffer, i);
				
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
	
	private byte[] make_pkt(byte[] chunk, int sequenceNumber) {
		// prepend header
		byte[] header = ByteBuffer.allocate(4).putInt(sequenceNumber).array();
		for (int i = 0; i < HEADER_SIZE; i++)
			chunk[i] = header[i];
		
		return chunk;
	}
	
	private boolean udt_send(byte[] data) throws IOException {
		DatagramPacket packet = new DatagramPacket(data, data.length);
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
			System.err.println("Incorect arguments. Valid arguments are <Port> <FileName> [RetryTimeout] [WindowSize]");
			return;
		} 
		
		
	}

}
