package es.iespuertodelacruz.dam.gtdnow.activity;


import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import es.iespuertodelacruz.dam.gtdnow.R;

import es.iespuertodelacruz.dam.gtdnow.model.entity.Task;
import es.iespuertodelacruz.dam.gtdnow.utility.MenuListener;


public class MainActivity extends AppCompatActivity  {
    private EditText emailEditText;
    private EditText passEditText;
    private EditText repeatedPassEditText;
    private Switch isNewSwitch;
    private Button loginButton;
    private TextView recoverPassTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        emailEditText = findViewById(R.id.edittext_main_email);
        passEditText = findViewById(R.id.edittext_main_pass);
        repeatedPassEditText = findViewById(R.id.edittext_main_repeatedpass);
        isNewSwitch = findViewById(R.id.switch_main_isregistered);
        loginButton = findViewById(R.id.button_main_login);
        recoverPassTextView = findViewById(R.id.textview_main_recoverpass);

        isNewSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    repeatedPassEditText.setVisibility(View.VISIBLE);
                    loginButton.setText(R.string.main_button_register);
                    recoverPassTextView.setVisibility(View.GONE);
                }
                else{
                    repeatedPassEditText.setVisibility(View.GONE);
                    loginButton.setText(R.string.main_button_login);
                    recoverPassTextView.setVisibility(View.VISIBLE);
                }
            }
        });

        recoverPassTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "Se ha enviado un email a su correo electrónico para recuperar la contraseña", Toast.LENGTH_LONG).show();
            }
        });

    }

}
