package net.branium.view.fragments.main;

import static android.content.Context.MODE_PRIVATE;
import static net.branium.view.activities.MusicActivity.currentPosition;
import static net.branium.view.activities.MusicActivity.isRepeat;
import static net.branium.view.activities.MusicActivity.isShuffle;
import static net.branium.view.activities.MusicActivity.mediaPlayer;
import static net.branium.view.activities.MusicActivity.position;
import static net.branium.view.activities.MusicActivity.uri;

import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;

import net.branium.R;
import net.branium.databinding.FragmentMiniPlayerBinding;
import net.branium.model.Song;
import net.branium.utils.Constants;
import net.branium.utils.Notification;
import net.branium.view.services.MusicAction;

import java.util.Random;

public class MiniPlayerFragment extends Fragment implements MusicAction {
    private FragmentMiniPlayerBinding binding;

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
                skipNext();
                currentPosition = position;
            }
        });

        return binding.getRoot();
    }
    @Override
    public void onResume() {
        super.onResume();
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(Constants.SHARE_PREFERENCE_KEY, MODE_PRIVATE);
        String title = sharedPreferences.getString(Constants.MUSIC_TITLE, null);
        String artist = sharedPreferences.getString(Constants.MUSIC_ARTIST, null);
        String image = sharedPreferences.getString(Constants.MUSIC_IMAGE, null);
        String source = sharedPreferences.getString(Constants.MUSIC_SOURCE, null);
        if (title != null && artist != null && image != null && source != null) {
            binding.tvSongNameMiniPlayer.setText(title);
            binding.tvSongArtistMiniPlayer.setText(artist);
            Glide.with(binding.ivMiniAlbumArt).load(image).into(binding.ivMiniAlbumArt);

            if(mediaPlayer != null) {
                if(mediaPlayer.isPlaying()) {
                    binding.fabPlayPauseMiniPlayer.setImageResource(R.drawable.ic_pause_24);
                }else{
                    binding.fabPlayPauseMiniPlayer.setImageResource(R.drawable.ic_play_24);
                }
            }
        }
        if(mediaPlayer != null) {
            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    skipNext();
                    currentPosition = position;
                }
            });
        }
    }

    @Override
    public void playPause() {

    }

    @Override
    public void skipNext() {
        mediaPlayer.stop();
        mediaPlayer.release();
        if (!isShuffle && !isRepeat) {
            position = ((position + 1) % Constants.PLAYLIST_SONG_LIST.size());
        } else if (isShuffle && !isRepeat) {
            position = new Random().nextInt(Constants.PLAYLIST_SONG_LIST.size());
        } // (!isShuffle && isRepeat) or (isShuffle && isRepeat) => position = currentPosition
        setUpSongData(Constants.PLAYLIST_SONG_LIST.get(position));
        updateUI(Constants.PLAYLIST_SONG_LIST.get(position));
        mediaPlayer = MediaPlayer.create(getContext(), uri);

        Notification.showNotification(getContext(), R.drawable.ic_pause_24);

        mediaPlayer.setOnCompletionListener(mp -> skipNext());
        mediaPlayer.start();
    }

    @Override
    public void skipPrevious() {

    }
    private void setUpSongData(Song currentSong) {
        uri = Uri.parse(currentSong.getSource());

        Constants.MINI_PLAYER_ACTIVE = true;
        SharedPreferences.Editor editor = getContext().getSharedPreferences(Constants.SHARE_PREFERENCE_KEY, MODE_PRIVATE).edit();
        editor.putString(Constants.MUSIC_TITLE, currentSong.getTitle());
        editor.putString(Constants.MUSIC_ARTIST, currentSong.getArtist());
        editor.putString(Constants.MUSIC_IMAGE, currentSong.getImage());
        editor.putString(Constants.MUSIC_SOURCE, uri.toString());
        editor.apply();
    }

    private void updateUI(Song currentSong) {
        binding.tvSongNameMiniPlayer.setText(currentSong.getTitle());
        binding.tvSongArtistMiniPlayer.setText(currentSong.getArtist());
        Glide.with(binding.ivMiniAlbumArt).load(currentSong.getImage()).into(binding.ivMiniAlbumArt);
        binding.fabPlayPauseMiniPlayer.setImageResource(R.drawable.ic_pause_24);
    }
}