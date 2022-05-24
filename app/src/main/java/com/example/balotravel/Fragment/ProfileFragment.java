package com.example.balotravel.Fragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.balotravel.Adapter.MyPostAdapter;
import com.example.balotravel.EditProfileActivity;
import com.example.balotravel.FollowersActivity;
import com.example.balotravel.Model.Post;
import com.example.balotravel.Model.User;
import com.example.balotravel.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.EventListener;
import java.util.HashMap;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


public class ProfileFragment extends Fragment {
    String db = "https://balotravel-9a424-default-rtdb.asia-southeast1.firebasedatabase.app";
    ImageView image_profile, options;
    TextView posts, followers, following, fullname, bio, username,phoneNum;
    Button edit_profile;
    FirebaseUser firebaseUser;
    String profileid;
    User user;
    ImageButton myPost,myFollowers;
    private RecyclerView recyclerView;
    private MyPostAdapter myPostAdapter;
    private List<Post> postList;

    protected FirebaseAuth mAuth = FirebaseAuth.getInstance();
    protected DatabaseReference mDatabase = FirebaseDatabase.getInstance(db).getReference();


    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        SharedPreferences prefs = getContext().getSharedPreferences("PREFS", MODE_PRIVATE);
        profileid = prefs.getString("profileid", "none");

        image_profile = view.findViewById(R.id.image_profile);
        posts = view.findViewById(R.id.posts);
        followers = view.findViewById(R.id.followers);
        following = view.findViewById(R.id.following);
        fullname = view.findViewById(R.id.full_name_profile);
        bio = view.findViewById(R.id.bio);
        edit_profile = view.findViewById(R.id.edit_profile);
        username = view.findViewById(R.id.username);
        phoneNum = view.findViewById(R.id.tv_phone);
        myPost = view.findViewById(R.id.my_post);
        myFollowers = view.findViewById(R.id.my_followers);

        recyclerView = view.findViewById(R.id.recycler_view_profile);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager mLayoutManager = new GridLayoutManager(this.getContext(), 3);
        recyclerView.setLayoutManager(mLayoutManager);
        postList = new ArrayList<>();
        myPostAdapter = new MyPostAdapter(this.getContext(), postList);
        recyclerView.setAdapter(myPostAdapter);
        userInfo();
        checkPost();
        checkFollowers();
        myPosts();
        recyclerView.setVisibility(View.VISIBLE);
        if (profileid.equals(firebaseUser.getUid())){
            edit_profile.setText("Chỉnh sửa trang cá nhân");
        } else {
            checkFollow();
            myFollowers.setVisibility(View.GONE);
        }

        edit_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String btn = edit_profile.getText().toString();

                if (btn.equals("Chỉnh sửa trang cá nhân")){
                    Intent intent = new Intent(getActivity(), EditProfileActivity.class);

                    intent.putExtra("currentUser",user);
                    startActivity(intent);

                } else if (btn.equals("Theo dõi")){

                    FirebaseDatabase.getInstance(db).getReference().child("follows").child(firebaseUser.getUid())
                            .child("following").child(profileid).setValue(true);
                    FirebaseDatabase.getInstance(db).getReference().child("follows").child(profileid)
                            .child("followers").child(firebaseUser.getUid()).setValue(true);
                    addNotification();
                } else if (btn.equals("Đã theo dõi")){

                    FirebaseDatabase.getInstance(db).getReference().child("follows").child(firebaseUser.getUid())
                            .child("following").child(profileid).removeValue();
                    FirebaseDatabase.getInstance(db).getReference().child("follows").child(profileid)
                            .child("followers").child(firebaseUser.getUid()).removeValue();

                }
            }
        });

        myPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                recyclerView.setVisibility(View.VISIBLE);
            }
        });

        followers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), FollowersActivity.class);
                intent.putExtra("id", profileid);
                intent.putExtra("title", "followers");
                startActivity(intent);
            }
        });

        following.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), FollowersActivity.class);
                intent.putExtra("id", profileid);
                intent.putExtra("title", "following");
                startActivity(intent);
            }
        });

        return view;
    }


    private void addNotification(){
        DatabaseReference reference = FirebaseDatabase.getInstance(db).getReference("notifications").child(profileid);

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("userid", firebaseUser.getUid());
        hashMap.put("text", "started following you");
        hashMap.put("postid", "");
        hashMap.put("ispost", false);

        reference.push().setValue(hashMap);
    }

    private void userInfo(){
        DatabaseReference reference = FirebaseDatabase.getInstance(db).getReference("users").child(profileid);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (getContext() == null){
                    return;
                }
                user = dataSnapshot.getValue(User.class);
                Log.d("user",user.getImage_profile());
                Glide.with(getContext()).load(user.getImage_profile()).into(image_profile);
                username.setText(user.getFullname());
                fullname.setText(" "+user.getFullname());
                phoneNum.setText("0"+user.getPhonenumber());
                bio.setText(user.getBio());

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void checkFollow(){
        DatabaseReference reference = FirebaseDatabase.getInstance(db).getReference()
                .child("follows").child(firebaseUser.getUid()).child("following");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.child(profileid).exists()){
                    edit_profile.setText("Đã theo dõi");
                } else{
                    edit_profile.setText("Theo dõi");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void checkFollowers() {
        DatabaseReference reference = mDatabase.child("follows").child(profileid).child("followers");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                followers.setText(""+dataSnapshot.getChildrenCount());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        DatabaseReference reference1 = mDatabase.child("follows").child(profileid).child("following");
        reference1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                following.setText(""+dataSnapshot.getChildrenCount());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    private void checkPost() {
        DatabaseReference ref = mDatabase.child("posts");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int numPost=0;
                for(DataSnapshot _snapshot: snapshot.getChildren()){
                    Post p = _snapshot.getValue(Post.class);
                    if(p.getPostPublisher().equals(profileid)){
                        ++numPost;
                    }
                }
                posts.setText(String.valueOf(numPost));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("Num of MyPost","Error show num of MyPost");
            }
        });
    }
    private void myPosts(){
        DatabaseReference reference = mDatabase.child("posts");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                postList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Post post = snapshot.getValue(Post.class);
                    post.setPostId(snapshot.getKey());
                    if (post.getPostPublisher().equals(profileid)){
                        postList.add(post);
                    }
                }
                Collections.reverse(postList);
                myPostAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

//    private void readSaves(){
//        DatabaseReference reference = FirebaseDatabase.getInstance(db).getReference("posts");
//        reference.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                postList_saves.clear();
//                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
//                    Post post = snapshot.getValue(Post.class);
//
//                    for (String id : my) {
//                        if (post.getPostid().equals(id)) {
//                            postList_saves.add(post);
//                        }
//                    }
//                }
//                myFotosAdapter_saves.notifyDataSetChanged();
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        });
//    }
}