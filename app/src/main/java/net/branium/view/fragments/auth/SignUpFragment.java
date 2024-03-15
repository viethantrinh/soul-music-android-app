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
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

import net.branium.R;
import net.branium.databinding.FragmentSignUpBinding;
import net.branium.model.User;
import net.branium.repository.UserRepository;
import net.branium.view.activities.MainActivity;


public class SignUpFragment extends Fragment implements View.OnClickListener {
    private User user = new User();
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private UserRepository userRepo = new UserRepository();
    private FragmentSignUpBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_sign_up, container, false);
        binding.setUser(user);
        registerClickEvent();
        return binding.getRoot();
    }

    @Override
    public void onClick(View v) {
        var id = v.getId();
        if (binding.tvAlreadyHaveAccount.getId() == id) {
            setFragment(new SignInFragment());
        } else if (binding.ivRegisterShowPwd.getId() == id) {
            togglePasswordMask(binding.etRegisterPassword, binding.ivRegisterShowPwd);
        } else if (binding.ivRegisterShowRePwd.getId() == id) {
            togglePasswordMask(binding.etRegisterRePassword, binding.ivRegisterShowRePwd);
        } else if (binding.mtBtnConfirmRegister.getId() == id) {
            signUpWithFireBase();
        }
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

    private void signUpWithFireBase() {
        String username = user.getUsername();
        String email = user.getEmail();
        String password = user.getPassword();
        String rePassword = binding.etRegisterRePassword.getText().toString();
        if (checkInputs(username, email, password, rePassword)) {
            binding.pbRegisterProcess.setVisibility(View.VISIBLE);
            mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        FirebaseUser firebaseUser = mAuth.getCurrentUser();
                        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                .setDisplayName(username)
                                .build();
                        firebaseUser.updateProfile(profileUpdates)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            user.setId(firebaseUser.getUid());
                                            userRepo.createUser(user); // save to postgres
                                            Intent intent = new Intent(requireActivity(), MainActivity.class);
                                            startActivity(intent);
                                        }
                                    }
                                });
                    } else {
                        Toast.makeText(getActivity(), task.getException().getMessage(),
                                Toast.LENGTH_SHORT).show();
                    }
                }
            });
            binding.pbRegisterProcess.setVisibility(View.INVISIBLE);
        }
    }

    private boolean checkInputs(String username, String email, String password, String rePassword) {
        boolean valid = true;
        if (username == null || username.isBlank() || username.isEmpty()) {
            binding.etRegisterUsername.setError("Tên đăng nhập không được để trống!");
            valid = false;
        }

        if (email == null || email.isBlank() || email.isEmpty()) {
            binding.etRegisterEmail.setError("Email không được để trống!");
            valid = false;
        }

        if (email != null && !email.matches("[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+")) {
            binding.etRegisterEmail.setError("Email không đúng định dạng!");
            valid = false;
        }

        if (password == null || password.isBlank() || password.isEmpty()) {
            binding.etRegisterPassword.setError("Mật khẩu không được để trống!");
            valid = false;
        }

        if (password != null && password.length() < 6) {
            binding.etRegisterPassword.setError("Độ dài mật khẩu tối thiểu 6 kí tự!");
            valid = false;
        }

        if (rePassword == null || rePassword.isBlank() || rePassword.isEmpty()) {
            binding.etRegisterRePassword.setError("Mật khẩu xác nhận không được để trống!");
            valid = false;
        }

        if (password != null && rePassword != null && !rePassword.equals(password)) {
            binding.etRegisterRePassword.setError("Mật khẩu xác nhận không trùng khớp!");
            valid = false;
        }

        return valid;
    }

    private void registerClickEvent() {
        binding.tvAlreadyHaveAccount.setOnClickListener(this);
        binding.ivRegisterShowPwd.setOnClickListener(this);
        binding.ivRegisterShowRePwd.setOnClickListener(this);
        binding.mtBtnConfirmRegister.setOnClickListener(this);
    }

    private void setFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = requireActivity().getSupportFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.from_left, R.anim.out_from_right);
        fragmentTransaction.replace(requireActivity().findViewById(R.id.frm_layout_auth).getId(), fragment);
        fragmentTransaction.commit();
    }

}