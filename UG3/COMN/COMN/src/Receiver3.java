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
import java.security.acl.LastOwnerException;
import java.util.Arrays;


public class Receiver3 {

	private int PORT_OFFSET = 5;

	private static int PAYLOAD_SIZE = 1024;
	private static int PACKET_SIZE = 1027;
	private static int HEADER_SIZE = 3;	//  bytes
	private static int ACK_SIZE = 2;

	private DatagramSocket socket;
	FileOutputStream fileStream;

	private int highestPacketReceived = 0;

	public Receiver3(int port, File file) {

		try {
			fileStream = new FileOutputStream(file);
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		try {
			// Bind a socket to port
			socket = new DatagramSocket(port);

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

					byte[] byteHeader = Arrays.copyOfRange(packet.getData(), 0, 2);
					int packetSequenceNum = Tools.byteArrayToInt(byteHeader);

//					System.out.println("Received packet # " + packetSequenceNum);
//					System.out.println("Highest packet: " + (highestPacketReceived + 1));

					if (packetSequenceNum == highestPacketReceived + 1) {
						highestPacketReceived = packetSequenceNum;
						packetReceived = true;
						send_response(packet.getAddress(), packet.getPort(), byteHeader);
//						System.out.println("ACK for packet " + packetSequenceNum + " sent");
					} else {
						byte[] sequence = Tools.intToByteArray((short) highestPacketReceived);
						send_response(packet.getAddress(), packet.getPort(), sequence);
//						System.out.println("ACK for packet " + highestPacketReceived + " sent");
					}

				} catch (IOException e) {
					System.err.println("Receiving an ACK failed. Exiting.");
					System.exit(0);
				}
			} while (!packetReceived);

			// Extract data and determine if we need to receive more
			listening = extract(packet.getData(), packet.getLength());
//			System.out.println("Are we listening for more: " + listening);
		}

		// Deliver data to the top level application
		deliver_data();
	}

	private short getSequenceNumber(byte[] sequenceHeader) {
		short sequenceNum = new BigInteger(sequenceHeader).shortValue();
		return sequenceNum;
	}

	private byte[] shortToByteArray(short s) {
		ByteBuffer buffer = ByteBuffer.allocate(2);
		buffer.putShort(s);
		return buffer.array();
	}

	private void send_response(InetAddress address, int port, byte[] content) {
		if (port != -1) {
			InetSocketAddress socketAddr = new InetSocketAddress(address, port);
			DatagramPacket packet;
			try {
				packet = new DatagramPacket(content, content.length, socketAddr);
				socket.send(packet);
			} catch (SocketException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}



	public boolean extract(byte[] packet, int size) {
		// Slice packet into a header
		byte[] byteHeader = Arrays.copyOfRange(packet, 0, 2);
		// Get the sequence number as an integer
		short sequenceNum = new BigInteger(byteHeader).shortValue();

//		System.out.printf("Received packet %d\n", sequenceNum);
		byte[] byteEof = Arrays.copyOfRange(packet, 2, 3);
		int eof = new BigInteger(byteEof).intValue();

		byte[] payload = Arrays.copyOfRange(packet, 3, size);

//		System.out.printf("Packet %d has %d bytes.\n", sequenceNum, packet.length);

		// Write the payload, payload located at packet[3] - end of packet
		try {
			fileStream.write(payload);
		} catch (IOException e) {
			e.printStackTrace();
			System.err.println("Could not write bytes into the output buffer.");
		}

		return true;
	}



	public boolean deliver_data() {
		boolean success = false;
		try {
			fileStream.flush();
			fileStream.close();

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
			int port = Integer.parseInt(args[0]);
			String filename = args[1];

			// Initialize file
			File file = new File(filename);

			System.out.println("Starting Receiver3.");

			// Start Sender
			new Receiver3(port, file);


		} catch(IndexOutOfBoundsException e) {
			System.err.println("Incorect arguments. Valid arguments are <Port> <FileName> [RetryTimeout] [WindowSize]");
			return;
		}
	}

}
