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
import java.util.concurrent.ConcurrentHashMap;


public class Sender3 {
	
	private int PACKET_SIZE = 1027;
	private int HEADER_SIZE = 3;
	private int PAYLOAD_SIZE = 1024;
	private int ACK_SIZE = 2;
	
	private int timeout, windowSize, totalPackets;
	
	private int base = 0;
	
	private DatagramSocket socket;
	private ConcurrentHashMap<Integer, DatagramPacket> buffer = new ConcurrentHashMap<Integer, DatagramPacket>();
	private InetSocketAddress address;
	
	private long execStartTime;

	public Sender3(int port, File file, int timeout, int windowSize) {
		this.timeout = timeout;
		this.windowSize = windowSize;
		
		try {
			socket = new DatagramSocket();
			address = new InetSocketAddress("localhost", port);
			
			ACKListener ackListener = new ACKListener();
			ackListener.start();
			
			execStartTime = System.currentTimeMillis();
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
		int chunks = (int) Math.ceil(fsize / (double) PAYLOAD_SIZE);
		totalPackets = chunks;
		int i = 1;
		
		try {
			while(!hasFinished()) {
				
				if (base <= i && i < base + windowSize + 1) {
					
					byte[] buffer = new byte[PACKET_SIZE];
					int readSize = fstream.read(buffer, HEADER_SIZE, PAYLOAD_SIZE);
					
					// Adjust buffer if it's the last packet
					if (readSize < PACKET_SIZE)
						buffer = Arrays.copyOfRange(buffer, 0, readSize + 3);
					
					DatagramPacket packet = make_pkt(buffer, i, i == chunks);
					
					udt_send(packet);
					
//					System.out.println("Sent packet #"+i);
					this.buffer.put(i, packet);
					
					i += 1;
					
				}
				
			}
			
			System.out.println("Exiting.");
			
			long execEndTime = System.currentTimeMillis();
			double timeElapsed = (execEndTime - execStartTime) / 1000.0;
			double throughput = file.length() / timeElapsed;
			System.out.println("Throughput: " + throughput / 1024 + " KB/s");
			
			System.exit(0);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void udt_send(DatagramPacket packet) throws IOException {
		socket.send(packet);
	}
	
	public DatagramPacket make_pkt(byte[] chunk, int sequenceNumber, boolean isLast) {

		// Set sequence header
		byte[] byteSequence = Tools.intToByteArray((short) sequenceNumber);
		for (int i = 0; i < byteSequence.length; i++)
			chunk[i] = byteSequence[i];

		chunk[2] = isLast ? (byte) 1 : (byte) 0;

//		if (isLast)
//			System.out.printf("Chunk %d is the LAST one!\n", sequenceNumber);

		DatagramPacket packet;
		try {
			packet = new DatagramPacket(chunk, chunk.length, address);
			return packet;
		} catch (SocketException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	private boolean hasFinished() {
		return totalPackets == base;
	}
	
	private class ACKListener extends Thread {
		
		@Override
		public void run() {
			
			while (!hasFinished()) {
				byte[] buff = new byte[ACK_SIZE];
				DatagramPacket packet = new DatagramPacket(buff, buff.length);
				
				long futureTimeout = System.currentTimeMillis() + timeout;
				
				try {
					socket.setSoTimeout(timeout);
					socket.receive(packet);
					
					int ackNum = Tools.byteArrayToInt(packet.getData());
					
//					System.out.println("Received ACK #" + ackNum);
					
					if (ackNum > base) {
						
						for (Integer key: buffer.keySet()) {
							if (key <= ackNum)
								buffer.remove(key);
						}
						
						base = ackNum;
						
					}
					
					// Send all above the current ack
//					for (Integer key: buffer.keySet()) {
//						if (key > base) {
//							DatagramPacket pk = buffer.get(key);
//							socket.send(pk);
//							System.out.println("Re-resent packet " + key);
//						}
//					}
				} catch (SocketTimeoutException e) {
					
					if (futureTimeout - System.currentTimeMillis() < 0) {
						resendPackets();
						futureTimeout = System.currentTimeMillis() + timeout;
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			
		}
		
		public void resendPackets() {
			for (Integer key: buffer.keySet()) {
				DatagramPacket packet = buffer.get(key);
				if (packet != null)
					try {
						socket.send(packet);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				else {
					System.err.println("Packet " + key + " is null!!!");
				}
			}
//			System.out.println("Packets resent.");
		}
		
	}
	
	
	public static void main(String[] args) {
		
		int port = Integer.parseInt(args[1]);
		String filename = args[2];
		int timeout = Integer.parseInt(args[3]);
		int windowSize = Integer.parseInt(args[4]);
		File file = new File(filename);
		
		new Sender3(port, file, timeout, windowSize);

	}

}
