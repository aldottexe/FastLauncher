package com.example.launchertest1;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

public class AppsDrawer extends AppCompatActivity {

    ArrayList<appInfo> data = new ArrayList();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apps_drawer);


        // **** fill array with apps here
        PackageManager pm = getPackageManager();
        Intent i = new Intent(Intent.ACTION_MAIN, null);
        i.addCategory(Intent.CATEGORY_LAUNCHER);

        List<ResolveInfo> allApps = pm.queryIntentActivities(i, 0);
        for(ResolveInfo ri:allApps) {
            appInfo app = new appInfo();
            app.packageName = ri.activityInfo.packageName;
            app.icon = ri.activityInfo.loadIcon(pm);
            data.add(app);
        }



        //convert the arraylist to an array.
        appInfo[] dataArray = (appInfo[]) data.toArray();

        //attaches the filler classes to the recyclerview
        RecyclerView drawer = findViewById(R.id.recyclerView);
        drawer.setLayoutManager(new GridLayoutManager(this, 4));
        drawer.setAdapter(new drawerAdapter(dataArray));
    }
}