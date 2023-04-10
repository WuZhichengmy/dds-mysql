package com.xmu.modules.display_config.config;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.xmu.utils.SecurityUtils;
import org.apache.ibatis.reflection.MetaObject;

import java.util.Date;

/**
 * 属性自动填充配置类
 * @author Xing
 */
public class DateMetaObjectHandler implements MetaObjectHandler {

    private static final String CREATE_USER = "createUser";
    private static final String CREATE_TIME = "createTime";
    private static final String MODIFY_USER = "modifyUser";
    private static final String MODIFY_TIME = "modifyTime";

    /**
     * 插入填充
     * @param metaObject
     */
    @Override
    public void insertFill(MetaObject metaObject) {
        String username = SecurityUtils.getCurrentUsername();

        Object createUser = getFieldValByName(CREATE_USER, metaObject);
        Object createTime = getFieldValByName(CREATE_TIME, metaObject);
        Object modifyUser = getFieldValByName(MODIFY_USER, metaObject);
        Object modifyTime = getFieldValByName(MODIFY_TIME, metaObject);

        if (createUser == null || modifyUser == null) {
            if (createUser == null) {
                setFieldValByName(CREATE_USER, username, metaObject);
            }
            if (modifyUser == null) {
                setFieldValByName(MODIFY_USER, username, metaObject);
            }
        }

        if (createTime == null || modifyTime == null) {
            Date date = new Date();
            if (createTime == null) {
                setFieldValByName(CREATE_TIME, date, metaObject);
            }
            if (modifyTime == null) {
                setFieldValByName(MODIFY_TIME, date, metaObject);
            }
        }
    }

    /**
     * 更新填充
     * @param metaObject
     */
    @Override
    public void updateFill(MetaObject metaObject) {
        Object modifyUser = getFieldValByName(MODIFY_USER, metaObject);

        if (modifyUser == null) {
            String username = SecurityUtils.getCurrentUsername();
            setFieldValByName(MODIFY_USER, username, metaObject);
        }
        setFieldValByName(MODIFY_TIME, new Date(), metaObject);
    }
}
