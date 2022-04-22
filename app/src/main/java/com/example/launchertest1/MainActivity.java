package com.example.launchertest1;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.WallpaperManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewOutlineProvider;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;

import java.util.Arrays;

import io.alterac.blurkit.BlurLayout;

public class MainActivity extends AppCompatActivity {

    boolean editing = false;

    BlurLayout blur;
    //holds the list of package names referenced by the tiles
    String[] favoritePackagesArray = new String[16];
    //holds the label and icon for favorited apps
    appInfo[] favInfo;
    final int threshHold = 200;
    int appChoice;
    //the gesture detectors will choose an app to launch on release based on this layout
    //0  1   |   4  5
    //2  3   |   6  7
    //---------------
    //8  9   |   12 13
    //10 11  |   14 15

    SharedPreferences sharedPref;

    @RequiresApi(api = Build.VERSION_CODES.R)
    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //hides title bar
        getSupportActionBar().hide();
        getWindow().setDecorFitsSystemWindows(false);

        //reference for a blur object behind the tiles
        blur = findViewById(R.id.blurLayout);

        ConstraintLayout bh = findViewById(R.id.box_holder);
        bh.setOutlineProvider(ViewOutlineProvider.BACKGROUND);
        bh.setClipToOutline(true);

        //grabs the system wallpaper and sets it to an imageview behind
        ImageView bg = findViewById(R.id.bg);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        Drawable currentSystemBg = WallpaperManager.getInstance(this).getDrawable();
        bg.setImageDrawable(currentSystemBg);

        //get preference file (theres a default one included with each activity that can be used to save preferences)
        sharedPref = getPreferences(Context.MODE_PRIVATE);

        //grab a string with the value "packageList", the empty string in the second parameter is the value if the key doesn't match anything.
        String raw = sharedPref.getString("packageList","com.android.chrome");
        System.out.println("raw data: " + raw);
        if(raw != null){
            String[] rawArray = raw.split(", ");
            for(int i = 0; i < rawArray.length; i++)
                favoritePackagesArray[i] = rawArray[i];
        }

        //takes the
        favInfo = infoFromPackageList(favoritePackagesArray);
        System.out.println("========================================================================================");
        ///////////////////////////SWIPE DETECTORS//////////////////////////////
        ConstraintLayout[] tiles = {findViewById(R.id.tl_box),
                findViewById(R.id.tr_box),
                findViewById(R.id.bl_box),
                findViewById(R.id.br_box)};

