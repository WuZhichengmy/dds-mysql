package com.xmu.modules.display_config.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xmu.modules.display_config.domain.RuleSet;
import com.xmu.modules.display_config.request.RuleSetDTO;
import org.springframework.http.ResponseEntity;

import java.util.List;

/**
 * @author Xing
 */
public interface RuleSetService extends IService<RuleSet> {

    /**
     * 获取规则集
     * @param resourceId
     * @return
     */
    ResponseEntity<List<RuleSetDTO>> listRuleSet(Long resourceId);

    /**
     * 创建/修改规则集
     * @param ruleSetDTO
     * @return
     */
    ResponseEntity<Long> saveOrUpdateRuleSet(RuleSetDTO ruleSetDTO);

    /**
     * 删除规则集
     * @param id
     * @return
     */
    ResponseEntity deleteRuleSet(Long id);
}
