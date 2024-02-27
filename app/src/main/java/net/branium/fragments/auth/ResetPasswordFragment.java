package net.branium.fragments.auth;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;

import net.branium.R;


public class ResetPasswordFragment extends Fragment {

    TextView tvBackToLogin;
    FrameLayout frmLayoutAuth;
    EditText etRePasswordEmail;
    TextView tvCheckYourEmail;
    ProgressBar pbRePasswordProcess;
    MaterialButton mtBtnRePassword;
    FirebaseAuth mAuth;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_reset_password, container, false);
        tvBackToLogin = view.findViewById(R.id.tv_back_to_login);
        frmLayoutAuth = requireActivity().findViewById(R.id.frm_layout_auth);
        etRePasswordEmail = view.findViewById(R.id.et_re_password_email);
        tvCheckYourEmail = view.findViewById(R.id.tv_check_your_email);
        pbRePasswordProcess = view.findViewById(R.id.pb_re_password_process);
        mtBtnRePassword = view.findViewById(R.id.mt_btn_re_password);
        mAuth = FirebaseAuth.getInstance();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        tvBackToLogin.setOnClickListener(v -> setFragment(new SignInFragment()));
        mtBtnRePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkInput()) {
                    pbRePasswordProcess.setVisibility(View.VISIBLE);
                    resetPasswordWithFirebase();
                }
            }
        });
    }

    private void resetPasswordWithFirebase() {
        String resetEmail = etRePasswordEmail.getText().toString();
        mAuth.setLanguageCode("vn");
        mAuth.sendPasswordResetEmail(resetEmail)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            tvCheckYourEmail.setText(R.string.txt_tv_check_your_email);
                            tvCheckYourEmail.setTextColor(getResources().getColor(R.color.medium_green));
                        } else {
                            tvCheckYourEmail.setText("Có lỗi xảy ra khi gửi email đi!");
                            tvCheckYourEmail.setTextColor(getResources().getColor(R.color.red));
                        }
                        pbRePasswordProcess.setVisibility(View.INVISIBLE);
                        tvCheckYourEmail.setVisibility(View.VISIBLE);
                    }
                });
    }


    private boolean checkInput() {
        boolean isValid = true;
        if (etRePasswordEmail.getText().toString().isEmpty()) {
            etRePasswordEmail.setError("Email không được để trống!");
            isValid = false;
        }

        if (!etRePasswordEmail.getText().toString().isEmpty() && !etRePasswordEmail.getText().toString().matches("[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+")) {
            etRePasswordEmail.setError("Emai sai định dạng!");
            isValid = false;
        }

        return isValid;
    }

    private void setFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = requireActivity().getSupportFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.from_left, R.anim.out_from_right);
        fragmentTransaction.replace(frmLayoutAuth.getId(), fragment);
        fragmentTransaction.commit();
    }
}
