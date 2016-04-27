package edu.uw.filedemo;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class PhotoActivity extends AppCompatActivity {

    private static final String TAG = "Photo";

    private static final int REQUEST_IMAGE_CAPTURE = 1;

    private Uri pictureUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo);

        //action bar "back"
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    public void takePicture(View v){
        Log.v(TAG, "Taking picture...");

        File file = null;

        try {
            // Create the file & name where the picture that we get from camera should go
            String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            File dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
            file = new File(dir, "PIC_" + timestamp);
            file.createNewFile();

            pictureUri = Uri.fromFile(file);
        } catch (IOException ioe) {
            Log.d(TAG, Log.getStackTraceString(ioe));
        }

        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, pictureUri);

        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {

//            Bundle extras = data.getExtras();
//            Bitmap imageBitmap = (Bitmap)extras.get("data");
            ImageView imageView = (ImageView)findViewById(R.id.imgThumbnail);
            imageView.setImageURI(pictureUri);
//            imageView.setImageBitmap(imageBitmap);

        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public void sharePicture(View v){
        Log.v(TAG, "Sharing picture...");

        Intent intent = new Intent(Intent.ACTION_SEND);
        // Send the location (uri) to the activity that accepts it
        intent.setType("image/*");   // We're going to send uri of an image file
        intent.putExtra(Intent.EXTRA_STREAM, pictureUri);

        // Text on chooser
        Intent chooser = Intent.createChooser(intent, "Share File");// Intent that asks user which application to use to send other intent
        if (intent.resolveActivity(getPackageManager()) != null) {  // Ensures some activity can handle
            startActivity(chooser);
        }

    }
}
