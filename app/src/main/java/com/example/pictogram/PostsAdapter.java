package com.example.pictogram;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.pictogram.Model.Post;

import java.util.List;

public class PostsAdapter extends RecyclerView.Adapter<PostsAdapter.ViewHolder> {

    private Context context;
    private List<Post> posts;

    public PostsAdapter(Context context, List<Post> posts) {
        this.context = context;
        this.posts = posts;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_post, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Post post = posts.get(position);
        holder.bind(post);
    }

    @Override
    public int getItemCount() {
        return posts.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        private TextView tv_username;
        private TextView tv_description;
        private ImageView iv_pic;

        public ViewHolder(@NonNull View itemView){
            super(itemView);
            tv_username = itemView.findViewById(R.id.tv_name);
            tv_description = itemView.findViewById(R.id.tv_desc);
            iv_pic = itemView.findViewById(R.id.iv_pic);
        }

        public void bind(Post post) {
            tv_username.setText(post.getUser().getUsername());
            tv_description.setText(post.getDescription());
            Glide.with(context).load(post.getImage().getUrl()).into(iv_pic);
        }
    }

    /* Within the RecyclerView.Adapter class */



// Clean all elements of the recycler

    public void clear() {

        posts.clear();

        notifyDataSetChanged();

    }



// Add a list of items -- change to type used

    public void addAll(List<Post> list) {

        posts.addAll(list);

        notifyDataSetChanged();

    }
}
