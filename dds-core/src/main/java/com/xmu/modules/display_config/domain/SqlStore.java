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
@TableName("sql_store")
public class SqlStore extends BaseEntity {

    @TableField("project_id")
    private Long projectId;
    @TableField("resource_id")
    private Long resourceId;
    @TableField("fields")
    private String fields;
    @TableField("type")
    private Integer compType;
}
