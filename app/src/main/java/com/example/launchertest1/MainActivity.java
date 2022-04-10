package com.example.launchertest1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ImageView chromeIcon = (ImageView) findViewById(R.id.chromeButton);

        //chromeIcon.setImageDrawable(getActivityIcon(this, "com.oneplus.deskclock", "com.oneplus.deskclock.DeskClock"));

    }

    public void onChromeButtonClick(View v) {
        Intent launchIntent = getPackageManager().getLaunchIntentForPackage("com.android.chrome");
        startActivity(launchIntent);
    }

    public void onMenuClick(View view){
        Intent intent = new Intent(this, AppsDrawer.class);
        startActivity(intent);
    }


    public static Drawable getActivityIcon(Context context, String packageName, String activityName) {
        PackageManager pm = context.getPackageManager();
        Intent intent = new Intent();
        intent.setComponent(new ComponentName(packageName, activityName));
        System.out.println("this is the intent: " + intent);
        System.out.println("this is the pm:" + pm);
        ResolveInfo resolveInfo = pm.resolveActivity(intent, 0);
        System.out.println(resolveInfo);
        return resolveInfo.loadIcon(pm);
    }

}
