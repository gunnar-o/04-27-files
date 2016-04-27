package edu.uw.filedemo;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "Main";

    private RadioButton externalButton; //save reference for quick access

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        externalButton = (RadioButton)findViewById(R.id.radio_external);
    }


    public void saveFile(View v){
        Log.v(TAG, "Saving file...");
        EditText textEntry = (EditText)findViewById(R.id.textEntry); //what we're going to save

        if(externalButton.isChecked()){ //external storage

            if (isExternalStorageWritable()) {
                                                                        // Which directory we want to access
                //File dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS); // Phone's file hierarchy

                File dir = getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS); // Documents in the file hierarchy for this app
                File file = new File(dir, "notes.txt");
                Log.v(TAG, file.getAbsolutePath());

                // Write what the user typed in in textEntry to the file
                try {
                    PrintWriter writer = new PrintWriter(new FileWriter(file, true));
                    writer.println(textEntry.getText());
                    writer.close();

                } catch (IOException ioe) {
                    Log.d(TAG, Log.getStackTraceString(ioe));
                }

            }
        }
        else { //internal storage
            // Internal storage - don't need permissions, don't need to check if it's writable/mounted

//            File dir = getFilesDir();
//            File file = new File(dir, "notes.txt");
//            Log.v(TAG, file.getAbsolutePath());
//
//            // Write what the user typed in in textEntry to the file
//            try {
//                PrintWriter writer = new PrintWriter(new FileWriter(file, true));
//                writer.println(textEntry.getText());
//                writer.close();
//
//            } catch (IOException ioe) {
//                Log.d(TAG, Log.getStackTraceString(ioe));
//            }

            try {
                FileOutputStream fis = openFileOutput("notes.txt", MODE_PRIVATE);
                // some other stuff (look in doc)
            } catch (IOException ioe) {

            }
        }
    }


    public void loadFile(View v){
        Log.v(TAG, "Loading file...");
        TextView textDisplay = (TextView)findViewById(R.id.txtDisplay); //where we're going to show
        textDisplay.setText(""); //clear initially

        if(externalButton.isChecked()){ //external storage

            if (isExternalStorageWritable()) {
                File dir = getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS); // Documents in the file hierarchy for this app
                File file = new File(dir, "notes.txt");

                try {
                    BufferedReader reader = new BufferedReader(new FileReader(file));
                    StringBuilder text = new StringBuilder();

                    String line = reader.readLine();
                    // Appends each line one by one using the StringBuilder
                    while (line != null) {
                        text.append(line + "\n");
                        line = reader.readLine();

                    }
                } catch(IOException ioe) {

                }
            }
        }
        else { //internal storage - private to the app
            File dir = getFilesDir(); // data/data/package.name/files    //getExternalFilesDir() is the equivalent for internal
            File file = new File(dir, "notes.txt");

            try {
                BufferedReader reader = new BufferedReader(new FileReader(file));
                StringBuilder text = new StringBuilder();

                String line = reader.readLine();
                // Appends each line one by one using the StringBuilder
                while (line != null) {
                    text.append(line + "\n");
                    line = reader.readLine();

                }
            } catch(IOException ioe) {

            }
        }
    }


    public void shareFile(View v) {
        Log.v(TAG, "Sharing file...");

        Uri fileUri = null;


        if(externalButton.isChecked()){ //external storage

            File dir = getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS);
            File file = new File(dir, "notes.txt");
            fileUri = Uri.fromFile(file); // Gets the uri of the given file
        }
        else { //internal storage
            // Internal files can't be shared (except using FileProvider - pain in ass)
            File dir = getFilesDir();
            File file = new File(dir, "notes.txt");
            fileUri = Uri.fromFile(file); // Gets the uri of the given file
        }
        //
        Intent intent = new Intent(Intent.ACTION_SEND);
        // Send the location (uri) to the activity that accepts it
        intent.setType("text/plain");   // We're going to send uri of a plain text file
        intent.putExtra(Intent.EXTRA_STREAM, fileUri);

                                                    // Text on chooser
        Intent chooser = Intent.createChooser(intent, "Share File");// Intent that asks user which application to use to send other intent
        if (intent.resolveActivity(getPackageManager()) != null) {  // Ensures some activity can handle
            startActivity(intent);
        }
    }

    // Checks to make sure storage is available
    public boolean isExternalStorageWritable() {
        String extStorageState = Environment.getExternalStorageState();
        return (extStorageState.equals(Environment.MEDIA_MOUNTED));
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case R.id.menu_photo:
                startActivity(new Intent(MainActivity.this, PhotoActivity.class));
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
