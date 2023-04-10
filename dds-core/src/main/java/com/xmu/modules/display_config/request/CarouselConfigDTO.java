package com.xmu.modules.display_config.request;

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
public class CarouselConfigDTO {

    @NotNull
    private Long resourceId;
//    @NotNull
//    private SqlDTO sql;
    @NotNull
    private Long recordId;

    @NotNull
    private String url;

//    public String toSql(String table){
////        sql.setNeed(true);
////        return sql.toSql(null,table);
//        return sql;
//    }

//    public CarouselDTO toData(String table){
//        return new CarouselDTO().setResourceId(resourceId)
//                .setSqlAndUrl(new HashMap<String,String>(){{put(toSql(table),url);}});
//    }
}
