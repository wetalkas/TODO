package com.keepsolid.wetalkas.keepsolid.fragments;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Notification;
import android.app.NotificationManager;
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
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.design.widget.TextInputLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.support.v7.widget.SearchView;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import com.keepsolid.wetalkas.keepsolid.activities.MainActivity;
import com.keepsolid.wetalkas.keepsolid.sdk.CustomFragmentManager;
import com.keepsolid.wetalkas.keepsolid.sdk.Sdk;
import com.keepsolid.wetalkas.keepsolid.services.AlarmManagerHelper;
import com.keepsolid.wetalkas.keepsolid.todo_sdk.Constants;
import com.keepsolid.wetalkas.keepsolid.R;
import com.keepsolid.wetalkas.keepsolid.todo_sdk.adapter.TabAdapter;
import com.keepsolid.wetalkas.keepsolid.todo_sdk.adapter.TaskAdapter;
import com.keepsolid.wetalkas.keepsolid.todo_sdk.model.Item;
import com.keepsolid.wetalkas.keepsolid.todo_sdk.model.SectionModel;
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
    private CustomFragmentManager customFragmentManager;

    String currentLogin;

    View view;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //Inflate the layout for this fragment

        View rootView = inflater.inflate(R.layout.fragment_tasks, container, false);

        this.setHasOptionsMenu(true);



        preferenceManager = CustomPreferenceManager.getInstance();

        customFragmentManager = CustomFragmentManager.getInstance();

        currentLogin = preferenceManager.getString("current_login");


        if (getActivity() != null) {
            activity = (MainActivity)getActivity();
        }


        calendar = Calendar.getInstance();

        setUI(rootView);

        customSQLiteHelper = CustomSQLiteHelper.getInstance(getActivity().getApplicationContext());

        sqLiteDatabase = customSQLiteHelper.getWritableDatabase();




        taskAdapter = new TaskAdapter(getActivity());



        lvTasks.setDivider(new ColorDrawable(activity.getResources().getColor(R.color.white_12)));   //0xAARRGGBB
        lvTasks.setDividerHeight(1);

        //lvTasks.setItemsCanFocus(true);

        lvTasks.setAdapter(taskAdapter);

        lvTasks.setOnItemLongClickListener(longClickListener);

        lvTasks.setOnItemClickListener(itemClickListener);


        String order = preferenceManager.getString("order");



        List<Item> tasks = restoreTasks(order);
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




        view = rootView;


        coordinatorLayout = (CoordinatorLayout) rootView.findViewById(R.id.coordinatorLayout);



        /*ViewPager viewPager = (ViewPager) rootView.findViewById(R.id.viewpager);

        

        TabLayout tabLayout = (TabLayout) rootView.findViewById(R.id.tabs);

        //tabLayout.setupWithViewPager(viewPager);



        tabLayout.canScrollHorizontally(View.SCROLL_AXIS_HORIZONTAL);

        tabLayout.setTabTextColors(getResources().getColor(R.color.abc_primary_text_material_dark),
                getResources().getColor(R.color.abc_primary_text_material_dark));*/





        /*searchView = (SearchView) rootView.findViewById(R.id.searchView);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                List<Item> items;

                if (newText.equals("")) {
                    taskAdapter.deleteAll();
                    items = restoreTasks(null);

                } else {
                    taskAdapter.deleteAll();
                    items = findTasks(newText);
                }

                taskAdapter.addTask(items);
                taskAdapter.notifyDataSetChanged();

                return false;
            }
        });*/




    }



    private void changeTaskDialog(Context context, final TaskModel taskModel) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(context);

        builder.setTitle("Changing task");

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







        tilTitle.setHint("Title");

        tilDescription.setHint("Description");
        tilDate.setHint("Date");
        tilTime.setHint("Time");

        etTitle.setText(taskModel.getName());
        etDescription.setText(taskModel.getDescription());

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yy");
        etDate.setText(dateFormat.format(taskModel.getDate()));

        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
        etTime.setText(timeFormat.format(taskModel.getDate()));

        if (etDate.getText() == null) {
            etDate.setEnabled(false);
        } else {
            cbAddTaskRemind.setChecked(true);
        }
        if (etTime.getText() == null) {
            etTime.setEnabled(false);
        } else {
            cbAddTaskRemind.setChecked(true);
        }




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

        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item,
                Constants.PRIORITY_LEVELS);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);



        spPriority.setAdapter(adapter);

        spPriority.setSelection(3 - (int) taskModel.getPriority());

        spPriority.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                taskModel.setPriority(3  - position);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });




        builder.setView(container);



        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
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

                long timeStamp = new Date().getTime();



                if (cbAddTaskRemind.isChecked() && dateFull != null) {

                    AlarmManagerHelper alarmManagerHelper = AlarmManagerHelper.getInstance();

                    alarmManagerHelper.setNotification(etTitle.getText().toString(),
                            calendar.getTimeInMillis(), timeStamp);







                }




                //addTask(taskItem);
                taskModel.setName(etTitle.getText().toString());
                taskModel.setDescription(etDescription.getText().toString());
                taskModel.setDate(calendar.getTimeInMillis());

                taskModel.setDoneBool(false);

                customSQLiteHelper.updateTask(taskModel);

                taskAdapter.notifyDataSetChanged();


                etDate.setEnabled(true);
                etTime.setEnabled(true);
            }


        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });


        final AlertDialog alertDialog = builder.show();





        final Button positiveButoon = alertDialog.getButton(DialogInterface.BUTTON_POSITIVE);

        positiveButoon.setEnabled(true);



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

        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item,
                Constants.PRIORITY_LEVELS);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spPriority.setAdapter(adapter);

        spPriority.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                priority[0] = 3  - position;
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

                long timeStamp = new Date().getTime();


                if (cbAddTaskRemind.isChecked() && dateFull != null) {

                    AlarmManagerHelper alarmManagerHelper = AlarmManagerHelper.getInstance();

                    alarmManagerHelper.setNotification(etTitle.getText().toString(),
                            calendar.getTimeInMillis(), timeStamp);







                }



                TaskModel taskItem = new TaskModel(etTitle.getText().toString(), etDescription.getText().toString(),
                        calendar.getTimeInMillis(), priority[0], 1, timeStamp);
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
                calendar.set(Calendar.SECOND, 0);
                calendar.set(Calendar.MILLISECOND, 0);
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

                List<Item> items = new ArrayList<>();
                String order = null;
                switch (which) {
                    case 0:
                        taskAdapter.deleteAll();
                        order = CustomSQLiteHelper.TASK_DATE_COLUMN;
                        items = restoreTasks(order);
                        break;
                    case 1:
                        taskAdapter.deleteAll();
                        order = CustomSQLiteHelper.TASK_NAME_COLUMN;
                        items = restoreTasks(order);
                        break;

                    case 2:
                        taskAdapter.deleteAll();
                        order = CustomSQLiteHelper.TASK_PRIORITY_COLUMN;
                        items = restoreTasks(order);
                        break;


                    case 3:
                        taskAdapter.deleteAll();
                        order = CustomSQLiteHelper.TASK_STATUS_COLUMN;
                        items = restoreTasks(order);
                        break;
                }

                if (!items.isEmpty()) {
                    preferenceManager.putString("order", order);
                    //taskAdapter.addTask(tasks);
                    taskAdapter.notifyDataSetChanged();
                }

            }
        });

        final AlertDialog alertDialog = alert.show();
    }




    private void addTask(Item item) {



        //list.add(item);

        //taskAdapter.addTask(list);





        ContentValues newValues = new ContentValues();
        // Задайте значения для каждой строки.

        if (item.isSection()) {

        } else {

            TaskModel taskModel = (TaskModel) item;

            newValues.put(CustomSQLiteHelper.USER_LOGIN_COLUMN, currentLogin);
            newValues.put(CustomSQLiteHelper.TASK_NAME_COLUMN, taskModel.getName());
            newValues.put(CustomSQLiteHelper.TASK_DESCRIPTION_COLUMN, taskModel.getDescription());
            newValues.put(CustomSQLiteHelper.TASK_DATE_COLUMN, taskModel.getDate());
            newValues.put(CustomSQLiteHelper.TASK_STATUS_COLUMN, taskModel.getDone());
            newValues.put(CustomSQLiteHelper.TASK_PRIORITY_COLUMN, taskModel.getPriority());

            newValues.put(CustomSQLiteHelper.TASK_TIME_COLUMN, taskModel.getTimeStamp());
            // Вставляем данные в базу
            long rowID = sqLiteDatabase.insert(CustomSQLiteHelper.DATABASE_TABLE, null, newValues);

            Log.d("rowID", " " + rowID);

            //preferenceManager.putTaskModel(taskAdapter.getAllTasks());
        }


        taskAdapter.deleteAll();


        taskAdapter.addTask(restoreTasks(null));
        taskAdapter.notifyDataSetChanged();


    }



    public List<Item> restoreTasks(String orderBy) {
        List<Item> items = new ArrayList<>();



        String selection = CustomSQLiteHelper.USER_LOGIN_COLUMN + " = ?";
        String[] selectionArgs = new String[] {currentLogin};

        Cursor c = sqLiteDatabase.query(CustomSQLiteHelper.DATABASE_TABLE, null, selection,
                selectionArgs, null, null, CustomSQLiteHelper.TASK_DATE_COLUMN);

        int sectionStatus = -1;

        if (c.moveToFirst()) {

            do {

                boolean needSection = true;

                String taskName = c.getString(c.getColumnIndex(CustomSQLiteHelper.TASK_NAME_COLUMN));
                String taskDescr = c.getString(c.getColumnIndex(CustomSQLiteHelper.TASK_DESCRIPTION_COLUMN));
                long taskDate = c.getLong(c.getColumnIndex(CustomSQLiteHelper.TASK_DATE_COLUMN));
                long priority = c.getLong(c.getColumnIndex(CustomSQLiteHelper.TASK_PRIORITY_COLUMN));
                long taskStatus = c.getLong(c.getColumnIndex(CustomSQLiteHelper.TASK_STATUS_COLUMN));
                long timeStamp = c.getLong(c.getColumnIndex(CustomSQLiteHelper.TASK_TIME_COLUMN));



                calendar.setTimeInMillis(taskDate);

                Log.d("task status = ", taskStatus + "");

                if (taskStatus != Constants.STATUS_DONE) {

                    if (calendar.get(Calendar.DAY_OF_YEAR) < Calendar.getInstance().get(Calendar.DAY_OF_YEAR)
                            && sectionStatus != Constants.STATUS_OVERDUE) {
                        sectionStatus = Constants.STATUS_OVERDUE;
                        Log.d("day", "overdue " + calendar.get(Calendar.DAY_OF_YEAR) + " " + Calendar.getInstance().get(Calendar.DAY_OF_YEAR));

                    } else if (calendar.get(Calendar.DAY_OF_YEAR) == Calendar.getInstance().get(Calendar.DAY_OF_YEAR)
                            && sectionStatus != Constants.STATUS_TODAY) {
                        sectionStatus = Constants.STATUS_TODAY;

                        Log.d("day", "today " + calendar.get(Calendar.DAY_OF_YEAR) + " " + Calendar.getInstance().get(Calendar.DAY_OF_YEAR));

                    } else if (calendar.get(Calendar.DAY_OF_YEAR) == Calendar.getInstance().get(Calendar.DAY_OF_YEAR) + 1
                            && sectionStatus != Constants.STATUS_TOMORROW) {
                        sectionStatus = Constants.STATUS_TOMORROW;
                        Log.d("day", "tomorow " + calendar.get(Calendar.DAY_OF_YEAR) + " " + Calendar.getInstance().get(Calendar.DAY_OF_YEAR));

                    } else if (calendar.get(Calendar.DAY_OF_YEAR) > Calendar.getInstance().get(Calendar.DAY_OF_YEAR)
                            && sectionStatus != Constants.STATUS_FUTURE) {
                        sectionStatus = Constants.STATUS_FUTURE;
                        Log.d("day", "future " + calendar.get(Calendar.DAY_OF_YEAR) + " " + Calendar.getInstance().get(Calendar.DAY_OF_YEAR));
                    } else {
                        needSection = false;
                        Log.d("day", "wtf "  + calendar.get(Calendar.DAY_OF_YEAR) + " " + Calendar.getInstance().get(Calendar.DAY_OF_YEAR));
                    }
                }

                if (needSection) {
                    SectionModel section = new SectionModel(sectionStatus);

                    items.add(section);
                }

                TaskModel item = new TaskModel(taskName, taskDescr, taskDate, priority, taskStatus, timeStamp);

                items.add(item);
            } while (c.moveToNext());

        } else
            Log.d("DataBase", "0 rows");
        c.close();



        return items;
    }






    AdapterView.OnItemLongClickListener longClickListener = new AdapterView.OnItemLongClickListener() {
        @Override
        public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

            Log.d("long", "click");

            //removeTaskDialog(getActivity(), position);
            Item item = taskAdapter.getItem(position);

            if (!item.isSection()) {

                changeTaskDialog(getActivity(), (TaskModel) item);
            }


            return true;
        }
    };



    /*private void removeTaskDialog(Context context, final int position) {

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

                            long count = sqLiteDatabase.delete(CustomSQLiteHelper.DATABASE_TABLE,
                                    CustomSQLiteHelper.TASK_TIME_COLUMN
                                    + " = " + deletingTask.getTimeStamp(), null);

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

    }*/




    public List<Item> findTasks(String key) {
        List<Item> tasks = new ArrayList<>();



        String selection = CustomSQLiteHelper.TASK_NAME_COLUMN + " LIKE ? AND "
                + CustomSQLiteHelper.USER_LOGIN_COLUMN + " = ?";
        String[] selectionArgs = new String[] {"%" + key + "%", currentLogin};


        Cursor c = sqLiteDatabase.query(CustomSQLiteHelper.DATABASE_TABLE, null, selection,
                selectionArgs, null, null, null);



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



    AdapterView.OnItemClickListener itemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            Log.d("onitemClickListener", "running");
            Item item = taskAdapter.getItem(position);

            if (!item.isSection()) {


                changeTaskDialog(getActivity(), (TaskModel) item);
            }
        }

    };




    @Override
    public void onCreateOptionsMenu (Menu menu, MenuInflater inflater) {
        Log.i("FragCreateList","onCreateOptionsMenu called");
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_main, menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();


        switch (id) {
            //noinspection SimplifiableIfStatement
            case R.id.action_logout:
                preferenceManager.putBoolean("remembered", false);
                AuthorisationFragment authorisationFragment = new AuthorisationFragment();
                customFragmentManager.setFragment(R.id.container, authorisationFragment, false);
                return true;

            case R.id.action_create_new_database:
                deleteAllTasksFromDataBase();
                break;

            case R.id.action_sorting:
                showSortingDialog(getActivity());
                break;
        }
        return super.onOptionsItemSelected(item);
    }





}
