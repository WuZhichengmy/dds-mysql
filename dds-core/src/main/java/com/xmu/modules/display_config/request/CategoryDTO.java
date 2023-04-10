package com.xmu.modules.display_config.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;

/**
 * @author summer
 * @see <a href=""></a><br/>
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class CategoryDTO {

    @NotNull
    private Long resourceId;
    @JsonProperty(value = "xAxis")
    @NotNull
    private String xAxis;
//    @JsonProperty(value = "yAxis")
//    @NotNull
//    private String yAxis;
//    @NotNull
//    private SqlDTO sql;
//    @NotNull
//    private String sql;
//
//    public String toSql(String table){
////        sql.setNeed(false);
////        return sql.toSql(null,table);
//        return sql;
//    }
}
