package net.branium.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import net.branium.R;


public class ResetPasswordFragment extends Fragment {

    TextView tvBackToLogin;
    FrameLayout frmLayoutAuth;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_reset_password, container, false);
        tvBackToLogin = view.findViewById(R.id.tv_back_to_login);
        frmLayoutAuth = requireActivity().findViewById(R.id.frm_layout_auth);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        tvBackToLogin.setOnClickListener(v -> setFragment(new SignInFragment()));
    }

    private void setFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = requireActivity().getSupportFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.from_left, R.anim.out_from_right);
        fragmentTransaction.replace(frmLayoutAuth.getId(), fragment);
        fragmentTransaction.commit();
    }
}
