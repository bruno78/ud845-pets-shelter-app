/*
 * Copyright (C) 2016 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.android.pets;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.android.pets.data.PetDbHelper;
import com.example.android.pets.data.PetContract.PetEntry;
import com.example.android.pets.data.PetProvider;

import static com.example.android.pets.data.PetContract.BASE_CONTENT_URI;

/**
 * Displays list of pets that were entered and stored in the app.
 */
public class CatalogActivity extends AppCompatActivity {

    private PetDbHelper mPetDbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_catalog);

        // Setup FAB to open EditorActivity
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CatalogActivity.this, EditorActivity.class);
                startActivity(intent);
            }
        });

        displayDatabaseInfo();
    }

    /**
     * Temporary helper method to display information in the onscreen of the TextView about the state of
     * the pets database
     */
    private void displayDatabaseInfo() {

        // Perform this raw SQL query "SELECT * FROM pets"
        // to get a Cursor that contains all rows from the pets table.
        // Cursor cursor = db.rawQuery("SELECT * FROM " + PetEntry.TABLE_NAME, null);
        String[] projection = {
                PetEntry._ID,
                PetEntry.COLUMN_NAME,
                PetEntry.COLUMN_BREED,
                PetEntry.COLUMN_GENDER,
                PetEntry.COLUMN_WEIGHT
        };

        // Peform a query on the provider using the ContentResolver
        // Use the @link{PetEntry#CONTENT_URI} to access the pet data.
        Cursor cursor = getContentResolver().query(
                PetEntry.CONTENT_URI,  // The content URI of the words table
                projection,            // The columns to return for each row
                null,         // Selection criteria
                null,      // Selection criteria
                null);        // The sort order for the returned rows

        TextView displayView = (TextView) findViewById(R.id.text_view_pet);

        try {
            // Display the number of rows in the Cursor (which reflects the number of rows in the
            // pets table in the database).
            displayView.setText("The pets table contains: " + cursor.getCount() + " pets. \n\n");
            displayView.append(PetEntry._ID + " - " + PetEntry.COLUMN_NAME + " - " +
                    PetEntry.COLUMN_GENDER + " - " + PetEntry.COLUMN_BREED + " - " + PetEntry.COLUMN_WEIGHT + "\n\n");

            // Figure out the index of each column
            int idColumnIndex = cursor.getColumnIndex(PetEntry._ID);
            int nameColumnIndex = cursor.getColumnIndex(PetEntry.COLUMN_NAME);
            int genderColumnIndex = cursor.getColumnIndex(PetEntry.COLUMN_GENDER);
            int breedColumnIndex = cursor.getColumnIndex(PetEntry.COLUMN_BREED);
            int weightColumnIndex = cursor.getColumnIndex(PetEntry.COLUMN_WEIGHT);

            while(cursor.moveToNext()) {

                String id = Integer.toString(cursor.getInt(idColumnIndex));
                String name = cursor.getString(nameColumnIndex);
                String gender = cursor.getString(genderColumnIndex);
                String breed = cursor.getString(breedColumnIndex);
                String weight = Integer.toString(cursor.getInt(weightColumnIndex));

                displayView.append(id + " - " + name + " - " + gender + " - " + breed + " - " + weight + "\n");

            }

        } finally {
            // Always close the cursor when you're done reading from it. This releases all its
            // resources and makes it invalid.
            cursor.close();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu options from the res/menu/menu_catalog.xml file.
        // This adds menu items to the app bar.
        getMenuInflater().inflate(R.menu.menu_catalog, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // User clicked on a menu option in the app bar overflow menu
        switch (item.getItemId()) {
            // Respond to a click on the "Insert dummy data" menu option
            case R.id.action_insert_dummy_data:
                // Do nothing for now
                inserDummyData();
                displayDatabaseInfo();
                return true;
            // Respond to a click on the "Delete all entries" menu option
            case R.id.action_delete_all_entries:
                // Do nothing for now
                deleteAllEntries();
                displayDatabaseInfo();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void inserDummyData() {
        // Get an instance of PetDbHelper
        mPetDbHelper = new PetDbHelper(this);

        // Get data repository in write mode
        SQLiteDatabase db = mPetDbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(PetEntry.COLUMN_NAME, "Toto");
        values.put(PetEntry.COLUMN_BREED, "Terrier");
        values.put(PetEntry.COLUMN_GENDER, PetEntry.GENDER_MALE);
        values.put(PetEntry.COLUMN_WEIGHT, 7);
        long newRowId = db.insert(PetEntry.TABLE_NAME, null, values);
        db.close();

        Log.v("CatalogActivtiy", "New row ID " + newRowId);
    }

    private void deleteAllEntries() {
        // Get an instance of PetDbHelper
        mPetDbHelper = new PetDbHelper(this);

        // Get data repository in write mode
        SQLiteDatabase db = mPetDbHelper.getWritableDatabase();
        db.execSQL("DELETE FROM "+ PetEntry.TABLE_NAME);
        db.close();
    }
}