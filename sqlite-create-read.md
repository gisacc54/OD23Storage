# SQLite in Android (Java) — Save & Read a User

A simple step-by-step guide to store users (name, email, password) in a local SQLite database, and read them back.

---

## What you'll build

- `activity_db.xml` — the input form and list
- `DatabaseHelper.java` — creates the database and the table
- `DBActivity.java` — saves a user and reads users back into a simple list

Only 3 files — that's all.

---

## Step 1: Add an input form (layout)

**Where:** in `res/layout/activity_db.xml` (replace its whole content).

Add fields for name, email, password, plus a save button and a list view:

```xml
<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
    android:orientation="vertical"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:padding="16dp">

    <EditText
        android:id="@+id/editName"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:hint="Name" />

    <EditText
        android:id="@+id/editEmail"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:hint="Email" />

    <EditText
        android:id="@+id/editPassword"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:hint="Password" />

    <Button
        android:id="@+id/btnSave"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:text="Save User" />

    <ListView
        android:id="@+id/listUsers"
        android:layout_width="match_parent"
        android:layout_height="match_parent" />

</LinearLayout>
```

What each part does:

- `LinearLayout` with `android:orientation="vertical"` — stacks its children top to bottom, one under the other.
- `android:layout_width="match_parent"` — as wide as the screen; `wrap_content` — only as big as its content needs.
- `android:padding="16dp"` — space between the screen edge and the views.
- `EditText` — a text input box. `android:hint` is the gray text shown while the box is empty.
- `android:id="@+id/editName"` — gives the view a **name**. In Java we use these ids with `findViewById(...)` to reach the views.
- `Button` — the user presses it to save; `android:text` is the label on the button.
- `ListView` — a scrollable list; it will show one row per saved user.

---

## Step 2: Create the Database Helper

**Where:** create a **new file** `DatabaseHelper.java`, in the same package (folder) as your activity.

This class creates the database file and the `users` table the first time the app needs it. This is the complete file — copy all of it:

```java
package tz.ac.dit.od23storage;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    public DatabaseHelper(Context context) {
        // database file name = "MyApp.db", version = 1
        super(context, "MyApp.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE users (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "name TEXT, " +
                "email TEXT, " +
                "password TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Simplest possible upgrade: delete the old table and create a new one
        db.execSQL("DROP TABLE IF EXISTS users");
        onCreate(db);
    }
}
```

What each part does:

