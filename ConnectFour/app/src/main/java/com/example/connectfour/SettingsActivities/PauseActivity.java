package com.example.connectfour.SettingsActivities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.connectfour.Board.VsPlayerGameBoard;
import com.example.connectfour.R;
import com.example.connectfour.StartScreenActivity;

public class PauseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if(AppCompatDelegate.getDefaultNightMode()==AppCompatDelegate.MODE_NIGHT_YES) {
            setTheme(R.style.DarkTheme);;
        }
        else {
            setTheme(R.style.AppTheme);
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pause);

        ImageButton sound = (ImageButton) findViewById(R.id.sound);

        sound.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(PauseActivity.this, "Sound Off", Toast.LENGTH_SHORT).show();
            }
        });

        /**
         * Takes player to the main screen when icon clicked
         */
        ImageButton home = findViewById(R.id.home);
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent returnHome = new Intent(PauseActivity.this, StartScreenActivity.class);
                startActivity(returnHome);
            }
        });

        /**
         * Takes player to the settings menu when icon clicked
         */
        ImageButton options = findViewById(R.id.settings);
        options.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent changeSettings = new Intent(PauseActivity.this, SettingsActivity.class);
                startActivity(changeSettings);
            }
        });

        /**
         * Takes player back to the game board when icon clicked
         */
        Button resume = findViewById(R.id.resume);
        resume.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}
