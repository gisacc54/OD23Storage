package tz.ac.dit.od23storage;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class DBActivity extends AppCompatActivity {

    DatabaseHelper dbHelper;
    EditText editName, editEmail, editPassword;
    ArrayAdapter<String> adapter;
    ArrayList<String> users = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_db);

        dbHelper = new DatabaseHelper(this);

        editName = findViewById(R.id.editName);
        editEmail = findViewById(R.id.editEmail);
        editPassword = findViewById(R.id.editPassword);
        Button btnSave = findViewById(R.id.btnSave);
        ListView listUsers = findViewById(R.id.listUsers);

        // Built-in adapter + built-in row layout — no custom XML needed
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, users);
        listUsers.setAdapter(adapter);

        btnSave.setOnClickListener(v -> saveUser());

        loadUsers(); // show existing users when the screen opens
    }

    private void saveUser() {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        // One ContentValues = one row. Keys are the column names.
        ContentValues values = new ContentValues();
        values.put("name", editName.getText().toString());
        values.put("email", editEmail.getText().toString());
        values.put("password", editPassword.getText().toString());

        db.insert("users", null, values);

        // Clear the form
        editName.setText("");
        editEmail.setText("");
        editPassword.setText("");

        loadUsers(); // refresh the list
    }

    private void loadUsers() {
        users.clear();

        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM users", null);

        while (cursor.moveToNext()) {
            String name = cursor.getString(cursor.getColumnIndexOrThrow("name"));
            String email = cursor.getString(cursor.getColumnIndexOrThrow("email"));
            users.add(name + " - " + email);
        }
        cursor.close();

        adapter.notifyDataSetChanged();
    }
}
