import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigInteger;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.concurrent.ConcurrentHashMap;


public class Sender3 {
	
	private int ACK_SIZE = 2;
	private int PAYLOAD_SIZE = 1024;
	private int PACKET_SIZE = 1027;
	private int HEADER_SIZE = PACKET_SIZE - PAYLOAD_SIZE;
	
	private int timeout;
	private int windowSize;
	
	private DatagramSocket socket;
	private DatagramSocket ackSocket;
	private InetSocketAddress address;
	
	private boolean listening = true;
	private ACKReceiverThread ACKReceiver;
	
	private ConcurrentHashMap<Integer, DatagramPacket> packetBuffer;
	
	private int highestACKReceived = -1;

	public Sender3(int port, File file, int timeout, int windowSize) {
		this.timeout = timeout;
		this.windowSize = windowSize;
		
		packetBuffer = new ConcurrentHashMap<Integer, DatagramPacket>();
		
		
		try {
			socket = new DatagramSocket();
			address = new InetSocketAddress("localhost", port);
			
			ackSocket = new DatagramSocket(socket.getLocalPort() - 10);
			
			PacketSenderThread packetSender = new PacketSenderThread(file);
			packetSender.start();
			ACKReceiver = new ACKReceiverThread();
			ACKReceiver.start();
//			rdt_send(file);
		} catch (IOException e) {
			System.err.println("Error creating socket. Exiting.");
			System.exit(0);
		}
		
		ACKReceiver = new ACKReceiverThread();
		ACKReceiver.run();
		
	}
	
	
	
	private class PacketSenderThread extends Thread {
		
		private File file;
		
		private PacketSenderThread(File file) {
			this.file = file;
			
		}
		
		public void run() {
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
			int chunkCount = (int) Math.ceil(fsize / (double) PAYLOAD_SIZE);
			int i = 0;
			// Submit each chunk
			try {
				while (i < chunkCount) {
					
					if (packetBuffer.size() <= windowSize) {
						System.out.println("Window size: " + packetBuffer.size());
						byte[] buffer = new byte[PACKET_SIZE];
						
						fstream.read(buffer, HEADER_SIZE, PAYLOAD_SIZE);
						DatagramPacket packet = make_pkt(buffer, i, i == chunkCount-1);
						
						packetBuffer.put(i, packet);
						
						System.out.printf("Sending packet # %d\n", i);
						udt_send(packet);
						i += 1;
					}
					try {
						Thread.sleep(200);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				
				fstream.close();
					
			} catch (IOException e) {
				e.printStackTrace();
				System.err.println(String.format("Sending file failed."));
				return false;
			}
			return true;
		}
		
		private DatagramPacket make_pkt(byte[] chunk, int sequenceNumber, boolean isLast) {
			
			short seq = (short) sequenceNumber;
			
			// Set sequence header		
			byte[] byteSequence = Tools.intToByteArray(seq);
			for (int i = 0; i < byteSequence.length; i++)
				chunk[i] = byteSequence[i];
				
			
			chunk[3] = isLast ? (byte) 1 : (byte) 0;
			
			if (isLast)
				System.out.printf("Chunk %d is the LAST one!\n", sequenceNumber);
			
			DatagramPacket packet;
			try {
				packet = new DatagramPacket(chunk, chunk.length, address);
				return packet; 
			} catch (SocketException e) {
				e.printStackTrace();
			}
			
			return null; 
		}
		
		private boolean udt_send(DatagramPacket packet) throws IOException {
			socket.send(packet);
			return false;
		}
		
	}
	
	private class ACKReceiverThread extends Thread {
		
		public void run() {
			System.out.println("Running ACK Receiver.");
			while (listening) {
				byte[] buffer = new byte[ACK_SIZE];
				DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
				
				// Receive the ACK
				try {
					System.out.println(">>> Waiting for an ACK...");
					ackSocket.setSoTimeout(timeout);
					ackSocket.receive(packet);
					
					int ackNumber = getACKNumber(packet.getData());
					
					System.out.println("Current buffer size: " + packetBuffer.size());
					
					System.out.println("ACK received: " + ackNumber);
					System.out.println("Highest ACK so far: " + highestACKReceived);
					
					if (ackNumber == highestACKReceived + 1) {
						highestACKReceived = ackNumber;
						for (Integer key: packetBuffer.keySet()) {
							if (key <= highestACKReceived + 1) {
								packetBuffer.remove(key);
								System.out.println("Removing key: " + key);
							}
						}
						System.out.println("Removing from buffer.");
					}
					
					System.out.println("Received packet. Current highest ACK is " + highestACKReceived);
				} catch (SocketTimeoutException e) {
					System.err.println("Socket timeout occured.");
				} catch (IOException e) {
					System.err.println("IO Exception on ACK ReceiverThread occured. Exiting.");
					System.exit(0);
				}
			}
		}
		
		private int getACKNumber(byte[] data) {
			return new BigInteger(data).shortValue();
		}
		
		
		
	}
	
	public static void main(String[] args) {
		// Parse arguments from the command line
		try {
			String host = args[0];
			int port = Integer.parseInt(args[1]);
			String filename = args[2];
			int timeout = Integer.parseInt(args[3]);
			int windowSize = Integer.parseInt(args[4]);
			
			// Init file
			File file = new File(filename);
			
			// Start sender
			new Sender3(port, file, timeout, windowSize);
		} catch (IndexOutOfBoundsException e) {
			e.printStackTrace();
			System.err.println("Incorect arguments. Valid arguments are <Host> <Port> <FileName> [RetryTimeout] [WindowSize]");
			return;
		}

	}

}
