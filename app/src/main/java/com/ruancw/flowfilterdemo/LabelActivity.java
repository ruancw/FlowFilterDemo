package com.ruancw.flowfilterdemo;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LabelActivity extends AppCompatActivity {

    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.lgl_label)
    LabelGridLayout lglLabel;
    @BindView(R.id.tv_reset)
    TextView tvReset;
    @BindView(R.id.tv_confirm)
    TextView tvConfirm;

    private List<FilterBean> labelLists;
    private boolean isMul;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_label);
        ButterKnife.bind(this);
        isMul=getIntent().getBooleanExtra("isMul",false);
        initData();
        Log.e("rcw","labelLists==="+labelLists.size());
        setViewData();
    }

    private void initData() {
        labelLists=new ArrayList<>();
        List<FilterBean.TableMode> label=new ArrayList<>();
        label.add(new FilterBean.TableMode("全部"));
        label.add(new FilterBean.TableMode("超A"));
        label.add(new FilterBean.TableMode("A类"));
        label.add(new FilterBean.TableMode("B类"));
        label.add(new FilterBean.TableMode("C类"));
        label.add(new FilterBean.TableMode("D类"));
        label.add(new FilterBean.TableMode("E类"));
        labelLists.add(new FilterBean(new FilterBean.TableMode("全部"),label));

        Log.e("rcw","labelLists="+labelLists.size());
        Log.e("rcw","TableMode="+label.size());
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void setViewData() {
        if (isMul){
            tvTitle.setText("无标题的标签(多选)");
            lglLabel.setMulEnable(true);
        }else {
            tvTitle.setText("无标题的标签(单选)");
            lglLabel.setMulEnable(false);
        }
        lglLabel.setColumnCount(3);
        lglLabel.setLabelBg(R.drawable.flow_popup);
        lglLabel.setGridData(labelLists);
    }

    @OnClick({R.id.tv_reset, R.id.tv_confirm})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_confirm:
                Log.e("rcw","label="+lglLabel.getLabelData());
                break;
            case R.id.tv_reset:
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        lglLabel.resetData();
                    }
                });

                break;
        }
    }
}
