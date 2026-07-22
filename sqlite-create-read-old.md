# SQLite in Android (Java) — Save & Read a User

A simple step-by-step guide to store a `User` (id, name, email, password) in a local SQLite database, and read it back.


---

## What you'll build

- `activity_db.xml` — the input form and list
- `UserContract.java` — names of the table and columns
- `DatabaseHelper.java` — creates the database and table
- `User.java` — a simple model class to hold user data
- `DBActivity.java` — saves a user and reads users back into a simple list

---

## Step 1: Add an input form (layout)

In `res/layout/activity_db.xml`, add fields for name, email, password, plus a save button and a list view:

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

---

## Step 2: Create the Contract class

This class just holds the table name and column names as constants, so you never mistype them.

Create `UserContract.java`:

```java
public final class UserContract {

    // Prevent someone from instantiating this class
    private UserContract() {}

    public static class UserEntry implements BaseColumns {
        public static final String TABLE_NAME = "user";
        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_EMAIL = "email";
        public static final String COLUMN_PASSWORD = "password";
    }
}
```

`BaseColumns` automatically gives you an `_ID` column for the primary key.

---

## Step 3: Create the Database Helper

This class creates the database and the `user` table the first time the app runs.

Create `DatabaseHelper.java`:

```java
public class DatabaseHelper extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "MyApp.db";

    private static final String SQL_CREATE_USER_TABLE =
            "CREATE TABLE " + UserEntry.TABLE_NAME + " (" +
                    UserEntry._ID + " INTEGER PRIMARY KEY," +
                    UserEntry.COLUMN_NAME + " TEXT," +
                    UserEntry.COLUMN_EMAIL + " TEXT," +
                    UserEntry.COLUMN_PASSWORD + " TEXT)";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_USER_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Simplest possible upgrade: drop and recreate
        db.execSQL("DROP TABLE IF EXISTS " + UserEntry.TABLE_NAME);
        onCreate(db);
    }
}
```

---

## Step 4: Create the User model class

A plain class to carry user data around your app.

Create `User.java`:

```java
public class User {

    private long id;
    private String name;
    private String email;
    private String password;

    public User(String name, String email, String password) {
        this.name = name;
        this.email = email;
        this.password = password;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }
}
```

---

## Step 5: Save (insert) a user

Wherever you want to save a user (e.g. in your Activity, on a "Register" button click), do this:

```java
DatabaseHelper dbHelper = new DatabaseHelper(this);
SQLiteDatabase db = dbHelper.getWritableDatabase();

ContentValues values = new ContentValues();
values.put(UserContract.UserEntry.COLUMN_NAME, "Joel Mmasi");
values.put(UserContract.UserEntry.COLUMN_EMAIL, "joel@example.com");
values.put(UserContract.UserEntry.COLUMN_PASSWORD, "myPassword123");

long newRowId = db.insert(UserContract.UserEntry.TABLE_NAME, null, values);
```

`newRowId` is the new user's `_ID`, or `-1` if something went wrong.

**Needed imports for this step:**
```java
import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
```

---

## Step 6: Read (query) all users

```java
List<User> userList = new ArrayList<>();

DatabaseHelper dbHelper = new DatabaseHelper(this);
SQLiteDatabase db = dbHelper.getReadableDatabase();

String[] projection = {
        UserContract.UserEntry._ID,
        UserContract.UserEntry.COLUMN_NAME,
        UserContract.UserEntry.COLUMN_EMAIL,
        UserContract.UserEntry.COLUMN_PASSWORD
};

Cursor cursor = db.query(
        UserContract.UserEntry.TABLE_NAME,  // table
        projection,                          // columns to return
        null,                                 // selection (WHERE) — null means "all rows"
        null,                                 // selection args
        null,                                 // group by
        null,                                 // having
        null                                  // order by
);

while (cursor.moveToNext()) {
    User user = new User(
            cursor.getString(cursor.getColumnIndexOrThrow(UserContract.UserEntry.COLUMN_NAME)),
            cursor.getString(cursor.getColumnIndexOrThrow(UserContract.UserEntry.COLUMN_EMAIL)),
            cursor.getString(cursor.getColumnIndexOrThrow(UserContract.UserEntry.COLUMN_PASSWORD))
    );
    user.setId(cursor.getLong(cursor.getColumnIndexOrThrow(UserContract.UserEntry._ID)));
    userList.add(user);
}
cursor.close();
```

