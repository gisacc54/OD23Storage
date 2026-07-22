# Navigation Drawer in Android (Java) — The Hamburger Menu (☰)

A simple step-by-step guide to add a **navigation drawer** — the menu that slides in from the left when you tap the **hamburger icon (☰)** at the top-left of the app bar (like Gmail).

To keep it as simple as possible, tapping a menu item will just change a text on screen.

---

## What you'll build

- `res/values/strings.xml` — two small text lines the hamburger button needs
- `res/menu/drawer_menu.xml` — the menu items (Home, Profile, Logout)
- `res/layout/activity_drawer.xml` — the sliding panel + the screen content
- `DrawerActivity.java` — opens the drawer and reacts to clicks

Only 4 files — that's all.

---

## Step 1: Create a new activity

**Where:** Android Studio menu: **File → New → Activity → Empty Views Activity** → Activity Name: `DrawerActivity` → Finish. (In older Android Studio it is called "Empty Activity".)

Android Studio creates three things for you automatically:

- `DrawerActivity.java` — the Java file
- `res/layout/activity_drawer.xml` — its layout
- an entry in `AndroidManifest.xml` — so Android knows the screen exists

---

## Step 2: Create the menu file

**Where:** a **new file** `drawer_menu.xml` inside the `res/menu` folder.

