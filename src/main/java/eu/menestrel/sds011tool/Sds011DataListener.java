package eu.menestrel.sds011tool;

import com.fazecast.jSerialComm.SerialPort;
import com.fazecast.jSerialComm.SerialPortDataListener;
import com.fazecast.jSerialComm.SerialPortEvent;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Hex;

/**
 * Implementation of a {@link SerialPortDataListener} that can interpret data that is received from the SDS011 over
 * serial port.
 */
@Slf4j
public class Sds011DataListener implements SerialPortDataListener {

  @Override
  public int getListeningEvents() {
    return SerialPort.LISTENING_EVENT_DATA_RECEIVED;
  }

  /**
   * Will receive events of the serial bus. Everything that is not 10 bytes will be ignored.
   */
  @Override
  public void serialEvent(SerialPortEvent event) {
    if (event.getEventType() == SerialPort.LISTENING_EVENT_DATA_RECEIVED && event.getReceivedData().length == 10) {
      Sds011Packet receivedPacket = new Sds011Packet(event.getReceivedData());
      log.info("received: {}", Hex.encodeHexString(event.getReceivedData()));
      log.info("received packet: {}", receivedPacket);
    }
  }
}
