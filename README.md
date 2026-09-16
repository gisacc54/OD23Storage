# OD23 Storage — Android with Java, step by step

OD23 Storage is a small teaching app for **Android**, written in **Java**. The app you build here stores data on the device two ways: one name in **SharedPreferences**, and a list of users in a **SQLite** database shown in a `ListView`. Three further lessons add the menus Android gives you — the three-dot menu, the sliding drawer and the bottom bar.

These notes are the written version of our sessions: read one lesson per session, and type the code yourself.

---

## How you move through the app

```
launcher icon --(tap)--> MainActivity --(Save)--> name stored, shown under the button
                              |
                              +--(three dots: Settings / About)--> Toast pops up
                              |
                              +--(three dots: Exit)--> finish(), the screen closes
                              |
                              +--(move the LAUNCHER intent-filter)--> DBActivity --(Save User)--> new row in the ListView
                              |
                              +--(move the LAUNCHER intent-filter)--> DrawerActivity --(tap the hamburger)--> drawer slides in --(Home / Profile / Logout)--> text changes
                              |
                              +--(move the LAUNCHER intent-filter)--> BottomActivity --(tap a tab)--> text changes
```

`MainActivity` is the only screen the launcher opens. `DBActivity` is registered in `AndroidManifest.xml`, but nothing starts it yet, and `DrawerActivity` and `BottomActivity` do not exist until their own lessons create them. Until then you reach each one by moving the `<intent-filter>` block, exactly as the lesson tells you.

---

## How to use these notes

- Read the lessons in order. Each one assumes the file the lesson before it left behind.
- Some lessons share a file. Lesson 3 reads `activity_main.xml` and lesson 4 replaces it; lesson 4 writes `MainActivity.java` and lesson 5 adds two methods below its `onCreate()`; lesson 6 creates `DBActivity.java` and lesson 7 fills it; lesson 2 reads `strings.xml` and lesson 8 adds two lines to it.
- Type the code, do not paste it. You remember what your fingers write.
- Every step names the exact file and the exact place in it — "below the `package` line", "inside `onCreate()`, under `setContentView(...)`".
- Every lesson ends with a "check the complete file" step. Compare yours against it before you run the app.
- **A class name shows red — what now?** Put the caret on it and press `Alt+Enter` (`Option+Enter` on macOS). Android Studio adds the missing import.
- **How do you run the app?** `Ctrl+R` on macOS, `Shift+F10` on Windows. From a terminal in the project root: `./gradlew installDebug`.
- **How do you tidy a messy file?** `Cmd+Alt+L` on macOS, `Ctrl+Alt+L` on Windows reformats the whole file.
- **How do you start again with empty data?** Long-press the app icon on the device, open App info, then Storage, then Clear storage. That wipes both the `user_data` preferences file and `MyApp.db`.

---

## The lessons

| # | Lesson | What you learn | Files it touches |
|---|---|---|---|
| 1 | [The Project Skeleton](project-skeleton.md) | `namespace`, `applicationId`, `minSdk`, `targetSdk`, the `libs.versions.toml` version catalog, `implementation`, `<activity>`, `<intent-filter>`, `LAUNCHER`, `android:exported` | `app/build.gradle.kts`, `gradle/libs.versions.toml`, `settings.gradle.kts`, `AndroidManifest.xml` |
| 2 | [Theme And Resources](theme-and-resources.md) | `@string/app_name`, `@color`, `@style`, `Theme.MaterialComponents.DayNight.DarkActionBar`, `colorPrimary`, `values-night`, mipmap launcher icons | `res/values/*`, `res/values-night/themes.xml`, `res/mipmap-*/` |
| 3 | [Layouts And Views](layouts-and-views.md) | `ConstraintLayout` against `LinearLayout`, `android:orientation`, `match_parent`, `wrap_content`, `android:padding`, `EditText`, `android:hint`, `Button`, `TextView`, `ListView`, `@+id` | the two files in `res/layout/` |
| 4 | [Saving With SharedPreferences](sharedpreferences.md) | `findViewById`, `setOnClickListener`, `getSharedPreferences`, `MODE_PRIVATE`, `edit()`, `putString`, `apply()`, `getString` with a default value, `setText` | `MainActivity.java`, `res/layout/activity_main.xml` |
| 5 | [The Three-Dot Menu](options-menu.md) | menu resource files, `<item>`, `onCreateOptionsMenu`, `getMenuInflater().inflate`, `onOptionsItemSelected`, `getItemId`, `Toast.makeText`, `finish()`, `app:showAsAction` | `res/menu/main_menu.xml`, `MainActivity.java` |
| 6 | [Adding A Second Screen](second-screen.md) | the Empty Views Activity wizard, `<activity android:name=".DBActivity">`, `android:exported`, `setContentView(R.layout.activity_db)`, moving the `LAUNCHER` intent-filter to test a screen | `AndroidManifest.xml`, `DBActivity.java`, `res/layout/activity_db.xml` |
| 7 | [The Users Database](sqlite-create-read.md) | `SQLiteOpenHelper`, `execSQL`, `CREATE TABLE`, the database version and `onUpgrade`, `getWritableDatabase`, `ContentValues`, `insert`, `rawQuery`, `Cursor`, `moveToNext`, `getColumnIndexOrThrow`, `ArrayAdapter`, `simple_list_item_1`, `notifyDataSetChanged` | `DatabaseHelper.java`, `DBActivity.java` |
| 8 | [The Hamburger Menu](hamburger-menu.md) | `DrawerLayout`, `NavigationView`, `app:menu`, `layout_gravity="start"`, `ActionBarDrawerToggle`, `addDrawerListener`, `syncState`, `setDisplayHomeAsUpEnabled`, `setNavigationItemSelectedListener`, `closeDrawers` | `DrawerActivity.java`, `res/menu/drawer_menu.xml`, `res/layout/activity_drawer.xml`, `res/values/strings.xml` |
| 9 | [Bottom Navigation](bottom-menu.md) | `BottomNavigationView`, `app:menu`, `layout_weight`, `setOnItemSelectedListener`, `getItemId`, the built-in `@android:drawable` icons | `BottomActivity.java`, `res/menu/bottom_menu.xml`, `res/layout/activity_bottom.xml` |

