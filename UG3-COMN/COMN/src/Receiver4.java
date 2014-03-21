import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketException;
import java.util.Arrays;
import java.util.HashMap;


public class Receiver4 {
	
	private int PACKET_SIZE = 1027;
	private int SEQUENCE_HEADER = 2;
	
	private int ackBase = 1;
	private int lastWritten = 0;

	private int windowSize;
	private DatagramSocket socket;
	private ByteArrayOutputStream output;
	private File file;
	
	private HashMap<Integer, DatagramPacket> packetBuffer;
	private boolean isListening = false;
	
	public Receiver4(int port, File file, int windowSize) {
		this.windowSize = windowSize;
		this.file = file;
		
		// Set up sockets and address
		try {
			socket = new DatagramSocket(port);
	
			output = new ByteArrayOutputStream();
			
			rdt_rcv();
			
		} catch (SocketException e) {
			e.printStackTrace();
		}
	}
	
	private void rdt_rcv() {
		packetBuffer = new HashMap<Integer, DatagramPacket>();
		isListening = true;
		
		// Set up buffer
		byte[] buffer = new byte[PACKET_SIZE];
		DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
		
		while (isListening) {
			try {
				socket.receive(packet);
				
				int packetNumber = getSequenceNumber(packet.getData(), packet.getLength());
				
				System.out.println("Received packet # " + packetNumber);
				
				boolean isLast = isLastPacket(packet.getData());
				
				System.out.println("Is last packet: " + isLast);
				// Check packet falls within the window
				if (packetNumber >= ackBase && packetNumber < ackBase + windowSize) {
					bufferPacket(packetNumber, packet);
					acknowledgePacket(packetNumber, packet);
					
					// Increment base ACK
					for (int i = ackBase; i < ackBase + windowSize; i++) {
						if (packetBuffer.containsKey(i))
							ackBase += 1;
						else
							break;
					}
				}
				
				writeToBufferedOutput();
				isListening = !isLast;
				
				System.out.println("ACK Base: " + ackBase);
				System.out.println("Packet Buffer Size: " + packetBuffer.size());
				
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		deliver_data();
	}
	
	private void deliver_data() {
		try {
			FileOutputStream fileStream = new FileOutputStream(file);
			BufferedOutputStream bufferedOutput = new BufferedOutputStream(fileStream);
			
			bufferedOutput.write(output.toByteArray());
			
			bufferedOutput.flush();
			bufferedOutput.close();
			output.close();
		} catch (FileNotFoundException e) {
			System.err.println("Could not find file to write to.");
		} catch (IOException e) {
			e.printStackTrace();
			System.err.println("Writing to file failed.");
		}
	}
	
	private boolean isLastPacket(byte[] data) {
		return data[2] == (byte) 1;
	}
	
	private int getSequenceNumber(byte[] data, int length) {
		byte[] sequenceHeader = Arrays.copyOfRange(data, 0, SEQUENCE_HEADER);
		return Tools.byteArrayToInt(sequenceHeader);
	}
	
	private void bufferPacket(int sequenceNumber, DatagramPacket data) {
		packetBuffer.put(sequenceNumber, data);
	}
	
	private void acknowledgePacket(int packetNumber, DatagramPacket packet) {
		try {
			byte[] ack = Tools.intToByteArray((short)packetNumber);
			
			// Build address
			InetAddress iAddress = packet.getAddress();
			int port = packet.getPort();
			InetSocketAddress address = new InetSocketAddress(iAddress, port);
			
			DatagramPacket ackPacket = new DatagramPacket(ack, ack.length, address);
			socket.send(ackPacket);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void writeToBufferedOutput() {
		for (int i = lastWritten + 1; i <= ackBase; i++) {
			if (packetBuffer.containsKey(i)) {
				DatagramPacket packet = packetBuffer.get(i);
				// Write the packet
				byte[] payload = Arrays.copyOfRange(packet.getData(), 3, packet.getLength());
				try {
					output.write(payload);
					lastWritten += 1;
				} catch (IOException e) {
					e.printStackTrace();
				}
				// Clean up the buffer.
				packetBuffer.remove(i);
			} else {
				// Stop checking as we're missing the next packet to write.
				break;
			}
		}
	}

	public static void main(String[] args) {
		// java Receiver4 <port> <filename> <window_size>
		try {
			int port = Integer.parseInt(args[0]);
			String filename = args[1];
			int windowSize = Integer.parseInt(args[2]);
			
			File file = new File(filename);
			
			new Receiver4(port, file, windowSize);
		} catch (ArrayIndexOutOfBoundsException e) {
			System.err.println("Incorrect syntax. Use java Receiver4 <port> <filename> <window_size>");
		}
	}

}
