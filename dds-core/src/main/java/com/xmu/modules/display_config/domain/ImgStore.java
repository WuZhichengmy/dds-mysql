package com.xmu.modules.display_config.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.xmu.model.BaseEntity;
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
@TableName("img_store")
public class ImgStore extends BaseEntity {
    @TableField("img_url")
    private String imgUrl;
    @TableField("project_id")
    private Long projectId;
    @TableField("comp_type")
    private String compType;
    @TableField("resource_id")
    private Long resourceId;
    @TableField("record_id")
    private Long recordId;

}