**Needed imports for this step:**
```java
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import java.util.ArrayList;
import java.util.List;
```

---

## Step 7: Wire up the form + a simple ListView (no custom adapter)

Instead of building a custom adapter/layout, use Android's built-in `ArrayAdapter` with the built-in row layout `android.R.layout.simple_list_item_1`. It just shows one line of text per row — perfect for keeping things simple.

In `DBActivity.java`:

```java
public class DBActivity extends AppCompatActivity {

    private DatabaseHelper dbHelper;
    private EditText editName, editEmail, editPassword;
    private ListView listUsers;
    private ArrayAdapter<String> adapter;
    private List<String> userStrings = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_db);

        dbHelper = new DatabaseHelper(this);

        editName = findViewById(R.id.editName);
        editEmail = findViewById(R.id.editEmail);
        editPassword = findViewById(R.id.editPassword);
        listUsers = findViewById(R.id.listUsers);
        Button btnSave = findViewById(R.id.btnSave);

        // Built-in adapter, built-in row layout — no custom XML needed
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, userStrings);
        listUsers.setAdapter(adapter);

        btnSave.setOnClickListener(v -> saveUser());

        loadUsers();
    }

    private void saveUser() {
        String name = editName.getText().toString();
        String email = editEmail.getText().toString();
        String password = editPassword.getText().toString();

        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(UserContract.UserEntry.COLUMN_NAME, name);
        values.put(UserContract.UserEntry.COLUMN_EMAIL, email);
        values.put(UserContract.UserEntry.COLUMN_PASSWORD, password);

        db.insert(UserContract.UserEntry.TABLE_NAME, null, values);

        // Clear the form
        editName.setText("");
        editEmail.setText("");
        editPassword.setText("");

        loadUsers(); // refresh the list
    }

    private void loadUsers() {
        userStrings.clear();

        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor cursor = db.query(
                UserContract.UserEntry.TABLE_NAME,
                null,
                null, null, null, null, null
        );

        while (cursor.moveToNext()) {
            String name = cursor.getString(cursor.getColumnIndexOrThrow(UserContract.UserEntry.COLUMN_NAME));
            String email = cursor.getString(cursor.getColumnIndexOrThrow(UserContract.UserEntry.COLUMN_EMAIL));
            userStrings.add(name + " - " + email);
        }
        cursor.close();

        adapter.notifyDataSetChanged();
    }

    @Override
    protected void onDestroy() {
        dbHelper.close();
        super.onDestroy();
    }
}
```

**Needed imports for this step:**
```java
import androidx.appcompat.app.AppCompatActivity;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import java.util.ArrayList;
import java.util.List;
```

Each row in the list just shows `"name - email"` as plain text — nothing custom to build or maintain. Note this Activity already closes `dbHelper` in `onDestroy()`, since `dbHelper` is a field (not a fresh instance each time).

---

## Quick recap of the flow

1. **Layout** → the form (name/email/password/save button) and the `ListView`
2. `UserContract` → defines table/column names
3. `DatabaseHelper` → creates the table when the app first runs
4. `User` → a plain object to hold the data in Java
5. To **save**: get a writable database → put values in `ContentValues` → call `insert()`
6. To **read**: get a readable database → call `query()` with no filter → loop through the `Cursor` → build `User` objects
7. `DBActivity` ties it together: save button inserts a row, and the built-in `ArrayAdapter` + `ListView` shows all saved users — no custom adapter or row layout required

---

## A note on the password

This guide stores the password as plain text, as requested, purely to keep things simple for learning. In any real app, never store plain-text passwords — hash them (e.g. with a library like bcrypt) before saving. This is worth mentioning to students as a "don't do this in production" callout.

**Reference:** [Save data using SQLite — Android Developers](https://developer.android.com/training/data-storage/sqlite)
