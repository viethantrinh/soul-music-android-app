package net.branium.view.fragments.auth;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import net.branium.R;
import net.branium.databinding.FragmentSignInBinding;
import net.branium.model.User;
import net.branium.repository.UserRepository;
import net.branium.utils.Constants;
import net.branium.view.activities.MainActivity;

public class SignInFragment extends Fragment implements View.OnClickListener {
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private GoogleSignInOptions gso;
    private GoogleSignInClient gsc;
    private UserRepository userRepo;
    private FragmentSignInBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(Constants.GOOGLE_SIGN_IN_KEY)
                .requestEmail()
                .build();
        gsc = GoogleSignIn.getClient(requireContext(), gso);
        userRepo = new UserRepository();
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_sign_in, container, false);
        registerEventListener();
        return binding.getRoot();
    }


    @Override
    public void onClick(View v) {
        var id = v.getId();
        if (id == binding.tvForgetPassword.getId()) {
            setFragment(new ResetPasswordFragment());
        } else if (id == binding.mtBtnRegister.getId()) {
            setFragment(new SignUpFragment());
        } else if (id == binding.ivShowPwd.getId()) {
            togglePasswordMask(binding.etLoginPassword, binding.ivShowPwd);
        } else if (id == binding.mtBtnLogin.getId()) {
            signInWithFireBase();
        } else if (id == binding.mtBtnLoginWithGoogle.getId()) {
            signInWithGoogle();
        }
    }

    private void registerEventListener() {
        binding.tvForgetPassword.setOnClickListener(this);
        binding.mtBtnRegister.setOnClickListener(this);
        binding.ivShowPwd.setOnClickListener(this);
        binding.mtBtnLogin.setOnClickListener(this);
        binding.mtBtnLoginWithGoogle.setOnClickListener(this);
    }

    private void setFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = requireActivity().getSupportFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.from_right, R.anim.out_from_left);
        fragmentTransaction.replace(requireActivity().findViewById(R.id.frm_layout_auth).getId(), fragment);
        fragmentTransaction.commit();
    }

    private void togglePasswordMask(EditText password, ImageView toggle) {
        if (password.getTransformationMethod().equals(PasswordMaskTransformation.getInstance())) {
            password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            toggle.setImageResource(R.drawable.ic_hidden_pwd_24);
        } else {
            password.setTransformationMethod(PasswordMaskTransformation.getInstance());
            toggle.setImageResource(R.drawable.ic_show_pwd_24);
        }
    }

    private boolean checkInput(String email, String pasword) {
        boolean isValid = true;
        if (email == null || email.isEmpty()) {
            binding.etLoginEmail.setError("Email không được để trống!");
            isValid = false;
        }

        if (email != null && !email.matches("[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+")) {
            binding.etLoginEmail.setError("Emai sai định dạng!");
            isValid = false;
        }

        if (pasword == null || pasword.isEmpty()) {
            binding.etLoginPassword.setError("Mật khẩu không được để trống!");
            isValid = false;
        }

        return isValid;
    }

    private void signInWithFireBase() {
        String email = binding.etLoginEmail.getText().toString();
        String password = binding.etLoginPassword.getText().toString();
        binding.pbLoginProcess.setVisibility(View.VISIBLE);
        if (checkInput(email, password)) {
            mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        Intent intent = new Intent(getContext(), MainActivity.class);
                        startActivity(intent);
                    } else {
                        Toast.makeText(getContext(), task.getException().getMessage(),
                                Toast.LENGTH_SHORT).show();
                        binding.pbLoginProcess.setVisibility(View.INVISIBLE);
                    }
                }
            });
            binding.pbLoginProcess.setVisibility(View.INVISIBLE);
        }
    }

    private void signInWithGoogle() {
        Intent intent = gsc.getSignInIntent();
        startActivityForResult(intent, 20);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        binding.pbLoginProcess.setVisibility(View.VISIBLE);
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 20) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                var idToken = account.getIdToken();
                AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
                mAuth.signInWithCredential(credential)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    FirebaseUser firebaseUser = mAuth.getCurrentUser();
                                    User user = new User(firebaseUser.getUid(), firebaseUser.getDisplayName(), firebaseUser.getEmail(), null);
                                    userRepo.createUser(user);
                                    Intent intent = new Intent(requireActivity(), MainActivity.class);
                                    startActivity(intent);
                                } else {
                                    Toast.makeText(getContext(), task.getException().getMessage(),
                                            Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            } catch (ApiException e) {
                Toast.makeText(getContext(), "Đăng nhập vs google thất bại!", Toast.LENGTH_SHORT).show();
            }
        }
        binding.pbLoginProcess.setVisibility(View.INVISIBLE);
    }

}