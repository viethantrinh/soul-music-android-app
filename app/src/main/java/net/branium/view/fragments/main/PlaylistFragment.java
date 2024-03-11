package net.branium.view.fragments.main;


import static android.content.Context.MODE_PRIVATE;
import static net.branium.view.activities.SplashActivity.musicFiles;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import net.branium.R;
import net.branium.model.Song;
import net.branium.view.activities.SplashActivity;
import net.branium.view.adapters.MusicAdapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class PlaylistFragment extends Fragment implements SearchView.OnQueryTextListener {
    RecyclerView recyclerView;
    MusicAdapter musicAdapter;
    List<Song> songLists = new ArrayList<>();
    public static String MY_SORT_PREF = "SortOrder";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_playlist, container, false);

        // Thiết lập Toolbar
        Toolbar toolbar = view.findViewById(R.id.toolbar);
        AppCompatActivity activity = (AppCompatActivity) requireActivity();
        activity.setSupportActionBar(toolbar);

        // Báo cho Fragment biết rằng nó sẽ có menu của riêng mình
        setHasOptionsMenu(true);

        recyclerView = view.findViewById(R.id.recycler_view_playlist);
        recyclerView.setHasFixedSize(true);

        // Đăng ký RecyclerView cho ContextMenu
        registerForContextMenu(recyclerView);


        songLists = SplashActivity.musicFiles;

        if (!(musicFiles.size() < 1)) {
            musicAdapter = new MusicAdapter(getContext(), musicFiles);
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
            recyclerView.setAdapter(musicAdapter);
        }
        return view;
    }
    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.search, menu);
        MenuItem menuItem = menu.findItem(R.id.search_option);
        SearchView searchView = (SearchView) menuItem.getActionView();
        searchView.setOnQueryTextListener(this);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        String userInput = newText.toLowerCase();
        ArrayList<Song> songs = new ArrayList<>();
        for (Song song : songLists) {
            if(song.getTitle().toLowerCase().contains(userInput)) {
                songs.add(song);
            }
        }
        musicAdapter.updateList(songs);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.by_name_list) {
            songLists.sort(Comparator.comparing(Song::getTitle));
            musicAdapter.notifyDataSetChanged();
        } else if (item.getItemId() == R.id.by_artist_list) {
            songLists.sort(Comparator.comparing(Song::getArtist));
            musicAdapter.notifyDataSetChanged();
        }else if (item.getItemId() == R.id.by_duration_list) {
            songLists.sort(Comparator.comparing(Song::getDuration));
            musicAdapter.notifyDataSetChanged();
        }
        return true;
    }
}