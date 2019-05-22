package com.ruancw.flowfilterdemo;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
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
public class TypeLabelGridLayout extends GridLayout {

    private Context context;
    //默认的标题和标签的大小（sp）
    private int titleTextSize = 16;
    private int tabTextSize = 16;
    //标题字体颜色
    private int titleTextColor = Color.parseColor("#333333");
    private int lineColor = Color.parseColor("#F5F5F5");
    //tab标签字体颜色
    private int labelTextColor = R.color.color_popup;
    //tab标签背景颜色
    private int labelBg = R.drawable.flow_popup;
    //当前加载的行数
    private int row = -1;
    private int columnCount;
    private List<String> labelLists=new ArrayList<>();
    //记录上次选中的状态集合
    private List<FilterBean.TableMode> selectTab=new ArrayList<>();
    private List<TextView> selectLabel=new ArrayList<>();
    private List<FilterBean> listData;
    private boolean mulEnable=false;
    private boolean defaultFirst=false;

    public TypeLabelGridLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.context=context;
    }

    public TypeLabelGridLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context=context;
    }

    public TypeLabelGridLayout(Context context) {
        super(context);
        this.context=context;
    }

    /**
     * 将数据设置给GridLayout
     */
    @SuppressLint("RtlHardcoded")
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void setGridData(List<FilterBean> listData) {
        this.listData=listData;
        //设置列数
        setColumnCount(columnCount);
        //设置行数
        setRowCount(getTabRowCount());
        //GridLayout属性参数设置
        for (int i = 0; i < listData.size(); i++){
            //行数++
            ++row;
            //显示每个条目类型的控件
            TextView tvType = new TextView(context);
            tvType.setText(listData.get(i).getTypeName());
            tvType.setTextColor(titleTextColor);
            tvType.setTextSize(titleTextSize);
            //配置列 第一个参数是起始列标 第二个参数是占几列 title（筛选类型）应该占满整行，so -> 总列数
            GridLayout.Spec columnSpec = GridLayout.spec(0,columnCount);
            //配置行 第一个参数是起始行标  起始行+起始列就是一个确定的位置
            GridLayout.Spec rowSpec = GridLayout.spec(row);
            //将Spec传入GridLayout.LayoutParams并设置宽高为0或者WRAP_CONTENT，必须设置宽高，否则视图异常
            GridLayout.LayoutParams layoutParams = new GridLayout.LayoutParams(rowSpec, columnSpec);
            layoutParams.width = GridLayout.LayoutParams.WRAP_CONTENT;
            layoutParams.height = GridLayout.LayoutParams.WRAP_CONTENT;
            layoutParams.setGravity(Gravity.LEFT | Gravity.CENTER_VERTICAL);
            layoutParams.bottomMargin = context.getResources().getDimensionPixelSize(R.dimen.dp_8);
            addView(tvType,layoutParams);
            //添加分割线
            View view=new View(context);
            GridLayout.LayoutParams layoutParams1 = new GridLayout.LayoutParams(rowSpec, columnSpec);
            layoutParams1.width = GridLayout.LayoutParams.WRAP_CONTENT;
            layoutParams1.height = context.getResources().getDimensionPixelSize(R.dimen.dp_1);
            layoutParams1.setGravity(Gravity.CENTER| Gravity.BOTTOM);
            view.setBackgroundColor(lineColor);
            addView(view,layoutParams1);
            //添加tab标签
            if (mulEnable){
                mulAddTabs(listData.get(i));
            }else {
                addSingleTabs(listData.get(i),i);
            }

        }
    }

    /**
     * 添加tab多选标签
     * @param model 数据bean
     */
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void mulAddTabs(final FilterBean model){
        boolean isSelect=false;
        List<FilterBean.TableMode> tabs = model.getTabs();
        for (int i = 0; i < tabs.size(); i++){
            if (i % columnCount == 0){
                row ++;
            }
            final FilterBean.TableMode tab = tabs.get(i);
            //显示标签的控件
            final TextView label = new TextView(context);
            //是否默认选中第一项
            isSelect = setDefaultFirst(model, isSelect, tabs, i, tab, label);

            setLabel(i, tab, label);
            //记录上次选中状态
            if (model.getLabels()!=null){
                //存储上次选中的集合
                for (int j=0;j<model.getLabels().size();j++){
                    if (tab == model.getLabels().get(j)){
                        label.setSelected(true);
                        //解决不点击label情况下记录上次选中的状态及参数
                        selectTab.add(tab);
                        labelLists.add(model.getTypeName()+"-"+tab.name);
                        selectLabel.add(label);
                    }
                }
            }
            //标签的点击事件
            label.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (label.isSelected()){
                        label.setSelected(false);
                        labelLists.remove(model.getTypeName()+"-"+label.getText().toString());
                        Log.e("rcw","labelLists="+labelLists);
                        selectTab.remove(tab);
                        Log.e("rcw","selectTab="+selectTab.size());
                        Log.e("rcw","selectTab="+selectTab);
                        selectLabel.remove(label);
                    }else {
                        label.setSelected(true);
                        labelLists.add(model.getTypeName()+"-"+label.getText().toString());
                        Log.e("rcw","labelLists="+labelLists);
                        selectTab.add(tab);
                        Log.e("rcw","selectTab="+selectTab.size());
                        Log.e("rcw","selectTab="+selectTab);
                        selectLabel.add(label);
                    }
                    //记录选中的label
                    model.setLabels(selectTab);
                }
            });
        }
        //记录选中的label
        model.setLabels(selectTab);
    }

    /**
     * 设置是否默认选中
     * @param model model
     * @param isSelect boolean
     * @param tabs tabs
     * @param i 下标
     * @param tab tab
     * @param label 标签
     * @return boolean
     */
    private boolean setDefaultFirst(FilterBean model, boolean isSelect, List<FilterBean.TableMode> tabs, int i, FilterBean.TableMode tab, TextView label) {
        //循环判断是否有选中的标签
        for (int j = 0; j < tabs.size(); j++){
            if (tabs.get(j) == model.getTab()){
                isSelect=true;
            }
        }
        //没有选中的标签则，设置默认选中第一个
        if (i==0&&!isSelect&&defaultFirst) {
            //每个tab的第一个设置为选中
            label.setSelected(true);
            //记录选中的tab值
            model.setTab(tab);
        }
        return isSelect;
    }

    /**
     * 设置标签的属性
     * @param tabIndex 标签的位置
     * @param tab 标签数据
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
     * 添加tab标签
     * @param model 数据bean
     * @param labelIndex 标签的标号
     */
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void addSingleTabs(final FilterBean model, final int labelIndex){
        boolean isSelect=false;//判断是否有选中的标签
        List<FilterBean.TableMode> tabs = model.getTabs();
        for (int i = 0; i < tabs.size(); i++){
            if (i % columnCount == 0){
                row ++;
            }
            final FilterBean.TableMode tab = tabs.get(i);
            //显示标签的控件
            final TextView label = new TextView(context);
            //循环判断是否有选中的标签
            isSelect = setDefaultFirst(model, isSelect, tabs, i, tab, label);
            //设置标签属性
            setLabel(i, tab, label);
            //记录上次选中状态
            if (tabs.get(i) == model.getTab()){
                label.setSelected(true);
                //将上次记录的标签添加到集合
                labelLists.add(model.getTypeName()+"-"+tab.name);
            }
            //标签的点击事件
            label.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //if (tab != model.getTab()){
                        Log.e("rcw","index--->"+getIndex(model,labelIndex));
                        //清空上次选中的状态
                        getChildAt(getIndex(model,labelIndex)).setSelected(false);
                        //设置当前点击选中的tab值
                        model.setTab(tab);
                        label.setSelected(true);
                        selectLabel.add(label);
                        String labelText=label.getText().toString();
                        //解决tab未被点击时 ，不添加默认的“不限”数据到集合中
                        int flag=-1;//用于记录需要替换的位置
                        //默认选中
                        for (int i=0;i<labelLists.size();i++){
                            String tvDes=labelLists.get(i);
                            //判断当前集合中是否包含TypeName
                            if (tvDes.contains(model.getTypeName())){
                                flag=i;
                            }
                        }
                        if (flag!=-1){
                            //先删除返回数据集合中的之前选中的
                            labelLists.remove(flag);
                            //添加当前选中的数据到集合
                            labelLists.add(flag,model.getTypeName()+"-"+labelText);
                        }
                        //记录选中状态下
                        recordStatus(labelText, flag, model);
                        //labelLists.add(model.getTypeName()+"-"+labelText);
                        Log.e("rcw","labelText--->"+model.getTypeName()+"-"+labelText);
                    }
                //}
            });
        }
    }

    /**
     * 记录数据
     * @param labelText 标签上的文字
     * @param flag 标识
     * @param model 数据模型
     */
    private void recordStatus(String labelText, int flag, FilterBean model) {
        if (labelLists.size()!=0){
            for (int i=0;i<labelLists.size();i++){
                String tvDes=labelLists.get(i);
                //判断当前集合中是否包含TypeName
                if (tvDes.contains(model.getTypeName())){
                    flag=i;
                    break;//匹配成功，跳出循环
                }else {
                    flag=-1;
                }
            }
            if (flag!=-1){
                //先删除返回数据集合中的之前选中的
                labelLists.remove(flag);
                //添加当前选中的数据到集合
                labelLists.add(flag,model.getTypeName()+"-"+labelText);
            }else {
                labelLists.add(model.getTypeName()+"-"+labelText);
            }
        }else {
            labelLists.add(model.getTypeName()+"-"+labelText);
        }
    }

    /**
     * 设置GridLayout的条目属性
     * @param i 位置
     * @param row 行数
     * @return GridLayout.LayoutParams
     */
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
     * 获取当前选中标签在整个GridLayout的索引
     * @return 标签下标
     */
    private int getIndex(FilterBean model, int labelIndex){
        int index = 0;
        for (int i = 0; i < labelIndex; i++){
            //计算当前类型之前的元素所占的个数 title算一个,分割线也算一个
            index += listData.get(i).getTabs().size() + 2;
        }
        //加上当前 title下的索引
        FilterBean.TableMode tableModel = model.getTab();
        index += model.getTabs().indexOf(tableModel) + 2;
        return index;
    }

    /**
     * 获取内容行数
     * @return 行数
     */
    public int getTabRowCount(){
        int row = 0;
        for (FilterBean model : listData){
            //计算当前类型之前的元素所占的个数 标题栏也算一行
            row ++;
            int size = model.getTabs().size();
            row += (size / columnCount) + (size % columnCount > 0 ? 1 : 0) ;
        }
        //+3是分割线也算一行2018/8/31
        return row+3;
    }


    /**
     * 设置gridLayout的列数
     * @param columnCount 列数
     */
    public void setColumnCount(int columnCount){
        this.columnCount = columnCount;
    }

    /**
     * 标题字体大小
     * @param titleTextSize 字体大小
     */
    public void setTitleSize(int titleTextSize) {
        this.titleTextSize = titleTextSize;
    }

    /**
     * tab标签字体大小
     * @param tabTextSize 标签字体大小
     */
    public void setLabelSize(int tabTextSize) {
        this.tabTextSize = tabTextSize;
    }

    /**
     * 标题字体颜色
     * @param titleTextColor 颜色
     */
    public void setTitleColor(int titleTextColor) {
        this.titleTextColor = titleTextColor;
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
     * 设置标签是否可以多选
     * @param mulEnable 是否可以多选标签
     */
    public void setMulEnable(boolean mulEnable){
        this.mulEnable = mulEnable;
    }

    /**
     * 设置标签是否默认选中首项
     * @param defaultFirst 是否默认选中首项
     */
    public void setDefaultFirst(boolean defaultFirst){
        this.defaultFirst = defaultFirst;
    }

    /**
     * 获取筛选的数据
     */
    public List<String> getLabelData(){
        return labelLists;
    }

    /**
     * 重置数据
     */
    public void resetData(){
        for (int i=0;i<selectLabel.size();i++){
            selectLabel.get(i).setSelected(false);
        }
        selectTab.clear();
        labelLists.clear();
    }
}
