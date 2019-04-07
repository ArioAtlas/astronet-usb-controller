package com.arioatlas.astronet.usb;

import java.io.IOException;
import java.util.Scanner;

import jssc.*;

public class ManualOverRide {
	
	public static void main(String[] args) throws InterruptedException {
		String[] portNames = SerialPortList.getPortNames();
	    
		if (portNames.length == 0) {
		    System.out.println("There are no serial-ports :( You can use an emulator, such ad VSPE, to create a virtual serial port.");
		    System.out.println("Press Enter to exit...");
		    try {
		        System.in.read();
		    } catch (IOException e) {
		         // TODO Auto-generated catch block
		          e.printStackTrace();
		    }
		    return;
		}

		for (int i = 0; i < portNames.length; i++){
		    System.out.println(portNames[i]);
		}
		
		
		SerialPort serialPort = new SerialPort("COM4");
		try {
		    serialPort.openPort();

		    serialPort.setParams(SerialPort.BAUDRATE_9600,
		                         SerialPort.DATABITS_8,
		                         SerialPort.STOPBITS_1,
		                         SerialPort.PARITY_NONE);

		    serialPort.setFlowControlMode(SerialPort.FLOWCONTROL_RTSCTS_IN | 
		                                  SerialPort.FLOWCONTROL_RTSCTS_OUT);

		    serialPort.addEventListener(new PortReader(serialPort), SerialPort.MASK_RXCHAR);
		    

		    
		}
		catch (SerialPortException ex) {
		    System.out.println("There are an error on writing string to port т: " + ex);
		}
	}
	
	private static class PortReader implements SerialPortEventListener {
		public SerialPort serialPort;
		private Scanner keyboard = new Scanner(System.in);
		
		
	    public PortReader(SerialPort serialPort) {
			super();
			this.serialPort = serialPort;
		}


		@Override
	    public void serialEvent(SerialPortEvent event) {
	        if(event.isRXCHAR() && event.getEventValue() > 0) {
	            try {
	                String receivedData = serialPort.readString(event.getEventValue());
	                System.out.println("Received response: " + receivedData);
	                if(receivedData.contains("READY")) {
	                	writeStream();
	                }
	            }
	            catch (SerialPortException ex) {
	                System.out.println("Error in receiving string from COM-port: " + ex);
	            }
	        }
	    }
		
		public void writeStream() throws SerialPortException {
//			this.serialPort.writeString("SON 5 1\n");
			System.out.println("Pilot test started\n");
			String cmd="";
			String id="";
				try {
					
					this.serialPort.writeString("SON 5 1\n");
					System.out.println("SON 5 1\n");
					
					Thread.sleep(2000);
					
					this.serialPort.writeString("SON 5 0\n");
					System.out.println("SON 5 0\n");
					
					Thread.sleep(2000);
					
					this.serialPort.writeString("SON 5 1\n");
					System.out.println("SON 5 1\n");
					
					Thread.sleep(5000);
					
					this.serialPort.writeString("SON 5 0\n");
					System.out.println("SON 5 0\n");
					
					Thread.sleep(2000);
					
					for(int i=0 ; i<10; i++) {
						this.serialPort.writeString("SON 5 1\n");
						System.out.println("SON 5 1\n");
						
						Thread.sleep(800);
						
						this.serialPort.writeString("SON 5 0\n");
						System.out.println("SON 5 0\n");
						Thread.sleep(800);
					}
					
					System.out.println("Test is done successfully\n");
				} catch (SerialPortException | InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					closePort(serialPort);
					return;
				}
			}
		
		public void closePort(SerialPort port) {
			try {
				serialPort.closePort();
			} catch (SerialPortException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
			
			
		}
		
		

	}
	
