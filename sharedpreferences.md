# SharedPreferences in Android (Java) — Save & Load a Name

A simple step-by-step guide to save a small piece of data (a name) using `SharedPreferences`, and show it on screen.

`SharedPreferences` is for **small key/value data** — a name, a setting, a yes/no flag. For many rows of data (like a list of users), use SQLite instead — see `sqlite-create-read.md`.

---

## What you'll build

- `activity_main.xml` — a text input, a save button, and a text view to display the saved name
- `MainActivity.java` — saves the name to `SharedPreferences` and loads it back

Only 2 files — that's all.

---

## Step 1: Add the layout

**Where:** in `res/layout/activity_main.xml` (replace its whole content).

Add an input field, a save button, and a text view to show the result:

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

What each part does:

- `LinearLayout` with `android:orientation="vertical"` — stacks its children top to bottom, one under the other.
- `android:layout_width="match_parent"` — as wide as the screen; `wrap_content` — only as big as its content needs.
- `android:padding="16dp"` — space between the screen edge and the views.
- `EditText` — a text input box. `android:hint` is the gray text shown while the box is empty.
- `android:id="@+id/nameInput"` — gives the view a **name**. In Java we use these ids with `findViewById(...)` to reach the views.
- `Button` — the user presses it to save; `android:text` is the label on the button.
- `TextView` — empty at first; it will display the saved name. `layout_marginTop` adds space above it, `textSize="18sp"` makes the text a bit bigger.

---

## Step 2: The starting point of `MainActivity.java`

From here, everything goes into **one file**: `MainActivity.java`. When Android Studio created this activity, it already came with the `onCreate()` method:

```java
package tz.ac.dit.od23storage;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
// (1) MORE IMPORTS ................. Step 3

public class MainActivity extends AppCompatActivity {

    // (2) VARIABLE ................. Step 3

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // (3) SETUP CODE ........... Step 4

        // (4) SAVE ON CLICK ........ Step 5
    }

    // (5) loadUserData() ........... Step 6

}
```

What the generated code does:

- `onCreate()` runs when the screen opens — the starting point of this activity.
- `setContentView(R.layout.activity_main)` connects the layout from Step 1 to this screen.

The numbered comments `(1)`–`(5)` are the **spots where we will insert code** in Steps 3–6.

> If Android Studio also generated extra lines (`EdgeToEdge.enable(this)` and a `ViewCompat.setOnApplyWindowInsetsListener(...)` block), **delete them** — we keep the file as simple as possible.

---

## Step 3: Add the imports and the variable

**Where (imports):** at the **top of the file**, directly below the `package` line — spot **(1)** on the map. Two of them (`Bundle` and `AppCompatActivity`) are already there — add the missing ones so the list looks like this:

```java
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
```

Imports simply tell Java which classes we are using in this file. Tip: if a class name shows **red** in Android Studio, press `Alt+Enter` on it and the import is added for you.

**Where (variable):** **inside the class**, directly below the line `public class MainActivity extends AppCompatActivity {` — spot **(2)** on the map.

```java
    TextView nameTextView;
```

Why at class level?

- `nameTextView` is declared **outside** `onCreate()` because another method (`loadUserData()`, Step 6) also needs it.
- The input box and the button are only used inside `onCreate()`, so they can stay local there (you will see this in Step 4).

---

## Step 4: Add the setup code inside `onCreate()`

**Where:** inside `onCreate()`, directly below the `setContentView(R.layout.activity_main);` line — spot **(3)** on the map. The `onCreate()` method itself is already there from Android Studio — you only **add** these lines inside it:

```java
        nameTextView = findViewById(R.id.nameTextView);
        EditText nameInput = findViewById(R.id.nameInput);
        Button saveButton = findViewById(R.id.saveButton);

        loadUserData(); // show the saved name when the screen opens
```

What each part does:

- `findViewById(R.id.nameTextView)` — connects the Java variable to the view with that id in the layout from Step 1. Same for the other views.
- `loadUserData();` — shows the saved name as soon as the screen opens (so a name saved yesterday appears immediately today).

> Android Studio will show `loadUserData()` in **red** for now — it doesn't exist yet. We write it in Step 6.

---

## Step 5: Save the name when the button is clicked

**Where:** inside `onCreate()`, directly below the `loadUserData();` line — spot **(4)** on the map.

```java
        saveButton.setOnClickListener(v -> {
            String name = nameInput.getText().toString();

            SharedPreferences preferences = getSharedPreferences("user_data", MODE_PRIVATE);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString("name", name);
            editor.apply();

            loadUserData(); // refresh the displayed name
        });
```

What each part does:

- `saveButton.setOnClickListener(v -> { ... })` — means: "when the button is clicked, run the code between the `{ }`".
- `nameInput.getText().toString()` — reads what the user typed into the box, as a `String`.
- `getSharedPreferences("user_data", MODE_PRIVATE)` — opens (or creates) a preferences file named `user_data`. `MODE_PRIVATE` means only **this app** can read it.
- `preferences.edit()` — gives an `Editor`, the object used to change the stored values.
- `editor.putString("name", name)` — pairs a **key** (`"name"`) with the **value** to store. Later we read it back using the same key.
- `editor.apply()` — actually saves, in the background. (Without `apply()`, nothing is stored!)
- `loadUserData()` — refreshes the `TextView` so the new name appears immediately.

---

## Step 6: Write `loadUserData()` — load the saved name

**Where:** inside the class, below `onCreate()` — spot **(5)** on the map.

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

What each part does:

- `getSharedPreferences("user_data", MODE_PRIVATE)` — opens the **same** preferences file we saved into in Step 5.
- `preferences.getString("name", "")` — reads the value stored under the key `"name"`. The second argument (`""`) is the **default value**, returned if nothing has been saved yet.
- `if (name.isEmpty())` — if nothing was saved yet, we show the message `"No User Data"` instead of an empty screen.
- `nameTextView.setText(name)` — displays the result on screen.

---

## Step 7: Check the complete file

After Steps 3–6, your `MainActivity.java` should look exactly like this:

```java
package tz.ac.dit.od23storage;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    TextView nameTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        nameTextView = findViewById(R.id.nameTextView);
        EditText nameInput = findViewById(R.id.nameInput);
        Button saveButton = findViewById(R.id.saveButton);

        loadUserData(); // show the saved name when the screen opens

        saveButton.setOnClickListener(v -> {
            String name = nameInput.getText().toString();

            SharedPreferences preferences = getSharedPreferences("user_data", MODE_PRIVATE);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString("name", name);
            editor.apply();

            loadUserData(); // refresh the displayed name
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

Run the app: at first it shows **"No User Data"**. Type a name and press **Save** — the name appears below. Close and reopen the app: the name is still there, because it is stored in the preferences file on the device.

---

## Quick recap of the flow

1. **Layout** → an `EditText` to type a name, a `Button` to save, a `TextView` to show it
2. **Save** → `getSharedPreferences()` → `edit()` → `putString("name", ...)` → `apply()`
3. **Load** → `getSharedPreferences()` → `getString("name", default)` → show it on the `TextView`
4. `loadUserData()` runs when the screen opens **and** right after saving, so what's shown always matches what's stored
5. Remember: **same file name** (`"user_data"`) and **same key** (`"name"`) for saving and loading — that's how the data is found again

**Reference:** [Save simple data with SharedPreferences — Android Developers](https://developer.android.com/training/data-storage/shared-preferences)
