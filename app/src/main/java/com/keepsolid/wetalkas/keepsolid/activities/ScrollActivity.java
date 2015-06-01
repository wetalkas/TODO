package com.keepsolid.wetalkas.keepsolid.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v4.widget.AutoScrollHelper;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;


import com.keepsolid.wetalkas.keepsolid.Constants;
import com.keepsolid.wetalkas.keepsolid.sdk.CustomPreferenceManager;
import com.keepsolid.wetalkas.keepsolid.R;
import com.keepsolid.wetalkas.keepsolid.sdk.SQLiteHelperCustom;
import com.keepsolid.wetalkas.keepsolid.sdk.Sdk;
import com.melnykov.fab.FloatingActionButton;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

public class ScrollActivity extends ActionBarActivity {



    TaskAdapter taskAdapter;

    ListView lvTasks;

    Calendar calendar;

    FloatingActionButton fab;

    SearchView searchView;


    SQLiteHelperCustom sqLiteHelperCustom;
    SQLiteDatabase sqLiteDatabase;


    CustomPreferenceManager preferenceManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scroll);

        CustomPreferenceManager.getInstance().init(getApplicationContext(), "");


        calendar = Calendar.getInstance();


        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        if (toolbar != null) {

            Log.d("toolbar", "not null");

            //toolbar.setPopupTheme();

            toolbar.setTitle("TODO");
            toolbar.setTitleTextColor(getResources().getColor(R.color.secondary_text_default_material_light));



            setSupportActionBar(toolbar);

        }

        setUI();


        sqLiteHelperCustom = new SQLiteHelperCustom(this, "mydatabase.db", null, 1);

        sqLiteDatabase = sqLiteHelperCustom.getWritableDatabase();


        taskAdapter = new TaskAdapter(ScrollActivity.this);

        lvTasks.setAdapter(taskAdapter);

        lvTasks.setOnItemLongClickListener(longClickListener);


        lvTasks.setDividerHeight(0);
        lvTasks.setDivider(null);

        preferenceManager = CustomPreferenceManager.getInstance();

        List<TaskItem> tasks = restoreTasks();



        taskAdapter.addItem(tasks);

        taskAdapter.notifyDataSetChanged();



        //CustomPreferenceManager






    }




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_scroll, menu);





        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }


        if (id == R.id.action_create_new_database) {
            sqLiteHelperCustom.onUpgrade(sqLiteDatabase, Integer.valueOf(SQLiteHelperCustom.DATABASE_VERSION),
                    Integer.valueOf(SQLiteHelperCustom.DATABASE_VERSION) + 1);

            taskAdapter.deleteAll();
            taskAdapter.notifyDataSetChanged();
        }


        return super.onOptionsItemSelected(item);
    }



    private void setUI() {
        lvTasks = (ListView)findViewById(R.id.lvTasks);

        fab = (FloatingActionButton)findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addTaskDialog(ScrollActivity.this);
            }
        });

        searchView = (SearchView) findViewById(R.id.searchView);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                List<TaskItem> tasks;


                Log.d("onQueryTextChanged", "newText = " + newText);


                if (newText.equals("")) {




                    taskAdapter.deleteAll();

                    tasks = restoreTasks();


                } else {

                    taskAdapter.deleteAll();

                    tasks = findTasks(newText);

                }

                taskAdapter.addItem(tasks);
                taskAdapter.notifyDataSetChanged();

                return false;
            }
        });

    }








    private void addTaskDialog(Context context) {
        final AlertDialog.Builder alert = new AlertDialog.Builder(context);

        alert.setTitle("Adding task");

        ScrollView container = (ScrollView) getLayoutInflater().inflate(R.layout.dialog_add_new_task, null);

        final EditText etTitle = (EditText) container.findViewById(R.id.etDialogAddTaskName);
        final EditText etDescription = (EditText) container.findViewById(R.id.etDialogAddTaskDescription);
        final EditText etDate = (EditText) container.findViewById(R.id.etDialogAddTaskDate);
        final EditText etTime = (EditText) container.findViewById(R.id.etDialogAddTaskTime);

        final Spinner spPriority = (Spinner) container.findViewById(R.id.spDialogAddTaskPriority);





        etDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog(ScrollActivity.this, etDate);


            }
        });


        etTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTimePickerDialog(ScrollActivity.this, etTime);
            }
        });


        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,
                Constants.PRIORITY_LEVELS);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);



        spPriority.setAdapter(adapter);


        spPriority.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

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


                try {

                    dateFull = dateFormat.parse(dateFullString);

                } catch (Exception e) {
                    e.printStackTrace();
                }

                TaskItem taskItem = new TaskItem(etTitle.getText().toString(), etDescription.getText().toString(),
                        dateFull.getTime(), false, new Date().getTime());
                addTask(taskItem);

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






    private void addTask(TaskItem taskItem) {


        List<TaskItem> list = new ArrayList<>();

        list.add(taskItem);

        taskAdapter.addItem(list);

        ContentValues newValues = new ContentValues();
        // Задайте значения для каждой строки.
        newValues.put(SQLiteHelperCustom.TASK_NAME_COLUMN, taskItem.name);
        newValues.put(SQLiteHelperCustom.TASK_DESCRIPTION_COLUMN, taskItem.description);
        newValues.put(SQLiteHelperCustom.TASK_DATE_COLUMN, taskItem.date);
        newValues.put(SQLiteHelperCustom.TASK_STATUS_COLUMN, taskItem.done);
        newValues.put(SQLiteHelperCustom.TASK_PRIORITY_COLUMN, "pr");

        newValues.put(SQLiteHelperCustom.TASK_TIME_COLUMN, taskItem.timeStamp);
        // Вставляем данные в базу
        long rowID = sqLiteDatabase.insert("tasks", null, newValues);

        Log.d("rowID", " " + rowID);

        //preferenceManager.putTaskModel(taskAdapter.getAllTasks());

        taskAdapter.notifyDataSetChanged();
    }



    public List<TaskItem> restoreTasks() {
        List<TaskItem> tasks = new ArrayList<>();

        Cursor c = sqLiteDatabase.query("tasks", null, null, null, null, null, null);



        // ставим позицию курсора на первую строку выборки
        // если в выборке нет строк, вернется false
        if (c.moveToFirst()) {

            // определяем номера столбцов по имени в выборке


            do {
                // получаем значения по номерам столбцов и пишем все в лог

                String taskName = c.getString(c.getColumnIndex(SQLiteHelperCustom.TASK_NAME_COLUMN));
                String taskDescr = c.getString(c.getColumnIndex(SQLiteHelperCustom.TASK_DESCRIPTION_COLUMN));
                long taskDate = c.getLong(c.getColumnIndex(SQLiteHelperCustom.TASK_DATE_COLUMN));
                boolean taskStatus = Boolean.getBoolean(c.getString(c.getColumnIndex(SQLiteHelperCustom.TASK_STATUS_COLUMN)));
                long timeStamp = c.getLong(c.getColumnIndex(SQLiteHelperCustom.TASK_TIME_COLUMN));

                TaskItem item = new TaskItem(taskName, taskDescr, taskDate, taskStatus, timeStamp);


                tasks.add(item);




                // переход на следующую строку
                // а если следующей нет (текущая - последняя), то false - выходим из цикла
            } while (c.moveToNext());


        } else
            Log.d("DataBase", "0 rows");
        c.close();



        return tasks;
    }




    AdapterView.OnItemLongClickListener longClickListener = new AdapterView.OnItemLongClickListener() {
        @Override
        public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

            //TaskItem item = taskAdapter.getItem(position);



            Log.d("long", "click");

            removeTaskDialog(ScrollActivity.this, position);









            return false;
        }
    };



    private void removeTaskDialog(Context context, final int position) {

        final AlertDialog.Builder alert = new AlertDialog.Builder(context);

        alert.setTitle("Remove task");






        alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {



                long count = sqLiteDatabase.delete("tasks", SQLiteHelperCustom.TASK_TIME_COLUMN
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





    public List<TaskItem> findTasks(String key) {
        List<TaskItem> tasks = new ArrayList<>();


        String[] columns = new String[] { key };

        String selection = "task_name = ?";
        String[] selectionArgs = new String[] { key };


        Cursor c = sqLiteDatabase.query("tasks", null, selection, selectionArgs, null, null, null);



        // ставим позицию курсора на первую строку выборки
        // если в выборке нет строк, вернется false
        if (c.moveToFirst()) {

            // определяем номера столбцов по имени в выборке


            do {
                // получаем значения по номерам столбцов и пишем все в лог

                String taskName = c.getString(c.getColumnIndex(SQLiteHelperCustom.TASK_NAME_COLUMN));
                String taskDescr = c.getString(c.getColumnIndex(SQLiteHelperCustom.TASK_DESCRIPTION_COLUMN));
                long taskDate = c.getLong(c.getColumnIndex(SQLiteHelperCustom.TASK_DATE_COLUMN));
                boolean taskStatus = Boolean.getBoolean(c.getString(c.getColumnIndex(SQLiteHelperCustom.TASK_STATUS_COLUMN)));
                long timeStamp = c.getLong(c.getColumnIndex(SQLiteHelperCustom.TASK_TIME_COLUMN));

                TaskItem item = new TaskItem(taskName, taskDescr, taskDate, taskStatus, timeStamp);


                tasks.add(item);

                Log.d("what", "");




                // переход на следующую строку
                // а если следующей нет (текущая - последняя), то false - выходим из цикла
            } while (c.moveToNext());


        } else
            Log.d("DataBase", "0 rows");
        c.close();


        Log.d("query", key);






        return tasks;
    }









    public class TaskAdapter extends ArrayAdapter<TaskItem> {

        private List<TaskItem> tasks = new ArrayList<>();

        public TaskAdapter(Context context) {
            super(context, R.layout.element_list_view);
        }



        @Override
        public int getCount() {
            return tasks.size();
        }

        public void addItem(List<TaskItem> data) {
            this.tasks.addAll(data);
        }

        public List<TaskItem> getAllTasks() {
            return  tasks;
        }

        public TaskItem getTask(int position) {
            return tasks.get(position);

        }

        public void deleteAll() {
            this.tasks = new ArrayList<TaskItem>();;
        }

        public void deleteItem(int position) {
            this.tasks.remove(position);
        }



        @Override
        public int getPosition(TaskItem item) {
            return tasks.indexOf(item);
        }


        @Override
        public TaskItem getItem(int position) {
            return tasks.get(position);
        }


        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder;

            if(convertView == null) {
                // inflate the GridView item layout
                LayoutInflater inflater = LayoutInflater.from(getContext());
                convertView = inflater.inflate(R.layout.element_list_view, parent, false);


                // initialize the view holder
                viewHolder = new ViewHolder();
                viewHolder.tvTaskName = (TextView) convertView.findViewById(R.id.tvTaskName);
                viewHolder.tvTaskDate = (TextView) convertView.findViewById(R.id.tvTaskDate);
                viewHolder.tvTaskDescription = (TextView) convertView.findViewById(R.id.tvTaskDescription);
                viewHolder.cbTaskDone = (CheckBox) convertView.findViewById(R.id.cbTaskDone);

                convertView.setTag(viewHolder);
            } else {
                // recycle the already inflated view
                viewHolder = (ViewHolder) convertView.getTag();
            }

            // update the item view
            TaskItem item = tasks.get(position);



            viewHolder.tvTaskName.setText(item.name);
            viewHolder.tvTaskDate.setText(Sdk.getDateWithCurrentLocale(item.date, ScrollActivity.this));
            viewHolder.tvTaskDescription.setText(item.description);
            viewHolder.cbTaskDone.setChecked(item.done);


            return convertView;
        }

    }

    private static class ViewHolder {
        TextView tvTaskName;
        TextView tvTaskDate;
        TextView tvTaskDescription;
        CheckBox cbTaskDone;
    }




    public class TaskItem {
        public final String name;
        public final long date;
        public final String description;
        public final boolean done;
        public final long timeStamp;



        public TaskItem(String name, String description, long date, boolean done, long timeStamp) {
            this.name = name;
            this.date = date;
            this.description = description;
            this.done = done;
            this.timeStamp = timeStamp;

        }
    }

    public TaskItem getNewTask(String name, long date, String description) {
        return /*new TaskItem(name, date, description, false)*/null;
    }

}
