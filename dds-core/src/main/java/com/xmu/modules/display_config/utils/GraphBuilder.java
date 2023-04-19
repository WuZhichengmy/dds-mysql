package com.xmu.modules.display_config.utils;

import cn.hutool.core.lang.Pair;
import com.xmu.modules.display_config.request.GraphConfig;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

import java.util.*;
import java.util.stream.Collectors;

@Getter
public class GraphBuilder implements Builder {
    @Data
    @AllArgsConstructor
    public static class Node {
        private String name;
        private Object value;

        private Object symbolSize;
        private String category;

        public Node(String name, Object value) {
            this.name = name;
            this.value = value;
        }

        public Map<String, Object> toMap() {
            Map<String, Object> map = new HashMap<>();
            map.put("name", name);
            if (value != null)
                map.put("value", value);
            if (category != null)
                map.put("category", category);
            if (symbolSize != null)
                map.put("symbolSize", symbolSize);
            else map.put("symbolSize", 4);
            return map;
        }
    }
    private List<Map<String, Object>> data;
    private List<Map<String, Object>> links;
    private List<Map<String, Object>> categories;
    private List<Map<String, Object>> queryResult;
    private GraphConfig graphConfig;

    public GraphBuilder(List<Map<String, Object>> queryResult, GraphConfig graphConfig) {
        this();
        this.queryResult = queryResult;
        this.graphConfig = graphConfig;
    }

    public GraphBuilder() {
        categories = new ArrayList<>();
        links = new ArrayList<>();
        data = new ArrayList<>();
    }

    @Override
    public void build() {
//        int categoryId = 0;
        Map<String, Node> nodeMap = new HashMap<>();
//        Map<String, Integer> categoryMap = new HashMap<>();
        Map<Pair<String, String>, Object> linkMap = new HashMap<>();
        // 对于期望的数据形制类似于：
        // | relations  | link value | node valve（暂时移除） | category（已暂时移除） |
        // | ---------- | ---------- | ---------- | -------- |
        // | 检查;安装; | 1          | 1          | c1       |
        for (Map<String, Object> row : queryResult) {

            // 对于主体分类的工作，似乎需要转移到另外一部分sql语句来实现
//            String category = "";
//            String graphCategory = graphConfig.getCategory();
//            if (graphCategory != null && !graphCategory.trim().isEmpty()) {
//                category = (String) row.get(graphCategory);
//                if (!categoryMap.containsKey(category)) {
//                    categoryMap.put(category, categoryId++);
//                }
//                row.remove(graphCategory);
//            }

            String linkValue = "";
            boolean isLinkOfCooccurence = false;
            String graphLinkValue = graphConfig.getGraphLinkValue();
            if (graphLinkValue != null && !graphLinkValue.trim().isEmpty()) {
                // TODO 涉及到对linkValue的解析
                linkValue = (String) row.get(graphLinkValue);
                row.remove(graphLinkValue);
            } else {
                // 缺省，意味着是共现值
                isLinkOfCooccurence = true;
            }

            // 为时尚早的讨论
//            String nodeValue = "";
//            boolean isNodeOfCooccurence = false;
//            String graphNodeValue = graphConfig.getGraphNodeValue();
//            if (graphNodeValue != null && !graphNodeValue.trim().isEmpty()) {
//                // TODO 涉及到对nodeValue的解析
//                nodeValue = (String) row.get(graphNodeValue);
//                row.remove(graphNodeValue);
//            } else {
//                // 缺省，意味着是共现值
//                isNodeOfCooccurence = true;
//            }

            // 剩下的项即为relation
            if (row.size() != 1) {
                System.err.println("wrong data format, there is more than 1 column for relations.");
                return;
            }
            String[] splits = new ArrayList<>(row.entrySet()).get(0).getValue().toString().split(";");
            splits = Arrays.stream(splits).filter(s -> !s.trim().isEmpty())
                    .collect(Collectors.toSet()).toArray(new String[]{});
            if (isLinkOfCooccurence) {
                for (String nodeName : splits) {
                    updateCooccurenceNodeMap(nodeMap, nodeName);
                }
            } else {
                // TODO 对节点的更新
            }
            String directed = graphConfig.getIsDirected();
            boolean isDirected = directed == null || !directed.trim().isEmpty();
            for (int i = 0; i < splits.length - 1; i++) {
                for (int j = isDirected ? 0 : i + 1; j < splits.length; j++) {
                    if (isLinkOfCooccurence) {
                        updateCooccurenceLinkMap(linkMap, splits[i], splits[j]);
                    } else {
                        // TODO 如果不是共现关系
                    }
                }
            }
        }

        // 数据转换部分
        // 计算symbolSize
        updateNodeMapSymbolSize(nodeMap);

        String nodeValueThreshold = graphConfig.getNodeValueThreshold();
        int intNodeThreshold;
        if (nodeValueThreshold != null && !nodeValueThreshold.trim().isEmpty()) {
            intNodeThreshold = Integer.parseInt(nodeValueThreshold);
        } else {
            intNodeThreshold = Integer.MIN_VALUE;
        }
        data = nodeMap.values().stream().filter(node -> {
            if (node.value instanceof Number) {
                return (int) node.value >= intNodeThreshold;
            }
            else return true;
        }).map(Node::toMap).collect(Collectors.toList());

        String linkValueThreshold = graphConfig.getLinkValueThreshold();
        int intLinkThreshold;
        if (linkValueThreshold != null && !linkValueThreshold.trim().isEmpty()) {
            intLinkThreshold = Integer.parseInt(linkValueThreshold);
        } else {
            intLinkThreshold = Integer.MIN_VALUE;
        }
        links = linkMap.entrySet().stream().filter(entry -> {
        if (entry.getValue() instanceof Number) {
            return (int) entry.getValue() >= intLinkThreshold;
        }
        else return true;
        }).map(entry -> {
            Map<String, Object> retMap = new HashMap<>();
            retMap.put("source", entry.getKey().getKey());
            retMap.put("target", entry.getKey().getValue());
            retMap.put("value", entry.getValue());
            retMap.put("lineStyle", new HashMap<String, Object>());
            ((Map<String, Object>) retMap.get("lineStyle")).put("normal", new HashMap<>());
            return retMap;
        }).collect(Collectors.toList());

        // 更新link的width
        updateLinksWidth(links);
    }