- `extends SQLiteOpenHelper` — our class becomes a database helper. Android then handles opening, creating and upgrading the database for us.
- `super(context, "MyApp.db", null, 1)` — the database is stored in a file called `MyApp.db`, and its version is `1`. (The `null` is an advanced option we don't need.)
- `onCreate()` — runs **once**, the first time the database is opened. This is where the table is created.
- `db.execSQL(...)` — executes one SQL statement (a statement that returns no rows, like `CREATE TABLE`).
- The SQL itself — `CREATE TABLE users (...)` creates a table named `users` with 4 columns:
  - `id INTEGER PRIMARY KEY AUTOINCREMENT` — a number that identifies each row. Android fills it automatically: 1, 2, 3, …
  - `name TEXT`, `email TEXT`, `password TEXT` — text columns for the user's data.
- `onUpgrade()` — runs only if you later change the version number `1` to `2` (for example after changing the table). The simplest upgrade: `DROP TABLE` deletes the old table, then `onCreate(db)` builds the new one.
- You never call these methods yourself — Android calls them for you at the right moment.

---

## Step 3: The starting point of `DBActivity.java`

From here, everything goes into **one file**: `DBActivity.java`. When Android Studio created this activity, it already came with the `onCreate()` method:

```java
package tz.ac.dit.od23storage;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
// (1) MORE IMPORTS ................. Step 4

public class DBActivity extends AppCompatActivity {

    // (2) VARIABLES ................ Step 4

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_db);

        // (3) SETUP CODE ........... Step 5
    }

    // (4) saveUser() ............... Step 6

    // (5) loadUsers() .............. Step 7

}
```

What the generated code does:

- `onCreate()` runs when the screen opens — the starting point of this activity.
- `setContentView(R.layout.activity_db)` connects the layout from Step 1 to this screen.

The numbered comments `(1)`–`(5)` are the **spots where we will insert code** in Steps 4–7.

> If Android Studio also generated extra lines (`EdgeToEdge.enable(this)` and a `ViewCompat.setOnApplyWindowInsetsListener(...)` block), **delete them** — we keep the file as simple as possible.

---

## Step 4: Add the imports and the variables

**Where (imports):** at the **top of the file**, directly below the `package` line — spot **(1)** on the map. Two of them (`Bundle` and `AppCompatActivity`) are already there — add the missing ones so the list looks like this:

```java
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
```

Imports simply tell Java which classes we are using in this file. Tip: if a class name shows **red** in Android Studio, press `Alt+Enter` on it and the import is added for you.

**Where (variables):** **inside the class**, directly below the line `public class DBActivity extends AppCompatActivity {` — spot **(2)** on the map.

```java
    DatabaseHelper dbHelper;
    EditText editName, editEmail, editPassword;
    ArrayAdapter<String> adapter;
    ArrayList<String> users = new ArrayList<>();
```

What they are:

- `dbHelper` — our helper from Step 2, used to open the database.
- `editName`, `editEmail`, `editPassword` — the three input fields from the layout.
- `users` — a list of strings like `"Joel - joel@example.com"`, one per saved user.
- `adapter` — shows each string in `users` as one row of the `ListView`.

---

## Step 5: Add the setup code inside `onCreate()`

**Where:** inside `onCreate()`, directly below the `setContentView(R.layout.activity_db);` line — spot **(3)** on the map. The `onCreate()` method itself is already there from Android Studio — you only **add** these lines inside it:

```java
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
```

What each part does:

- `dbHelper = new DatabaseHelper(this)` — creates our helper from Step 2, ready to open the database. (The database file itself is not created yet — only when we first use it.)
- `editName = findViewById(R.id.editName)` — connects the Java variable to the view with that id in the layout from Step 1. Same for the other views.
- `new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, users)` — an **adapter** turns a Java list into list rows. `simple_list_item_1` is a ready-made row layout from Android that shows one line of text — that is why we don't need any custom row XML.
- `listUsers.setAdapter(adapter)` — connects the adapter to the `ListView`, so the list on screen shows whatever is inside `users`.
- `btnSave.setOnClickListener(v -> saveUser())` — means: "when the button is clicked, run `saveUser()`".
- `loadUsers();` — fills the list as soon as the screen opens, so already-saved users appear immediately.

> Android Studio will show `saveUser()` and `loadUsers()` in **red** for now — they don't exist yet. We write them in the next two steps.

---

## Step 6: Write `saveUser()` — save (insert) a user

**Where:** inside the class, below `onCreate()` — spot **(4)** on the map.

```java
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
```

What each part does:

- `dbHelper.getWritableDatabase()` — opens the database **for writing**. The very first time, this is the moment the database file is created and `onCreate()` in your helper runs.
- `editName.getText().toString()` — reads what the user typed into the box, as a `String`.
- `new ContentValues()` — an object that holds **one row** to insert.
- `values.put("name", ...)` — pairs a **column name** with the **value** to store there. The names must match the columns from the `CREATE TABLE` in Step 2.
- `db.insert("users", null, values)` — inserts the row into the `users` table. It returns the new row's `id`, or `-1` if something went wrong. (The `null` is an advanced option we can ignore.)
- `editName.setText("")` — clears the input boxes, so the form is empty and ready for the next user.
- `loadUsers()` — reads all users again, so the new user appears in the list immediately.

---

## Step 7: Write `loadUsers()` — read (query) all users

**Where:** inside the class, below `saveUser()` — spot **(5)** on the map.

```java
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
```

What each part does:

- `users.clear()` — empties the list first, so we don't show the same users twice after a refresh.
- `dbHelper.getReadableDatabase()` — opens the database **for reading**.
- `db.rawQuery("SELECT * FROM users", null)` — runs normal SQL (`*` means "all columns"). It returns a `Cursor` — think of it as a pointer that walks through the result rows, one row at a time.
- `cursor.moveToNext()` — moves the cursor to the next row. It returns `false` when there are no more rows — and that ends the `while` loop.
- `cursor.getColumnIndexOrThrow("name")` — finds the position of the `name` column; `cursor.getString(...)` then reads the text in that column of the **current row**.
- `users.add(name + " - " + email)` — builds the text for one list row, e.g. `Joel - joel@example.com`.
- `cursor.close()` — always close the cursor when you finish reading, to free its memory.
- `adapter.notifyDataSetChanged()` — tells the `ListView`: "the data changed — redraw yourself".

---

## Step 8: Check the complete file

After Steps 4–7, your `DBActivity.java` should look exactly like this:

```java
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
```

Run the app, type a name, email and password, and press **Save User** — the user appears in the list. Close and reopen the app: the users are still there, because they are stored in the database file on the device.

---

## Quick recap of the flow

1. **Layout** → the form (name/email/password/save button) and the `ListView`
2. `DatabaseHelper` → creates the `users` table the first time the app runs
3. `DBActivity` → connects the views, prepares the list adapter, and links the button to `saveUser()`
4. To **save**: get a writable database → put values in `ContentValues` → call `insert()`
5. To **read**: get a readable database → `rawQuery("SELECT * FROM users", null)` → loop through the `Cursor` → show as `name - email`

---

## A note on the password

This guide stores the password as plain text purely to keep things simple for learning. In any real app, **never store plain-text passwords** — hash them (e.g. with a library like bcrypt) before saving. Worth mentioning to students as a "don't do this in production" callout.

**Reference:** [Save data using SQLite — Android Developers](https://developer.android.com/training/data-storage/sqlite)
