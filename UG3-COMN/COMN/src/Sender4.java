import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;


public class Sender4 {
	
	private int PAYLOAD_SIZE = 1024;
	private int PACKET_SIZE = 1027;
	private int HEADER_SIZE = 3;
	
	private int timeout, windowSize;
	
	private DatagramSocket socket;
	private InetSocketAddress address;
	
	private ConcurrentHashMap<Integer, DatagramPacket> packetBuffer;
	private ConcurrentHashMap<Integer, Long> timeouts;
	private boolean isListeningACKs = true;
	private int totalPackets;
	private Set<Integer> acksReceived;
	
	private int ACK_SIZE = 2;

	public Sender4(int port, File file, int timeout, int windowSize) {
		this.timeout = timeout;
		this.windowSize = windowSize;
		
		acksReceived = new HashSet<Integer>();
		
		packetBuffer = new ConcurrentHashMap<Integer, DatagramPacket>();
		timeouts = new ConcurrentHashMap<Integer, Long>();
		
		try {
			socket = new DatagramSocket();
			address = new InetSocketAddress("localhost", port);
			
			rdt_send(file);
		} catch (SocketException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	private void rdt_send(File file) throws FileNotFoundException {
		FileInputStream fstream = new FileInputStream(file);
		
		long fsize = file.length();
		int chunkCount = (int) Math.ceil(fsize / (double) PAYLOAD_SIZE);
		totalPackets = chunkCount;
		int i = 1;
		
		ACKListener ackListener = new ACKListener();
		ackListener.start();
		
		try {
			// Do the hard work.
			while (i <= chunkCount) {
				// Send a packet if we haven't reached the window
				if (packetBuffer.size() <= windowSize) {
					byte[] buffer = new byte[PACKET_SIZE];
					
					// Read from buffer
					int sizeRead = fstream.read(buffer, HEADER_SIZE, PAYLOAD_SIZE);
					
					if (sizeRead < PAYLOAD_SIZE) {
						buffer = Arrays.copyOfRange(buffer, 0, sizeRead+3);
					}
					
					System.out.println("Size read: " + sizeRead);
					
					// Build packet
					DatagramPacket packet = make_pkt(buffer, i, i == chunkCount);
					// Buffer the packet
					packetBuffer.put(i, packet);
					
					udt_send(packet, i);
					i += 1;
				}
			}
		} catch (SocketException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
//		isListeningACKs = false;
		try {
			fstream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
	}
	
	private DatagramPacket make_pkt(byte[] data, int seqenceNum, boolean isLastPacket) throws SocketException {
		byte[] byteSequence = Tools.intToByteArray((short) seqenceNum);
		for (int i = 0; i < byteSequence.length; i++)
			data[i] = byteSequence[i];
		
		data[2] = isLastPacket ? (byte) 1 : (byte) 0;
		return new DatagramPacket(data, data.length, address);
	}
	
	private void udt_send(DatagramPacket packet, int sequenceNum) throws IOException {
		socket.send(packet);
		
		System.out.println("Sending packet # " + sequenceNum);
		
		// Add to packet timeouts
		timeouts.put(sequenceNum, System.currentTimeMillis() + timeout);
	}
	
	private class ACKListener extends Thread {
		
		@Override
		public void run() {
			while (isListeningACKs) {
				
				if (timeouts.size() > 0) {
					byte[] buffer = new byte[ACK_SIZE];
					DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
					
					
					int earlyTimeoutKey = findSoonestToTimeOut();
					
					System.out.println("Timeout Key: " + earlyTimeoutKey);
					
					int earlyTimeout;
					if (timeouts.get(earlyTimeoutKey) != null)
						earlyTimeout = (int) timeouts.get(earlyTimeoutKey).longValue();
					else
						earlyTimeout = timeout;
					
					try {
						if (earlyTimeout > 0)
							socket.setSoTimeout(earlyTimeout);
						else
							socket.setSoTimeout(timeout);
						socket.receive(packet);
						
						int ackNumber = Tools.byteArrayToInt(buffer);
						
						System.out.println("Received ACK # " + ackNumber);
						
						acksReceived.add(ackNumber);
						
						if (acksReceived.size() == totalPackets)
							isListeningACKs = false;
						
						// Remove packet from buffers
						packetBuffer.remove(ackNumber);
						timeouts.remove(ackNumber);
						
					} catch (SocketException e) {
						e.printStackTrace();
					} catch (SocketTimeoutException e) {
						resendPacket(earlyTimeout);
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}
		
		private void resendPacket(int key) {
			DatagramPacket packet = packetBuffer.get(key);
			if (packet != null) {
				try {
					socket.send(packet);
					System.out.println("Resending packet # " + key);
					timeouts.put(key, System.currentTimeMillis() + timeout);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		
		private int findSoonestToTimeOut() {
			int soonest = -1;
			long currTime = System.currentTimeMillis();
			long minCurrTime = timeout;
			for (Integer key: timeouts.keySet()) {
				if (timeouts.get(key) - currTime < minCurrTime) {
					minCurrTime = timeouts.get(key) - currTime;
					soonest = key;
				}					
			}
			return soonest;
		}
		
	}
	
	public static void main(String[] args) {
		// java Sender4 localhost <port> <filename> <timeout> <windowSize>
		try {
			int port = Integer.parseInt(args[1]);
			String filename = args[2];
			int timeout = Integer.parseInt(args[3]);
			int windowSize = Integer.parseInt(args[4]);
			
			File file = new File(filename);
			new Sender4(port, file, timeout, windowSize);			
			
		} catch (ArrayIndexOutOfBoundsException e) {
			System.err.println("Incorrect syntax. Use java Sender4 localhost <port> <filename> <timeout> <windowSize>");
		}

	}

}
