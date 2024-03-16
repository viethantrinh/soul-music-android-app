package net.branium.view.fragments.main;

import static net.branium.view.activities.MainActivity.getViewPagerMain;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import net.branium.R;
import net.branium.databinding.FragmentHomeBinding;
import net.branium.model.Album;
import net.branium.model.Playlist;
import net.branium.model.Song;
import net.branium.utils.Constants;
import net.branium.view.adapters.HomeAlbumAdapter;
import net.branium.view.adapters.HomeLovePlaylistAdapter;
import net.branium.view.adapters.HomeMusicAdapter;
import net.branium.viewmodel.HomeFragmentViewModel;

import java.util.List;

public class HomeFragment extends Fragment {
    private HomeFragmentViewModel homeFragmentViewModel;
    private HomeMusicAdapter homeMusicAdapter;
    private HomeAlbumAdapter homeAlbumAdapter;
    private HomeLovePlaylistAdapter homeLovePlaylistAdapter;
    private FragmentHomeBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false);
        homeFragmentViewModel = new ViewModelProvider(this).get(HomeFragmentViewModel.class);
        handleAlbumSlideConflict();
        getSongList();
        getAlbumList();
        getUserLovePlaylist();
        return binding.getRoot();
    }

    private void getUserLovePlaylist() {
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        String currentUserId = firebaseUser.getUid();
        homeFragmentViewModel.getAllUserPlaylist(currentUserId).observe(requireActivity(), new Observer<List<Playlist>>() {
            @Override
            public void onChanged(List<Playlist> playlists) {
                homeLovePlaylistAdapter = new HomeLovePlaylistAdapter(Constants.USER_PLAYLIST_LIST, requireContext());
                binding.homeRecycleViewUserLovePlaylist.setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false));
                binding.homeRecycleViewUserLovePlaylist.setAdapter(homeLovePlaylistAdapter);
                homeLovePlaylistAdapter.notifyDataSetChanged();
            }
        });
    }

    private void getAlbumList() {
        homeFragmentViewModel.getAllAlbums().observe(requireActivity(), new Observer<List<Album>>() {
            @Override
            public void onChanged(List<Album> albumList) {
                homeAlbumAdapter = new HomeAlbumAdapter(Constants.ALBUM_LIST, requireContext());
                binding.homeRecycleViewListAlbum.setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false));
                binding.homeRecycleViewListAlbum.setAdapter(homeAlbumAdapter);
                homeAlbumAdapter.notifyDataSetChanged();
            }
        });
    }

    private void getSongList() {
        homeFragmentViewModel.getAllSongs().observe(requireActivity(), new Observer<List<Song>>() {
            @Override
            public void onChanged(List<Song> songList) {
                homeMusicAdapter = new HomeMusicAdapter(Constants.SONG_LIST, requireContext());
                binding.homeRecycleViewListSong.setLayoutManager(new GridLayoutManager(requireContext(), 2, LinearLayoutManager.VERTICAL, false));
                binding.homeRecycleViewListSong.setAdapter(homeMusicAdapter);
                homeMusicAdapter.notifyDataSetChanged();
            }
        });
    }

    private void handleAlbumSlideConflict() {
        binding.homeRecycleViewListAlbum.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
            int lastX = 0;

            @Override
            public boolean onInterceptTouchEvent(@NonNull RecyclerView rv, @NonNull MotionEvent e) {
                switch (e.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        lastX = (int) e.getX();
                        break;
                    case MotionEvent.ACTION_MOVE:
                        boolean isScrollingRight = e.getX() < lastX;
                        if ((isScrollingRight && ((LinearLayoutManager) binding.homeRecycleViewListAlbum.getLayoutManager()).findLastCompletelyVisibleItemPosition() == binding.homeRecycleViewListAlbum.getAdapter().getItemCount() - 1) || (!isScrollingRight && ((LinearLayoutManager) binding.homeRecycleViewListAlbum.getLayoutManager()).findFirstCompletelyVisibleItemPosition() == 0)) {
                            getViewPagerMain().setUserInputEnabled(true);
                        } else {
                            getViewPagerMain().setUserInputEnabled(false);
                        }
                        break;
                    case MotionEvent.ACTION_UP:
                        lastX = 0;
                        getViewPagerMain().setUserInputEnabled(true);
                        break;
                }
                return false;
            }

            @Override
            public void onTouchEvent(@NonNull RecyclerView rv, @NonNull MotionEvent e) {
            }

            @Override
            public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

            }
        });
    }
}