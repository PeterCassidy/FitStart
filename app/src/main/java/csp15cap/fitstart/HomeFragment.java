package csp15cap.fitstart;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;


/**
 * Created by Peter Cassidy on 15/03/2018.
 */

public class HomeFragment extends Fragment {
    private static final String TAG = "HomeFragment";
    private Button btnExerciseFrag;
    private Button btnFoodFrag;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home_layout, container, false);

        btnExerciseFrag = view.findViewById(R.id.btn_exercise);
        btnFoodFrag = view.findViewById(R.id.btn_food);
        Log.d(TAG, "started");

        btnExerciseFrag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getActivity(), "Exercise fragment...", Toast.LENGTH_SHORT).show();
                //((MainActivity)getActivity()).setViewPager(1);
            }
        });


        btnFoodFrag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getActivity(), "Food fragment...", Toast.LENGTH_SHORT).show();
                //((MainActivity)getActivity()).setViewPager(2);
            }
        });

        return view;


    }
}
