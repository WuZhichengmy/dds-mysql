package com.xmu.modules.display_config.request;

import com.xmu.modules.display_config.enums.OrderEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * table已知
 * @author summer
 * @see <a href=""></a><br/>
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class DataSourceDTO {

//    private String type;
//    private Long resourceId;
    private String table;
    private List<String> fetchSources;
    private List<Condition> conditions;
    private Integer limit;
    private String orderColumn;
    private String order;

    public String toSql(){
        StringBuilder fetchSource= new StringBuilder();
        fetchSources.forEach(s -> fetchSource.append(s).append(","));
        String fetchS = fetchSource.deleteCharAt(fetchSource.length() - 1).toString();
        StringBuilder con=new StringBuilder();
        conditions.forEach(condition -> {
            con.append(condition.getField())
                    .append(" ")
                    .append(condition.getCondition())
                    .append(" ")
                    .append(condition.getValue())
                    .append(",");
        });
        String conditions = con.deleteCharAt(con.length() - 1).toString();
        return "select "+fetchS+" "+
                "from "+table+" "+
                "where "+conditions+" "+
                "order by "+orderColumn+" "+
                order+" "+
                "limit 0,"+limit;
    }
}
