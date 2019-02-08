package com.example.coni;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class RecyclerAdapter extends RecyclerView.Adapter <RecyclerAdapter.MyViewHolder> {

    private LayoutInflater mLayoutInflater;
    private ArrayList<ChildArray> c_array = new ArrayList<>();
    private Context context;

    public RecyclerAdapter(ArrayList<ChildArray> carray, Context context) {

        this.c_array = carray;
        this.context = context;
    }

    public ArrayList<ChildArray> getChildArray() {
        return this.c_array;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        mLayoutInflater = LayoutInflater.from(context);
        View view = mLayoutInflater.inflate(R.layout.activity_child_list, parent, false);
        MyViewHolder mHolder = new MyViewHolder(view);

        return mHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        //Getting Photo

        byte[] photo = c_array.get(position).getChild_photo();

        //Convert Photo Bytes to Usable Image

        Bitmap decodedPhoto = BitmapFactory.decodeByteArray(photo, 0, photo.length);

        holder.cphoto.setImageBitmap(decodedPhoto);
        holder.cname.setText(c_array.get(position).getChild_fname());
        holder.selectedCArray = c_array.get(position);
        Glide.with(context).load(c_array.get(position).getChild_photo()).into(holder.cphoto);

        //Get location and photo

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView cname;
        TextView cloc;
        TextView cbatt;
        ImageView cphoto;

        ChildArray selectedCArray;

        public MyViewHolder(View itemview) {
            super(itemview);

            this.cname = itemView.findViewById(R.id.txtChildName);
            this.cloc = itemView.findViewById(R.id.txtCurrentLoc);
            this.cbatt = itemView.findViewById(R.id.txtBattStatus);
            this.cphoto = itemView.findViewById(R.id.imageChild);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent preview = new Intent(context, ChildList.class);
                    preview.putExtra("KeyName", selectedCArray.getChild_fname());

                    //getlcoation and battery status
//                    preview.putExtra("KeyLoc", selectedCArray.get)

                    preview.putExtra("KeyImg", selectedCArray.getChild_photo());
                    context.startActivity(preview);
                }
            });
        }

        public int getItemCount() {

            return c_array.size();
        }
    }
}