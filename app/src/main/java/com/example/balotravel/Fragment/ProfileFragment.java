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
import com.example.balotravel.EditProfileActivity;
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
import java.util.EventListener;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;


public class ProfileFragment extends Fragment {
    String db = "https://balotravel-9a424-default-rtdb.asia-southeast1.firebasedatabase.app";
    ImageView image_profile, options;
    TextView posts, followers, following, fullname, bio, username,phoneNum;
    Button edit_profile;

    User currentUser;

    protected FirebaseAuth mAuth = FirebaseAuth.getInstance();
    protected DatabaseReference mDatabase = FirebaseDatabase.getInstance(db).getReference();


    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
//        SharedPreferences prefs = getContext().getSharedPreferences("PREFS", MODE_PRIVATE);
//        currentUser.setUserId(prefs.getString("profileid", "none"));
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                int i=0;
                for(DataSnapshot ds : dataSnapshot.getChildren()) {
                    try {
                        String value = (String) ds.getValue();
                        if(i==0) currentUser.setBio(value);
                        if(i==1) currentUser.setFullname(value);
                        if(i==2) currentUser.setImage_profile(value);
                        if(i==4) currentUser.setUserId(value);
                        if(i==5) currentUser.setUsername(value);

                    }catch (ClassCastException e){
                        long value= (long) ds.getValue();
                        currentUser.setPhonenumber((int) value);
                    }
                    i++;
                }

                bio.setText(currentUser.getBio());
                fullname.setText(currentUser.getFullname());
                phoneNum.setText("0" + currentUser.getPhonenumber()+"");
                
                Glide.with(ProfileFragment.this).load(Uri.parse(currentUser.getImage_profile())).into(image_profile);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        };
        mDatabase.child("users").child(mAuth.getUid()).addListenerForSingleValueEvent(valueEventListener);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // get all information of user
        currentUser = new User();
        currentUser.setUserId(mAuth.getCurrentUser().getUid());
        image_profile = view.findViewById(R.id.image_profile);
        posts = view.findViewById(R.id.posts);
        followers = view.findViewById(R.id.followers);
        following = view.findViewById(R.id.following);
        fullname = view.findViewById(R.id.full_name_profile);
        bio = view.findViewById(R.id.bio);
        edit_profile = view.findViewById(R.id.edit_profile);
        username = view.findViewById(R.id.username);
        phoneNum = view.findViewById(R.id.tv_phone);


        edit_profile.setOnClickListener(v->{
                Intent intent = new Intent(getActivity(), EditProfileActivity.class);
                intent.putExtra("currentUser",currentUser);
                startActivity(intent);

        });

        checkPost();
        checkFollowers();
    }

    private void checkFollowers() {
        DatabaseReference reference = mDatabase.child("follows").child(currentUser.getUserId()).child("followers");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                followers.setText(""+dataSnapshot.getChildrenCount());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        DatabaseReference reference1 = mDatabase.child("follows").child(currentUser.getUserId()).child("following");
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
                    Log.d("My post",p.getPostPublisher() + " current user " + currentUser.getUserId()+" " + currentUser.getFullname());
                    if(p.getPostPublisher() == currentUser.getUserId()){
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
}