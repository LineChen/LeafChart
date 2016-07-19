# LeafChart


## 设置
-  坐标轴颜色、宽度
-  坐标轴刻度字体大小、颜色
-  折线或曲线宽度、颜色
-  点的大小、颜色
-  是否有标签
-  标签背景色、弧度
-  是否填充



## 屏幕截图
![截图1](https://github.com/LineChen/LeafChart/blob/master/screenshot/cubic_filled.png)

![截图2](https://github.com/LineChen/LeafChart/blob/master/screenshot/fold_not_filled.png)

## 使用

1. 导入library到项目中
2. 代码中使用

```
    <com.beiing.leafchart.LeafLineChart
        android:id="@+id/leaf_chart"
        android:layout_width="match_parent"
        android:layout_height="300dp"
        android:background="#ffffff"/>


    private List<AxisValue> getAxisValuesX(){
        List<AxisValue> axisValues = new ArrayList<>();
        for (int i = 1; i <= 12; i++) {
            AxisValue value = new AxisValue();
            value.setLabel(i + "月");
            axisValues.add(value);
        }
        return axisValues;
    }


```













































=======
>>>>>>> 4a6f06f3c5b8c8cf12621c219573446a9833fe2d
