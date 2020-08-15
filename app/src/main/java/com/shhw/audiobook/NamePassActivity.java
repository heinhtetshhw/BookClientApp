package com.shhw.audiobook;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class NamePassActivity extends AppCompatActivity {
    String id="";
    Button btnCreateAccount;
    FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_name_pass);
        btnCreateAccount = findViewById(R.id.btnCreateAccount);
        final EditText firstName = findViewById(R.id.firstName);
        final EditText lastName = findViewById(R.id.lastName);
        final TextView email = findViewById(R.id.email);
        mAuth = FirebaseAuth.getInstance();
        final Button btnUpdate = findViewById(R.id.btnUpdateAccount);
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        final CollectionReference ref = db.collection("user");
        final FirebaseUser user = mAuth.getCurrentUser();

        if(user!=null)
        {
            ref.whereEqualTo("email",user.getEmail())
                    .addSnapshotListener(new EventListener<QuerySnapshot>() {
                        @Override
                        public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                            User user = new User();
                            for(QueryDocumentSnapshot snapshot : value)
                            {
                                user = snapshot.toObject(User.class);
                                id = snapshot.getId();
                            }
                            firstName.setText(user.firstName);
                            lastName.setText(user.lastName);
                            email.setText(user.getEmail());
                            btnCreateAccount.setVisibility(View.GONE);
                            btnUpdate.setVisibility(View.VISIBLE);

                        }
                    });
            btnUpdate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    User newUser = new User();
                    newUser.firstName = firstName.getText().toString().trim();
                    newUser.lastName = lastName.getText().toString().trim();
                    newUser.email = user.getEmail();
                    ref.document(id).set(newUser);
                }
            });

        }


        btnCreateAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!firstName.getText().toString().trim().equals(""))
                {
                    if(!lastName.getText().toString().trim().equals(""))
                    {
                        FirebaseUser googleuser = mAuth.getCurrentUser();
                        User user = new User();
                        user.firstName = firstName.getText().toString().trim();
                        user.lastName = lastName.getText().toString().trim();
                        user.email =googleuser.getEmail();
                        ref.add(user);
                        firstName.setText("");
                        lastName.setText("");
                        email.setVisibility(View.GONE);
                        btnCreateAccount.setVisibility(View.VISIBLE);
                        btnUpdate.setVisibility(View.GONE);
                        Intent intent = new Intent(getApplicationContext(),HomeActivity.class);
                        startActivity(intent);
                        
                    }
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        mAuth.signOut();;
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        GoogleSignInClient mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        mGoogleSignInClient.signOut();
        mGoogleSignInClient.revokeAccess();
    }


}