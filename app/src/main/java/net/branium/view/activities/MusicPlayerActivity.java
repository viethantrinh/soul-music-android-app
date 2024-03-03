package net.branium.view.activities;

import static net.branium.view.activities.SplashActivity.musicFiles;

import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import net.branium.R;
import net.branium.model.MusicFiles;

import java.util.ArrayList;
import java.util.List;

public class MusicPlayerActivity extends AppCompatActivity {
    private ImageView ivMusicPlayerCloseDown;
    private ImageView ivMusicPlayerOption;
    private ImageView ivMusicPlayerPhoto;
    private TextView tvMusicPlayerTitle;
    private TextView tvMusicPlayerArtist;
    private TextView tvMusicPlayerDurationRun;
    private TextView tvMusicPlayerDurationTotal;
    private SeekBar seekBarMusicPlayer;
    private ImageView ivMusicPlayerShuffle;
    private ImageView ivMusicPlayerRepeat;
    private ImageView ivMusicPlayerSkipPrevious;
    private ImageView ivMusicPlayerSkipNext;
    private FloatingActionButton fltBtnMusicPlayerPlayPause;
    private int position = -1;
    static List<MusicFiles> listSongs = new ArrayList<>();
    static Uri uri;
    static MediaPlayer mediaPlayer;
    private Handler handler = new Handler();
    private Thread playPauseThread;
    private Thread skipPreviousThread;
    private Thread skipNextThread;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music_player);
        initializeViewComponent();
        getIntentMethod();
        tvMusicPlayerTitle.setText(listSongs.get(position).getTitle());
        tvMusicPlayerArtist.setText(listSongs.get(position).getArtist());
        seekBarMusicPlayer.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
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
        MusicPlayerActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (mediaPlayer != null) {
                    int mCurrentPosition = mediaPlayer.getCurrentPosition() / 1000;
                    seekBarMusicPlayer.setProgress(mCurrentPosition);
                    tvMusicPlayerDurationRun.setText(formattedTime(mCurrentPosition));
                }
                handler.postDelayed(this, 1000);
            }
        });
    }

    @Override
    protected void onResume() {
        playPauseThreadBtn();
        skipPreviousThreadBtn();
        skipNextThreadBtn();
        super.onResume();
    }
    private void playPauseThreadBtn() {
        playPauseThread = new Thread() {
            @Override
            public void run() {
                super.run();
                fltBtnMusicPlayerPlayPause.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        playPauseBtnClicked();
                    }
                });
            }
        };
        playPauseThread.start();
    }
    private void skipNextThreadBtn() {
        skipNextThread = new Thread() {
            @Override
            public void run() {
                super.run();
                ivMusicPlayerSkipNext.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        skipNextBtnClicked();
                    }
                });
            }
        };
        skipNextThread.start();
    }
    private void skipPreviousThreadBtn() {
        skipPreviousThread = new Thread() {
            @Override
            public void run() {
                super.run();
                ivMusicPlayerSkipPrevious.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        skipPreviousBtnClicked();
                    }
                });
            }
        };
        skipPreviousThread.start();

    }

    private void playPauseBtnClicked() {
        if (mediaPlayer.isPlaying()) {
            fltBtnMusicPlayerPlayPause.setImageResource(R.drawable.ic_play_24);
            mediaPlayer.pause();
            seekBarMusicPlayer.setMax(mediaPlayer.getDuration() / 1000);
            MusicPlayerActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (mediaPlayer != null) {
                        int mCurrentPosition = mediaPlayer.getCurrentPosition() / 1000;
                        seekBarMusicPlayer.setProgress(mCurrentPosition);
                    }
                    handler.postDelayed(this, 1000);
                }
            });
        } else {
            fltBtnMusicPlayerPlayPause.setImageResource(R.drawable.ic_pause_24);
            mediaPlayer.start();
            seekBarMusicPlayer.setMax(mediaPlayer.getDuration() / 1000);
            MusicPlayerActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (mediaPlayer != null) {
                        int mCurrentPosition = mediaPlayer.getCurrentPosition() / 1000;
                        seekBarMusicPlayer.setProgress(mCurrentPosition);
                    }
                    handler.postDelayed(this, 1000);
                }
            });
        }
    }
    private void skipNextBtnClicked() {
        if (mediaPlayer.isPlaying()) {
            mediaPlayer.stop();
            mediaPlayer.release();
            position = ((position + 1) % listSongs.size());
            uri = Uri.parse(listSongs.get(position).getPath());
            mediaPlayer = MediaPlayer.create(getApplicationContext(), uri);
            metaData(uri);
            tvMusicPlayerTitle.setText(listSongs.get(position).getTitle());
            tvMusicPlayerArtist.setText(listSongs.get(position).getArtist());
            seekBarMusicPlayer.setMax(mediaPlayer.getDuration() / 1000);
            MusicPlayerActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (mediaPlayer != null) {
                        int mCurrentPosition = mediaPlayer.getCurrentPosition() / 1000;
                        seekBarMusicPlayer.setProgress(mCurrentPosition);
                    }
                    handler.postDelayed(this, 1000);
                }
            });
            fltBtnMusicPlayerPlayPause.setImageResource(R.drawable.ic_pause_24);
            mediaPlayer.start();
        } else {
            mediaPlayer.stop();
            mediaPlayer.release();
            position = ((position + 1) % listSongs.size());
            uri = Uri.parse(listSongs.get(position).getPath());
            mediaPlayer = MediaPlayer.create(getApplicationContext(), uri);
            metaData(uri);
            tvMusicPlayerTitle.setText(listSongs.get(position).getTitle());
            tvMusicPlayerArtist.setText(listSongs.get(position).getArtist());
            seekBarMusicPlayer.setMax(mediaPlayer.getDuration() / 1000);
            MusicPlayerActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (mediaPlayer != null) {
                        int mCurrentPosition = mediaPlayer.getCurrentPosition() / 1000;
                        seekBarMusicPlayer.setProgress(mCurrentPosition);
                    }
                    handler.postDelayed(this, 1000);
                }
            });
            fltBtnMusicPlayerPlayPause.setImageResource(R.drawable.ic_play_24);
        }
    }
    private void skipPreviousBtnClicked() {
        if (mediaPlayer.isPlaying()) {
            mediaPlayer.stop();
            mediaPlayer.release();
            position = (position - 1) < 0 ? (listSongs.size() - 1) : (position - 1);
            uri = Uri.parse(listSongs.get(position).getPath());
            mediaPlayer = MediaPlayer.create(getApplicationContext(), uri);
            metaData(uri);
            tvMusicPlayerTitle.setText(listSongs.get(position).getTitle());
            tvMusicPlayerArtist.setText(listSongs.get(position).getArtist());
            seekBarMusicPlayer.setMax(mediaPlayer.getDuration() / 1000);
            MusicPlayerActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (mediaPlayer != null) {
                        int mCurrentPosition = mediaPlayer.getCurrentPosition() / 1000;
                        seekBarMusicPlayer.setProgress(mCurrentPosition);
                    }
                    handler.postDelayed(this, 1000);
                }
            });
            fltBtnMusicPlayerPlayPause.setImageResource(R.drawable.ic_pause_24);
            mediaPlayer.start();
        } else {
            mediaPlayer.stop();
            mediaPlayer.release();
            position = ((position - 1) < 0 ? (listSongs.size() - 1) : (position - 1));
            uri = Uri.parse(listSongs.get(position).getPath());
            mediaPlayer = MediaPlayer.create(getApplicationContext(), uri);
            metaData(uri);
            tvMusicPlayerTitle.setText(listSongs.get(position).getTitle());
            tvMusicPlayerArtist.setText(listSongs.get(position).getArtist());
            seekBarMusicPlayer.setMax(mediaPlayer.getDuration() / 1000);
            MusicPlayerActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (mediaPlayer != null) {
                        int mCurrentPosition = mediaPlayer.getCurrentPosition() / 1000;
                        seekBarMusicPlayer.setProgress(mCurrentPosition);
                    }
                    handler.postDelayed(this, 1000);
                }
            });
            fltBtnMusicPlayerPlayPause.setImageResource(R.drawable.ic_play_24);
        }
    }


    private void metaData(Uri uri) {
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        retriever.setDataSource(uri.toString());
        int durationTotal = Integer.parseInt(listSongs.get(position).getDuration()) / 1000;
        tvMusicPlayerDurationTotal.setText(formattedTime(durationTotal));
        byte[] musicPlayerPhoto = retriever.getEmbeddedPicture();
        if (musicPlayerPhoto != null) {
            Glide.with(this).asBitmap().load(musicPlayerPhoto).into(ivMusicPlayerPhoto);
        } else {
            Glide.with(this).asBitmap().load(R.drawable.ic_launcher_background).into(ivMusicPlayerPhoto);
        }
    }
    private String formattedTime(int mCurrentPosition) {
        String totalOut = "";
        String totalNew = "";
        String seconds = String.valueOf(mCurrentPosition % 60);
        String minutes = String.valueOf(mCurrentPosition / 60);
        totalOut = minutes + ":" + seconds;
        totalNew = minutes + ":" + "0" + seconds;
        if (seconds.length() == 1) {
            return totalNew;
        } else {
            return totalOut;
        }

    }
    private void getIntentMethod() {
        position = getIntent().getIntExtra("position", -1);
        listSongs = musicFiles;
        if (!listSongs.isEmpty()) {
            fltBtnMusicPlayerPlayPause.setImageResource(R.drawable.ic_pause_24);
            uri = Uri.parse(listSongs.get(position).getPath());
        }

        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = MediaPlayer.create(getApplicationContext(), uri);
            mediaPlayer.start();
        } else {
            mediaPlayer = MediaPlayer.create(getApplicationContext(), uri);
            mediaPlayer.start();
        }
        seekBarMusicPlayer.setMax(mediaPlayer.getDuration() / 1000);
        metaData(uri);
    }
    private void initializeViewComponent() {
        ivMusicPlayerCloseDown = findViewById(R.id.iv_music_player_close_down);
        ivMusicPlayerOption = findViewById(R.id.iv_music_player_option);
        ivMusicPlayerPhoto = findViewById(R.id.iv_music_player_photo);
        tvMusicPlayerTitle = findViewById(R.id.tv_music_player_title);
        tvMusicPlayerArtist = findViewById(R.id.tv_music_player_artist);
        tvMusicPlayerDurationRun = findViewById(R.id.tv_music_player_duration_run);
        tvMusicPlayerDurationTotal = findViewById(R.id.tv_music_player_duration_total);
        seekBarMusicPlayer = findViewById(R.id.seek_bar_music_player);
        ivMusicPlayerShuffle = findViewById(R.id.iv_music_player_shuffle);
        ivMusicPlayerRepeat = findViewById(R.id.iv_music_player_repeat);
        ivMusicPlayerSkipPrevious = findViewById(R.id.iv_music_player_skip_previous);
        ivMusicPlayerSkipNext = findViewById(R.id.iv_music_player_skip_next);
        fltBtnMusicPlayerPlayPause = findViewById(R.id.flt_btn_music_player_play_pause);
    }
}