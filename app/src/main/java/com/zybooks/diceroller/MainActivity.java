package com.zybooks.diceroller;

import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.CountDownTimer;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    public static final int MAX_DICE = 3;

    private int mVisibleDice;
    private Dice[] mDice;
    private ImageView[] mDiceImageViews;
    private Menu mMenu;
    private CountDownTimer mTimer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Create an array of Dice
        mDice = new Dice[MAX_DICE];
        for (int i = 0; i < MAX_DICE; i++) {
            mDice[i] = new Dice(i + 1);
        }

        // Create an array of ImageViews
        mDiceImageViews = new ImageView[MAX_DICE];
        mDiceImageViews[0] = findViewById(R.id.dice1);
        mDiceImageViews[1] = findViewById(R.id.dice2);
        mDiceImageViews[2] = findViewById(R.id.dice3);

        // All dice are initially visible
        mVisibleDice = MAX_DICE;

        showDice();
    }


    private void rollDice() {
        mMenu.findItem(R.id.action_stop).setVisible(true);

        if (mTimer != null) {
            mTimer.cancel();
        }

        mTimer = new CountDownTimer(2000, 100) {
            public void onTick(long millisUntilFinished) {
                for (int i = 0; i < mVisibleDice; i++) {
                    mDice[i].roll();
                }
                showDice();
            }

            public void onFinish() {
                mMenu.findItem(R.id.action_stop).setVisible(false);
            }
        }.start();
    }


       @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        mMenu = menu;
        return super.onCreateOptionsMenu(menu);
    }

    private void showDice() {
        // Display only the number of dice visible
        for (int i = 0; i < mVisibleDice; i++) {
            Drawable diceDrawable;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                diceDrawable = getResources().getDrawable(mDice[i].getImageId(),
                        getApplicationContext().getTheme());
            } else {
                diceDrawable = getResources().getDrawable(mDice[i].getImageId());
            }

            mDiceImageViews[i].setImageDrawable(diceDrawable);
            mDiceImageViews[i].setContentDescription(Integer.toString(mDice[i].getNumber()));
        }
    }




    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        // Determine which menu option was chosen
        switch (item.getItemId()) {
            case R.id.action_stop:
                mTimer.cancel();
                item.setVisible(false);
                return true;

            case R.id.action_roll:
                rollDice();
                return true;
        }
    }

    private void changeDiceVisibility(int numVisible) {
        mVisibleDice = numVisible;

        // Make dice visible
        for (int i = 0; i < numVisible; i++) {
            mDiceImageViews[i].setVisibility(View.VISIBLE);
        }

        // Hide remaining dice
        for (int i = numVisible; i < MAX_DICE; i++) {
            mDiceImageViews[i].setVisibility(View.GONE);
        }
    }
}
