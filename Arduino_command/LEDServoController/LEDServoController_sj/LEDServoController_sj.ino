#include <Servo.h>

Servo servoX; // X축 서보 모터
Servo servoY; // Y축 서보 모터

int LEDpin_1 = 11, LEDpin_2 = 12, LEDpin_3 = 15, LEDpin_4 = 16;

void setup() {
  servoX.attach(9); //X축 서보 모터 핀 연결
  servoY.attach(10); //Y축 서보 모터 핀 연결

  servoX.write(90); // 서보 초기 위치 설정
  servoY.write(90);

  pinMode(LEDpin_1, OUTPUT);
  pinMode(LEDpin_2, OUTPUT);
  pinMode(LEDpin_3, OUTPUT);
  pinMode(LEDpin_4, OUTPUT);

  Serial.begin(9600); // 시리얼 통신 시작
}

void loop() {
  if (Serial.available() > 0) {
    String command = Serial.readStringUntil('\n'); // 문자열로 명령어 수신

    Serial.println(command);

    // 문자열에 따라 명령어 처리
    if (command == "Front") {
      servoX.write(90); // 서보 초기 위치 설정
      servoY.write(90);
    } else if (command == "Up") {
      servoY.write(max(30, servoY.read() - 20)); // 위로 이동
    } else if (command == "Down") {
      servoY.write(min(150, servoY.read() + 20)); // 아래로 이동
    } else if (command == "Left") {
      servoX.write(max(30, servoX.read() - 30)); // 왼쪽으로 이동
    } else if (command == "Right") {
      servoX.write(min(150, servoX.read() + 30)); // 오른쪽으로 이동
    } else if (command == "ONLED1") { // LED1을 킴
      digitalWrite(LEDpin_1, HIGH);
    } else if (command == "ONLED2") { // LED2을 킴
      digitalWrite(LEDpin_2, HIGH);
    } else if (command == "ONLED3") { // LED3을 킴
      digitalWrite(LEDpin_3, HIGH);
    } else if (command == "ONLED4") { // LED4를 킴
      digitalWrite(LEDpin_4, HIGH);
    } else if (command == "OFFLED1") { // LED1를 끔
      digitalWrite(LEDpin_1, LOW);
    } else if (command == "OFFLED2") { // LED2를 끔
      digitalWrite(LEDpin_2, LOW);
    } else if (command == "OFFLED3") { // LED3를 끔
      digitalWrite(LEDpin_3, LOW);
    } else if (command == "OFFLED4") { // LED4를 끔
      digitalWrite(LEDpin_4, LOW);
    }
  }
}
