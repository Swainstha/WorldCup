package com.example.swainstha.worldcup.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.swainstha.worldcup.Classes.GroupRankData;
import com.example.swainstha.worldcup.Classes.MatchData;
import com.example.swainstha.worldcup.R;

import java.util.ArrayList;
import java.util.TreeSet;

/**
 * Created by swainstha on 5/29/18.
 */

public class GroupRankListAdapter  extends BaseAdapter{

    private static final int TYPE_ITEM = 0;
    private static final int TYPE_SEPARATOR = 1;
    private static final int TYPE_SEPARATOR_HEADER = 2;

    private ArrayList<GroupRankData> mData = new ArrayList<>();
    private TreeSet<Integer> sectionHeader = new TreeSet<Integer>();
    private TreeSet<Integer> subSectionHeader = new TreeSet<>();

    private LayoutInflater mInflater;

    public GroupRankListAdapter(@NonNull Context context) {
        mInflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    //adding other items
    public void addItem(final GroupRankData item) {
        mData.add(item);
        notifyDataSetChanged();
    }

    //adding section header like group1, group2
    public void addSectionHeaderItem(final GroupRankData item) {
        mData.add(item);
        sectionHeader.add(mData.size() - 1);
        notifyDataSetChanged();
    }

    public void addSubSectionHeaderItem(final GroupRankData item) {
        mData.add(item);
        subSectionHeader.add(mData.size() - 1);
        notifyDataSetChanged();
    }

    //checking if it is a header or other item
    @Override
    public int getItemViewType(int position) {
        if(subSectionHeader.contains(position)) {
            return TYPE_SEPARATOR_HEADER;
        }
        return sectionHeader.contains(position) ? TYPE_SEPARATOR : TYPE_ITEM;
    }

    @Override
    public int getViewTypeCount() {
        return 3;
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public GroupRankData getItem(int position) {
        return mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        GroupRankListAdapter.ViewHolder holder = null;
        int rowType = getItemViewType(position);

        if (convertView == null) {
            //mInflater = LayoutInflater.from(context);
            holder = new GroupRankListAdapter.ViewHolder();
            switch (rowType) {
                case TYPE_ITEM:
                    convertView = mInflater.inflate(R.layout.group_rank_list, null);
                    holder.id = convertView.findViewById(R.id.sn);
                    holder.teamName = convertView.findViewById(R.id.teamName);
                    holder.point = convertView.findViewById(R.id.score);
                    holder.g_scored = convertView.findViewById(R.id.g_scored);
                    holder.g_conceded = convertView.findViewById(R.id.g_conceded);
                    break;
                case TYPE_SEPARATOR:
                    convertView = mInflater.inflate(R.layout.match_list_header, null);
                    holder.group = convertView.findViewById(R.id.match_list_head);
                    break;
                case TYPE_SEPARATOR_HEADER:
                    convertView = mInflater.inflate(R.layout.group_rank_subheader, null);
                    holder.rank = convertView.findViewById(R.id.rank);
                    holder.teamName = convertView.findViewById(R.id.country);
                    holder.point = convertView.findViewById(R.id.pts);
                    holder.g_scored = convertView.findViewById(R.id.gs);
                    holder.g_conceded = convertView.findViewById(R.id.gc);
                    break;
            }
            convertView.setTag(holder);
        } else {
            holder = (GroupRankListAdapter.ViewHolder) convertView.getTag();
        }
        switch (rowType) {
            case TYPE_ITEM:
                holder.id.setText(mData.get(position).getId() + "");
                holder.teamName.setText(mData.get(position).getTeamName());
                holder.point.setText(mData.get(position).getPoint() + "");
                holder.g_scored.setText(mData.get(position).getG_scored() + "");
                holder.g_conceded.setText(mData.get(position).getG_conceded() + "");

                break;

            case TYPE_SEPARATOR:
                holder.group.setText(mData.get(position).getGroup());
                break;

            case TYPE_SEPARATOR_HEADER:
                holder.rank.setText(mData.get(position).getRank());
                holder.teamName.setText(mData.get(position).getTeamName());
                holder.point.setText(mData.get(position).getPts());
                holder.g_scored.setText(mData.get(position).getGs());
                holder.g_conceded.setText(mData.get(position).getGc());
        }

        return convertView;
    }

    //viewholder cache
    public static class ViewHolder {
        public TextView id;
        public TextView rank;
        public TextView teamName;
        public TextView point;
        public TextView g_scored;
        public TextView g_conceded;
        public TextView group;
    }
}
