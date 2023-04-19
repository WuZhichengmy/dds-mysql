package com.xmu.modules.display_config.utils;

import lombok.Getter;
import lombok.Setter;

import java.util.*;

@Getter
@Setter
public class MultiPartDatasetBuilder implements Builder {

    public enum DataType {
        LINE, BAR
    }

    private String x;
    // 作为y轴出现的维度名
    private String y;

    // 对于data而言，sql执行的结果要求至少是一个包含x，y的三元组
    private List<Map<String, Object>> data;
    private List<String> dimensions;
    private List<Map<String, Object>> source;
    private List<Map<String, Object>> series;

    public MultiPartDatasetBuilder() {
        this.dimensions = new ArrayList<>();
        this.source = new ArrayList<>();
        this.series = new ArrayList<>();
    }

    public MultiPartDatasetBuilder(String x, String y, List<Map<String, Object>> data) {
        this();
        this.x = x;
        this.y = y;
        this.data = data;
    }

    @Override
    public void build() {
        if (this.x == null || this.x.isEmpty()
                || this.y == null || this.y.isEmpty()
                || this.data == null) {
            System.err.println("param error in DatasetBuilder.");
            return;
        }
        Set<String> dimensionSet = new HashSet<>();
        Map<String, List<Map<String, Object>>> partitions = new HashMap<>();

        // 以x的需求进行分割
        for (Map<String, Object> row : data) {
            String key = row.get(x).toString();
            row.remove(x);

            partitions.putIfAbsent(key, new ArrayList<>());
            partitions.get(key).add(row);
        }

        for (Map.Entry<String, List<Map<String, Object>>> e : partitions.entrySet()) {
            Map<String, Object> items = new HashMap<>();
            items.put(x, e.getKey());
            for (Map<String, Object> attr : e.getValue()) {
                // 分离出y
                String value = attr.get(y).toString();
                attr.remove(y);
                Integer intValue = Integer.parseInt(value);
                // 将剩余项作为子维度
                StringBuilder sb = new StringBuilder();
                for (Object o : attr.values()) {
                    sb.append(o.toString());
                }
                String subDimension = sb.toString();
                dimensionSet.add(subDimension);

                items.put(subDimension, intValue);
            }
            source.add(items);
        }
        ArrayList<String> dList = new ArrayList<>(dimensionSet);
        dList.sort(Comparator.naturalOrder());
        dList.add(0, x);
        dimensions.addAll(dList);
    }

    public Map<String, Object> getDataset() {
        Map<String, Object> ret = new HashMap<>();
        source.sort((o1, o2) -> o1.get(x).toString().compareTo(o2.get(x).toString()));
        ret.put("dimensions", dimensions);
        ret.put("source", source);
        return ret;
    }

    public List<Map<String, Object>> getSeries(DataType type, boolean isStack) {
        String seriesType;
        switch (type) {
            case LINE:
                seriesType = "line";
                break;
            case BAR:
            default:
                seriesType = "bar";
        }
        for (int i = 0; i < dimensions.size() - 1; i++) {
            Map<String, Object> map = new HashMap<>();

            map.put("type", seriesType);
            if (isStack) {
                map.put("stack", "all");
            }
            series.add(map);
        };
        return series;
    }
}
