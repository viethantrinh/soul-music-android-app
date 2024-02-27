package net.branium.fragments.auth;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import net.branium.R;
import net.branium.activities.MainActivity;
import net.branium.fragments.auth.SignInFragment;
import net.branium.utils.PasswordMaskTransformation;

import java.util.HashMap;
import java.util.Map;


public class SignUpFragment extends Fragment {
    TextView tvAlreadyHaveAccount;
    FrameLayout frmLayoutAuth;
    EditText etRegisterUserName;
    EditText etRegisterEmail;
    EditText etRegisterPassword;
    EditText etRegisterRePassword;
    MaterialButton mtBtnConfirmRegister;
    ProgressBar pbRegisterProcess;
    ImageView ivRegisterShowPwd;
    ImageView ivRegisterShowRePwd;
    FirebaseAuth mAuth;
    FirebaseFirestore db;

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
        ivRegisterShowPwd = view.findViewById(R.id.iv_register_show_pwd);
        ivRegisterShowRePwd = view.findViewById(R.id.iv_register_show_re_pwd);
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
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
                mtBtnConfirmRegister.setStrokeColorResource(R.color.very_light_green);
            }
        });

        ivRegisterShowPwd.setOnClickListener(v -> {
            if (etRegisterPassword.getTransformationMethod().equals(PasswordMaskTransformation.getInstance())) {
                etRegisterPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                ivRegisterShowPwd.setImageResource(R.drawable.ic_hidden_pwd_24);
            } else {
                etRegisterPassword.setTransformationMethod(PasswordMaskTransformation.getInstance());
                ivRegisterShowPwd.setImageResource(R.drawable.ic_show_pwd_24);
            }
        });

        ivRegisterShowRePwd.setOnClickListener(v -> {
            if (etRegisterRePassword.getTransformationMethod().equals(PasswordMaskTransformation.getInstance())) {
                etRegisterRePassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                ivRegisterShowRePwd.setImageResource(R.drawable.ic_hidden_pwd_24);
            } else {
                etRegisterRePassword.setTransformationMethod(PasswordMaskTransformation.getInstance());
                ivRegisterShowRePwd.setImageResource(R.drawable.ic_show_pwd_24);
            }
        });


    }

    private void signUpWithFireBase() {
        String username = etRegisterUserName.getText().toString();
        String email = etRegisterEmail.getText().toString();
        String password = etRegisterPassword.getText().toString();
        String rePassword = etRegisterRePassword.getText().toString();
        if (email.matches("[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+")) {
            if (password.equals(rePassword)) {
                pbRegisterProcess.setVisibility(View.VISIBLE);
                mAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                pbRegisterProcess.setVisibility(View.INVISIBLE);
                                if (task.isSuccessful()) {
                                    Map<String, Object> userMap = new HashMap<>();
                                    userMap.put("username", username);
                                    userMap.put("email", email);
                                    db.collection("users")
                                            .document(task.getResult().getUser().getUid())
                                            .set(userMap)
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void unused) {
                                                    Intent intent = new Intent(getActivity(), MainActivity.class);
                                                    startActivity(intent);
                                                    getActivity().finish();
                                                }
                                            })
                                            .addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    Toast.makeText(getActivity(), e.getMessage(),
                                                            Toast.LENGTH_SHORT).show();
                                                    mtBtnConfirmRegister.setEnabled(true);
                                                    mtBtnConfirmRegister.setBackgroundColor(getResources().getColor(R.color.medium_green));
                                                    mtBtnConfirmRegister.setStrokeColorResource(R.color.medium_green);
                                                }
                                            });
                                } else {
                                    Toast.makeText(getActivity(), task.getException().getMessage(),
                                            Toast.LENGTH_SHORT).show();
                                    mtBtnConfirmRegister.setEnabled(true);
                                    mtBtnConfirmRegister.setBackgroundColor(getResources().getColor(R.color.medium_green));
                                    mtBtnConfirmRegister.setStrokeColorResource(R.color.medium_green);
                                }
                            }
                        });
            } else {
                etRegisterRePassword.setError("Xác nhân mật khẩu không khớp!");
                mtBtnConfirmRegister.setEnabled(true);
                mtBtnConfirmRegister.setBackgroundColor(getResources().getColor(R.color.medium_green));
                mtBtnConfirmRegister.setStrokeColorResource(R.color.medium_green);
            }
        } else {
            etRegisterEmail.setError("Email sai định dạng!");
            mtBtnConfirmRegister.setEnabled(true);
            mtBtnConfirmRegister.setBackgroundColor(getResources().getColor(R.color.medium_green));
            mtBtnConfirmRegister.setStrokeColorResource(R.color.medium_green);
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
                        mtBtnConfirmRegister.setStrokeColorResource(R.color.medium_green);
                    } else {
                        mtBtnConfirmRegister.setEnabled(false);
                        mtBtnConfirmRegister.setBackgroundColor(getResources().getColor(R.color.very_light_green));
                        mtBtnConfirmRegister.setStrokeColorResource(R.color.very_light_green);
                    }
                } else {
                    mtBtnConfirmRegister.setEnabled(false);
                    mtBtnConfirmRegister.setBackgroundColor(getResources().getColor(R.color.very_light_green));
                    mtBtnConfirmRegister.setStrokeColorResource(R.color.very_light_green);
                }
            } else {
                mtBtnConfirmRegister.setEnabled(false);
                mtBtnConfirmRegister.setBackgroundColor(getResources().getColor(R.color.very_light_green));
                mtBtnConfirmRegister.setStrokeColorResource(R.color.very_light_green);
            }
        } else {
            mtBtnConfirmRegister.setEnabled(false);
            mtBtnConfirmRegister.setBackgroundColor(getResources().getColor(R.color.very_light_green));
            mtBtnConfirmRegister.setStrokeColorResource(R.color.very_light_green);
        }
    }
}