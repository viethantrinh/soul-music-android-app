package net.branium.view.fragments.main;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import net.branium.R;
import net.branium.view.activities.AuthActivity;

public class UserFragment extends Fragment {
    String userUID;
    MaterialButton mt_btn_delete_account;
    MaterialButton mt_btn_logout;
    MaterialButton mt_btn_update_account;
    TextView tv_username_account;
    TextView tv_email_account;
    FirebaseAuth mAuth;
    FirebaseFirestore db;
    DocumentReference userRef;
    FirebaseUser firebaseUser;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_user, container, false);

        mt_btn_logout = view.findViewById(R.id.mt_btn_logout);
        mt_btn_delete_account = view.findViewById(R.id.mt_btn_delete_account);
        mt_btn_update_account = view.findViewById(R.id.mt_btn_update_account);
        tv_email_account = view.findViewById(R.id.tv_email_account);
        tv_username_account = view.findViewById(R.id.tv_username_account);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        userUID = mAuth.getCurrentUser().getUid();
        userRef = db.collection("users").document(userUID);

//        ConstraintLayout constraintLayout = view.findViewById(R.id.fragment_user_container);
//        constraintLayout.setMaxHeight(100);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        GetUserByUID();

        mt_btn_update_account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), UserUpdateActivity.class);
                startActivity(intent);
            }
        });

        mt_btn_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showConfirmationDialog("Đăng xuất", "Bạn có chắc chắn muốn đăng xuất?", "Đăng xuất", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        LogoutAccount();
                    }
                });
            }
        });

        mt_btn_delete_account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showConfirmationDialog("Xác nhận xóa", "Bạn có chắc chắn muốn xóa?", "Xóa", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        DeleteUserAccount();
                    }
                });
            }
        });
    }

    private void loadFragment(Fragment fragment) {
        FragmentManager manager = getActivity().getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.body_container, fragment);
        transaction.commit();
    }

    private void GetUserByUID() {
        db.collection("users").document(userUID).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                    tv_username_account.setText(documentSnapshot.getString("username"));
                    tv_email_account.setText(documentSnapshot.getString("email"));
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getContext(), "Lỗi: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void DeleteUserAccount() {
        // xóa trong firebase
        userRef.delete();

        // xóa trong authentication
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (firebaseUser != null) {
            firebaseUser.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        // Xóa thành công
                        Toast.makeText(getContext(), "Xóa tài khoản thành công", Toast.LENGTH_SHORT).show();

                        // Chuyển đến màn hình đăng nhập hoặc màn hình khác nếu cần
                        Intent intent = new Intent(getContext(), AuthActivity.class);
                        startActivity(intent);
                    } else {
                        Toast.makeText(getContext(), "Lỗi: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    private void LogoutAccount() {
        mAuth.signOut();
        Toast.makeText(getContext(), "Đăng xuất thành công", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(getContext(), AuthActivity.class);
        startActivity(intent);
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


}