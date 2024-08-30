# 인공지능 홈CCTV 모바일 제어 시스템

### 목차
1. [로그인 기능 (Login Activity)](#로그인-기능-login-activity---task-01)
2. [메인화면 (Main Activity)](#Task-02-메인화면-구성하기)
3. CCTV제어(CCTV Acticity)<br>
   3.1 [CCTV 영상 송출 기능 구현하기](#task-03-cctv-영상-송출-기능-구현하기)<br>
   3.2 [CCTV 화면 방향 조정 기능 구현하기 using button](#task-04-cctv-화면-움직임-기능-구현하기-using-button)<br>
   3.3 [CCTV 화면 방향 조정 기능 구현하기 using 음성인식](#task-05-cctv-화면-움직임-기능-구현하기-using-음성인식)<br>

-------------------------------------------------------------------------------------------------------------------------   
1. 로그인 기능(Login Acticity)
2. 메인화면(main Acticity)
CCTV 제어
전등제어
집안온도확인
3. CCTV제어(CCTV Acticity)
JPEG 파일 주고 받는 예제 활용
거실, 아기방, 펫방, 상하좌우 회전 기능, 침입 감지 기능(리눅스 서버에서 구현) 음성 통신 기능(선택)
4. 전등 제어(Light Acticity)
(거실, 안방, 작은방1, 작은방2, 주방)
5. 집안 온도 확인(Temp Acticity)
(거실, 안방, 작은방1, 작은방2, 주방)

-------------------------------------------------------------------------------------------------------------------------
### Task 01 사용자가 입력한 ID와 비밀번호를 확인하고, 올바르다면 메인 페이지로 이동하는 로직을 구현하기

a. 회원가입 대화상자에서 사용자가 입력한 ID와 비밀번호 저장하기

링크: SQLiteOpenHelper를 사용하여 데이터베이스를 관리하고, AlertDialog를 통해 사용자로부터 아이디와 비밀번호를 입력받습니다.
https://chatgpt.com/share/c0c6268a-bd61-40a4-bc29-b1fb33c28c41

- SQLite 데이터베이스를 관리하기 위해 SQLiteOpenHelper 클래스를 만들어야 합니다.(교재 477 페이지 예제 참고)
- AlertDialog를 사용하여 사용자로부터 아이디와 비밀번호를 입력받는 메서드를 구현합니다.
- 사용자가 입력한 정보를 SQLite 데이터베이스에 저장합니다.

  ※next step => 로컬에서 읽는거를 통신을 통해서 원격으로 읽는걸로 업데이트, 자바 SQL 서칭 필요(server 자바, client 안드로이드 앱)

링크: Android SQLite 데이터베이스 테이블 데이터 확인 방법 (DB Browser for SQLite)<br>
https://velog.io/@haehyunlee/Android-SQLite-%EB%8D%B0%EC%9D%B4%ED%84%B0%EB%B2%A0%EC%9D%B4%EC%8A%A4-%ED%85%8C%EC%9D%B4%EB%B8%94-%EB%8D%B0%EC%9D%B4%ED%84%B0-%ED%99%95%EC%9D%B8-%EB%B0%A9%EB%B2%95

b. 사용자 입력 값 확인
사용자가 입력한 ID와 비밀번호를 가져와서 지정된 값과 비교합니다.

c. 조건이 충족되면 메인 페이지로 이동
사용자가 올바른 ID와 비밀번호를 입력했을 때 메인 페이지로 이동하는 코드를 작성합니다.

d. Intent를 사용하여 다른 액티비티로 이동
Android에서 페이지 간 이동은 Intent를 사용하여 다른 액티비티로 전환합니다.

-------------------------------------------------------------------------------------------------------------------------
### Task 02 메인화면 구성하기

CCTV 확인, 전등 제어, 온도 확인 버튼 생성

버튼을 누르면 Intent를 사용하여 각각의 액티비티로 이동

:x: 문제사항1<br>
각각의 액티비티로 이동 후 back 했을때 버튼이 비활성화되어 다시 원하는 액티비티로 이동 불가

:white_check_mark:

-------------------------------------------------------------------------------------------------------------------------
### Task 03 CCTV 영상 송출 기능 구현하기

CCTV 액티비티 화면 UI 구현:

총 4개의 영상을 2*2 배치, 영상 클릭하면 해당 화면이 확대되어 보여지게 레이아웃 편집하기

원격 영상 수신하는 코드 구현:

여러 비디오를 동시에 재생해야 할 경우, 비동기적으로 로드하는 방식을 권장(스레드 사용)
각각 다른 동영상 종류(a,b,c)를 수신하는 방법은 아래와 같다.

a. Public ip camera url에서 영상 수신하기
링크: 원격 카메라 링크 모음
https://github.com/fury999io/public-ip-cams?tab=readme-ov-file

:x: 문제사항2<br>
a-1. VideoView 클래스 => 실패

````````````````````````````````
videoView1 = findViewById(R.id.videoView1);

        // 원격 비디오 URI 설정
        Uri videoUri = Uri.parse("http://220.233.144.165:8888/mjpg/video.mjpg");

        // 비디오 설정 및 재생
        videoView1.setVideoURI(videoUri);
        videoView1.start();
````````````````````````````````

:white_check_mark: a-2. SurfaceView 상속받아 커스텀 뷰 생성하여 동영상 이미지 그려주기

참고링크: 안드로이드로 원격 카메라 영상 가져오는 코드(2013년도 코드로 일부 수정필요)
https://answers.opencv.org/question/15812/ip-camera-frames-manipulation/

참고링크: JPEG 파일 속 들여다 보기
https://yottu.tistory.com/20

b. USB 카메라(웹캠) 영상 수신하기

동일한 네트워크 상에 있는 영상만 수신 가능하다. :heavy_plus_sign: 외부에서 내부 네트워크로의 접근을 허용하고 싶다면 포트포워딩이 필요하다.

- HTTP를 통해 접근할 수 있는 IP 카메라로 전환하기
  링크: cam2web 프로그램으로 IP 카메라로 전환
  https://github.com/cvsandbox/cam2web?tab=readme-ov-file
  
  cam2web 프로그램 실행<br>
  => 웹페이지 켜서 ip 주소 입력하여 웹캠 연결 확인<br>
  => 영상출력해주는 프로그램 코드에 url 주소 입력("http://192.168.0.100:8000/camera/mjpeg)<br>
  => [res]-[xml]-[파일생성(ex.security)]-특정 도메인 주소 입력<br>
  ###### 기본적으로 Android 9(Pie)부터는 모든 HTTP 요청이 차단됩니다. networkSecurityConfig 파일을 사용하면 특정 도메인에 대해 HTTP를 허용할 수 있습니다.

      <?xml version="1.0" encoding="utf-8"?>
      <network-security-config>
          <domain-config cleartextTrafficPermitted="true">
              <domain includeSubdomains="true">192.168.0.100</domain>
              <domain includeSubdomains="true">192.168.137.1</domain>
              <domain includeSubdomains="true">192.168.137.53</domain>
          </domain-config>
      </network-security-config>

    => 매니페스트에 인터넷 권한 추가
    
       <uses-permission android:name="android.permission.INTERNET" />
        <application
          android:usesCleartextTraffic="true"
          android:networkSecurityConfig="@xml/security">


- a-2와 동일한 코드로 동영상 그려주기

c. 핸드폰 영상 수신하기
카모어플=>cam2web 프로그램으로 IP 카메라로 전환

-------------------------------------------------------------------------------------------------------------------------
AI 스마트 CCTV 시스템 프로젝트(8월26일)
 1. 리모콘 기능(UDP 통신 : 서버(PC), 클라이언트(안드로이드)) : 1~2일
 2. 음성 인식 기능(SpeechToText : text -> 서버(PC, ChatGPT 파이썬 연동) : 2일
 3. 통화기능(SIP 프로토콜) : 2일
 4. webcam IP streaming (Java OpenCV 라이브러리 이용) : 1일

-------------------------------------------------------------------------------------------------------------------------

### Task 04 CCTV 화면 움직임 기능 구현하기 using button(8월 28일 코딩 완료)

가상 장치(AVD) 또는 안드로이드 앱에서 화면 방향 조정 버튼을 클릭하면 해당 방향으로 서브 모터 동작하여 화면 전환이 되도록 한다.

a. 확대 영상 하단에 상하좌우 리모트 버튼(화면 방향 조정 버튼) 배치을 배치하고 각 버튼에 대한 이벤트 리스너를 구현합니다.

각 버튼 클릭 시, 해당 방향에 맞는 명령어를 송신하도록 합니다.

b. 명령어 설정: 아두이노 프로그램으로 서브 모터가 연결된 아두이노 하드웨어 보드에 상하좌우 동작 명령어를 설정한다.(USB 포트 사용)

c. 명령어 송신, 동작 수행: 앱에서 블루투스 통신을 통해 아두이노로 상하좌우 제어 명령어를 전송합니다.

d. 명령어 수신: 서브 모터의 동작이 완료된 후 아두이노에서 블루투스 통신을 통해 결과를 앱으로 반환합니다.

:smile: What is jSerialComm? 자바에서 시리얼 통신을 하기위해 필요한 외부 라이브러리<br>
링크:  링크 내에 JAR file 다운로드<br>
https://fazecast.github.io/jSerialComm/?source=post_page-----c5d9741aa263--------------------------------<br>

인텔리제이에 외부라이브러리 적용해주는 경로<br>
[intellij]-[파일]-[프로젝트구조]-[모듈]-[종속요소]-[+]-[1.JARs or Directories]

-------------------------------------------------------------------------------------------------------------------------
### Task 05 CCTV 화면 움직임 기능 구현하기 using 음성인식

링크: android-speech-to-text

https://medium.com/voice-tech-podcast/android-speech-to-text-tutorial-8f6fa71606ac

음성인식하기

약속된 명령어를 음성으로 통신을 통해서 보내기

-------------------------------------------------------------------------------------------------------------------------
### Task 06 CCTV 화면 움직임 기능 구현하기 using 음성인식+ 챗 GPT 파인튜닝

링크: https://github.com/openai/openai-quickstart-python<br>
관련폴더: ChatGPT_Fine-Tuning

LLM을 이용하여 다양한 음성 명령을 통신을 통해서 보내기

a. 챗지피티 API는 자바 언어 지원 안함(3번의 통신이 필요)<br>
1. 안드로이드에서 보이스 데이터를 파이썬으로 보내줌(파이썬UDP서버)<br>
파이썬에서 UDP 뚫는 방법 링크: https://w-world.tistory.com/217<br>
2. 파이썬에서 OpenAI 파인튜닝으로 보이스 데이터를 명령어로 변환해서 자바로 보내줌(자바UDP서버)<br>
3. 자바 서버에서 USB 포트로 아두이노에 명령어 전송(시리얼 통신)

b. Gemini API는 자바 언어 지원(2번의 통신이 필요)<br>
1. 안드로이드 내에 OpenAI 파인튜닝으로 보이스 데이터를 명령어로 변환해서 자바로 보내줌(자바UDP서버)<br>
링크: https://ai.google.dev/api?hl=ko&lang=android#java
2. 자바 서버에서 USB 포트로 아두이노에 명령어 전송(시리얼 통신)

-------------------------------------------------------------------------------------------------------------------------
### Task 07 IP를 통한 음성 송수신

수업 내용 보충을 위한 챗지피티 질문 링크: https://chatgpt.com/c/0150a406-7a4a-4b2b-9fe5-77c76372b7d0

전화기 서버를 우리가 만들 수 있다.<br>
음성통신 서비스를 제공한다면 리눅스로 서버를 구축해 놓거나 아마존같은 곳에서 대여해서 구축할 수 있다.<br>
참고 사이트: https://blog.naver.com/romanst/220635666592<br>

FTP도 TCP방식 중 하나다.<br>
IP위에 두개의 TCP나 UDP가 있고 그 위에 쭈욱 사용자 정의가 있다?<br>

미니 SIP 서버 설치<br>
참고 사이트: https://www.myvoipapp.com/<br>

다른 전화기 설치(br>
참고 사이트: https://www.microsip.org/<br>

자바 전화기, 안드로이드 전화기, 아이폰 전화기 등 전화기 앱 다운로드 사이트<br>
안드로이드 SIP 라이브러리 다운로드, Sample project 다운로드<br>
참고 사이트: https://www.mizu-voip.com/Software/SIPSDK/AndroidSIPSDK.aspx



