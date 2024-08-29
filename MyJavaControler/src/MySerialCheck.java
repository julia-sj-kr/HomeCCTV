import com.fazecast.jSerialComm.SerialPort;

import java.io.Serial;

public class MySerialCheck {
    public static void main(String[] args) {
        //자바 jSerialComm 라이브러리 가져오기
        SerialPort[] ports=SerialPort.getCommPorts();
        for (int i=0;i<ports.length;i++){
            System.out.println("ports["+i+"]:"+ports[i].getSystemPortName());
        }
    }
}
