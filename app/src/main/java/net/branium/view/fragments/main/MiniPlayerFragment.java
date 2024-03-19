package net.branium.view.fragments.main;

import static net.branium.view.activities.MusicActivity.mediaPlayer;

import android.content.Context;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;

import net.branium.R;
import net.branium.databinding.FragmentMiniPlayerBinding;
import net.branium.utils.Constants;
import net.branium.view.services.MusicAction;

import java.util.Random;

public class MiniPlayerFragment extends Fragment {
    private FragmentMiniPlayerBinding binding;
    MusicAction musicAction;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_mini_player, container, false);



        binding.fabPlayPauseMiniPlayer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mediaPlayer.isPlaying()) {
                    mediaPlayer.pause();
                    binding.fabPlayPauseMiniPlayer.setImageResource(R.drawable.ic_play_24);
                } else {
                    mediaPlayer.start();
                    binding.fabPlayPauseMiniPlayer.setImageResource(R.drawable.ic_pause_24);
                }
            }
        });
        binding.ivMiniSkipNextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                MusicPlayerActivity.mediaPlayer.stop();
//                MusicPlayerActivity.mediaPlayer.release();
//                if (!isShuffle && !isRepeat) {
//                    position = ((position + 1) % Constants.PLAYLIST_SONG_LIST.size());
//                } else if (isShuffle && !isRepeat) {
//                    position = new Random().nextInt(Constants.PLAYLIST_SONG_LIST.size());
//                } // (!isShuffle && isRepeat) or (isShuffle && isRepeat) => position = currentPosition
//                setUpSongData(Constants.PLAYLIST_SONG_LIST.get(position));
//                mediaPlayer = MediaPlayer.create(getApplicationContext(), uri);
//                binding.fltBtnMusicPlayerPlayPause.setImageResource(R.drawable.ic_pause_24);
//                mediaPlayer.setOnCompletionListener(mp -> onClickSkipNextBtn());
//                mediaPlayer.start();
                if(musicAction != null) {
                    musicAction.skipNext();
                }
                Toast.makeText(getContext(), "Hahaha", Toast.LENGTH_SHORT).show();
//                mediaPlayer.stop();
//                mediaPlayer.release();
//                if (!isShuffle && !isRepeat) {
//                    position = ((position + 1) % Constants.PLAYLIST_SONG_LIST.size());
//                } else if (isShuffle && !isRepeat) {
//                    position = new Random().nextInt(Constants.PLAYLIST_SONG_LIST.size());
//                } // (!isShuffle && isRepeat) or (isShuffle && isRepeat) => position = currentPosition
//                setUpSongData(Constants.PLAYLIST_SONG_LIST.get(position));
//                mediaPlayer = MediaPlayer.create(getContext(), uri);
//
//                showNotification(R.drawable.ic_pause_24);
//                binding.fabPlayPauseMiniPlayer.setImageResource(R.drawable.ic_pause_24);
//                mediaPlayer.setOnCompletionListener(mp -> skipNext());
//                mediaPlayer.start();
            }
        });

        return binding.getRoot();
    }

    @Override
    public void onResume() {
        super.onResume();
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(Constants.SHARE_PREFERENCE_KEY, Context.MODE_PRIVATE);
        String title = sharedPreferences.getString(Constants.MUSIC_TITLE, null);
        String artist = sharedPreferences.getString(Constants.MUSIC_ARTIST, null);
        String image = sharedPreferences.getString(Constants.MUSIC_IMAGE, null);
        String source = sharedPreferences.getString(Constants.MUSIC_SOURCE, null);
        if (title != null && artist != null && image != null && source != null) {
            binding.tvSongNameMiniPlayer.setText(title);
            binding.tvSongArtistMiniPlayer.setText(artist);
            Glide.with(binding.ivMiniAlbumArt).load(image).into(binding.ivMiniAlbumArt);
            binding.fabPlayPauseMiniPlayer.setImageResource(R.drawable.ic_pause_24);
        }
    }

    public void setMusicAction(MusicAction musicAction) {
        this.musicAction = musicAction;
    }
}