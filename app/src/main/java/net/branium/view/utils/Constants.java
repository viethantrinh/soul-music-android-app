package net.branium.view.utils;

import net.branium.model.Album;
import net.branium.model.Song;

import java.util.ArrayList;
import java.util.List;

public class Constants {
    public static final String BASE_PATH = "http://localhost:1234";
    public static final String API_PATH = "/api";
    public static final String FULL_PATH = BASE_PATH + API_PATH + "/";
    public static final String EMULATOR_FULL_PATH = "http://10.0.2.2:1234/api/";
    public static final String OPPO_FULL_PATH_ON_WINDOW = "http://192.168.1.108:1234/api/";

    // static data for home fragment
    public static final List<Song> songList = new ArrayList<>();
    public static final List<Song> homeSongList = new ArrayList<>();
    public static final List<Album> albumList = new ArrayList<>();


}
