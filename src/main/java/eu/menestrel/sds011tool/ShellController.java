package eu.menestrel.sds011tool;

import com.fazecast.jSerialComm.SerialPort;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;

@Slf4j
@ShellComponent
@PropertySource("classpath:sds011.properties")
public class ShellController {

  @Value("${sds011.baudrate}")
  private int baudrate;

  @Value("${sds011.databits}")
  private int databits;

  @Value("${sds011.parity}")
  private int parity;

  @Value("${sdsd011.stopbits}")
  private int stopbits;

  @ShellMethod("List all COM ports")
  public List<String> list() {
    SerialPort[] ports = SerialPort.getCommPorts();
    String[] result = new String[ports.length];
    for (int i = 0; i < ports.length; i++) {
      result[i] = String.format("[%s] %s", i, ports[i].getPortDescription());
    }
    return Arrays.asList(result);
  }

  @ShellMethod("Print received data")
  public void open(int index) {
    getSerialPort(index).ifPresent(port -> {
      log.info("will connect to port {]", port.getDescriptivePortName());

      log.info("opening channel");
      boolean successfullyOpened = port.openPort();
      if (!successfullyOpened) {
        log.error("couldn't open the port");
        return;
      }

      port.addDataListener(new Sds011DataListener());
    });
  }

  @ShellMethod("Close the port and not receive data any more")
  public void close(int index) {
    getSerialPort(index).ifPresent(port -> {
      log.info("will close port: {}", port.getDescriptivePortName());
      port.removeDataListener();
      log.info("removed datalistener");
      boolean closed = port.closePort();
      log.info("port closed? {}", closed);
    });
  }

  /**
   * Get the port at the given index.
   *
   * @return Optional.of(port) if nothing went wrong, otherwise Optional.empty() if an error occurred.
   */
  private Optional<SerialPort> getSerialPort(int index) {
    SerialPort port;
    SerialPort[] commPorts = SerialPort.getCommPorts();
    if (index < 0) {
      log.error("index is below 0 but a positive number was expected");
      return Optional.empty();
    }
    if (index >= commPorts.length) {
      log.error("index is bigger than the possible maximum %s", commPorts.length);
      return Optional.empty();
    }
    port = configurePortFromConfiguration(commPorts[index]);
    return Optional.of(port);
  }

  @NonNull
  private SerialPort configurePortFromConfiguration(SerialPort port) {
    log.info("configuring port");

    log.info("baudrate: {}", baudrate);
    port.setBaudRate(baudrate);

    log.info("databits: {}", databits);
    port.setNumDataBits(databits);

    log.info("parity: {}", parity);
    port.setParity(parity);

    log.info("stopbits: {}", stopbits);
    port.setNumStopBits(stopbits);

    return port;
  }

}
