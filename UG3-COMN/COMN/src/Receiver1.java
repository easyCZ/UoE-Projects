import java.io.File;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;


public class Receiver1 {
	
	private static int PAYLOAD_SIZE = 1024;
	private static int PACKET_SIZE = 1028;	
	private static int HEADER_SIZE = 4;	// 4 bytes
	
	private DatagramSocket socket;

	public Receiver1(int port, File file) {
		try {
			
			socket = new DatagramSocket(port);
			
			rdt_rcv();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void rdt_rcv() throws IOException {
		// Create packet
		byte[] buffer = new byte[PACKET_SIZE];
		DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
		socket.receive(packet);
		
		for (byte b: buffer)
			System.out.println(b);
	}
	
	public byte[] extract(DatagramPacket packet) {
		return new byte[10];
	}
	
	public boolean deliver_data(File file) {
		return true;
	}
	
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		try {
			String host = args[0];
			int port = Integer.parseInt(args[1]);
			String filename = args[2];
			
			// Initialize file 
			File file = new File("received_file.jpg");
			
			System.out.println("Starting Receiver1.");
			
			// Start Sender
			new Receiver1(port, file);
			
			
		} catch(IndexOutOfBoundsException e) {
			System.err.println("Incorect arguments. Valid arguments are <Port> <FileName> [RetryTimeout] [WindowSize]");
			return;
		} 
	}

}
