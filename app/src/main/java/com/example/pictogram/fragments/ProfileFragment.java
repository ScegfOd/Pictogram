package com.example.pictogram.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.pictogram.Model.Post;
import com.example.pictogram.R;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.List;


public class ProfileFragment extends PostsFragment {
    protected void queryPosts() {
        // Specify which class to query
        ParseQuery<Post> query = ParseQuery.getQuery(Post.class);
        query.include(Post.KEY_USER);
        query.whereEqualTo(Post.KEY_USER, ParseUser.getCurrentUser());
        query.setLimit(20);
        query.addDescendingOrder(Post.KEY_CREATED_AT);
        query.findInBackground( new FindCallback<Post>() {
            @Override
            public void done(List<Post> posts, ParseException e) {
                if (e == null) {
                    for(Post post : posts){
                        Log.i(TAG, post.toString());
                        Log.i(TAG, post.getDescription());
                        Log.i(TAG, "Desc: " + post.getDescription() + "; user: " + post.getUser().getUsername());
                    }
                    all_posts.clear();
                    all_posts.addAll(posts);
                    postsAdapter.notifyDataSetChanged();
                } else {
                    Log.e(TAG, "could not load posts", e);
                }
            }
        });
        swipeContainer.setRefreshing(false);
    }
}