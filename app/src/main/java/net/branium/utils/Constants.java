package net.branium.utils;

import net.branium.model.Album;
import net.branium.model.Playlist;
import net.branium.model.Song;

import java.util.ArrayList;
import java.util.List;

public class Constants {
    // static path
    public static final String BASE_PATH = "http://localhost:1234";
    public static final String API_PATH = "/api";
    public static final String FULL_PATH = BASE_PATH + API_PATH + "/";
    public static final String EMULATOR_FULL_PATH = "http://10.0.2.2:1234/api/";
    public static final String OPPO_FULL_PATH_ON_WINDOW = "http://192.168.1.108:1234/api/";
    public static final String MACOS_FULL_PATH_BACKEND = "http://192.168.1.174:1234/api/";

    // Lưu ý: nếu đăng nhập vs google không thành công thì gen ra một SHA 1 key bằng cách sử dụng ./gradlew signingReport
    // sau đó add vào trong firebase
    public static final String GOOGLE_SIGN_IN_KEY = "41333975428-s0f62era7i1uujcphvte64dbn3cjfp4d.apps.googleusercontent.com";

    // constants for service


    // static data for home fragment
    public static final List<Song> HOME_SONG_LIST = new ArrayList<>();
    public static final List<Song> PLAYLIST_SONG_LIST = new ArrayList<>();
    public static final List<Album> ALBUM_LIST = new ArrayList<>();
    public static final List<Playlist> USER_PLAYLIST_LIST = new ArrayList<>();
}
