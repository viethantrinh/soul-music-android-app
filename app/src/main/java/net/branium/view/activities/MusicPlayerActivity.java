package net.branium.view.activities;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.SeekBar;


import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import net.branium.R;
import net.branium.databinding.ActivityMusicPlayerBinding;
import net.branium.utils.Constants;


public class MusicPlayerActivity extends AppCompatActivity {
    private static int position = -1;
    private static Uri uri;
    private static MediaPlayer mediaPlayer;
    private static ActivityMusicPlayerBinding binding;
    private static Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music_player);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_music_player);
        setupMediaPlayer();
        binding.seekBarMusicPlayer.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                mediaPlayer.seekTo(progress * 1000);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        MusicPlayerActivity.this.runOnUiThread(new Runnable() {
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
        binding.fltBtnMusicPlayerPlayPause.setImageResource(R.drawable.ic_pause_24);
        uri = Uri.parse(Constants.PLAYLIST_SONG_LIST.get(position).getSource());

        if (mediaPlayer != null) { // trong trường hợp media player tồn tại bài hát -> stop -> release -> chạy lại
            mediaPlayer.stop();
            mediaPlayer.release();
        }

        mediaPlayer = MediaPlayer.create(getApplicationContext(), uri);
        mediaPlayer.start();

        binding.seekBarMusicPlayer.setMax(mediaPlayer.getDuration() / 1000);
    }
}