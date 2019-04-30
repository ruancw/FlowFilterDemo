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

public class TypeLabelActivity extends AppCompatActivity {
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.lgl_label)
    TypeLabelGridLayout lglLabel;
    @BindView(R.id.tv_reset)
    TextView tvReset;
    @BindView(R.id.tv_confirm)
    TextView tvConfirm;
    private List<FilterBean> typeLabelLists;
    private boolean isMul;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_type_label);
        ButterKnife.bind(this);
        isMul=getIntent().getBooleanExtra("isMul",false);
        initData();
        setViewData();
    }

    private void initData() {

        //流式布局数据
        typeLabelLists = new ArrayList<>();
        List<FilterBean.TableMode> list = new ArrayList<>();
        list.add(new FilterBean.TableMode("不限"));
        list.add(new FilterBean.TableMode("已开通"));
        list.add(new FilterBean.TableMode("未开通"));
        typeLabelLists.add(new FilterBean("业务状态", new FilterBean.TableMode("不限"), list));
        List<FilterBean.TableMode> list1 = new ArrayList<>();
        list1.add(new FilterBean.TableMode("不限"));
        list1.add(new FilterBean.TableMode("已打印"));
        list1.add(new FilterBean.TableMode("未打印"));
        typeLabelLists.add(new FilterBean("打印状态", new FilterBean.TableMode("不限"), list1));
        List<FilterBean.TableMode> list2 = new ArrayList<>();
        list2.add(new FilterBean.TableMode("不限"));
        list2.add(new FilterBean.TableMode("未知"));
        list2.add(new FilterBean.TableMode("制卡中"));
        list2.add(new FilterBean.TableMode("已完成"));
        typeLabelLists.add(new FilterBean("制卡状态", new FilterBean.TableMode("不限"), list2));
        List<FilterBean.TableMode> list3 = new ArrayList<>();
        list3.add(new FilterBean.TableMode("不限"));
        list3.add(new FilterBean.TableMode("未知"));
        list3.add(new FilterBean.TableMode("已订购"));
        list3.add(new FilterBean.TableMode("未订购"));
        typeLabelLists.add(new FilterBean("订购状态", new FilterBean.TableMode("不限"), list3));
        List<FilterBean.TableMode> list4 = new ArrayList<>();
        list4.add(new FilterBean.TableMode("不限"));
        list4.add(new FilterBean.TableMode("是"));
        list4.add(new FilterBean.TableMode("否"));
        typeLabelLists.add(new FilterBean("黑名单", new FilterBean.TableMode("不限"), list4));
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void setViewData() {
        if (isMul){
            tvTitle.setText("带标题的标签(多选)");
            lglLabel.setMulEnable(true);
        }else {
            tvTitle.setText("带标题的标签(单选)");
            lglLabel.setMulEnable(false);
        }
        lglLabel.setColumnCount(3);
        lglLabel.setLabelBg(R.drawable.flow_popup);
        lglLabel.setGridData(typeLabelLists);
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
