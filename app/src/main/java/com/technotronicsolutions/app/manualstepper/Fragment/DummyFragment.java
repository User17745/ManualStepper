package com.technotronicsolutions.app.manualstepper.Fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.technotronicsolutions.app.manualstepper.MainActivity;
import com.technotronicsolutions.app.manualstepper.R;

/**
 * Created by novat on 6/30/2017.
 */

public class DummyFragment extends Fragment {

    private View view;
    private LinearLayout linearLayout;
    private View dummyView;
    private int[] dummyColors;

    //Collection Handles to used to load product lists
    private String collectionHandles[] = {"Processors", "Motherboards", "RAM", "HDDs", "SSDs",
            "GPUs/Graphics Cards", "Cabinets", "Power Supply/PSU", "Optical Drives", "Monitors",
            "Mouse & Keyboards"};

    private MainActivity mainActivity;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.layout_fragment_dummy, container, false);

        mainActivity = new MainActivity();

        linearLayout = view.findViewById(R.id.dummy_layout);
        dummyView = view.findViewById(R.id.dummy_view);
        dummyColors = view.getContext().getResources().getIntArray(R.array.dummy_color);

        mapper();
        setBackColor();

        return view;
    }

    public static DummyFragment newInstance(int currentStep) {

        Bundle args = new Bundle();
        args.putInt("current_step", currentStep + 1);

        DummyFragment fragment = new DummyFragment();
        fragment.setArguments(args);
        return fragment;
    }

    private void mapper(){
        dummyView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mainActivity.isPartSelected(getArguments().getInt("current_step", 0) - 1)) {
                    mainActivity.setPartSelected(false, getArguments().getInt("current_step", 0) - 1);
                } else {
                    mainActivity.setPartSelected(true, getArguments().getInt("current_step", 0) - 1);
                }
                setBackColor();
            }
        });
    }

    private void setBackColor() {
        if (mainActivity.isPartSelected(getArguments().getInt("current_step", 0) - 1)) {
            linearLayout.setBackgroundColor(dummyColors[11 - getArguments().getInt("current_step", 0)]);

        } else {
            linearLayout.setBackgroundColor(dummyColors[getArguments().getInt("current_step", 0)]);
        }
    }
}