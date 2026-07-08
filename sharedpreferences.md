# SharedPreferences in Android (Java) — Save & Load a Name

A simple step-by-step guide to save a small piece of data (a name) using `SharedPreferences`, and show it on screen.


---

## What you'll build

- `activity_main.xml` — a text input, a save button, and a text view to display the saved name
- `MainActivity.java` — saves the name to `SharedPreferences` and loads it back

---

## Step 1: Add the layout

In `res/layout/activity_main.xml`, add an input field, a save button, and a text view to show the result:

```xml
<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
    android:orientation="vertical"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:padding="16dp">

    <EditText
        android:id="@+id/nameInput"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:hint="Enter your name" />

    <Button
        android:id="@+id/saveButton"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:text="Save" />

    <TextView
        android:id="@+id/nameTextView"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:layout_marginTop="16dp"
        android:textSize="18sp" />

</LinearLayout>
```

---

## Step 2: Get references to the views

In `MainActivity.java`, inside `onCreate()`, connect the layout views to Java:

```java
public class MainActivity extends AppCompatActivity {

    TextView nameTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        nameTextView = findViewById(R.id.nameTextView);
        EditText nameInput = findViewById(R.id.nameInput);
        Button saveButton = findViewById(R.id.saveButton);
    }
}
```

**Needed imports for this step:**
```java
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
```

---

## Step 3: Save the name when the button is clicked

`SharedPreferences` stores data as simple key/value pairs, under a file name you choose (here: `"user_data"`).

Add this inside `onCreate()`, after `saveButton` is set up:

```java
saveButton.setOnClickListener(v -> {
    String name = nameInput.getText().toString();

    SharedPreferences preferences = getSharedPreferences("user_data", MODE_PRIVATE);
    SharedPreferences.Editor editor = preferences.edit();
    editor.putString("name", name);
    editor.apply();
});
```

- `getSharedPreferences("user_data", MODE_PRIVATE)` opens (or creates) a preferences file named `user_data`, private to your app.
- `editor.putString("name", name)` stages the value to be saved.
- `editor.apply()` saves it in the background.

**Needed import for this step:**
```java
import android.content.SharedPreferences;
```

---

## Step 4: Load the saved name

Add a method to read the value back and display it:

```java
private void loadUserData() {
    SharedPreferences preferences = getSharedPreferences("user_data", MODE_PRIVATE);
    String name = preferences.getString("name", "");

    if (name.isEmpty()) {
        name = "No User Data";
    }

    nameTextView.setText(name);
}
```

- `preferences.getString("name", "")` reads the value stored under `"name"`. The second argument (`""`) is the default value if nothing has been saved yet.

---

## Step 5: Put it all together

Call `loadUserData()` when the screen opens, and again after saving, so the display always reflects the latest value:

```java
public class MainActivity extends AppCompatActivity {

    TextView nameTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        nameTextView = findViewById(R.id.nameTextView);
        EditText nameInput = findViewById(R.id.nameInput);
        Button saveButton = findViewById(R.id.saveButton);
        loadUserData();

        saveButton.setOnClickListener(v -> {
            String name = nameInput.getText().toString();
            SharedPreferences preferences = getSharedPreferences("user_data", MODE_PRIVATE);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString("name", name);
            editor.apply();
            loadUserData();
        });
    }

    private void loadUserData() {
        SharedPreferences preferences = getSharedPreferences("user_data", MODE_PRIVATE);
        String name = preferences.getString("name", "");
        if (name.isEmpty()) {
            name = "No User Data";
        }
        nameTextView.setText(name);
    }
}
```

**All imports needed:**
```java
import androidx.appcompat.app.AppCompatActivity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
```

---

## Quick recap of the flow

1. **Layout** → an `EditText` to type a name, a `Button` to save, a `TextView` to show it
2. **Save** → `getSharedPreferences()` → `edit()` → `putString()` → `apply()`
3. **Load** → `getSharedPreferences()` → `getString()` with a default value → set it on the `TextView`
4. Call `loadUserData()` on screen open and again right after saving, so what's shown always matches what's stored
