package ir.myapp.controller3.activity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.widget.EditText;
import android.widget.Toast;

import ir.myapp.controller3.R;
import ir.myapp.controller3.settings.Settings;

public class PasswordActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paswword);
        final String password = new Settings(this).getPassword();


        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Password");

        builder.setPositiveButton(android.R.string.ok, null);
        builder.setNegativeButton(android.R.string.cancel, null);

// Set up the input
        final EditText input = new EditText(this);
// Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
        input.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
        builder.setView(input);

// Set up the buttons
        final AlertDialog dialog = builder.create();

        builder.setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String enteredPass = input.getText().toString();

                if (enteredPass.length() == 0 || !enteredPass.equals(password)) {
                    Toast.makeText(
                            getApplicationContext(),
                            "Password wrong", Toast.LENGTH_LONG).show();
                    return;
                }else{
                    Intent intent = new Intent(PasswordActivity.this, MainActivity.class);
                    startActivity(intent);
                }
            }
        });

        builder.show();
    }
}
