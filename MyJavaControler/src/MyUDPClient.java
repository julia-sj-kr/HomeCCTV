import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Scanner;

public class MyUDPClient {
    public static void main(String[] args) {
        Scanner scanner=new Scanner(System.in);
        System.out.print(">> ");
        String msg=scanner.nextLine();
        System.out.println("송신 메시지 크기 : "+msg.getBytes().length);
        try{
            DatagramSocket ds=new DatagramSocket();
            InetAddress ia=InetAddress.getByName("127.0.0.1");
            DatagramPacket dp=new DatagramPacket(msg.getBytes(), msg.getBytes().length, ia,7777);
            ds.send(dp);
            ds.close();
        }catch (Exception e){

        }
    }
}
