package com.xmu.modules.display_config.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Lists;
import com.xmu.exception.EntityNotFoundException;
import com.xmu.modules.display_config.domain.ImportProperty;
import com.xmu.modules.display_config.domain.Resource;
import com.xmu.modules.display_config.domain.RuleSet;
import com.xmu.modules.display_config.mapper.RuleSetMapper;
import com.xmu.modules.display_config.request.RuleSetDTO;
import com.xmu.modules.display_config.service.ImportPropertyService;
import com.xmu.modules.display_config.service.ResourceService;
import com.xmu.modules.display_config.service.RuleSetService;
import com.xmu.utils.SnowFlakeUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * @author Xing
 */
@Service
public class RuleSetServiceImpl extends ServiceImpl<RuleSetMapper, RuleSet> implements RuleSetService {

    @Autowired
    private ResourceService resourceService;
    @Autowired
    private ImportPropertyService importPropertyService;

    @Override
    public ResponseEntity<List<RuleSetDTO>> listRuleSet(Long resourceId) {
        // 查资源
        Resource resource = resourceService.getById(resourceId);
        if (null == resource) {
            throw new EntityNotFoundException(Resource.class, "id", resourceId);
        }
        List<RuleSet> list = this.list(Wrappers.<RuleSet>lambdaQuery().eq(RuleSet::getResourceId, resourceId));
        List<RuleSetDTO> result = Lists.newArrayListWithCapacity(list.size());
        list.forEach(ruleSet -> {
            RuleSetDTO ruleSetDTO = new RuleSetDTO();
            BeanUtils.copyProperties(ruleSet, ruleSetDTO);
            result.add(ruleSetDTO);
        });
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResponseEntity<Long> saveOrUpdateRuleSet(RuleSetDTO ruleSetDTO) {
        // 查资源
        Resource resource = resourceService.getById(ruleSetDTO.getResourceId());
        if (null == resource) {
            throw new EntityNotFoundException(Resource.class, "id", ruleSetDTO.getResourceId());
        }
        // 新增
        if (null == ruleSetDTO.getId()) {
            RuleSet ruleSet = new RuleSet();
            BeanUtils.copyProperties(ruleSetDTO, ruleSet);
            ruleSet.setId(SnowFlakeUtil.getFlowIdInstance().nextId());
            ruleSet.setCreateTime(new Date());
            ruleSet.setModifyTime(new Date());
            this.save(ruleSet);
            return new ResponseEntity<>(ruleSet.getId(), HttpStatus.OK);
        }
        RuleSet ruleSet = this.getById(ruleSetDTO.getId());
        if (null == ruleSet) {
            throw new EntityNotFoundException(RuleSet.class, "id", ruleSetDTO.getResourceId());
        }
        ruleSet.setName(ruleSetDTO.getName());
        ruleSet.setDescription(ruleSetDTO.getDescription());
        ruleSet.setModifyTime(new Date());
        this.updateById(ruleSet);
        return new ResponseEntity(HttpStatus.OK);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResponseEntity deleteRuleSet(Long id) {
        importPropertyService.remove(Wrappers.<ImportProperty>lambdaQuery()
                .eq(ImportProperty::getRuleSetId, id));
        this.removeById(id);
        return new ResponseEntity(HttpStatus.OK);
    }
}
