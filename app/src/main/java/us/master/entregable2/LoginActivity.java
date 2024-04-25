package us.master.entregable2;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.transition.AutoTransition;
import androidx.transition.Transition;
import androidx.transition.TransitionManager;
import androidx.transition.TransitionSet;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import us.master.entregable2.entities.User;
import us.master.entregable2.services.FirebaseDatabaseService;
import us.master.entregable2.services.LocalPreferences;

public class LoginActivity extends AppCompatActivity {
    private static final int REQUEST_CODE_SIGN_IN = 0x456;
    private FirebaseAuth mAuth;
    private Button signinButtonGoogle;
    private Button signinButtonMail;
    private Button loginButtonSignup;
    private ProgressBar progressBar;
    private TextInputLayout loginEmail;
    private EditText loginEmailEt;
    private TextInputLayout loginPass;
    private EditText loginPassEt;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        FirebaseDatabaseService.resetUserId();

        mAuth = FirebaseAuth.getInstance();

        signinButtonGoogle = findViewById(R.id.login_button_google);
        signinButtonMail = findViewById(R.id.login_button_mail);
        loginButtonSignup = findViewById(R.id.login_button_register);
        progressBar = findViewById(R.id.login_progress);
        loginEmail = findViewById(R.id.login_email);
        loginEmailEt = findViewById(R.id.login_email_et);
        loginPass = findViewById(R.id.login_pass);
        loginPassEt = findViewById(R.id.login_pass_et);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        signinButtonMail.setOnClickListener(v -> attemptLoginEmail());

        signinButtonGoogle.setOnClickListener(v -> attemptLoginGoogle(gso));

        loginButtonSignup.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
            intent.putExtra(RegisterActivity.EMAIL_PARAM, loginEmailEt.getText().toString());
            startActivity(intent);
        });
    }

    private void attemptLoginEmail() {
        loginEmail.setError(null);
        loginPass.setError(null);

        if (loginEmailEt.getText().length() == 0) {
            loginEmail.setError(getString(R.string.login_error_mail_empty));
            loginEmail.requestFocus();
            return;
        } else if (loginPassEt.getText().length() == 0) {
            loginPass.setError(getString(R.string.login_error_pass_empty));
            loginPass.requestFocus();
            return;
        } else {
            signInEmail();
        }
    }

    private void attemptLoginGoogle(GoogleSignInOptions gso) {
        GoogleSignInClient mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, REQUEST_CODE_SIGN_IN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                assert account != null;
                AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
                if (mAuth != null) {
                    mAuth.signInWithCredential(credential).addOnCompleteListener(this, task1 -> {
                        if (task1.isSuccessful()) {
                            FirebaseUser user = mAuth.getCurrentUser();
                            checkUserDatabaseLogin(user);
                        } else {
                            showErrorDialogueMail();
                        }
                    });
                }
                else {
                    showGooglePlayServicesError();
                }
            } catch (ApiException e) {
                showErrorDialogueMail();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void signInEmail() {
        if (mAuth == null) {
            mAuth = FirebaseAuth.getInstance();
        }

        if (mAuth != null) {
            mAuth.signInWithEmailAndPassword(loginEmailEt.getText().toString(), loginPassEt.getText().toString())
                    .addOnCompleteListener(this, task -> {
                        if (!task.isSuccessful() || task.getResult().getUser() == null) {
                            showErrorDialogueMail();
                        } else if (!task.getResult().getUser().isEmailVerified()) {
                            showErrorEmailVerified(task.getResult().getUser());
                        } else {
                            FirebaseUser user = task.getResult().getUser();
                            checkUserDatabaseLogin(user);
                        }
                    });
        }
        else {
            showGooglePlayServicesError();
        }
    }

    private void showGooglePlayServicesError() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(getString(R.string.login_error_google_play_services))
                .setPositiveButton(getString(R.string.login_error_google_play_services_yes), (dialog, which) -> {
                    dialog.dismiss();
                })
                .setNegativeButton(getString(R.string.login_error_google_play_services_no), (dialog, which) -> dialog.dismiss())
                .create()
                .show();
    }

    private void checkUserDatabaseLogin(FirebaseUser firebaseUser) {
        FirebaseDatabaseService.getServiceInstance().getUser().addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                if (user == null) {
                    user = new User();
                    user.set_id(firebaseUser.getUid());
                    user.setEmail(firebaseUser.getEmail());
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
                    String dateString = sdf.format(Calendar.getInstance().getTime());
                    user.setCreated(dateString);
                    FirebaseDatabaseService.getServiceInstance().saveUser(user);
                }
                LocalPreferences.getLocalPreferencesInstance().saveLocalUserInformation(LoginActivity.this, user);
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                showErrorDialogueMail();
            }
        });
    }

    private void showErrorEmailVerified(FirebaseUser user) {
        hideLoginButton(false);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(getString(R.string.login_error_email_verified))
                .setPositiveButton(getString(R.string.login_error_email_verified_yes), (dialog, which) -> {
                    user.sendEmailVerification().addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Snackbar.make(signinButtonMail, getString(R.string.register_verification_mail_sent), Snackbar.LENGTH_SHORT).show();
                        } else {
                            Snackbar.make(signinButtonMail, getString(R.string.register_verification_mail_not_sent), Snackbar.LENGTH_SHORT).show();
                        }
                    });
                    dialog.dismiss();
                })
                .setNegativeButton(getString(R.string.login_error_email_verified_no), (dialog, which) -> dialog.dismiss())
                .create()
                .show();
    }

    private void showErrorDialogueMail() {
        hideLoginButton(false);
        Snackbar.make(signinButtonMail, getString(R.string.login_error_mail), Snackbar.LENGTH_SHORT).show();
    }

    private void hideLoginButton(boolean hide) {
        TransitionSet transitionSet = new TransitionSet();
        Transition layoutFade = new AutoTransition();
        layoutFade.setDuration(1000);
        transitionSet.addTransition(layoutFade);

        if (hide) {
            TransitionManager.beginDelayedTransition(findViewById(R.id.login_layout), transitionSet);
            progressBar.setVisibility(View.VISIBLE);
            signinButtonMail.setVisibility(View.GONE);
            signinButtonGoogle.setVisibility(View.GONE);
            loginButtonSignup.setVisibility(View.GONE);
            loginEmail.setEnabled(false);
            loginPass.setEnabled(false);
        } else {
            progressBar.setVisibility(View.GONE);
            signinButtonMail.setVisibility(View.VISIBLE);
            signinButtonGoogle.setVisibility(View.VISIBLE);
            loginButtonSignup.setVisibility(View.VISIBLE);
            loginEmail.setEnabled(true);
            loginPass.setEnabled(true);
        }
    }
}
