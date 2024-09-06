package com.example.homecctv;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.mizuvoip.jvoip.SIPNotification;
import com.mizuvoip.jvoip.SIPNotificationListener;
import com.mizuvoip.jvoip.SipStack;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.TimeZone;

public class VoiceTalkActivity extends AppCompatActivity
{
    public static String LOGTAG = "AJVoIP";
    TextView mParams = null;
    EditText mDestNumber = null;
    Button mBtnCall = null;
    Button mBtnHangup = null;
    Button mBtnTest = null;
    TextView mStatus = null;
    TextView mNotifications = null;
    SipStack mysipclient = null;
    Context ctx = null;

    public static VoiceTalkActivity instance = null;

    private StringBuilder parameters = null;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        //Message messageToMainThread = new Message();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.voicetalk);
        ctx = this;
        instance = this;

        mParams = (TextView) findViewById(R.id.parameters_view);
        mDestNumber = (EditText) findViewById(R.id.dest_number);
        mBtnCall = (Button) findViewById(R.id.btn_call);
        mBtnHangup = (Button) findViewById(R.id.btn_hangup);
        mBtnTest = (Button) findViewById(R.id.btn_test);
        mStatus = (TextView) findViewById(R.id.status);
        mNotifications = (TextView) findViewById(R.id.notifications);
        mNotifications.setMovementMethod(new ScrollingMovementMethod());

        DisplayLogs("oncreate");

        //SIP stack parameters separated by CRLF. Will be passed to AJVoIP with the SetParameters API call (you might  use the SetParameter API instead to pass the parameters separately)
        //Add other settings after your needs. See the documentation "Parameters" chapter for the full list of available settings.

        parameters = new StringBuilder();

        //parameters.append("loglevel=5\r\n"); //for development you should set the loglevel to 5.
        parameters.append("loglevel=1\r\n"); //for production you should set the loglevel to 1.
        parameters.append("serveraddress=192.168.0.100\r\n");
        parameters.append("username=103\r\n");
        parameters.append("password=103\r\n");

        // ArrayList 사용
        ArrayList<String> userinfo = new ArrayList<>();

        userinfo.add("서버주소=192.168.0.100");
        userinfo.add("나의번호=103");

    // 줄바꿈을 사용해 리스트의 요소를 하나의 문자열로 연결
        mParams.setText(String.join("\n", userinfo));


        DisplayStatus("Ready.");

        //SIP 스택을 시작함.
        try{
                    // start SipStack if it's not already running
                    if (mysipclient == null) //check if AJVoIP instance already exists
                    {
                        DisplayLogs("Starting SIPStack");

                        //initialize the SIP engine
                        mysipclient = new SipStack();
                        mysipclient.Init(ctx);

                        //subscribe to notification events
                        MyNotificationListener listener = new MyNotificationListener();
                        mysipclient.SetNotificationListener(listener);

                        SetParameters(); //pass the configuration (parameters can be changed also later at run-time)

                        DisplayLogs("SIPStack Start");

                        //start the SIP engine
                        mysipclient.Start();
                        //mysipclient.Register();
                        instance.CheckPermissions();

                        DisplayLogs("SIPStack Started");
                    }
                    else
                    {
                        DisplayLogs("SIPStack already started");
                    }
        }catch (Exception e) { DisplayLogs("ERROR, StartSIPStack"); }


