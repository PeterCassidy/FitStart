package csp15cap.fitstart;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import com.fatsecret.platform.model.CompactFood;
import com.fatsecret.platform.model.CompactRecipe;
import com.fatsecret.platform.model.Food;
import com.fatsecret.platform.model.Recipe;
import com.fatsecret.platform.services.Response;
import com.fatsecret.platform.services.android.Request;
import com.fatsecret.platform.services.android.ResponseListener;


import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class FatSecretResultsFragment extends Fragment {
    private String mQuery;
    private String selectedDate;

    TextView tvQuery;
    TextView tvDate;

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    public FatSecretResultsFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mQuery = getArguments().getString("query");
            selectedDate = getArguments().getString("selectedDate");
        }


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_fat_secret_results, container, false);
        //set action bar title
        ((MainActivity) getActivity()).getSupportActionBar().setTitle("Search Results");

        tvDate = view.findViewById(R.id.tv_fs_result_date);
        tvQuery = view.findViewById(R.id.tv_fs_result_query);
        tvDate.setText(selectedDate);
        tvQuery.setText(mQuery);



        final ArrayList<CompactFood> resultsArray = new ArrayList<>();

        mRecyclerView = view.findViewById(R.id.rv_fs_result);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new FatsecretResultsListAdapter(resultsArray, selectedDate);
        mRecyclerView.setAdapter(mAdapter);

        //fatsecret inits
        String key = "d5727033ff8341a6a17c79f1de41d530";
        String secret = "f56a610adcca4660a9f1854d57a9b3fe";
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());

        Request req = new Request(key, secret, new ResponseListener() {
            @Override
            public void onFoodListRespone(Response<CompactFood> response) {
                List<CompactFood> foods = response.getResults();
                for(CompactFood food : foods) {
                    if(food.getType().equals("Generic")) {
                        resultsArray.add(food);
                    }
                }
                mAdapter.notifyDataSetChanged();
            }
        });

        req.getFoods(requestQueue,mQuery,0 );










        // Inflate the layout for this fragment
        return view;
    }

}
