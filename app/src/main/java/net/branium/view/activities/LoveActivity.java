package net.branium.view.activities;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import net.branium.R;

/**
 * Class này để là activity khi nhấn vào một love playlist sẽ hiển ra
 */
public class LoveActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_love);

    }
}