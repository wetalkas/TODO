package com.keepsolid.wetalkas.keepsolid.todo_sdk.adapter;

import android.content.Context;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.support.design.widget.FloatingActionButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.keepsolid.wetalkas.keepsolid.R;
import com.keepsolid.wetalkas.keepsolid.todo_sdk.Constants;
import com.keepsolid.wetalkas.keepsolid.todo_sdk.model.TaskModel;
import com.keepsolid.wetalkas.keepsolid.sdk.Sdk;


import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;


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
        final ViewHolder viewHolder;

        if(convertView == null) {
            // inflate the GridView item layout
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(R.layout.model_task, parent, false);


            // initialize the view holder
            viewHolder = new ViewHolder();
            viewHolder.tvTaskName = (TextView) convertView.findViewById(R.id.tvTaskName);
            viewHolder.tvTaskDescription = (TextView) convertView.findViewById(R.id.tvTaskDescription);
            viewHolder.tvTaskDate = (TextView) convertView.findViewById(R.id.tvTaskDate);
            viewHolder.ivTaksPriority = (CircleImageView) convertView.findViewById(R.id.ivTaskPriority);
            convertView.setTag(viewHolder);
        } else {
            // recycle the already inflated view
            viewHolder = (ViewHolder) convertView.getTag();
        }

        // update the item view
        final TaskModel item = tasks.get(position);



        viewHolder.tvTaskName.setText(item.name);
        viewHolder.tvTaskDescription.setText(item.description);
        viewHolder.tvTaskDate.setText(Sdk.getDateWithCurrentLocale(item.date, context));


        final View finalConvertView = convertView;


        setChecking(viewHolder, item, finalConvertView);



        viewHolder.ivTaksPriority.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                item.done = !item.done;

                setChecking(viewHolder, item, finalConvertView);
            }
        });
        //viewHolder.cbTaskDone.setChecked(item.done);


       // LinearLayout.LayoutParams l
        return convertView;
    }


    private static class ViewHolder {
        TextView tvTaskName;
        TextView tvTaskDescription;
        TextView tvTaskDate;
        CircleImageView ivTaksPriority;
        CheckBox cbTaskDone;
    }



    public void setChecking(ViewHolder viewHolder, TaskModel item, View view) {
        if (!item.done) {
            setUnchecked(viewHolder, item, view);
        } else {
            setChecked(viewHolder, item, view);
        }
    }


    private void setChecked(ViewHolder viewHolder, TaskModel item, View view) {
        viewHolder.ivTaksPriority.setColorFilter(context.getResources()
                .getColor(Constants.PRIORITY_COLORS_CLICK[(int) item.priority]));
        viewHolder.ivTaksPriority.setImageDrawable(context.getResources()
                .getDrawable(R.drawable.ic_checkbox_marked_circle_black_48dp));

        view.setBackgroundColor(context.getResources().getColor(R.color.gray_200));

    }


    private void setUnchecked(ViewHolder viewHolder, TaskModel item, View view) {
        viewHolder.ivTaksPriority.setImageDrawable(context.getResources()
                .getDrawable(R.drawable.ic_checkbox_blank_circle_black_48dp));

        viewHolder.ivTaksPriority.setColorFilter(context.getResources()
                .getColor(Constants.PRIORITY_COLORS[(int) item.priority]));

        view.setBackgroundColor(context.getResources().getColor(R.color.gray_50));

    }



}





