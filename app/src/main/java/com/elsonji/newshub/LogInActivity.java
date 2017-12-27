package com.elsonji.newshub;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;

public class LogInActivity extends AppCompatActivity {

    private GoogleSignInClient mGoogleSignInClient;
    private static final int REQUEST_CODE_SIGN_IN = 101;
    public static final String GOOGLE_SIGN_IN_USED = "GOOGLE_SIGN_IN_USED";
    public static final String GOOGLE_SIGN_IN_SKIPPED = "GOOGLE_SIGN_IN_SKIPPED";
    private SharedPreferences mSignInStatusPref = null;
    private boolean mGoogleSignInUsed;
    private boolean mGoogleSignInSkipped = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);

        mSignInStatusPref = getSharedPreferences("GOOGLE_SIGN_IN_SKIPPED", MODE_PRIVATE);
        if (mSignInStatusPref != null) {
            mGoogleSignInSkipped = mSignInStatusPref.getBoolean(GOOGLE_SIGN_IN_SKIPPED, false);
        }


        if (!mGoogleSignInSkipped) {
            Button skipButton = findViewById(R.id.skip_button);
            skipButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mGoogleSignInUsed = false;
                    mGoogleSignInSkipped = true;

                    mSignInStatusPref = getSharedPreferences(GOOGLE_SIGN_IN_SKIPPED, MODE_PRIVATE);
                    SharedPreferences.Editor editor = mSignInStatusPref.edit();
                    editor.putBoolean(GOOGLE_SIGN_IN_SKIPPED, mGoogleSignInSkipped);
                    editor.apply();

                    Intent mainActivityIntent = new Intent(getApplicationContext(), NewsListActivity.class);
                    mainActivityIntent.putExtra(GOOGLE_SIGN_IN_USED, mGoogleSignInUsed);
                    startActivity(mainActivityIntent);
                }
            });

            // Configure Google Sign In
            GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestIdToken(getString(R.string.default_web_client_id))
                    .requestEmail()
                    .build();

            mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
            mGoogleSignInClient = GoogleSignInClientSingleton.getInstance(mGoogleSignInClient).getGoogleSignInClient();


            SignInButton signInButton = findViewById(R.id.sign_in_button);
            signInButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mGoogleSignInSkipped = false;

                    mSignInStatusPref = getSharedPreferences(GOOGLE_SIGN_IN_SKIPPED, MODE_PRIVATE);
                    SharedPreferences.Editor editor = mSignInStatusPref.edit();
                    editor.putBoolean(GOOGLE_SIGN_IN_SKIPPED, mGoogleSignInSkipped);
                    editor.apply();

                    signIn();
                }
            });
        }
    }

    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, REQUEST_CODE_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == REQUEST_CODE_SIGN_IN) {

            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);

        }
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);

            // Signed in successfully, show authenticated UI.
            if (account != null) {
                Intent mainActivityIntent = new Intent(getApplicationContext(), NewsListActivity.class);
                startActivity(mainActivityIntent);
            }
        } catch (ApiException e) {
            Toast.makeText(LogInActivity.this, "Authentication failed.",
                    Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        if (account != null) {
            Intent mainActivityIntent = new Intent(getApplicationContext(), NewsListActivity.class);
            startActivity(mainActivityIntent);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        mGoogleSignInSkipped = false;
    }

}
