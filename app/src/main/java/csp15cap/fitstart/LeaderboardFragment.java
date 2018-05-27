package csp15cap.fitstart;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class LeaderboardFragment extends Fragment {

    private FirebaseAuth mAuth;
    private DatabaseReference mDbRef;

    private EditText etSearch;
    private Button btnSearch;

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;


    public LeaderboardFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_leaderboard, container, false);
        //set action bar title
        ((MainActivity) getActivity()).getSupportActionBar().setTitle("Find Friends");

        final ArrayList<LeaderboardEntry> resultsArray = new ArrayList<>();
        btnSearch = view.findViewById(R.id.btn_friend_search);
        etSearch = view.findViewById(R.id.et_friend_query);
        mRecyclerView = view.findViewById(R.id.rv_find_friends);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new LeaderboardListAdapter(resultsArray);
        mRecyclerView.setAdapter(mAdapter);

        mAuth = FirebaseAuth.getInstance();
        String CurrentUid = mAuth.getCurrentUser().getUid();
        mDbRef = FirebaseDatabase.getInstance().getReference().child("Users");



        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDbRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if(dataSnapshot.exists()){
                            resultsArray.clear();
                            for (DataSnapshot friendSnapshot : dataSnapshot.getChildren()){
                                LeaderboardEntry ffe = new LeaderboardEntry();

                               System.out.println(friendSnapshot.getKey());
                               ffe.setUniqueId(friendSnapshot.getKey());
                               ffe.setName(friendSnapshot.child("user_name").getValue().toString());
                               if(friendSnapshot.child("profile_image").exists()) {
                               ffe.setProfilePicUrl(friendSnapshot.child("profile_image").getValue().toString());
                               }

                                resultsArray.add(ffe);
                            }
                        mAdapter.notifyDataSetChanged();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

            }
        });




        return view;
    }
}
