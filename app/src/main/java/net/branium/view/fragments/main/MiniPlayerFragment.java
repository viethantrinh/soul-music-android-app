package net.branium.view.fragments.main;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import net.branium.R;

public class MiniPlayerFragment extends Fragment {
//    ImageView nextBtn;
//    ImageView albumArtImage;
//    TextView artist;
//    TextView songName;
//    FloatingActionButton playPauseBtn;
//    MusicService musicService;
//    public static final String MUSIC_LAST_PLAYED = "LAST_PLAYED";
//    public static final String MUSIC_FILE = "STORED_MUSIC";
//    public static final String ARTIST_NAME = "ARTIST NAME";
//    public static final String SONG_NAME = "SONG NAME";
//
//    public MiniPlayerFragment() {
//    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_mini_player, container, false);

//        artist = view.findViewById(R.id.tv_song_artist_mini_player);
//        songName = view.findViewById(R.id.tv_song_name_mini_player);
//        albumArtImage = view.findViewById(R.id.iv_mini_album_art);
//        nextBtn = view.findViewById(R.id.iv_mini_skip_next_bottom);
//        playPauseBtn = view.findViewById(R.id.fab_play_pause_mini_player);
//
//        nextBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if(musicService != null) {
//                    try {
//                        musicService.nextBtnClicked();
//                        if(getActivity() != null) {
//                            SharedPreferences.Editor editor = getActivity().getSharedPreferences(MUSIC_LAST_PLAYED, MODE_PRIVATE).edit();
//                            editor.putString(MUSIC_FILE, musicService.songsList
//                                    .get(musicService.position).getSource());
//                            editor.putString(ARTIST_NAME, musicService.songsList
//                                    .get(musicService.position).getArtist());
//                            editor.putString(SONG_NAME, musicService.songsList
//                                    .get(musicService.position).getTitle());
//                            editor.apply();
//
//                            SharedPreferences preferences = getActivity().getSharedPreferences(MUSIC_LAST_PLAYED, MODE_PRIVATE);
//                            String path = preferences.getString(MUSIC_FILE, null);
//                            String artistName = preferences.getString(ARTIST_NAME, null);
//                            String song_name = preferences.getString(SONG_NAME, null);
//                            if(path != null) {
//                                SHOW_MINI_PLAYER = true;
//                                PATH_TO_FRAG = path;
//                                ARTIST_TO_FRAG = artistName;
//                                SONG_NAME_TO_FRAG = song_name;
//                            }else {
//                                SHOW_MINI_PLAYER = false;
//                                PATH_TO_FRAG = null;
//                                ARTIST_TO_FRAG = null;
//                                SONG_NAME_TO_FRAG = null;
//                            }
//                            if(SHOW_MINI_PLAYER) {
////                                if(PATH_TO_FRAG != null) {
////                                    try {
////                                        byte[] art = getMusicPhoto(PATH_TO_FRAG);
////                                        if(art != null) {
////                                            Glide.with(requireContext()).load(art).into(albumArtImage);
////                                        }else {
////                                            Glide.with(requireContext()).load(R.drawable.logo_image).into(albumArtImage);
////                                        }
////                                        songName.setText(SONG_NAME_TO_FRAG);
////                                        artist.setText(ARTIST_TO_FRAG);
////                                    } catch (IOException e) {
////                                        throw new RuntimeException(e);
////                                    }
////                                }
//                            }
//                        }
//                    } catch (IOException e) {
//                        throw new RuntimeException(e);
//                    }
//                }
//            }
//        });
//
//        playPauseBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if(musicService != null) {
//                    try {
//                        musicService.playPauseBtnClicked();
//                        if(musicService.isPlaying()) {
//                            playPauseBtn.setImageResource(R.drawable.ic_pause_24);
//                        }else {
//                            playPauseBtn.setImageResource(R.drawable.ic_play_24);
//                        }
//                    } catch (IOException e) {
//                        throw new RuntimeException(e);
//                    }
//                }
//            }
//        });
//
        return view;
    }

//    @Override
//    public void onResume() {
//        super.onResume();
//        if(SHOW_MINI_PLAYER) {
//            if(PATH_TO_FRAG != null) {
//                try {
//                    byte[] art = getMusicPhoto(PATH_TO_FRAG);
//                    if(art != null) {
//                        Glide.with(getContext()).load(art).into(albumArtImage);
//                    }else {
//                        Glide.with(getContext()).load(R.drawable.logo_image).into(albumArtImage);
//                    }
//                    songName.setText(SONG_NAME_TO_FRAG);
//                    artist.setText(ARTIST_TO_FRAG);
//
//                    Intent intent = new Intent(getContext(), MusicService.class);
//                    if(getContext() != null) {
//                        getContext().bindService(intent, this, BIND_AUTO_CREATE);
//                    }
//                } catch (IOException e) {
//                    throw new RuntimeException(e);
//                }
//            }
//        }
//    }

//    @Override
//    public void onPause() {
//        super.onPause();
//        if(getContext() != null) {
//            getContext().unbindService(this);
//        }
////        if (musicService != null && getContext() != null) {
////            getContext().unbindService(this);
////        }
//    }
//
//    private byte[] getMusicPhoto(String uri) throws IOException {
//        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
//        retriever.setDataSource(uri);
//        byte[] photo = retriever.getEmbeddedPicture();
//        retriever.release();
//        return photo;
//    }
//
//    @Override
//    public void onServiceConnected(ComponentName name, IBinder service) {
//        MusicService.MyBinder myBinder = (MusicService.MyBinder) service;
//        musicService = myBinder.getService();
//    }
//
//    @Override
//    public void onServiceDisconnected(ComponentName name) {
//        musicService = null;
//    }
}