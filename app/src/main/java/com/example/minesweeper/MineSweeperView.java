package com.example.minesweeper;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.View;

import java.util.Random;

public class MineSweeperView extends View implements OnClickCanvas {
    private int index1 = 10, index2 = 10;
    private Rect square[][] = new Rect[index1][index2];
    private int field[][] = new int[index1][index2];
    private String clicked[][] = new String[index1][index2];
    private Canvas canvas;
    private Context context;
    private Rect tempRect;
    private boolean check = false, gameEnd = false;
    private int xAxis = 0, yAxis = 0, tIndex1, tIndex2;
    private Drawable drawable, drawableGrass;

    public MineSweeperView(Context context) {
        super(context);
        this.context = context;
        drawable = getResources().getDrawable(R.drawable.ic_bomb);
        drawableGrass = getResources().getDrawable(R.drawable.ic_grass);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        this.canvas = canvas;
        int width = canvas.getWidth();
        int height = canvas.getHeight();
        int left = 0, right = width / index1, top = 0, bottom = height / index2;
        int tempWidth = width / index1, tempHeight = height / index2;
        Paint paint = new Paint();
        paint.setStrokeWidth(3);
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(Color.BLACK);
        paint.setTextSize(40);
        if (check == false)
            plantMines();
        if (gameEnd) {
            for (int n = 0; n < index1; n++)
                for (int m = 0; m < index2; m++) {
                    canvas.drawRect(square[n][m], paint);
                    if (field[n][m] == 100) {
                        drawable.setBounds(square[n][m]);
                        drawable.draw(canvas);
                    } else if (field[n][m] == 0)
                        canvas.drawText("", square[n][m].left, square[n][m].top, paint);
                    else
                        canvas.drawText(field[n][m] + "", square[n][m].left + 20, square[n][m].bottom - 20, paint);
                }
            return;
        }
        for (int i = 0; i < index1; i++) {
            for (int j = 0; j < index2; j++) {
                square[i][j] = new Rect(left, top, right, bottom);
                Rect rect = square[i][j];
                canvas.drawRect(square[i][j], paint);
                //Check if this field was already clicked
                if (check && clicked[i][j].equals("clicked")) {
                    if (field[i][j] == 0)
                        canvas.drawText("", square[i][j].left, square[i][j].top, paint);
                    else
                        canvas.drawText(field[i][j] + "", square[i][j].left + 20, square[i][j].bottom - 20, paint);
                } else {
                    if (!(check && i == tIndex1 && j == tIndex2)) {
                        drawableGrass.setBounds(rect.left, rect.top, rect.right - 5, rect.bottom - 5);
                        drawableGrass.draw(canvas);
                    } else {
                        if (field[tIndex1][tIndex2] == 100) {
                            gameEnd = true;
                            invalidate();
                        } else {
                            if (field[tIndex1][tIndex2] == 0) {
                                canvas.drawText("", tempRect.left, tempRect.top, paint);

                            } else {
                                canvas.drawText(field[tIndex1][tIndex2] + "", tempRect.left + 20, tempRect.bottom - 20, paint);
                            }
                        }
                        clicked[tIndex1][tIndex2] = "clicked";
                    }
                }
                left = left + tempWidth;
                right = right + tempWidth;
            }
            left = 0;
            right = tempWidth;
            top = top + tempHeight;
            bottom = bottom + tempHeight;
        }
    }
    @Override
    public void onClickOnCanvas(int x, int y) {
        if (xAxis != x && yAxis != y) {
            xAxis = x;
            yAxis = y;

            for (int i = 0; i < index1; i++) {
                for (int j = 0; j < index2; j++) {
                    Rect rect = square[i][j];
                    if (x >= rect.left && x <= rect.right && y >= rect.top && y <= rect.bottom) {
                        tempRect = square[i][j];
                        tIndex1 = i;
                        tIndex2 = j;
                        check = true;
                        invalidate();
                        return;
                    }
                }
            }
        } else
            return;
    }

    private void plantMines() {
        // checking if clicked
        for (int i = 0; i < index1; i++)
            for (int j = 0; j < index2; j++)
                clicked[i][j] = "";


        Random rand = new Random();
        for (int i = 0; i < 20; i++) {
            int indexS = rand.nextInt((9 - 0) + 1) + 0;
            int indexF = rand.nextInt((9 - 0) + 1) + 0;
            field[indexF][indexS] = 100;
        }
        plantSafeAreas();
    }

    private void plantSafeAreas() {
        //For First Row
        for (int i = 0; i < index1; i++) {
            int mines = 0;
            if (field[0][i] != 100) {
                if (i != 0) {
                    if (field[0][i - 1] == 100)
                        mines++;
                    if (field[1][i - 1] == 100)
                        mines++;
                }
                if (i < index1 - 1) {
                    if (field[0][i + 1] == 100)
                        mines++;
                    if (field[1][i + 1] == 100)
                        mines++;
                }
                if (field[1][i] == 100)
                    mines++;
                if (mines != 0)
                    field[0][i] = mines;
            }
        }
        //For First Column
        for (int i = 1; i < index2 - 2; i++) {
            int mines = 0;
            if (field[i][0] != 100) {
                if (i != 0) {
                    if (field[i - 1][0] == 100)
                        mines++;
                    if (field[i - 1][1] == 100)
                        mines++;
                }
                if (i < index1 - 1) {
                    if (field[i + 1][0] == 100)
                        mines++;
                    if (field[i + 1][1] == 100)
                        mines++;
                }
                if (field[i][1] == 100)
                    mines++;
                if (mines != 0)
                    field[i][0] = mines;
            }
        }
        //For Last Row
        for (int i = 0; i < index1; i++) {
            int mines = 0;
            if (i != 0) {
                if (field[index1 - 1][i] != 100) {
                    if (field[index1 - 1][i - 1] == 100)
                        mines++;
                    if (field[index1 - 2][i - 1] == 100)
                        mines++;
                }
                if (i < index1 - 1) {
                    if (field[index1 - 1][i + 1] == 100)
                        mines++;
                    if (field[index1 - 2][i + 1] == 100)
                        mines++;
                }
                if (field[index1 - 2][i] == 100)
                    mines++;
                if (mines != 0)
                    field[index1 - 1][i] = mines;
            }
        }
        //For Last Column
        for (int i = 1; i < index2 - 2; i++) {
            int mines = 0;

            if (field[i][0] != 100) {
                if (i != 0) {
                    if (field[i - 1][index1 - 1] == 100)
                        mines++;

                    if (field[i - 1][index1 - 2] == 100)
                        mines++;
                }
                if (field[i][index1 - 2] == 100)
                    mines++;
                if (i < index1 - 1) {
                    if (field[i + 1][index1 - 2] == 100)
                        mines++;
                    if (field[i + 1][index1 - 1] == 100)
                        mines++;
                }
                if (mines != 0)
                    field[i][index1 - 1] = mines;
            }
        }

        for (int i = 1; i < index1 - 1; i++) {
            for (int j = 1; j < index2 - 1; j++) {
                int bombs = 0;
                if (field[i][j] != 100) {
                    for (int n = i - 1; n <= i + 1; n++) {
                        for (int m = j - 1; m <= j + 1; m++) {
                            if (field[n][m] == 100)
                                bombs++;
                        }
                    }
                    if (bombs != 0)
                        field[i][j] = bombs;
                }
            }
        }
    }
}
