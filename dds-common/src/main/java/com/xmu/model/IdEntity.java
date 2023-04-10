package com.xmu.model;

import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.io.Serializable;

/**
 * Created by zhangchen on 2020/3/29.
 */
@Data
public class IdEntity implements Serializable{

    private static final long serialVersionUID = -47787789784928184L;

    @TableId
    private Long id;
}
