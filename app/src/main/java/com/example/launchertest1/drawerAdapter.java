package com.example.launchertest1;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class drawerAdapter extends RecyclerView.Adapter<drawerAdapter.ViewHolder> {

    private appInfo[] localData;

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
            System.out.println("item clicked! : " + localData[pos].packageName);
        }
    }

    public drawerAdapter(appInfo[] data){
        localData = data;
    }


    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.app_icon, parent, false);
        return new ViewHolder(view);
    }

    public void onBindViewHolder(ViewHolder holder, int pos) {
        ImageView imageView = holder.getIcon();
        imageView.setImageDrawable(localData[pos].icon);
    }

    public int getItemCount() {
        return localData.length;
    }


}
