package com.example.minesweeper;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Vibrator;
import android.provider.ContactsContract;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class GameOverActivity extends Activity {

    String playerName;

    int difficulty;

    private ImageView imageView;
    private MyThread thread = null;
    public Drawable[] imageArray = new Drawable[6];
    boolean didWin;

    int gameSeconds;
    int score;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_over);

        Bundle extras = getIntent().getExtras();
        imageView = (ImageView) findViewById(R.id.ivAnimat);

		for (int i=1; i<=6; i++)
		{
			String n="ani"+i;
			imageArray[i-1]= getResources().getDrawable(getResources().getIdentifier(n,"drawable", getPackageName()));
		}
		imageView.setVisibility(View.INVISIBLE);

        playerName = extras.getString("name");
        difficulty = extras.getInt("difficulty"); // 1=easy, 2=medium, 3=hard
        didWin = extras.getBoolean("didWin");
        gameSeconds = extras.getInt("time");
        TextView et = (TextView) findViewById(R.id.tv1);

        if (didWin) {
            score = (int) 1000000.0 * difficulty / gameSeconds;
            et.setText("Congratulations " + playerName + " you won! Time: " + gameSeconds + " seconds. Score:" + score);
        } else {
            et.setText("You suck, " + playerName + ", better luck next time! Time: " + gameSeconds + " seconds");
        }
        addToFile();
        vibrate();
        animation();
    }

    public void animation() {
        imageView.setVisibility(View.VISIBLE);
        imageView.setImageDrawable(imageArray[0]);
        thread = new MyThread(this);
        thread.start();
    }

    public void changeFrame(int num) {
        imageView.setImageDrawable(imageArray[num]);
    }

    //הפעולה מקבלת את הכפתור שחוזר ומחזירה אותך למשחק
    public void back(View v) {
        Intent i = new Intent();
        i.putExtra("goToMain", false);
        setResult(RESULT_OK, i);
        finish();
    }

    //הפעולה מקבלת את הכפתור שחוזר לתפריט הראשי ומחזירה אותך לשם
    public void mainmenu(View v) {
        Intent i = new Intent();
        i.putExtra("goToMain", true);
        setResult(RESULT_OK, i);
        finish();
    }

    public void addToFile() {
        if (!didWin)
            return;
        String stats = FileManager.readFromFile("leaderboardStats");
        stats += playerName + "," + score + ":";
        FileManager.writeToFile("leaderboardStats", stats);
    }

    private void vibrate() {
        Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        if (vibrator.hasVibrator()) {
            vibrator.vibrate(500);
        } else {
            Toast.makeText(getApplicationContext(), "Vibrating", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onDestroy() {
        if(thread != null){
            thread.stopRunning();
        }
        super.onDestroy();
    }
}