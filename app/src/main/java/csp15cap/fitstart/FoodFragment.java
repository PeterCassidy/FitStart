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

public class FoodFragment extends Fragment {
    private Button btnExerciseFrag;
    private Button btnHomeFrag;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_food_layout, container, false);

        btnExerciseFrag = view.findViewById(R.id.btn_exercise);
        btnHomeFrag = view.findViewById(R.id.btn_home);

        btnExerciseFrag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getActivity(), "Exercise fragment...", Toast.LENGTH_SHORT).show();
                //((MainActivity)getActivity()).setViewPager(1);
            }
        });


        btnHomeFrag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getActivity(), "Home fragment...", Toast.LENGTH_SHORT).show();
                //((MainActivity)getActivity()).setViewPager(0);
            }
        });

        return view;


    }
}