        //X0
        //00
        tiles[0].setOnTouchListener(new View.OnTouchListener() {
            final float[] refs = new float[2];
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch(motionEvent.getAction()) {

                    case(MotionEvent.ACTION_DOWN):{
                        drawIcons(tiles, 0);

                        refs[0] = motionEvent.getX();
                        refs[1] = motionEvent.getY();
                        System.out.println(refs[0] + "," + refs[1]);
                        colorTiles(tiles, R.color.tile1, R.color.gray, R.color.gray, R.color.gray);
                        appChoice = 0;
                        return true;
                    }

                    case(MotionEvent.ACTION_MOVE):{
                        float deltaX = motionEvent.getX() - refs[0];
                        float deltaY = motionEvent.getY() - refs[1];
                        if (motionEvent.getX() < 0 || motionEvent.getY() < 0){
                            colorTiles(tiles, R.color.gray, R.color.gray, R.color.gray, R.color.gray);
                            appChoice = -1;
                        }else if (deltaX > threshHold / 1.4 && deltaY > threshHold / 1.4) {
                            colorTiles(tiles, R.color.gray, R.color.gray, R.color.gray, R.color.tile1);
                            appChoice = 3;
                        } else if (deltaX > threshHold) {
                            colorTiles(tiles, R.color.gray, R.color.tile1, R.color.gray, R.color.gray);
                            appChoice = 1;
                        } else if (deltaY > threshHold) {
                            colorTiles(tiles, R.color.gray, R.color.gray, R.color.tile1, R.color.gray);
                            appChoice = 2;
                        } else {
                            colorTiles(tiles, R.color.tile1, R.color.gray, R.color.gray, R.color.gray);
                            appChoice = 0;
                        }
                    return true;
                    }

                    case(MotionEvent.ACTION_UP):{
                        colorTiles(tiles, R.color.gray, R.color.gray, R.color.gray, R.color.gray);
                        resetIcons(tiles);

                        if(appChoice < 0) return true;

                        if(editing
                                || !(favoritePackagesArray[appChoice] instanceof String)
                                || favoritePackagesArray[appChoice].equals("null")){
                            editing = true;
                            mStartForResult.launch(null);
                        }
                        else launch(favoritePackagesArray[appChoice]);
                    }
                }
                return true;
            }
        });

        //0X
        //00
        tiles[1].setOnTouchListener(new View.OnTouchListener() {
            final float[] refs = new float[2];
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch(motionEvent.getAction()) {
                    case(MotionEvent.ACTION_DOWN):{
                        drawIcons(tiles, 1);

                        refs[0] = motionEvent.getX();
                        refs[1] = motionEvent.getY();

                        colorTiles(tiles, R.color.gray, R.color.tile2, R.color.gray, R.color.gray);

                        appChoice = 5;
                        return true;
                    }
                    case(MotionEvent.ACTION_MOVE):{

                        float deltaX = motionEvent.getX() - refs[0];
                        float deltaY = motionEvent.getY() - refs[1];
                        if (motionEvent.getX() > 400 || motionEvent.getY() < 0){
                            colorTiles(tiles, R.color.gray, R.color.gray, R.color.gray, R.color.gray);
                            appChoice = -1;
                        }else if (deltaX < threshHold / -1.4 && deltaY > threshHold / 1.4) {
                            colorTiles(tiles, R.color.gray, R.color.gray, R.color.tile2, R.color.gray);
                            appChoice = 6;
                        } else if (deltaX < -1 * threshHold) {
                            colorTiles(tiles, R.color.tile2, R.color.gray, R.color.gray, R.color.gray);
                            appChoice = 4;
                        } else if (deltaY > threshHold) {
                            colorTiles(tiles, R.color.gray, R.color.gray, R.color.gray, R.color.tile2);
                            appChoice = 7;
                        } else {
                            colorTiles(tiles, R.color.gray, R.color.tile2, R.color.gray, R.color.gray);
                            appChoice = 5;
                        }
                        return true;
                    }
                    case(MotionEvent.ACTION_UP):{
                        colorTiles(tiles, R.color.gray, R.color.gray, R.color.gray, R.color.gray);
                        resetIcons(tiles);

                        if(appChoice < 0) return true;

                        if(editing
                                || !(favoritePackagesArray[appChoice] instanceof String)
                                || favoritePackagesArray[appChoice].equals("null")) {
                            editing = true;
                            mStartForResult.launch(null);
                        }
                        else launch(favoritePackagesArray[appChoice]);
                    }
                }
                return true;
            }
        });

        //00
        //X0
        tiles[2].setOnTouchListener(new View.OnTouchListener() {
            final float[] refs = new float[2];
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch(motionEvent.getAction()) {
                    case(MotionEvent.ACTION_DOWN):{

                        drawIcons(tiles, 2);

                        refs[0] = motionEvent.getX();
                        refs[1] = motionEvent.getY();

                        colorTiles(tiles, R.color.gray, R.color.gray, R.color.tile3, R.color.gray);
                        appChoice = 10;
                        return true;
                    }
                    case(MotionEvent.ACTION_MOVE):{

                        float deltaX = motionEvent.getX() - refs[0];
                        float deltaY = motionEvent.getY() - refs[1];

                        if (motionEvent.getX() < 0 || motionEvent.getY() > 560){
                            colorTiles(tiles, R.color.gray, R.color.gray, R.color.gray, R.color.gray);
                            appChoice = -1;
                        }else if (deltaX > threshHold / 1.4 && deltaY < threshHold / -1.4) {
                            colorTiles(tiles, R.color.gray, R.color.tile3, R.color.gray, R.color.gray);
                            appChoice = 9;
                        } else if (deltaX > threshHold) {
                            colorTiles(tiles, R.color.gray, R.color.gray, R.color.gray, R.color.tile3);
                            appChoice = 11;
                        } else if (deltaY < -1 * threshHold) {
                            colorTiles(tiles, R.color.tile3, R.color.gray, R.color.gray, R.color.gray);
                            appChoice = 8;
                        } else {
                            colorTiles(tiles, R.color.gray, R.color.gray, R.color.tile3, R.color.gray);
                            appChoice = 10;
                        }
                        return true;
                    }
                    case(MotionEvent.ACTION_UP):{
                        colorTiles(tiles, R.color.gray, R.color.gray, R.color.gray, R.color.gray);
                        resetIcons(tiles);

                        if(appChoice < 0) return true;

                        if(editing
                                || !(favoritePackagesArray[appChoice] instanceof String)
                                || favoritePackagesArray[appChoice].equals("null")) {
                            editing = true;
                            mStartForResult.launch(null);
                        }
                        else launch(favoritePackagesArray[appChoice]);
                    }

                }
                return true;
            }
        });



        //00
        //0X
        tiles[3].setOnTouchListener(new View.OnTouchListener() {
            final float[] refs = new float[2];
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch(motionEvent.getAction()) {
                    case(MotionEvent.ACTION_DOWN):{
                        drawIcons(tiles, 3);

                        refs[0] = motionEvent.getX();
                        refs[1] = motionEvent.getY();

                        colorTiles(tiles, R.color.gray, R.color.gray, R.color.gray, R.color.tile4);
                        appChoice = 15;
                        return true;
                    }
                    case(MotionEvent.ACTION_MOVE):{
                        float deltaX = motionEvent.getX() - refs[0];
                        float deltaY = motionEvent.getY() - refs[1];
                        if (motionEvent.getX() > 400 || motionEvent.getY() > 550){
                            colorTiles(tiles, R.color.gray, R.color.gray, R.color.gray, R.color.gray);
                            appChoice = -1;
                        }else if (deltaX < threshHold / -1.4 && deltaY < threshHold / -1.4) {
                            colorTiles(tiles, R.color.tile4, R.color.gray, R.color.gray, R.color.gray);
                            appChoice = 12;
                        } else if (deltaX < -1 * threshHold) {
                            colorTiles(tiles, R.color.gray, R.color.gray, R.color.tile4, R.color.gray);
                            appChoice = 14;
                        } else if (deltaY < -1 * threshHold) {
                            colorTiles(tiles, R.color.gray, R.color.tile4, R.color.gray, R.color.gray);
                            appChoice = 13;
                        } else {
                            colorTiles(tiles, R.color.gray, R.color.gray, R.color.gray, R.color.tile4);
                            appChoice = 15;
                        }
                    return true;
                    }
                    case(MotionEvent.ACTION_UP):{
                        colorTiles(tiles, R.color.gray, R.color.gray, R.color.gray, R.color.gray);
                        resetIcons(tiles);

                        if(appChoice < 0) return true;

                        if(editing
                            || !(favoritePackagesArray[appChoice] instanceof String)
                                || favoritePackagesArray[appChoice].equals("null")) {
                            editing = true;
                        mStartForResult.launch(null);
                        }
                        else launch(favoritePackagesArray[appChoice]);
                    }

                }
                return true;
            }
        });
    }
    @Override
    protected void onStart(){
        System.out.println("ONSTSART CALLED !!!!!!!!!!!!!!!!!!!!!!!");
        super.onStart();
        //blur.startBlur();
    }
    @Override
    protected void onStop(){
        //blur.pauseBlur();
        super.onStop();
    }

    public void colorTiles(ConstraintLayout[] tiles, int tlColor, int trColor, int blColor, int brColor){
        tiles[0].setBackgroundColor(getResources().getColor(tlColor));
        tiles[1].setBackgroundColor(getResources().getColor(trColor));
        tiles[2].setBackgroundColor(getResources().getColor(blColor));
        tiles[3].setBackgroundColor(getResources().getColor(brColor));
    }

    public void launch(String packageName){
        Intent launchIntent = getPackageManager().getLaunchIntentForPackage(packageName);
        startActivity(launchIntent);
    }

    public void onMenuClick(View view) {
        editing = false;
        mStartForResult.launch(null);

    }

    public void onEditClick(View view){
        editing = !editing;
        Button button = view.findViewById(R.id.editButton);
        button.setText(editing?"editing" : "edit");
        resetIcons(new ConstraintLayout[]{findViewById(R.id.tl_box),findViewById(R.id.tr_box),findViewById(R.id.bl_box),findViewById(R.id.br_box)});
    }

    public appInfo[] infoFromPackageList(String[] packageNames){
        System.out.println("============== creating app info objects from package list ===================");
        appInfo[] finalList = new appInfo[16];
        PackageManager pm = getApplicationContext().getPackageManager();

        for(int i = 0; i < packageNames.length; i++){
            if((packageNames[i] instanceof String) && !(packageNames[i]).equals("null")){

                appInfo app = new appInfo();
                app.packageName = packageNames[i];

                ActivityInfo ai = pm.getLaunchIntentForPackage(packageNames[i]).resolveActivityInfo(pm,0);
                app.label = (String) ai.loadLabel(pm);
                app.icon = ai.loadIcon(pm);

                System.out.println(" -- resources loaded");
                finalList[i] = app;
            }
            else System.out.println(i + "is null, skipped");
        }
        return finalList;
    }

    public void drawIcons(ConstraintLayout[] tiles,int tileNum){
        System.out.println("labeling tileset: " + tileNum);
        int fav = tileNum * 4;
        for(ConstraintLayout tile : tiles){
            System.out.println("drawing tile" + fav);

            TextView t = (TextView) tile.getChildAt(1);
            if (favInfo[fav] instanceof appInfo){
                ImageView i =  (ImageView) tile.getChildAt(0);
                i.setImageDrawable(favInfo[fav].icon);
                //System.out.println("drawable set");
                t.setText(favInfo[fav].label);
            }
            else t.setText("not set"); //favInfo[fav].label
            fav ++;
        }
    }

    public void resetIcons(ConstraintLayout[] tiles) {
        int[] categories = {R.string.Category1, R.string.Category2, R.string.Category3, R.string.Category4};
        int[] icons = {R.drawable.ic_message_solid,R.drawable.ic_compass_solid,R.drawable.ic_ethernet_solid,R.drawable.ic_graduation_cap_solid};

        for (int j = 0; j < 4; j++) {
            TextView t = (TextView) tiles[j].getChildAt(1);
            ImageView i = (ImageView) tiles[j].getChildAt(0);
            i.setImageDrawable(getDrawable(icons[j]));
            t.setText(getText(categories[j]));
        }
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

                    if (editing){
                        //update favorites array
                        favoritePackagesArray[appChoice] = resultPackage;

                        //convert array to string
                        String raw = Arrays.toString(favoritePackagesArray);
                        raw = raw.substring(1,raw.length()-1);

                        //add add the new app to the resources list
                        PackageManager pm = getApplicationContext().getPackageManager();
                        ActivityInfo ai = pm.getLaunchIntentForPackage(resultPackage).resolveActivityInfo(pm,0);

                        appInfo newAppInfo = new appInfo();
                        newAppInfo.packageName = resultPackage;
                        newAppInfo.label = (String) ai.loadLabel(pm);
                        newAppInfo.icon = ai.loadIcon(pm);


                        //update the resource list
                        favInfo[appChoice] = newAppInfo;

                        //save Preferences
                        SharedPreferences.Editor editor = sharedPref.edit();
                        editor.putString("packageList", raw);
                        editor.apply();
                        onEditClick(findViewById(R.id.editButton));

                    }
                    else if (resultPackage!= null)
                        launch(resultPackage);
                }

            });

    public void onViewClick(View view){
        Toast.makeText(this, "I WORK",Toast.LENGTH_SHORT).show();
    }
}
