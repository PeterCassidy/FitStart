package csp15cap.fitstart;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class FindFriendListAdapter extends RecyclerView.Adapter<FindFriendListAdapter.FindFriendViewHolder> {

    private ArrayList<FindFriendEntry> mFriendList;

    public FindFriendListAdapter(ArrayList<FindFriendEntry> list){
        mFriendList = list;
    }

    @NonNull
    @Override
    public FindFriendViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.find_friend_entry_layout, viewGroup, false);

        return new FindFriendViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull FindFriendViewHolder holder, int i) {
        FindFriendEntry ffe = mFriendList.get(i);
        if (ffe!=null){
            holder.vName.setText(ffe.getName());
            Picasso.get().load(ffe.getProfilePicUrl()).placeholder(R.drawable.no_profile).into(holder.vProfilePic);
        }


    }

    @Override
    public int getItemCount() {
        return mFriendList.size();
    }

    public class FindFriendViewHolder extends RecyclerView.ViewHolder{

        protected TextView vName;
        protected ImageView vProfilePic;
        public FindFriendViewHolder(View itemView) {
            super(itemView);

            vName = itemView.findViewById(R.id.tv_find_friend_name);
            vProfilePic = itemView.findViewById(R.id.iv_find_friend_pic);

        }
    }
}
