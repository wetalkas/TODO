package com.keepsolid.wetalkas.keepsolid.fragments;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import com.keepsolid.wetalkas.keepsolid.activities.MainActivity;
import com.keepsolid.wetalkas.keepsolid.todo_sdk.Constants;
import com.keepsolid.wetalkas.keepsolid.R;
import com.keepsolid.wetalkas.keepsolid.todo_sdk.adapter.TaskAdapter;
import com.keepsolid.wetalkas.keepsolid.todo_sdk.model.TaskModel;
import com.keepsolid.wetalkas.keepsolid.sdk.CustomPreferenceManager;
import com.keepsolid.wetalkas.keepsolid.sdk.CustomSQLiteHelper;
import com.keepsolid.wetalkas.keepsolid.services.TaskReminderService;
import com.melnykov.fab.FloatingActionButton;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


public class TasksFragment extends Fragment {

    TaskAdapter taskAdapter;

    ListView lvTasks;

    Calendar calendar;

    FloatingActionButton fab;

    SearchView searchView;


    CustomSQLiteHelper customSQLiteHelper;
    SQLiteDatabase sqLiteDatabase;

    MainActivity activity;


    CustomPreferenceManager preferenceManager;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //Inflate the layout for this fragment

        View rootView = inflater.inflate(R.layout.fragment_tasks, container, false);

        CustomPreferenceManager.getInstance().init(getActivity().getApplicationContext(), "");

        if (getActivity() != null) {
            activity = (MainActivity)getActivity();
        }


        calendar = Calendar.getInstance();

        setUI(rootView);

        customSQLiteHelper = new CustomSQLiteHelper(getActivity(), "mydatabase.db", null, 1);

        sqLiteDatabase = customSQLiteHelper.getWritableDatabase();


        taskAdapter = new TaskAdapter(getActivity());

        lvTasks.setAdapter(taskAdapter);

        lvTasks.setOnItemLongClickListener(longClickListener);


        lvTasks.setDividerHeight(0);
        lvTasks.setDivider(null);

        preferenceManager = CustomPreferenceManager.getInstance();

        List<TaskModel> tasks = restoreTasks(null);
        taskAdapter.addTask(tasks);
        taskAdapter.notifyDataSetChanged();

