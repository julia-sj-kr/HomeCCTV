#include <Servo.h>

Servo servoX; // X축 서보 모터
Servo servoY; // Y축 서보 모터

int LEDpin_1 = 11, LEDpin_2 = 12, LEDpin_3 = 15, LEDpin_4 = 16;

void setup() {
  servoX.attach(9); //X축 서보 모터 핀 연결
  servoY.attach(10); //y축 서보 모터 핀 연결

  servoX.write(90);//서보 초기 위치 설정
  servoY.write(90);

  pinMode(LEDpin_1, OUTPUT);
  pinMode(LEDpin_2, OUTPUT);
  pinMode(LEDpin_3, OUTPUT);
  pinMode(LEDpin_4, OUTPUT);

  Serial.begin(9600);//시리얼 통신 시작

}

void loop() {
  if (Serial.available() > 0) {

    char command = Serial.read();
    
    Serial.println(command);
    
    switch(command) {

      case 'F':
        servoX.write(90);//서보 초기 위치 설정
        servoY.write(90);
        break;

      case 'U':
        servoY.write(max(30,servoY.read() - 20)); // 위로 이동
        break;
      case 'D':
        servoY.write(min(150,servoY.read() + 20)); // 아래로 이동
        break;
      case 'L':
        servoX.write(max(30,servoX.read() - 30)); // 왼쪽으로 이동
        break;
      case 'R':
        servoX.write(min(150,servoX.read() + 30)); // 오른쪽으로 이동
        break;

      case 'a': // LED1을 킴
        digitalWrite(LEDpin_1, HIGH);
        break;
      case 'c': // LED2을 킴
        digitalWrite(LEDpin_2, HIGH);
        break;
      case 'e': // LED3을 킴
        digitalWrite(LEDpin_3, HIGH);
        break;
      case 'g': // LED4를 킴
        digitalWrite(LEDpin_4, HIGH);
        break;
      case 'b': // LED1를 끔
        digitalWrite(LEDpin_1, LOW);
        break;
      case 'd': // LED2를 끔
        digitalWrite(LEDpin_2, LOW);
        break;
      case 'f': // LED3를 끔
        digitalWrite(LEDpin_3, LOW);
        break;
      case 'h': // LED4를 끔
        digitalWrite(LEDpin_4, LOW);
        break;

    }
  }
}

