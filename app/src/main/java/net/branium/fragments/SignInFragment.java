package net.branium.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.button.MaterialButton;

import net.branium.R;

import java.util.Objects;

public class SignInFragment extends Fragment {
    MaterialButton mtBtnRegister;
    TextView tvForgetPassword;
    FrameLayout frmLayoutAuth;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_sign_in, container, false);
        mtBtnRegister = view.findViewById(R.id.mt_btn_register);
        tvForgetPassword = view.findViewById(R.id.tv_forget_password);
        frmLayoutAuth = requireActivity().findViewById(R.id.frm_layout_auth);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Xử lý sự kiện khi nhấn quên mật khẩu
        tvForgetPassword.setOnClickListener(v -> setFragment(new ResetPasswordFragment()));

        // Xử lý sự kiện khi nhấn nút đăng ký
        mtBtnRegister.setOnClickListener(v -> setFragment(new SignUpFragment()));
    }

    private void setFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = requireActivity().getSupportFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.from_right, R.anim.out_from_left);
        fragmentTransaction.replace(frmLayoutAuth.getId(), fragment);
        fragmentTransaction.commit();
    }
}