package net.branium.view.love;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import net.branium.R;
import net.branium.databinding.FragmentLoveBinding;
import net.branium.model.Playlist;
import net.branium.utils.Constants;
import net.branium.view.musicplayer.MusicActivity;

import java.util.List;

public class LoveFragment extends Fragment {
    private FragmentLoveBinding binding;
    private LoveViewModel loveViewModel;
    private LovePlaylistAdapter lovePlaylistAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container
            , Bundle savedInstanceState) {

        binding = DataBindingUtil.inflate(
                inflater,
                R.layout.fragment_love,
                container,
                false
        );
        loveViewModel = new ViewModelProvider(this).get(LoveViewModel.class);
        getUserLovePlaylist();

        handleEventListener();

        return binding.getRoot();
    }

    private void handleEventListener() {
        binding.ivBtnAddLovePlaylist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), LoveCreateActivity.class);
                intent.putExtra("flag_check_album", false);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        getUserLovePlaylist();
    }

    private void getUserLovePlaylist() {
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        String currentUserId = firebaseUser.getUid();
        loveViewModel.getAllUserPlaylist(currentUserId).observe(requireActivity(), new Observer<List<Playlist>>() {
            @Override
            public void onChanged(List<Playlist> playlists) {
                lovePlaylistAdapter = new LovePlaylistAdapter(Constants.USER_PLAYLIST_LIST, requireContext());
                binding.loveRecycleViewUserLovePlaylist.setLayoutManager(new GridLayoutManager(requireContext(), 1));
                binding.loveRecycleViewUserLovePlaylist.setAdapter(lovePlaylistAdapter);
                lovePlaylistAdapter.notifyDataSetChanged();
            }
        });
    }
}