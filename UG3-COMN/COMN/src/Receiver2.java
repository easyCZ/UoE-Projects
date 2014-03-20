import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.nio.ByteBuffer;
import java.util.Arrays;


public class Receiver2 {
	
	private int PORT_OFFSET = 5;
	
	private static int PAYLOAD_SIZE = 1024;
	private static int PACKET_SIZE = 1026;	
	private static int HEADER_SIZE = 2;	// 2 bytes
	private static int ACK_SIZE = 1;
	
	private byte nextAckNum = (byte) 0; 
	
	private ByteArrayOutputStream output;	
	private DatagramSocket socket;
	private File file;
	private int timeout;

	public Receiver2(int port, File file, int timeout) {
		this.file = file;
		this.timeout = timeout;
		try {
			// Bind a socket to port
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
	
	public void rdt_rcv() {
		boolean listening = true;
		while (listening) {
			// Create packet
			byte[] buffer = new byte[PACKET_SIZE];
			DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
			
			boolean packetReceived = false;
			do {
				// Receive the packet
				try {
					socket.receive(packet);
					
					if (buffer[0] == nextAckNum) {
						packetReceived = true;
						send_response(packet.getAddress(), packet.getPort(), nextAckNum);
//						System.out.println("ACK sent.");
					} else
						// Send ACK for previous
//						System.out.println("Sending an ACK for previous packet.");
						send_response(packet.getAddress(), packet.getPort(), (byte) (((int)nextAckNum + 1) % 2));
				} catch (SocketTimeoutException e) {
//					System.out.println("Receive packet timout occured. NAK sent.");
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} while (!packetReceived);
			
			// Flip the nextAckNumber 
			nextAckNum = (byte) (((int) nextAckNum + 1) % 2);			
			
			// Extract data and determine if we need to receive more
			listening = extract(packet.getData());
		}
		
		// Deliver data to the top level application
		deliver_data();
	}
	
	private void send_response(InetAddress address, int port, byte content) {
		if (port != -1) {
			InetSocketAddress socketAddr = new InetSocketAddress(address, port);
			byte[] data = {content};
			DatagramPacket packet;
			try {
				packet = new DatagramPacket(data, data.length, socketAddr);
				socket.send(packet);
//				System.out.println("Sent a NAK");
			} catch (SocketException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	
	
	public boolean extract(byte[] packet) {
		// Slice packet into a header
		byte sequenceNum = packet[0];
		int eof = (int) packet[1];
		
		byte[] payload = Arrays.copyOfRange(packet, 2, packet.length);
		
		System.out.printf("Packet %d has %d bytes.\n", sequenceNum, packet.length);
		
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
			
//			System.out.println("Buffer has length " + output.size());
			
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
			int timeout = Integer.parseInt(args[3]);
			
			// Initialize file 
			File file = new File("receive.jpg");
			
			System.out.println("Starting Receiver1.");
			
			// Start Sender
			new Receiver2(port, file, timeout);
			
			
		} catch(IndexOutOfBoundsException e) {
			System.err.println("Incorect arguments. Valid arguments are <Port> <FileName> [RetryTimeout] [WindowSize]");
			return;
		} 
	}

}
