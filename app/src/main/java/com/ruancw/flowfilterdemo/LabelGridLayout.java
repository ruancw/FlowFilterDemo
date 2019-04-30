package com.ruancw.flowfilterdemo;


import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.GridLayout;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * create by ruancw on 2019/4/29
 * description
 */
public class LabelGridLayout extends GridLayout {

    private Context context;

    private int columnCount;//列数
    private int row=-1;//行数
    private int tabTextSize = 16;
    //tab标签字体颜色
    private int labelTextColor = R.color.color_popup;
    //tab标签背景颜色
    private int labelBg = R.drawable.shape_circle_bg;
    //选择的筛选数据
    private List<String> selectLabel=new ArrayList<>();
    private List<TextView> selectTvLabel=new ArrayList<>();
    //记录上次选中的状态集合
    private List<FilterBean.TableMode> selectTab=new ArrayList<>();
    //是否开启多选
    private boolean mulEnable=false;

    public LabelGridLayout(Context context) {
        super(context);
        this.context=context;
    }

    public LabelGridLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context=context;
    }

    public LabelGridLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context=context;
    }

    /**
     * 将数据设置给GridLayout
     */
    @SuppressLint("RtlHardcoded")
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void setGridData(List<FilterBean> listData) {
        //设置列数
        setColumnCount(columnCount);
        //设置行数
        setRowCount(getRowCount(listData));
        //将数据源设置到每个标签
        for (int i = 0; i < listData.size(); i++){
            //行数++
            ++row;
            //显示每个条目类型的控件
            //配置列 第一个参数是起始列标 第二个参数是占几列，so -> 总列数
            GridLayout.Spec columnSpec = spec(0,columnCount);
            //配置行 第一个参数是起始行标  起始行+起始列就是一个确定的位置
            GridLayout.Spec rowSpec = spec(row);
            //将Spec传入GridLayout.LayoutParams并设置宽高为0或者WRAP_CONTENT，必须设置宽高，否则视图异常
            GridLayout.LayoutParams layoutParams = new GridLayout.LayoutParams(rowSpec, columnSpec);
            //设置宽度
            layoutParams.width = GridLayout.LayoutParams.WRAP_CONTENT;
            //设置高度
            layoutParams.height = GridLayout.LayoutParams.WRAP_CONTENT;
            //设置位置
            layoutParams.setGravity(Gravity.LEFT | Gravity.CENTER_VERTICAL);
            //设置间距
            layoutParams.bottomMargin = context.getResources().getDimensionPixelSize(R.dimen.dp_8);
            //根据mulEnable属性值添加tab标签
            if (mulEnable){
                //多选
                mulAddTabs(listData.get(i));
            }else {
                //单选
                singleAddTabs(listData.get(i));
            }

        }
    }

    /**
     * 添加tab多选标签
     * @param model 数据bean
     */
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void mulAddTabs(final FilterBean model){
        List<FilterBean.TableMode> tabs = model.getTabs();
        for (int i = 0; i < tabs.size(); i++){
            //判断是否增加行数
            if (i % columnCount == 0){
                row ++;
            }
            final FilterBean.TableMode tab = tabs.get(i);
            //显示标签的控件TextView
            final TextView label = new TextView(context);
            //设置标签属性
            setLabel(i, tab, label);
            //记录上次选中状态
            Log.e("rcw","tabs.get(i)="+tabs.get(i));
            if (model.getLabels()!=null){
                //存储上次选中的集合
                for (int j=0;j<model.getLabels().size();j++){
                    if (tab == model.getLabels().get(j)){
                        label.setSelected(true);
                        //解决不点击label情况下记录上次选中的状态及参数
                        selectTab.add(tab);
                        selectLabel.add(tab.name);
                        //记录选中的标签用于重置数据
                        selectTvLabel.add(label);
                    }
                }
            }

            //标签的点击事件
            label.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //控制标签的选择与取消
                    if (label.isSelected()){
                        label.setSelected(false);
                        selectLabel.remove(label.getText().toString());
                        Log.e("rcw","selectLabel="+selectLabel);
                        selectTab.remove(tab);
                        Log.e("rcw","selectTab="+selectTab.size());
                        Log.e("rcw","selectTab="+selectTab);
                        selectTvLabel.remove(label);
                    }else {
                        label.setSelected(true);
                        selectLabel.add(label.getText().toString());
                        Log.e("rcw","selectLabel="+selectLabel);
                        selectTab.add(tab);
                        Log.e("rcw","selectTab="+selectTab.size());
                        Log.e("rcw","selectTab="+selectTab);
                        selectTvLabel.add(label);
                    }
                    //记录选中的label
                    model.setLabels(selectTab);
                }
            });
        }
        Log.e("rcw","selectLabel="+selectLabel);
        //不走点击事件，重新设置上次的记录
        model.setLabels(selectTab);
    }

    /**
     * 设置标签属性
     * @param tabIndex 标签的位置
     * @param tab 标签的tab
     * @param label 标签控件
     */
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void setLabel(int tabIndex, FilterBean.TableMode tab, TextView label) {
        label.setTextSize(tabTextSize);
        label.setTextColor(context.getResources().getColorStateList(labelTextColor));
        label.setBackgroundDrawable(context.getResources().getDrawable(labelBg));
        label.setSingleLine(true);
        label.setGravity(Gravity.CENTER);
        label.setEllipsize(TextUtils.TruncateAt.MIDDLE);
        //上下padding值
        int paddingT = context.getResources().getDimensionPixelSize(R.dimen.dp_8);
        //左右padding值
        int paddingL = context.getResources().getDimensionPixelSize(R.dimen.dp_8);
        label.setPadding(paddingL,paddingT,paddingL,paddingT);
        //getItemLayoutParams用于设置label标签的参数
        addView(label,getItemLayoutParams(tabIndex,row));
        label.setText(tab.name);
    }

    /**
     * 添加tab单选标签
     * @param model 数据bean
     */
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void singleAddTabs(final FilterBean model){
        List<FilterBean.TableMode> tabs = model.getTabs();
        for (int i = 0; i < tabs.size(); i++){
            //行数
            if (i % columnCount == 0){
                row ++;
            }
            final FilterBean.TableMode tab = tabs.get(i);
            //显示标签的控件
            final TextView label = new TextView(context);
            //设置标签属性
            setLabel(i, tab, label);
            //记录上次选中状态
            if (tabs.get(i) == model.getTab()){
                label.setSelected(true);
            }
            //标签的点击事件
            label.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (tab != model.getTab()){
                        //清除上次选中的状态
                        getChildAt(getIndex(model)).setSelected(false);
                        //设置当前点击选中的tab值
                        model.setTab(tab);
                        label.setSelected(true);
                        selectTvLabel.add(label);
                        String labelText=label.getText().toString();
                        //清楚集合中的数据再添加
                        selectLabel.clear();
                        selectLabel.add(labelText);
                        Log.e("rcw","labelText--->"+labelText);
                    }
                }
            });
        }
    }

    /**
     * 获取当前选中标签在整个GridLayout的索引
     * @return 标签下标
     */
    private int getIndex(FilterBean model){
        int index = 0;
        //加上当前 title下的索引
        FilterBean.TableMode tableModel = model.getTab();
        if (!(model.getTabs().indexOf(tableModel)==-1))
            index += model.getTabs().indexOf(tableModel);
        return index;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private GridLayout.LayoutParams getItemLayoutParams(int i, int row){
        //使用Spec定义子控件的位置和比重
        GridLayout.Spec rowSpec = GridLayout.spec(row,1f);
        GridLayout.Spec columnSpec = GridLayout.spec(i%columnCount,1f);
        //将Spec传入GridLayout.LayoutParams并设置宽高为0，必须设置宽高，否则视图异常
        GridLayout.LayoutParams lp = new GridLayout.LayoutParams(rowSpec, columnSpec);
        lp.width = 0;
        lp.height = GridLayout.LayoutParams.WRAP_CONTENT;
        lp.bottomMargin = context.getResources().getDimensionPixelSize(R.dimen.dp_12);
        lp.topMargin = context.getResources().getDimensionPixelSize(R.dimen.dp_12);
        if(i % columnCount == 0) {//最左边
            lp.leftMargin = context.getResources().getDimensionPixelSize(R.dimen.dp_10);
            lp.rightMargin = context.getResources().getDimensionPixelSize(R.dimen.dp_20);
        }else if((i + 1) % columnCount == 0){//最右边
            lp.rightMargin = context.getResources().getDimensionPixelSize(R.dimen.dp_10);
        }else {//中间
            lp.rightMargin = context.getResources().getDimensionPixelSize(R.dimen.dp_20);
        }
        return lp;
    }

    /**
     * tab标签字体大小
     * @param tabTextSize 标签字体大小
     */
    public void setLabelSize(int tabTextSize) {
        this.tabTextSize = tabTextSize;
    }

    /**
     * tab标签字体颜色
     * @param labelTextColor 颜色
     */
    public void setLabelColor(int labelTextColor) {
        this.labelTextColor = labelTextColor;
    }

    /**
     * 设置标签的背景色
     * @param labelBg 背景色（drawable）
     */
    public void setLabelBg(int labelBg) {
        this.labelBg = labelBg;
    }

    /**
     * 获取内容行数
     * @return 行数
     */
    public int getRowCount(List<FilterBean> listData){
        int row = 0;
        for (FilterBean model : listData){
            //计算当前类型之前的元素所占的个数 标题栏也算一行
            row ++;
            int size = model.getTabs().size();
            row += (size / columnCount) + (size % columnCount > 0 ? 1 : 0) ;
        }
        return row;
    }

    /**
     * 设置gridLayout的列数
     * @param columnCount 列数
     */
    public void setColumnCount(int columnCount){
        this.columnCount = columnCount;
    }

    /**
     * 设置标签是否可以多选
     * @param mulEnable 是否可以多选标签
     */
    public void setMulEnable(boolean mulEnable){
        this.mulEnable = mulEnable;
    }

    /**
     * 获取筛选的数据
     */
    public List<String> getLabelData(){
        return selectLabel;
    }

    /**
     * 重置数据
     */
    public void resetData(){
        for (int i=0;i<selectTvLabel.size();i++){
            selectTvLabel.get(i).setSelected(false);
        }
        selectTab.clear();
        selectLabel.clear();
    }
}
