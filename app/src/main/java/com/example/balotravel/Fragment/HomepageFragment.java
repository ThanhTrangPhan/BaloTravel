package com.example.balotravel.Fragment;

import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.example.balotravel.Adapter.PostAdapter;
import com.example.balotravel.Model.Place;
import com.example.balotravel.Model.Post;
import com.example.balotravel.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class HomepageFragment extends Fragment {
    private String urlFirebase = "https://balotravel-9a424-default-rtdb.asia-southeast1.firebasedatabase.app/";
    private RecyclerView recyclerView;
    private PostAdapter postAdapter;
    private List<Post> postList;
    private List<String> followingList;
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.fragment_home, container,false);
        recyclerView = view.findViewById(R.id.recycler_view_home);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        postList = new ArrayList<>();
        postAdapter = new PostAdapter(this.getContext(),postList);
        recyclerView.setAdapter(postAdapter);
        checkFollowing();
        //readPosts();
        return view;
    }

    private void checkFollowing(){
        followingList = new ArrayList<>();

        DatabaseReference reference = FirebaseDatabase.getInstance(urlFirebase).getReference("follows")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .child("following");
        Log.d("inside","22");

        ValueEventListener eventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                followingList.clear();
                Log.d("inside","22333");
                for(DataSnapshot _snapshot : dataSnapshot.getChildren()){
                    Log.d("following list ",_snapshot.getKey());
                    followingList.add(_snapshot.getKey());
                }
                readPosts();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {}
        };

        reference.addListenerForSingleValueEvent(eventListener);
        reference.addValueEventListener(eventListener);
    }

    private void readPosts(){
        DatabaseReference reference = FirebaseDatabase.getInstance(urlFirebase).getReference("posts");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                postList.clear();
                for(DataSnapshot _snapshot : snapshot.getChildren()){
                    // get Post
                    Post post = _snapshot.getValue(Post.class);
                    for(String id : followingList){
                        // check id publisher is equal to id in following list
                        if(post.getPostPublisher().equals(id)){
                            Log.d(" ",post.getPostPublisher());
                            ArrayList<Place> _p = new ArrayList<>();
                            for(DataSnapshot s:_snapshot.child("places").getChildren()){
                                Place p = s.getValue(Place.class);
                                ArrayList<String> imgs = new ArrayList<>();
                                for(DataSnapshot _s : s.child("imageList").getChildren()){
                                    String str = _s.getValue(String.class);
                                    Log.d("imaf",str);
                                    imgs.add(str);
                                }
                                p.setImageList(imgs);
                                Log.d("places",p.getAddress());
                                _p.add(p);
                            }

                            post.setPlaceList(_p);
                            postList.add(post);
                            postAdapter.notifyDataSetChanged();
                            break;
                        }
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                System.out.println("The read failed: " + error.getCode());
            }
        });
    }
}
