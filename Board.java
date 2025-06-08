package com.example.minesweeper;

import java.util.ArrayList;

import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

public class Board implements OnClickListener {
    GameActivity gameActivity;
    Square[][] board;
    private boolean won = false;

    public Board(Button[][] buttons, int row, int col, int bombs,
                 GameActivity ga) {
        Log.d("111", "in Board Constructor");
        gameActivity = ga;
        board = new Square[row][col];
        System.out.println("row=" + row + ", col=" + col);
        createBoard(buttons, bombs);

    }


    public void createBoard(Button[][] buttons, int bombs) {
        Log.d("111", "in Create Board");

        ArrayList<Boolean> mines = new ArrayList<Boolean>();
        if (bombs != 0) {
            for (int i = 0; i < bombs; i++) {
                mines.add(true);
            }
            for (int i = 0; i < buttons.length * buttons[0].length - bombs; i++) {
                mines.add(false);
            }

            mines = shuffle(mines);
        }
        boolean isBomb;
        for (int row = 0; row < board.length; row++) {
            for (int col = 0; col < board[0].length; col++) {
                if (bombs == 0) {
                    isBomb = false;
                } else {
                    isBomb = mines.get(0);
                }
                board[row][col] = new Square(buttons[row][col], isBomb, col, row, this);
                if (bombs != 0) {
                    mines.remove(0);
                }
            }
        }
        Log.d("111", "done Create Board");
//		if(board[0][0].sqrButton==null)
//			System.out.println("WTF");
//		for(int row=0;row<board.length;row++)
//			for(int col=0;col<board[0].length;col++)
//				System.out.println(board[row][col].sqrButton.getHint());	
    }

    private ArrayList<Boolean> shuffle(ArrayList<Boolean> mines) {
        int size = mines.size();
        ArrayList<Boolean> result = new ArrayList<Boolean>();
        while (size > 0) {
            int randomPlace = (int) (Math.random() * mines.size());
            result.add(mines.get(randomPlace));
            mines.remove(randomPlace);
            size--;
        }
        return result;
    }

    @Override
    public void onClick(View v) {
        if (won) {
            return;
        }
        Button btn = (Button) v;
        System.out.println("CLICK---------------------");

        Log.d("1111", "in onClick");

        if (btn == null)
            System.out.println("BTN IS NULL ------------");

        if (!(v instanceof Button))
            return;


        Square square = getSquareByButton(btn);

        if (square == null) {
            System.out.println("Rip?");
            return;
        }

        if (gameActivity.isFlagMode())
            square.onFlag();
        else if (!square.onClick(board)) {
            gameActivity.createGameOverActivity(false);
            //Toast.makeText(gameActivity.getApplicationContext(), "MINE", Toast.LENGTH_SHORT).show();
        }

        if (checkWin()) {
            won = true;
            gameActivity.running = false;
            gameActivity.createGameOverActivity(true);
        }
    }


    public Square getSquareByButton(Button btn) {
        for (int row = 0; row < board.length; row++) {
            for (int col = 0; col < board[0].length; col++) {
                if (board[row][col].getButton() == btn) {
                    return board[row][col];
                }
            }
        }
        System.out.println("not putting in");
        return null;
    }

    public Square getSquareByLocation(int x, int y) {
        return board[x][y];
    }

    public boolean checkWin() {
        boolean won = true;
        for (int row = 0; row < board.length; row++)
            for (int col = 0; col < board[0].length; col++)
                if (!(board[row][col].isBomb)) {
                    if (!board[row][col].isClicked)
                        won = false;
                }
        return won;
    }
}