package net.branium.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import net.branium.R;
import net.branium.activities.MainActivity;

import java.util.Objects;

public class SignInFragment extends Fragment {
    MaterialButton mtBtnRegister;
    TextView tvForgetPassword;
    FrameLayout frmLayoutAuth;
    EditText etLoginEmail;
    EditText etLoginPassword;
    MaterialButton mtBtnLogin;
    ProgressBar pbLoginProcess;
    FirebaseAuth mAuth;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_sign_in, container, false);
        mtBtnRegister = view.findViewById(R.id.mt_btn_register);
        tvForgetPassword = view.findViewById(R.id.tv_forget_password);
        frmLayoutAuth = requireActivity().findViewById(R.id.frm_layout_auth);
        etLoginEmail = view.findViewById(R.id.et_login_email);
        etLoginPassword = view.findViewById(R.id.et_login_password);
        mtBtnLogin = view.findViewById(R.id.mt_btn_login);
        pbLoginProcess = view.findViewById(R.id.pb_login_process);
        mAuth = FirebaseAuth.getInstance();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Xử lý sự kiện khi nhấn quên mật khẩu
        tvForgetPassword.setOnClickListener(v -> setFragment(new ResetPasswordFragment()));

        // Xử lý sự kiện khi nhấn nút đăng ký
        mtBtnRegister.setOnClickListener(v -> setFragment(new SignUpFragment()));

        mtBtnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkInput()) {
                    pbLoginProcess.setVisibility(View.VISIBLE);
                    signInWithFireBase();
                }
            }
        });

    }

    private void signInWithFireBase() {
        String email = etLoginEmail.getText().toString();
        String password = etLoginPassword.getText().toString();
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        pbLoginProcess.setVisibility(View.VISIBLE);
                        if (task.isSuccessful()) {
                            Intent intent = new Intent(getContext(), MainActivity.class);
                            startActivity(intent);
                        } else {
                            Toast.makeText(getContext(), task.getException().getMessage(),
                                    Toast.LENGTH_SHORT).show();
                            pbLoginProcess.setVisibility(View.INVISIBLE);
                        }
                    }
                });
    }

    private boolean checkInput() {
        boolean isValid = true;
        if (etLoginEmail.getText().toString().isEmpty()) {
            etLoginEmail.setError("Email không được để trống!");
            isValid = false;
        }

        if (!etLoginEmail.getText().toString().isEmpty() && !etLoginEmail.getText().toString().matches("[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+")) {
            etLoginEmail.setError("Emai sai định dạng!");
            isValid = false;
        }

        if (etLoginPassword.getText().toString().isEmpty()) {
            etLoginPassword.setError("Mật khẩu không được để trống!");
            isValid = false;
        }

        return isValid;
    }

    private void setFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = requireActivity().getSupportFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.from_right, R.anim.out_from_left);
        fragmentTransaction.replace(frmLayoutAuth.getId(), fragment);
        fragmentTransaction.commit();
    }


}