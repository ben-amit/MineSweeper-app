package com.example.minesweeper;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Adapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Collections;

public class LeaderboardActivity extends Activity {
    ListView lv;
    LeaderboardAdapter adapter;
    ArrayList<PlayerInfo> players = new ArrayList<PlayerInfo>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leaderboard);
        lv =findViewById(R.id.lv);
        adapter = new LeaderboardAdapter(this, players);
        lv.setAdapter(adapter);
        refreshPlayers();
    }

    public void refreshPlayers()
    {
        players.clear();

        String [] arr =  (FileManager.readFromFile("leaderboardStats")).split(":");
        for(String stats:arr)
        {
            if(stats.equals(""))
                continue;
            String[] p = stats.split(",");
            players.add(new PlayerInfo(p[0],Integer.parseInt(p[1])));
        }
        Collections.sort(players, Collections.reverseOrder());

        adapter.notifyDataSetChanged();
    }
}