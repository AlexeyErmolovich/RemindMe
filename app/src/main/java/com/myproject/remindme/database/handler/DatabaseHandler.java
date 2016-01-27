package com.myproject.remindme.database.handler;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.myproject.remindme.database.data.Birthday;
import com.myproject.remindme.database.data.Idea;
import com.myproject.remindme.database.data.Todo;

import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;

/**
 * Created by Alexey on 1/19/2016.
 */
public class DatabaseHandler extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 3;
    public static final String DATABASE_NAME = "RemindMe";

    private static DatabaseHandler databaseHandler;

    private DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public static DatabaseHandler getInstance(Context context) {
        if (databaseHandler == null) {
            databaseHandler = new DatabaseHandler(context);
        }
        return databaseHandler;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(IdeasTable.CREATE_TABLE);
        db.execSQL(TodoTable.CREATE_TABLE);
        db.execSQL(TodoItemTable.CREATE_TABLE);
        db.execSQL(BirthdaysTable.CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + IdeasTable.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + TodoTable.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + TodoItemTable.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + BirthdaysTable.TABLE_NAME);
        onCreate(db);
    }

    public IdeasTable getIdeasTable() {
        return new IdeasTable();
    }

    public TodoTable getTodoTable() {
        return new TodoTable();
    }

    public TodoItemTable getTodoItemTable() {
        return new TodoItemTable();
    }

    public BirthdaysTable getBirthdaysTable() {
        return new BirthdaysTable();
    }

    public class IdeasTable {

        private static final String TABLE_NAME = "Ideas";
        private static final String KEY_ID = "id";
        private static final String KEY_HANDLER = "Handler";
        private static final String KEY_IDEA = "Idea";
        private static final String KEY_KEYWORD = "Keyword";
        private static final String KEY_DATEADDED = "Date_Added";

        private static final String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + "("
                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + KEY_HANDLER + " TEXT NOT NULL, "
                + KEY_IDEA + " TEXT NOT NULL, "
                + KEY_KEYWORD + " TEXT NOT NULL, "
                + KEY_DATEADDED + " INT NOT NULL)";

        private IdeasTable() {
        }

        public void add(Idea idea) {
            SQLiteDatabase database = databaseHandler.getWritableDatabase();
            ContentValues values = new ContentValues();

            values.put(KEY_HANDLER, idea.getHeader());
            values.put(KEY_IDEA, idea.getIdea());
            values.put(KEY_KEYWORD, idea.getKeyword());
            values.put(KEY_DATEADDED, idea.getDateAdded().getTimeInMillis());

            long id = database.insert(TABLE_NAME, null, values);
            idea.setId(id);
            database.close();
        }

        public List<Idea> getAll() {
            List<Idea> ideaList = new ArrayList<>();
            String selectQuery = "SELECT * FROM " + TABLE_NAME;

            SQLiteDatabase database = databaseHandler.getReadableDatabase();
            Cursor cursor = database.rawQuery(selectQuery, null);

            if (cursor.moveToFirst()) {
                do {
                    GregorianCalendar calendar = new GregorianCalendar();
                    Idea idea = new Idea(
                            cursor.getString(cursor.getColumnIndex(KEY_HANDLER)),
                            cursor.getString(cursor.getColumnIndex(KEY_IDEA)),
                            cursor.getString(cursor.getColumnIndex(KEY_KEYWORD)));
                    idea.setId(cursor.getLong(cursor.getColumnIndex(KEY_ID)));
                    calendar.setTimeInMillis(cursor.getLong(cursor.getColumnIndex(KEY_DATEADDED)));
                    idea.setDateAdded(calendar);
                    ideaList.add(idea);
                } while (cursor.moveToNext());
            }

            database.close();
            return ideaList;
        }

        public int update(Idea idea) {
            SQLiteDatabase database = databaseHandler.getWritableDatabase();

            ContentValues values = new ContentValues();
            values.put(KEY_HANDLER, idea.getHeader());
            values.put(KEY_IDEA, idea.getIdea());
            values.put(KEY_KEYWORD, idea.getKeyword());

            int update = database.update(TABLE_NAME, values, KEY_ID + " = ?",
                    new String[]{String.valueOf(idea.getId())});

            database.close();
            return update;
        }

        public boolean remove(Idea idea) {
            SQLiteDatabase database = databaseHandler.getWritableDatabase();
            database.delete(TABLE_NAME, KEY_ID + " = ?", new String[]{String.valueOf(idea.getId())});
            database.close();
            return false;
        }

        public void removeAll() {
            SQLiteDatabase database = databaseHandler.getWritableDatabase();
            database.delete(TABLE_NAME, null, null);
            database.close();
        }
    }

    public class TodoTable {

        private static final String TABLE_NAME = "Todo";
        private static final String KEY_ID = "id";
        private static final String KEY_HANDLER = "Handler";
        private static final String KEY_KEYWORD = "Keyword";
        private static final String KEY_DATEADDED = "Date_Added";

        private static final String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + "("
                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + KEY_HANDLER + " TEXT NOT NULL, "
                + KEY_KEYWORD + " TEXT NOT NULL, "
                + KEY_DATEADDED + " INT NOT NULL)";

        private TodoItemTable todoItemTable;

        private TodoTable() {
            todoItemTable = new TodoItemTable();
        }

        public void add(Todo todo) {
            SQLiteDatabase database = databaseHandler.getWritableDatabase();
            ContentValues values = new ContentValues();

            values.put(KEY_HANDLER, todo.getHeader());
            values.put(KEY_KEYWORD, todo.getKeyword());
            values.put(KEY_DATEADDED, todo.getDateAdded().getTimeInMillis());

            long id = database.insert(TABLE_NAME, null, values);
            todo.setId(id);

            todoItemTable.setItems(todo);

            database.close();
        }

        public List<Todo> getAll() {
            List<Todo> todoList = new ArrayList<>();
            String selectQuery = "SELECT * FROM " + TABLE_NAME;

            SQLiteDatabase database = databaseHandler.getReadableDatabase();
            Cursor cursor = database.rawQuery(selectQuery, null);

            if (cursor.moveToFirst()) {
                do {
                    GregorianCalendar calendar = new GregorianCalendar();
                    Todo todo = new Todo(
                            cursor.getString(cursor.getColumnIndex(KEY_HANDLER)),
                            cursor.getString(cursor.getColumnIndex(KEY_KEYWORD)));
                    todo.setId(cursor.getLong(cursor.getColumnIndex(KEY_ID)));
                    calendar.setTimeInMillis(cursor.getLong(cursor.getColumnIndex(KEY_DATEADDED)));
                    todo.setDateAdded(calendar);
                    todo.setTodo(todoItemTable.getItems(todo));
                    todoList.add(todo);
                } while (cursor.moveToNext());
            }

            database.close();
            return todoList;
        }

        public int update(Todo todo) {
            SQLiteDatabase database = databaseHandler.getWritableDatabase();

            ContentValues values = new ContentValues();
            values.put(KEY_HANDLER, todo.getHeader());
            values.put(KEY_KEYWORD, todo.getKeyword());

            int update = database.update(TABLE_NAME, values, KEY_ID + " = ?",
                    new String[]{String.valueOf(todo.getId())});

            todoItemTable.updateItems(todo);

            database.close();
            return update;
        }

        public boolean remove(Todo todo) {
            SQLiteDatabase database = databaseHandler.getWritableDatabase();
            database.delete(TABLE_NAME, KEY_ID + " = ?", new String[]{String.valueOf(todo.getId())});
            todoItemTable.removeItems(todo);
            database.close();
            return false;
        }

        public void removeAll() {
            SQLiteDatabase database = databaseHandler.getWritableDatabase();
            database.delete(TABLE_NAME, null, null);
            todoItemTable.removeAll();
            database.close();
        }
    }

    public class TodoItemTable {
        private static final String TABLE_NAME = "TodoItem";
        private static final String KEY_ID = "id";
        private static final String KEY_TODOID = "TodoId";
        private static final String KEY_CHECKED = "Checked";
        private static final String KEY_TODO = "Todo";

        private static final String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + "("
                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + KEY_TODOID + " INTEGER NOT NULL, "
                + KEY_CHECKED + " INTEGER NOT NULL, "
                + KEY_TODO + " TEXT NOT NULL)";

        public TodoItemTable() {
        }


        public void add(long todoId, Todo.TodoItem todoItem) {
            SQLiteDatabase database = databaseHandler.getWritableDatabase();
            ContentValues values = new ContentValues();

            values.put(KEY_TODOID, todoId);
            values.put(KEY_CHECKED, todoItem.isChecked());
            values.put(KEY_TODO, todoItem.getTodoItem());

            long id = database.insert(TABLE_NAME, null, values);
            todoItem.setId(id);

            database.close();
        }

        public List<Todo.TodoItem> getItems(Todo todo) {
            List<Todo.TodoItem> todoItems = new ArrayList<>();
            String selectQuery = "SELECT * FROM " + TABLE_NAME + " WHERE " + KEY_TODOID + "=" + todo.getId();

            SQLiteDatabase database = databaseHandler.getReadableDatabase();
            Cursor cursor = database.rawQuery(selectQuery, null);

            if (cursor.moveToFirst()) {
                do {
                    int aInt = cursor.getInt(cursor.getColumnIndex(KEY_CHECKED));
                    boolean checked;
                    if (aInt == 1) {
                        checked = true;
                    } else {
                        checked = false;
                    }
                    Todo.TodoItem todoItem = todo.getTodoItem(
                            checked,
                            cursor.getString(cursor.getColumnIndex(KEY_TODO)));
                    todoItem.setId(cursor.getLong(cursor.getColumnIndex(KEY_ID)));
                    todoItems.add(todoItem);
                } while (cursor.moveToNext());
            }

            database.close();
            return todoItems;
        }

        public void setItems(Todo todo) {
            List<Todo.TodoItem> todoItems = todo.getTodo();
            SQLiteDatabase database = databaseHandler.getWritableDatabase();

            for (Todo.TodoItem item : todoItems) {
                ContentValues values = new ContentValues();
                values.put(KEY_TODOID, todo.getId());
                values.put(KEY_CHECKED, item.isChecked());
                values.put(KEY_TODO, item.getTodoItem());

                long id = database.insert(TABLE_NAME, null, values);
                item.setId(id);
            }
            database.close();
        }

        public int update(long todoId, Todo.TodoItem todoItem) {
            SQLiteDatabase database = databaseHandler.getWritableDatabase();

            ContentValues values = new ContentValues();
            values.put(KEY_TODOID, todoId);
            values.put(KEY_CHECKED, todoItem.isChecked());
            values.put(KEY_TODO, todoItem.getTodoItem());
            int update = database.update(TABLE_NAME, values, KEY_ID + " = ?",
                    new String[]{String.valueOf(todoItem.getId())});

            database.close();
            return update;
        }

        public void updateItems(Todo todo) {
            List<Todo.TodoItem> todoItems = todo.getTodo();
            SQLiteDatabase database = databaseHandler.getWritableDatabase();

            for (Todo.TodoItem item : todoItems) {
                ContentValues values = new ContentValues();
                values.put(KEY_TODOID, todo.getId());
                values.put(KEY_CHECKED, item.isChecked());
                values.put(KEY_TODO, item.getTodoItem());
                database.update(TABLE_NAME, values, KEY_ID + " = ?",
                        new String[]{String.valueOf(item.getId())});
            }
            database.close();
        }

        public boolean remove(Todo.TodoItem todoItem) {
            SQLiteDatabase database = databaseHandler.getWritableDatabase();
            database.delete(TABLE_NAME, KEY_ID + " = ?", new String[]{String.valueOf(todoItem.getId())});
            database.close();
            return false;
        }

        public void removeItems(Todo todo) {
            List<Todo.TodoItem> todoItems = todo.getTodo();
            SQLiteDatabase database = databaseHandler.getWritableDatabase();

            for (Todo.TodoItem item : todoItems) {
                database.delete(TABLE_NAME, KEY_ID + " = ?", new String[]{String.valueOf(item.getId())});
            }
            database.close();
        }

        public void removeAll() {
            SQLiteDatabase database = databaseHandler.getWritableDatabase();
            database.delete(TABLE_NAME, null, null);
            database.close();
        }
    }

    public class BirthdaysTable {

        private static final String TABLE_NAME = "Birthdays";
        private static final String KEY_ID = "id";
        private static final String KEY_FIRSTNAME = "FirstName";
        private static final String KEY_LASTNAME = "LastNAme";
        private static final String KEY_BIRTHDAY = "Birthday";
        private static final String KEY_RELATIONSHIP = "Relationship";
        private static final String KEY_DATEADDED = "Date_Added";

        private static final String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + "("
                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + KEY_FIRSTNAME + " TEXT NOT NULL, "
                + KEY_LASTNAME + " TEXT, "
                + KEY_BIRTHDAY + " INT NOT NULL, "
                + KEY_RELATIONSHIP + " TEXT NOT NULL, "
                + KEY_DATEADDED + " INT NOT NULL)";

        private BirthdaysTable() {
        }

        public void add(Birthday birthday) {
            SQLiteDatabase database = databaseHandler.getWritableDatabase();
            ContentValues values = new ContentValues();

            values.put(KEY_FIRSTNAME, birthday.getFirstName());
            values.put(KEY_LASTNAME, birthday.getLastName());
            values.put(KEY_BIRTHDAY, birthday.getBirthday().getTimeInMillis());
            values.put(KEY_RELATIONSHIP, birthday.getRelationship());
            values.put(KEY_DATEADDED, birthday.getDateAdded().getTimeInMillis());

            long id = database.insert(TABLE_NAME, null, values);
            birthday.setId(id);
            database.close();
        }

        public List<Birthday> getAll() {
            List<Birthday> birthdayList = new ArrayList<>();
            String selectQuery = "SELECT * FROM " + TABLE_NAME;

            SQLiteDatabase database = databaseHandler.getReadableDatabase();
            Cursor cursor = database.rawQuery(selectQuery, null);

            if (cursor.moveToFirst()) {
                do {
                    GregorianCalendar calendar = new GregorianCalendar();
                    calendar.setTimeInMillis(cursor.getLong(cursor.getColumnIndex(KEY_BIRTHDAY)));
                    Birthday birthday = new Birthday(
                            cursor.getString(cursor.getColumnIndex(KEY_FIRSTNAME)),
                            cursor.getString(cursor.getColumnIndex(KEY_LASTNAME)),
                            calendar,
                            cursor.getString(cursor.getColumnIndex(KEY_RELATIONSHIP)));
                    birthday.setId(cursor.getLong(cursor.getColumnIndex(KEY_ID)));
                    calendar = new GregorianCalendar();
                    calendar.setTimeInMillis(cursor.getLong(cursor.getColumnIndex(KEY_DATEADDED)));
                    birthday.setDateAdded(calendar);
                    birthdayList.add(birthday);
                } while (cursor.moveToNext());
            }

            database.close();
            return birthdayList;
        }

        public int update(Birthday birthday) {
            SQLiteDatabase database = databaseHandler.getWritableDatabase();

            ContentValues values = new ContentValues();
            values.put(KEY_FIRSTNAME, birthday.getFirstName());
            values.put(KEY_LASTNAME, birthday.getLastName());
            values.put(KEY_BIRTHDAY, birthday.getBirthday().getTimeInMillis());
            values.put(KEY_RELATIONSHIP, birthday.getRelationship());

            int update = database.update(TABLE_NAME, values, KEY_ID + " = ?",
                    new String[]{String.valueOf(birthday.getId())});

            database.close();
            return update;
        }

        public boolean remove(Birthday birthday) {
            SQLiteDatabase database = databaseHandler.getWritableDatabase();
            database.delete(TABLE_NAME, KEY_ID + " = ?", new String[]{String.valueOf(birthday.getId())});
            database.close();
            return false;
        }

        public void removeAll() {
            SQLiteDatabase database = databaseHandler.getWritableDatabase();
            database.delete(TABLE_NAME, null, null);
            database.close();
        }
    }
}
