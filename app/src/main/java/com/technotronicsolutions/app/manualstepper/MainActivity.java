package com.technotronicsolutions.app.manualstepper;

import android.graphics.Typeface;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.technotronicsolutions.app.manualstepper.Class.NonSwipeableViewPager;
import com.technotronicsolutions.app.manualstepper.Class.StepperViewAdapter;

public class MainActivity extends AppCompatActivity {

    public static int totalSteps = 11;
    private int errorDuration = 1500; //Milliseconds

    public static NonSwipeableViewPager stepperViewPager;
    private StepperViewAdapter stepperViewAdapter;

    public static Button stepperForwards, stepperBackwards;
    public static ProgressBar stepperProgressBar;
    private int singleStepProgressWeight;

    public static boolean[] isStepOptional;

    //For keeping track of what parts are selected
    public static boolean partSelected[] = new boolean[totalSteps];

    private HorizontalScrollView horizontalScrollView;
    private LinearLayout errorLayout;

    private Animation slideDownAnimation, slideUpAnimation;

    //Step Tab Layouts
    private LinearLayout stepLayout1, stepLayout2, stepLayout3, stepLayout4, stepLayout5;
    private LinearLayout stepLayout6, stepLayout7, stepLayout8, stepLayout9, stepLayout10;
    private LinearLayout stepLayout11;
    private LinearLayout[] stepLayouts;
    //Step Tab Circles
    private View stepCircle1, stepCircle2, stepCircle3, stepCircle4, stepCircle5, stepCircle6;
    private View stepCircle7, stepCircle8, stepCircle9, stepCircle10, stepCircle11;
    private View[] stepCircles;
    //Step Tab Circle Texts
    private TextView txtStepCircle1, txtStepCircle2, txtStepCircle3, txtStepCircle4, txtStepCircle5;
    private TextView txtStepCircle6, txtStepCircle7, txtStepCircle8, txtStepCircle9, txtStepCircle10;
    private TextView txtStepCircle11;
    private TextView[] txtStepCircles;
    //Step Tab Labels
    private TextView txtStepLabel1, txtStepLabel2, txtStepLabel3, txtStepLabel4, txtStepLabel5;
    private TextView txtStepLabel6, txtStepLabel7, txtStepLabel8, txtStepLabel9, txtStepLabel10;
    private TextView txtStepLabel11;
    private TextView[] txtStepLabels;
    //Step Tab Optional Labels
    private TextView txtStepLabelOptional1, txtStepLabelOptional2, txtStepLabelOptional3;
    private TextView txtStepLabelOptional4, txtStepLabelOptional5, txtStepLabelOptional6;
    private TextView txtStepLabelOptional7, txtStepLabelOptional8, txtStepLabelOptional9;
    private TextView txtStepLabelOptional10, txtStepLabelOptional11;
    private TextView[] txtStepLabelOptionals;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Calculating progess weight of each step (to be used in filling the ProgressBar
        singleStepProgressWeight = 100 / totalSteps;

        //Manually arrange all the steps that are optional
        isStepOptional = new boolean[]{
                false, false, false, false, true, true, false, true, true, true, true};

        slideUpAnimation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_up);
        slideDownAnimation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_down);

        mapper();
        stepTabMapper();
        setTxtStepLabelOptionals();

        stepperViewAdapter = new StepperViewAdapter(getSupportFragmentManager());
        stepperViewPager.setAdapter(stepperViewAdapter);

        setAnimations();
        setNavButtonListeners();
        setUiElements();
    }

    public void setPartSelected(boolean selected, int partNumber) {
        partSelected[partNumber] = selected;
        setForwards();
    }

    public void setForwards() {
        if (partSelected[stepperViewPager.getCurrentItem()])
            if (getCurrentStep() == totalSteps)
                stepperForwards.setText(R.string.stepper_btn_forwards_final);
            else
                stepperForwards.setText(R.string.stepper_btn_forwards);
        else if (isStepOptional[stepperViewPager.getCurrentItem()])
            if (getCurrentStep() == totalSteps)
                stepperForwards.setText(R.string.stepper_btn_forwards_optional_final);
            else
                stepperForwards.setText(R.string.stepper_btn_forwards_optional);
    }

    public boolean isPartSelected(int partNumber) {
        return partSelected[partNumber];
    }

    private void mapper() {
        //Maps all the basic UI elements to the XML
        horizontalScrollView = (HorizontalScrollView) findViewById(R.id.stepper_horizontalScrollView);
        errorLayout = (LinearLayout) findViewById(R.id.layout_error);
        stepperProgressBar = (ProgressBar) findViewById(R.id.stepper_progress);
        stepperViewPager = (NonSwipeableViewPager) findViewById(R.id.stepper_viewPager);
        stepperForwards = (Button) findViewById(R.id.stepper_btn_forwards);
        stepperBackwards = (Button) findViewById(R.id.stepper_btn_backwards);
    }

    private void setSteps() {
        for (int i = 0; i < totalSteps; i++) {
            //Passed
            if (i < stepperViewPager.getCurrentItem()) {
                //Layout Click
                stepLayouts[i].setClickable(true);
                //Circle Color
                stepCircles[i].setBackgroundDrawable(getResources().getDrawable(R.drawable.step_circle_passed));
                //Circle Text
                txtStepCircles[i].setTypeface(null, Typeface.NORMAL);
                //Label Text
                txtStepLabels[i].setTextColor(getResources().getColor(R.color.step_label));
                txtStepLabels[i].setTypeface(null, Typeface.NORMAL);
            }
            //Inactive
            else if (i > stepperViewPager.getCurrentItem()) {
                stepLayouts[i].setClickable(false);
                stepCircles[i].setBackgroundDrawable(getResources().getDrawable(R.drawable.step_circle_inactive));
                txtStepCircles[i].setTypeface(null, Typeface.NORMAL);
                txtStepLabels[i].setTextColor(getResources().getColor(R.color.step_label_inactive));
                txtStepLabels[i].setTypeface(null, Typeface.NORMAL);
            }
            //Active
            else {
                stepLayouts[i].setClickable(true);
                stepCircles[i].setBackgroundDrawable(getResources().getDrawable(R.drawable.step_circle_active));
                txtStepCircles[i].setTypeface(null, Typeface.BOLD);
                txtStepLabels[i].setTextColor(getResources().getColor(R.color.step_label));
                txtStepLabels[i].setTypeface(null, Typeface.BOLD);
                //Scroll to the current Step Tab
                horizontalScrollView.smoothScrollTo(stepLayouts[i].getLeft(), stepLayouts[i].getTop());
            }
        }
    }

    private void setTxtStepLabelOptionals() {
        //Puts the optional label under the steps that are optional.

        for (int i = 0; i < totalSteps; i++)
            if (isStepOptional[i])
                txtStepLabelOptionals[i].setVisibility(View.VISIBLE);
            else
                txtStepLabelOptionals[i].setVisibility(View.GONE);
    }

    private void setAnimations() {
        //Sets up animation for error so that the once the error slideUp animation is called,
        //it automatically calls the slideDown animation with a predefined interval so that the user
        //could view the error.

        slideUpAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                stepperForwards.setEnabled(false);
                errorLayout.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        errorLayout.startAnimation(slideDownAnimation);
                    }
                }, errorDuration);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        slideDownAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                errorLayout.setVisibility(View.GONE);
                stepperForwards.setEnabled(true);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    private void setNavButtonListeners() {
        stepperForwards.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //If part is either optional or already selected
                if (isStepOptional[stepperViewPager.getCurrentItem()] ||
                        partSelected[stepperViewPager.getCurrentItem()]) {
                    //If page is not the last page
                    if (getCurrentStep() < totalSteps) {
                        //Move to the next page
                        stepperViewPager.setCurrentItem(stepperViewPager.getCurrentItem() + 1);
                    } else {
                        //All the step are completed, Build the PC now
                    }

                    //Set all the UI elements
                    setUiElements();
                }
                //In case neither the part is optional nor selected
                else {
                    showError();
                }
            }
        });
        stepperBackwards.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (stepperViewPager.getCurrentItem() > 0)
                    stepperViewPager.setCurrentItem(stepperViewPager.getCurrentItem() - 1);
                setUiElements();
            }
        });
    }

    private void setUiElements() {
        setBackwards();
        setForwards();
        setSteps();
        setProgress();
    }

    private void setBackwards() {
        if (stepperViewPager.getCurrentItem() == 0)
            stepperBackwards.setVisibility(View.GONE);
        else
            stepperBackwards.setVisibility(View.VISIBLE);

    }

    private void showError() {
        errorLayout.startAnimation(slideUpAnimation);
    }

    private void setProgress() {
        stepperProgressBar.setProgress(getCurrentStep() * singleStepProgressWeight);
    }

    private int getCurrentStep() {
        return stepperViewPager.getCurrentItem() + 1;
    }

    private void stepTabMapper() {
        //Maps all the elements inside the HorizontalScrollView to the XML
        //and adds them into respective arrays

        //Step Tab Layouts
        stepLayout1 = (LinearLayout) findViewById(R.id.layout_step1);
        stepLayout2 = (LinearLayout) findViewById(R.id.layout_step2);
        stepLayout3 = (LinearLayout) findViewById(R.id.layout_step3);
        stepLayout4 = (LinearLayout) findViewById(R.id.layout_step4);
        stepLayout5 = (LinearLayout) findViewById(R.id.layout_step5);
        stepLayout6 = (LinearLayout) findViewById(R.id.layout_step6);
        stepLayout7 = (LinearLayout) findViewById(R.id.layout_step7);
        stepLayout8 = (LinearLayout) findViewById(R.id.layout_step8);
        stepLayout9 = (LinearLayout) findViewById(R.id.layout_step9);
        stepLayout10 = (LinearLayout) findViewById(R.id.layout_step10);
        stepLayout11 = (LinearLayout) findViewById(R.id.layout_step11);

        //Step Tab Circles
        stepCircle1 = (View) findViewById(R.id.step_circle1);
        stepCircle2 = (View) findViewById(R.id.step_circle2);
        stepCircle3 = (View) findViewById(R.id.step_circle3);
        stepCircle4 = (View) findViewById(R.id.step_circle4);
        stepCircle5 = (View) findViewById(R.id.step_circle5);
        stepCircle6 = (View) findViewById(R.id.step_circle6);
        stepCircle7 = (View) findViewById(R.id.step_circle7);
        stepCircle8 = (View) findViewById(R.id.step_circle8);
        stepCircle9 = (View) findViewById(R.id.step_circle9);
        stepCircle10 = (View) findViewById(R.id.step_circle10);
        stepCircle11 = (View) findViewById(R.id.step_circle11);

        //Step Tab Circle Texts
        txtStepCircle1 = (TextView) findViewById(R.id.txt_step_circle1);
        txtStepCircle2 = (TextView) findViewById(R.id.txt_step_circle2);
        txtStepCircle3 = (TextView) findViewById(R.id.txt_step_circle3);
        txtStepCircle4 = (TextView) findViewById(R.id.txt_step_circle4);
        txtStepCircle5 = (TextView) findViewById(R.id.txt_step_circle5);
        txtStepCircle6 = (TextView) findViewById(R.id.txt_step_circle6);
        txtStepCircle7 = (TextView) findViewById(R.id.txt_step_circle7);
        txtStepCircle8 = (TextView) findViewById(R.id.txt_step_circle8);
        txtStepCircle9 = (TextView) findViewById(R.id.txt_step_circle9);
        txtStepCircle10 = (TextView) findViewById(R.id.txt_step_circle10);
        txtStepCircle11 = (TextView) findViewById(R.id.txt_step_circle11);

        //Step Tab Labels
        txtStepLabel1 = (TextView) findViewById(R.id.step_txt_label1);
        txtStepLabel2 = (TextView) findViewById(R.id.step_txt_label2);
        txtStepLabel3 = (TextView) findViewById(R.id.step_txt_label3);
        txtStepLabel4 = (TextView) findViewById(R.id.step_txt_label4);
        txtStepLabel5 = (TextView) findViewById(R.id.step_txt_label5);
        txtStepLabel6 = (TextView) findViewById(R.id.step_txt_label6);
        txtStepLabel7 = (TextView) findViewById(R.id.step_txt_label7);
        txtStepLabel8 = (TextView) findViewById(R.id.step_txt_label8);
        txtStepLabel9 = (TextView) findViewById(R.id.step_txt_label9);
        txtStepLabel10 = (TextView) findViewById(R.id.step_txt_label10);
        txtStepLabel11 = (TextView) findViewById(R.id.step_txt_label11);

        //Step Tab Optional Labels
        txtStepLabelOptional1 = (TextView) findViewById(R.id.step_txt_label_optional1);
        txtStepLabelOptional2 = (TextView) findViewById(R.id.step_txt_label_optional2);
        txtStepLabelOptional3 = (TextView) findViewById(R.id.step_txt_label_optional3);
        txtStepLabelOptional4 = (TextView) findViewById(R.id.step_txt_label_optional4);
        txtStepLabelOptional5 = (TextView) findViewById(R.id.step_txt_label_optional5);
        txtStepLabelOptional6 = (TextView) findViewById(R.id.step_txt_label_optional6);
        txtStepLabelOptional7 = (TextView) findViewById(R.id.step_txt_label_optional7);
        txtStepLabelOptional8 = (TextView) findViewById(R.id.step_txt_label_optional8);
        txtStepLabelOptional9 = (TextView) findViewById(R.id.step_txt_label_optional9);
        txtStepLabelOptional10 = (TextView) findViewById(R.id.step_txt_label_optional10);
        txtStepLabelOptional11 = (TextView) findViewById(R.id.step_txt_label_optional11);

        stepLayouts = new LinearLayout[]{stepLayout1, stepLayout2, stepLayout3, stepLayout4,
                stepLayout5, stepLayout6, stepLayout7, stepLayout8, stepLayout9, stepLayout10,
                stepLayout11};
        stepCircles = new View[]{stepCircle1, stepCircle2, stepCircle3, stepCircle4, stepCircle5,
                stepCircle6, stepCircle7, stepCircle8, stepCircle9, stepCircle10, stepCircle11};
        txtStepCircles = new TextView[]{txtStepCircle1, txtStepCircle2, txtStepCircle3,
                txtStepCircle4, txtStepCircle5, txtStepCircle6, txtStepCircle7, txtStepCircle8,
                txtStepCircle9, txtStepCircle10, txtStepCircle11};
        txtStepLabels = new TextView[]{txtStepLabel1, txtStepLabel2, txtStepLabel3, txtStepLabel4, txtStepLabel5, txtStepLabel6,
                txtStepLabel7, txtStepLabel8, txtStepLabel9, txtStepLabel10, txtStepLabel11};
        txtStepLabelOptionals = new TextView[]{txtStepLabelOptional1, txtStepLabelOptional2,
                txtStepLabelOptional3, txtStepLabelOptional4, txtStepLabelOptional5, txtStepLabelOptional6,
                txtStepLabelOptional7, txtStepLabelOptional8, txtStepLabelOptional9, txtStepLabelOptional10,
                txtStepLabelOptional11};
    }
}