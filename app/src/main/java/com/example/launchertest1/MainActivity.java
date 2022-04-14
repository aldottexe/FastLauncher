package com.example.launchertest1;

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

    public int threshHold = 200;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final float[] refs = new float[2];
        float refY;

        ConstraintLayout tlBox = findViewById(R.id.tl_box);
        ConstraintLayout trBox = findViewById(R.id.tr_box);
        ConstraintLayout blBox = findViewById(R.id.bl_box);
        ConstraintLayout brBox = findViewById(R.id.br_box);


        tlBox.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View view, MotionEvent motionEvent) {
               //System.out.println(motionEvent.getAction());

                if(motionEvent.getAction() == MotionEvent.ACTION_DOWN){
                    refs[0] = motionEvent.getX();
                    refs[1] = motionEvent.getY();

                }
                else if(motionEvent.getAction() == MotionEvent.ACTION_MOVE){
                    System.out.println("moving @ " + motionEvent.getX() + "," + motionEvent.getY());

                    float deltaX = motionEvent.getX() - refs[0];
                    float deltaY = motionEvent.getY() - refs[1];

                    if(deltaX > threshHold / 1.4 && deltaY > threshHold / 1.4){
                        trBox.setBackgroundColor(Color.parseColor("#EEEEEE"));
                        blBox.setBackgroundColor(Color.parseColor("#EEEEEE"));
                        brBox.setBackgroundColor(Color.parseColor("#e6f542"));
                    }
                    else if(deltaX > threshHold){
                        trBox.setBackgroundColor(Color.parseColor("#e6f542"));
                        blBox.setBackgroundColor(Color.parseColor("#EEEEEE"));
                        brBox.setBackgroundColor(Color.parseColor("#FFFFFF"));
                    }
                    else if(deltaY > threshHold){
                        trBox.setBackgroundColor(Color.parseColor("#EEEEEE"));
                        blBox.setBackgroundColor(Color.parseColor("#e6f542"));
                        brBox.setBackgroundColor(Color.parseColor("#FFFFFF"));
                    }
                    else{
                        trBox.setBackgroundColor(Color.parseColor("#EEEEEE"));
                        blBox.setBackgroundColor(Color.parseColor("#EEEEEE"));
                        brBox.setBackgroundColor(Color.parseColor("#FFFFFF"));
                    }
                }
                else{
                    trBox.setBackgroundColor(Color.parseColor("#EEEEEE"));
                    blBox.setBackgroundColor(Color.parseColor("#EEEEEE"));
                    brBox.setBackgroundColor(Color.parseColor("#FFFFFF"));
                }
                return true;
            }
        });





    }


    public void onMenuClick(View view) {
        mStartForResult.launch(null);

    }

    public void onScroll1(View view){
        System.out.println("scrolling");
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
                        //Toast.makeText(getApplicationContext(), s, Toast.LENGTH_SHORT).show();
                        Intent launchIntent = getPackageManager().getLaunchIntentForPackage(resultPackage);
                        startActivity(launchIntent);
                    }
                }
            });


}
