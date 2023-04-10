package com.xmu.model;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by zhangchen on 2020/3/29.
 */
@Data
public class BaseEntity extends IdEntity implements Serializable{

    private static final long serialVersionUID = -5345685318511961138L;

    @TableField(fill = FieldFill.INSERT)
    private String createUser;

    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private String modifyUser;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date modifyTime;
}
