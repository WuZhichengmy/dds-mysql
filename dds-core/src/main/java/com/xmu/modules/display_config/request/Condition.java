package com.xmu.modules.display_config.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * @author summer
 * @see <a href=""></a><br/>
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class Condition {
    private String field;
    private String condition;
    private String value;

    public String toCondition(){
        if(condition.equals("like")){
            return field+" "+condition+" '%"+value+"%'";
        }
        return field+" "+condition+" "+value;
    }

}
