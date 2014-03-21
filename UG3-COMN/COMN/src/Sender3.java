import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOError;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.Arrays;
import java.util.concurrent.ConcurrentHashMap;


public class Sender3 {
	
	private int ACK_SIZE = 2;
	private int PAYLOAD_SIZE = 1024;
	private int HEADER_SIZE = 3;
	private int PACKET_SIZE = PAYLOAD_SIZE + HEADER_SIZE;
	
	private int socketTimeout;
	private int windowSize;
	
	private boolean isListening = true;
	private int maxACKreceived = -1;
	private int totalPackets = -1;
	private int base;
	
	private DatagramSocket socket;
	private InetSocketAddress address;
	
	private long execStartTime;
	private long execEndTime;
	
	private ConcurrentHashMap<Integer, DatagramPacket> packetBuffer;

	public Sender3(int port, File file, int timeout, int windowSize) {
		this.socketTimeout = timeout;
		this.windowSize = windowSize;
		
		packetBuffer = new ConcurrentHashMap<Integer, DatagramPacket>();
		
		try {
			socket = new DatagramSocket();
			address = new InetSocketAddress("localhost", port);
		} catch (SocketException e) {
			System.err.println("Socket already exists.");
			System.exit(0);
		}
		
		PacketSender packetSender = new PacketSender(file);
		ACKListener ackListener = new ACKListener();
		
		packetSender.start();
		ackListener.start();
	}
	
	private class PacketSender extends Thread {
		
		private File file;
		
		public PacketSender(File file) {
			this.file = file;
		}
		
		public void run() {	
			try {
				execStartTime = System.currentTimeMillis();
				rdt_send(file);
				execEndTime = System.currentTimeMillis();
				double timeElapsed = (execEndTime - execStartTime) / 1000.0;				
				double throughput = file.length() / timeElapsed;
				System.out.println("Throughput: " + throughput / 1024 + " KB/s");;
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		public boolean rdt_send(File file) throws FileNotFoundException {
			FileInputStream fstream = new FileInputStream(file);
			
			// Calculate the number of chunks needed.
			long fsize = file.length();
			int chunkCount = (int) Math.ceil(fsize / (double) PAYLOAD_SIZE);
			totalPackets = chunkCount;
			int i = 0;
			
			// Submit each chunk
			try {
				while (i < chunkCount) {
					
					if (packetBuffer.size() <= windowSize) {
//						System.out.println("Window size: " + packetBuffer.size());
						byte[] buffer = new byte[PACKET_SIZE];
						
						int readSize = fstream.read(buffer, HEADER_SIZE, PAYLOAD_SIZE);
						
						if (readSize < PACKET_SIZE) {
							buffer = Arrays.copyOfRange(buffer, 0, readSize + 3);
						}
						
						DatagramPacket packet = make_pkt(buffer, i, i == chunkCount-1);
						
						packetBuffer.put(i, packet);
						
						System.out.printf("Sending packet # %d\n", i);
						udt_send(packet);
						i += 1;
						
					} else {
//						System.out.println("*** Frame is full, waiting for ACKs");
					}
				}
				isListening = false;
				fstream.close();
					
			} catch (IOException e) {
				e.printStackTrace();
				System.err.println(String.format("Sending file failed."));
				return false;
			}
			return true;
		}
		
		public DatagramPacket make_pkt(byte[] chunk, int sequenceNumber, boolean isLast) {
			
			// Set sequence header		
			byte[] byteSequence = Tools.intToByteArray((short) sequenceNumber);
			for (int i = 0; i < byteSequence.length; i++)
				chunk[i] = byteSequence[i];
				
			chunk[2] = isLast ? (byte) 1 : (byte) 0;
			
//			if (isLast)
//				System.out.printf("Chunk %d is the LAST one!\n", sequenceNumber);
			
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
	
	private class ACKListener extends Thread {
		
		public void run() {
//			System.out.println("Started ACKListener");
			
			while (isListening && maxACKreceived <= totalPackets) {
				// Set up buffer
				byte[] buffer = new byte[ACK_SIZE];
				DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
				
				long timeoutTimer = System.currentTimeMillis() + socketTimeout;
				
				// Receive the ACK
				try {
//					System.out.println(">>> Waiting for an ACK...");
					
					// Set socket timeout
					socket.setSoTimeout(socketTimeout);
					socket.receive(packet);
					
//					System.out.println("Packet received. Processing...");
					
					int ackNumber = Tools.byteArrayToInt(buffer);
					
					// verify we got the next packet needed
					if (ackNumber > maxACKreceived) {
						
						
						// Remove all entries upt to maxACK + 1
						for (Integer key: packetBuffer.keySet()) {
							if (key <= ackNumber) {
								packetBuffer.remove(key);
							}
						}
//						System.out.println("Removed from buffer. New size is " + packetBuffer.size());
						// Update maximum ACK received
						maxACKreceived = ackNumber;
					} 
//					else {meoutTimer - Sys
//						if (timeoutTimer - System.currentTimeMillis() < 0) {
//							resendPackets();
//							timeoutTimer = System.currentTimeMillis() + socketTimeout;
//						}
//					}					
//					System.out.println("ACK Packet #" + ackNumber);
				} catch (SocketTimeoutException e) {
//					System.out.println("ACK Listener Socket timeout occured.");
//					System.out.println("Buffer size: " + packetBuffer.size());
					if (timeoutTimer - System.currentTimeMillis() < 0) {
						resendPackets();
						timeoutTimer = System.currentTimeMillis() + socketTimeout;
					}
				} catch (IOException e) {
					System.err.println("IO Exception on ACK ReceiverThread occured. Exiting.");
					System.exit(0);
				}
			}
		}
		
		public void resendPackets() {
			for (int i = maxACKreceived; i < packetBuffer.size() + maxACKreceived; i++) {
				try {
//					System.out.println("Socket is null: " + (socket == null));
					if (socket != null) {
						DatagramPacket packet = packetBuffer.get(i);
						if (packet != null)
							socket.send(packet);
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
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
