package net.branium.view.fragments.main;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

import net.branium.R;
import net.branium.databinding.ActivityUserUpdateBinding;
import net.branium.model.User;
import net.branium.repository.UserRepository;

public class UserUpdateActivity extends AppCompatActivity implements View.OnClickListener {
    private UserRepository userRepo = new UserRepository();
    private ActivityUserUpdateBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_update);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_user_update);
        registerEventListener();
    }

    @Override
    public void onClick(View v) {
        var id = v.getId();
        if (binding.mtBtnUpdateUserAccount.getId() == id) {
            updateAccountUser();
        } else if (binding.mtBtnBackUserAccount.getId() == id) {
            finish();
        }
    }

    private void registerEventListener() {
        binding.mtBtnUpdateUserAccount.setOnClickListener(this);
        binding.mtBtnBackUserAccount.setOnClickListener(this);
    }

    private void updateAccountUser() {
        if (!binding.etUsernameUpdate.getText().toString().isEmpty()) {
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            String updatedUsername = binding.etUsernameUpdate.getText().toString();
            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                    .setDisplayName(updatedUsername)
                    .build();
            user.updateProfile(profileUpdates).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        User userUpdated = new User(user.getUid(), updatedUsername,
                                user.getEmail(), null);
                        userRepo.updateUser(userUpdated);
                        finish();
                    }
                }
            });
            Toast.makeText(getApplicationContext(), "Cập nhật thành công", Toast.LENGTH_SHORT).show();
        } else {
            binding.etUsernameUpdate.setError("Tên người dùng k đc để trống!");
        }
    }
}