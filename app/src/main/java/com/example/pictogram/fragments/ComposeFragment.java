package com.example.pictogram.fragments;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.pictogram.LoginActivity;
import com.example.pictogram.MainActivity;
import com.example.pictogram.Model.Post;
import com.example.pictogram.R;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.io.File;
import java.util.List;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 */
public class ComposeFragment extends Fragment {

    private static final String TAG = "ComposeFragment";
    public static final int CAPTURE_IMAGE_CODE = 111;

    EditText description;
    ImageView photo;
    private File photoFile;
    public String photoFileName = "pic.jpg";

    public ComposeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onViewCreated(final View view, Bundle savedInstanceState) {
        description = view.findViewById(R.id.description);
        photo = view.findViewById(R.id.image_view);
        ((Button)view.findViewById(R.id.submit)).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String desc = description.getText().toString();
                if(desc.isEmpty()){
                    Toast.makeText(view.getContext(), "Every post needs a description!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(photoFile == null || photo.getDrawable() == null){
                    Toast.makeText(view.getContext(), "Every post needs a photo!", Toast.LENGTH_SHORT).show();
                    return;
                }
                ParseUser current_user = ParseUser.getCurrentUser();
                savePost(current_user, desc, photoFile);
            }
        });

        ((Button)view.findViewById(R.id.logout)).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                ParseUser.logOut();
                startActivity(new Intent(getContext(), LoginActivity.class));
                getActivity().finish();
            }
        });

        ((Button)view.findViewById(R.id.photo)).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

                photoFile = getPhotoFileUri(photoFileName);

                Uri fileProvider = FileProvider.getUriForFile( getContext(), "com.codepath.fileprovider", photoFile);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, fileProvider);

                if(intent.resolveActivity(getContext().getPackageManager()) != null){
                    startActivityForResult(intent, CAPTURE_IMAGE_CODE);
                }else{
                    Toast.makeText(getContext(), "Get a phone app...", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_compose, container, false);
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
                    Toast.makeText(getContext(), "Post failed!", Toast.LENGTH_SHORT).show();
                    Log.e(TAG, "post not saved!", e);
                }else{
                    Toast.makeText(getContext(), "Post successful!", Toast.LENGTH_SHORT).show();
                    description.setText("");
                    photo.setImageResource(0);
                }
            }
        });
    }


    private File getPhotoFileUri(String photoFileName) {
        File mediaStorageDir = new File(getContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES), TAG);

        if(!mediaStorageDir.exists() && !mediaStorageDir.mkdirs()){
            Log.d(TAG, "Failed to make directory");
        }

        return new File(mediaStorageDir.getPath() + File.separator + photoFileName);
    }

    @Override
    public void onActivityResult(int request_code, int result_code, @Nullable Intent data){
        super.onActivityResult(request_code,result_code,data);
        if(request_code == CAPTURE_IMAGE_CODE){
            if(result_code == RESULT_OK){
                Bitmap image = BitmapFactory.decodeFile(photoFile.getAbsolutePath());
                //resize
                photo.setImageBitmap(image);
            }else{
                Toast.makeText(getContext(), "Picture not taken!", Toast.LENGTH_SHORT).show();
            }
        }

    }
}