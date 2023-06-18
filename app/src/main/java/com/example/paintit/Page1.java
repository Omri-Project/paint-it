package com.example.paintit;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Page1#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Page1 extends Fragment {
    GridView gridView;
    MediaPlayer mediaPlayer;
    public Drawing[] drawings = new Drawing[8];
    private SharedPreferences preferences;
    private boolean soundEnabled;
    HelperDB helperDB;

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
        helperDB = new HelperDB(getContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        for (int i = 0; i < drawings.length; i++) {
            drawings[i] = new Drawing((i + 1), "Cat", null, null);
        }

        View rootView = inflater.inflate(R.layout.fragment_page1, container, false);
        gridView = rootView.findViewById(R.id.gvnew);

        boolean[] showImages = new boolean[drawings.length];
        for (int i = 0; i < drawings.length; i++) {
            showImages[i] = true; // Always show images in Page1
        }

        gridView.setAdapter(new GridViewAdapter(getActivity(), drawings, showImages));
        preferences = getActivity().getSharedPreferences("maPrefs", Context.MODE_PRIVATE);
        soundEnabled = preferences.getBoolean("SoundEffects", true);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                Intent intent1 = new Intent(getActivity(), GameActivity.class);
                playAudio();
                intent1.putExtra("id", (position + 1));
                startActivity(intent1);
                getActivity().finish();
            }
        });

        return rootView;
    }



    private void playAudio() {
        if (soundEnabled) {
            mediaPlayer = MediaPlayer.create(getActivity(), R.raw.button_click);
        }
        if (mediaPlayer != null) {
            mediaPlayer.start();
        }
    }
}
