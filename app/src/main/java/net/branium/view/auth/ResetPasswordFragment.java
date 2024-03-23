package net.branium.view.auth;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

import net.branium.R;
import net.branium.databinding.FragmentResetPasswordBinding;


public class ResetPasswordFragment extends Fragment implements View.OnClickListener {
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FragmentResetPasswordBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_reset_password, container, false);
        registerEventListener();
        return binding.getRoot();
    }

    @Override
    public void onClick(View v) {
        var id = v.getId();
        if (id == binding.tvBackToLogin.getId()) {
            setFragment(new SignInFragment());
        } else if (id == binding.mtBtnRePassword.getId()) {
            resetPasswordWithFirebase();
        }
    }

    private void registerEventListener() {
        binding.tvBackToLogin.setOnClickListener(this);
        binding.mtBtnRePassword.setOnClickListener(this);
    }

    private void resetPasswordWithFirebase() {
        String resetEmail = binding.etRePasswordEmail.getText().toString();
        binding.pbRePasswordProcess.setVisibility(View.VISIBLE);
        if (checkInput(resetEmail)) {
            mAuth.setLanguageCode("vn");
            mAuth.sendPasswordResetEmail(resetEmail).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        binding.tvCheckYourEmail.setText(R.string.txt_tv_check_your_email);
                        binding.tvCheckYourEmail.setTextColor(getResources().getColor(R.color.medium_green));
                    } else {
                        binding.tvCheckYourEmail.setText("Có lỗi xảy ra khi gửi email đi!");
                        binding.tvCheckYourEmail.setTextColor(getResources().getColor(R.color.red));
                    }
                }
            });
            binding.pbRePasswordProcess.setVisibility(View.INVISIBLE);
            binding.tvCheckYourEmail.setVisibility(View.VISIBLE);
        }
    }


    private boolean checkInput(String email) {
        boolean isValid = true;
        if (email == null || email.isEmpty()) {
            binding.etRePasswordEmail.setError("Email không được để trống!");
            isValid = false;
        }

        if (email != null && !email.matches("[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+")) {
            binding.etRePasswordEmail.setError("Emai sai định dạng!");
            isValid = false;
        }

        return isValid;
    }

    private void setFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = requireActivity().getSupportFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.from_right, R.anim.out_from_left);
        fragmentTransaction.replace(requireActivity().findViewById(R.id.frm_layout_auth).getId(), fragment);
        fragmentTransaction.commit();
    }
}
