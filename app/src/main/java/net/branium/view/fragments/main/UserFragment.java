package net.branium.view.fragments.main;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import net.branium.R;
import net.branium.databinding.FragmentUserBinding;
import net.branium.model.Playlist;
import net.branium.repository.UserRepository;
import net.branium.utils.Constants;
import net.branium.view.activities.AuthActivity;
import net.branium.view.adapters.HomeLovePlaylistAdapter;
import net.branium.viewmodel.HomeFragmentViewModel;

import java.util.List;

public class UserFragment extends Fragment implements View.OnClickListener {
    private UserRepository userRepo = new UserRepository();
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FragmentUserBinding binding;
    private HomeLovePlaylistAdapter homeLovePlaylistAdapter;
    private HomeFragmentViewModel homeFragmentViewModel;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_user, container, false);
        binding.tvUsernameAccount.setText(mAuth.getCurrentUser().getDisplayName());
        binding.tvEmailAccount.setText(mAuth.getCurrentUser().getEmail());
        registerEventListener();
        getUserLovePlaylist();
        return binding.getRoot();
    }


    @Override
    public void onClick(View v) {
        var id = v.getId();
        if (binding.mtBtnUpdateAccount.getId() == id) {
            Intent intent = new Intent(getActivity(), UserUpdateActivity.class);
            startActivityForResult(intent, 0);
        } else if (binding.mtBtnLogout.getId() == id) {
            showConfirmationDialog("Đăng xuất", "Bạn có chắc chắn muốn đăng xuất?", "Đăng xuất", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    logoutAccount();
                }
            });
        } else if (binding.mtBtnDeleteAccount.getId() == id) {
            showConfirmationDialog("Xác nhận xóa", "Bạn có chắc chắn muốn xóa?", "Xóa", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    deleteUserAccount();
                }
            });
        }
    }

    private void deleteUserAccount() {
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (firebaseUser != null) {
            firebaseUser.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        Toast.makeText(getContext(), "Xóa tài khoản thành công", Toast.LENGTH_SHORT).show();
                        userRepo.deleteUser(firebaseUser.getUid());
                        Intent intent = new Intent(requireActivity(), AuthActivity.class);
                        startActivity(intent);
                    } else {
                        Toast.makeText(getContext(), "Lỗi: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    private void logoutAccount() {
        mAuth.signOut();
        Toast.makeText(getContext(), "Đăng xuất thành công", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(requireActivity(), AuthActivity.class);
        startActivity(intent);
    }


    private void registerEventListener() {
        binding.mtBtnUpdateAccount.setOnClickListener(this);
        binding.mtBtnLogout.setOnClickListener(this);
        binding.mtBtnDeleteAccount.setOnClickListener(this);
    }

    private void showConfirmationDialog(String title, String message, String button, DialogInterface.OnClickListener positiveClickListener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle(title);
        builder.setMessage(message);
        // Thiết lập nút xác nhận và màu sắc cho nó
        builder.setPositiveButton(button, positiveClickListener);
        builder.setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // Hủy bỏ thao tác xóa
                dialog.dismiss();
            }
        });
        // Tạo và hiển thị AlertDialog
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0) {
            binding.tvUsernameAccount.setText("" + mAuth.getCurrentUser().getDisplayName());
        }
    }

    private void getUserLovePlaylist() {
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        String currentUserId = firebaseUser.getUid();
        homeFragmentViewModel = new ViewModelProvider(this).get(HomeFragmentViewModel.class);
        homeFragmentViewModel.getAllUserPlaylist(currentUserId).observe(requireActivity(), new Observer<List<Playlist>>() {
            @Override
            public void onChanged(List<Playlist> playlists) {
                homeLovePlaylistAdapter = new HomeLovePlaylistAdapter(Constants.USER_PLAYLIST_LIST, requireContext());
                binding.recyclerViewUserLovePlaylistInUserFrag.setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false));
                binding.recyclerViewUserLovePlaylistInUserFrag.setAdapter(homeLovePlaylistAdapter);
                homeLovePlaylistAdapter.notifyDataSetChanged();
            }
        });
    }
}