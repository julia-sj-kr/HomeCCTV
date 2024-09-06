import com.fazecast.jSerialComm.SerialPort;

import javax.xml.crypto.Data;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.Scanner;

//서버구축, (클라이언트(안드로이드) => 서버 => 아두이노)

public class MySerialClientUDPServer {
    public static void main(String[] args) {
        SerialPort[] ports=SerialPort.getCommPorts();
        SerialPort mySerialPort=ports[2];

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
//            Scanner scanner = new Scanner(System.in);
            DatagramSocket ds=new DatagramSocket(7777);
            byte[] data=new byte[1024];
            DatagramPacket dp=new DatagramPacket(data,data.length);
            System.out.println("데이터 수신 준비 완료...");
            while (true) {
//                System.out.print(">> ");
//                String msg=scanner.nextLine();
                ds.receive(dp);
                System.out.println("송신 IP : "+dp.getAddress());
                InputStream is=new ByteArrayInputStream(
                        dp.getData(),dp.getOffset(),dp.getLength());
                InputStreamReader isr=new InputStreamReader(is);
                BufferedReader br=new BufferedReader(isr);
                String msg=br.readLine();
                //System.out.println("수신 메시지 크기 : " + msg.length());
                System.out.println("보내 온 내용 : "+msg);
                mySerialPort.writeBytes(msg.getBytes(),msg.getBytes().length);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        mySerialPort.closePort();

    }

}
