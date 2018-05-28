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
    public void onBindViewHolder(@NonNull FindFriendViewHolder holder,int i) {
        FindFriendEntry ffe = mFriendList.get(i);
        if (ffe!=null){
            holder.vPosition.setText(String.valueOf(i+1));
            holder.vName.setText(ffe.getName());
            Picasso.get().load(ffe.getProfilePicUrl()).placeholder(R.drawable.no_profile).into(holder.vProfilePic);
            long exp = ffe.getExp();
            holder.vExp.setText(String.valueOf(exp));
            holder.vLevel.setText(String.valueOf(getLevelFromExperience(exp)));
        }


    }

    @Override
    public int getItemCount() {
        return mFriendList.size();
    }

    public class FindFriendViewHolder extends RecyclerView.ViewHolder{

        protected TextView vPosition, vName, vExp, vLevel;
        protected ImageView vProfilePic;
        public FindFriendViewHolder(View itemView) {
            super(itemView);
            vPosition = itemView.findViewById(R.id.tv_find_friend_position);
            vName = itemView.findViewById(R.id.tv_find_friend_name);
            vProfilePic = itemView.findViewById(R.id.iv_find_friend_pic);
            vExp = itemView.findViewById(R.id.tv_find_friend_exp);
            vLevel= itemView.findViewById(R.id.tv_find_friend_level);

        }
    }

    private int getLevelFromExperience(long exp){
        if(exp<=99){return 1;}
        else if(exp<=299){return 2;}
        else if(exp<=599){return 3;}
        else if(exp<=999){return 4;}
        else if(exp<=1499){return 5;}
        else if(exp<=2099){return 6;}
        else if(exp<=2799){return 7;}
        else if(exp<=3599){return 8;}
        else if(exp<=4499){return 9;}
        else{return 10;}
    }
}
