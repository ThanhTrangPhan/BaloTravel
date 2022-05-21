package com.example.balotravel.Adapter;

import static android.content.Context.MODE_PRIVATE;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;
import com.example.balotravel.CommentActivity;
import com.example.balotravel.FollowersActivity;
import com.example.balotravel.Fragment.PostDetailFragment;
import com.example.balotravel.Fragment.ProfileFragment;
import com.example.balotravel.Model.Place;
import com.example.balotravel.Model.Post;
import com.example.balotravel.Model.User;
import com.example.balotravel.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthEmailException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.ViewHolder>  {
    private String db ="https://balotravel-9a424-default-rtdb.asia-southeast1.firebasedatabase.app";
    private Context mContext;
    private List<Post> mPost;
    private DatabaseReference ref = FirebaseDatabase.getInstance(db).getReference();
    private FirebaseUser firebaseUser;

    public PostAdapter(Context mContext, List<Post> mPost){
        this.mContext = mContext;
        this.mPost = mPost;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.post_item,parent, false);
        return new PostAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int i) {
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        final Post post = mPost.get(i);
        List<SlideModel> listPostImg = new ArrayList<>();
        ArrayList<Place> places = post.getPlaceList();

        for(Place _p : places){
            ArrayList<String> listImages = _p.getImageList();
            for(String _i : listImages){
                Log.d("adpater img",_i);
                listPostImg.add(new SlideModel(_i,_p.getName(), null));
            }

        }
        holder.post_image_slider.setImageList(listPostImg);
//        ref.child(post.getPostId()).addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                for(DataSnapshot snap : snapshot.getChildren()){
//                    Post post = snap.getValue(Post.class);
//
//
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });

        if(post.getDescription().equals("")){
            holder.description.setVisibility(View.GONE);
        } else {
            holder.description.setVisibility(View.VISIBLE);
            holder.description.setText(post.getDescription());
        }
        publisherInfo(holder.image_profile, holder.username, holder.publisher, post.getPostPublisher());
        //isLiked(post.getPostId(), holder.like);
        //isSaved(post.getPostId(), holder.save);
//        nrLikes(holder.likes, post.getPostId());
//        getCommetns(post.getPostId(), holder.comments);

        holder.like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (holder.like.getTag().equals("like")) {
                    ref.child("likes").child(post.getPostId())
                            .child(firebaseUser.getUid()).setValue(true);
                    //addNotification(post.getPostPublisher(), post.getPostId());
                } else {
                    ref.child("likes").child(post.getPostId())
                            .child(firebaseUser.getUid()).removeValue();
                }
            }
        });

        holder.save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (holder.save.getTag().equals("save")){
                    ref.child("saves").child(firebaseUser.getUid())
                            .child(post.getPostId()).setValue(true);
                } else {
                    ref.child("saves").child(firebaseUser.getUid())
                            .child(post.getPostId()).removeValue();
                }
            }
        });

        holder.image_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences.Editor editor = mContext.getSharedPreferences("PREFS", MODE_PRIVATE).edit();
                editor.putString("profileid", post.getPostPublisher());
                editor.apply();

                ((FragmentActivity)mContext).getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new ProfileFragment()).commit();
            }
        });

        holder.username.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences.Editor editor = mContext.getSharedPreferences("PREFS", MODE_PRIVATE).edit();
                editor.putString("profileid", post.getPostPublisher());
                editor.apply();

                ((FragmentActivity)mContext).getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new ProfileFragment()).commit();
            }
        });

        holder.publisher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences.Editor editor = mContext.getSharedPreferences("PREFS", MODE_PRIVATE).edit();
                editor.putString("profileid", post.getPostPublisher());
                editor.apply();

                ((FragmentActivity)mContext).getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new ProfileFragment()).commit();
            }
        });
//
        holder.comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, CommentActivity.class);
                intent.putExtra("postid", post.getPostId());
                intent.putExtra("publisherid", post.getPostPublisher());
                mContext.startActivity(intent);
            }
        });

        holder.comments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, CommentActivity.class);
                intent.putExtra("postid", post.getPostId());
                intent.putExtra("publisherid", post.getPostPublisher());
                mContext.startActivity(intent);
            }
        });

        holder.post_image_slider.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences.Editor editor = mContext.getSharedPreferences("PREFS", MODE_PRIVATE).edit();
                editor.putString("postid", post.getPostId());
                editor.apply();

                ((FragmentActivity)mContext).getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new PostDetailFragment()).commit();
            }
        });

        holder.likes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, FollowersActivity.class);
                intent.putExtra("id", post.getPostId());
                intent.putExtra("title", "likes");
                mContext.startActivity(intent);
            }
        });

