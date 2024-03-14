package net.branium.service;

import static net.branium.view.activities.MusicPlayerActivity.listSongs;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;

import net.branium.model.Song;

import java.io.IOException;
import java.util.ArrayList;

public class MusicService extends Service implements MediaPlayer.OnCompletionListener {
    MyBinder myBinder = new MyBinder();
    MediaPlayer mediaPlayer;
    public ArrayList<Song> songsList = new ArrayList<>();
    Uri uri;
    public int position = -1;
    ActionPlayingService actionPlayingService;
    public static final String MUSIC_LAST_PLAYED = "LAST_PLAYED";
    public static final String MUSIC_FILE = "STORED_MUSIC";
    public static final String ARTIST_NAME = "ARTIST NAME";
    public static final String SONG_NAME = "SONG NAME";
    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Log.e("Bind", "Method");
        return myBinder;
    }



    public class MyBinder extends Binder {
        public MusicService getService() {
            return MusicService.this;
        }
    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if(intent != null) {
            int myPosition = intent.getIntExtra("servicePosition", -1);
            String actionName = intent.getStringExtra("ActionName");
            if(myPosition != -1) {
                playMedia(myPosition);
            }
            if(actionName != null) {
                switch (actionName) {
                    case "playPause" -> {
                        if (actionPlayingService != null) {
                            try {
                                playPauseBtnClicked();
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                        }
                    }
                    case "next" -> {
                        if (actionPlayingService != null) {
                            try {
                                nextBtnClicked();
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                        }
                    }
                    case "previous" -> {
                        if (actionPlayingService != null) {
                            try {
                                previousBtnClicked();
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                        }
                    }
                }
            }
        }
        return START_STICKY;
    }

    private void playMedia(int startPosition) {
        songsList = (ArrayList<Song>) listSongs;
        position = startPosition;
        if(mediaPlayer != null) {
            mediaPlayer .stop();
            mediaPlayer.release();
            if(songsList != null) {
                createMediaPlayer(position);
                mediaPlayer.start();
            }
        }else {
            createMediaPlayer(position);
            mediaPlayer.start();
        }
    }

    public void start() {
        mediaPlayer.start();
    }
    public boolean isPlaying() {
        if (mediaPlayer != null) {
            return mediaPlayer.isPlaying();
        } else {
            return false; // or handle the case where mediaPlayer is null
        }
    }
    public void stop() {
        mediaPlayer.stop();
    }
    public void release() {
        mediaPlayer.release();
    }
    public int getDuration() {
        return mediaPlayer.getDuration();
    }
    public void seekTo(int position) {
        mediaPlayer.seekTo(position);
    }
    public int getCurrentPosition() {
        return mediaPlayer.getCurrentPosition();
    }
    public void createMediaPlayer(int positionInner) {
        position = positionInner;
        uri = Uri.parse(songsList.get(position).getSource());
        SharedPreferences.Editor editor = getSharedPreferences(MUSIC_LAST_PLAYED, MODE_PRIVATE).edit();
        editor.putString(MUSIC_FILE, uri.toString());
        editor.putString(ARTIST_NAME, songsList.get(position).getArtist());
        editor.putString(SONG_NAME, songsList.get(position).getTitle());
        editor.apply();
        mediaPlayer = MediaPlayer.create(getBaseContext(), uri);
    }
    public void pause() {
        mediaPlayer.pause();
    }

    public void OnCompleted() {
        mediaPlayer.setOnCompletionListener(this);
    }
    @Override
    public void onCompletion(MediaPlayer mp) {
        if(actionPlayingService != null) {
            try {
                actionPlayingService.skipNextBtnClicked();
                if(mediaPlayer != null) {
                    createMediaPlayer(position);
                    mediaPlayer.start();
                    OnCompleted();
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
    public void setCallBack(ActionPlayingService actionPlayingService) {
        this.actionPlayingService = actionPlayingService;
    }

    public void playPauseBtnClicked() throws IOException {
        if(actionPlayingService != null) {
            actionPlayingService.playPauseBtnClicked();
        }
    }
    public void previousBtnClicked() throws IOException {
        if(actionPlayingService != null) {
            actionPlayingService.skipPreviousBtnClicked();
        }
    }
    public void nextBtnClicked() throws IOException {
        if(actionPlayingService != null) {
            actionPlayingService.skipNextBtnClicked();
        }
    }
}
