package com.example.krupa.project4;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.provider.Contacts;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;

public class MainActivity extends AppCompatActivity {
    Game game;
    Handler UIHandler, p1Handler, p2Handler;
    ListView lv;
    final int MSG_1 = 1;
    final int MSG_2 = 2;
    Context context;
    Game.shotResponse p1Response = null;
    Game.shotResponse p2Response = null;
    public Thread plThread = null;
    public Thread p2Thread = null;
    public int scrollPosition = 0;
    int delayTime = 2000;

    @Override
    @SuppressLint("HandlerLeak")
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        setContentView(R.layout.activity_main);
        lv = findViewById(R.id.listView);
        game = new Game(5, 10, new Random().nextInt(5 * 10));
        final String [] ad = game.getFlatNameArray();
        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_expandable_list_item_1, game.getFlatNameArray()){
            @NonNull
            @Override
            public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                View view = convertView;

                LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                view = inflater.inflate(R.layout.list_view_layout, parent, false);

                TextView text = view.findViewById(R.id.text_list_view);
                text.setText(game.getFlatName(position));
                text.setBackgroundColor(game.getColor(position, getResources()));
                return view;
            }
        };

        lv.setAdapter(adapter);


        plThread = new Thread(new Player1());
        plThread.start();

        p2Thread = new Thread(new Player2());
        p2Thread.start();

        p1Response = game.pickAShot(0, Game.Shot_Options.RANDOM, game.p1);
        p2Response = game.pickAShot(0, Game.Shot_Options.RANDOM, game.p2);

        UIHandler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                if (game.gameStatus){
                    switch (msg.what) {
                        case MSG_1:
                            switch (msg.arg1) {
                                case 0:
                                    p1Response = game.pickAShot(p1Response.currentHole, Game.Shot_Options.RANDOM, game.p1);
                                    break;
                                case 1:
                                    p1Response = game.pickAShot(p1Response.currentHole, Game.Shot_Options.CLOSE_GROUP, game.p1);
                                    break;
                                case 2:
                                    p1Response = game.pickAShot(p1Response.currentHole, Game.Shot_Options.SAME_GROUP, game.p1);
                                    break;
                            }


                            //Ask p2 to make move
                            scrollPosition = p1Response.currentHole;
                            lv.smoothScrollToPosition(scrollPosition);
                            p2Handler.sendEmptyMessage(0);
                            break;

                        case MSG_2:
                            switch (msg.arg1) {
                                case 0:
                                    p2Response = game.pickAShot(p2Response.currentHole, Game.Shot_Options.RANDOM, game.p2);
                                    break;
                                case 1:
                                    p2Response = game.pickAShot(p2Response.currentHole, Game.Shot_Options.CLOSE_GROUP, game.p2);
                                    break;
                                case 2:
                                    p2Response = game.pickAShot(p2Response.currentHole, Game.Shot_Options.SAME_GROUP, game.p2);
                                    break;
                            }


                            // Ask p1 to make move
                            scrollPosition = p2Response.currentHole;
                            lv.smoothScrollToPosition(scrollPosition);
                            p1Handler.sendEmptyMessage(messageEncode(p1Response.response));
                            break;

                        default:
                            break;
                    }
                    //adapter.notifyDataSetInvalidated();

                    adapter.notifyDataSetChanged();
                } else{
                    String winnerText = "Winner: " + game.getWinner();
                    lv.smoothScrollToPosition(game.winnerHole);
                    Toast.makeText(context, winnerText,
                            Toast.LENGTH_LONG).show();
                    adapter.notifyDataSetChanged();
                }

            }

        };
        while(p1Handler == null || p2Handler == null){
           //do nothing
        }
        p1Handler.sendEmptyMessage(6);


    }

    int messageEncode(Game.Shot_Responses response){
        switch (response){
            case JACKPOT:
                return 0;
            case BIG_MISS:
                return 1;
            case NEAR_MISS:
                return 2;
            case NEAR_GROUP:
                return 3;
            case CATASTROPHE:
                return 4;
            case INVALID_MOVE:
                return 5;
            default:
                break;
        }
        return 0;
    }


    public class Player1 implements Runnable {
        @Override
        @SuppressLint("HandlerLeak")
        public void run() {
            Looper.prepare();
            p1Handler = new Handler(){
                @Override
                public void handleMessage(Message msg) {
                    try {
                        Thread.sleep(delayTime);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    Message m = new Message();

                    switch (msg.what){
                        case 1:
                            m.arg1 =0;
                            break;
                        case 2:
                            m.arg1 =2;
                            break;
                        case 3:
                            m.arg1 =1;
                            break;
                        case 6:
                            m.arg1 = 0;
                            break;
                        default:
                            break;
                    }

                    m.what = MSG_1;
                    UIHandler.sendMessage(m);
                }
            };
            Looper.loop();
        }
    }

    /*
    p2 always shoots at random hole
    plays on luck
     */
    public class Player2 implements Runnable {

        @Override
        @SuppressLint("HandlerLeak")
        public void run() {
            Looper.prepare();
            p2Handler = new Handler(){
                @Override
                public void handleMessage(Message msg) {
                    try {
                        Thread.sleep(delayTime);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    Message m = new Message();
                    m.arg1 = 0;
                    m.what = MSG_2;
                    UIHandler.sendMessage(m);
                    return;

                }
            };
            Looper.loop();
        }
    }

}
