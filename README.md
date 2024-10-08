<div align="center">
   
![header](https://capsule-render.vercel.app/api?type=Rounded&color=0:56CCF2,100:2F80ED&height=170&section=header&text=인공지능%20홈CCTV%20모바일%20제어%20시스템&fontSize=50&fontColor=FFFFFF)
</div>

----

💡**1인 프로젝트**    
🗓️ **개발기간** `24.08~24.09(1개월)`   
🛠️ **플랫폼** `Android`  
📚**기술 스택** `Java (Android 앱 개발 및 서버)`, `Python (서버 및 OpenAI 연동)`, `Arduino (IoT 제어)`  
🔧**개발 도구** `Android Studio`, `IntelliJ IDEA`, `Thonny`, `Arduino IDE`  
<sub> ⚠️ 이 프로그램은 동일한 로컬 네트워크 내에서만 작동하며, 모든 디바이스가 같은 네트워크에 연결되어 있어야 합니다. 원격 접속이 필요할 경우, 포트 포워딩 또는 VPN 설정을 통해 외부 네트워크에서도 접근할 수 있습니다.  

<br>
인공지능 홈 CCTV 모바일 제어 시스템 프로젝트는 클라이언트-서버-디바이스 아키텍처로 구성하여 단순한 앱이 아닌 하드웨어와 네트워크를 연동한 복합 IoT 시스템입니다.<br>
아래 다이어그램은 CCTV 액티비티에서 음성 명령이 처리되고 해당 명령이 기기까지 전달되는 시스템 흐름을 도식화한 것입니다.<br>
또한, CCTV 영상 수신, 화면 제어, 전등 제어, 음성 통신 등 다양한 기능을 포함하며 클라이언트와 서버, 디바이스 간의 긴밀한 상호작용을 기반으로 합니다.<br>

<p align="center">
    <img src="https://github.com/user-attachments/assets/4389dc7a-65ec-4882-bde9-3a99407e0cfd" alt="image" width="700">
</p>
<p align="center">
    <strong><음성 명령 처리 및 제어 아키텍처></strong>
</p>

----

### 목차
1. [로그인 기능 (Login Activity)](#task-01-사용자가-입력한-id와-비밀번호를-확인하고-올바르다면-메인-페이지로-이동하는-로직을-구현하기)
2. [메인화면 (Main Activity)](#task-02-메인화면-구성하기)
3. CCTV제어(CCTV Activity)<br>
   3.1 [CCTV 영상 수신](#task-03-cctv-영상-수신)<br>
   3.2 [CCTV 화면 방향 움직임 기능 구현하기 using button](#task-04-cctv-화면-움직임-기능-구현하기-using-button)<br>
   3.3 [CCTV 화면 방향 움직임 기능 구현하기 using 음성인식](#task-05-cctv-화면-움직임-기능-구현하기-using-음성인식)<br>
   3.4 [CCTV 화면 움직임 기능 구현하기 using 음성인식+ ChatGPT API 활용](#task-06-cctv-화면-움직임-기능-구현하기-using-음성인식-chatgpt-api-활용)<br>
5. [전등 제어 (Light Activity)](#task-07-전등-제어)
6. [음성 통신(Call Activity)](#task-08-음성-통신)


---------------------------------------------------------------------------------------------------------------------
### Task 01 사용자가 입력한 ID와 비밀번호를 확인하고, 올바르다면 메인 페이지로 이동하는 로직을 구현하기

<p align="center">
    <img src="https://github.com/user-attachments/assets/4e59ef15-dcbd-4e9c-a322-094f80edd8f5" alt="image" width="200">
    &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<!-- 여기에 간격을 조정할 수 있습니다 -->
    <img src="https://github.com/user-attachments/assets/f96a91ad-58a8-4060-a538-99b8b3a1adae" alt="image" width="200"/>
</p>

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

> :memo: SQLite는 가벼운 내장형 관계형 데이터베이스 관리 시스템(RDBMS)입니다.  
> 서버가 필요 없고, 모든 데이터베이스 구조와 데이터가 하나의 파일 안에 저장됩니다.   
> 주로 소규모 애플리케이션이나 모바일 기기, 임베디드 시스템에서 많이 사용됩니다.

---------------------------------------------------------------------------------------------------------------------
### Task 02 메인화면 구성하기
<p align="center">
    <img src="https://github.com/user-attachments/assets/ceaa1bdf-fcbd-4ec1-8e03-9756a9076d81" alt="image" width="200">
</p>

버튼을 누르면 Intent를 사용하여 각각의 액티비티로 이동
- CCTV: 총 4개의 화면 수신, 화면을 클릭하면 해당 화면이 확대되고 버튼 또는 음성으로 화면 각도 제어
- 전등 ON/OFF: 포트로 연결된 아두이노 LED 등 제어
- 보이스톡: SIP(Session Initiation Protocol)을 사용하여 음성 통화
- 전자제품 on/off: 향후 기능 추가(마이크로컨트롤러를 사용하여 전자기기와 연결하여 원격으로 제어)

:x: __Challenges__<br>
CCTV 액티비티로 이동 후 back 했을때 메인화면에 ANR(Application Not Responding) 오류가 발생.    

:white_check_mark: __Solutions__  
- 영상 수신 파트에서 여러가지 가정을 두고 관련 코드 사이에 로그메시지를 체크하면서 원인을 파악.
  - 가정1: 4개의 화면 중 하나 이상의 영상이 수신되지 않을때 문제 발생 => 2초 시간 내에 연결이 이루어지지 않거나 데이터가 읽히지 않으면 예외가 발생하도록 코드 보완하였으나 문제 해결되지 않음.
  - 가정2: 스레드 영상을 Join하는 것이 시간이 오래 걸려 그 시간동안 UI 스레드가 블로킹되어 문제 발생 => Join 코드를 주석처리하고 재실행 하였으나 문제 해결되지 않음.  
  - __가정3: 영상화면을 destroy한 후 비트맵에 이미지를 그리는 과정이 오랜 시간 계속 되어 UI 스레드가 블로킹되어 문제 발생 => 원인 파악완료__
      ```java
      // 이미지 수신을 받는 과정 사이에 주기적으로 스레드 상태를 체크하여 스레드가 중지된 경우에는 읽기를 중단하고 리소스를 적절히 해제하도록 코드 보완.
      // 매 100번마다 현재 스레드가 실행 중인지 확인하고 아니라면 스트림을 닫아 더 이상 데이터 수신을 하지 않도록 함.
      if (i%100==0&&threadRunning==false){
            in.close();
            return;}
      ```
    

-------------------------------------------------------------------------------------------------------------------------
### [Task 03 CCTV 영상 수신](https://github.com/julia-sj-kr/Image-Processing)

<p align="center">
    <img src="https://github.com/user-attachments/assets/cc4631d4-4e33-4b04-9ecc-04749548c36d" alt="image" width="200">
    &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<!-- 여기에 간격을 조정할 수 있습니다 -->
    <img src="https://github.com/user-attachments/assets/6e078b57-c61a-4821-9f3f-1d772cd8bb77" alt="image" width="200"/>
</p>


CCTV 액티비티 화면 UI 구현:

총 4개의 영상을 2*2 배치, 영상 클릭하면 해당 화면이 확대되어 보여지게 레이아웃 편집하기

원격 영상 수신하는 코드 구현:

여러 비디오를 동시에 재생해야 할 경우, 비동기적으로 로드하는 방식을 권장(스레드 사용)
각각 다른 동영상 종류(a,b,c)를 수신하는 방법은 아래와 같다.

a. Public ip camera url에서 영상 수신하기  
링크: 원격 카메라 링크 모음
https://github.com/fury999io/public-ip-cams?tab=readme-ov-file

:x: __Challenges__  
a-1. VideoView 클래스 => 실패

````````````````````````````````
videoView1 = findViewById(R.id.videoView1);

        // 원격 비디오 URI 설정
        Uri videoUri = Uri.parse("http://220.233.144.165:8888/mjpg/video.mjpg");

        // 비디오 설정 및 재생
        videoView1.setVideoURI(videoUri);
        videoView1.start();
````````````````````````````````

:white_check_mark: __Solutions__

a-2. SurfaceView 상속받아 커스텀 뷰 생성하여 동영상 이미지 그려주기


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

---------------------------------------------------------------------------------------------------------------------

### Task 04 CCTV 화면 움직임 기능 구현하기 using button

각 PC 간의 네트워크 통신을 통해 실시간으로 영상 스트리밍과 카메라 제어가 가능하도록 설계되었습니다.  
초기 설계에서는 URL만 매개변수로 받아 4개의 영상 소스를 수신하도록 구성되었습니다.  
상용 환경을 고려했을 때, 영상 송출과 동시에 개별 카메라를 원격으로 제어할 필요가 있다고 판단하여, 추가적으로 IP 주소를 매개변수로 포함하여 각 카메라를 독립적으로 관리할 수 있도록 설계를 확장하였습니다.  

https://github.com/user-attachments/assets/e667d3b1-ead7-4d72-a050-639cb365f689
<p align="center">
       <img src="https://github.com/user-attachments/assets/1161979b-89d2-428a-be46-2411a6954ad5">
</p>

가상 장치(AVD) 또는 안드로이드 앱에서 화면 방향 조정 버튼을 클릭하면 해당 방향으로 서보 모터 동작하여 화면 전환이 되도록 한다.

a. 확대 영상 하단에 상하좌우 리모트 버튼(화면 방향 조정 버튼) 배치을 배치하고 각 버튼에 대한 이벤트 리스너를 구현합니다.

각 버튼 클릭 시, 해당 방향에 맞는 명령어를 송신하도록 합니다.

b. 명령어 설정: 아두이노 프로그램으로 서보 모터가 연결된 아두이노 하드웨어 보드에 상하좌우 동작 명령어를 설정한다.(USB 포트 사용)

c. 명령어 송신, 동작 수행: 앱에서 블루투스 통신을 통해 아두이노로 상하좌우 제어 명령어를 전송합니다.

d. 명령어 수신: 서보 모터의 동작이 완료된 후 아두이노에서 블루투스 통신을 통해 결과를 앱으로 반환합니다.

:smile: What is jSerialComm? 자바에서 시리얼 통신을 하기위해 필요한 외부 라이브러리<br>
링크:  링크 내에 JAR file 다운로드<br>
https://fazecast.github.io/jSerialComm/?source=post_page-----c5d9741aa263--------------------------------<br>

인텔리제이에 외부라이브러리 적용해주는 경로<br>
[intellij]-[파일]-[프로젝트구조]-[모듈]-[종속요소]-[+]-[1.JARs or Directories]

---------------------------------------------------------------------------------------------------------------------
### [Task 05 CCTV 화면 움직임 기능 구현하기 using 음성인식](https://github.com/julia-sj-kr/VoiceRecognitionApp)

사용자가 음성을 입력하면 이를 텍스트로 변환하여 화면에 표시하고, 지정된 IP 주소로 전송합니다.  
음성 인식 애플리케이션 구현을 위해 `SpeechRecognizer 객체 사용`과 `음성 인식 인텐트 필터` 두 가지 방식을 검토하였습니다.  
그 중 음성 인식 인텐트 필터를 사용하는 방식이 인식 정확도가 높고 속도가 빨라서 홈CCTV 프로그램에는 해당 기술을 사용하였습니다.  

a. Android 플랫폼에서 **RecognizerIntent**를 사용하여 시스템 음성 인식 기능을 호출.   
b. UDP 프로토콜을 통해 음성으로 입력된 텍스트 데이터를 네트워크로 전송.

> :memo: *암시적 인텐트란,*
사용자가 지정한 액션을 수행할 수 있는 컴포넌트를 시스템이 자동으로 선택할 수 있도록 하여 유연한 앱 간 통신을 가능하게 합니다.
>- 작업 기반: 암시적 인텐트는 특정 작업을 수행하기 위한 의도를 나타내며, 해당 작업을 처리할 수 있는 여러 컴포넌트 중 하나를 선택합니다.
>- 유연성: 앱이 설치되어 있지 않거나 기본 음성 인식 앱이 다를 경우에도 동작할 수 있습니다.
>- 액션 및 데이터 필드: 인텐트를 정의할 때 액션, 카테고리, 데이터와 같은 필드를 사용하여 원하는 작업을 지정합니다.

---------------------------------------------------------------------------------------------------------------------
### [Task 06 CCTV 화면 움직임 기능 구현하기 using 음성인식+ ChatGPT API 활용](https://github.com/julia-sj-kr/OpenAI-API.git).

Task 05에서 음성으로 화면 움직임 제어 하는 기능은 **미리 정의된 명령어**가 입력되었을 때만 작동합니다.   
이를 확장하여 ChatGPT API를 통해 **사용자가 어떤 명령을 내리든** 그 의도를 파악하여 정해진 명령어로 변환해주는 기능을 추가하였습니다.  
현재 프로그램에서는 LLM API로 ChatGPT를 사용하였습니다. Gemini API를 사용하면 Java를 직접 지원하므로, 통신 과정이 한단계 줄어듭니다.

링크: https://github.com/openai/openai-quickstart-python<br>

a. ChatGPT API는 Java를 직접 지원하지 않기 때문에, 3단계 통신이 필요합니다.  
1. 안드로이드에서 보이스 데이터를 파이썬으로 보내줌(파이썬UDP서버)<br>
파이썬에서 UDP 뚫는 방법 링크: https://w-world.tistory.com/217<br>
2. 파이썬 서버에서 OpenAI 파인튜닝으로 보이스 데이터를 명령어로 변환해서 자바 서버로 전송(자바UDP서버)<br>
3. 자바 서버에서 명령어를 USB 포트를 통해 아두이노로 전달(시리얼 통신)

<p align="center">
    <img src="https://github.com/user-attachments/assets/5c7fa352-bdae-4098-acd5-51f16dc964c5" alt="image" width="200">
</p>
<p align="center">
    <img src="https://github.com/user-attachments/assets/2125cb79-f165-425f-bf16-28f50509485c" alt="image" width="380"/>
    &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<!-- 여기에 간격을 조정할 수 있습니다 -->
   <img src="https://github.com/user-attachments/assets/6c6ca3ef-5f4d-4c50-baf7-71c6c7fab78a" alt="image" width="280">
</p>
b. Gemini API는 Java를 직접 지원하므로, 통신 과정이 더 단순화됩니다.<br>
1. 안드로이드에서 Gemini API를 통해 보이스 데이터를 명령어로 변환해서 자바 서버로 전송(자바UDP서버)<br>
링크: https://ai.google.dev/api?hl=ko&lang=android#java<br>
2. 자바 서버에서 명령어를 USB 포트를 통해 아두이노로 전달(시리얼 통신)

-----------------
### Task 07 전등 제어

<p align="center">
    <img src="https://github.com/user-attachments/assets/0d1816d9-ec59-444b-802e-3a7d4ef8d8d8" alt="image" width="300">
    &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<!-- 여기에 간격을 조정할 수 있습니다 -->
    <img src="https://github.com/user-attachments/assets/3402e90a-70de-48a5-8d47-231275b60f87" alt="image" width="200"/>
</p>

안드로이드 앱에서 각 버튼을 클릭하면 UDP 패킷이 서버로 전송되고, 아두이노가 해당 패킷을 수신하여 전등을 제어합니다.<br>
필요한 경우 IP 주소와 포트 번호를 환경에 맞게 조정해야합니다.

----------------------------------------------------------------------------------------------------
### [Task 08 음성 통신](https://github.com/julia-sj-kr/Networking)

<p align="center">
    <img src="https://github.com/user-attachments/assets/ce5cbfe8-1c03-484d-88d6-0ce397bfd933" alt="image" width="500">
    &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<!-- 여기에 간격을 조정할 수 있습니다 -->
    <img src="https://github.com/user-attachments/assets/74678ad2-5cd2-422a-9c6c-494c4504e0b4" alt="image" width="200"/>
</p>

음성 통신 기능은 miniSIPServer와 같은 외부 프로그램(서버 소프트웨어)과 AJVoIP 라이브러리와 같은 외부 라이브러리를 사용하여 구현하였습니다.  
자세한 내용은 소제목을 눌러 해당 레포지토리로 이동하면 참고하실 수 있습니다.