In Android Studio: right-click the `res` folder → **New → Android Resource File** → File name: `drawer_menu`, Resource type: **Menu** → OK. (If the `menu` folder doesn't exist yet, this dialog creates it too.)

This is the complete file — copy all of it:

```xml
<menu xmlns:android="http://schemas.android.com/apk/res/android">

    <item
        android:id="@+id/drawerHome"
        android:title="Home"
        android:icon="@android:drawable/ic_menu_compass" />

    <item
        android:id="@+id/drawerProfile"
        android:title="Profile"
        android:icon="@android:drawable/ic_menu_myplaces" />

    <item
        android:id="@+id/drawerLogout"
        android:title="Logout"
        android:icon="@android:drawable/ic_menu_close_clear_cancel" />

</menu>
```

What each part does:

- `<item>` — **one row** in the sliding menu.
- `android:id` — the row's **name**; in Java we use it to know which row was clicked.
- `android:title` — the text of the row.
- `android:icon` — the small picture next to the text. `@android:drawable/...` are ready-made icons that come with Android.

---

## Step 3: The layout

**Where:** in `res/layout/activity_drawer.xml` (replace its whole content).

```xml
<androidx.drawerlayout.widget.DrawerLayout
    xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    android:id="@+id/drawerLayout"
    android:layout_width="match_parent"
    android:layout_height="match_parent">

    <!-- The normal screen content -->
    <TextView
        android:id="@+id/screenText"
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        android:gravity="center"
        android:text="Home"
        android:textSize="24sp" />

    <!-- The sliding menu panel -->
    <com.google.android.material.navigation.NavigationView
        android:id="@+id/navView"
        android:layout_width="wrap_content"
        android:layout_height="match_parent"
        android:layout_gravity="start"
        app:menu="@menu/drawer_menu" />

</androidx.drawerlayout.widget.DrawerLayout>
```

What each part does:

- `DrawerLayout` — a special root layout that allows a panel to **slide over** the screen. It must be the outermost element.
- The `TextView` — the normal screen content (the **first child** of `DrawerLayout` is always the normal screen). Tapping a menu item will change this text.
- `NavigationView` — the sliding panel itself, from the Material library. It stays hidden until the user opens it.
- `android:layout_gravity="start"` — the panel slides in **from the left**.
- `app:menu="@menu/drawer_menu"` — fills the panel with the items from the menu file in Step 2 (needs the `xmlns:app` line on the root).

---

## Step 4: Add two small strings

**Where:** in `res/values/strings.xml`, inside `<resources>`, next to the existing `<string>` lines.

```xml
    <string name="open_drawer">Open menu</string>
    <string name="close_drawer">Close menu</string>
```

Why: the hamburger button (Step 6) requires two short descriptions — they are read aloud by the screen reader for blind users. The user never sees them on screen.

---

## Step 5: The starting point of `DrawerActivity.java`

When Android Studio created this activity in Step 1, it already came with the `onCreate()` method:

```java
package tz.ac.dit.od23storage;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
// (1) MORE IMPORTS ................. Step 6

public class DrawerActivity extends AppCompatActivity {

    // (2) VARIABLES ................ Step 6

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drawer);

        // (3) SETUP CODE ........... Step 7
    }

    // (4) onOptionsItemSelected() .. Step 8

}
```

What the generated code does:

- `onCreate()` runs when the screen opens — the starting point of this activity.
- `setContentView(R.layout.activity_drawer)` connects the layout from Step 3 to this screen.

The numbered comments `(1)`–`(4)` are the **spots where we will insert code** in Steps 6–8.

Note: the hamburger icon lives in the **app bar** (the colored bar at the top). This project's theme (`Theme.MaterialComponents.DayNight.DarkActionBar`) already has an app bar.

> If Android Studio also generated extra lines (`EdgeToEdge.enable(this)` and a `ViewCompat.setOnApplyWindowInsetsListener(...)` block), **delete them** — we keep the file as simple as possible.

---

## Step 6: Add the imports and the variables

**Where (imports):** at the **top of the file**, directly below the `package` line — spot **(1)** on the map. Two of them (`Bundle` and `AppCompatActivity`) are already there — add the missing ones so the list looks like this:

```java
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;
```

Imports simply tell Java which classes we are using in this file. Tip: if a class name shows **red** in Android Studio, press `Alt+Enter` on it and the import is added for you.

**Where (variables):** **inside the class**, directly below the line `public class DrawerActivity extends AppCompatActivity {` — spot **(2)** on the map.

```java
    DrawerLayout drawerLayout;
    ActionBarDrawerToggle toggle;
```

Why at class level?

- Both are declared **outside** `onCreate()` because another method (`onOptionsItemSelected()`, Step 8) also needs them.
- `drawerLayout` — the whole sliding layout; `toggle` — the hamburger button (☰).

---

## Step 7: Add the setup code inside `onCreate()`

**Where:** inside `onCreate()`, directly below the `setContentView(R.layout.activity_drawer);` line — spot **(3)** on the map.

```java
        drawerLayout = findViewById(R.id.drawerLayout);
        TextView screenText = findViewById(R.id.screenText);
        NavigationView navView = findViewById(R.id.navView);

        // The hamburger button (☰): connects the app bar with the drawer
        toggle = new ActionBarDrawerToggle(
                this, drawerLayout, R.string.open_drawer, R.string.close_drawer);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        navView.setNavigationItemSelectedListener(item -> {
            int id = item.getItemId();

            if (id == R.id.drawerHome) {
                screenText.setText("Home");
            } else if (id == R.id.drawerProfile) {
                screenText.setText("Profile");
            } else if (id == R.id.drawerLogout) {
                screenText.setText("Logout");
            }

            drawerLayout.closeDrawers(); // close the menu after a click
            return true; // mark the clicked row as selected
        });
```

What each part does:

- `findViewById(...)` — connects the Java variables to the views from the layout in Step 3.
- `new ActionBarDrawerToggle(...)` — creates the ready-made hamburger button. We give it the activity, the drawer, and the two strings from Step 4.
- `drawerLayout.addDrawerListener(toggle)` — lets the button watch the drawer, so its icon can react to opening/closing.
- `toggle.syncState()` — draws the ☰ icon in the app bar in its correct state.
- `getSupportActionBar().setDisplayHomeAsUpEnabled(true)` — turns on the button space at the top-left of the app bar (the toggle fills it with ☰).
- `setNavigationItemSelectedListener(item -> { ... })` — runs every time a menu row is clicked. You already know this pattern from the options menu lesson: `getItemId()` + `if / else if` decide what to do.
- `drawerLayout.closeDrawers()` — slides the menu closed after a click, so the user sees the result.
- `return true` — means "highlight this row as the selected one".

---

## Step 8: Let the hamburger button handle its click

**Where:** inside the class, below `onCreate()` — spot **(4)** on the map.

```java
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Give the hamburger button the first chance to handle the click
        if (toggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
```

What each part does:

- When the user taps the ☰ icon, Android reports it here (the same method the options menu lesson used).
- `toggle.onOptionsItemSelected(item)` — asks the hamburger button: "is this your click?" If yes, it opens or closes the drawer and returns `true` — done.
- Anything else goes to `super.onOptionsItemSelected(item)` for Android's default behavior.

---

## Step 9: Check the complete file

After Steps 6–8, your `DrawerActivity.java` should look exactly like this:

```java
package tz.ac.dit.od23storage;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;

public class DrawerActivity extends AppCompatActivity {

    DrawerLayout drawerLayout;
    ActionBarDrawerToggle toggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drawer);

        drawerLayout = findViewById(R.id.drawerLayout);
        TextView screenText = findViewById(R.id.screenText);
        NavigationView navView = findViewById(R.id.navView);

        // The hamburger button (☰): connects the app bar with the drawer
        toggle = new ActionBarDrawerToggle(
                this, drawerLayout, R.string.open_drawer, R.string.close_drawer);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        navView.setNavigationItemSelectedListener(item -> {
            int id = item.getItemId();

            if (id == R.id.drawerHome) {
                screenText.setText("Home");
            } else if (id == R.id.drawerProfile) {
                screenText.setText("Profile");
            } else if (id == R.id.drawerLogout) {
                screenText.setText("Logout");
            }

            drawerLayout.closeDrawers(); // close the menu after a click
            return true; // mark the clicked row as selected
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Give the hamburger button the first chance to handle the click
        if (toggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
```

**How to open this screen for testing:** a new activity is not the first screen of the app. Quick way: in `AndroidManifest.xml`, move these 4 lines from inside `MainActivity`'s `<activity>` tag into `DrawerActivity`'s `<activity>` tag (move them back when you finish testing):

```xml
<intent-filter>
    <action android:name="android.intent.action.MAIN" />
    <category android:name="android.intent.category.LAUNCHER" />
</intent-filter>
```

Run the app: the **☰ icon** appears at the top-left. Tap it — the menu slides in from the left with **Home / Profile / Logout**. Tap a row — the menu slides away and the text in the middle changes. You can also open the menu by **swiping from the left edge** of the screen.

---

## Quick recap of the flow

1. **Menu XML** → each `<item>` with an `id`, `title` and `icon` is one row of the sliding menu
2. **Layout** → `DrawerLayout` as root: first child = normal screen, `NavigationView` = the sliding panel (`layout_gravity="start"` = from the left)
3. **Strings** → two small descriptions the hamburger button requires
4. **`ActionBarDrawerToggle`** → the ready-made ☰ button: create it, `addDrawerListener`, `syncState()`, and enable the home button
5. **Clicks** → `setNavigationItemSelectedListener` + `getItemId()` + `if / else if`, then `closeDrawers()`
6. **`onOptionsItemSelected()`** → give the toggle the first chance, so tapping ☰ opens the drawer

**Reference:** [Add a navigation drawer — Android Developers](https://developer.android.com/develop/ui/views/components/drawer)
