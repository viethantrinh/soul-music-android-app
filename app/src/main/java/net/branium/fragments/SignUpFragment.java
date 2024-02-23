package net.branium.fragments;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

import net.branium.R;
import net.branium.activities.MainActivity;


public class SignUpFragment extends Fragment {
    TextView tvAlreadyHaveAccount;
    FrameLayout frmLayoutAuth;

    EditText etRegisterUserName;
    EditText etRegisterEmail;
    EditText etRegisterPassword;
    EditText etRegisterRePassword;
    MaterialButton mtBtnConfirmRegister;
    ProgressBar pbRegisterProcess;
    FirebaseAuth mAuth;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_sign_up, container, false);
        tvAlreadyHaveAccount = view.findViewById(R.id.tv_already_have_account);
        frmLayoutAuth = requireActivity().findViewById(R.id.frm_layout_auth);

        etRegisterUserName = view.findViewById(R.id.et_register_username);
        etRegisterEmail = view.findViewById(R.id.et_register_email);
        etRegisterPassword = view.findViewById(R.id.et_register_password);
        etRegisterRePassword = view.findViewById(R.id.et_register_re_password);
        mtBtnConfirmRegister = view.findViewById(R.id.mt_btn_confirm_register);
        pbRegisterProcess = view.findViewById(R.id.pb_register_process);
        mAuth = FirebaseAuth.getInstance();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Xử lý sự kiện khi nhấn đã có mật khẩu
        tvAlreadyHaveAccount.setOnClickListener(v -> setFragment(new SignInFragment()));

        etRegisterUserName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                checkInputs();

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        etRegisterEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                checkInputs();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        etRegisterPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                checkInputs();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        etRegisterRePassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                checkInputs();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        mtBtnConfirmRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signUpWithFireBase();
                mtBtnConfirmRegister.setEnabled(false);
                mtBtnConfirmRegister.setBackgroundColor(getResources().getColor(R.color.very_light_green));
            }
        });


    }

    private void signUpWithFireBase() {
        if (etRegisterEmail.getText().toString().matches("[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+")) {
            if (etRegisterPassword.getText().toString().equals(etRegisterRePassword.getText().toString())) {
                pbRegisterProcess.setVisibility(View.VISIBLE);
                mAuth.createUserWithEmailAndPassword(etRegisterEmail.getText().toString(), etRegisterPassword.getText().toString())
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                pbRegisterProcess.setVisibility(View.INVISIBLE);
                                if (task.isSuccessful()) {
                                    Intent intent = new Intent(getActivity(), MainActivity.class);
                                    startActivity(intent);
                                    getActivity().finish();
                                } else {
                                    Toast.makeText(getActivity(), task.getException().getMessage(),
                                            Toast.LENGTH_SHORT).show();
                                    mtBtnConfirmRegister.setEnabled(true);
                                    mtBtnConfirmRegister.setBackgroundColor(getResources().getColor(R.color.medium_green));

                                }
                            }
                        });
            } else {
                etRegisterRePassword.setError("Xác nhân mật khẩu không khớp!");
                mtBtnConfirmRegister.setEnabled(true);
                mtBtnConfirmRegister.setBackgroundColor(getResources().getColor(R.color.medium_green));
            }
        } else {
            etRegisterEmail.setError("Email sai định dạng!");
            mtBtnConfirmRegister.setEnabled(true);
            mtBtnConfirmRegister.setBackgroundColor(getResources().getColor(R.color.medium_green));
        }
    }

    private void setFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = requireActivity().getSupportFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.from_left, R.anim.out_from_right);
        fragmentTransaction.replace(frmLayoutAuth.getId(), fragment);
        fragmentTransaction.commit();
    }

    private void checkInputs() {
        if (!etRegisterUserName.getText().toString().isEmpty()) {
            if (!etRegisterEmail.getText().toString().isEmpty()) {
                if (!etRegisterPassword.getText().toString().isEmpty() && etRegisterPassword.getText().length() >= 6) {
                    if (!etRegisterRePassword.getText().toString().isEmpty()) {
                        mtBtnConfirmRegister.setEnabled(true);
                        mtBtnConfirmRegister.setBackgroundColor(getResources().getColor(R.color.medium_green));
                    } else {
                        mtBtnConfirmRegister.setEnabled(false);
                        mtBtnConfirmRegister.setBackgroundColor(getResources().getColor(R.color.very_light_green));
                    }
                } else {
                    mtBtnConfirmRegister.setEnabled(false);
                    mtBtnConfirmRegister.setBackgroundColor(getResources().getColor(R.color.very_light_green));
                }
            } else {
                mtBtnConfirmRegister.setEnabled(false);
                mtBtnConfirmRegister.setBackgroundColor(getResources().getColor(R.color.very_light_green));
            }
        } else {
            mtBtnConfirmRegister.setEnabled(false);
            mtBtnConfirmRegister.setBackgroundColor(getResources().getColor(R.color.very_light_green));
        }
    }
}