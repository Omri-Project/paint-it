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
public class Page2 extends Fragment {
    GridView gridView;
    MediaPlayer mediaPlayer;
    public Drawing[] drawings = new Drawing[10];
    private SharedPreferences preferences;
    private boolean soundEnabled;
    HelperDB helperDB;
    private long userId;

    public Page2() {
        // Required empty public constructor
    }


    public static Page2 newInstance(String param1, String param2) {
        Page2 fragment = new Page2();
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
        preferences = getActivity().getSharedPreferences("maPrefs", Context.MODE_PRIVATE);
        soundEnabled = preferences.getBoolean("SoundEffects", true);
        userId = preferences.getLong("connectedId", 0);

        for (int i = 0; i < drawings.length; i++) {
            drawings[i] = new Drawing((i + 1), "Cat", null, null);
        }
        boolean[] showImages = new boolean[drawings.length];
        for (int i = 0; i < drawings.length; i++) {
            showImages[i] = helperDB.isPaintingStarted(drawings[i].getId(), userId);
        }

        View rootView = inflater.inflate(R.layout.fragment_page1, container, false);
        gridView = rootView.findViewById(R.id.gvnew);
        gridView.setAdapter(new GridViewAdapter(getActivity(), drawings, showImages));
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                    Intent intent1 = new Intent(getActivity(), GameActivity.class);
                    playAudio();
                    intent1.putExtra("id", drawings[position].getId());
                    startActivity(intent1);
                    getActivity().finish();
            }
        });

        return rootView;
    }


    private void playAudio() {
        if (soundEnabled) {
            mediaPlayer = MediaPlayer.create(getActivity(), R.raw.button_click);
        }if (mediaPlayer != null) {
            mediaPlayer.start();
        }
    }

}