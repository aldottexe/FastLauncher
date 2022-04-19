package com.example.launchertest1;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.ActivityResultRegistry;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

public class AppsDrawer extends AppCompatActivity {

    ArrayList data = new ArrayList<appInfo>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apps_drawer);

        //hides title bar
        getSupportActionBar().hide();

        // **** fill array with apps here
        PackageManager pm = getPackageManager();

        //Intent.ACTION_MAIN filters intents by those that start other apps
        Intent i = new Intent(Intent.ACTION_MAIN, null);
        i.addCategory(Intent.CATEGORY_LAUNCHER);

        List<ResolveInfo> allApps = pm.queryIntentActivities(i, 0);

        for(ResolveInfo ri:allApps) {
            System.out.println( "package name" + ri.activityInfo.packageName);
            appInfo app = new appInfo();
            app.packageName = ri.activityInfo.packageName;
            app.label = (String) ri.loadLabel(pm);
            app.icon = ri.activityInfo.loadIcon(pm);
            data.add(app);
        }
        System.out.println("done making list!!! " + data.size() + " items");

        //attaches the filler classes to the recyclerview
        RecyclerView drawer = findViewById(R.id.recyclerView);
        drawer.setLayoutManager(new GridLayoutManager(this, 4));
        drawer.setAdapter(new drawerAdapter(this, data));



    }


}