        mBtnCall.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v) //Call button click
            {
                DisplayLogs("Call on click");

                String number = mDestNumber.getText().toString().trim();
                if (number == null || number.length() < 1)
                {
                    DisplayStatus("ERROR, Invalid destination number");
                    return;
                }
                if (mysipclient == null) {
                    DisplayStatus("ERROR, cannot initiate call because SipStack is not started");
                    return;
                }

                instance.CheckPermissions();

                if (mysipclient.Call(-1, number))
                {
                    /*
                        optinal flags (you might set these also for incoming calls):
                        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);
                        getWindow().addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED);
                        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
                        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
                        if(Build.VERSION.SDK_INT >= 24) getWindow().setSustainedPerformanceMode(true);
                        instance.setShowWhenLocked(true);
                        instance.setTurnScreenOn(true);
                    */
                }
            }
        });

        mBtnHangup.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v) //Hangup button click
            {
                DisplayLogs("Hangup on click");

                if (mysipclient == null)
                    DisplayStatus("ERROR, cannot hangup because SipStack is not started");
                else
                    mysipclient.Hangup();
            }
        });

        mBtnTest.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v) //Test button click
            {
                //any test code here
                DisplayLogs("Toogle loudspeaker");
                if (mysipclient == null)
                    DisplayStatus("ERROR, SipStack not started");
                else
                    //SetSpeakerMode는 SIP 관련 기능을 제공하는 라이브러리(이 경우 Mizu VoIP)에서 정의된 메서드로,
                    // 이 라이브러리를 사용하는 개발자는 이 메서드를 호출하여 스피커 모드를 제어할 수 있습니다.
                    mysipclient.SetSpeakerMode(!mysipclient.IsLoudspeaker());
            }
        });

    }

    /**
     * Pass the parameters to AJVoIP
     */
    public void SetParameters()
    {
        String params = parameters.toString();
        if (params == null || mysipclient == null) return;
        params = params.trim();

        DisplayLogs("SetParameters: " + params);

        mysipclient.SetParameters(params);

        //we could also set the parameters individually like:
        //mysipclient.SetParameter("loglevel",5);
    }


    /**
     * Handle audio record permissions
     */
    void CheckPermissions()
    {
        if (Build.VERSION.SDK_INT >= 23 && ctx.checkSelfPermission(android.Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED)
        {
            //we need RECORD_AUDIO permission before to make/receive any call
            DisplayStatus("Microphone permission required");
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECORD_AUDIO}, 555);

        }
    }

    /**
     * You will receive the notification events from JVoIP in this class by overriding the SIPNotificationListener base class member functions.
     * Don't block or wait in the overwritten functions for too long.
     * See the "Notifications" chapter in the documentation and/or the following javadoc for the details:
     * https://www.mizu-voip.com/Portals/0/Files/ajvoip_javadoc/index.html
     */
    class MyNotificationListener extends SIPNotificationListener
    {
        //here are some examples about how to handle the notifications:
        @Override
        public void onAll(SIPNotification e) {
            //we receive all notifications (also) here. we just print them from here
            DisplayLogs(e.getNotificationTypeText()+" notification received: " + e.toString());
        }

        //handle connection (REGISTER) state
        @Override
        public void onRegister(SIPNotification.Register e)
        {
            //check if/when we are registered to the SIP server
            if(!e.getIsMain()) return; //we ignore secondary accounts here

            switch(e.getStatus())
            {
                case SIPNotification.Register.STATUS_INPROGRESS: DisplayStatus("Registering..."); break;
                case SIPNotification.Register.STATUS_SUCCESS: DisplayStatus("Registered successfully."); break;
                case SIPNotification.Register.STATUS_FAILED: DisplayStatus("Register failed because "+e.getReason()); break;
                case SIPNotification.Register.STATUS_UNREGISTERED: DisplayStatus("Unregistered."); break;
            }
        }

        //onStatus 메서드는 SIP 스택에서 발생하는 상태 알림 이벤트에 의해 자동으로 호출됩니다.
        // 코드 내에서 직접적으로 보이지 않지만, SIP 통신 과정에서 발생하는 상태 변경 이벤트가 있을 때 해당 메서드가 자동으로 실행됩니다.
        @Override
        public void onStatus(SIPNotification.Status e)
        {
            if(e.getLine() == -1) return; //we are ignoring the global state here (but you might check only the global state instead or look for the particular lines separately if you must handle multiple simultaneous calls)

            //log call state
            if(e.getStatus() >= SIPNotification.Status.STATUS_CALL_SETUP && e.getStatus() <= SIPNotification.Status.STATUS_CALL_FINISHED)
            {
                DisplayStatus("Call state is: "+ e.getStatusText());
            }

            //catch outgoing call connect
            if(e.getStatus() == SIPNotification.Status.STATUS_CALL_CONNECT && e.getEndpointType() == SIPNotification.Status.DIRECTION_OUT)
            {
                DisplayStatus("Outgoing call connected to "+ e.getPeer());


                //there are many things we can do on call connect. for example:
                //mysipclient.Dtmf(e.getLine(),"1"); //send DTMF digit 1
                //mysipclient.PlaySound(e.getLine(), "mysound.wav", 0, false, true, true, -1,"",false); //stream an audio file

            }
            //catch incoming calls
            else if(e.getStatus() == SIPNotification.Status.STATUS_CALL_RINGING && e.getEndpointType() == SIPNotification.Status.DIRECTION_IN)
            {
                DisplayStatus("Incoming call from "+ e.getPeerDisplayname());

                //auto accepting the incoming call (instead of auto accept, you might present an Accept/Reject button for the user which will call Accept / Reject)
                mysipclient.Accept(e.getLine());
            }
            //catch incoming call connect
            else if(e.getStatus() == SIPNotification.Status.STATUS_CALL_CONNECT && e.getEndpointType() == SIPNotification.Status.DIRECTION_IN)
            {
                DisplayStatus("Incoming call connected");
            }

        }

        //print important events (EVENT)
        @Override
        public void onEvent( SIPNotification.Event e)
        {
            DisplayStatus("Important event: "+e.getText());
        }

        //IM handling
        @Override
        public void onChat( SIPNotification.Chat e)
        {
            DisplayStatus("Message from "+e.getPeer()+": "+e.getMsg());

            //auto answer
            mysipclient.SendChat(-1, e.getPeer(),"Received");

        }
    }

    public void DisplayStatus(String stat)
    {
        try{
            if (stat == null) return;
            if (mStatus != null) {
                if ( stat.length() > 70)
                    mStatus.setText(stat.substring(0,60)+"...");
                else
                    mStatus.setText(stat);
            }
            DisplayLogs(stat);
        }catch(Throwable e){  Log.e(LOGTAG, "ERROR, DisplayStatus", e); }
    }

    public void DisplayLogs(String logmsg)
    {
        try{
            if (logmsg == null || logmsg.length() < 1) return;

            if ( logmsg.length() > 2500) logmsg = logmsg.substring(0,300)+"...";
            logmsg = "["+ new java.text.SimpleDateFormat("HH:mm:ss:SSS").format(Calendar.getInstance(TimeZone.getTimeZone("GMT")).getTime()) +  "] " + logmsg + "\r\n";

            Log.v(LOGTAG, logmsg);
            if (mNotifications != null) mNotifications.append(logmsg);
        }catch(Throwable e){  Log.e(LOGTAG, "ERROR, DisplayLogs", e); }
    }

    @Override
    public void onResume()
    {
        super.onResume();
    }

    @Override
    protected void onDestroy()
    {
        try{
            super.onDestroy();
            DisplayLogs("ondestroy");
            if (mysipclient != null)
            {
                DisplayLogs("Stop SipStack");
                mysipclient.Stop(true);
            }

            mysipclient = null;
        }catch(Throwable e){  Log.e(LOGTAG, "ERROR, on destroy", e); }
    }
}

