package com.ruancw.flowfilterdemo;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by ruancw on 2018/5/31.
 * 用于筛选的数据类
 */

public class FilterBean implements Parcelable{
    private String typeName;//标题名字
    private TableMode tab;//用于记录上次点击的位置
    private List<TableMode> tabs; //标签集合
    private List<TableMode> labels; //标签集合

    public FilterBean(TableMode tab, List<TableMode> tabs) {
        this.tab = tab;
        this.tabs = tabs;
    }

    public FilterBean(String typeName, TableMode tab, List<TableMode> tabs) {
        this.typeName = typeName;
        this.tab = tab;
        this.tabs = tabs;
    }

    protected FilterBean(Parcel in) {
        typeName = in.readString();
    }

    public static final Creator<FilterBean> CREATOR = new Creator<FilterBean>() {
        @Override
        public FilterBean createFromParcel(Parcel in) {
            return new FilterBean(in);
        }

        @Override
        public FilterBean[] newArray(int size) {
            return new FilterBean[size];
        }
    };

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public TableMode getTab() {
        return tab;
    }

    public void setTab(TableMode tab) {
        this.tab = tab;
    }

    public List<TableMode> getTabs() {
        return tabs;
    }

    public void setTabs(List<TableMode> tabs) {
        this.tabs = tabs;
    }

    public List<TableMode> getLabels() {
        return labels;
    }

    public void setLabels(List<TableMode> labels) {
        this.labels = labels;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(typeName);
    }

    public static class TableMode implements Parcelable{
        public String name;

        public TableMode(String name) {
            this.name = name;
        }

        protected TableMode(Parcel in) {
            name = in.readString();
        }

        public static final Creator<TableMode> CREATOR = new Creator<TableMode>() {
            @Override
            public TableMode createFromParcel(Parcel in) {
                return new TableMode(in);
            }

            @Override
            public TableMode[] newArray(int size) {
                return new TableMode[size];
            }
        };

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel parcel, int i) {
            parcel.writeString(name);
        }
    }

}
