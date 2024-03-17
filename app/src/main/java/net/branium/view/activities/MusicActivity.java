package net.branium.view.activities;

import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.SeekBar;


import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.bumptech.glide.Glide;

import net.branium.R;
import net.branium.databinding.ActivityMusicPlayerBinding;
import net.branium.model.Song;
import net.branium.utils.Constants;
import net.branium.view.services.MusicAction;

import java.util.Random;

@FunctionalInterface
interface OnThreadClickListener {
    void action();
}

public class MusicActivity extends AppCompatActivity implements MusicAction {
    private static int position = -1;
    private static Uri uri;
    public static MediaPlayer mediaPlayer;
    public static boolean isShuffle = false;
    public static boolean isRepeat = false;
    private ActivityMusicPlayerBinding binding;
    private Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music_player);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_music_player);
        setupMediaPlayer();
        handleEventListener();
    }

    private void handleEventListener() {
        binding.seekBarMusicPlayer.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (mediaPlayer != null && fromUser) {
                    mediaPlayer.seekTo(progress * 1000);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        MusicActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (mediaPlayer != null) {
                    int musicCurrentPosition = mediaPlayer.getCurrentPosition() / 1000;
                    binding.seekBarMusicPlayer.setProgress(musicCurrentPosition);
                    binding.tvMusicPlayerDurationRun.setText(getFormattedDuration(musicCurrentPosition));
                }
                handler.postDelayed(this, 1000);
            }
        });

        mediaPlayer.setOnCompletionListener(mp -> skipNext());

        binding.ivMusicPlayerShuffle.setOnClickListener(v -> {
            if (isShuffle) {
                binding.ivMusicPlayerShuffle.setImageResource(R.drawable.ic_shuffle_off_24);
            } else {
                binding.ivMusicPlayerShuffle.setImageResource(R.drawable.ic_shuffle_on_24);
            }
            isShuffle = !isShuffle;
        });

        binding.ivMusicPlayerRepeat.setOnClickListener(v -> {
            if (isRepeat) {
                binding.ivMusicPlayerRepeat.setImageResource(R.drawable.ic_repeat_off_24);
            } else {
                binding.ivMusicPlayerRepeat.setImageResource(R.drawable.ic_repeat_on_24);
            }
            isRepeat = !isRepeat;
        });

        binding.ivMusicPlayerCloseDown.setOnClickListener(v -> {
//            if (mediaPlayer != null) {
//                mediaPlayer.stop();
//                mediaPlayer.release();
//                mediaPlayer = null;
//            }
            finish();
        });
    }

    private String getFormattedDuration(int musicCurrentPosition) {
        String seconds = String.valueOf(musicCurrentPosition % 60);
        String minutes = String.valueOf(musicCurrentPosition / 60);
        if (seconds.length() == 1) {
            return minutes + ":" + "0" + seconds;
        } else {
            return minutes + ":" + seconds;
        }
    }

    private void setupMediaPlayer() {
        position = getIntent().getIntExtra("position", -1);
        Song currentSong = Constants.PLAYLIST_SONG_LIST.get(position);
        setUpSongData(currentSong);
        binding.fltBtnMusicPlayerPlayPause.setImageResource(R.drawable.ic_pause_24);
        if (mediaPlayer != null) { // trong trường hợp media player tồn tại bài hát -> stop -> release -> chạy lại
            mediaPlayer.stop();
            mediaPlayer.release();
        }
        mediaPlayer = MediaPlayer.create(getApplicationContext(), uri);
        mediaPlayer.start();
    }

    private void setUpSongData(Song currentSong) {
        binding.tvMusicPlayerTitle.setText(currentSong.getTitle());
        binding.tvMusicPlayerArtist.setText(currentSong.getArtist());
        Glide.with(getApplicationContext()).load(currentSong.getImage()).into(binding.ivMusicPlayerPhoto);
        binding.tvMusicPlayerDurationTotal.setText(getFormattedDuration(currentSong.getDuration()));
        binding.seekBarMusicPlayer.setMax(currentSong.getDuration());
        uri = Uri.parse(currentSong.getSource());

        Constants.MINI_PLAYER_ACTIVE = true;
        SharedPreferences.Editor editor = getSharedPreferences(Constants.SHARE_PREFERENCE_KEY, MODE_PRIVATE).edit();
        editor.putString(Constants.MUSIC_TITLE, currentSong.getTitle());
        editor.putString(Constants.MUSIC_ARTIST, currentSong.getArtist());
        editor.putString(Constants.MUSIC_IMAGE, currentSong.getImage());
        editor.putString(Constants.MUSIC_SOURCE, uri.toString());
        editor.apply();
    }

    @Override
    protected void onResume() {
        setUpThread(binding.fltBtnMusicPlayerPlayPause, this::playPause);
        setUpThread(binding.ivMusicPlayerSkipNext, this::skipNext);
        setUpThread(binding.ivMusicPlayerSkipPrevious, this::skipPrevious);
        super.onResume();
    }

    private void setUpThread(View view, OnThreadClickListener onThreadClickListener) {
        Thread thread = new Thread() {
            @Override
            public void run() {
                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onThreadClickListener.action();
                    }
                });
            }
        };
        thread.start();
    }

    @Override
    public void playPause() {
        if (mediaPlayer.isPlaying()) {
            binding.fltBtnMusicPlayerPlayPause.setImageResource(R.drawable.ic_play_24);
            mediaPlayer.pause();
        } else {
            binding.fltBtnMusicPlayerPlayPause.setImageResource(R.drawable.ic_pause_24);
            mediaPlayer.start();
        }
    }

    @Override
    public void skipPrevious() {
        mediaPlayer.stop();
        mediaPlayer.release();
        if (!isShuffle && !isRepeat) {
            position = (position - 1 < 0) ? (Constants.PLAYLIST_SONG_LIST.size() - 1) : (position - 1);
        } else if (isShuffle && !isRepeat) {
            position = new Random().nextInt(Constants.PLAYLIST_SONG_LIST.size());
        } // (!isShuffle && isRepeat) or (isShuffle && isRepeat) => position = currentPosition
        setUpSongData(Constants.PLAYLIST_SONG_LIST.get(position));
        mediaPlayer = MediaPlayer.create(getApplicationContext(), uri);
        binding.fltBtnMusicPlayerPlayPause.setImageResource(R.drawable.ic_pause_24);
        mediaPlayer.setOnCompletionListener(mp -> skipNext());
        mediaPlayer.start();
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
        mediaPlayer = MediaPlayer.create(getApplicationContext(), uri);
        binding.fltBtnMusicPlayerPlayPause.setImageResource(R.drawable.ic_pause_24);
        mediaPlayer.setOnCompletionListener(mp -> {
            skipNext();
        });
        mediaPlayer.start();
    }
}