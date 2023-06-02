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
    private boolean[] showImages;
    private LayoutInflater inflater;
    private int[] drawables = {R.drawable.duck, R.drawable.canvas_pixle_art, R.drawable.mushroom, R.drawable.frog, R.drawable.nyancat};

    public GridViewAdapter(Context context, Drawing[] drawings, boolean[] showImages) {
        this.context = context;
        this.drawings = drawings;
        this.showImages = showImages;
        inflater = LayoutInflater.from(context);
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
        if (view == null) {
            view = inflater.inflate(R.layout.single_drawing_layout, viewGroup, false);
        }

        ImageView img = view.findViewById(R.id.imageView2);
        if (showImages[i]) {
            img.setImageResource(drawables[i]);
            img.setVisibility(View.VISIBLE);
        } else {
            img.setVisibility(View.GONE);
        }

        return view;
    }
}