        return rootView;
    }





    private void setUI(View rootView) {
        lvTasks = (ListView) rootView.findViewById(R.id.lvTasks);

        fab = (FloatingActionButton) rootView.findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addTaskDialog(getActivity());
            }
        });

        Toolbar toolbar = (Toolbar)rootView.findViewById(R.id.toolbar);
        if (toolbar != null) {
            Log.d("toolbar", "not null");
            toolbar.setTitle("TODO");
            toolbar.setTitleTextColor(getResources().getColor(R.color.secondary_text_default_material_light));
            ((ActionBarActivity)getActivity()).setSupportActionBar(toolbar);
        }

        searchView = (SearchView) rootView.findViewById(R.id.searchView);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                List<TaskModel> tasks;

                if (newText.equals("")) {
                    taskAdapter.deleteAll();
                    tasks = restoreTasks(null);

                } else {
                    taskAdapter.deleteAll();
                    tasks = findTasks(newText);
                }

                taskAdapter.addTask(tasks);
                taskAdapter.notifyDataSetChanged();

                return false;
            }
        });

    }





    private void addTaskDialog(Context context) {
        final AlertDialog.Builder alert = new AlertDialog.Builder(context);


        alert.setTitle("Adding task");

        ScrollView container = (ScrollView) getActivity().getLayoutInflater().inflate(R.layout.dialog_add_new_task, null);

        final EditText etTitle = (EditText) container.findViewById(R.id.etDialogAddTaskName);
        final EditText etDescription = (EditText) container.findViewById(R.id.etDialogAddTaskDescription);
        final EditText etDate = (EditText) container.findViewById(R.id.etDialogAddTaskDate);
        final EditText etTime = (EditText) container.findViewById(R.id.etDialogAddTaskTime);
        final Spinner spPriority = (Spinner) container.findViewById(R.id.spDialogAddTaskPriority);

        final long[] priority = {0};






        etDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog(getActivity(), etDate);
            }
        });

        etTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTimePickerDialog(getActivity(), etTime);
            }
        });

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item,
                Constants.PRIORITY_LEVELS);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spPriority.setAdapter(adapter);

        spPriority.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                switch (position) {
                    case 0:
                        priority[0] = 3;
                        break;
                    case 1:
                        priority[0] = 2;
                        break;
                    case 2:
                        priority[0] = 1;
                        break;
                    case 3:
                        priority[0] = 0;
                        break;
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });




        alert.setView(container);





        alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMMM yyyy HH:mm");

                String date = etDate.getText().toString();
                String time = etTime.getText().toString();

                String dateFullString = date + " " + time;

                Date dateFull = new Date();


                if (!dateFullString.equals(" ")) {
                    try {

                        dateFull = dateFormat.parse(dateFullString);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }


                Calendar calendar = Calendar.getInstance();
                calendar.setTimeInMillis(dateFull.getTime());

                Intent intent = new Intent(getActivity(),
                        TaskReminderService.class);

                PendingIntent pendingIntent = PendingIntent.getService(getActivity(), 0,
                        intent, 0);


                AlarmManager manager = (AlarmManager) getActivity().getSystemService(
                        Context.ALARM_SERVICE);


                manager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);

                TaskModel taskItem = new TaskModel(etTitle.getText().toString(), etDescription.getText().toString(),
                        dateFull.getTime(), priority[0], false, new Date().getTime());
                addTask(taskItem);


            }


        });

        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });


        final AlertDialog alertDialog = alert.show();


        final Button positiveButoon = alertDialog.getButton(DialogInterface.BUTTON_POSITIVE);

        positiveButoon.setEnabled(false);



        etTitle.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (s.length() < 1) {
                    positiveButoon.setEnabled(false);
                } else {
                    positiveButoon.setEnabled(true);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!positiveButoon.isEnabled()) {
                    Toast.makeText(activity, "Title can not be empty.", Toast.LENGTH_LONG).show();
                }
            }
        });
    }


    public long showDatePickerDialog(Context context, final EditText etDate) {
        DatePickerDialog.OnDateSetListener mDatePickerListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                calendar.set(Calendar.MONTH, monthOfYear);
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMMM yyyy");
                etDate.setText(dateFormat.format(calendar.getTime()));

            }
        };
        DatePickerDialog datePickerDialog = new DatePickerDialog(context,
                mDatePickerListener, calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();

        return calendar.getTimeInMillis();
    }




    public long showTimePickerDialog(Context context, final EditText etTime) {

        TimePickerDialog.OnTimeSetListener mTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                calendar.set(Calendar.MINUTE, minute);
                SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
                etTime.setText(timeFormat.format(calendar.getTime()));
            }
        };
        TimePickerDialog timePickerDialog = new TimePickerDialog(context,
                mTimeSetListener, calendar.get(Calendar.HOUR_OF_DAY),
                calendar.get(Calendar.MINUTE), true);
        timePickerDialog.show();



        return calendar.getTimeInMillis();
    }


    public void showSortingDialog(Context context) {
        Log.d("TaskFragment", "sorting");




        final AlertDialog.Builder alert = new AlertDialog.Builder(context);


        alert.setTitle("Sorting by:");


        alert.setItems(Constants.SORTING_BY, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                List<TaskModel> tasks = new ArrayList<TaskModel>();
                switch (which) {
                    case 0:
                        taskAdapter.deleteAll();
                        tasks = restoreTasks(CustomSQLiteHelper.TASK_DATE_COLUMN);
                        break;
                    case 1:
                        taskAdapter.deleteAll();
                        tasks = restoreTasks(CustomSQLiteHelper.TASK_NAME_COLUMN);
                        break;

                    case 2:
                        taskAdapter.deleteAll();
                        tasks = restoreTasks(CustomSQLiteHelper.TASK_PRIORITY_COLUMN);
                        break;
                }

                if (!tasks.isEmpty()) {
                    taskAdapter.addTask(tasks);
                    taskAdapter.notifyDataSetChanged();
                }

            }
        });

        final AlertDialog alertDialog = alert.show();
    }




    private void addTask(TaskModel taskItem) {

        List<TaskModel> list = new ArrayList<>();

        list.add(taskItem);

        taskAdapter.addTask(list);

        ContentValues newValues = new ContentValues();
        // Задайте значения для каждой строки.
        newValues.put(CustomSQLiteHelper.TASK_NAME_COLUMN, taskItem.name);
        newValues.put(CustomSQLiteHelper.TASK_DESCRIPTION_COLUMN, taskItem.description);
        newValues.put(CustomSQLiteHelper.TASK_DATE_COLUMN, taskItem.date);
        newValues.put(CustomSQLiteHelper.TASK_STATUS_COLUMN, taskItem.done);
        newValues.put(CustomSQLiteHelper.TASK_PRIORITY_COLUMN, taskItem.priority);

        newValues.put(CustomSQLiteHelper.TASK_TIME_COLUMN, taskItem.timeStamp);
        // Вставляем данные в базу
        long rowID = sqLiteDatabase.insert("tasks", null, newValues);

        Log.d("rowID", " " + rowID);

        //preferenceManager.putTaskModel(taskAdapter.getAllTasks());

        taskAdapter.notifyDataSetChanged();
    }



    public List<TaskModel> restoreTasks(String orderBy) {
        List<TaskModel> tasks = new ArrayList<>();

        Cursor c = sqLiteDatabase.query("tasks", null, null, null, null, null, orderBy);

        if (c.moveToFirst()) {

            do {

                String taskName = c.getString(c.getColumnIndex(CustomSQLiteHelper.TASK_NAME_COLUMN));
                String taskDescr = c.getString(c.getColumnIndex(CustomSQLiteHelper.TASK_DESCRIPTION_COLUMN));
                long taskDate = c.getLong(c.getColumnIndex(CustomSQLiteHelper.TASK_DATE_COLUMN));
                long priority = c.getLong(c.getColumnIndex(CustomSQLiteHelper.TASK_PRIORITY_COLUMN));
                boolean taskStatus = Boolean.getBoolean(c.getString(c.getColumnIndex(CustomSQLiteHelper.TASK_STATUS_COLUMN)));
                long timeStamp = c.getLong(c.getColumnIndex(CustomSQLiteHelper.TASK_TIME_COLUMN));

                TaskModel item = new TaskModel(taskName, taskDescr, taskDate, priority, taskStatus, timeStamp);

                tasks.add(item);
            } while (c.moveToNext());

        } else
            Log.d("DataBase", "0 rows");
        c.close();



        return tasks;
    }




    AdapterView.OnItemLongClickListener longClickListener = new AdapterView.OnItemLongClickListener() {
        @Override
        public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

            Log.d("long", "click");

            removeTaskDialog(getActivity(), position);

            return false;
        }
    };



    private void removeTaskDialog(Context context, final int position) {

        final AlertDialog.Builder alert = new AlertDialog.Builder(context);

        alert.setTitle("Remove task");

        alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {



                long count = sqLiteDatabase.delete("tasks", CustomSQLiteHelper.TASK_TIME_COLUMN
                        + " = " + taskAdapter.getItem(position).timeStamp, null);

                taskAdapter.deleteItem(position);
                taskAdapter.notifyDataSetChanged();

                Log.d("deleting tasks from db", "count = " + count);
            }
        });

        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        alert.show();

    }




    public List<TaskModel> findTasks(String key) {
        List<TaskModel> tasks = new ArrayList<>();


        String[] columns = new String[] { CustomSQLiteHelper.TASK_NAME_COLUMN };

        String selection = CustomSQLiteHelper.TASK_NAME_COLUMN + " LIKE ?";
        String[] selectionArgs = new String[] {"%" + key + "%"};


        Cursor c = sqLiteDatabase.query("tasks", null, selection, selectionArgs, null, null, null);


        if (c.moveToFirst()) {

            do {

                String taskName = c.getString(c.getColumnIndex(CustomSQLiteHelper.TASK_NAME_COLUMN));
                String taskDescr = c.getString(c.getColumnIndex(CustomSQLiteHelper.TASK_DESCRIPTION_COLUMN));
                long taskDate = c.getLong(c.getColumnIndex(CustomSQLiteHelper.TASK_DATE_COLUMN));
                long taskPriority = c.getLong(c.getColumnIndex(CustomSQLiteHelper.TASK_PRIORITY_COLUMN));
                boolean taskStatus = Boolean.getBoolean(c.getString(c.getColumnIndex(CustomSQLiteHelper.TASK_STATUS_COLUMN)));
                long timeStamp = c.getLong(c.getColumnIndex(CustomSQLiteHelper.TASK_TIME_COLUMN));

                TaskModel item = new TaskModel(taskName, taskDescr, taskDate, taskPriority, taskStatus, timeStamp);


                tasks.add(item);

            } while (c.moveToNext());


        } else
            Log.d("DataBase", "0 rows");
        c.close();

        return tasks;
    }


    public void deleteAllTasksFromDataBase() {
        customSQLiteHelper.onUpgrade(sqLiteDatabase, Integer.valueOf(CustomSQLiteHelper.DATABASE_VERSION),
                Integer.valueOf(CustomSQLiteHelper.DATABASE_VERSION) + 1);

        taskAdapter.deleteAll();
        taskAdapter.notifyDataSetChanged();
    }
}
