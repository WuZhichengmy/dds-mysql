package com.xmu.modules.display_config.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.Map;

/**
 * @author summer
 * @see <a href=""></a><br/>
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class CarouselItmRetDTO {
    private Long resourceId;
//    private Long id;
    private String img;
//    private String text;
    private Map<String,Object> map;
}
