package net.branium.view.playlist;

import android.graphics.Color;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import net.branium.R;
import net.branium.databinding.FragmentPlaylistBinding;
import net.branium.model.Song;
import net.branium.utils.Constants;
import net.branium.view.home.HomeFragmentViewModel;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class PlaylistFragment extends Fragment implements SearchView.OnQueryTextListener {
    private HomeFragmentViewModel homeFragmentViewModel;
    private PlaylistMusicAdapter playlistMusicAdapter;
    private FragmentPlaylistBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_playlist, container, false);
        // Config toolbar
        AppCompatActivity activity = (AppCompatActivity) requireActivity();
        activity.setSupportActionBar(binding.toolbar);
        // Báo cho Fragment biết rằng nó sẽ có menu của riêng mình
        setHasOptionsMenu(true);
        // Đăng ký RecyclerView cho ContextMenu
        registerForContextMenu(binding.recyclerViewPlaylist);
        getSongList();
        return binding.getRoot();
    }


    private void getSongList() {
        homeFragmentViewModel = new ViewModelProvider(this).get(HomeFragmentViewModel.class);
        homeFragmentViewModel.getAllSongs().observe(requireActivity(), new Observer<List<Song>>() {
            @Override
            public void onChanged(List<Song> songListFromLiveData) {
                playlistMusicAdapter = new PlaylistMusicAdapter(requireContext(), Constants.PLAYLIST_SONG_LIST);
                binding.recyclerViewPlaylist.setLayoutManager(new LinearLayoutManager(requireActivity(), LinearLayoutManager.VERTICAL, false));
                binding.recyclerViewPlaylist.setAdapter(playlistMusicAdapter);
                playlistMusicAdapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.search, menu);
        MenuItem menuItemSearch = menu.findItem(R.id.search_option);

        MenuItem sortOptionsItem = menu.findItem(R.id.sort_options);
        sortOptionsItem.setIcon(R.drawable.ic_more_vert_24);
        sortOptionsItem.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);

        SearchView searchView = (SearchView) menuItemSearch.getActionView();

        changeIconSearchView(searchView);

        searchView.setOnQueryTextListener(this);
        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                playlistMusicAdapter.updateList1();
                playlistMusicAdapter.notifyDataSetChanged();
                return false;
            }
        });
        super.onCreateOptionsMenu(menu, inflater);
    }

    private void changeIconSearchView(SearchView searchView) {
        ImageView searchIcon = searchView.findViewById(androidx.appcompat.R.id.search_button);
        // Customize the icon
        if (searchIcon != null) {
            searchIcon.setColorFilter(Color.WHITE); // Set color to white
            searchIcon.setScaleX(1.4f); // Increase size
            searchIcon.setScaleY(1.4f); // Increase size
        }
        searchView.setOnSearchClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText searchEditText = searchView.findViewById(androidx.appcompat.R.id.search_src_text);
                if (searchEditText != null) {
                    searchEditText.setTextColor(Color.WHITE);
                    searchEditText.setTextSize(TypedValue.COMPLEX_UNIT_SP, 22);
                }
                ImageView closeIcon = searchView.findViewById(androidx.appcompat.R.id.search_close_btn);
                if (closeIcon != null) {
                    closeIcon.setColorFilter(Color.WHITE);
                    closeIcon.setScaleX(1.1f);
                    closeIcon.setScaleY(1.1f);
                }
            }
        });
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        String userInput = newText.toLowerCase();
        ArrayList<Song> songs = new ArrayList<>();
        if(!userInput.equals("")) {
            for (Song song : Constants.PLAYLIST_SONG_LIST) {
                if (song.getTitle().toLowerCase().contains(userInput)) {
                    songs.add(song);
                }
            }
        }else {
            songs.addAll(Constants.PLAYLIST_SONG_LIST);
        }
        playlistMusicAdapter.updateList(songs);
        playlistMusicAdapter.notifyDataSetChanged();
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.by_name_list) {
            playlistMusicAdapter.sortByTitle((ArrayList<Song>) Constants.PLAYLIST_SONG_LIST);
            Set<Song> uniqueSongs = new LinkedHashSet<>(Constants.PLAYLIST_SONG_LIST);
            Constants.PLAYLIST_SONG_LIST.clear();
            Constants.PLAYLIST_SONG_LIST.addAll(uniqueSongs);
            playlistMusicAdapter.setSongList(Constants.PLAYLIST_SONG_LIST);
            playlistMusicAdapter.notifyDataSetChanged();
        } else if (item.getItemId() == R.id.by_artist_list) {
            playlistMusicAdapter.sortByArtist((ArrayList<Song>) Constants.PLAYLIST_SONG_LIST);
            Set<Song> uniqueSongs = new LinkedHashSet<>(Constants.PLAYLIST_SONG_LIST);
            Constants.PLAYLIST_SONG_LIST.clear();
            Constants.PLAYLIST_SONG_LIST.addAll(uniqueSongs);
            playlistMusicAdapter.setSongList(Constants.PLAYLIST_SONG_LIST);
            playlistMusicAdapter.notifyDataSetChanged();
        } else if (item.getItemId() == R.id.by_duration_list) {
            playlistMusicAdapter.sortByDuration((ArrayList<Song>) Constants.PLAYLIST_SONG_LIST);
            Set<Song> uniqueSongs = new LinkedHashSet<>(Constants.PLAYLIST_SONG_LIST);
            Constants.PLAYLIST_SONG_LIST.clear();
            Constants.PLAYLIST_SONG_LIST.addAll(uniqueSongs);
            playlistMusicAdapter.setSongList(Constants.PLAYLIST_SONG_LIST);
            playlistMusicAdapter.notifyDataSetChanged();
        }
        return true;
    }

}