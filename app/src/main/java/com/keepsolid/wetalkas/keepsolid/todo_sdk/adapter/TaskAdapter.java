package com.keepsolid.wetalkas.keepsolid.todo_sdk.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.keepsolid.wetalkas.keepsolid.R;
import com.keepsolid.wetalkas.keepsolid.todo_sdk.Constants;
import com.keepsolid.wetalkas.keepsolid.todo_sdk.model.TaskModel;
import com.keepsolid.wetalkas.keepsolid.sdk.Sdk;

import java.util.ArrayList;
import java.util.List;




public class TaskAdapter extends ArrayAdapter<TaskModel> {

    private Context context;

    private List<TaskModel> tasks = new ArrayList<>();


    public TaskAdapter(Context context) {
        super(context, R.layout.model_task);
        this.context = context;
    }


    @Override
    public int getCount() {
        return tasks.size();
    }

    public TaskModel getTask(int position) {
        return tasks.get(position);

    }

    public List<TaskModel> getAllTasks() {
        return  tasks;
    }

    public void addTask(List<TaskModel> tasks) {
        this.tasks.addAll(tasks);
    }



    public void deleteAll() {
        this.tasks = new ArrayList<TaskModel>();;
    }

    public void deleteItem(int position) {
        this.tasks.remove(position);
    }



    @Override
    public int getPosition(TaskModel item) {
        return tasks.indexOf(item);
    }


    @Override
    public TaskModel getItem(int position) {
        return tasks.get(position);
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
            viewHolder.tvTaskName = (TextView) convertView.findViewById(R.id.tvTaskName);
            viewHolder.tvTaskDescription = (TextView) convertView.findViewById(R.id.tvTaskDescription);
            viewHolder.tvTaskDate = (TextView) convertView.findViewById(R.id.tvTaskDate);
            viewHolder.ivTaksPriority = (ImageView) convertView.findViewById(R.id.ivTaskPriority);
            viewHolder.cbTaskDone = (CheckBox) convertView.findViewById(R.id.cbTaskDone);

            convertView.setTag(viewHolder);
        } else {
            // recycle the already inflated view
            viewHolder = (ViewHolder) convertView.getTag();
        }

        // update the item view
        TaskModel item = tasks.get(position);



        viewHolder.tvTaskName.setText(item.name);
        viewHolder.tvTaskDescription.setText(item.description);
        viewHolder.tvTaskDate.setText(Sdk.getDateWithCurrentLocale(item.date, context));
        viewHolder.ivTaksPriority.setBackgroundColor(context.getResources().getColor(Constants.PRIORITY_COLORS[(int) item.priority]));
        viewHolder.cbTaskDone.setChecked(item.done);


        return convertView;
    }


    private static class ViewHolder {
        TextView tvTaskName;
        TextView tvTaskDescription;
        TextView tvTaskDate;
        ImageView ivTaksPriority;
        CheckBox cbTaskDone;
    }

}





