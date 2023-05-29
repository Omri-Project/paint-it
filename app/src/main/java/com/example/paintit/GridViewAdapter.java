package com.example.paintit;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class GridViewAdapter extends BaseAdapter {
    private Context context;
    private Drawing[] drawings;
    LayoutInflater inflater;
    int[] drawables = {R.drawable.duck, R.drawable.canvas_pixle_art, R.drawable.mushroom, R.drawable.frog, R.drawable.nyancat};


    public GridViewAdapter (Context context, Drawing[] drawings){
        this.context = context;
        this.drawings = drawings;
        inflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }
    @Override
    public int getCount() {
        return drawings.length;
    }
    @Override
    public Object getItem(int i) {
        return drawings[i];
    }
    @Override
    public long getItemId(int i) {
        return i;
    }
    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if (view == null){
            view = inflater.inflate(R.layout.single_drawing_layout, null);
        }
        ImageView img = view.findViewById(R.id.imageView2);
        img.setImageResource(drawables[i]);

        return view;
    }
}
