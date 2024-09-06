import com.fazecast.jSerialComm.SerialPort;

import java.util.Scanner;

//직접입력, 터미널에서 입력한 명령어를 보냄(클라이언트(터미널) => 아두이노)

public class MySerialClient {
    public static void main(String[] args) {
        SerialPort[] ports=SerialPort.getCommPorts();
        SerialPort mySerialPort=ports[3];

        int baudRate=9600, dataBits=8, stopBits=SerialPort.ONE_STOP_BIT;
        int parity=SerialPort.NO_PARITY;
        mySerialPort.setComPortParameters(baudRate,dataBits,stopBits,parity);
        mySerialPort.setComPortTimeouts(
                SerialPort.TIMEOUT_READ_BLOCKING,
                1000,0);
        mySerialPort.openPort();
        if(mySerialPort.isOpen()) System.out.println("open");
        else {
            System.out.println("not open");
            return;
        }
        try {
            Scanner scanner = new Scanner(System.in);
            while (true) {
                System.out.print(">> ");
                String msg=scanner.nextLine();
                mySerialPort.writeBytes(msg.getBytes(),msg.getBytes().length);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        mySerialPort.closePort();

    }
}