    private void updateLinksWidth(List<Map<String, Object>> linkMap) {
        double maxValue = Double.MIN_VALUE;
        for (Map<String, Object> e : linkMap) {
            maxValue = Math.max(((Integer) e.get("value")).doubleValue(), maxValue);
        }
        double scale = 3.0;
        for (Map<String, Object> e : linkMap) {
            ((Map<String, Object>) ((Map<String, Object>) e.get("lineStyle"))
                    .get("normal"))
                    .put("width", ((((Integer) e.get("value")).doubleValue() / maxValue) * scale));
        }
    }

    private void updateNodeMapSymbolSize(Map<String, Node> nodeMap) {
        int maxValue = Integer.MIN_VALUE;
        boolean isNumber = false;
        for (Map.Entry<String, Node> e : nodeMap.entrySet()) {
            if (!isNumber) {
                if (e.getValue().getValue() instanceof Number) {
                    isNumber = true;
                }
                else return;
            }
            maxValue = Math.max(maxValue, ((Number) e.getValue().getValue()).intValue());
        }

        // 将节点值映射到 0 - scale 范围
        double scale = 100;
        for (Map.Entry<String, Node> e : nodeMap.entrySet()) {
            e.getValue().setSymbolSize((((Integer) e.getValue().getValue()).doubleValue() / (double) maxValue) * scale);
        }
    }

    private void updateCooccurenceNodeMap(Map<String, Node> nodeMap, String nodeName) {
        if (nodeMap.containsKey(nodeName)) {
            nodeMap.get(nodeName).value = ((int) nodeMap.get(nodeName).value) + 1;
        } else {
            nodeMap.put(nodeName, new Node(nodeName, 1));
        }
    }

    private void updateCooccurenceLinkMap(Map<Pair<String, String>, Object> linkMap, String subject, String object) {
        // 对于共现矩阵而言，一定是无向图，于是规定关系主体是string中较小的那一个。
        String minor = subject.compareTo(object) < 0 ? subject : object;
        object = subject.compareTo(object) > 0 ? subject : object;
        subject = minor;
        Pair<String, String> keyPair = new Pair<>(object, subject);
        if (linkMap.containsKey(keyPair)) {
            linkMap.put(keyPair, ((int) linkMap.get(keyPair)) + 1);
        } else {
            linkMap.put(keyPair, 1);
        }
    }

}
