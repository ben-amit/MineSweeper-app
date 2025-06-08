package com.example.minesweeper;

import android.app.Activity;
import android.app.ApplicationErrorReport.CrashInfo;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.prefs.Preferences;

public class GameActivity extends Activity {
    // android:layout_marginRight="-7.7dp"

    static GameActivity ga;
    static final float bombsPercentage = 0.35f;

    final int maxRows = 10;
    final int maxCols = 10;
    String playerName;

    private TextView batteryState;
    private BroadcastReceiver batRec= new BroadcastReceiver()
    {
        @Override
        public void onReceive(Context context, Intent intent)
        {
            int lvl= intent.getIntExtra(BatteryManager.EXTRA_LEVEL +"", 0);
            batteryState.setText(String.valueOf(lvl)+"%");
        }
    };

    int seconds;
    boolean running;

    int difficulty;

    Board board;

    int rows;
    int cols;

    boolean isFlagMode = false;

    boolean didWin;// TODO check if he won - not sure if i did it right
    // (checkWin)
//הפעולה מקבלת את המצב של המשחק ופותחת אותו
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ga = this;
        Log.d("111", "in OnCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        batteryState = (TextView) findViewById(R.id.batteryState);
        this.registerReceiver(this.batRec, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));

        running = true;
        runTimer();
        
        Bundle extras = getIntent().getExtras();

        playerName = extras.getString("Name");
        difficulty = extras.getInt("Difficulty"); // 1=easy, 2=medium,
        // 3=hard

        createBoard(true);

        findViewById(R.id.flagBtn).setBackgroundResource(R.drawable.mine);
        
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.game_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onMenuItemSelected(int featureId, MenuItem item) {
        switch(item.getItemId()){
            case R.id.save:
                save();
                break;
            case R.id.load:
                load();
                break;
        }
        return super.onMenuItemSelected(featureId, item);
    }
//הפעולה מתחילה את הטיימר
    private void runTimer() {
        final TextView textView = (TextView) findViewById(R.id.timer);
        final Handler handler = new Handler();
        handler.post(new Runnable() {
            @Override
            public void run() {
                int hours = seconds / 3600;
                int minutes = (seconds % 3600) / 60;
                int sec = seconds % 60;
                String time = String.format("%d:%02d:%02d", hours, minutes, sec);
                textView.setText(time);
                if(running) {
                    seconds++;
                }
                handler.postDelayed(this, 1000);
            }
        });

    }
//הפעולה מקבלת משתנה בוליאני שקובע אם לשים פצצות ומכינה את הלוח בהתאם
    public void createBoard(boolean putBombs) {
        if (difficulty == 1) {
            rows = 6;
            cols = 6;
        }
        if (difficulty == 2) {
            rows = 8;
            cols = 8;
        }
        if (difficulty == 3) {
            rows = 10;
            cols = 10;
        }

        Button[][] allButtons = new Button[rows][cols];
        for (int i = 0; i < maxRows; i++) {
            for (int k = 0; k < maxCols; k++) {
                String str = "btn" + i + "_" + k;
                int resID = getResources().getIdentifier(str, "id",
                        "com.example.minesweeper");
                Button btn = (Button) findViewById(resID);
                btn.setBackgroundResource(R.drawable.borderline);
                btn.setHint("");
                if (i >= rows || k >= cols) {
                    btn.setVisibility(View.GONE);
                    continue;
                }
                btn.setVisibility(View.VISIBLE);
                allButtons[i][k] = btn;
            }
        }

        int bombs = putBombs ? (int) (rows * cols * bombsPercentage) : 0; //bombs amount
        board = new Board(allButtons, rows, cols,
                bombs, this);

        for (int i = 0; i < rows; i++) {
            for (int k = 0; k < cols; k++) {
                allButtons[i][k].setOnClickListener(board);
            }
        }
    }

    public void onFlag(View v) {
        if(!running){
            return;
        }
        if (isFlagMode)
            findViewById(R.id.flagBtn).setBackgroundResource(R.drawable.mine);
        else
            findViewById(R.id.flagBtn).setBackgroundResource(R.drawable.flag);

        isFlagMode = !isFlagMode;
    }

    public boolean isFlagMode() {
        return isFlagMode;
    }

    public void createGameOverActivity(boolean didWin) {
        Intent i = new Intent(this, GameOverActivity.class);
        i.putExtra("name", playerName);
        i.putExtra("difficulty", difficulty);
        i.putExtra("didWin", didWin);
        i.putExtra("time",seconds);
        startActivityForResult(i, 1);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                if (data.getExtras().getBoolean("goToMain")) {
                    finish();
                }
            } else {
                System.err.println("Bad result code.");
            }
        }
    }

    public void save() {
        if(!running){
            Toast.makeText(getApplicationContext(), "Can't save after you win!", Toast.LENGTH_LONG).show();
            return;
        }
        String s = gameToString();
        FileManager.writeToFile("game", s);
    }

    public void load() {
        String s = FileManager.readFromFile("game");
        importGame(s);
    }
//הפעולה טוענת את המשחק
    private void importGame(String string) {
        String[] splittedString = string.split(":");
        seconds = Integer.parseInt(splittedString[0]);
        difficulty = Integer.parseInt(splittedString[1]);
        createBoard(false);
        String boardInfo = splittedString[2];
        String[] lines = boardInfo.split(",");
        int row = 0;

        for (String line : lines) {
            int col = 0;
            for (char c : line.toCharArray()) {
                Square square = board.getSquareByLocation(row, col);
                if (c == 'f') {
                    square.isFlagged = true;
                    square.isBomb = false;
                    square.isClicked = false;

                    square.getButton().setBackgroundResource(R.drawable.flagborder);
                }
                if (c == 'F') {
                    square.isFlagged = true;
                    square.isBomb = true;
                    square.isClicked = false;

                    square.getButton().setBackgroundResource(R.drawable.flagborder);
                }
                if (c == 'E') {
                    square.isFlagged = false;
                    square.isBomb = true;
                    square.isClicked = false;
                }
                if (c == 'e') {
                    square.isFlagged = false;
                    square.isBomb = false;
                    square.isClicked = false;
                }
                if (c > '0' && c < '9') { //c is 1-8
                    square.isFlagged = false;
                    square.isBomb = false;
                    square.isClicked = true;
                    square.getButton().setText("" + c);
                }

                col++;
            }
            row++;
        }
    }
//הפעולה שומרת את המשחק
    private String gameToString() { //F flagged with bomb, f flagged without bomb, E empty (not clicked) with bomb, e empty (not clicked) without bomb
       String time = ((TextView) findViewById(R.id.timer)).getText().toString();
      String[]arrSec = time.split(":");
     int gameSeconds = 3600*Integer.parseInt(arrSec[0])+60*Integer.parseInt(arrSec[1])+Integer.parseInt(arrSec[2]);
        String result = gameSeconds+":"+ difficulty + ":";
        Square square;
        for (int row = 0; row < rows; row++) {
            if (row != 0) {
                result += ",";
            }
            for (int col = 0; col < cols; col++) {
                square = board.getSquareByLocation(row, col);

                if (square.isClicked) {
                    result += square.getButton().getText().toString();
                    continue;
                }

                if (square.isBomb) {
                    if (square.isFlagged) {
                        result += "F";
                    } else {
                        result += "E";
                    }
                } else {
                    if (square.isFlagged) {
                        result += "f";
                    } else {
                        result += "e";
                    }
                }
            }
        }
        return result;
    }
}