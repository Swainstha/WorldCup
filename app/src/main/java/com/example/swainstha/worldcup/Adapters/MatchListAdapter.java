package com.example.swainstha.worldcup.Adapters;

/**
 * Created by swainstha on 5/22/18.
 */
import java.util.ArrayList;
import java.util.TreeSet;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.swainstha.worldcup.Classes.MatchData;
import com.example.swainstha.worldcup.Classes.PositionData;
import com.example.swainstha.worldcup.R;


public class MatchListAdapter extends BaseAdapter {

    private static final int TYPE_ITEM = 0;
    private static final int TYPE_SEPARATOR = 1;
    private Context context;

    private ArrayList<MatchData> mData = new ArrayList<MatchData>();
    private TreeSet<Integer> sectionHeader = new TreeSet<Integer>();

    private LayoutInflater mInflater;

    public MatchListAdapter(@NonNull Context context) {
        mInflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.context = context;
    }

    //adding other items
    public void addItem(final MatchData item) {
        mData.add(item);
        notifyDataSetChanged();
    }

    //adding section header like group1, group2
    public void addSectionHeaderItem(final MatchData item) {
        mData.add(item);
        sectionHeader.add(mData.size() - 1);
        notifyDataSetChanged();
    }

    //checking if it is a header or other item
    @Override
    public int getItemViewType(int position) {
        return sectionHeader.contains(position) ? TYPE_SEPARATOR : TYPE_ITEM;
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public MatchData getItem(int position) {
        return mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        MatchListAdapter.ViewHolder holder = null;
        int rowType = getItemViewType(position);

        if (convertView == null) {
            //mInflater = LayoutInflater.from(context);
            holder = new MatchListAdapter.ViewHolder();
            switch (rowType) {
                case TYPE_ITEM:
                    convertView = mInflater.inflate(R.layout.match_list, null);
                    holder.team1 = (TextView) convertView.findViewById(R.id.item_match_team1);
                    holder.team2 = (TextView) convertView.findViewById(R.id.item_match_team2);
                    holder.score1 = (TextView) convertView.findViewById(R.id.item_match_score1);
                    holder.score2 = (TextView) convertView.findViewById(R.id.item_match_score2);
                    //holder.time = (TextView) convertView.findViewById(R.id.item_match_time);
                    //holder.date = (TextView) convertView.findViewById(R.id.item_match_date);
                    holder.country1 = convertView.findViewById(R.id.item_team1_flag);
                    holder.country2 = convertView.findViewById(R.id.item_team2_flag);
                    break;
                case TYPE_SEPARATOR:
                    convertView = mInflater.inflate(R.layout.match_list_header, null);
                    holder.group = (TextView) convertView.findViewById(R.id.match_list_head);
                    break;
            }
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        switch (rowType) {
            case TYPE_ITEM:
                holder.team1.setText(mData.get(position).getTeam1());
                holder.team2.setText(mData.get(position).getTeam2());
                holder.score1.setText(mData.get(position).getScore1());
                holder.score2.setText(mData.get(position).getScore2());
                String c1 = mData.get(position).getTeam1();
                c1 = c1.toLowerCase();
                c1 = c1.replaceAll("\\s","");
                int resID = context.getResources().getIdentifier(c1,
                        "drawable", context.getPackageName());
                holder.country1.setImageResource(resID);

                String c2 = mData.get(position).getTeam2();
                c2 = c2.toLowerCase();
                c2 = c2.replaceAll("\\s","");
                resID = context.getResources().getIdentifier(c2,
                        "drawable", context.getPackageName());
                holder.country2.setImageResource(resID);
                //holder.date.setText(mData.get(position).getDate());
                //holder.time.setText(mData.get(position).getTime());
                break;

            case TYPE_SEPARATOR:
                holder.group.setText(mData.get(position).getGroup());
        }

        return convertView;
    }

    //viewholder cache
    public static class ViewHolder {
        public TextView team1;
        public TextView team2;
        public TextView score1;
        public TextView score2;
        public TextView group;
        public ImageView country1;
        public ImageView country2;
        public TextView time;
        public TextView date;
    }

}
