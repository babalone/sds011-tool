package eu.menestrel.sds011tool;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import lombok.Data;
import org.apache.commons.codec.binary.Hex;

/**
 * Representation of a data packet of 10 bytes that was received from the SDS011
 */
@Data
public class Sds011Packet {

  private byte header;
  private byte commanderNo;
  private byte pm25lowByte;
  private byte pm25highByte;
  private byte pm10lowByte;
  private byte pm10highByte;
  private byte idByte1;
  private byte idByte2;
  private byte checksum;
  private byte messageTail;

  /**
   * Create a new instance by parsing the given bytes.
   */
  public Sds011Packet(byte[] bytes) {
    if (bytes.length != 10) {
      throw new IllegalArgumentException("was excpecting 10 bytes, but received " + bytes.length);
    }
    header = bytes[0];
    commanderNo = bytes[1];
    pm25lowByte = bytes[2];
    pm25highByte = bytes[3];
    pm10lowByte = bytes[4];
    pm10highByte = bytes[5];
    idByte1 = bytes[6];
    idByte2 = bytes[7];
    checksum = bytes[8];
    messageTail = bytes[9];
  }

  /**
   * Check if data is valid by checking header, commanderNo, messageTail and checksum.
   */
  public boolean isValidData() {
    return header == (byte) 0xAA
        && commanderNo == (byte) 0xC0
        && messageTail == (byte) 0xAB
        && checksum == calculateChecksum();

  }

  private byte calculateChecksum() {
    return (byte) (pm25lowByte + pm25highByte + pm10lowByte + pm10highByte + idByte1 + idByte2);
  }

  /**
   * @return The value of PM 2.5
   */
  public int getPm25() {
    return (pm25highByte << 8 & 0xFF) | pm25lowByte & 0xFF;
  }

  /**
   * @return The value of PM 10
   */
  public int getPm10() {
    return (pm10highByte << 8 & 0xFF) | pm10lowByte & 0xFF;
  }

  @Override
  public String toString() {
    return String
        .format("%s SDS011 Packet (pm 2.5 = %d; pm 10 = %d) from device %s", (isValidData() ? "valid" : "invalid"),
            getPm25(), getPm10(), Hex.encodeHexString(new byte[]{idByte1, idByte2}));
  }
}
