package com.xmu.modules.display_config.request;

import com.alibaba.fastjson.JSONObject;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.*;

/**
 * @author summer
 * @see <a href=""></a><br/>
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class SqlDTO {
    private Boolean need;
    //select importance as xAxis
    private Map<String,String> selectAs;
    //from table
    private Long resourceId;
    //where title like IARPA
    private List<Condition> conditions;
    //and,or
    private List<String> conjunctions;
    //group by
    private String groupBy;
    //order by
    private String orderBy;
    //desc
    private String orderType;
    //limit
    private Integer limit;


    //最好修改为有限状态自动机
    public String toSql(List<Long> ids,String table){
        StringBuilder sBuilder=new StringBuilder().append("select ");
        if(need&&(selectAs==null||!selectAs.containsKey("id"))){
            sBuilder.append("id,");
        }
        if(selectAs!=null){
            selectAs.forEach((k, v)->{
                if(!k.equals("")){
                    sBuilder.append(k);
                }
                if(!v.equals("")){
                    sBuilder.append(" as ").append(v);
                }
                sBuilder.append(",");
            });
        }
        //取出最后一个逗号
        sBuilder.deleteCharAt(sBuilder.length()-1);
        sBuilder.append(" from ").append(table);
        if(ids!=null){
            sBuilder.append(" where ").append(toInIds(ids));
        }
        if(conditions!=null){
            if(!sBuilder.toString().contains("where")){
                sBuilder.append(" where ").append(" (");
            }else {
                sBuilder.append("and").append(" (");
            }
            for (int i = 0,j=0; i < conditions.size(); i++) {
                Condition condition = conditions.get(i);
                sBuilder.append(" ").append(condition.toCondition()).append(" ");
                if(conjunctions!=null&&j< conjunctions.size()){
                    sBuilder.append(conjunctions.get(j++));
                }
            }
            sBuilder.deleteCharAt(sBuilder.length()-1).append(")");
        }
        if(groupBy!=null){
            sBuilder.append(" group by ").append(groupBy);
        }
        if(orderBy!=null){
            sBuilder.append(" order by ").append(orderBy);
        }
        if(orderType!=null){
            sBuilder.append(" ").append(orderType);
        }
        if(limit!=null){
            sBuilder.append(" limit ").append(limit);
        }
        return sBuilder.toString();
    }

    private String toInIds(List<Long> ids){
        StringBuilder sBuilder=new StringBuilder(Arrays.toString(ids.toArray()));
        sBuilder.deleteCharAt(0).deleteCharAt(sBuilder.length()-1);
        return "id in ("+sBuilder+" ) ";
    }

    public static void main(String[] args) {
        SqlDTO sqlDTO = new SqlDTO()
                .setSelectAs(new HashMap<String, String>() {{
                    put("importance", "");
                    put("count(importance)","value");
                }})
                .setConditions(new ArrayList<Condition>() {{
                    add(new Condition().setField("title").setCondition("like").setValue("IARPA"));
                }})
//                .setGroupBy("importance")
//                .setConjunctions(Arrays.stream(new String[]{
//                        "and", "or"
//                }).collect(Collectors.toList()))
//                .setOrderBy("importance")
//                .setOrderType("desc")
                .setGroupBy("importance");
        sqlDTO.setNeed(true);
        System.out.println(JSONObject.toJSON(sqlDTO));
        String sql = sqlDTO.toSql(null,"cdxm_news");
        System.out.println(sql);
    }
}
