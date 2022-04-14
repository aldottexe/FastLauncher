package com.example.launchertest1;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

public class MainActivity extends AppCompatActivity {

    final int threshHold = 200;
    int appChoice;
    //the gesture detectors will choose an app to launch on release based on this layout
    //0  1   |   4  5
    //2  3   |   6  7
    //---------------
    //8  9   |   12 13
    //10 11  |   14 15

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ConstraintLayout tlBox = findViewById(R.id.tl_box);
        ConstraintLayout trBox = findViewById(R.id.tr_box);
        ConstraintLayout blBox = findViewById(R.id.bl_box);
        ConstraintLayout brBox = findViewById(R.id.br_box);

        ///////////////////////////SWIPE DETECTORS//////////////////////////////

        //X0
        //00
        tlBox.setOnTouchListener(new View.OnTouchListener() {
            float[] refs = new float[2];
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch(motionEvent.getAction()) {
                    case(MotionEvent.ACTION_DOWN):{
                        refs[0] = motionEvent.getX();
                        refs[1] = motionEvent.getY();
                    }
                    case(MotionEvent.ACTION_MOVE):{

                        float deltaX = motionEvent.getX() - refs[0];
                        float deltaY = motionEvent.getY() - refs[1];

                        if (deltaX > threshHold / 1.4 && deltaY > threshHold / 1.4) {
                            colorTiles(tlBox, trBox, blBox, brBox, R.color.white, R.color.gray, R.color.gray, R.color.tile1);
                            appChoice = 3;
                        } else if (deltaX > threshHold) {
                            colorTiles(tlBox, trBox, blBox, brBox, R.color.white, R.color.tile1, R.color.gray, R.color.white);
                            appChoice = 1;
                        } else if (deltaY > threshHold) {
                            colorTiles(tlBox, trBox, blBox, brBox, R.color.white, R.color.gray, R.color.tile1, R.color.white);
                            appChoice = 2;
                        } else {
                            colorTiles(tlBox, trBox, blBox, brBox, R.color.tile1, R.color.gray, R.color.gray, R.color.white);
                            appChoice = 0;
                        }
                    }
                    case(MotionEvent.ACTION_UP):{
                        colorTiles(tlBox, trBox, blBox, brBox, R.color.white, R.color.gray, R.color.gray, R.color.white);

                    }
                    default:{
                        colorTiles(tlBox, trBox, blBox, brBox, R.color.white, R.color.gray, R.color.gray, R.color.white);
                    }
                }
                return true;
            }
        });

        //0X
        //00
        trBox.setOnTouchListener(new View.OnTouchListener() {
            float[] refs = new float[2];
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch(motionEvent.getAction()) {
                    case(MotionEvent.ACTION_DOWN):{
                        refs[0] = motionEvent.getX();
                        refs[1] = motionEvent.getY();
                    }
                    case(MotionEvent.ACTION_MOVE):{

                        float deltaX = motionEvent.getX() - refs[0];
                        float deltaY = motionEvent.getY() - refs[1];

                        if (deltaX < threshHold / -1.4 && deltaY > threshHold / 1.4) {
                            colorTiles(tlBox, trBox, blBox, brBox, R.color.white, R.color.gray, R.color.tile2, R.color.white);
                            appChoice = 7;
                        } else if (deltaX < -1 * threshHold) {
                            colorTiles(tlBox, trBox, blBox, brBox, R.color.tile2, R.color.gray, R.color.gray, R.color.white);
                            appChoice = 5;
                        } else if (deltaY > threshHold) {
                            colorTiles(tlBox, trBox, blBox, brBox, R.color.white, R.color.gray, R.color.gray, R.color.tile2);
                            appChoice = 6;
                        } else {
                            colorTiles(tlBox, trBox, blBox, brBox, R.color.white, R.color.tile2, R.color.gray, R.color.white);
                            appChoice = 4;
                        }
                    }
                    case(MotionEvent.ACTION_UP):{
                        colorTiles(tlBox, trBox, blBox, brBox, R.color.white, R.color.gray, R.color.gray, R.color.white);

                    }
                    default:{
                        colorTiles(tlBox, trBox, blBox, brBox, R.color.white, R.color.gray, R.color.gray, R.color.white);
                    }
                }
                return true;
            }
        });

        //00
        //X0
        blBox.setOnTouchListener(new View.OnTouchListener() {
            float[] refs = new float[2];
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch(motionEvent.getAction()) {
                    case(MotionEvent.ACTION_DOWN):{
                        refs[0] = motionEvent.getX();
                        refs[1] = motionEvent.getY();
                    }
                    case(MotionEvent.ACTION_MOVE):{

                        float deltaX = motionEvent.getX() - refs[0];
                        float deltaY = motionEvent.getY() - refs[1];

                        if (deltaX > threshHold / 1.4 && deltaY < threshHold / -1.4) {
                            colorTiles(tlBox, trBox, blBox, brBox, R.color.white, R.color.tile3, R.color.gray, R.color.white);
                            appChoice = 9;
                        } else if (deltaX > threshHold) {
                            colorTiles(tlBox, trBox, blBox, brBox, R.color.white, R.color.gray, R.color.gray, R.color.tile3);
                            appChoice = 11;
                        } else if (deltaY < -1 * threshHold) {
                            colorTiles(tlBox, trBox, blBox, brBox, R.color.tile3, R.color.gray, R.color.gray, R.color.white);
                            appChoice = 8;
                        } else {
                            colorTiles(tlBox, trBox, blBox, brBox, R.color.white, R.color.gray, R.color.tile3, R.color.white);
                            appChoice = 10;
                        }
                    }
                    case(MotionEvent.ACTION_UP):{
                        colorTiles(tlBox, trBox, blBox, brBox, R.color.white, R.color.gray, R.color.gray, R.color.white);

                    }
                    default:{
                        colorTiles(tlBox, trBox, blBox, brBox, R.color.white, R.color.gray, R.color.gray, R.color.white);
                    }
                }
                return true;
            }
        });


        //00
        //0X
        brBox.setOnTouchListener(new View.OnTouchListener() {
            float[] refs = new float[2];
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch(motionEvent.getAction()) {
                    case(MotionEvent.ACTION_DOWN):{
                        refs[0] = motionEvent.getX();
                        refs[1] = motionEvent.getY();
                    }
                    case(MotionEvent.ACTION_MOVE):{

                        float deltaX = motionEvent.getX() - refs[0];
                        float deltaY = motionEvent.getY() - refs[1];

                        if (deltaX < threshHold / -1.4 && deltaY < threshHold / -1.4) {
                            colorTiles(tlBox, trBox, blBox, brBox, R.color.tile4, R.color.gray, R.color.gray, R.color.white);
                            appChoice = 9;
                        } else if (deltaX < -1 * threshHold) {
                            colorTiles(tlBox, trBox, blBox, brBox, R.color.white, R.color.gray, R.color.tile4, R.color.white);
                            appChoice = 11;
                        } else if (deltaY < -1 * threshHold) {
                            colorTiles(tlBox, trBox, blBox, brBox, R.color.white, R.color.tile4, R.color.gray, R.color.white);
                            appChoice = 8;
                        } else {
                            colorTiles(tlBox, trBox, blBox, brBox, R.color.white, R.color.gray, R.color.gray, R.color.tile4);
                            appChoice = 10;
                        }
                    }
                    case(MotionEvent.ACTION_UP):{
                        colorTiles(tlBox, trBox, blBox, brBox, R.color.white, R.color.gray, R.color.gray, R.color.white);

                    }
                    default:{
                        colorTiles(tlBox, trBox, blBox, brBox, R.color.white, R.color.gray, R.color.gray, R.color.white);
                    }
                }
                return true;
            }
        });

    }

    public void colorTiles(ConstraintLayout tlBox, ConstraintLayout trBox, ConstraintLayout blBox, ConstraintLayout brBox, int tlColor, int trColor, int blColor, int brColor){
        tlBox.setBackgroundColor(tlColor);
        trBox.setBackgroundColor(trColor);
        blBox.setBackgroundColor(blColor);
        brBox.setBackgroundColor(brColor);
    }

    public void launch(String packageName){
        Intent launchIntent = getPackageManager().getLaunchIntentForPackage(packageName);
        startActivity(launchIntent);
    }

    public void onMenuClick(View view) {
        mStartForResult.launch(null);

    }



    ///////////////////////////////intent stuff////////////////////////////////////

    //custom contract guy so you can define what you send and receive from the app drawer activity
    public class drawerContract extends ActivityResultContract<Void, String>{

        @Override
        public Intent createIntent(Context context, Void input) {
            return new Intent(getApplicationContext(), AppsDrawer.class);
        }

        @Override
        public String parseResult(int resultCode, Intent intent) {
            if (resultCode != Activity.RESULT_OK || intent == null) {
                return null;
            }
            return intent.getStringExtra("packageName");
        }

    }

    //declares the object that is used to start the app drawer activity
    ActivityResultLauncher<Void> mStartForResult = registerForActivityResult(new drawerContract(),
            new ActivityResultCallback<String>() {

                //this method is called after the secondary activity is completed..
                //this is where you process the returned data
                @Override
                public void onActivityResult(String resultPackage) {
                    if (resultPackage!= null) {
                        System.out.println("data received" + resultPackage);
                        launch(resultPackage);
                    }
                }
            });


}
