package net.branium.view.auth;

import android.os.Bundle;
import android.widget.FrameLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import net.branium.R;

public class AuthActivity extends AppCompatActivity {
    FrameLayout frmLayoutAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);
        frmLayoutAuth = findViewById(R.id.frm_layout_auth);
        setFragment(new SignInFragment());
    }

    private void setFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(frmLayoutAuth.getId(), fragment);
        fragmentTransaction.commit();
    }
}