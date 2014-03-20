import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketTimeoutException;
import java.nio.ByteBuffer;
import java.util.TimerTask;


/*
 * Milan Pavlik s1115104
 */


public class Sender2 {
	
	private static int PAYLOAD_SIZE = 1024;
	private static int PACKET_SIZE = 1026;	
	private static int HEADER_SIZE = 2;	// 2 bytes
	private static int ACK_SIZE = 1;
	
	private byte nextAckNum = (byte) 0;
	
	private DatagramSocket socket;
	private InetSocketAddress address;
	
	private int timeout;	// Timeout in ms
	
	private int retransmissionCount = 0;
	private long execStartTime;
	private long execEndTime;
	
	public Sender2(int port, File file, int timeout) {
		this.timeout = timeout;
		try {
			// Create socket bound to random port
			socket = new DatagramSocket();
			
			// Receiver location
			address = new InetSocketAddress("localhost", port);
			
			execStartTime = System.currentTimeMillis();
			rdt_send(file);
			execEndTime = System.currentTimeMillis();
			double timeElapsed = (execEndTime - execStartTime) / 1000.0;
			System.out.println("Transmission time: " + timeElapsed + " seconds");
			
			long bytesSent = retransmissionCount * PACKET_SIZE + file.length();
			System.out.println("Bytes sent: " + bytesSent);
			double throughput = bytesSent / timeElapsed;
			System.out.println("Throughput: " + throughput / 1024 + " KB/s");
			System.out.println("# of retransmissions: " + retransmissionCount);
			
		} catch (FileNotFoundException e) {
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
		int chunkCount = (int) Math.floor(fsize / (double) PAYLOAD_SIZE);
		long remainingSize = fsize;
		// Submit each chunk
		try {
			for (short i = 0; i <= chunkCount; i++) {
				
				System.out.println("Remaining size:" + remainingSize);
				
				byte[] buffer = new byte[PACKET_SIZE];
			
				fstream.read(buffer, HEADER_SIZE, PAYLOAD_SIZE);
				byte[] packet = make_pkt(buffer, nextAckNum, i == chunkCount, i);
				
				System.out.printf("Sending packet # %d\n", i);
				udt_send(packet);
				
				remainingSize -= buffer.length - HEADER_SIZE;
			}
			
			fstream.close();
				
		} catch (IOException e) {
			e.printStackTrace();
			System.err.println(String.format("Sending file failed."));
			return false;
		}
		return true;
	}
		
	/**
	 * Make a full packet including headers out of the data and header info.
	 * 
	 * @param chunk				data to send
	 * @param sequenceNumber	the sequence number
	 * @param isLast			is it the last packet?
	 * @return the full packet
	 */
	private byte[] make_pkt(byte[] chunk, byte ackState, boolean isLast, int sequenceNumber) {
		
		// Set sequence header
		chunk[0] = ackState;		
		
		// Set the last packet header
		chunk[1] = isLast ? (byte) 1 : (byte) 0;
		
		if (isLast)
			System.out.printf("Chunk %d is the LAST one!\n", sequenceNumber);
		
		return chunk;
	}
	
	private void udt_send(byte[] data) throws IOException {
		DatagramPacket packet = new DatagramPacket(data, data.length, address);
//		System.out.println("Sending packet.");
		socket.send(packet);
//		System.out.println(socket.getLocalPort());
		
		long timeoutTime = System.nanoTime() + 1000000  * timeout;
		
		// Keep sending the packet until we get an ACK
		while (!rdt_rcv()) {
			// If our timer has timed out, resend the packet. Otherwise wait
			if (timeoutTime - System.nanoTime() < 0) {
				socket.send(packet);
				retransmissionCount += 1;
			}
		}
		
		// Flip sequence number
		nextAckNum = (byte) ((nextAckNum + 1) % 2);
	}
	
	private boolean rdt_rcv() {
		byte[] buffer = new byte[ACK_SIZE];
		DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
		// Receive the ACK
		try {			
			socket.setSoTimeout(timeout);
			socket.receive(packet);
			
			boolean isack = isACK(packet.getData());
//			System.out.println("ACK received.");
			return isack;
		} catch (SocketTimeoutException e) {
			System.out.println("Receiving an ACK timed out. Resending the packet.");
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	private boolean isACK(byte[] packet) {
		return packet.length == 1 && packet[0] == nextAckNum;
	}

	public static void main(String[] args) {
		
		// Parse args
		try {
			String host = args[0];
			int port = Integer.parseInt(args[1]);
			String filename = args[2];
			int timeout = Integer.parseInt(args[3]);
			
			// Initialize file 
			File file = new File(filename);
			
//			System.out.println("Starting Sender1.");
			
			// Start Sender
			new Sender2(port, file, timeout);
			
		} catch(IndexOutOfBoundsException e) {
			e.printStackTrace();
			System.err.println("Incorect arguments. Valid arguments are <Port> <FileName> [RetryTimeout] [WindowSize]");
			return;
		} 
		
		
	}

}
