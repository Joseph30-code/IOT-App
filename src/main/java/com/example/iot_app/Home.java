package com.example.iot_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Home extends AppCompatActivity {

    private TextView qrtxt, name, info, status, espTime, b1time, tTimer1, b2time, tTimer2, b3time, tTimer3, b4time, tTimer4;
    private Button set_board_time;
    private static final String SHARED_PREFS = "sharedPrefs";
    private static final String TEXT = "text";
    private String textView,personId;
    private ImageView logout, qr_reset, profile, device_state, get_esp32_time, Board1, Board2, Board3, Board4;
    private DatabaseReference reference,referencecheck,esp32getval;
    private long startTime1,startTime2,startTime3,startTime4;
    private String timeString1, dateString1,timeString2, dateString2,timeString3, dateString3,timeString4, dateString4,saveText1,saveText2,saveText3,saveText4;
    private int setTime1,setTime2,setTime3,setTime4,closeTime1,closeTime2,closeTime3,closeTime4,x_length,vx_personId,xer_total;
    private String endTime1,endTime2,endTime3,endTime4;
    int chk_data1,chk_data2,chk_data3,chk_data4;
    private Button btndismiss1, btndismiss2, btndismiss3, btndismiss4;

    public static final String SHARED_PREFS1 = "sharedPrefs1";
    public static final String TEXT1 = "text1";
    public static final String SHARED_PREFS2 = "sharedPrefs2";
    public static final String TEXT2 = "text2";
    public static final String SHARED_PREFS3 = "sharedPrefs3";
    public static final String TEXT3 = "text3";
    public static final String SHARED_PREFS4 = "sharedPrefs4";
    public static final String TEXT4 = "text4";


    private static final String TAG = "Home";

    @Override
    protected void onStart() {
        super.onStart();
        Checkmobilestate();
    }

    private void Checkmobilestate() {
        if (!isConnected(this)) {
            ShowCustomDialog();
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        //Always check the State of the timer
        if(closeTime1==0) {
            start_countdown_timer1();
        }
        if(closeTime2==0){
            start_countdown_timer2();
        }
        if(closeTime3==0){
            start_countdown_timer3();
        }
        if(closeTime4==0){
            start_countdown_timer4();
        }





        qrtxt = findViewById(R.id.qrtxt);
        logout = findViewById(R.id.logout);
        name = findViewById(R.id.name);
        qr_reset = findViewById(R.id.qrreset);
        profile = findViewById(R.id.profile);
        info = findViewById(R.id.info);
        device_state = findViewById(R.id.devicestate);
        status = findViewById(R.id.status);
        get_esp32_time = findViewById(R.id.get_esp32_time);
        espTime = findViewById(R.id.esp_time);
        set_board_time= findViewById(R.id.textAmount);
        //First Board
        Board1=findViewById(R.id.board1);
        b1time= findViewById(R.id.texttime1);
        tTimer1 = findViewById(R.id.textAmount1);
        btndismiss1 = findViewById(R.id.button1);
        //Second Board
        Board2=findViewById(R.id.board2);
        b2time= findViewById(R.id.texttime1);
        tTimer2 = findViewById(R.id.textAmount2);
        btndismiss2 = findViewById(R.id.button1);
        //Third Board
        Board3=findViewById(R.id.board3);
        b3time= findViewById(R.id.texttime3);
        tTimer3 = findViewById(R.id.textAmount3);
        btndismiss3 = findViewById(R.id.button1);
        //Fourth Board
        Board4=findViewById(R.id.board4);
        b4time= findViewById(R.id.texttime4);
        tTimer4 = findViewById(R.id.textAmount4);
        btndismiss4 = findViewById(R.id.button1);


        loadData();
        updateViews();
        

        /*Get Esp 32 time*/
        get_esp32_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GetEsp32Internet();
            }
        });

        /*Set board time*/
        set_board_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CheckEsp32Internet();
            }
        });

        /*Set first Board*/
        Board1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getmobiledatastatus1();
            }
        });
        /*Set second Board*/
        Board2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getmobiledatastatus2();
            }
        });
        /*Set third Board*/
        Board3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getmobiledatastatus3();
            }
        });
        /*Set fourth Board*/
        Board4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getmobiledatastatus4();
            }
        });
        /*Dismiss Board One*/
        btndismiss1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CheckmobileInt1();
            }
        });
        /*Dismiss Board Two*/
        btndismiss2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CheckmobileInt2();
            }
        });
        /*Dismiss Board Three*/
        btndismiss3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CheckmobileInt3();
            }
        });
        /*Dismiss Board Four*/
        btndismiss4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CheckmobileInt4();
            }
        });

        SharedPreferences onetime = getSharedPreferences("onetime", MODE_PRIVATE);
        boolean gonetime = onetime.getBoolean("gonetime", true);
        if (gonetime) {
            showstartpath();
        }

        device_state.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CheckInternetst();
            }
        });

        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userInfo();
            }
        });

        info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoUrl("https://jmtechbog.netlify.app/index.html");
            }
        });

        qr_reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(Home.this).setCancelable(false)
                        .setTitle("QR CODE")
                        .setIcon(R.drawable.codex)
                        .setMessage("You value:" + textView)
                        .setPositiveButton("OK",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                }).setNegativeButton("RESET", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Resetqr();
                    }
                }).create().show();
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(Home.this).setCancelable(false)
                        .setTitle("IOT APP")
                        .setIcon(R.drawable.logout)
                        .setMessage("You are about to LOG OUT")
                        .setPositiveButton("YES",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                        LogoutUser();
                                    }
                                }).setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).create().show();

            }
        });

        GoogleSignInAccount signInAccount = GoogleSignIn.getLastSignedInAccount(this);
        if (signInAccount != null) {
            name.setText(signInAccount.getGivenName());
        }
        personId = signInAccount.getId();
        xer_total = 0;
        vx_personId = personId.length();
        for( x_length = 0; x_length < vx_personId; x_length++){
            char chx = personId.charAt(x_length);
            int val1 = Integer.parseInt(String.valueOf(chx));
            xer_total += val1;
        }
        reference = FirebaseDatabase.getInstance().getReference().child("All User").child(String.valueOf(textView));
        reference.child("auth_Id").setValue(xer_total);

    }

    private void CheckmobileInt1() {
        if (!isConnected(this)) {
            ShowCustomDialog();
        }else{
            referencecheck = FirebaseDatabase.getInstance().getReference().child("All User").child(String.valueOf(textView)).child(String.valueOf(xer_total));
            referencecheck.child("Esp32_set_val").setValue(0);
            referencecheck.child("Esp32_get_val").setValue(0);
            Getauthdismissstatus1();
        }
    }

    private void Getauthdismissstatus1() {
        referencecheck = FirebaseDatabase.getInstance().getReference().child("All User").child(String.valueOf(textView)).child(String.valueOf(xer_total));

        referencecheck.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    int currentval= snapshot.child("Authentication_confirmed").getValue(int.class);
                    if(currentval==xer_total){
                        Gotodismissalarm1();
                    }else{
                        InvalidDialog();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void Gotodismissalarm1() {
        referencecheck = FirebaseDatabase.getInstance().getReference().child("All User").child(String.valueOf(textView)).child(String.valueOf(xer_total));

        referencecheck.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    int espval = snapshot.child("Esp32_Internet_State").getValue(int.class);
                    if(espval==23){
                        referencecheck = FirebaseDatabase.getInstance().getReference().child("All User").child(String.valueOf(textView)).child(String.valueOf(xer_total));
                        referencecheck.child("Esp32_Internet_State").setValue(0);
                        SureGotodismissalarm1();
                    }else{
                        Esp32hasNoInternet();
                    }
                }

            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void SureGotodismissalarm1() {
        new AlertDialog.Builder(this).setCancelable(false)
                .setTitle("NOTE").setMessage("Your Device is under process . Please wait for some time ").setIcon(R.drawable.cpu)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(Home.this, "Wait for 10 seconds", Toast.LENGTH_SHORT).show();
                        ProgressDialog dialog = ProgressDialog.show(
                                Home.this,
                                "ESP 32",
                                "Detecting....",
                                true);
                        dialog.setCancelable(false);
                        dialog.setIcon(R.drawable.calendar);
                        dialog.show();
                        final Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                dialog.dismiss();
                                referencecheck = FirebaseDatabase.getInstance().getReference().child("All User").child(String.valueOf(textView)).child(String.valueOf(xer_total));

                                referencecheck.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        if (snapshot.exists()) {
                                            int sureconfirm = snapshot.child("Esp32_Internet_State").getValue(int.class);
                                            if (sureconfirm == 23) {
                                                Toast.makeText(Home.this, "Process Sucessfull", Toast.LENGTH_SHORT).show();
                                                esp32getval = FirebaseDatabase.getInstance().getReference().child("All User").child(String.valueOf(textView)).child(String.valueOf(xer_total));
                                                esp32getval.child("Esp32_get_val").setValue(50);
                                                handleDismissButton1();
                                            } else {
                                                Esp32hasNoInternet();
                                            }

                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });
                            }
                        },10000);
                    }
                }).create().show();
    }

    private void handleDismissButton1() {
        ProgressDialog dialog = ProgressDialog.show(
                Home.this,
                "ESP 32",
                "Processing....",
                true);
        dialog.setCancelable(false);
        dialog.setIcon(R.drawable.calendar);
        dialog.show();
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                referencecheck = FirebaseDatabase.getInstance().getReference().child("All User").child(String.valueOf(textView)).child(String.valueOf(xer_total));

                referencecheck.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.exists()){
                            int getespval = snapshot.child("Esp32_set_val").getValue(int.class);
                            if(getespval==46){
                                referencecheck = FirebaseDatabase.getInstance().getReference().child("All User").child(String.valueOf(textView)).child(String.valueOf(xer_total));
                                referencecheck.child("Esp32_set_val").setValue(0);
                                referencecheck.child("Esp32_get_val").setValue(0);
                                Stop_alarm_time1();
                                dialog.dismiss();
                                Toast.makeText(Home.this, "Succesfully Completed", Toast.LENGTH_SHORT).show();
                            }else{
                                dialog.dismiss();
                                Somethingwentwrong();
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

            }
        },20000);
    }


    private void CheckmobileInt2() {
        if (!isConnected(this)) {
            ShowCustomDialog();
        }else{
            referencecheck = FirebaseDatabase.getInstance().getReference().child("All User").child(String.valueOf(textView)).child(String.valueOf(xer_total));
            referencecheck.child("Esp32_set_val").setValue(0);
            referencecheck.child("Esp32_get_val").setValue(0);
            Getauthdismissstatus2();
        }
    }

    private void Getauthdismissstatus2() {
        referencecheck = FirebaseDatabase.getInstance().getReference().child("All User").child(String.valueOf(textView)).child(String.valueOf(xer_total));

        referencecheck.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    int currentval= snapshot.child("Authentication_confirmed").getValue(int.class);
                    if(currentval==xer_total){
                        Gotodismissalarm2();
                    }else{
                        InvalidDialog();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void Gotodismissalarm2() {
        referencecheck = FirebaseDatabase.getInstance().getReference().child("All User").child(String.valueOf(textView)).child(String.valueOf(xer_total));

        referencecheck.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    int espval = snapshot.child("Esp32_Internet_State").getValue(int.class);
                    if(espval==23){
                        referencecheck = FirebaseDatabase.getInstance().getReference().child("All User").child(String.valueOf(textView)).child(String.valueOf(xer_total));
                        referencecheck.child("Esp32_Internet_State").setValue(0);
                        SureGotodismissalarm2();
                    }else{
                        Esp32hasNoInternet();
                    }
                }

            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void SureGotodismissalarm2() {
        new AlertDialog.Builder(this).setCancelable(false)
                .setTitle("NOTE").setMessage("Your Device is under process . Please wait for some time ").setIcon(R.drawable.cpu)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(Home.this, "Wait for 10 seconds", Toast.LENGTH_SHORT).show();
                        ProgressDialog dialog = ProgressDialog.show(
                                Home.this,
                                "ESP 32",
                                "Detecting....",
                                true);
                        dialog.setCancelable(false);
                        dialog.setIcon(R.drawable.calendar);
                        dialog.show();
                        final Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                dialog.dismiss();
                                referencecheck = FirebaseDatabase.getInstance().getReference().child("All User").child(String.valueOf(textView)).child(String.valueOf(xer_total));

                                referencecheck.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        if (snapshot.exists()) {
                                            int sureconfirm = snapshot.child("Esp32_Internet_State").getValue(int.class);
                                            if (sureconfirm == 23) {
                                                Toast.makeText(Home.this, "Process Sucessfull", Toast.LENGTH_SHORT).show();
                                                esp32getval = FirebaseDatabase.getInstance().getReference().child("All User").child(String.valueOf(textView)).child(String.valueOf(xer_total));
                                                esp32getval.child("Esp32_get_val").setValue(51);
                                                handleDismissButton2();
                                            } else {
                                                Esp32hasNoInternet();
                                            }

                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });
                            }
                        },10000);
                    }
                }).create().show();
    }

    private void handleDismissButton2() {
        ProgressDialog dialog = ProgressDialog.show(
                Home.this,
                "ESP 32",
                "Processing....",
                true);
        dialog.setCancelable(false);
        dialog.setIcon(R.drawable.calendar);
        dialog.show();
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                referencecheck = FirebaseDatabase.getInstance().getReference().child("All User").child(String.valueOf(textView)).child(String.valueOf(xer_total));

                referencecheck.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.exists()){
                            int getespval = snapshot.child("Esp32_set_val").getValue(int.class);
                            if(getespval==46){
                                referencecheck = FirebaseDatabase.getInstance().getReference().child("All User").child(String.valueOf(textView)).child(String.valueOf(xer_total));
                                referencecheck.child("Esp32_set_val").setValue(0);
                                referencecheck.child("Esp32_get_val").setValue(0);
                                Stop_alarm_time2();
                                dialog.dismiss();
                                Toast.makeText(Home.this, "Succesfully Completed", Toast.LENGTH_SHORT).show();
                            }else{
                                dialog.dismiss();
                                Somethingwentwrong();
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

            }
        },20000);
    }

    private void CheckmobileInt3() {
        if (!isConnected(this)) {
            ShowCustomDialog();
        }else{
            referencecheck = FirebaseDatabase.getInstance().getReference().child("All User").child(String.valueOf(textView)).child(String.valueOf(xer_total));
            referencecheck.child("Esp32_set_val").setValue(0);
            referencecheck.child("Esp32_get_val").setValue(0);
            Getauthdismissstatus3();
        }
    }

    private void Getauthdismissstatus3() {
        referencecheck = FirebaseDatabase.getInstance().getReference().child("All User").child(String.valueOf(textView)).child(String.valueOf(xer_total));

        referencecheck.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    int currentval= snapshot.child("Authentication_confirmed").getValue(int.class);
                    if(currentval==xer_total){
                        Gotodismissalarm3();
                    }else{
                        InvalidDialog();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void Gotodismissalarm3() {
        referencecheck = FirebaseDatabase.getInstance().getReference().child("All User").child(String.valueOf(textView)).child(String.valueOf(xer_total));

        referencecheck.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    int espval = snapshot.child("Esp32_Internet_State").getValue(int.class);
                    if(espval==23){
                        referencecheck = FirebaseDatabase.getInstance().getReference().child("All User").child(String.valueOf(textView)).child(String.valueOf(xer_total));
                        referencecheck.child("Esp32_Internet_State").setValue(0);
                        SureGotodismissalarm3();
                    }else{
                        Esp32hasNoInternet();
                    }
                }

            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void SureGotodismissalarm3() {
        new AlertDialog.Builder(this).setCancelable(false)
                .setTitle("NOTE").setMessage("Your Device is under process . Please wait for some time ").setIcon(R.drawable.cpu)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(Home.this, "Wait for 10 seconds", Toast.LENGTH_SHORT).show();
                        ProgressDialog dialog = ProgressDialog.show(
                                Home.this,
                                "ESP 32",
                                "Detecting....",
                                true);
                        dialog.setCancelable(false);
                        dialog.setIcon(R.drawable.calendar);
                        dialog.show();
                        final Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                dialog.dismiss();
                                referencecheck = FirebaseDatabase.getInstance().getReference().child("All User").child(String.valueOf(textView)).child(String.valueOf(xer_total));

                                referencecheck.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        if (snapshot.exists()) {
                                            int sureconfirm = snapshot.child("Esp32_Internet_State").getValue(int.class);
                                            if (sureconfirm == 23) {
                                                Toast.makeText(Home.this, "Process Sucessfull", Toast.LENGTH_SHORT).show();
                                                esp32getval = FirebaseDatabase.getInstance().getReference().child("All User").child(String.valueOf(textView)).child(String.valueOf(xer_total));
                                                esp32getval.child("Esp32_get_val").setValue(52);
                                                handleDismissButton3();
                                            } else {
                                                Esp32hasNoInternet();
                                            }

                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });
                            }
                        },10000);
                    }
                }).create().show();
    }

    private void handleDismissButton3() {
        ProgressDialog dialog = ProgressDialog.show(
                Home.this,
                "ESP 32",
                "Processing....",
                true);
        dialog.setCancelable(false);
        dialog.setIcon(R.drawable.calendar);
        dialog.show();
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                referencecheck = FirebaseDatabase.getInstance().getReference().child("All User").child(String.valueOf(textView)).child(String.valueOf(xer_total));

                referencecheck.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.exists()){
                            int getespval = snapshot.child("Esp32_set_val").getValue(int.class);
                            if(getespval==46){
                                referencecheck = FirebaseDatabase.getInstance().getReference().child("All User").child(String.valueOf(textView)).child(String.valueOf(xer_total));
                                referencecheck.child("Esp32_set_val").setValue(0);
                                referencecheck.child("Esp32_get_val").setValue(0);
                                Stop_alarm_time3();
                                dialog.dismiss();
                                Toast.makeText(Home.this, "Succesfully Completed", Toast.LENGTH_SHORT).show();
                            }else{
                                dialog.dismiss();
                                Somethingwentwrong();
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

            }
        },20000);
    }


    private void CheckmobileInt4() {
        if (!isConnected(this)) {
            ShowCustomDialog();
        }else{
            referencecheck = FirebaseDatabase.getInstance().getReference().child("All User").child(String.valueOf(textView)).child(String.valueOf(xer_total));
            referencecheck.child("Esp32_set_val").setValue(0);
            referencecheck.child("Esp32_get_val").setValue(0);
            Getauthdismissstatus4();
        }
    }

    private void Getauthdismissstatus4() {
        referencecheck = FirebaseDatabase.getInstance().getReference().child("All User").child(String.valueOf(textView)).child(String.valueOf(xer_total));

        referencecheck.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    int currentval= snapshot.child("Authentication_confirmed").getValue(int.class);
                    if(currentval==xer_total){
                        Gotodismissalarm4();
                    }else{
                        InvalidDialog();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void Gotodismissalarm4() {
        referencecheck = FirebaseDatabase.getInstance().getReference().child("All User").child(String.valueOf(textView)).child(String.valueOf(xer_total));

        referencecheck.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    int espval = snapshot.child("Esp32_Internet_State").getValue(int.class);
                    if(espval==23){
                        referencecheck = FirebaseDatabase.getInstance().getReference().child("All User").child(String.valueOf(textView)).child(String.valueOf(xer_total));
                        referencecheck.child("Esp32_Internet_State").setValue(0);
                        SureGotodismissalarm4();
                    }else{
                        Esp32hasNoInternet();
                    }
                }

            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void SureGotodismissalarm4() {
        new AlertDialog.Builder(this).setCancelable(false)
                .setTitle("NOTE").setMessage("Your Device is under process . Please wait for some time ").setIcon(R.drawable.cpu)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(Home.this, "Wait for 10 seconds", Toast.LENGTH_SHORT).show();
                        ProgressDialog dialog = ProgressDialog.show(
                                Home.this,
                                "ESP 32",
                                "Detecting....",
                                true);
                        dialog.setCancelable(false);
                        dialog.setIcon(R.drawable.calendar);
                        dialog.show();
                        final Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                dialog.dismiss();
                                referencecheck = FirebaseDatabase.getInstance().getReference().child("All User").child(String.valueOf(textView)).child(String.valueOf(xer_total));

                                referencecheck.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        if (snapshot.exists()) {
                                            int sureconfirm = snapshot.child("Esp32_Internet_State").getValue(int.class);
                                            if (sureconfirm == 23) {
                                                Toast.makeText(Home.this, "Process Sucessfull", Toast.LENGTH_SHORT).show();
                                                esp32getval = FirebaseDatabase.getInstance().getReference().child("All User").child(String.valueOf(textView)).child(String.valueOf(xer_total));
                                                esp32getval.child("Esp32_get_val").setValue(53);
                                                handleDismissButton4();
                                            } else {
                                                Esp32hasNoInternet();
                                            }

                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });
                            }
                        },10000);
                    }
                }).create().show();
    }

    private void handleDismissButton4() {
        ProgressDialog dialog = ProgressDialog.show(
                Home.this,
                "ESP 32",
                "Processing....",
                true);
        dialog.setCancelable(false);
        dialog.setIcon(R.drawable.calendar);
        dialog.show();
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                referencecheck = FirebaseDatabase.getInstance().getReference().child("All User").child(String.valueOf(textView)).child(String.valueOf(xer_total));

                referencecheck.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.exists()){
                            int getespval = snapshot.child("Esp32_set_val").getValue(int.class);
                            if(getespval==46){
                                referencecheck = FirebaseDatabase.getInstance().getReference().child("All User").child(String.valueOf(textView)).child(String.valueOf(xer_total));
                                referencecheck.child("Esp32_set_val").setValue(0);
                                referencecheck.child("Esp32_get_val").setValue(0);
                                Stop_alarm_time4();
                                dialog.dismiss();
                                Toast.makeText(Home.this, "Succesfully Completed", Toast.LENGTH_SHORT).show();
                            }else{
                                dialog.dismiss();
                                Somethingwentwrong();
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

            }
        },20000);
    }



    private void Stop_alarm_time1()
    {
        b1time.setText("Your value");
        saveData1();
        chk_data1=3;
        tTimer1.setText("D:00 H:00 M:00 S:00");
    }
    private void Stop_alarm_time2()
    {
        b2time.setText("Your value");
        saveData2();
        chk_data2=3;
        tTimer2.setText("D:00 H:00 M:00 S:00");
    }
    private void Stop_alarm_time3()
    {
        b3time.setText("Your value");
        saveData3();
        chk_data3=3;
        tTimer3.setText("D:00 H:00 M:00 S:00");
    }
    private void Stop_alarm_time4()
    {
        b4time.setText("Your value");
        saveData4();
        chk_data4=3;
        tTimer4.setText("D:00 H:00 M:00 S:00");
    }

    private void getmobiledatastatus1(){
        if (!isConnected(this)) {
            ShowCustomDialog();
        }else{
            referencecheck = FirebaseDatabase.getInstance().getReference().child("All User").child(String.valueOf(textView)).child(String.valueOf(xer_total));
            referencecheck.child("Esp32_set_val").setValue(0);
            referencecheck.child("Esp32_get_val").setValue(0);
            Getauthstatus1();
        }
    }

    private void Getauthstatus1() {
        referencecheck = FirebaseDatabase.getInstance().getReference().child("All User").child(String.valueOf(textView)).child(String.valueOf(xer_total));

        referencecheck.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    int currentval= snapshot.child("Authentication_confirmed").getValue(int.class);
                    if(currentval==xer_total){
                        Gotosetalarm1();
                    }else{
                        InvalidDialog();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void Gotosetalarm1() {
        referencecheck = FirebaseDatabase.getInstance().getReference().child("All User").child(String.valueOf(textView)).child(String.valueOf(xer_total));

        referencecheck.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    int espval = snapshot.child("Esp32_Internet_State").getValue(int.class);
                    if(espval==23){
                        referencecheck = FirebaseDatabase.getInstance().getReference().child("All User").child(String.valueOf(textView)).child(String.valueOf(xer_total));
                        referencecheck.child("Esp32_Internet_State").setValue(0);
                        SureGotoSetalarm1();
                    }else{
                        Esp32hasNoInternet();
                    }
                }

            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void SureGotoSetalarm1() {
        new AlertDialog.Builder(this).setCancelable(false)
                .setTitle("NOTE").setMessage("Your Device is under process . Please wait for some time ").setIcon(R.drawable.cpu)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(Home.this, "Wait for 10 seconds", Toast.LENGTH_SHORT).show();
                        ProgressDialog dialog = ProgressDialog.show(
                                Home.this,
                                "ESP 32",
                                "Detecting....",
                                true);
                        dialog.setCancelable(false);
                        dialog.setIcon(R.drawable.calendar);
                        dialog.show();
                        final Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                dialog.dismiss();
                                referencecheck = FirebaseDatabase.getInstance().getReference().child("All User").child(String.valueOf(textView)).child(String.valueOf(xer_total));

                                referencecheck.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        if (snapshot.exists()) {
                                            int sureconfirm = snapshot.child("Esp32_Internet_State").getValue(int.class);
                                            if (sureconfirm == 23) {
                                                Toast.makeText(Home.this, "Process Sucessfull", Toast.LENGTH_SHORT).show();
                                                esp32getval = FirebaseDatabase.getInstance().getReference().child("All User").child(String.valueOf(textView)).child(String.valueOf(xer_total));
                                                esp32getval.child("Esp32_get_val").setValue(46);
                                                handleDateButton1();
                                            } else {
                                                Esp32hasNoInternet();
                                            }

                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });
                            }
                        },10000);
                    }
                }).create().show();
    }

    private void getmobiledatastatus2(){
        if (!isConnected(this)) {
            Toast.makeText(this, "No Internet", Toast.LENGTH_SHORT).show();
            ShowCustomDialog();
        }else{
            referencecheck = FirebaseDatabase.getInstance().getReference().child("All User").child(String.valueOf(textView)).child(String.valueOf(xer_total));
            referencecheck.child("Esp32_set_val").setValue(0);
            referencecheck.child("Esp32_get_val").setValue(0);
            Getauthstatus2();
        }
    }

    private void Getauthstatus2() {
        referencecheck = FirebaseDatabase.getInstance().getReference().child("All User").child(String.valueOf(textView)).child(String.valueOf(xer_total));

        referencecheck.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    int currentval= snapshot.child("Authentication_confirmed").getValue(int.class);
                    if(currentval==xer_total){
                        Gotosetalarm2();
                    }else{
                        InvalidDialog();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void Gotosetalarm2() {
        referencecheck = FirebaseDatabase.getInstance().getReference().child("All User").child(String.valueOf(textView)).child(String.valueOf(xer_total));

        referencecheck.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    int espval = snapshot.child("Esp32_Internet_State").getValue(int.class);
                    if(espval==23){
                        referencecheck = FirebaseDatabase.getInstance().getReference().child("All User").child(String.valueOf(textView)).child(String.valueOf(xer_total));
                        referencecheck.child("Esp32_Internet_State").setValue(0);
                        SureGotoSetalarm2();
                    }else{
                        Esp32hasNoInternet();
                    }
                }

            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void SureGotoSetalarm2() {
        new AlertDialog.Builder(this).setCancelable(false)
                .setTitle("NOTE").setMessage("Your Device is under process . Please wait for some time ").setIcon(R.drawable.cpu)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(Home.this, "Wait for 10 seconds", Toast.LENGTH_SHORT).show();
                        ProgressDialog dialog = ProgressDialog.show(
                                Home.this,
                                "ESP 32",
                                "Detecting....",
                                true);
                        dialog.setCancelable(false);
                        dialog.setIcon(R.drawable.calendar);
                        dialog.show();
                        final Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                dialog.dismiss();
                                referencecheck = FirebaseDatabase.getInstance().getReference().child("All User").child(String.valueOf(textView)).child(String.valueOf(xer_total));

                                referencecheck.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        if (snapshot.exists()) {
                                            int sureconfirm = snapshot.child("Esp32_Internet_State").getValue(int.class);
                                            if (sureconfirm == 23) {
                                                Toast.makeText(Home.this, "Process Sucessfull", Toast.LENGTH_SHORT).show();
                                                esp32getval = FirebaseDatabase.getInstance().getReference().child("All User").child(String.valueOf(textView)).child(String.valueOf(xer_total));
                                                esp32getval.child("Esp32_get_val").setValue(47);
                                                handleDateButton2();
                                            } else {
                                                Esp32hasNoInternet();
                                            }

                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });
                            }
                        },10000);
                    }
                }).create().show();
    }

    private void getmobiledatastatus3(){
        if (!isConnected(this)) {
            Toast.makeText(this, "No Internet", Toast.LENGTH_SHORT).show();
            ShowCustomDialog();
        }else{
            referencecheck = FirebaseDatabase.getInstance().getReference().child("All User").child(String.valueOf(textView)).child(String.valueOf(xer_total));
            referencecheck.child("Esp32_set_val").setValue(0);
            referencecheck.child("Esp32_get_val").setValue(0);
            Getauthstatus3();
        }
    }

    private void Getauthstatus3() {
        referencecheck = FirebaseDatabase.getInstance().getReference().child("All User").child(String.valueOf(textView)).child(String.valueOf(xer_total));

        referencecheck.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    int currentval= snapshot.child("Authentication_confirmed").getValue(int.class);
                    if(currentval==xer_total){
                        Gotosetalarm3();
                    }else{
                        InvalidDialog();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void Gotosetalarm3() {
        referencecheck = FirebaseDatabase.getInstance().getReference().child("All User").child(String.valueOf(textView)).child(String.valueOf(xer_total));

        referencecheck.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    int espval = snapshot.child("Esp32_Internet_State").getValue(int.class);
                    if(espval==23){
                        referencecheck = FirebaseDatabase.getInstance().getReference().child("All User").child(String.valueOf(textView)).child(String.valueOf(xer_total));
                        referencecheck.child("Esp32_Internet_State").setValue(0);
                        SureGotoSetalarm3();
                    }else{
                        Esp32hasNoInternet();
                    }
                }

            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void SureGotoSetalarm3() {
        new AlertDialog.Builder(this).setCancelable(false)
                .setTitle("NOTE").setMessage("Your Device is under process . Please wait for some time ").setIcon(R.drawable.cpu)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(Home.this, "Wait for 10 seconds", Toast.LENGTH_SHORT).show();
                        ProgressDialog dialog = ProgressDialog.show(
                                Home.this,
                                "ESP 32",
                                "Detecting....",
                                true);
                        dialog.setCancelable(false);
                        dialog.setIcon(R.drawable.calendar);
                        dialog.show();
                        final Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                dialog.dismiss();
                                referencecheck = FirebaseDatabase.getInstance().getReference().child("All User").child(String.valueOf(textView)).child(String.valueOf(xer_total));

                                referencecheck.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        if (snapshot.exists()) {
                                            int sureconfirm = snapshot.child("Esp32_Internet_State").getValue(int.class);
                                            if (sureconfirm == 23) {
                                                Toast.makeText(Home.this, "Process Sucessfull", Toast.LENGTH_SHORT).show();
                                                esp32getval = FirebaseDatabase.getInstance().getReference().child("All User").child(String.valueOf(textView)).child(String.valueOf(xer_total));
                                                esp32getval.child("Esp32_get_val").setValue(48);
                                                handleDateButton3();
                                            } else {
                                                Esp32hasNoInternet();
                                            }

                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });
                            }
                        },10000);
                    }
                }).create().show();
    }

    private void getmobiledatastatus4(){
        if (!isConnected(this)) {
            Toast.makeText(this, "No Internet", Toast.LENGTH_SHORT).show();
            ShowCustomDialog();
        }else{
            referencecheck = FirebaseDatabase.getInstance().getReference().child("All User").child(String.valueOf(textView)).child(String.valueOf(xer_total));
            referencecheck.child("Esp32_set_val").setValue(0);
            referencecheck.child("Esp32_get_val").setValue(0);
            Getauthstatus4();
        }
    }

    private void Getauthstatus4() {
        referencecheck = FirebaseDatabase.getInstance().getReference().child("All User").child(String.valueOf(textView)).child(String.valueOf(xer_total));

        referencecheck.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    int currentval= snapshot.child("Authentication_confirmed").getValue(int.class);
                    if(currentval==xer_total){
                        Gotosetalarm4();
                    }else{
                        InvalidDialog();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void Gotosetalarm4() {
        referencecheck = FirebaseDatabase.getInstance().getReference().child("All User").child(String.valueOf(textView)).child(String.valueOf(xer_total));

        referencecheck.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    int espval = snapshot.child("Esp32_Internet_State").getValue(int.class);
                    if(espval==23){
                        referencecheck = FirebaseDatabase.getInstance().getReference().child("All User").child(String.valueOf(textView)).child(String.valueOf(xer_total));
                        referencecheck.child("Esp32_Internet_State").setValue(0);
                        SureGotoSetalarm4();
                    }else{
                        Esp32hasNoInternet();
                    }
                }

            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void SureGotoSetalarm4() {
        new AlertDialog.Builder(this).setCancelable(false)
                .setTitle("NOTE").setMessage("Your Device is under process . Please wait for some time ").setIcon(R.drawable.cpu)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(Home.this, "Wait for 10 seconds", Toast.LENGTH_SHORT).show();
                        ProgressDialog dialog = ProgressDialog.show(
                                Home.this,
                                "ESP 32",
                                "Detecting....",
                                true);
                        dialog.setCancelable(false);
                        dialog.setIcon(R.drawable.calendar);
                        dialog.show();
                        final Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                dialog.dismiss();
                                referencecheck = FirebaseDatabase.getInstance().getReference().child("All User").child(String.valueOf(textView)).child(String.valueOf(xer_total));

                                referencecheck.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        if (snapshot.exists()) {
                                            int sureconfirm = snapshot.child("Esp32_Internet_State").getValue(int.class);
                                            if (sureconfirm == 23) {
                                                Toast.makeText(Home.this, "Process Sucessfull", Toast.LENGTH_SHORT).show();
                                                esp32getval = FirebaseDatabase.getInstance().getReference().child("All User").child(String.valueOf(textView)).child(String.valueOf(xer_total));
                                                esp32getval.child("Esp32_get_val").setValue(49);
                                                handleDateButton4();
                                            } else {
                                                Esp32hasNoInternet();
                                            }

                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });
                            }
                        },10000);
                    }
                }).create().show();
    }



    /*Setting date and time with calender*/
    private void handleDateButton1() {
        Calendar calendar = Calendar.getInstance();
        int YEAR = calendar.get(Calendar.YEAR);
        int MONTH = calendar.get(Calendar.MONTH);
        int DATE = calendar.get(Calendar.DATE);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int date) {
                month++;
                dateString1 = year + "/" + month + "/" + date;
                b1time.setText(dateString1 +", "+ timeString1);
                referencecheck = FirebaseDatabase.getInstance().getReference().child("All User").child(String.valueOf(textView)).child(String.valueOf(xer_total));
                referencecheck.child("Plug").child("S1").child("year").setValue(year);
                referencecheck.child("Plug").child("S1").child("month").setValue(month);
                referencecheck.child("Plug").child("S1").child("date").setValue(date);
                handleTimeButton1();
            }
        }, YEAR, MONTH, DATE);
        datePickerDialog.show();

    }

    private void handleTimeButton1() {
        Calendar calendar = Calendar.getInstance();
        int HOUR = calendar.get(calendar.HOUR);
        int MINUTE = calendar.get(calendar.MINUTE);
        TimePickerDialog timePickerDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int hour, int minute) {
                timeString1 =  hour + ":" + minute+":00";
                b1time.setText(dateString1 +", "+ timeString1);
                referencecheck = FirebaseDatabase.getInstance().getReference().child("All User").child(String.valueOf(textView)).child(String.valueOf(xer_total));
                referencecheck.child("Plug").child("S1").child("hour").setValue(hour);
                referencecheck.child("Plug").child("S1").child("min").setValue(minute);
                Somethingwentwrongalarm();
                saveData1();
                start_countdown_timer1();
            }
        }, HOUR, MINUTE, true);
        timePickerDialog.show();
    }
    private void handleDateButton2() {
        Calendar calendar = Calendar.getInstance();
        int YEAR = calendar.get(Calendar.YEAR);
        int MONTH = calendar.get(Calendar.MONTH);
        int DATE = calendar.get(Calendar.DATE);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int date) {
                month++;
                dateString2 = year + "/" + month + "/" + date;
                b2time.setText(dateString2 +", "+ timeString2);
                referencecheck = FirebaseDatabase.getInstance().getReference().child("All User").child(String.valueOf(textView)).child(String.valueOf(xer_total));
                referencecheck.child("Plug").child("S2").child("year").setValue(year);
                referencecheck.child("Plug").child("S2").child("month").setValue(month);
                referencecheck.child("Plug").child("S2").child("date").setValue(date);
                handleTimeButton2();
            }
        }, YEAR, MONTH, DATE);
        datePickerDialog.show();

    }

    private void handleTimeButton2() {
        Calendar calendar = Calendar.getInstance();
        int HOUR = calendar.get(calendar.HOUR);
        int MINUTE = calendar.get(calendar.MINUTE);
        TimePickerDialog timePickerDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int hour, int minute) {
                timeString2 =  hour + ":" + minute+":00";
                b2time.setText(dateString2 +", "+ timeString2);
                referencecheck = FirebaseDatabase.getInstance().getReference().child("All User").child(String.valueOf(textView)).child(String.valueOf(xer_total));
                referencecheck.child("Plug").child("S2").child("hour").setValue(hour);
                referencecheck.child("Plug").child("S2").child("min").setValue(minute);
                Somethingwentwrongalarm();
                saveData2();
                start_countdown_timer2();
            }
        }, HOUR, MINUTE, true);
        timePickerDialog.show();
    }

    private void handleDateButton3() {
        Calendar calendar = Calendar.getInstance();
        int YEAR = calendar.get(Calendar.YEAR);
        int MONTH = calendar.get(Calendar.MONTH);
        int DATE = calendar.get(Calendar.DATE);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int date) {
                month++;
                dateString3 = year + "/" + month + "/" + date;
                b3time.setText(dateString3+", "+ timeString3);
                referencecheck = FirebaseDatabase.getInstance().getReference().child("All User").child(String.valueOf(textView)).child(String.valueOf(xer_total));
                referencecheck.child("Plug").child("S3").child("year").setValue(year);
                referencecheck.child("Plug").child("S3").child("month").setValue(month);
                referencecheck.child("Plug").child("S3").child("date").setValue(date);
                handleTimeButton3();
            }
        }, YEAR, MONTH, DATE);
        datePickerDialog.show();

    }

    private void handleTimeButton3() {
        Calendar calendar = Calendar.getInstance();
        int HOUR = calendar.get(calendar.HOUR);
        int MINUTE = calendar.get(calendar.MINUTE);
        TimePickerDialog timePickerDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int hour, int minute) {
                timeString3 =  hour + ":" + minute+":00";
                b3time.setText(dateString3+", "+ timeString3);
                referencecheck = FirebaseDatabase.getInstance().getReference().child("All User").child(String.valueOf(textView)).child(String.valueOf(xer_total));
                referencecheck.child("Plug").child("S3").child("hour").setValue(hour);
                referencecheck.child("Plug").child("S3").child("min").setValue(minute);
                Somethingwentwrongalarm();
                saveData3();
                start_countdown_timer3();
            }
        }, HOUR, MINUTE, true);
        timePickerDialog.show();
    }

    private void handleDateButton4() {
        Calendar calendar = Calendar.getInstance();
        int YEAR = calendar.get(Calendar.YEAR);
        int MONTH = calendar.get(Calendar.MONTH);
        int DATE = calendar.get(Calendar.DATE);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int date) {
                month++;
                dateString4 = year + "/" + month + "/" + date;
                b4time.setText(dateString4 +", "+ timeString4);
                referencecheck = FirebaseDatabase.getInstance().getReference().child("All User").child(String.valueOf(textView)).child(String.valueOf(xer_total));
                referencecheck.child("Plug").child("S4").child("year").setValue(year);
                referencecheck.child("Plug").child("S4").child("month").setValue(month);
                referencecheck.child("Plug").child("S4").child("date").setValue(date);
                handleTimeButton4();
            }
        }, YEAR, MONTH, DATE);
        datePickerDialog.show();

    }

    private void handleTimeButton4() {
        Calendar calendar = Calendar.getInstance();
        int HOUR = calendar.get(calendar.HOUR);
        int MINUTE = calendar.get(calendar.MINUTE);
        TimePickerDialog timePickerDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int hour, int minute) {
                timeString4 =  hour + ":" + minute+":00";
                b4time.setText(dateString4 +", "+ timeString4);
                referencecheck = FirebaseDatabase.getInstance().getReference().child("All User").child(String.valueOf(textView)).child(String.valueOf(xer_total));
                referencecheck.child("Plug").child("S4").child("hour").setValue(hour);
                referencecheck.child("Plug").child("S4").child("min").setValue(minute);
                Somethingwentwrongalarm();
                saveData4();
                start_countdown_timer4();
            }
        }, HOUR, MINUTE, true);
        timePickerDialog.show();
    }


    /*Saving Data on SharedPreferences*/
    public void saveData1() {
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS1, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(TEXT1, b1time.getText().toString());
        editor.apply();
        loadData1();
        updateViews1();
    }
    public void loadData1() {
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS1, MODE_PRIVATE);
        saveText1 = sharedPreferences.getString(TEXT1, "Current Time and Date");
    }
    public void updateViews1() {
        b1time.setText(saveText1);
    }
    public void saveData2() {
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS2, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(TEXT2, b1time.getText().toString());
        editor.apply();
        loadData2();
        updateViews2();
    }
    public void loadData2() {
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS2, MODE_PRIVATE);
        saveText2 = sharedPreferences.getString(TEXT2, "Current Time and Date");
    }
    public void updateViews2() {
        b2time.setText(saveText2);
    }

    public void saveData3() {
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS3, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(TEXT3, b3time.getText().toString());
        editor.apply();
        loadData3();
        updateViews3();
    }
    public void loadData3() {
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS3, MODE_PRIVATE);
        saveText3 = sharedPreferences.getString(TEXT3, "Current Time and Date");
    }
    public void updateViews3() {
        b3time.setText(saveText3);
    }

    public void saveData4() {
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS4, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(TEXT4, b4time.getText().toString());
        editor.apply();
        loadData4();
        updateViews4();
    }
    public void loadData4() {
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS4, MODE_PRIVATE);
        saveText4 = sharedPreferences.getString(TEXT4, "Current Time and Date");
    }
    public void updateViews4() {
        b4time.setText(saveText4);
    }


    /*Count Down Timer Starts Here*/
    private void start_countdown_timer1()
    {

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd, HH:mm:ss");

        formatter.setLenient(false);

        if(setTime1==0) {
            endTime1 = dateString1 + ", " + timeString1;
        }
        // Toast.makeText(this, "v1"+endTime1, Toast.LENGTH_SHORT).show();
        long milliseconds1=0;

        final CountDownTimer mCountDownTimer1;

        Date endDate1;
        try {
            endDate1 = formatter.parse(endTime1);
            milliseconds1 = endDate1.getTime();
            // Toast.makeText(this, "v2"+endDate, Toast.LENGTH_SHORT).show();

        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            //Toast.makeText(this, "error"+e, Toast.LENGTH_SHORT).show();
        }

        startTime1 = System.currentTimeMillis();


        mCountDownTimer1 = new CountDownTimer(milliseconds1, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {

                startTime1=startTime1-1;
                Long serverUptimeSeconds =
                        (millisUntilFinished - startTime1) / 1000;

                String daysLeft1 = String.format("%d", serverUptimeSeconds / 86400);
                //txtViewDays.setText(daysLeft);
                Log.d("daysLeft",daysLeft1);

                String hoursLeft1 = String.format("%d", (serverUptimeSeconds % 86400) / 3600);
                //txtViewHours.setText(hoursLeft);
                Log.d("hoursLeft",hoursLeft1);

                String minutesLeft1 = String.format("%d", ((serverUptimeSeconds % 86400) % 3600) / 60);
                //txtViewMinutes.setText(minutesLeft);
                Log.d("minutesLeft",minutesLeft1);

                String secondsLeft1 = String.format("%d", ((serverUptimeSeconds % 86400) % 3600) % 60);
                //txtViewSecond.setText(secondsLeft);
                Log.d("secondsLeft",secondsLeft1);

                tTimer1.setText("D:"+daysLeft1+" H:"+hoursLeft1+" M:"+minutesLeft1+" S:"+secondsLeft1);

                int x = Integer.parseInt(secondsLeft1);
                int x1 = Integer.parseInt(minutesLeft1);
                int x2 = Integer.parseInt(hoursLeft1);
                int x3 = Integer.parseInt(daysLeft1);

                if (x <= -5 && x1 <= 0 && x2<= 0 && x3<=0){
                    tTimer1.setText("D:00 H:00 M:00 S:00");
                    closeTime1++;
                }

                if(chk_data1==3){
                    tTimer1.setText("D:00 H:00 M:00 S:00");
                    closeTime1++;
                }

            }

            @Override
            public void onFinish() {

            }
        }.start();


    }

    private void start_countdown_timer2()
    {


        SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd, HH:mm:ss");

        formatter.setLenient(false);
        if(setTime2==0) {
            endTime2 = dateString2 + ", " + timeString2;
        }
        // Toast.makeText(this, "v1"+endTime, Toast.LENGTH_SHORT).show();
        long milliseconds2=0;

        final CountDownTimer mCountDownTimer2;

        Date endDate2;
        try {
            endDate2 = formatter.parse(endTime2);
            milliseconds2 = endDate2.getTime();

        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        startTime2 = System.currentTimeMillis();


        mCountDownTimer2 = new CountDownTimer(milliseconds2, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {

                startTime2=startTime2-1;
                Long serverUptimeSeconds =
                        (millisUntilFinished - startTime2) / 1000;

                String daysLeft2 = String.format("%d", serverUptimeSeconds / 86400);
                //txtViewDays.setText(daysLeft);
                Log.d("daysLeft",daysLeft2);

                String hoursLeft2 = String.format("%d", (serverUptimeSeconds % 86400) / 3600);
                //txtViewHours.setText(hoursLeft);
                Log.d("hoursLeft",hoursLeft2);

                String minutesLeft2 = String.format("%d", ((serverUptimeSeconds % 86400) % 3600) / 60);
                //txtViewMinutes.setText(minutesLeft);
                Log.d("minutesLeft",minutesLeft2);

                String secondsLeft2 = String.format("%d", ((serverUptimeSeconds % 86400) % 3600) % 60);
                //txtViewSecond.setText(secondsLeft);
                Log.d("secondsLeft",secondsLeft2);

                tTimer2.setText("D:"+daysLeft2+" H:"+hoursLeft2+" M:"+minutesLeft2+" S:"+secondsLeft2);

                int y = Integer.parseInt(secondsLeft2);
                int y1 = Integer.parseInt(minutesLeft2);
                int y2 = Integer.parseInt(hoursLeft2);
                int y3 = Integer.parseInt(daysLeft2);

                if (y <= -5 && y1 <= 0 && y2<= 0 && y3<=0){
                    tTimer2.setText("D:00 H:00 M:00 S:00");
                    closeTime2++;
                }

                if(chk_data2==3){
                    tTimer2.setText("D:00 H:00 M:00 S:00");
                    closeTime2++;
                }

            }

            @Override
            public void onFinish() {

            }
        }.start();


    }

    private void start_countdown_timer3()
    {


        SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd, HH:mm:ss");

        formatter.setLenient(false);

        if(setTime3==0) {
            endTime3 = dateString3 + ", " + timeString3;
        }
        // Toast.makeText(this, "v1"+endTime, Toast.LENGTH_SHORT).show();
        long milliseconds3=0;

        final CountDownTimer mCountDownTimer3;

        Date endDate3;
        try {
            endDate3 = formatter.parse(endTime3);
            milliseconds3 = endDate3.getTime();
            // Toast.makeText(this, "v2"+endDate, Toast.LENGTH_SHORT).show();

        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            //Toast.makeText(this, "error"+e, Toast.LENGTH_SHORT).show();
        }

        startTime3 = System.currentTimeMillis();


        mCountDownTimer3 = new CountDownTimer(milliseconds3, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {

                startTime3=startTime3-1;
                Long serverUptimeSeconds =
                        (millisUntilFinished - startTime3) / 1000;

                String daysLeft3 = String.format("%d", serverUptimeSeconds / 86400);
                //txtViewDays.setText(daysLeft);
                Log.d("daysLeft",daysLeft3);

                String hoursLeft3 = String.format("%d", (serverUptimeSeconds % 86400) / 3600);
                //txtViewHours.setText(hoursLeft);
                Log.d("hoursLeft",hoursLeft3);

                String minutesLeft3 = String.format("%d", ((serverUptimeSeconds % 86400) % 3600) / 60);
                //txtViewMinutes.setText(minutesLeft);
                Log.d("minutesLeft",minutesLeft3);

                String secondsLeft3 = String.format("%d", ((serverUptimeSeconds % 86400) % 3600) % 60);
                //txtViewSecond.setText(secondsLeft);
                Log.d("secondsLeft",secondsLeft3);

                tTimer3.setText("D:"+daysLeft3+" H:"+hoursLeft3+" M:"+minutesLeft3+" S:"+secondsLeft3);

                int x = Integer.parseInt(secondsLeft3);
                int x1 = Integer.parseInt(minutesLeft3);
                int x2 = Integer.parseInt(hoursLeft3);
                int x3 = Integer.parseInt(daysLeft3);

                if (x <= -5 && x1 <= 0 && x2<= 0 && x3<=0){
                    tTimer3.setText("D:00 H:00 M:00 S:00");
                    closeTime3++;
                }

                if(chk_data3==3){
                    tTimer3.setText("D:00 H:00 M:00 S:00");
                    closeTime3++;
                }

            }

            @Override
            public void onFinish() {

            }
        }.start();


    }

    private void start_countdown_timer4()
    {

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd, HH:mm:ss");

        formatter.setLenient(false);

        if(setTime4==0) {
            endTime4 = dateString4 + ", " + timeString4;
        }
        // Toast.makeText(this, "v1"+endTime, Toast.LENGTH_SHORT).show();
        long milliseconds4=0;

        final CountDownTimer mCountDownTimer4;

        Date endDate4;
        try {
            endDate4 = formatter.parse(endTime4);
            milliseconds4 = endDate4.getTime();
            // Toast.makeText(this, "v2"+endDate, Toast.LENGTH_SHORT).show();

        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            //Toast.makeText(this, "error"+e, Toast.LENGTH_SHORT).show();
        }

        startTime4 = System.currentTimeMillis();


        mCountDownTimer4 = new CountDownTimer(milliseconds4, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {

                startTime4=startTime4-1;
                Long serverUptimeSeconds =
                        (millisUntilFinished - startTime4) / 1000;

                String daysLeft4 = String.format("%d", serverUptimeSeconds / 86400);
                //txtViewDays.setText(daysLeft);
                Log.d("daysLeft",daysLeft4);

                String hoursLeft4 = String.format("%d", (serverUptimeSeconds % 86400) / 3600);
                //txtViewHours.setText(hoursLeft);
                Log.d("hoursLeft",hoursLeft4);

                String minutesLeft4 = String.format("%d", ((serverUptimeSeconds % 86400) % 3600) / 60);
                //txtViewMinutes.setText(minutesLeft);
                Log.d("minutesLeft",minutesLeft4);

                String secondsLeft4 = String.format("%d", ((serverUptimeSeconds % 86400) % 3600) % 60);
                //txtViewSecond.setText(secondsLeft);
                Log.d("secondsLeft",secondsLeft4);

                tTimer4.setText("D:"+daysLeft4+" H:"+hoursLeft4+" M:"+minutesLeft4+" S:"+secondsLeft4);

                int x = Integer.parseInt(secondsLeft4);
                int x1 = Integer.parseInt(minutesLeft4);
                int x2 = Integer.parseInt(hoursLeft4);
                int x3 = Integer.parseInt(daysLeft4);

                if (x <= -5 && x1 <= 0 && x2<= 0 && x3<=0){
                    tTimer4.setText("D:00 H:00 M:00 S:00");
                    closeTime4++;
                }

                if(chk_data4==3){
                    tTimer4.setText("D:00 H:00 M:00 S:00");
                    closeTime4++;
                }

            }

            @Override
            public void onFinish() {

            }
        }.start();


    }


    private void Somethingwentwrongalarm() {
        ProgressDialog dialog = ProgressDialog.show(
                Home.this,
                "ESP 32",
                "Processing....",
                true);
        dialog.setCancelable(false);
        dialog.setIcon(R.drawable.calendar);
        dialog.show();
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                referencecheck = FirebaseDatabase.getInstance().getReference().child("All User").child(String.valueOf(textView)).child(String.valueOf(xer_total));

                referencecheck.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.exists()){
                            int getespval = snapshot.child("Esp32_set_val").getValue(int.class);
                            if(getespval==46){
                                referencecheck = FirebaseDatabase.getInstance().getReference().child("All User").child(String.valueOf(textView)).child(String.valueOf(xer_total));
                                referencecheck.child("Esp32_set_val").setValue(0);
                                referencecheck.child("Esp32_get_val").setValue(0);
                                dialog.dismiss();
                                Toast.makeText(Home.this, "Succesfully Completed", Toast.LENGTH_SHORT).show();
                            }else{
                                dialog.dismiss();
                                Somethingwentwrong();
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

            }
        },20000);
    }


    private void GetEsp32Internet() {
        if(!isConnected(this)){
            ShowCustomDialog();
        }else{
            referencecheck = FirebaseDatabase.getInstance().getReference().child("All User").child(String.valueOf(textView)).child(String.valueOf(xer_total));
            referencecheck.child("Esp32_set_val").setValue(0);
            referencecheck.child("Esp32_get_val").setValue(0);
            GetfirebaseData();
        }
    }

    private void GetfirebaseData() {
        referencecheck = FirebaseDatabase.getInstance().getReference().child("All User").child(String.valueOf(textView)).child(String.valueOf(xer_total));

        referencecheck.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    int value = snapshot.child("Authentication_confirmed").getValue(int.class);
                    if(value==xer_total){
                        Esp32Internet();
                    }else {
                        InvalidDialog();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(Home.this, "error"+error, Toast.LENGTH_SHORT).show();
            }
        });


    }

    private void Esp32Internet() {
        referencecheck = FirebaseDatabase.getInstance().getReference().child("All User").child(String.valueOf(textView)).child(String.valueOf(xer_total));

        referencecheck.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    int espval = snapshot.child("Esp32_Internet_State").getValue(int.class);
                    if(espval==23){
                        referencecheck = FirebaseDatabase.getInstance().getReference().child("All User").child(String.valueOf(textView)).child(String.valueOf(xer_total));
                        referencecheck.child("Esp32_Internet_State").setValue(0);
                        SureVerification();
                    }else{
                        Esp32hasNoInternet();
                    }
                }

            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void SureVerification() {
        new AlertDialog.Builder(this).setCancelable(false)
                .setTitle("NOTE").setMessage("Your Device is under process . Please wait for some time ").setIcon(R.drawable.cpu)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(Home.this, "Wait for 10 seconds", Toast.LENGTH_SHORT).show();
                        ProgressDialog dialog = ProgressDialog.show(
                                Home.this,
                                "ESP 32",
                                "Detecting....",
                                true);
                        dialog.setCancelable(false);
                        dialog.setIcon(R.drawable.calendar);
                        dialog.show();
                        final Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                dialog.dismiss();
                                referencecheck = FirebaseDatabase.getInstance().getReference().child("All User").child(String.valueOf(textView)).child(String.valueOf(xer_total));

                                referencecheck.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        if (snapshot.exists()) {
                                            int sureconfirm = snapshot.child("Esp32_Internet_State").getValue(int.class);
                                            if (sureconfirm == 23) {
                                                esp32getval = FirebaseDatabase.getInstance().getReference().child("All User").child(String.valueOf(textView)).child(String.valueOf(xer_total));
                                                esp32getval.child("Esp32_get_val").setValue(45);
                                                Getesp32DateandTime();
                                            } else {
                                                Esp32hasNoInternet();
                                            }

                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });
                            }
                        },10000);
                    }
                }).create().show();


    }

    private void Getesp32DateandTime() {
        ProgressDialog dialog = ProgressDialog.show(
                Home.this,
                "ESP 32",
                "Verifying....",
                true);
        dialog.setCancelable(false);
        dialog.setIcon(R.drawable.calendar);
        dialog.show();
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                referencecheck = FirebaseDatabase.getInstance().getReference().child("All User").child(String.valueOf(textView)).child(String.valueOf(xer_total));
                referencecheck.child("Esp32_set_val").setValue(0);
                referencecheck.child("Esp32_get_val").setValue(0);
                dialog.dismiss();
                esp32getval = FirebaseDatabase.getInstance().getReference().child("All User").child(String.valueOf(textView)).child(String.valueOf(xer_total)).child("Esp32_Time");

                esp32getval.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        if(snapshot.exists()){
                            int espdate= snapshot.child("day_esp32").getValue(int.class);
                            int espmonth= snapshot.child("month_esp32").getValue(int.class);
                            int espyear= snapshot.child("year_esp32").getValue(int.class);
                            int esphour= snapshot.child("hour_esp32").getValue(int.class);
                            int espminutes= snapshot.child("minutes_esp32").getValue(int.class);

                            String finalvalue="D: "+espyear+"/"+espmonth+"/"+espdate+","+"T:"+esphour+":"+espminutes;
                            espTime.setText(finalvalue);
                            final Handler handler1=new Handler();
                            handler1.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    espTime.setText("Time and Date");
                                }
                            },4000);

                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        },20000);



    }

    private void CheckEsp32Internet() {
        if (!isConnected(this)) {
            ShowCustomDialog();
        }else{
            referencecheck = FirebaseDatabase.getInstance().getReference().child("All User").child(String.valueOf(textView)).child(String.valueOf(xer_total));
            referencecheck.child("Esp32_set_val").setValue(0);
            referencecheck.child("Esp32_get_val").setValue(0);
            GetfirebasevalueTime();
        }
    }

    private void GetfirebasevalueTime() {
        referencecheck = FirebaseDatabase.getInstance().getReference().child("All User").child(String.valueOf(textView)).child(String.valueOf(xer_total));

        referencecheck.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    int currentval= snapshot.child("Authentication_confirmed").getValue(int.class);
                    if(currentval==xer_total){
                        GotosetdateandTime();
                    }else{
                        InvalidDialog();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void GotosetdateandTime() {
        referencecheck = FirebaseDatabase.getInstance().getReference().child("All User").child(String.valueOf(textView)).child(String.valueOf(xer_total));

        referencecheck.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    int espval = snapshot.child("Esp32_Internet_State").getValue(int.class);
                    if(espval==23){
                        referencecheck = FirebaseDatabase.getInstance().getReference().child("All User").child(String.valueOf(textView)).child(String.valueOf(xer_total));
                        referencecheck.child("Esp32_Internet_State").setValue(0);
                        SureVerification1();
                    }else{
                        Esp32hasNoInternet();
                    }
                }

            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void SureVerification1() {
        new AlertDialog.Builder(this).setCancelable(false)
                .setTitle("NOTE").setMessage("Your Device is under process . Please wait for some time ").setIcon(R.drawable.cpu)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(Home.this, "Wait for 10 seconds", Toast.LENGTH_SHORT).show();
                        ProgressDialog dialog = ProgressDialog.show(
                                Home.this,
                                "ESP 32",
                                "Detecting....",
                                true);
                        dialog.setCancelable(false);
                        dialog.setIcon(R.drawable.time);
                        dialog.show();
                        final Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                dialog.dismiss();
                                referencecheck = FirebaseDatabase.getInstance().getReference().child("All User").child(String.valueOf(textView)).child(String.valueOf(xer_total));

                                referencecheck.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        if (snapshot.exists()) {
                                            int sureconfirm = snapshot.child("Esp32_Internet_State").getValue(int.class);
                                            if (sureconfirm == 23) {
                                                Toast.makeText(Home.this, "Process Sucessfull", Toast.LENGTH_SHORT).show();
                                                esp32getval = FirebaseDatabase.getInstance().getReference().child("All User").child(String.valueOf(textView)).child(String.valueOf(xer_total));
                                                esp32getval.child("Esp32_get_val").setValue(43);
                                                Setesp32DateandTime();
                                            } else {
                                                Esp32hasNoInternet();
                                            }

                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });
                            }
                        },10000);
                    }
                }).create().show();
    }

    private void Setesp32DateandTime() {
        Calendar calendar = Calendar.getInstance();

        int thisYear = calendar.get(Calendar.YEAR);
        int thisMonth = calendar.get(Calendar.MONTH);
        thisMonth=thisMonth+1;
        int thisDay = calendar.get(Calendar.DAY_OF_MONTH);
        int hour = calendar.get(Calendar.HOUR);
        int minute = calendar.get(Calendar.MINUTE);
        int seconds = calendar.get(Calendar.SECOND);


        String val = "D:"+thisYear+"/"+thisMonth+"/"+thisDay+","+"T: "+hour+":"+minute;
        espTime.setText(val);
        esp32getval = FirebaseDatabase.getInstance().getReference().child("All User").child(String.valueOf(textView)).child(String.valueOf(xer_total)).child("Esp32_Time");
        esp32getval.child("day").setValue(thisDay);
        esp32getval.child("month").setValue(thisMonth);
        esp32getval.child("year").setValue(thisYear);
        esp32getval.child("hour").setValue(hour);
        esp32getval.child("minutes").setValue(minute);
        esp32getval.child("seconds").setValue(seconds);

        /*here to do the change*/

        Gotocheckesp32();

    }

    private void Gotocheckesp32() {
        ProgressDialog dialog = ProgressDialog.show(
                Home.this,
                "ESP 32",
                "Processing....",
                true);
        dialog.setCancelable(false);
        dialog.setIcon(R.drawable.time);
        dialog.show();
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                referencecheck = FirebaseDatabase.getInstance().getReference().child("All User").child(String.valueOf(textView)).child(String.valueOf(xer_total));

                referencecheck.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.exists()){
                            int getespval = snapshot.child("Esp32_set_val").getValue(int.class);
                            if(getespval==46){
                                referencecheck = FirebaseDatabase.getInstance().getReference().child("All User").child(String.valueOf(textView)).child(String.valueOf(xer_total));
                                referencecheck.child("Esp32_set_val").setValue(0);
                                referencecheck.child("Esp32_get_val").setValue(0);
                                dialog.dismiss();
                                Toast.makeText(Home.this, "Succesfully Completed", Toast.LENGTH_SHORT).show();
                                espTime.setText("Date and Time");
                            }else{
                                dialog.dismiss();
                                espTime.setText("Date and Time");
                                Somethingwentwrong();
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

            }
        },20000);
    }

    private void Somethingwentwrong() {
        new AlertDialog.Builder(this).setCancelable(false)
                .setTitle("Error").setMessage("Something went wrong, Please try again").setIcon(R.drawable.errorx)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                }).create().show();
    }

    private void CheckInternetst() {
        if (!isConnected(this)) {
            Toast.makeText(this, "No Internet", Toast.LENGTH_SHORT).show();
            ShowCustomDialog();
        } else {
            referencecheck = FirebaseDatabase.getInstance().getReference().child("All User").child(String.valueOf(textView)).child(String.valueOf(xer_total));
            referencecheck.child("Esp32_set_val").setValue(0);
            referencecheck.child("Esp32_get_val").setValue(0);
            Getauthstatus();
        }
    }

    private void Getauthstatus() {
        referencecheck = FirebaseDatabase.getInstance().getReference().child("All User").child(String.valueOf(textView)).child(String.valueOf(xer_total));

        referencecheck.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    int currentval= snapshot.child("Authentication_confirmed").getValue(int.class);
                    if(currentval==xer_total){
                        GotoESPBoard();
                    }else{
                        InvalidDialog();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void GotoESPBoard() {
        referencecheck = FirebaseDatabase.getInstance().getReference().child("All User").child(String.valueOf(textView)).child(String.valueOf(xer_total));

        referencecheck.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    int espval = snapshot.child("Esp32_Internet_State").getValue(int.class);
                    if(espval==23){
                        referencecheck = FirebaseDatabase.getInstance().getReference().child("All User").child(String.valueOf(textView)).child(String.valueOf(xer_total));
                        referencecheck.child("Esp32_Internet_State").setValue(0);
                        status.setText("Online");
                        status.setTextColor(getResources().getColor(R.color.teal_200));
                    }else{
                        Esp32hasNoInternet();
                    }
                }

            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void Esp32hasNoInternet() {
        status.setText("Offline");
        status.setTextColor(getResources().getColor(R.color.teal_red));
        new AlertDialog.Builder(this).setCancelable(false)
                .setTitle("Error").setMessage("Your device is currently offline").setIcon(R.drawable.error)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                }).create().show();

    }

    private void InvalidDialog() {
        new AlertDialog.Builder(this).setCancelable(false)
                .setTitle("ALERT !").setMessage("Your Authentication Key is Invalid")
                .setIcon(R.drawable.cpu)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                }).create().show();
    }

    private void ShowCustomDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(Home.this);
        builder.setMessage("Please Check your connection")
                .setTitle("IOT APP")
                .setCancelable(false)
                .setIcon(R.drawable.c_joseph)
                .setPositiveButton("Connect", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        startActivity(new Intent(android.provider.Settings.ACTION_WIFI_SETTINGS));
                    }
                }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        }).create().show();
    }

    private boolean isConnected(Home mainActivity) {
        ConnectivityManager connectivityManager = (ConnectivityManager) mainActivity.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo wifiCon = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        NetworkInfo mobileConn = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

        if ((wifiCon != null && wifiCon.isConnected()) || (mobileConn != null && mobileConn.isConnected())) {
            return true;
        } else {
            return false;
        }
    }

    private void gotoUrl(String s) {
        Uri uri = Uri.parse(s);
        startActivity(new Intent(Intent.ACTION_VIEW, uri));
    }

    private void userInfo() {
        GoogleSignInAccount signInAccount = GoogleSignIn.getLastSignedInAccount(this);
        new AlertDialog.Builder(Home.this).setCancelable(false)
                .setTitle("IOT APP")
                .setIcon(R.drawable.icon_google)
                .setMessage("Name : " + signInAccount.getDisplayName() + "\n" + "Email : " + signInAccount.getEmail())
                .setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        }).create().show();
    }

    private void Resetqr() {
        SharedPreferences prefs = getSharedPreferences("prefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean("firstStart", true);
        editor.apply();
        Intent qr = new Intent(Home.this, MainActivity.class);
        startActivity(qr);
    }

    private void LogoutUser() {
        FirebaseAuth.getInstance().signOut();
        Intent intent = new Intent(getApplicationContext(), Login.class);
        startActivity(intent);
    }

    public void loadData() {
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        textView = sharedPreferences.getString(TEXT, "");

    }

    public void updateViews() {
        char code1 = textView.charAt(textView.length() - 2);
        char code2 = textView.charAt(textView.length() - 1);
        qrtxt.setText("X" + code1 + code2);
    }

    private void showstartpath() {
        int number=0;
        try{
            number = Integer.parseInt(textView);
            System.out.println(number);
        }
        catch (NumberFormatException ex){
            ex.printStackTrace();
        }
        GoogleSignInAccount signInAccount = GoogleSignIn.getLastSignedInAccount(this);

        int finalNumber = number;
        new AlertDialog.Builder(this).setCancelable(false).setIcon(R.drawable.fingerprints)
                .setTitle("ALERT !").setMessage("Click OK and than press the Authentication Button")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        reference = FirebaseDatabase.getInstance().getReference().child("All User").child(String.valueOf(textView)).child(String.valueOf(xer_total));
                        reference.child("Name").setValue(signInAccount.getDisplayName());
                        reference.child("Email").setValue(signInAccount.getEmail());
                        reference.child("QR_Code").setValue(finalNumber);
                        reference.child("Authentication_confirmed").setValue(0);
                        reference.child("Esp32_get_val").setValue(0);
                        reference.child("Esp32_set_val").setValue(0);
                        reference.child("Esp32_Internet_State").setValue(0);
                        reference.child("Plug").child("S1").child("year").setValue("null");
                        reference.child("Plug").child("S1").child("month").setValue("null");
                        reference.child("Plug").child("S1").child("date").setValue("null");
                        reference.child("Plug").child("S1").child("hour").setValue("null");
                        reference.child("Plug").child("S1").child("min").setValue("null");
                        reference.child("Plug").child("S2").child("year").setValue("null");
                        reference.child("Plug").child("S2").child("month").setValue("null");
                        reference.child("Plug").child("S2").child("date").setValue("null");
                        reference.child("Plug").child("S2").child("hour").setValue("null");
                        reference.child("Plug").child("S2").child("min").setValue("null");
                        reference.child("Plug").child("S3").child("year").setValue("null");
                        reference.child("Plug").child("S3").child("month").setValue("null");
                        reference.child("Plug").child("S3").child("date").setValue("null");
                        reference.child("Plug").child("S3").child("hour").setValue("null");
                        reference.child("Plug").child("S3").child("min").setValue("null");
                        reference.child("Plug").child("S4").child("year").setValue("null");
                        reference.child("Plug").child("S4").child("month").setValue("null");
                        reference.child("Plug").child("S4").child("date").setValue("null");
                        reference.child("Plug").child("S4").child("hour").setValue("null");
                        reference.child("Plug").child("S4").child("min").setValue("null");
                        reference.child("Esp32_Time").child("day").setValue(0);
                        reference.child("Esp32_Time").child("month").setValue(0);
                        reference.child("Esp32_Time").child("year").setValue(0);
                        reference.child("Esp32_Time").child("hour").setValue(0);
                        reference.child("Esp32_Time").child("minutes").setValue(0);
                        reference.child("Esp32_Time").child("seconds").setValue(0);
                        reference.child("Esp32_Time").child("day_esp32").setValue(0);
                        reference.child("Esp32_Time").child("month_esp32").setValue(0);
                        reference.child("Esp32_Time").child("year_esp32").setValue(0);
                        reference.child("Esp32_Time").child("hour_esp32").setValue(0);
                        reference.child("Esp32_Time").child("minutes_esp32").setValue(0);
                        reference.child("Esp32_Time").child("seconds_esp32").setValue(0);
                    }
                }).create().show();

        SharedPreferences onetime = getSharedPreferences("onetime", MODE_PRIVATE);
        SharedPreferences.Editor one_editor = onetime.edit();
        one_editor.putBoolean("gonetime", false);
        one_editor.apply();
    }

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this).setCancelable(false)
                .setTitle("IOT APP")
                .setIcon(R.drawable.c_joseph)
                .setMessage("Are you sure you want to exit this IOT APP Application?")
                .setPositiveButton("YES",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                finishAffinity();
                            }
                        }).setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        }).create().show();
    }
}