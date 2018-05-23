package csp15cap.fitstart;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Peter Cassidy on 15/03/2018.
 */

public class HomeFragment extends Fragment {
    private static final String TAG = "HomeFragment";


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home_layout, container, false);
        //set action bar title
        ((MainActivity)getActivity()).getSupportActionBar().setTitle("Home");

        LineChart weightChart = view.findViewById(R.id.c_weight_chart);

        List<Entry> entries = new ArrayList<>();
        entries.add(new Entry(01, 200));
        entries.add(new Entry(02, 199));
        entries.add(new Entry(03, 188));
        entries.add(new Entry(04, 185));
        entries.add(new Entry(05, 185));

        LineDataSet dataset = new LineDataSet(entries, "progress");
        dataset.setColor(R.color.colorPrimaryDark);
        dataset.setValueTextColor(R.color.colorPrimary);
        LineData lineData = new LineData(dataset);
        weightChart.setData(lineData);
        weightChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        weightChart.setVisibleXRange(1,5);
        weightChart.invalidate();
        weightChart.getAxisRight().setEnabled(false);
        weightChart.getAxisLeft().setDrawGridLines(false);
        weightChart.getXAxis().setDrawGridLines(false);
        weightChart.getLegend().setEnabled(false);
        weightChart.getDescription().setEnabled(false);
        weightChart.getXAxis().setGranularity(1f);
        weightChart.setTouchEnabled(false);





        return view;


    }
}
