package com.keepsolid.wetalkas.keepsolid.todo_sdk.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.keepsolid.wetalkas.keepsolid.R;
import com.keepsolid.wetalkas.keepsolid.todo_sdk.model.SettingModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wetalkas on 04.06.15.
 */
public class SettingsAdapter extends ArrayAdapter<SettingModel> {
    private Context context;

    private List<SettingModel> settings = new ArrayList<>();


    public SettingsAdapter(Context context) {
        super(context, R.layout.model_task);
        this.context = context;
    }


    @Override
    public int getCount() {
        return settings.size();
    }

    public SettingModel getTask(int position) {
        return settings.get(position);

    }

    public List<SettingModel> getAllTasks() {
        return settings;
    }

    public void addTask(List<SettingModel> settings) {
        this.settings.addAll(settings);
    }



    public void deleteAll() {
        this.settings = new ArrayList<SettingModel>();;
    }

    public void deleteItem(int position) {
        this.settings.remove(position);
    }



    @Override
    public int getPosition(SettingModel item) {
        return settings.indexOf(item);
    }


    @Override
    public SettingModel getItem(int position) {
        return settings.get(position);
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;

        if(convertView == null) {
            // inflate the GridView item layout
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(R.layout.model_task, parent, false);


            // initialize the view holder
            viewHolder = new ViewHolder();
            viewHolder.tvSettingName = (TextView) convertView.findViewById(R.id.tvTaskName);
            viewHolder.tvTaskDescription = (TextView) convertView.findViewById(R.id.tvTaskDescription);
            convertView.setTag(viewHolder);
        } else {
            // recycle the already inflated view
            viewHolder = (ViewHolder) convertView.getTag();
        }

        // update the item view
        SettingModel item = settings.get(position);



        viewHolder.tvSettingName.setText(item.name);
        viewHolder.tvTaskDescription.setText(item.description);


        return convertView;
    }


    private static class ViewHolder {
        TextView tvSettingName;
        TextView tvTaskDescription;
        RelativeLayout container;
    }

}
