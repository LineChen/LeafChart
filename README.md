# LeafChart


##一、折线图


### 1.1 设置

>坐标轴

-  `Axis.setAxisColor` 	坐标轴颜色
-  `Axis.setAxisWidth`	坐标轴宽度
-  `Axis.setTextSize`	坐标轴刻度字体大小
-  `Axis.setTextColor`	坐标轴刻度字体颜色

>折线

-  `Line.setLineWidth`	折线宽度
-  `Line.setLineColor`	折线颜色
-  `Line.setPointRadius`	点的大小
-  `Line.setPointColor`		点的颜色
-  `Line.setHasLabels`		是否有标签
-  `Line.setLabelColor`		标签背景色
-  `Line.setLabelRadius`	标签弧度
-  `Line.setFill`		是否填充
-  `Line.setFillColr`	填充颜色(默认为有透明度的折线颜色)



### 1.2 效果图
![截图1](https://github.com/LineChen/LeafChart/blob/master/screenshot/animate_line1.gif)

![截图2](https://github.com/LineChen/LeafChart/blob/master/screenshot/animate_line2.gif)

多线条支持

![多线条支持](https://github.com/LineChen/LeafChart/blob/master/screenshot/multi_lines.png)

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
        axisX.setAxisColor(Color.parseColor("#33B5E5")).setTextColor(Color.DKGRAY).setHasLines(true);
        Axis axisY = new Axis(getAxisValuesY());
        axisY.setAxisColor(Color.parseColor("#33B5E5")).setTextColor(Color.DKGRAY).setHasLines(true).setShowText(true);
        lineChart.setAxisX(axisX);
        lineChart.setAxisY(axisY);
        List<Line> lines = new ArrayList<>();
        lines.add(getFoldLine());
        lines.add(getCompareLine());
        lineChart.setChartData(lines);

        lineChart.showWithAnimation(3000);

	//无动画
//lineChart.show();
```


## 二、直方图

使用类似折线图

###2.1  设置
- `Square.setWidth`	直方图宽度
- `Square.setBorderWidth`	边框宽度
- `Square.setBorderColor`	边框颜色
- `Square.setFill`	是否填充
- `Square.setHasLabels`  是否有标签
- `Square.setLabelColor`		标签背景色
- `Square.setLabelRadius`	标签弧度


###2.2 效果图

![square](https://github.com/LineChen/LeafChart/blob/master/screenshot/square.png)


![square](https://github.com/LineChen/LeafChart/blob/master/screenshot/square2.png)



#License

```
   Copyright 2016 LineChen <15764230067@163.com>

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
```

