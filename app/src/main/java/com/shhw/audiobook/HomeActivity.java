package com.shhw.audiobook;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class HomeActivity extends AppCompatActivity {

    private GoogleSignInClient mGoogleSignInClient;
    ImageView icon;
    DrawerLayout DrawerLayout;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

      



        mAuth = FirebaseAuth.getInstance();
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        NavigationView navView = findViewById(R.id.navView);
        navView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                if(item.getItemId()==R.id.login)
                {

                    FirebaseUser user = mAuth.getCurrentUser();
                    if(user!=null)
                    {
                        signOut();
                    }
                    else {

                        Intent intent = new Intent(getApplicationContext(),LoginActivity.class);
                        startActivity(intent);

                    }
                }
                if(item.getItemId()==R.id.support)
                {
                    Intent intent = new Intent(getApplicationContext(),ChatActivity.class);
                    startActivity(intent);
                }
                return false;
            }
        });



    }




    private FirebaseAuth mAuth;


    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser!=null) {
            updateUI(currentUser);
        }
    }

    private void updateUI(FirebaseUser user) {
        NavigationView navView = findViewById(R.id.navView);
        View myView = navView.getHeaderView(0);
        ImageView icon = myView.findViewById(R.id.icon);
        final TextView name = myView.findViewById(R.id.name);
        Glide.with(getApplicationContext())
                .load(user.getPhotoUrl())
                .into(icon);

        FirebaseFirestore db =FirebaseFirestore.getInstance();
        CollectionReference ref = db.collection("user");
        ref.whereEqualTo("email",user.getEmail())
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException error) {
                        for(QueryDocumentSnapshot snapshot : queryDocumentSnapshots)
                        {
                            User user1 = snapshot.toObject(User.class);
                            name.setText("Hey\n"+user1.firstName+" "+user1.getLastName());
                        }

                    }
                });

        navView.getMenu().getItem(5).setTitle("LogOut");

    }
    public void signOut()
    {
        mAuth.signOut();
        mGoogleSignInClient.signOut();
        mGoogleSignInClient.revokeAccess();
        updateUIRollaback();
    }

    private void updateUIRollaback() {
        NavigationView navView = findViewById(R.id.navView);
        View myView = navView.getHeaderView(0);
        ImageView icon = myView.findViewById(R.id.icon);
        TextView name = myView.findViewById(R.id.name);
        icon.setImageResource(R.drawable.tom);
        name.setText("Hey\n"+"Tom");
        navView.getMenu().getItem(5).setTitle("LogIn");

    }


    public void editButton(View view) {
        Intent intent = new Intent(getApplicationContext(),NamePassActivity.class);
        startActivity(intent);






    }
}
