package net.branium.view.fragments.auth;

import android.content.Intent;
import android.os.Bundle;
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

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.firestore.FirebaseFirestore;

import net.branium.R;
import net.branium.view.activities.MainActivity;
import net.branium.view.utils.PasswordMaskTransformation;

import java.util.HashMap;
import java.util.Map;

public class SignInFragment extends Fragment {
    MaterialButton mtBtnRegister;
    TextView tvForgetPassword;
    FrameLayout frmLayoutAuth;
    EditText etLoginEmail;
    EditText etLoginPassword;
    MaterialButton mtBtnLogin;
    ProgressBar pbLoginProcess;
    MaterialButton mtBtnLoginWithGoogle;
    FirebaseAuth mAuth;
    FirebaseFirestore db;
    GoogleSignInOptions gso;
    GoogleSignInClient gsc;
    ImageView ivShowPwd;
    int RC_SIGN_IN = 20;

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
        ivShowPwd = view.findViewById(R.id.iv_show_pwd);
        mtBtnLoginWithGoogle = view.findViewById(R.id.mt_btn_login_with_google);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken("41333975428-s0f62era7i1uujcphvte64dbn3cjfp4d.apps.googleusercontent.com").
                requestEmail().build();
        gsc = GoogleSignIn.getClient(getActivity(), gso);

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

        mtBtnLoginWithGoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signInWithGoogle();
            }
        });

        ivShowPwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                processPassword();
            }
        });

    }

    private void processPassword() {
        if (etLoginPassword.getTransformationMethod().equals(PasswordMaskTransformation.getInstance())) {
            etLoginPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            ivShowPwd.setImageResource(R.drawable.ic_hidden_pwd_24);
        } else {
            etLoginPassword.setTransformationMethod(PasswordMaskTransformation.getInstance());
            ivShowPwd.setImageResource(R.drawable.ic_show_pwd_24);
        }
    }

    private void signInWithGoogle() {
        Intent intent = gsc.getSignInIntent();
        startActivityForResult(intent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                var idToken = account.getIdToken();
                firebaseAuth(idToken);
            } catch (ApiException e) {
                Toast.makeText(getContext(), "Somethong wroing", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void firebaseAuth(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = mAuth.getCurrentUser();
                            Map<String, Object> map = new HashMap<>();
                            map.put("id", user.getUid());
                            map.put("username", user.getDisplayName());
                            map.put("email", user.getEmail());

                            db.collection("users")
                                    .document(user.getUid())
                                    .set(map)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {
                                            Intent intent = new Intent(getContext(), MainActivity.class);
                                            startActivity(intent);
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(getContext(), e.getMessage(),
                                                    Toast.LENGTH_SHORT).show();
                                        }
                                    });
                        } else {
                            Toast.makeText(getContext(), task.getException().getMessage(),
                                    Toast.LENGTH_SHORT).show();
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