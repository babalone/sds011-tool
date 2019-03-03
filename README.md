# sds011-tool
A tool to talk with the SDS011 air quality sensor

The SDS011 is a relatively cheap sensor for particulate matter PM 10 and PM 2.5 that is also popular for hobby-projects.
[Here](https://aqicn.org/sensor/sds011) is a website explaining the sensor and [here](https://luftdaten.info/messgenauigkeit/) is a project that does citizen science with it.

## Technology
The tool is based on Spring boot and opens a shell that allows interaction with the sensor.
Communication with the sensor is done via Serial Port.

![screenshot of the shell](https://github.com/babalone/sds011-tool/blob/master/documentation/shell-example.png)
