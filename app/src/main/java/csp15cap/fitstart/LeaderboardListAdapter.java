package csp15cap.fitstart;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class LeaderboardListAdapter extends RecyclerView.Adapter<LeaderboardListAdapter.FindFriendViewHolder> {

    private ArrayList<LeaderboardEntry> mFriendList;


    public LeaderboardListAdapter(ArrayList<LeaderboardEntry> list){
        mFriendList = list;
    }

    @NonNull
    @Override
    public FindFriendViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.leaderboard_entry_layout, viewGroup, false);

        return new FindFriendViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull FindFriendViewHolder holder, int i) {
        LeaderboardEntry ffe = mFriendList.get(i);
        String currentUid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference mDbRef = FirebaseDatabase.getInstance().getReference().child("Friends").child(currentUid);

        if (ffe!=null){
            holder.vName.setText(ffe.getName());
            Picasso.get().load(ffe.getProfilePicUrl()).placeholder(R.drawable.no_profile).into(holder.vProfilePic);
            holder.vAddButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                }
            });
        }


    }

    @Override
    public int getItemCount() {
        return mFriendList.size();
    }

    public class FindFriendViewHolder extends RecyclerView.ViewHolder{

        protected TextView vName;
        protected ImageView vProfilePic, vAddButton;
        public FindFriendViewHolder(View itemView) {
            super(itemView);

            vName = itemView.findViewById(R.id.tv_find_friend_name);
            vProfilePic = itemView.findViewById(R.id.iv_find_friend_pic);
            vAddButton = itemView.findViewById(R.id.iv_add_friend);

        }
    }
}
