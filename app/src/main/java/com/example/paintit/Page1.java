package com.example.paintit;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Page1#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Page1 extends Fragment {
    GridView gridView;
    MediaPlayer mp;
    public Drawing[] drawings = new Drawing[10];


    private GridViewAdapter adapter;
    Intent intent;

    public Page1() {
        // Required empty public constructor
    }


    public static Page1 newInstance(String param1, String param2) {
        Page1 fragment = new Page1();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        for (int i = 0; i < drawings.length; i++){
            drawings[i] = new Drawing("Cat", 1, R.drawable.ic_launcher_background);
        }

        View rootView = inflater.inflate(R.layout.fragment_page1, container, false);
        gridView = rootView.findViewById(R.id.gvnew);
        gridView.setAdapter(new GridViewAdapter(getActivity(), drawings));
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {

                Toast.makeText(getActivity(), "" + (position+1), Toast.LENGTH_SHORT).show();
                Intent intent1 = new Intent(getActivity(), GameActivity.class);
                HelperDB helperDB = new HelperDB(getContext());
                helperDB.addPredefinedPainting();
                intent1.putExtra("id", (position+1));
                startActivity(intent1);
            }
        });

        return rootView;
    }
}