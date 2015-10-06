import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.util.Arrays;


public class Receiver1 {
	
	private static int PAYLOAD_SIZE = 1024;
	private static int PACKET_SIZE = 1028;	
	private static int HEADER_SIZE = 4;	// 4 bytes
	
	private ByteArrayOutputStream output;	
	private DatagramSocket socket;
	private File file;

	public Receiver1(int port, File file) {
		this.file = file;
		try {
			
			socket = new DatagramSocket(port);
			
			// Init buffered stream
			output = new ByteArrayOutputStream();
			rdt_rcv();
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (socket != null) socket.close();
		}
	}
	
	public void rdt_rcv() throws IOException {
		boolean listening = true;
		while (listening) {
			// Create packet
			byte[] buffer = new byte[PACKET_SIZE];
			DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
			
			// Receive the packet
			socket.receive(packet);
			
			// Extract data and determine if we need to receive more
			listening = extract(packet.getData());
		}
		
		// Deliver data to the top level application
		deliver_data();
	}
	
	public boolean extract(byte[] packet) {
		// Slice packet into a header
		byte[] byteHeader = Arrays.copyOfRange(packet, 0, 2);
		// Get the sequence number as an integer
		short sequenceNum = new BigInteger(byteHeader).shortValue();	

		System.out.printf("Received packet %d\n", sequenceNum);
		byte[] byteEof = Arrays.copyOfRange(packet, 3, 4);
		int eof = new BigInteger(byteEof).intValue();
		
		byte[] payload = Arrays.copyOfRange(packet, 4, packet.length);
		
//		System.out.printf("Packet %d has %d bytes.\n", sequenceNum, packet.length);
		
		// Write the payload, payload located at packet[3] - end of packet
		try {
			output.write(payload);
		} catch (IOException e) {
			e.printStackTrace();
			System.err.println("Could not write bytes into the output buffer.");
		}		
		
		return eof != 1;
	}
	
	public boolean deliver_data() {
		boolean success = false;
		try {
			// Set up streams
			FileOutputStream fileStream = new FileOutputStream(file);
			BufferedOutputStream bufferedOutput = new BufferedOutputStream(fileStream);
			
			System.out.println("Buffer has length " + output.size());
			
			// Write to a file
			bufferedOutput.write(output.toByteArray());
						
			// Housekeeping
			bufferedOutput.flush();
			bufferedOutput.close();
			success = true;
			output.close();
			
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
			System.err.println("Could not find file to write to.");
		} catch (IOException e) {
			e.printStackTrace();
			System.err.println("Writing to file failed.");
		} 
		return success;
	}
	
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		try {
			String host = args[0];
			int port = Integer.parseInt(args[1]);
			String filename = args[2];
			
			// Initialize file 
			File file = new File("receive.jpg");
			
			System.out.println("Starting Receiver1.");
			
			// Start Sender
			new Receiver1(port, file);
			
			
		} catch(IndexOutOfBoundsException e) {
			System.err.println("Incorect arguments. Valid arguments are <Port> <FileName> [RetryTimeout] [WindowSize]");
			return;
		} 
	}

}
