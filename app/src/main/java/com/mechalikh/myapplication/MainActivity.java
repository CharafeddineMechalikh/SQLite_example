package com.mechalikh.myapplication;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;


public class MainActivity extends AppCompatActivity {

    private MyDatabaseHelper dbHelper;
    private SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dbHelper = new MyDatabaseHelper(this);
        db = dbHelper.getWritableDatabase();

        // Insert data into the database
        ContentValues values = new ContentValues();
        values.put("name", "John Doe");
        values.put("age", 25);
        long newRowId = db.insert("mytable", null, values);

        // Query the database and display the results
        Cursor cursor = db.query("mytable", null, null, null, null, null, null);
        TextView textView = findViewById(R.id.textView);
        StringBuilder data = new StringBuilder();
        if (cursor.moveToFirst()) {
            do {
                String name = cursor.getString(cursor.getColumnIndex("name"));
                int age = cursor.getInt(cursor.getColumnIndex("age"));
                data.append("Name: ").append(name).append(", Age: ").append(age).append("\n");
            } while (cursor.moveToNext());
        }
        cursor.close();
        textView.setText(data.toString());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        dbHelper.close();
    }

    public class MyDatabaseHelper extends SQLiteOpenHelper {

        private static final String DATABASE_NAME = "mydatabase.db";
        private static final int DATABASE_VERSION = 1;

        public MyDatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            String createTableQuery = "CREATE TABLE mytable (_id INTEGER PRIMARY KEY, name TEXT, age INTEGER);";
            db.execSQL(createTableQuery);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            if (oldVersion < 2) {
                db.execSQL("ALTER TABLE mytable ADD COLUMN email TEXT;");
            }
        }
    }
}
