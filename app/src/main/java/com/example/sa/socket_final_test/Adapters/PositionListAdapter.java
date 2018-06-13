package com.example.sa.socket_final_test.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.sa.socket_final_test.Classes.PositionData;
import com.example.sa.socket_final_test.R;

import java.util.ArrayList;

/**
 * Created by swainstha on 5/22/18.
 */

public class PositionListAdapter extends ArrayAdapter<PositionData> {

    Context context;
    private ArrayList<PositionData> positionList;

    // View lookup cache
    private static class ViewHolder {
        TextView position;
        TextView name;
        TextView score;
    }

    public PositionListAdapter(@NonNull Context context, ArrayList<PositionData> list) {
        super(context, 0, list);
        this.context = context;
        this.positionList = list;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        // to add a custom view..

        PositionListAdapter.ViewHolder viewHolder;
        PositionData positionData= getItem(position);
        final View result;

        if(convertView == null) {
            LayoutInflater layoutInflater = LayoutInflater.from(context);
            convertView = layoutInflater.inflate(R.layout.position_list, parent, false);

            viewHolder = new PositionListAdapter.ViewHolder();

            viewHolder.position = convertView.findViewById(R.id.item_position_position);
            viewHolder.name = convertView.findViewById(R.id.item_position_name);
            viewHolder.score = convertView.findViewById(R.id.item_position_score);

            convertView.setTag(viewHolder);

        } else {
            viewHolder = (PositionListAdapter.ViewHolder) convertView.getTag();
            result=convertView;
        }

        viewHolder.position.setText(positionData.getPosition());
        viewHolder.name.setText(positionData.getName());
        viewHolder.score.setText(positionData.getScore());


        // making view clickable
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                PositionListAdapter.ViewHolder v = (PositionListAdapter.ViewHolder)view.getTag();
//                Intent intent = new Intent(context, DetailActivity.class);
//                intent.putExtra("id", info.id);
//                context.startActivity(intent);

            }
        });
        return convertView;
    }
}
