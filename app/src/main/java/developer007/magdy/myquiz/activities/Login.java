package developer007.magdy.myquiz.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import developer007.magdy.myquiz.R;
import developer007.magdy.myquiz.database.SharedPrefManager;

public class Login extends AppCompatActivity {
    EditText etPassword, etName;
    AppCompatButton btnLogin;
    private String password, userName;
    private int id;
    private SharedPrefManager sharedPrefManager;
    private String key;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        etName = findViewById(R.id.etName);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);
        key = "id";

        sharedPrefManager = new SharedPrefManager();
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userName = "" + etName.getText().toString().toLowerCase().trim();
                password = "" + etPassword.getText().toString().trim();
                if (userName.isEmpty()) {
                    Toast.makeText(Login.this, "Enter the User Name", Toast.LENGTH_LONG).show();
                    return;
                }
                if (password.isEmpty()) {
                    Toast.makeText(Login.this, "Enter the Password", Toast.LENGTH_LONG).show();
                    return;


                }
                if (password.length() < 6) {
                    Toast.makeText(Login.this, "Short password", Toast.LENGTH_LONG).show();
                    return;


                }

                if (userName.contains("admin") && password.equals("123456")) {
                    startActivity(new Intent(Login.this, AdminActivity.class));
                } else if (userName.equals("student1")
                        && password.equals("123456")) {

                    SharedPrefManager.setPrefVal(Login.this, key, "1");
                    SharedPrefManager.setPrefVal(Login.this, "userName", userName);
                    startActivity(new Intent(Login.this, StudentActivity.class));
                } else if (userName.equals("student2") && password.equals("123456")) {


                    SharedPrefManager.setPrefVal(Login.this, key, "2");
                    SharedPrefManager.setPrefVal(Login.this, "userName", userName);

                    startActivity(new Intent(Login.this, StudentActivity.class));

                } else if (userName.equals("student3") && password.equals("123456")) {


                    SharedPrefManager.setPrefVal(Login.this, key, "3");
                    SharedPrefManager.setPrefVal(Login.this, "userName", userName);

                    startActivity(new Intent(Login.this, StudentActivity.class));

                } else {
                    Toast.makeText(Login.this, "The user name or password is not correct", Toast.LENGTH_LONG).show();

                }

            }
        });

    }
}