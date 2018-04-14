package com.example.krupa.project4;

import android.annotation.SuppressLint;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.Random;

public class MainActivity extends AppCompatActivity {
    Game game;
    Handler UIHandler, p1Handler, p2Handler;
    ListView lv;
    @Override
    @SuppressLint("HandlerLeak")
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        lv = findViewById(R.id.listView);
        game = new Game(5, 10, new Random().nextInt(5 * 10));
        Adapter adapter = new ArrayAdapter<String>(this, android.R.layout.simple_expandable_list_item_1, R.id.listView,);

        UIHandler = new Handler(){
            @Override
            public void handleMessage(Message msg) {

            }
        };
    }

    public class Player1 implements Runnable {
        @Override
        @SuppressLint("HandlerLeak")
        public void run() {
            Looper.prepare();
            p1Handler = new Handler(){
                @Override
                public void handleMessage(Message msg) {

                }
            };
            Looper.loop();
        }
    }
    public class Player2 implements Runnable {

        @Override
        @SuppressLint("HandlerLeak")
        public void run() {
            Looper.prepare();
            p2Handler = new Handler(){
                @Override
                public void handleMessage(Message msg) {

                }
            };
            Looper.loop();
        }
    }
}
