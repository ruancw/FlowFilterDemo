package com.ruancw.flowfilterdemo;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
    }



    @OnClick({R.id.tv_type_label, R.id.tv_label,R.id.tv_type_mul_label, R.id.tv_mul_label})
    public void onViewClicked(View view) {
        Intent turnIntent = new Intent();
        switch (view.getId()) {
            case R.id.tv_type_label:
                turnIntent.setClass(this,TypeLabelActivity.class);
                skipActivity(turnIntent,false);
                break;
            case R.id.tv_label:
                turnIntent.setClass(this,LabelActivity.class);
                skipActivity(turnIntent,false);
                break;
            case R.id.tv_type_mul_label:
                turnIntent.setClass(this,TypeLabelActivity.class);
                skipActivity(turnIntent,true);
                break;
            case R.id.tv_mul_label:
                turnIntent.setClass(this,LabelActivity.class);
                skipActivity(turnIntent,true);
                break;
        }
    }

    private void skipActivity(Intent intent,boolean isMul){
        intent.putExtra("isMul",isMul);
        startActivity(intent);
    }
}
