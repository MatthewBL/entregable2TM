package us.master.entregable2;

import android.os.Bundle;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;

public class RegisterActivity extends AppCompatActivity {

    public static final String EMAIL_PARAM = "EMAIL_PARAM";

    private EditText login_email_et;
    private EditText login_pass_et;
    private EditText login_pass_confirmation_et;

    private TextInputLayout login_email;
    private TextInputLayout login_pass;
    private TextInputLayout login_pass_confirmation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        String emailParam = getIntent().getStringExtra(EMAIL_PARAM);

        login_email_et = findViewById(R.id.login_email_et);
        login_pass_et = findViewById(R.id.login_pass_et);
        login_pass_confirmation_et = findViewById(R.id.login_pass_confirmation_et);

        login_email = findViewById(R.id.login_email);
        login_pass = findViewById(R.id.login_pass);
        login_pass_confirmation = findViewById(R.id.login_pass_confirmation);

        login_email_et.setText(emailParam);

        findViewById(R.id.signin_button_mail).setOnClickListener(v -> {
            String email = login_email_et.getText().toString();
            String pass = login_pass_et.getText().toString();
            String passConfirmation = login_pass_confirmation_et.getText().toString();

            if (email.isEmpty()) {
                login_email.setError("El correo es obligatorio");
                return;
            }

            if (pass.isEmpty()) {
                login_pass.setError("La contraseña es obligatoria");
                return;
            }

            if (!pass.equals(passConfirmation)) {
                login_pass_confirmation.setError("Las contraseñas no coinciden");
                return;
            }

            FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, pass)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {

                            finish();
                        } else {
                            login_email.setError("Error al registrar el usuario");
                        }
                    });
        });
    }
}