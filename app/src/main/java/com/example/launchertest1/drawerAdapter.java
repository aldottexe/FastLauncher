package com.example.launchertest1;

import static android.app.Activity.RESULT_OK;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class drawerAdapter extends RecyclerView.Adapter<drawerAdapter.ViewHolder> {

    private ArrayList<appInfo> localData;
    private Context activityContext;

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private final ImageView icon;
        public ViewHolder(View view){
            super(view);

            icon = (ImageView) view.findViewById(R.id.appImage);
            icon.setOnClickListener(this);
        }

        public ImageView getIcon(){
            return icon;
        }

        //MAKE ME RETURN AN INTENT OF THE APP CHOSEN... or at least the package name idk
        public void onClick(View view) {
            int pos = getAdapterPosition();
            String appClicked = localData.get(pos).packageName;
            System.out.println("item clicked! : " + appClicked);
            ((AppsDrawer)activityContext).setResult(RESULT_OK, new Intent().putExtra("packageName",appClicked));
            ((AppsDrawer)activityContext).finish();
        }
    }

    public drawerAdapter(Context c, ArrayList data){
        activityContext = c;
        localData = data;
    }


    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.app_icon, parent, false);
        return new ViewHolder(view);
    }

    public void onBindViewHolder(ViewHolder holder, int pos) {
        ImageView imageView = holder.getIcon();
        imageView.setImageDrawable(localData.get(pos).icon);
    }

    public int getItemCount() {
        return localData.size();
    }


}
