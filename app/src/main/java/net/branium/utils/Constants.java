package net.branium.utils;

import android.media.MediaPlayer;
import android.net.Uri;

import net.branium.model.Album;
import net.branium.model.Playlist;
import net.branium.model.Song;

import java.util.ArrayList;
import java.util.List;

public class Constants {
    // static path
    public static final String BASE_PATH = "https://soul-music-48cabeabd033.herokuapp.com";
    public static final String API_PATH = "/api";
    public static final String FULL_PATH = BASE_PATH + API_PATH + "/";
    public static final String EMULATOR_FULL_PATH = "http://10.0.2.2:1234/api/";
    public static final String WINDOW_FULL_PATH_BACKEND = "http://192.168.1.108:1234/api/";
    public static final String MACOS_FULL_PATH_BACKEND = "http://192.168.1.174:1234/api/";

    // Lưu ý: nếu đăng nhập vs google không thành công thì gen ra một SHA 1 key bằng cách sử dụng ./gradlew signingReport
    // sau đó add vào trong firebase
    public static final String GOOGLE_SIGN_IN_KEY = "41333975428-s0f62era7i1uujcphvte64dbn3cjfp4d.apps.googleusercontent.com";

    // Constants for share preferences
    public static final String SHARE_PREFERENCE_KEY = "BRANIUMTECH";
    public static final String MUSIC_TITLE = "MUSIC_TITLE";
    public static final String MUSIC_ARTIST = "MUSIC_ARTIST";
    public static final String MUSIC_IMAGE = "MUSIC_IMAGE";
    public static final String MUSIC_SOURCE = "MUSIC_SOURCE";

    // Music state constants
    public static int position = -1;
    public static int currentPosition = -1;
    public static int type = -1;
    public static Uri uri;
    public static MediaPlayer mediaPlayer;
    public static boolean isRepeat = false;
    public static boolean isShuffle = false;
    public static boolean MINI_PLAYER_ACTIVE = false;
    public static List<Song> CURRENT_SONG_LIST = new ArrayList<>(); // song list hiện tại tùy type


    // constants for broadcast receiver
    public static final String CHANNEL_ID_1 = "channel1";
    public static final String CHANNEL_ID_2 = "channel2";
    public static final String ACTION_PREVIOUS = "actionprevious";
    public static final String ACTION_NEXT = "actionnext";
    public static final String ACTION_PLAY = "actionplay";

    // static data for home fragment
    /* HOME_SONG_LIST và PLAYLIST_SONG_LIST dùng chung một list */
    public static final List<Song> HOME_SONG_LIST = new ArrayList<>(); // type = 1 - để mediaplayer phân biệt được nên chơi nhạc từ list nào
    public static final List<Song> PLAYLIST_SONG_LIST = new ArrayList<>(); // type = 1 - để mediaplayer phân biệt được nên chơi nhạc từ list nào
    public static final List<Album> ALBUM_LIST = new ArrayList<>();
    public static final List<Song> ALBUM_SONG_LIST = new ArrayList<>(); // type = 2 - để mediaplayer phân biệt được nên chơi nhạc từ list nào
    public static final List<Playlist> USER_PLAYLIST_LIST = new ArrayList<>();
}
