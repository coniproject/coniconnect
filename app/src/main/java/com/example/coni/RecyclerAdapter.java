package com.example.coni;

import android.content.Context;
import android.media.Image;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {

    private ArrayList<Place> places;
    private Context context;

    public RecyclerAdapter(ArrayList<Place> places, Context c) {
        this.places= places;
        this.context = c;
    }


    @NonNull
    @Override
    public RecyclerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_child_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerAdapter.ViewHolder holder, int position) {

        holder.displayName.setText(places.get(position).getCname());
        holder.displaycurrloc.setText(places.get(position).getCurrloc());
        holder.displayPhoto.setImageResource(places.get(position).getPhoto());
    }

    @Override
    public int getItemCount() {
        return places.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView displayName, displaycurrloc;
        ImageView displayPhoto;

        public ViewHolder(View view) {
            super(view);

            displayName = view.findViewById(R.id.txtChildName);
            displaycurrloc = view.findViewById(R.id.txtCurrentLoc);
            displayPhoto = view.findViewById(R.id.imageChild);
        }
    }
}
