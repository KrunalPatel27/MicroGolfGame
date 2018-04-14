package com.example.krupa.project4;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.Random;

public class MainActivity extends AppCompatActivity {
    Game game;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        game = new Game(5, 10, new Random().nextInt(5 * 10));
    }

    public class Player1 implements Runnable {
        @Override
        public void run() {
        }
    }
    public class Player2 implements Runnable {

        @Override
        public void run() {

        }
    }
}
