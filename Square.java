package com.example.minesweeper;

import android.graphics.Color;
import android.view.ViewDebug.FlagToString;
import android.widget.Button;
import android.widget.Toast;

public class Square {

    int x;
    int y;

    boolean isBomb;
    boolean isFlagged;
    boolean isClicked;

    Button sqrButton;

    Board b;

    public Square(Button btn, boolean isBomb, int x, int y, Board a) {
        this.sqrButton = btn;
        isFlagged = false;
        isClicked = false;
        this.isBomb = isBomb;
        this.x = x;
        this.y = y;
        b = a;

        btn.setText("");
    }

    public boolean onClick(Square[][] squares) {// return true if still in game,
        // false if loses
        System.out.println("clicking");
        if (isBomb)

        {
            isClicked = false;
            return false; // If dead, true if not dead.
        }

        if (isFlagged)
            return true;
        if (isClicked) {//TODO change the isFlagAround
            if (FlagAroundEqual(squares, this.sqrButton.getText().toString()))
                if (!openAroundClicked(squares))
                    b.gameActivity.createGameOverActivity(false);

        }
        isClicked = true;

        int bombsNearby = bombsNearby(squares);

        if (bombsNearby == 0) {
            openNearbySquares(squares);
            sqrButton.setText("-");
        } else {
            sqrButton.setText("" + bombsNearby);
        }

        return true;
    }

    private boolean FlagAroundEqual(Square[][] squares, String num) {
        int cnt = 0;
        for (int row = y - 1; row <= y + 1; row++) {
            for (int col = x - 1; col <= x + 1; col++) {
                if (row == y && col == x)
                    continue;
                try {
                    Square s = squares[row][col];
                    if (s.isFlagged) {
                        cnt++;
                    }
                } catch (Exception e) {
                }
            }
        }
        return (cnt + "").equals(num);
    }

    private boolean openAroundClicked(Square[][] squares) {
        Boolean inGame = true;
        for (int row = y - 1; row <= y + 1; row++) {
            for (int col = x - 1; col <= x + 1; col++) {
                if (row == y && col == x)
                    continue;
                try {
                    Square s = squares[row][col];
                    if (!s.isClicked && !s.isFlagged) {
                        if (!s.onClick(squares)) {
                            inGame = false;
                        }
                    }
                } catch (Exception e) {
                }
            }
        }
        return inGame;
    }

    public void openNearbySquares(Square[][] squares) {
        for (int row = y - 1; row <= y + 1; row++) {
            for (int col = x - 1; col <= x + 1; col++) {
                if (row == y && col == x)
                    continue;
                try {
                    Square s = squares[row][col];
                    if (!s.isClicked) {
                        s.onClick(squares);
                    }
                } catch (Exception e) {
                }
            }
        }
    }

    public int bombsNearby(Square[][] squares) {
        int result = 0;
        for (int row = y - 1; row <= y + 1; row++) {
            for (int col = x - 1; col <= x + 1; col++) {
                if (row == y && col == x)
                    continue;
                try {
                    if (squares[row][col].isBomb)
                        result++;
                } catch (Exception e) {
                }
            }
        }
        return result;
    }

    public void onFlag() {
        System.out.println("flagging");
        if (isClicked)
            return;
        isFlagged = !isFlagged;
        if (isFlagged)
            sqrButton.setBackgroundResource(R.drawable.flagborder);//-------------------------------
        else
            sqrButton.setBackgroundResource(R.drawable.borderline);//-------------------------------
    }

    public Button getButton() {
        if (sqrButton == null)
            System.out.println("NULLLLLLL");

        return sqrButton;
    }

    public boolean getIsBomb() {
        return isBomb;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}