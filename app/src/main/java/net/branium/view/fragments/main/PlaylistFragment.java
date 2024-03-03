package net.branium.view.fragments.main;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import net.branium.R;
import net.branium.model.Song;
import net.branium.view.activities.SplashActivity;
import net.branium.view.adapters.MusicAdapter;

import java.util.ArrayList;
import java.util.List;

public class PlaylistFragment extends Fragment {
    RecyclerView recyclerView;
    MusicAdapter musicAdapter;
    List<Song> songList = new ArrayList<>();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_playlist, container, false);

        recyclerView = view.findViewById(R.id.recycler_view_playlist);
        recyclerView.setHasFixedSize(true);

        songList = SplashActivity.musicFiles;

        if (!(songList.isEmpty())) {
            musicAdapter = new MusicAdapter(getContext(), songList);
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
            recyclerView.setAdapter(musicAdapter);
        }
        return view;
    }
}