//        holder.more.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                PopupMenu popupMenu = new PopupMenu(mContext, view);
//                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
//                    @Override
//                    public boolean onMenuItemClick(MenuItem menuItem) {
//                        switch (menuItem.getItemId()){
//                            case R.id.edit:
//                                editPost(post.getPostid());
//                                return true;
//                            case R.id.delete:
//                                final String id = post.getPostid();
//                                FirebaseDatabase.getInstance().getReference("Posts")
//                                        .child(post.getPostid()).removeValue()
//                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
//                                            @Override
//                                            public void onComplete(@NonNull Task<Void> task) {
//                                                if (task.isSuccessful()){
//                                                    deleteNotifications(id, firebaseUser.getUid());
//                                                }
//                                            }
//                                        });
//                                return true;
//                            case R.id.report:
//                                Toast.makeText(mContext, "Reported clicked!", Toast.LENGTH_SHORT).show();
//                                return true;
//                            default:
//                                return false;
//                        }
//                    }
//                });
//                popupMenu.inflate(R.menu.post_menu);
//                if (!post.getPublisher().equals(firebaseUser.getUid())){
//                    popupMenu.getMenu().findItem(R.id.edit).setVisible(false);
//                    popupMenu.getMenu().findItem(R.id.delete).setVisible(false);
//                }
//                popupMenu.show();
//            }
//        });
    }




    @Override
    public int getItemCount() {
        return mPost.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder{
        public ImageView image_profile, like, save;
        public ImageView  comments;
        public ImageSlider post_image_slider;
        public TextView username, likes, publisher, description, comment;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            image_profile = itemView.findViewById(R.id.image_profile);
            post_image_slider = itemView.findViewById(R.id.post_image_slider);
            like = itemView.findViewById(R.id.like);
            save = itemView.findViewById(R.id.save);
            comments = itemView.findViewById(R.id.comment);
            username = itemView.findViewById(R.id.username);
            likes = itemView.findViewById(R.id.likes);
            publisher = itemView.findViewById(R.id.publisher);
            description = itemView.findViewById(R.id.description);
            comment = itemView.findViewById(R.id.comments);

        }
    }

    private void publisherInfo(ImageView image_profile, TextView username, TextView publisher, String userId){
        DatabaseReference reference = FirebaseDatabase.getInstance(db).getReference("users").child(userId);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user = snapshot.getValue(User.class);
                Glide.with(mContext).load(user.getImage_profile()).into(image_profile);
                username.setText(user.getFullname());
                publisher.setText(user.getFullname());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private void isLiked(String postId, ImageView like) {
        final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference reference = FirebaseDatabase.getInstance(db).getReference()
                .child("likes").child(postId);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.child(firebaseUser.getUid()).exists()){
                    like.setImageResource(R.drawable.ic_baseline_favorite_24);
                    like.setTag("liked");
                } else{
                    like.setImageResource(R.drawable.ic_baseline_favorite_border_24);
                    like.setTag("like");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void isSaved(final String postid, final ImageView imageView){

        final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        DatabaseReference reference = FirebaseDatabase.getInstance(db).getReference()
                .child("saves").child(firebaseUser.getUid());
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.child(postid).exists()){
                    imageView.setImageResource(R.drawable.ic_baseline_bookmark_24);
                    imageView.setTag("saved");
                } else{
                    imageView.setImageResource(R.drawable.ic_baseline_bookmark_border_24);
                    imageView.setTag("save");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void editPost(final String postid){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(mContext);
        alertDialog.setTitle("Edit Post");

        final EditText editText = new EditText(mContext);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        editText.setLayoutParams(lp);
        alertDialog.setView(editText);

        getText(postid, editText);

        alertDialog.setPositiveButton("Edit",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        HashMap<String, Object> hashMap = new HashMap<>();
                        hashMap.put("description", editText.getText().toString());

                        FirebaseDatabase.getInstance().getReference("posts")
                                .child(postid).updateChildren(hashMap);
                    }
                });
        alertDialog.setNegativeButton("Cancel",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });
        alertDialog.show();
    }

    private void getText(String postid, final EditText editText){
        DatabaseReference reference = FirebaseDatabase.getInstance(db).getReference("posts")
                .child(postid);
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                editText.setText(dataSnapshot.getValue(Post.class).getDescription());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}