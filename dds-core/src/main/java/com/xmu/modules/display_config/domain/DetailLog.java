package com.xmu.modules.display_config.domain;

import com.xmu.model.IdEntity;
import lombok.Data;

import java.util.Date;

/**
 * @author Xing
 */
@Data
public class DetailLog extends IdEntity {

    private Long projectId;

    private Long resourceId;

    private Long detailDataId;

    private String username;

    private Date createTime;

}