package com.ma.nova.othello_try_3;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Point;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.util.ArrayMap;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import java.util.logging.Handler;

/**
 * Created by user on 2015/10/23.
 */


public class GameView extends GridLayout {

    private Bitmap white, black, white_hint, black_hint;
    int cardWidth;
    Button btn_newGame;
    TextView white_count, black_count;
    int white_num = 0;
    int black_num = 0;
    boolean turn = false;
    private Coordinates cardsCoordinate[][];
    private SoundPool soundPool;
    private int music;


    public GameView(Context context) {
        super(context);
        initGameView();
    }

    public GameView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initGameView();
    }

    public GameView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initGameView();
    }

    public GameView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initGameView();
    }

    private void initGameView() {

        setColumnCount(8);
        setBackgroundColor(getResources().getColor(R.color.back4));
        white = BitmapFactory.decodeResource(getResources(), R.drawable.white_chess);
        black = BitmapFactory.decodeResource(getResources(), R.drawable.black_chess);
        white_hint = BitmapFactory.decodeResource(getResources(), R.drawable.white_chess_t);
        black_hint = BitmapFactory.decodeResource(getResources(), R.drawable.black_chess_t);

        soundPool = new SoundPool(10, AudioManager.STREAM_SYSTEM,5);
        music = soundPool.load(MainActivity.getMainActivity(),R.raw.putdown,1);
    }


    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        cardWidth = (Math.min(w, h)) / 8;
        addCards(cardWidth, cardWidth);
        initGameCell();
        saveCardsCoordinates();
        restartGame();
        Hint();
        showTurn(turn);
    }

    public void initGameCell() {

        cardsMap[3][3].imageView.setImageBitmap(white);
        cardsMap[4][4].imageView.setImageBitmap(white);
        cardsMap[3][4].imageView.setImageBitmap(black);
        cardsMap[4][3].imageView.setImageBitmap(black);
        whitePoint.add(new Point(3, 3));
        whitePoint.add(new Point(4, 4));
        blackPoint.add(new Point(3, 4));
        blackPoint.add(new Point(4, 3));
        cardsMap[3][3].setNum(1);
        cardsMap[4][4].setNum(1);
        cardsMap[3][4].setNum(2);
        cardsMap[4][3].setNum(2);
        btn_hint = false;
        MainActivity.getMainActivity().btn_hint.setText("Hints OFF");
        MainActivity.getMainActivity().btn_hint.setBackgroundResource(R.drawable.mainpagebtn);
    }

    private void addCards(int cardWidth, int cardHeight) {

        Cards c;
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {

                c = new Cards(getContext());
                c.setNum(0);
                addView(c, cardWidth, cardHeight);
                cardsMap[i][j] = c;
            }
        }
    }

    private Cards cardsMap[][] = new Cards[8][8];
    private List<Point> whitePoint = new ArrayList<Point>();
    private List<Point> blackPoint = new ArrayList<Point>();


    //获取触点坐标


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        super.onTouchEvent(event);
        int touch_x = (int) event.getX();
        int touch_y = (int) event.getY();
        touchResponse(touch_x, touch_y);
        return false;
    }

    //判断格子是不是空的，如果不是空的就不能放棋子在那儿
    public boolean checkIsNull(int x, int y) {
        if (cardsMap[x][y].getNum() <= 0) {
            return true;
        } else {
            return false;
        }
    }



    //遍历所有的格子，每个格子都判断能否落子，如果全部不能落子，就返回false，换另一方，如果有能落子的地方，就返回true；

    public boolean isAnyMove(boolean turn) {
        int j = 0;

        for (int i = 0; i < 8; i++) {
            while (j < 8) {

                if (checkIsNull(i, j) && isAllowedPoint(i, j, turn)) {

                    return true;
                }
                j++;

            }
            j = 0;
        }


        return false;
    }


    public void refresh() {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (cardsMap[i][j].getNum() <= 0) {
                    cardsMap[i][j].imageView.setImageBitmap(null);
                }
            }
        }
    }

    //点击的格子里放棋子
    public void touchResponse(int x, int y) {


        if (findPoint(x, y) != null) {
            Cell position = findPoint(x, y);


            if (checkIsNull(position.getX(), position.getY())) {
                if (turn) {
                    if (isAllowedPoint(position.getX(), position.getY(), turn)) {
                        PutDown(position.getX(), position.getY(), turn);
                        cardsMap[position.getX()][position.getY()].imageView.setImageBitmap(white);
                        cardsMap[position.getX()][position.getY()].setNum(1);
                        whitePoint.add(new Point(position.getX(), position.getY()));

                        soundPool.play(music,1,1,1,0,1);

                        refresh();

                        turn = false;

                        showTurn(turn);

                    }


                } else if (!turn) {
                    if (isAllowedPoint(position.getX(), position.getY(), turn)) {
                        PutDown(position.getX(), position.getY(), turn);
                        cardsMap[position.getX()][position.getY()].imageView.setImageBitmap(black);
                        cardsMap[position.getX()][position.getY()].setNum(2);
                        blackPoint.add(new Point(position.getX(), position.getY()));
                        soundPool.play(music, 1, 1, 1, 0, 1);
                        refresh();
                        turn = true;
                        showTurn(turn);
                    }
                }
                if (btn_hint) {
                    DoHint();
                }
                if (!isAnyMove(turn)) {
                    turn = !turn;
                    showTurn(turn);
                    if (btn_hint) {
                        DoHint();
                    }
                }
                if (!isAnyMove(turn)) {
                    if(!isAnyMove(!turn)) {
                        GameOver();
                    }
                }

            }
            showCount();
        }


    }


    public void GameOver() {
        showCount();
        String winner = white_num > black_num ? "white wins!" : white_num == black_num ? "Draw!" : "black wins!";

        String gameTime = MainActivity.getMainActivity().timer.getText().toString();
       RecordPage recordPage = new RecordPage();
        recordPage.addrecord(winner,gameTime);
        new AlertDialog.Builder(MainActivity.getMainActivity()).setTitle("Game Over").setMessage(winner + "\n" + "Time:"+gameTime).setPositiveButton("New Game", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                whitePoint.clear();
                blackPoint.clear();
                turn = false;
                for (int i = 0; i < 8; i++) {
                    for (int j = 0; j < 8; j++) {
                        cardsMap[i][j].setNum(0);
                        cardsMap[i][j].imageView.setImageBitmap(null);
                    }
                }
                initGameCell();
                showCount();
                showTurn(turn);
                MainActivity.getMainActivity().timer.setText("0:0");
                MainActivity.getMainActivity().getTime();


            }
        }).setNeutralButton("Record", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(MainActivity.getMainActivity(),RecordPage.class);
                MainActivity.getMainActivity().startActivity(intent);


            }
        }).show();

    }


    public void showTurn(boolean turn) {
        MainActivity.getMainActivity().showTurn(turn);
    }

    public void restartGame() {
        MainActivity.getMainActivity().btn_newGame.setOnClickListener(l);
    }

    btn_listener l = new btn_listener();

    class btn_listener implements OnClickListener {

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btn_newGame:
                    whitePoint.clear();
                    blackPoint.clear();
                    turn = false;
                    for (int i = 0; i < 8; i++) {
                        for (int j = 0; j < 8; j++) {
                            cardsMap[i][j].setNum(0);
                            cardsMap[i][j].imageView.setImageBitmap(null);
                        }
                    }
                    initGameCell();
                    showCount();
                    showTurn(turn);
                    MainActivity.getMainActivity().timer.setText("0:0");
                    MainActivity.getMainActivity().getTime();

                    break;
                case R.id.btn_hint:
                    btn_hint = !btn_hint;
                    if (btn_hint) {
                        MainActivity.getMainActivity().btn_hint.setBackgroundResource(R.drawable.mainpagebtnlighter);

                        MainActivity.getMainActivity().btn_hint.setText(R.string.hint_on);
                        DoHint();
                    } else {
                        MainActivity.getMainActivity().btn_hint.setBackgroundResource(R.drawable.mainpagebtn);
                        MainActivity.getMainActivity().btn_hint.setText(R.string.hint_off);
                        refresh();
                    }
                    break;
            }
        }
    }


    boolean btn_hint = false;

    public void Hint() {
        MainActivity.getMainActivity().btn_hint.setOnClickListener(l);
    }

    public void DoHint() {
        int j = 0;

        for (int i = 0; i < 8; i++) {
            while (j < 8) {

                if (checkIsNull(i, j) && isAllowedPoint(i, j, turn)) {

                    if (turn)
                        cardsMap[i][j].imageView.setImageBitmap(white_hint);
                    else
                        cardsMap[i][j].imageView.setImageBitmap(black_hint);
                }
                j++;

            }
            j = 0;
        }

    }

    //数棋盘上有多少个白棋，多少个黑棋
    public void showCount() {

        white_num = whitePoint.size();
        black_num = blackPoint.size();
        MainActivity.getMainActivity().showNum(white_num, black_num);

    }

    //把格子四个点的坐标存在数组里
    public void saveCardsCoordinates() {
        cardsCoordinate = new Coordinates[8][8];
        int increase = cardWidth;
        int start_x = 5;
        int start_y = 5;
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                Point a = new Point(start_x, start_y);
                Point b = new Point(start_x + cardWidth, start_y);
                Point c = new Point(start_x + cardWidth, start_y + cardWidth);
                Point d = new Point(start_x, start_y + cardWidth);
                try {
                    cardsCoordinate[i][j] = new Coordinates(a, b, c, d);


                } catch (Exception e) {
                    Log.i("error", e.getMessage());
                }

                start_x += cardWidth;

            }
            start_x = 5;
            start_y += cardWidth;
        }

    }

    //寻找触点坐标所在的格子
    public Cell findPoint(int x, int y) {

        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (match(cardsCoordinate[i][j], x, y)) {

                    return new Cell(i, j);
                }
            }
        }
        return null;
    }

    //匹配触点坐标和格子坐标
    public boolean match(Coordinates position, int x, int y) {
        if ((x > position.getPoint(0).x && y > position.getPoint(0).y) && (x < position.getPoint(2).x && y < position.getPoint(2).y)) {
            return true;
        } else {
            return false;
        }
    }

    int a, b;

    int m, n;

    //  boolean moveUp = false, moveDown = false, moveLeft = false, moveRight = false, moveRightUp = false, moveRightDown = false, moveLeftUp = false, moveLeftDown = false;


    public boolean checkRight(int x, int y, boolean turn) {

//横着往右找
        if (turn) {
            a = 2;
            b = 1;
        } else if (!turn) {
            b = 2;
            a = 1;
        }
        int j = y + 1;
        if (cardsMap[x][y + 1].getNum() == a) {

            while (j < 8) {
                if (cardsMap[x][j].getNum() == b) {
                    for (int k = y + 1; k < j; k++) {
                        if (cardsMap[x][k].getNum() == 0) {

                            return false;
                        }
                    }
                    m = j;
                    return true;
                }
                j++;
            }
        }
        return false;
    }

    public void goRight(int x, int y, boolean turn) {

        if (checkRight(x, y, turn)) {
            for (int i = y + 1; i < m; i++) {
                if (turn) {
                    cardsMap[x][i].imageView.setImageBitmap(white);
                    cardsMap[x][i].setNum(1);
                    blackPoint.remove(new Point(x, i));
                    whitePoint.add(new Point(x, i));

                }
                if (!turn) {
                    cardsMap[x][i].imageView.setImageBitmap(black);
                    cardsMap[x][i].setNum(2);
                    whitePoint.remove(new Point(x, i));
                    blackPoint.add(new Point(x, i));
                }

            }

        }
    }


    public boolean checkLeft(int x, int y, boolean turn) {
        //moveLeft = false;
        //横着往左找
        if (turn) {
            a = 2;
            b = 1;
        } else if (!turn) {
            b = 2;
            a = 1;
        }
        int j = y - 1;
        if (cardsMap[x][y - 1].getNum() == a) {

            while (j > -1) {
                if (cardsMap[x][j].getNum() == b) {
                    for (int k = y - 1; k > j; k--) {
                        if (cardsMap[x][k].getNum() == 0) {

                            return false;
                        }
                    }
                    m = j;
                    return true;
                }
                j--;
            }
        }


        return false;
    }

    public void goLeft(int x, int y, boolean turn) {

        if (checkLeft(x, y, turn)) {
            for (int i = m + 1; i < y; i++) {
                if (turn) {
                    cardsMap[x][i].imageView.setImageBitmap(white);
                    cardsMap[x][i].setNum(1);
                    blackPoint.remove(new Point(x, i));
                    whitePoint.add(new Point(x, i));

                }
                if (!turn) {
                    cardsMap[x][i].imageView.setImageBitmap(black);
                    cardsMap[x][i].setNum(2);
                    whitePoint.remove(new Point(x, i));
                    blackPoint.add(new Point(x, i));
                }

            }

        }
    }

    public boolean checkDown(int x, int y, boolean turn) {
        //  moveDown = false;
        //竖着往下找
        if (turn) {
            a = 2;
            b = 1;
        } else if (!turn) {
            b = 2;
            a = 1;
        }
        int j = x + 1;
        if (cardsMap[x + 1][y].getNum() == a) {

            while (j < 8) {
                if (cardsMap[j][y].getNum() == b) {

                    for (int k = x + 1; k < j; k++) {
                        if (cardsMap[k][y].getNum() == 0) {

                            return false;
                        }
                    }
                    m = j;
                    return true;
                }
                j++;
            }
        }
        //      return m;
        return false;

    }

    public void goDown(int x, int y, boolean turn) {
        //     m=checkDown(x,y,turn);
        if (checkDown(x, y, turn)) {
            for (int i = x + 1; i < m; i++) {
                if (turn) {
                    cardsMap[i][y].imageView.setImageBitmap(white);
                    cardsMap[i][y].setNum(1);
                    whitePoint.add(new Point(i, y));
                    blackPoint.remove(new Point(i, y));
                }
                if (!turn) {
                    cardsMap[i][y].imageView.setImageBitmap(black);
                    cardsMap[i][y].setNum(2);
                    whitePoint.remove(new Point(i, y));
                    blackPoint.add(new Point(i, y));
                }

            }

        }
    }


    public boolean checkUp(int x, int y, boolean turn) {
        //   moveUp = false;
        //竖着往上找
        if (turn) {
            a = 2;
            b = 1;
        } else if (!turn) {
            b = 2;
            a = 1;
        }
        int j = x - 1;
        if (cardsMap[x - 1][y].getNum() == a) {

            while (j > -1) {
                if (cardsMap[j][y].getNum() == b) {
                    for (int k = x - 1; k > j; k--) {
                        if (cardsMap[k][y].getNum() == 0) {

                            return false;
                        }
                    }
                    m = j;
                    return true;
                }
                j--;
            }
        }

        return false;
//return m;
    }

    public void goUp(int x, int y, boolean turn) {
        //   m=checkUp(x,y,turn);
        if (checkUp(x, y, turn)) {
            for (int i = m + 1; i < x; i++) {
                if (turn) {
                    cardsMap[i][y].imageView.setImageBitmap(white);
                    cardsMap[i][y].setNum(1);
                    whitePoint.add(new Point(i, y));
                    blackPoint.remove(new Point(i, y));
                }
                if (!turn) {
                    cardsMap[i][y].imageView.setImageBitmap(black);
                    cardsMap[i][y].setNum(2);
                    whitePoint.remove(new Point(i, y));
                    blackPoint.add(new Point(i, y));
                }

            }

        }
    }

    public boolean checkRightUP(int x, int y, boolean turn) {
        //  moveRightUp = false;
        //往右上找
        if (turn) {
            a = 2;
            b = 1;
        } else if (!turn) {
            b = 2;
            a = 1;
        }
        int i = x - 1;
        int j = y + 1;
        if (cardsMap[x - 1][y + 1].getNum() == a) {

            while (i > -1 && j < 8) {
                if (cardsMap[i][j].getNum() == b) {
                    int k = i, c = j;
                    while (k < x - 1 && c > y + 1) {
                        if (cardsMap[k][c].getNum() == 0) {
                            return false;
                        }
                        k++;
                        c--;
                    }
                    m = j;
                    n = i;
                    return true;
                }
                j++;
                i--;
            }
        }
        return false;

    }

    public void goRightUp(int x, int y, boolean turn) {
        if (checkRightUP(x, y, turn)) {
            n = n + 1;
            m = m - 1;
            while (n < x && m > y) {
                if (turn) {
                    cardsMap[n][m].imageView.setImageBitmap(white);
                    cardsMap[n][m].setNum(1);
                    whitePoint.add(new Point(n, m));
                    blackPoint.remove(new Point(n, m));
                }
                if (!turn) {
                    cardsMap[n][m].imageView.setImageBitmap(black);
                    cardsMap[n][m].setNum(2);
                    whitePoint.remove(new Point(n, m));
                    blackPoint.add(new Point(n, m));
                }
                n++;
                m--;
            }


        }
    }

    public boolean checkRightDown(int x, int y, boolean turn) {
//往右下找

        if (turn) {
            a = 2;
            b = 1;
        } else if (!turn) {
            b = 2;
            a = 1;
        }
        int i = x + 1;
        int j = y + 1;
        if (cardsMap[x + 1][y + 1].getNum() == a) {

            while (i < 8 && j < 8) {
                if (cardsMap[i][j].getNum() == b) {
                    int k = i, c = j;
                    while (k > x + 1 && c > y + 1) {
                        if (cardsMap[k][c].getNum() == 0) {
                            return false;
                        }
                        k--;
                        c--;
                    }
                    m = j;
                    n = i;

                    return true;
                }
                j++;
                i++;
            }
        }
        return false;
    }


    public void goRightDown(int x, int y, boolean turn) {
        if (checkRightDown(x, y, turn)) {
            n = n - 1;
            m = m - 1;
            while (n > x && m > y) {
                if (turn) {
                    cardsMap[n][m].imageView.setImageBitmap(white);
                    cardsMap[n][m].setNum(1);
                    whitePoint.add(new Point(n, m));
                    blackPoint.remove(new Point(n, m));
                }
                if (!turn) {
                    cardsMap[n][m].imageView.setImageBitmap(black);
                    cardsMap[n][m].setNum(2);
                    whitePoint.remove(new Point(n, m));
                    blackPoint.add(new Point(n, m));
                }
                n--;
                m--;
            }


        }
    }

    public boolean checkLeftUP(int x, int y, boolean turn) {
//往左上找

        if (turn) {
            a = 2;
            b = 1;
        } else if (!turn) {
            b = 2;
            a = 1;
        }
        int i = x - 1;
        int j = y - 1;
        if (cardsMap[x - 1][y - 1].getNum() == a) {

            while (i > -1 && j > -1) {
                if (cardsMap[i][j].getNum() == b) {
                    int k = i, c = j;
                    while (k < x - 1 && c < y - 1) {
                        if (cardsMap[k][c].getNum() == 0) {
                            return false;
                        }
                        k++;
                        c++;
                    }
                    m = j;
                    n = i;
                    return true;
                }
                j--;
                i--;
            }
        }

        return false;
    }

    public void goLeftUp(int x, int y, boolean turn) {
        if (checkLeftUP(x, y, turn)) {
            n = n + 1;
            m = m + 1;
            while (n < x && m < y) {
                if (turn) {
                    cardsMap[n][m].imageView.setImageBitmap(white);
                    cardsMap[n][m].setNum(1);
                    whitePoint.add(new Point(n, m));
                    blackPoint.remove(new Point(n, m));
                }
                if (!turn) {
                    cardsMap[n][m].imageView.setImageBitmap(black);
                    cardsMap[n][m].setNum(2);
                    whitePoint.remove(new Point(n, m));
                    blackPoint.add(new Point(n, m));
                }
                n++;
                m++;
            }


        }
    }

    public boolean checkLeftDown(int x, int y, boolean turn) {
//往左下找


        if (turn) {
            a = 2;
            b = 1;
        } else if (!turn) {
            b = 2;
            a = 1;
        }
        int i = x + 1;
        int j = y - 1;
        if (cardsMap[x + 1][y - 1].getNum() == a) {

            while (i < 8 && j > -1) {
                if (cardsMap[i][j].getNum() == b) {
                    int k = i, c = j;
                    while (k > x + 1 && c < y - 1) {
                        if (cardsMap[k][c].getNum() == 0) {
                            return false;
                        }
                        k--;
                        c++;
                    }
                    m = j;
                    n = i;

                    return true;
                }
                j--;
                i++;
            }
        }

        return false;
    }

    public void goLeftDown(int x, int y, boolean turn) {
        if (checkLeftDown(x, y, turn)) {
            n = n - 1;
            m = m + 1;
            while (n > x && m < y) {
                if (turn) {
                    cardsMap[n][m].imageView.setImageBitmap(white);
                    cardsMap[n][m].setNum(1);
                    whitePoint.add(new Point(n, m));
                    blackPoint.remove(new Point(n, m));
                }
                if (!turn) {
                    cardsMap[n][m].imageView.setImageBitmap(black);
                    cardsMap[n][m].setNum(2);
                    whitePoint.remove(new Point(n, m));
                    blackPoint.add(new Point(n, m));
                }
                n--;
                m++;
            }


        }
    }


    //判断是否可以落子
    //第一步，判断棋子颜色
    //第二步，判断棋子旁边是否有另一种颜色的棋子
    //第三步，判断另一种颜色的另一边是否有本方棋子
    public boolean isAllowedPoint(int x, int y, boolean turn) {


        //判断棋子是否在最左边
        if (x == 0 && y == 0) {

            return (checkDown(x, y, turn) || checkRight(x, y, turn) || checkRightDown(x, y, turn));
        } else if (x == 0 && y == 7) {

            return (checkLeft(x, y, turn) || checkDown(x, y, turn) || checkLeftDown(x, y, turn));
        } else if (x == 7 && y == 7) {

            return (checkUp(x, y, turn) || checkLeft(x, y, turn) || checkLeftUP(x, y, turn));
        } else if (x == 7 && y == 0) {

            return (checkUp(x, y, turn) || checkRight(x, y, turn) || checkRightUP(x, y, turn));
        } else if (x == 0) {


            return (checkDown(x, y, turn) || checkRight(x, y, turn) || checkLeft(x, y, turn) || checkLeftDown(x, y, turn) || checkRightDown(x, y, turn));
        } else if (y == 0) {

            return (checkRight(x, y, turn) || checkUp(x, y, turn) || checkDown(x, y, turn) || checkRightDown(x, y, turn) || checkRightUP(x, y, turn));
        } else if (y == 7) {

            return (checkLeft(x, y, turn) || checkLeftDown(x, y, turn) || checkLeftUP(x, y, turn) || checkUp(x, y, turn) || checkDown(x, y, turn));
        } else if (x == 7) {
            checkUp(x, y, turn);

            return (checkUp(x, y, turn) || checkLeftUP(x, y, turn) || checkRightUP(x, y, turn) || checkRight(x, y, turn) || checkLeft(x, y, turn));
        } else {

            return (checkUp(x, y, turn) || checkDown(x, y, turn) || checkLeft(x, y, turn) || checkRight(x, y, turn) || checkLeftUP(x, y, turn) || checkLeftDown(x, y, turn) || checkRightUP(x, y, turn) || checkRightDown(x, y, turn));
        }

    }

    public void PutDown(int x, int y, boolean turn) {


        //判断棋子是否在最左边
        if (x == 0 && y == 0) {

            goDown(x, y, turn);
            goRight(x, y, turn);
            goRightDown(x, y, turn);

        } else if (x == 0 && y == 7) {
            goLeft(x, y, turn);
            goDown(x, y, turn);
            goLeftDown(x, y, turn);

        } else if (x == 7 && y == 7) {

            goUp(x, y, turn);
            goLeft(x, y, turn);
            goLeftUp(x, y, turn);

        } else if (x == 7 && y == 0) {

            goUp(x, y, turn);
            goRight(x, y, turn);
            goRightUp(x, y, turn);

        } else if (x == 0) {


            goDown(x, y, turn);
            goRight(x, y, turn);
            goLeft(x, y, turn);
            goLeftDown(x, y, turn);
            goRightDown(x, y, turn);

        } else if (y == 0) {

            goRight(x, y, turn);
            goUp(x, y, turn);
            goDown(x, y, turn);
            goRightDown(x, y, turn);
            goRightUp(x, y, turn);

        } else if (y == 7) {

            goLeft(x, y, turn);
            goLeftDown(x, y, turn);
            goLeftUp(x, y, turn);
            goUp(x, y, turn);
            goDown(x, y, turn);

        } else if (x == 7) {
            goUp(x, y, turn);
            goLeftUp(x, y, turn);
            goRightUp(x, y, turn);
            goRight(x, y, turn);
            goLeft(x, y, turn);

        } else {

            goUp(x, y, turn);
            goDown(x, y, turn);
            goLeft(x, y, turn);
            goRight(x, y, turn);
            goLeftUp(x, y, turn);
            goLeftDown(x, y, turn);
            goRightUp(x, y, turn);
            goRightDown(x, y, turn);

        }

    }
}

