# Bottom Navigation in Android (Java) — The Bottom Menu

A simple step-by-step guide to add a **bottom navigation bar** — the menu with icons at the bottom of the screen (like WhatsApp or YouTube) — and change the screen content when a tab is tapped.

To keep it as simple as possible, tapping a tab will just change a text on screen. (In real apps each tab usually shows a different *Fragment* — that is a later lesson.)

---

## What you'll build

- `res/menu/bottom_menu.xml` — the three tabs (Home, Search, Profile)
- `res/layout/activity_bottom.xml` — a text in the middle + the bar at the bottom
- `BottomActivity.java` — reacts when a tab is tapped

Only 3 files — that's all.

---

## Step 1: Create a new activity

**Where:** Android Studio menu: **File → New → Activity → Empty Views Activity** → Activity Name: `BottomActivity` → Finish. (In older Android Studio it is called "Empty Activity".)

Android Studio creates three things for you automatically:

- `BottomActivity.java` — the Java file
- `res/layout/activity_bottom.xml` — its layout
- an entry in `AndroidManifest.xml` — so Android knows the screen exists

---

## Step 2: Create the menu file (the tabs)

**Where:** a **new file** `bottom_menu.xml` inside the `res/menu` folder.

In Android Studio: right-click the `res` folder → **New → Android Resource File** → File name: `bottom_menu`, Resource type: **Menu** → OK. (If the `menu` folder doesn't exist yet, this dialog creates it too.)

This is the complete file — copy all of it:

```xml
<menu xmlns:android="http://schemas.android.com/apk/res/android">

    <item
        android:id="@+id/navHome"
        android:title="Home"
        android:icon="@android:drawable/ic_menu_compass" />

    <item
        android:id="@+id/navSearch"
        android:title="Search"
        android:icon="@android:drawable/ic_menu_search" />

    <item
        android:id="@+id/navProfile"
        android:title="Profile"
        android:icon="@android:drawable/ic_menu_myplaces" />

</menu>
```

What each part does:

- `<item>` — **one tab** in the bar. A bottom bar should have 3 to 5 tabs.
- `android:id` — the tab's **name**; in Java we use it to know which tab was tapped.
- `android:title` — the text shown under the icon.
- `android:icon` — the picture on the tab. Bottom tabs **must** have an icon. `@android:drawable/...` are ready-made icons that come with Android — no need to create image files. (In real apps you make your own: right-click `res` → New → Vector Asset.)

---

## Step 3: The layout

**Where:** in `res/layout/activity_bottom.xml` (replace its whole content).

A text in the middle of the screen, and the bar at the bottom:

```xml
<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    android:orientation="vertical"
    android:layout_width="match_parent"
    android:layout_height="match_parent">

    <TextView
        android:id="@+id/screenText"
        android:layout_width="match_parent"
        android:layout_height="0dp"
        android:layout_weight="1"
        android:gravity="center"
        android:text="Home"
        android:textSize="24sp" />

    <com.google.android.material.bottomnavigation.BottomNavigationView
        android:id="@+id/bottomNav"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        app:menu="@menu/bottom_menu" />

</LinearLayout>
```

What each part does:

- `xmlns:app="..."` — one extra line on the root that makes the `app:` attributes work.
- `TextView` — pretends to be our screen content; tapping a tab will change this text.
- `android:layout_height="0dp"` + `android:layout_weight="1"` — together they mean: "take **all the remaining space**" above the bar. That pushes the bar to the bottom.
- `android:gravity="center"` — centers the text inside the `TextView`.
- `BottomNavigationView` — the bar itself, from the Material library (the long name `com.google.android.material...` is just its full address).
- `app:menu="@menu/bottom_menu"` — connects the bar to the menu file from Step 2. The bar builds its tabs from that file.

---

## Step 4: The starting point of `BottomActivity.java`

When Android Studio created this activity in Step 1, it already came with the `onCreate()` method:

```java
package tz.ac.dit.od23storage;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
// (1) MORE IMPORTS ................. Step 5

public class BottomActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bottom);

        // (2) SETUP CODE ........... Step 6
    }

}
```

What the generated code does:

- `onCreate()` runs when the screen opens — the starting point of this activity.
- `setContentView(R.layout.activity_bottom)` connects the layout from Step 3 to this screen.

The numbered comments `(1)`–`(2)` are the **spots where we will insert code** in Steps 5–6.

> If Android Studio also generated extra lines (`EdgeToEdge.enable(this)` and a `ViewCompat.setOnApplyWindowInsetsListener(...)` block), **delete them** — we keep the file as simple as possible.

---

## Step 5: Add the imports

**Where:** at the **top of the file**, directly below the `package` line — spot **(1)** on the map. Two of them (`Bundle` and `AppCompatActivity`) are already there — add the missing ones so the list looks like this:

```java
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;
```

Imports simply tell Java which classes we are using in this file. Tip: if a class name shows **red** in Android Studio, press `Alt+Enter` on it and the import is added for you.

---

## Step 6: React when a tab is tapped

**Where:** inside `onCreate()`, directly below the `setContentView(R.layout.activity_bottom);` line — spot **(2)** on the map.

```java
        TextView screenText = findViewById(R.id.screenText);
        BottomNavigationView bottomNav = findViewById(R.id.bottomNav);

        bottomNav.setOnItemSelectedListener(item -> {
            int id = item.getItemId();

            if (id == R.id.navHome) {
                screenText.setText("Home");
            } else if (id == R.id.navSearch) {
                screenText.setText("Search");
            } else if (id == R.id.navProfile) {
                screenText.setText("Profile");
            }

            return true; // mark the tapped tab as selected
        });
```

What each part does:

- `findViewById(...)` — connects the Java variables to the views from the layout in Step 3.
- `setOnItemSelectedListener(item -> { ... })` — means: "every time a tab is tapped, run the code between the `{ }`". The tapped tab arrives as `item`.
- `item.getItemId()` — tells you **which** tab was tapped, using the ids from the menu file in Step 2.
- The `if / else if` — runs the matching code for that tab. Here we just change the text — in a real app this is where each tab would show its own content.
- `return true` — means "yes, highlight this tab as the selected one". (Return `false` and the tab won't stay selected.)

---

## Step 7: Check the complete file

After Steps 5–6, your `BottomActivity.java` should look exactly like this:

```java
package tz.ac.dit.od23storage;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class BottomActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bottom);

        TextView screenText = findViewById(R.id.screenText);
        BottomNavigationView bottomNav = findViewById(R.id.bottomNav);

        bottomNav.setOnItemSelectedListener(item -> {
            int id = item.getItemId();

            if (id == R.id.navHome) {
                screenText.setText("Home");
            } else if (id == R.id.navSearch) {
                screenText.setText("Search");
            } else if (id == R.id.navProfile) {
                screenText.setText("Profile");
            }

            return true; // mark the tapped tab as selected
        });
    }
}
```

**How to open this screen for testing:** a new activity is not the first screen of the app. Quick way: in `AndroidManifest.xml`, move these 4 lines from inside `MainActivity`'s `<activity>` tag into `BottomActivity`'s `<activity>` tag (move them back when you finish testing):

```xml
<intent-filter>
    <action android:name="android.intent.action.MAIN" />
    <category android:name="android.intent.category.LAUNCHER" />
</intent-filter>
```

Run the app: the bar with **Home / Search / Profile** sits at the bottom. Tap a tab — it becomes highlighted and the text in the middle changes to match.

---

## Quick recap of the flow

1. **Menu XML** → each `<item>` with an `id`, `title` and `icon` is one tab
2. **Layout** → the content on top (`layout_weight="1"` = take the remaining space) + `BottomNavigationView` at the bottom, connected to the menu with `app:menu`
3. **Java** → `setOnItemSelectedListener` runs on every tap; `getItemId()` + `if / else if` decide what to do
4. `return true` = keep the tapped tab highlighted
5. Real apps show a different **Fragment** per tab — same listener, different code inside the `if` blocks

**Reference:** [BottomNavigationView — Android Developers](https://developer.android.com/reference/com/google/android/material/bottomnavigation/BottomNavigationView)
