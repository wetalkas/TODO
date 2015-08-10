package com.keepsolid.wetalkas.keepsolid.todo_sdk.adapter;

import android.app.ActionBar;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.keepsolid.wetalkas.keepsolid.R;
import com.keepsolid.wetalkas.keepsolid.sdk.CustomSQLiteHelper;
import com.keepsolid.wetalkas.keepsolid.services.AlarmManagerHelper;
import com.keepsolid.wetalkas.keepsolid.todo_sdk.Constants;
import com.keepsolid.wetalkas.keepsolid.todo_sdk.model.Item;
import com.keepsolid.wetalkas.keepsolid.todo_sdk.model.SectionModel;
import com.keepsolid.wetalkas.keepsolid.todo_sdk.model.TaskModel;
import com.keepsolid.wetalkas.keepsolid.sdk.Sdk;


import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;


public class TaskAdapter extends ArrayAdapter<Item> {

    private Context context;

    private List<Item> items = new ArrayList<>();

    private CustomSQLiteHelper customSQLiteHelper;

    private SQLiteDatabase sqLiteDatabase;

    private AlarmManagerHelper alarmManagerHelper;

    public static final int TYPE_SEPARATOR = 0;
    public static final int TYPE_ITEM = 1;




    public TaskAdapter(Context context) {
        super(context, R.layout.model_task);
        this.context = context;
        alarmManagerHelper = AlarmManagerHelper.getInstance();
        customSQLiteHelper = CustomSQLiteHelper.getInstance(context);
        sqLiteDatabase = customSQLiteHelper.getWritableDatabase();
    }






    @Override
    public int getCount() {
        return items.size();
    }

    public Item getTask(int position) {
        return items.get(position);

    }

    public List<Item> getAllTasks() {
        return items;
    }

    public void addTask(List<Item> items) {
        this.items.addAll(items);
    }


    public void deleteAll() {
        this.items = new ArrayList<>();
    }

    public void deleteItem(int position) {
        this.items.remove(position);
    }


    @Override
    public boolean isEnabled(int position) {
        return true;
    }

    @Override
    public int getPosition(Item item) {
        return items.indexOf(item);
    }


    @Override
    public Item getItem(int position) {
        return items.get(position);
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public int getItemViewType(int position) {
        return getItem(position).isSection() ? TYPE_SEPARATOR : TYPE_ITEM;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder viewHolder;
        final SectionViewHolder sectionViewHolder;

        final Item item = items.get(position);


        LayoutInflater inflater = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);



        if (getItemViewType(position) == TYPE_SEPARATOR) {

            View row = convertView;



            if (row == null) {

                sectionViewHolder = new SectionViewHolder();
                row = inflater.inflate(R.layout.time_separator, parent, false);

                row.setEnabled(false);

                sectionViewHolder.tvSeparatorName = (TextView) row.findViewById(R.id.tvSeparatorName);

                row.setTag(sectionViewHolder);
            } else {

                sectionViewHolder = (SectionViewHolder) row.getTag();
            }

            final SectionModel section = (SectionModel) item;

            Log.d("hello", "motherfucker");
            sectionViewHolder.tvSeparatorName.setText(Sdk.getSeparatorName(section.getType(), context));

            return row;

        }


            else {

            View row = convertView;
            if (row == null) {
                viewHolder = new ViewHolder();

                row = inflater.inflate(R.layout.model_task, parent, false);

                viewHolder.tvTaskName = (TextView) row.findViewById(R.id.tvTaskName);
                viewHolder.tvTaskDescription = (TextView) row.findViewById(R.id.tvTaskDescription);
                viewHolder.tvTaskDate = (TextView) row.findViewById(R.id.tvTaskDate);
                viewHolder.ivTaksPriority = (CircleImageView) row.findViewById(R.id.ivTaskPriority);
                row.setTag(viewHolder);
            } else {

                viewHolder = (ViewHolder) row.getTag();
            }

            final TaskModel task = (TaskModel) item;

            viewHolder.tvTaskName.setText(task.getName());
            viewHolder.tvTaskDescription.setText(task.getDescription());

            if (task.getDate() != 0) {
                viewHolder.tvTaskDate.setText(Sdk.getDateWithCurrentLocale(task.getDate(), context));
            } else {
                viewHolder.tvTaskDate.setText(null);
            }





            setChecking(viewHolder, task, row);


            final View finalRow = row;
            viewHolder.ivTaksPriority.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    task.setDoneBool(!task.isDoneBool());
                    updateCheckToDB(task);
                    setChecking(viewHolder, task, finalRow);
                }
            });

            return row;
        }






        // update the item view

    }


    private static class ViewHolder {
        TextView tvTaskName;
        TextView tvTaskDescription;
        TextView tvTaskDate;
        CircleImageView ivTaksPriority;
    }

    private static class SectionViewHolder {
        TextView tvSeparatorName;
    }



    public void setChecking(ViewHolder viewHolder, TaskModel item, View view) {
        if (!item.isDoneBool()) {
            setUnchecked(viewHolder, item, view);
        } else {
            setChecked(viewHolder, item, view);
        }

    }


    private void setChecked(ViewHolder viewHolder, TaskModel item, View view) {
        viewHolder.ivTaksPriority.setColorFilter(context.getResources()
                .getColor(Constants.PRIORITY_COLORS_CLICK[(int) item.getPriority()]));
        viewHolder.ivTaksPriority.setImageDrawable(context.getResources()
                .getDrawable(R.drawable.ic_checkbox_marked_circle_black_48dp));

        view.setBackgroundColor(context.getResources().getColor(R.color.gray_200));

        alarmManagerHelper.cancelNotification(item.getName(), item.getTimeStamp());
        Log.d("cancelled", " " + item.getTimeStamp());

    }


    private void setUnchecked(ViewHolder viewHolder, TaskModel item, View view) {
        viewHolder.ivTaksPriority.setImageDrawable(context.getResources()
                .getDrawable(R.drawable.ic_checkbox_blank_circle_black_48dp));

        viewHolder.ivTaksPriority.setColorFilter(context.getResources()
                .getColor(Constants.PRIORITY_COLORS[(int) item.getPriority()]));

        view.setBackgroundColor(context.getResources().getColor(R.color.gray_50));

        alarmManagerHelper.setNotification(item.getName(), item.getDate(), item.getTimeStamp());
        Log.d("restored", " " + item.getTimeStamp());

    }

    private boolean getChecked(TaskModel task) {
        boolean isChecked = false;

        String selection = CustomSQLiteHelper.TASK_TIME_COLUMN + " = ?";
        String[] selectionArgs = new String[]{String.valueOf(task.getTimeStamp())};


        Cursor c = sqLiteDatabase.query("tasks", null, selection, selectionArgs, null, null, null);


        if (c.moveToFirst()) {

            do {

                isChecked = Boolean.getBoolean(c.getString(c.getColumnIndex(CustomSQLiteHelper.TASK_STATUS_COLUMN)));

            } while (c.moveToNext());



        }
        c.close();

        return isChecked;
    }


    public void updateCheckToDB(TaskModel task) {


        if (task.isDoneBool()) {
            task.setDone(0);
        } else {
            task.setDone(1);
        }

        customSQLiteHelper.updateLong(CustomSQLiteHelper.TASK_STATUS_COLUMN, task.getTimeStamp(), task.getDone());
    }








}





