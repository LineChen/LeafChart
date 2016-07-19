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



``` Android
    <com.beiing.leafchart.LeafLineChart
        android:id="@+id/leaf_chart"
        android:layout_width="match_parent"
        android:layout_height="300dp"
        android:background="#ffffff"/>

```


初始化X轴数据：
``` python
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


初始化Y轴数据：
```python
private List<AxisValue> getAxisValuesY(){
        List<AxisValue> axisValues = new ArrayList<>();
        for (int i = 0; i < 11; i++) {
            AxisValue value = new AxisValue();
            value.setLabel(String.valueOf(i * 10));
            axisValues.add(value);
        }
        return axisValues;
    }
```

初始化点数据和相关设置：
```python
    private Line getFoldLine(){
        List<PointValue> pointValues = new ArrayList<>();
        for (int i = 1; i <= 12; i++) {
            PointValue pointValue = new PointValue();
            pointValue.setX( (i - 1) / 11f);
            float var = (float) (Math.random() * 100);
            pointValue.setLabel(String.valueOf(var));
            pointValue.setY(var / 100);
            pointValues.add(pointValue);
        }

        Line line = new Line(pointValues);
        line.setLineColor(Color.parseColor("#33B5E5")).setPointColor(Color.YELLOW).
                setCubic(false).setPointRadius(3).setHasLabels(true)
                .setFill(false);
        return line;
    }
```

```python

Axis axisX = new Axis(getAxisValuesX());
axisX.setAxisColor(Color.parseColor("#7cb342")).setTextColor(Color.DKGRAY).setHasLines(true);
Axis axisY = new Axis(getAxisValuesY());
axisY.setAxisColor(Color.parseColor("#7cb342")).setTextColor(Color.DKGRAY).setHasLines(true).setShowText(true);
fuckLineChart.setAxisX(axisX);
fuckLineChart.setAxisY(axisY);
fuckLineChart.setLine(getFoldLine());


```













