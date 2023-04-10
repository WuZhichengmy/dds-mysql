package com.xmu.modules.display_config.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Map;

/**
 * @author summer
 * @see <a href=""></a><br/>
 * @description 用户知道的，用户自己来配置。
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class CarouselItemDTO {
    //[关键字、图片的URL]
    @NotNull
    private List<ImgInfo> imgInfos;
}
