package com.example.launchertest1;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {



    public class drawerContract extends ActivityResultContract<Void, String>{

        @Override
        public Intent createIntent(@NonNull Context context, Void input) {
            return new Intent(getApplicationContext(), AppsDrawer.class);
        }

        @Override
        public String parseResult(int resultCode, @Nullable Intent intent) {
            if (resultCode != Activity.RESULT_OK || intent == null) {
                return null;
            }
            return intent.getStringExtra("packageName");
        }

    }


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





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }


    public void onMenuClick(View view) {
        mStartForResult.launch(null);

    }
}
