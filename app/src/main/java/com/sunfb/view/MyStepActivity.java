package com.sunfb.view;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.sunfb.view.step.MyStepView;

public class MyStepActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step_layout);
        final MyStepView myStepView =findViewById(R.id.step_view);
        myStepView.setMaxStep(4000);
        //属性动画 利用属性动画动态修改当前步数值
        ValueAnimator valueAnimator = ObjectAnimator.ofInt(0,3000);
//        valueAnimator.setRepeatCount(2);
        valueAnimator.setDuration(2000);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener(){

            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
               int currentStepValue = (int) animation.getAnimatedValue();
               myStepView.setCurrentStep(currentStepValue);
            }
        });
        valueAnimator.start();
    }

}