# LeafChart


##一、折线图


### 1.1 设置
-  坐标轴颜色、宽度
-  坐标轴刻度字体大小、颜色
-  折线或曲线宽度、颜色
-  点的大小、颜色
-  是否有标签
-  标签背景色、弧度
-  是否填充



### 1.2 效果图
![截图1](https://github.com/LineChen/LeafChart/blob/master/screenshot/cubic_filled.png)

![截图2](https://github.com/LineChen/LeafChart/blob/master/screenshot/fold_not_filled.png)

### 1.3 使用

``` java
    <com.beiing.leafchart.LeafLineChart
        android:id="@+id/leaf_chart"
        android:layout_width="match_parent"
        android:layout_height="300dp"
        android:background="#ffffff"/>

```


初始化X轴数据：
``` java
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
```java
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
```java
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

```java

Axis axisX = new Axis(getAxisValuesX());
axisX.setAxisColor(Color.parseColor("#7cb342")).setTextColor(Color.DKGRAY).setHasLines(true);
Axis axisY = new Axis(getAxisValuesY());
axisY.setAxisColor(Color.parseColor("#7cb342")).setTextColor(Color.DKGRAY).setHasLines(true).setShowText(true);
fuckLineChart.setAxisX(axisX);
fuckLineChart.setAxisY(axisY);
fuckLineChart.setLine(getFoldLine());

```


## 二、直方图

使用类似折线图

###2.1  设置
- 直方图宽度
- 边框宽度、颜色
- 是否填充
-   是否有标签
-  标签背景色、弧度


###2.2 效果图

![square](https://github.com/LineChen/LeafChart/blob/master/screenshot/square.png)


![square](https://github.com/LineChen/LeafChart/blob/master/screenshot/square2.png)




