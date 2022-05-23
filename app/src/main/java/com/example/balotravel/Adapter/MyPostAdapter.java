package com.example.balotravel.Adapter;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.balotravel.Fragment.PostDetailFragment;
import com.example.balotravel.Model.Post;
import com.example.balotravel.R;

import java.util.List;

public class MyPostAdapter extends RecyclerView.Adapter<MyPostAdapter.ImageViewHolder> {

    private Context mContext;
    private List<Post> mPosts;

    public MyPostAdapter(Context context, List<Post> posts){
        mContext = context;
        mPosts = posts;
    }

    @NonNull
    @Override
    public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.photos_item, parent, false);
        Log.d("inside","123");
        return new ImageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ImageViewHolder holder, int position) {
        final Post post = mPosts.get(position);
        Log.d("post image in profile", post.getPostId()+" "+post.getPostImage());
        Glide.with(mContext).load(post.getPostImage()).into(holder.post_image);

        holder.post_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences.Editor editor = mContext.getSharedPreferences("PREFS", MODE_PRIVATE).edit();
                editor.putString("postid", post.getPostId());
                editor.apply();

                ((FragmentActivity)mContext).getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new PostDetailFragment()).commit();
            }
        });

    }

    @Override
    public int getItemCount() {
        return mPosts.size();
    }

    public class ImageViewHolder extends RecyclerView.ViewHolder {

        public ImageView post_image;


        public ImageViewHolder(View itemView) {
            super(itemView);
            Log.d("inside","11123");
            post_image = itemView.findViewById(R.id.post_image);

        }
    }
}