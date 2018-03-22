package csp15cap.fitstart;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

/**
 * Created by Peter Cassidy on 15/03/2018.
 */

public class ExerciseFragment extends Fragment {
    private Button btnHomeFrag;
    private Button btnFoodFrag;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_exercise_layout, container, false);

        //set action bar title
        ((MainActivity)getActivity()).getSupportActionBar().setTitle("Exercise");

        btnHomeFrag = view.findViewById(R.id.btn_home);
        btnFoodFrag = view.findViewById(R.id.btn_food);

        btnHomeFrag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getActivity(), "Home fragment...", Toast.LENGTH_SHORT).show();
               // ((MainActivity)getActivity()).setViewPager(0);
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
