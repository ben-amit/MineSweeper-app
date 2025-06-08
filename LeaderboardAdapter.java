package com.example.minesweeper;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class LeaderboardAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<PlayerInfo> list;

    public LeaderboardAdapter(Context context, ArrayList<PlayerInfo> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View view;

        if (convertView == null) {
            view = new View(context);

            view = inflater.inflate(R.layout.player_layout, null);

            TextView name = (TextView) view.findViewById(R.id.name);
            name.setText(list.get(position).getName());
            TextView score = (TextView) view.findViewById(R.id.score);
            score.setText(list.get(position).getScore() + "");
        } else {
            view = (View) convertView;
        }
        return view;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

}