Lessons 1 to 3 are read-along: you open files that already exist and learn to read them, and you type nothing. From lesson 4 on, every lesson adds code, and lesson 7 is the longest — eight steps, and the only one that writes two Java files at once.

---

## Project facts

| | |
|---|---|
| package and namespace | `tz.ac.dit.od23storage` |
| language | **Java**, source and target compatibility `VERSION_11` |
| build | **Gradle** wrapper 8.13, **Android Gradle Plugin** 8.13.2, Kotlin DSL `.kts` build files |
| sdk levels | `compileSdk 36`, `targetSdk 36`, `minSdk 24` (Android 7.0) |
| entry point | `MainActivity`, the only activity carrying the `LAUNCHER` intent-filter |
| name on the device | `OD23 Storage`, from `app_name` in `res/values/strings.xml` |
| permissions | none — the manifest declares no `<uses-permission>`, because preferences and the database live in the app's own private folder |
| backup config | `android:allowBackup="true"` with `@xml/backup_rules` and `@xml/data_extraction_rules`, both still the empty generated templates |
| libraries | `androidx.appcompat` 1.7.1, `com.google.android.material` 1.14.0, `androidx.activity` 1.13.0, `androidx.constraintlayout` 2.2.1, `junit` 4.13.2, `androidx.test.ext:junit` 1.3.0, `espresso-core` 3.7.0 |
| not used yet | no **Room**, no **Kotlin**, no **Jetpack Compose**, no **view binding**, no **Fragments**, no **RecyclerView**, no **Navigation component**, no password hashing |

---

## What is in the app

| Class | Its pair file | What it does |
|---|---|---|
| `MainActivity` | `res/layout/activity_main.xml` | **the launcher screen, still an empty shell — one `setContentView` call and an empty `ConstraintLayout`** |
| `DBActivity` | `res/layout/activity_db.xml` | holds the form and the list, inserts a user and shows every saved user |
| `DatabaseHelper` | `MyApp.db`, created on the device | creates the `users` table, and drops and rebuilds it when the version number changes |
| `Theme.OD23Storage` | `res/values/themes.xml`, `res/values-night/themes.xml` | day and night colors, plus the app bar that the menu lessons need |
| `ExampleUnitTest` | `app/src/test/java/…` | **generated placeholder, asserts `2 + 2 == 4` and tests nothing in this app** |
| `ExampleInstrumentedTest` | `app/src/androidTest/java/…` | **generated placeholder, checks the package name only** |

Some names here are historical, and they will confuse you if nobody says so. The project is called *Storage*, but three of the nine lessons are about menus — the repo started as a storage exercise and grew into the wider course. The database file is `MyApp.db`, a leftover from the first draft, not `od23.db`. That first draft still sits in the repo as `sqlite-create-read-old.md`: it splits the code over a `UserContract` class and a `User` model and calls the table `user`, none of which exist in the code today — read `sqlite-create-read.md` instead. And the lesson file `sharedpreferences.md` is the one filename without hyphens.

---

## Still to come

- a button in `MainActivity` that opens `DBActivity` with an **Intent**, so the second screen stops needing the manifest trick
- **`putExtra`** and `getIntent()` to carry the saved name across to that screen
- **`update()`** and **`delete()`** on `SQLiteDatabase`, so the database lesson covers all four operations, not only create and read
- **`setOnItemClickListener`** on the `ListView`, to load a tapped user back into the form
- empty-field checks with **`TextUtils.isEmpty`** before an insert
- password hashing with **bcrypt** in place of the plain text the lesson stores today
- **RecyclerView** and a custom adapter, replacing `ListView` and `ArrayAdapter`
- one **Fragment** per tab, so the bottom bar and the drawer swap real screens instead of a `TextView`
- **Room** on top of SQLite, once the raw SQL is familiar
- real tests with **JUnit** and **Espresso**, replacing the two generated placeholders

---

## Where to look things up

- [Save key-value data](https://developer.android.com/training/data-storage/shared-preferences) — SharedPreferences
- [Save data using SQLite](https://developer.android.com/training/data-storage/sqlite) — the database, `SQLiteOpenHelper` and `Cursor`
- [Layouts](https://developer.android.com/develop/ui/views/layout/declaring-layout) — views, ids and the XML attributes
- [Menus](https://developer.android.com/develop/ui/views/components/menus) — the options menu and the menu resource files

Every lesson ends with a Reference link to the exact page for its own topic.
