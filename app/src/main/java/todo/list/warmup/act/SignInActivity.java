package todo.list.warmup.act;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import todo.list.warmup.R;
import todo.list.warmup.anim.Animate;
import todo.list.warmup.api.RestUser;
import todo.list.warmup.val.Validation;
import todo.list.warmup.view.Snack;

/**
 * Created by jrvansuita on 20/10/16.
 */

public class SignInActivity extends FragmentActivity implements View.OnClickListener, GoogleApiClient.OnConnectionFailedListener {

    private static final int RC_SIGN_IN = 990;

    private EditText edEmail;
    private EditText edPassword;
    private ProgressBar pbProgress;

    private FirebaseAuth auth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private GoogleApiClient googleApiClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_sign_in);

        findViewById(R.id.sign_in).setOnClickListener(this);
        findViewById(R.id.register).setOnClickListener(this);
        findViewById(R.id.use_anonymously).setOnClickListener(this);
        findViewById(R.id.google_sign_in).setOnClickListener(this);


        edEmail = (EditText) findViewById(R.id.email);
        edPassword = (EditText) findViewById(R.id.password);
        pbProgress = (ProgressBar) findViewById(R.id.progress);

        auth = FirebaseAuth.getInstance();

        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                final FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    RestUser.get().newUser(user.getUid()).setOnSucess(new RestUser.OnSucess() {
                        @Override
                        public void onSucess(String s) {
                            openNextActivity();
                        }
                    }).setOnError(new RestUser.OnError() {
                        @Override
                        public void onError(String s) {
                            showError(s);
                            auth.signOut();
                        }
                    });
                } else {
                    // User is signed out ou never did the sign up.
                }
            }
        };

        GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestProfile()
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        googleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, googleSignInOptions)
                .build();
    }

    @Override
    public void onStart() {
        super.onStart();
        auth.addAuthStateListener(authStateListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (authStateListener != null) {
            auth.removeAuthStateListener(authStateListener);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.sign_in:
                if (isEmailAndPasswordValid())
                    signInEmailAndPassword();
                break;

            case R.id.register:
                if (isEmailAndPasswordValid())
                    registerEmailAndPassword();

                break;

            case R.id.use_anonymously:
                signInAnonymously();
                break;

            case R.id.google_sign_in:
                signInGoogle();
                break;
        }
    }


    private boolean isEmailAndPasswordValid() {
        boolean ok = true;

        if (!Validation.isValidEmail(edEmail.getText().toString())) {
            edEmail.setError(getString(R.string.invalid_email));
            ok = false;
        }

        if (Validation.isEmpty(edPassword.getText().toString())) {
            edPassword.setError(getString(R.string.no_password));
            ok = false;
        }

        return ok;
    }


    private void signInEmailAndPassword() {
        showProgress();
        auth.signInWithEmailAndPassword(edEmail.getText().toString(), edPassword.getText().toString())
                .addOnCompleteListener(this, defaultTask);
    }


    private void registerEmailAndPassword() {
        showProgress();
        auth.createUserWithEmailAndPassword(edEmail.getText().toString(), edPassword.getText().toString())
                .addOnCompleteListener(this, defaultTask);
    }

    private void signInAnonymously() {
        showProgress();
        auth.signInAnonymously()
                .addOnCompleteListener(this, defaultTask);
    }

    private void signInGoogle() {
        showProgress();
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (result.isSuccess()) {
                GoogleSignInAccount acct = result.getSignInAccount();
                AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
                auth.signInWithCredential(credential)
                        .addOnCompleteListener(this, defaultTask);
            } else {
                showError(result.getStatus().getStatusMessage());
            }
        }
    }

    private OnCompleteListener<AuthResult> defaultTask = new OnCompleteListener<AuthResult>() {
        @Override
        public void onComplete(@NonNull Task<AuthResult> task) {
            if (!task.isSuccessful())
                showError(task.getException().getMessage());
        }
    };

    private void showError(String error) {
        hideProgress();
        Snack.show(edEmail, error);
    }

    private void showProgress() {
        Animate.builder(pbProgress, R.anim.fab_in).start(true);
    }

    private void hideProgress() {
        Animate.builder(pbProgress, R.anim.fab_out).start(false);
    }

    private void openNextActivity() {
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
        finish();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        showError(getString(R.string.conn_failed));
    }


}
