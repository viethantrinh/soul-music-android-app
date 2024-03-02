package net.branium.fragments.main.user;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import net.branium.R;
import net.branium.fragments.main.user.UserFragment;

public class UserUpdateFragment extends Fragment {
    String userUID;
    MaterialButton mt_btn_update_user_account;
    EditText et_username_update;
    FirebaseAuth mAuth;
    FirebaseFirestore db;
    DocumentReference userRef;
    FirebaseUser firebaseUser;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_user_update, container, false);
        mt_btn_update_user_account = view.findViewById(R.id.mt_btn_update_user_account);
        et_username_update = view.findViewById(R.id.et_username_update);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        userUID = mAuth.getCurrentUser().getUid();
        userRef = db.collection("users").document(userUID);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        GetUserByUID();

        mt_btn_update_user_account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UpdateAccountUser();
            }
        });
    }

    private void UpdateAccountUser() {
        String username = et_username_update.getText().toString();
        // update username
        userRef.update("username", username);

        loadFragment(new UserFragment());
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
                if(documentSnapshot.exists()) {
                    et_username_update.setText(documentSnapshot.getString("username"));
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getContext(), "Lỗi: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}