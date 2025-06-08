package com.example.minesweeper;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import java.io.File;

public class MainActivity extends Activity {
    int difficulty;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FileManager.setPath(getFilesDir() + "");
    }

    //הפעולה מקבלת את הכפתור שמתחיל את המשחק ומתחילה את המשחק לפי הרמה שבחר המשתמש
    public void onClickStart(View v) {
        final String[] listItems = {"Easy", "Medium", "Hard"};


        AlertDialog.Builder myBuilder = new AlertDialog.Builder(MainActivity.this);
        myBuilder.setTitle("Choose difficulty");
        myBuilder.setSingleChoiceItems(listItems, -1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                difficulty = i + 1;
                dialogInterface.dismiss();
                startGame();
            }
        });
        AlertDialog myDialog = myBuilder.create();
        myDialog.show();
    }

    public void startGame() {
        Intent i = new Intent(this, GameActivity.class);
        EditText et = (EditText) findViewById(R.id.etName);
        String name = et.getText().toString();
        i.putExtra("Randomize", true);
        i.putExtra("Name", name);
        i.putExtra("Difficulty", difficulty);
        startActivity(i);
    }

    //הפעולה מקבלת את הכפתור שפותח את ההישגים ומציגה אותם
    public void openLeaderboard(View v) {
        Intent i = new Intent(this, LeaderboardActivity.class);
        startActivity(i);
    }

    // הפעולה מקבלת את הכפתור שמוחק את ההישגים ומוחק אותם
    public void resetLeaderboard(View v) {
        String before = FileManager.readFromFile("leaderboardStats");
        if (before == null || before.equals("")) {
            Toast.makeText(getApplicationContext(), "Already Cleared", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getApplicationContext(), "Success", Toast.LENGTH_SHORT).show();
            FileManager.writeToFile("leaderboardStats", "");
        }
    }
}