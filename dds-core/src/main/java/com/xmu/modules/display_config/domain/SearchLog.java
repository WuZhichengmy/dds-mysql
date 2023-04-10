package com.xmu.modules.display_config.domain;

import com.xmu.model.IdEntity;
import lombok.Data;

import java.util.Date;

/**
 * @author Xing
 */
@Data
public class SearchLog extends IdEntity {

    private Long projectId;

    private Long resourceId;

    private String username;

    private String words;

    private Date createTime;

}