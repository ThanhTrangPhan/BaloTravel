package com.example.balotravel.Fragment;

import static android.content.Context.MODE_PRIVATE;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.balotravel.Adapter.PostAdapter;
import com.example.balotravel.CreateNewJouneyActivity;
import com.example.balotravel.Model.Place;
import com.example.balotravel.Model.Post;
import com.example.balotravel.PlaceListOnMapActivity;
import com.example.balotravel.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class PostDetailFragment extends Fragment {

    String postid;
    private String urlFirebase = "https://balotravel-9a424-default-rtdb.asia-southeast1.firebasedatabase.app/";
    private RecyclerView recyclerView;
    private TextView onMap;
    private PostAdapter postAdapter;
    private List<Post> postList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_post_detail, container, false);

        SharedPreferences prefs = getContext().getSharedPreferences("PREFS", MODE_PRIVATE);
        postid = prefs.getString("postid", "none");
        onMap = view.findViewById(R.id.onMap);
        recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this.getContext());
        recyclerView.setLayoutManager(mLayoutManager);

        postList = new ArrayList<>();
        postAdapter = new PostAdapter(this.getContext(), postList);
        recyclerView.setAdapter(postAdapter);

        readPost();
        onMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (postList.size() > 0) {
                    Intent intent = new Intent(getActivity(), PlaceListOnMapActivity.class);
                    intent.putExtra("BUNDLE", postList.get(0).getPlaceList());
                    startActivity(intent);
                } else {
                    Toast.makeText(getActivity().getApplicationContext(), "Lỗi: Không có địa điểm nào", Toast.LENGTH_SHORT).show();
                }
            }
        });
        return view;
    }

    private void readPost(){
        DatabaseReference reference = FirebaseDatabase.getInstance(urlFirebase).getReference("posts").child(postid);

        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                postList.clear();
                Post post = dataSnapshot.getValue(Post.class);
                post.setPostId(dataSnapshot.getKey());
                ArrayList<Place> _p = new ArrayList<>();
                for(DataSnapshot s:dataSnapshot.child("places").getChildren()){
                    Place p = s.getValue(Place.class);
                    p.setLongitude(s.child("longitute").getValue(Double.class));
                    Log.d("places",p.getLatitude()+" "+p.getLongitude()+" "+p.getAddress());
                    ArrayList<String> imgs = new ArrayList<>();
                    for(DataSnapshot _s : s.child("imageList").getChildren()){
                        String str = _s.getValue(String.class);
                        imgs.add(str);
                    }
                    p.setImageList(imgs);
                    _p.add(p);

                }
                post.setPlaceList(_p);
                postList.add(post);
                postAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}