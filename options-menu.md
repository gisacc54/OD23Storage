# Options Menu in Android (Java) — The Three-Dot Menu

A simple step-by-step guide to add an **options menu** — the menu that opens from the **three dots (⋮)** at the top-right of the app bar — and run code when an item is clicked.

---

## What you'll build

- `res/menu/main_menu.xml` — the menu items (Settings, About, Exit)
- `MainActivity.java` — shows the menu and reacts when an item is clicked

Only 2 files — that's all.

---

## Step 1: Create the menu file

**Where:** a **new file** `main_menu.xml` inside a **new folder** `res/menu`.

In Android Studio: right-click the `res` folder → **New → Android Resource File** → File name: `main_menu`, Resource type: **Menu** → OK. (This one dialog creates both the `menu` folder and the file.)

This is the complete file — copy all of it:

```xml
<menu xmlns:android="http://schemas.android.com/apk/res/android">

    <item
        android:id="@+id/menuSettings"
        android:title="Settings" />

    <item
        android:id="@+id/menuAbout"
        android:title="About" />

    <item
        android:id="@+id/menuExit"
        android:title="Exit" />

</menu>
```

What each part does:

- `<menu>` — the root element; it holds the list of menu items.
- `<item>` — **one row** in the menu. Three items = three rows.
- `android:id="@+id/menuSettings"` — gives the item a **name**. In Java we use this id to know **which** item was clicked.
- `android:title="Settings"` — the text the user sees in the menu.

---

## Step 2: The starting point of `MainActivity.java`

Everything else goes into **one file**: `MainActivity.java`. When Android Studio created this activity, it already came with the `onCreate()` method:

```java
package tz.ac.dit.od23storage;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
// (1) MORE IMPORTS ................. Step 3

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    // (2) onCreateOptionsMenu() ..... Step 4

    // (3) onOptionsItemSelected() ... Step 5

}
```

What the generated code does:

- `onCreate()` runs when the screen opens — the starting point of this activity.
- `setContentView(R.layout.activity_main)` connects the activity's layout to this screen.

The numbered comments `(1)`–`(3)` are the **spots where we will insert code** in Steps 3–5.

Notes:

- This time **nothing goes inside `onCreate()`** — the menu uses its own two methods, written **below** `onCreate()` (but still inside the class).
- If your `onCreate()` already contains code from a previous lesson (for example the SharedPreferences lesson), that's fine — leave it; the menu does not touch it.
- The three-dot menu appears in the **app bar** (the colored bar at the top). This project's theme (`Theme.MaterialComponents.DayNight.DarkActionBar`) already has an app bar. If a project uses a `NoActionBar` theme, the menu will not appear.

> If Android Studio also generated extra lines (`EdgeToEdge.enable(this)` and a `ViewCompat.setOnApplyWindowInsetsListener(...)` block), **delete them** — we keep the file as simple as possible.

---

## Step 3: Add the imports

**Where:** at the **top of the file**, directly below the `package` line — spot **(1)** on the map. Two of them (`Bundle` and `AppCompatActivity`) are already there — add the missing ones so the list looks like this:

```java
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
```

Imports simply tell Java which classes we are using in this file. Tip: if a class name shows **red** in Android Studio, press `Alt+Enter` on it and the import is added for you.

---

## Step 4: Show the menu — `onCreateOptionsMenu()`

**Where:** inside the class, below `onCreate()` — spot **(2)** on the map.

```java
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }
```

What each part does:

- `onCreateOptionsMenu()` — Android calls this method **once**, when it builds the app bar. You never call it yourself.
- `getMenuInflater().inflate(R.menu.main_menu, menu)` — "inflate" means: read the XML file from Step 1 and turn it into real menu items on screen.
- `return true` — means "yes, show the menu". (Return `false` and the menu stays hidden.)

---

## Step 5: React to clicks — `onOptionsItemSelected()`

**Where:** inside the class, below `onCreateOptionsMenu()` — spot **(3)** on the map.

```java
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.menuSettings) {
            Toast.makeText(this, "Settings clicked", Toast.LENGTH_SHORT).show();
            return true;
        } else if (id == R.id.menuAbout) {
            Toast.makeText(this, "About clicked", Toast.LENGTH_SHORT).show();
            return true;
        } else if (id == R.id.menuExit) {
            finish(); // close this screen
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
```

What each part does:

- `onOptionsItemSelected()` — Android calls this method **every time** a menu item is clicked, and hands you the clicked `item`.
- `item.getItemId()` — tells you **which** item was clicked, using the ids from the XML in Step 1.
- The `if / else if` — compares the id and runs the matching code. Each item gets its own block — this is where you put what the item should **do**.
- `Toast.makeText(this, "Settings clicked", Toast.LENGTH_SHORT).show()` — a `Toast` is a small message that pops up at the bottom of the screen and disappears by itself. Don't forget `.show()` — without it, nothing appears!
- `finish()` — closes this screen. (Just to show a menu item can do something real.)
- `return true` — means "I handled this click, Android — you don't need to do anything else."
- `return super.onOptionsItemSelected(item)` — for any other item we didn't handle, let Android do its default behavior.

---

## Step 6: Check the complete file

After Steps 3–5, your `MainActivity.java` should look exactly like this:

```java
package tz.ac.dit.od23storage;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.menuSettings) {
            Toast.makeText(this, "Settings clicked", Toast.LENGTH_SHORT).show();
            return true;
        } else if (id == R.id.menuAbout) {
            Toast.makeText(this, "About clicked", Toast.LENGTH_SHORT).show();
            return true;
        } else if (id == R.id.menuExit) {
            finish(); // close this screen
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
```

Run the app: the **three dots (⋮)** appear at the top-right of the app bar. Tap them — the menu opens with **Settings**, **About** and **Exit**. Tap Settings or About and a small message pops up at the bottom; tap Exit and the screen closes.

---

## Bonus (optional): Show an item as an icon in the app bar

An item can sit **directly in the app bar** as an icon button, instead of hiding inside the three dots. In `main_menu.xml`, add the `app` namespace line to `<menu>`, and give the item an icon + `showAsAction`:

```xml
<menu xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto">

    <item
        android:id="@+id/menuSettings"
        android:title="Settings"
        android:icon="@android:drawable/ic_menu_preferences"
        app:showAsAction="always" />

    <item
        android:id="@+id/menuAbout"
        android:title="About" />

    <item
        android:id="@+id/menuExit"
        android:title="Exit" />

</menu>
```

What each part does:

- `xmlns:app="..."` — one extra line on `<menu>` that makes the `app:` attributes work.
- `android:icon` — the picture to show. `@android:drawable/ic_menu_preferences` is a ready-made icon that comes with Android.
- `app:showAsAction="always"` — always show this item as an icon in the bar. Use `"ifRoom"` to show it only when there is space, or `"never"` (the default) to keep it inside the three dots.

No Java changes needed — clicks still arrive in `onOptionsItemSelected()` the same way.

---

## Quick recap of the flow

1. **Menu XML** → each `<item>` with an `id` and a `title` is one row of the menu
2. `onCreateOptionsMenu()` → inflates the XML so the menu appears (Android calls it once)
3. `onOptionsItemSelected()` → Android calls it on every click; `getItemId()` + `if / else if` decide what to do
4. `return true` = "menu shown" / "click handled"
5. The same two methods work in **any** activity — add them to `DBActivity` and it gets the same menu

**Reference:** [Menus — Android Developers](https://developer.android.com/develop/ui/views/components/menus)
