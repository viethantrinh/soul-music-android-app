package net.branium.view.fragments.main;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import net.branium.R;

public class UserUpdateActivity extends AppCompatActivity {
    String userUID;
    MaterialButton mt_btn_update_user_account;
    MaterialButton mt_btn_back_user_account;
    EditText et_username_update;
    FirebaseAuth mAuth;
    FirebaseFirestore db;
    DocumentReference userRef;
    FirebaseUser firebaseUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_update);

        mt_btn_update_user_account = findViewById(R.id.mt_btn_update_user_account);
        mt_btn_back_user_account = findViewById(R.id.mt_btn_back_user_account);
        et_username_update = findViewById(R.id.et_username_update);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        userUID = mAuth.getCurrentUser().getUid();
        userRef = db.collection("users").document(userUID);


        GetUserByUID(); // lấy usernaem, email từ firebase

        mt_btn_update_user_account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UpdateAccountUser();
            }
        });
        mt_btn_back_user_account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void UpdateAccountUser() {
        String username = et_username_update.getText().toString();
        // update username
        userRef.update("username", username);
        Toast.makeText(getApplicationContext(), "Cập nhật thành công", Toast.LENGTH_SHORT).show();
    }

    private void GetUserByUID() {
        db.collection("users").document(userUID).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if(documentSnapshot.exists()) {
                    et_username_update.setText(documentSnapshot.getString("username"));
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(), "Lỗi: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}