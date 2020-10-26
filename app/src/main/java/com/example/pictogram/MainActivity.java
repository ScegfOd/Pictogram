package com.example.pictogram;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.pictogram.Model.Post;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.io.File;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    public static final int CAPTURE_IMAGE_CODE = 111;

    EditText description;
    ImageView photo;
    private File photoFile;
    public String photoFileName = "pic.jpg";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        description = findViewById(R.id.description);
        photo = findViewById(R.id.imageView);
        queryPosts();
    }

    private void queryPosts() {
        // Specify which class to query
        ParseQuery<Post> query = ParseQuery.getQuery(Post.class);
        query.include(Post.KEY_USER);
        query.findInBackground( new FindCallback<Post>() {
            @Override
            public void done(List<Post> posts, ParseException e) {
                if (e == null) {
                    for(Post post : posts){
                        Log.i(TAG, post.toString());
                        Log.i(TAG, post.getDescription());
                        Log.i(TAG, "Desc: " + post.getDescription() + "; user: " + post.getUser().getUsername());
                    }
                } else {
                    Log.e(TAG, "could not load posts", e);
                }
            }
        });
    }

    public void submitPost(View view) {
        String description = this.description.getText().toString();
        if(description.isEmpty()){
            Toast.makeText(view.getContext(), "Every post needs a description!", Toast.LENGTH_SHORT).show();
            return;
        }
        if(photoFile == null || photo.getDrawable() == null){
            Toast.makeText(view.getContext(), "Every post needs a photo!", Toast.LENGTH_SHORT).show();
            return;
        }
        ParseUser current_user = ParseUser.getCurrentUser();
        savePost(current_user, description, photoFile);
    }

    private void savePost(ParseUser current_user, String desc, File photoFile) {
        Post post = new Post();
        post.setDescription(desc);
        post.setUser(current_user);
        post.setImage(new ParseFile(photoFile));
        post.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if(e != null){
                    Toast.makeText(MainActivity.this, "Post failed!", Toast.LENGTH_SHORT).show();
                    Log.e(TAG, "post not saved!", e);
                }else{
                    Toast.makeText(MainActivity.this, "Post successful!", Toast.LENGTH_SHORT).show();
                    description.setText("");
                    photo.setImageResource(0);
                }
            }
        });
    }

    public void camera(View view) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        photoFile = getPhotoFileUri(photoFileName);

        Uri fileProvider = FileProvider.getUriForFile( MainActivity.this, "com.codepath.fileprovider", photoFile);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileProvider);

        if(intent.resolveActivity(getPackageManager()) != null){
            startActivityForResult(intent, CAPTURE_IMAGE_CODE);
        }else{
            Toast.makeText(MainActivity.this, "Get a phone app...", Toast.LENGTH_LONG).show();
        }

    }

    private File getPhotoFileUri(String photoFileName) {
        File mediaStorageDir = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), TAG);

        if(!mediaStorageDir.exists() && !mediaStorageDir.mkdirs()){
            Log.d(TAG, "Failed to make directory");
        }

        return new File(mediaStorageDir.getPath() + File.separator + photoFileName);
    }

    @Override
    protected void onActivityResult(int request_code, int result_code, @Nullable Intent data){
        super.onActivityResult(request_code,result_code,data);
        if(request_code == CAPTURE_IMAGE_CODE){
            if(result_code == RESULT_OK){
                Bitmap image = BitmapFactory.decodeFile(photoFile.getAbsolutePath());
                //resize
                ((ImageView)findViewById(R.id.imageView)).setImageBitmap(image);
            }else{
                Toast.makeText(this, "Picture not taken!", Toast.LENGTH_SHORT).show();
            }
        }

    }

    public void logout(View view) {
        ParseUser.logOut();
        startActivity(new Intent(this, LoginActivity.class));
        finish();
    }
}