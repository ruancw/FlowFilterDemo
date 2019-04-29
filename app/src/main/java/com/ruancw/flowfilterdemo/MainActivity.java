package com.ruancw.flowfilterdemo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.tv_type_label, R.id.tv_label})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_type_label:
                startActivity(new Intent(this,TypeLabelActivity.class));
                break;
            case R.id.tv_label:
                startActivity(new Intent(this,LabelActivity.class));
                break;
        }
    }
}
