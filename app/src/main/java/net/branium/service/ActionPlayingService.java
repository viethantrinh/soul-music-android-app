package net.branium.service;

import java.io.IOException;

public interface ActionPlayingService {
    void playPauseBtnClicked() throws IOException;
    void skipNextBtnClicked() throws IOException;
    void skipPreviousBtnClicked() throws IOException;
}
