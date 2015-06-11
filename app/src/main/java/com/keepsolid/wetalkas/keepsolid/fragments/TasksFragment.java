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
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.app.Fragment;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
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
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
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

    CoordinatorLayout coordinatorLayout;


    CustomPreferenceManager preferenceManager;

    String currentLogin;

    View view;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //Inflate the layout for this fragment

        View rootView = inflater.inflate(R.layout.fragment_tasks, container, false);



        preferenceManager = CustomPreferenceManager.getInstance();

        currentLogin = preferenceManager.getString("current_login");


        if (getActivity() != null) {
            activity = (MainActivity)getActivity();
        }


        calendar = Calendar.getInstance();

        setUI(rootView);

        customSQLiteHelper = new CustomSQLiteHelper(getActivity(), "mydatabase.db", null, 1);

        sqLiteDatabase = customSQLiteHelper.getWritableDatabase();




        taskAdapter = new TaskAdapter(getActivity());

        taskAdapter.setDataBase(sqLiteDatabase);

        lvTasks.setDivider(new ColorDrawable(activity.getResources().getColor(R.color.white_12)));   //0xAARRGGBB
        lvTasks.setDividerHeight(1);

        lvTasks.setAdapter(taskAdapter);

        lvTasks.setOnItemLongClickListener(longClickListener);


        String order = preferenceManager.getString("order");



        List<TaskModel> tasks = restoreTasks(order);
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
            toolbar.setTitleTextColor(getResources().getColor(R.color.abc_primary_text_material_dark));
            ((ActionBarActivity)getActivity()).setSupportActionBar(toolbar);

            //toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_dots_vertical_white_24dp));
        }


        view = rootView;


        coordinatorLayout = (CoordinatorLayout) rootView.findViewById(R.id.coordinatorLayout);



        //FloatingActionButton floatingActionButton = new FloatingActionButton(activity);
        CollapsingToolbarLayout.LayoutParams params = new CollapsingToolbarLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);


        //floatingActionButton.setImageResource(R.drawable.ic_plus_white_24dp);


        //coordinatorLayout.addView(floatingActionButton);




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

        final TextInputLayout tilTitle = (TextInputLayout) container.findViewById(R.id.tilDialogAddTaskName);
        final EditText etTitle = tilTitle.getEditText();

        final TextInputLayout tilDescription = (TextInputLayout) container.findViewById(R.id.tilDialogAddTaskDescription);
        final EditText etDescription = tilDescription.getEditText();

        final TextInputLayout tilDate = (TextInputLayout) container.findViewById(R.id.tilDialogAddTaskDate);
        final EditText etDate = tilDate.getEditText();

        final TextInputLayout tilTime = (TextInputLayout) container.findViewById(R.id.tilDialogAddTaskTime);
        final EditText etTime = tilTime.getEditText();

        final CheckBox cbAddTaskRemind = (CheckBox) container.findViewById(R.id.cbAddTaskRemind);

        final Spinner spPriority = (Spinner) container.findViewById(R.id.spDialogAddTaskPriority);

        final long[] priority = {0};



        tilTitle.setHint("Title");
        tilDescription.setHint("Description");
        tilDate.setHint("Date");
        tilTime.setHint("Time");


        etDate.setEnabled(false);
        etTime.setEnabled(false);

        cbAddTaskRemind.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    etDate.setEnabled(true);
                    etTime.setEnabled(true);
                } else {
                    etDate.setEnabled(false);
                    etTime.setEnabled(false);

                    etDate.setText(null);
                    etTime.setText(null);
                }
            }


        });









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

                SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yy HH:mm");

                String date = etDate.getText().toString();
                String time = etTime.getText().toString();

                String dateFullString = date + " " + time;

                Date dateFull = null;

                long dateLong = 0;


                if (!dateFullString.equals(" ")) {
                    try {

                        dateFull = dateFormat.parse(dateFullString);
                        dateLong = dateFull.getTime();


                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }


                if (cbAddTaskRemind.isChecked() && dateFull != null) {
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTimeInMillis(dateFull.getTime());

                    Intent intent = new Intent(getActivity(),
                            TaskReminderService.class);

                    intent.putExtra("task_title", etTitle.getText().toString());

                    PendingIntent pendingIntent = PendingIntent.getService(getActivity(), 0,
                            intent, 0);

                    AlarmManager manager = (AlarmManager) getActivity().getSystemService(
                            Context.ALARM_SERVICE);

                    manager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
                }



                TaskModel taskItem = new TaskModel(etTitle.getText().toString(), etDescription.getText().toString(),
                        dateLong, priority[0], 0, new Date().getTime());
                addTask(taskItem);

etDate.setEnabled(true);
                    etTime.setEnabled(true);
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
                    tilTitle.setError("Can not be empty.");
                } else {
                    tilTitle.setErrorEnabled(false);
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
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yy");
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
                String order = null;
                switch (which) {
                    case 0:
                        taskAdapter.deleteAll();
                        order = CustomSQLiteHelper.TASK_DATE_COLUMN;
                        tasks = restoreTasks(order);
                        break;
                    case 1:
                        taskAdapter.deleteAll();
                        order = CustomSQLiteHelper.TASK_NAME_COLUMN;
                        tasks = restoreTasks(order);
                        break;

                    case 2:
                        taskAdapter.deleteAll();
                        order = CustomSQLiteHelper.TASK_PRIORITY_COLUMN;
                        tasks = restoreTasks(order);
                        break;


                    case 3:
                        taskAdapter.deleteAll();
                        order = CustomSQLiteHelper.TASK_STATUS_COLUMN;
                        tasks = restoreTasks(order);
                        break;
                }

                if (!tasks.isEmpty()) {
                    preferenceManager.putString("order", order);
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

        newValues.put(CustomSQLiteHelper.USER_LOGIN_COLUMN, currentLogin);
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



        String selection = CustomSQLiteHelper.USER_LOGIN_COLUMN + " = ?";
        String[] selectionArgs = new String[] {currentLogin};

        Cursor c = sqLiteDatabase.query("tasks", null, selection, selectionArgs, null, null, orderBy);

        if (c.moveToFirst()) {

            do {

                String taskName = c.getString(c.getColumnIndex(CustomSQLiteHelper.TASK_NAME_COLUMN));
                String taskDescr = c.getString(c.getColumnIndex(CustomSQLiteHelper.TASK_DESCRIPTION_COLUMN));
                long taskDate = c.getLong(c.getColumnIndex(CustomSQLiteHelper.TASK_DATE_COLUMN));
                long priority = c.getLong(c.getColumnIndex(CustomSQLiteHelper.TASK_PRIORITY_COLUMN));
                long taskStatus = c.getLong(c.getColumnIndex(CustomSQLiteHelper.TASK_STATUS_COLUMN));
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

        final TaskModel deletingTask = taskAdapter.getTask(position);
        final boolean[] isDeleted = {false};

        alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {


                taskAdapter.deleteItem(position);
                taskAdapter.notifyDataSetChanged();

                Log.d("deleting tasks from", "list");


                isDeleted[0] = true;

                Snackbar snackbar = Snackbar.make(coordinatorLayout, "Deleting", Snackbar.LENGTH_LONG);

                snackbar.setAction("Cancel", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Log.d("snackbar", "click");
                        taskAdapter.deleteAll();
                        List<TaskModel> tasks = restoreTasks(null);
                        taskAdapter.addTask(tasks);
                        taskAdapter.notifyDataSetChanged();

                        isDeleted[0] = false;
                    }
                });

                snackbar.getView().addOnAttachStateChangeListener(new View.OnAttachStateChangeListener() {
                    @Override
                    public void onViewAttachedToWindow(View v) {
                        Log.d("snackbar", "attach");
                    }

                    @Override
                    public void onViewDetachedFromWindow(View v) {
                        Log.d("snackbar", "detach");

                        if (isDeleted[0]) {

                            long count = sqLiteDatabase.delete("tasks", CustomSQLiteHelper.TASK_TIME_COLUMN
                                    + " = " + deletingTask.timeStamp, null);

                            Log.d("deleting tasks from db", "count = " + count);
                        }
                    }
                });

                snackbar.show();

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



        String selection = CustomSQLiteHelper.TASK_NAME_COLUMN + " LIKE ? AND "
                + CustomSQLiteHelper.USER_LOGIN_COLUMN + " = ?";
        String[] selectionArgs = new String[] {"%" + key + "%", currentLogin};


        Cursor c = sqLiteDatabase.query("tasks", null, selection, selectionArgs, null, null, null);



        if (c.moveToFirst()) {

            do {

                String taskName = c.getString(c.getColumnIndex(CustomSQLiteHelper.TASK_NAME_COLUMN));
                String taskDescr = c.getString(c.getColumnIndex(CustomSQLiteHelper.TASK_DESCRIPTION_COLUMN));
                long taskDate = c.getLong(c.getColumnIndex(CustomSQLiteHelper.TASK_DATE_COLUMN));
                long taskPriority = c.getLong(c.getColumnIndex(CustomSQLiteHelper.TASK_PRIORITY_COLUMN));
                long taskStatus = c.getLong(c.getColumnIndex(CustomSQLiteHelper.TASK_STATUS_COLUMN));
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


    public SQLiteDatabase getSqLiteDatabase() {
        return sqLiteDatabase;
    }
}
