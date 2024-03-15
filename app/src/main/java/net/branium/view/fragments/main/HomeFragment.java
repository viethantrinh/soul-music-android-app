package net.branium.view.fragments.main;

import static net.branium.view.activities.MainActivity.viewPagerMain;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import net.branium.R;
import net.branium.databinding.FragmentHomeBinding;
import net.branium.model.Album;
import net.branium.model.Song;
import net.branium.utils.Constants;
import net.branium.view.adapters.HomeAlbumAdapter;
import net.branium.view.adapters.HomeMusicAdapter;
import net.branium.viewmodel.HomeFragmentViewModel;

import java.util.List;

public class HomeFragment extends Fragment {
    private HomeFragmentViewModel homeFragmentViewModel;
    private HomeMusicAdapter homeMusicAdapter;
    private HomeAlbumAdapter homeAlbumAdapter;
    private FragmentHomeBinding fragmentHomeBinding;
    private RecyclerView homeRecycleViewListSong;
    private RecyclerView homeRecycleViewListAlbum;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        fragmentHomeBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false);
        homeFragmentViewModel = new ViewModelProvider(this).get(HomeFragmentViewModel.class);

        if (!Constants.homeSongList.isEmpty()) {
            homeRecycleViewListSong = fragmentHomeBinding.homeRecycleViewListSong;
            homeMusicAdapter = new HomeMusicAdapter(Constants.homeSongList, requireContext());
            homeRecycleViewListSong.setLayoutManager(new GridLayoutManager(requireContext(), 2, RecyclerView.VERTICAL, false));
            homeRecycleViewListSong.setAdapter(homeMusicAdapter);
        } else {
            getSongList();
            getAlbumList();
        }
        return fragmentHomeBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        fragmentHomeBinding.homeRecycleViewListAlbum.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
            int lastX = 0;
            @Override
            public boolean onInterceptTouchEvent(@NonNull RecyclerView rv, @NonNull MotionEvent e) {
                switch (e.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        lastX = (int) e.getX();
                        break;
                    case MotionEvent.ACTION_MOVE:
                        boolean isScrollingRight = e.getX() < lastX;
                        if ((isScrollingRight && ((LinearLayoutManager) homeRecycleViewListAlbum.getLayoutManager()).findLastCompletelyVisibleItemPosition() == homeRecycleViewListAlbum.getAdapter().getItemCount() - 1) ||
                                (!isScrollingRight && ((LinearLayoutManager) homeRecycleViewListAlbum.getLayoutManager()).findFirstCompletelyVisibleItemPosition() == 0)) {
                            viewPagerMain.setUserInputEnabled(true);
                        } else {
                            viewPagerMain.setUserInputEnabled(false);
                        }
                        break;
                    case MotionEvent.ACTION_UP:
                        lastX = 0;
                        viewPagerMain.setUserInputEnabled(true);
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

    private void getAlbumList() {
        homeFragmentViewModel.getAllAlbums().observe(requireActivity(), new Observer<List<Album>>() {
            @Override
            public void onChanged(List<Album> albumListFromLiveData) {
                Constants.albumList.addAll(albumListFromLiveData);
                homeRecycleViewListAlbum = fragmentHomeBinding.homeRecycleViewListAlbum;
                homeAlbumAdapter = new HomeAlbumAdapter(Constants.albumList, requireContext());
                homeRecycleViewListAlbum.setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false));
                homeRecycleViewListAlbum.setAdapter(homeAlbumAdapter);
            }
        });
    }

    private void getSongList() {
        homeFragmentViewModel.getAllSongs().observe(requireActivity(), new Observer<List<Song>>() {
            @Override
            public void onChanged(List<Song> songListFromLiveData) {
                Constants.songList.addAll(songListFromLiveData);
                Constants.homeSongList.addAll(Constants.songList.subList(0, 8));
                homeRecycleViewListSong = fragmentHomeBinding.homeRecycleViewListSong;
                homeMusicAdapter = new HomeMusicAdapter(Constants.homeSongList, requireContext());
                homeRecycleViewListSong.setLayoutManager(new GridLayoutManager(requireContext(), 2, RecyclerView.VERTICAL, false));
                homeRecycleViewListSong.setAdapter(homeMusicAdapter);
            }
        });
    }
}