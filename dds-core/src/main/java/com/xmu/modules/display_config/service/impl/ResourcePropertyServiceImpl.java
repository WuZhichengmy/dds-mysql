package com.xmu.modules.display_config.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Lists;
import com.xmu.exception.BadRequestException;
import com.xmu.exception.EntityNotFoundException;
import com.xmu.modules.display_config.constant.Constants;
import com.xmu.modules.display_config.domain.Resource;
import com.xmu.modules.display_config.domain.ResourceProperty;
import com.xmu.modules.display_config.mapper.ResourcePropertyMapper;
import com.xmu.modules.display_config.request.*;
import com.xmu.modules.display_config.service.AnalysisMenuService;
import com.xmu.modules.display_config.service.ResourcePropertyService;
import com.xmu.modules.display_config.service.ResourceService;
import com.xmu.modules.display_config.service.SearchConfigService;
import com.xmu.utils.SnowFlakeUtil;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
public class ResourcePropertyServiceImpl extends ServiceImpl<ResourcePropertyMapper, ResourceProperty> implements ResourcePropertyService {

    @Autowired
    private ResourceService resourceService;
    @Autowired
    private SearchConfigService searchConfigService;
    @Autowired
    private AnalysisMenuService analysisMenuService;

    @Override
    public ResponseEntity<List<ResourcePropertyDTO>> listResourceProperty(Long projectId, Long resourceId) {
        // 查资源
        Resource resource = resourceService.getById(resourceId);
        if (null == resource) {
            throw new EntityNotFoundException(Resource.class, "id", resourceId);
        }
        List<ResourceProperty> list = this.list(Wrappers.<ResourceProperty>lambdaQuery()
                .eq(ResourceProperty::getProjectId, projectId)
                .eq(ResourceProperty::getResourceId, resourceId));
        List<ResourcePropertyDTO> result = Lists.newArrayList();
        list.forEach(resourceProperty -> {
            ResourcePropertyDTO resourcePropertyDTO = new ResourcePropertyDTO();
            BeanUtils.copyProperties(resourceProperty, resourcePropertyDTO);
            result.add(resourcePropertyDTO);
        });
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResponseEntity saveOrUpdateResourceProperty(ResourcePropertyDTO resourcePropertyDTO) {
        // 查默认属性
        if (null == resourcePropertyDTO.getId() && 1 == resourcePropertyDTO.getIsDefault()) {
            ResourceProperty defaultProperty = this.getOne(Wrappers.<ResourceProperty>lambdaQuery()
                    .eq(ResourceProperty::getIsDefault, 1)
                    .eq(ResourceProperty::getResourceId, resourcePropertyDTO.getResourceId()));
            if (null != defaultProperty) {
                throw new BadRequestException("already have a default field");
            }
        }
        // 查资源
        Resource resource = resourceService.getById(resourcePropertyDTO.getResourceId());
        if (null == resource) {
            throw new EntityNotFoundException(Resource.class, "id", resourcePropertyDTO.getResourceId());
        }
        // id不可操作
        if (Constants.PRIMARY_KEY.equalsIgnoreCase(resourcePropertyDTO.getName())) {
            throw new BadRequestException("主键id不可操作!");
        }
        // 已有数据无法操作
        Integer count = resourceService.countResourceData(resource.getTarget(), null, null);
        if (0 < count) {
            throw new BadRequestException("资源已有数据，无法添加/编辑!");
        }
        // 同名字段
        ResourceProperty sameName = this.getOne(Wrappers.<ResourceProperty>lambdaQuery()
                .eq(ResourceProperty::getProjectId, resourcePropertyDTO.getProjectId())
                .eq(ResourceProperty::getResourceId, resourcePropertyDTO.getResourceId())
                .eq(ResourceProperty::getName, resourcePropertyDTO.getName()));
        // 新增属性
        if (null == resourcePropertyDTO.getId()) {
            if (null != sameName) {
                throw new BadRequestException("已有重名属性!");
            }
            ResourceProperty resourceProperty = new ResourceProperty();
            BeanUtils.copyProperties(resourcePropertyDTO, resourceProperty);
            resourceProperty.setId(SnowFlakeUtil.getFlowIdInstance().nextId());

            //创建时间
            resourceProperty.setCreateTime(new Date());
            resourceProperty.setModifyTime(new Date());
            resourceService.createResourceProperty(resource.getTarget(), resourceProperty.getName(), resourceProperty.getType());
            this.save(resourceProperty);

            //其他检索配置的初始化
            List<SearchConfigDTO> params = initParams(resourceProperty);
            try {
                searchConfigService.searchConfig(resourceProperty.getProjectId(), params);
            } catch (Exception ex) {
                log.error("search config init failed : {}", ex.getMessage());
                throw new RuntimeException("search config init failed");
            }
            return new ResponseEntity(HttpStatus.OK);
        }
        // 修改属性
        if (null != sameName && !sameName.getId().equals(resourcePropertyDTO.getId())) {
            throw new BadRequestException("已有重名属性!");
        }
        ResourceProperty resourceProperty = this.getById(resourcePropertyDTO.getId());
        if (null == resourceProperty) {
            throw new EntityNotFoundException(ResourceProperty.class, "id", resourcePropertyDTO.getId());
        }
        String oldProperty = resourceProperty.getName();
        BeanUtils.copyProperties(resourcePropertyDTO, resourceProperty);
        //修改时间
        resourceProperty.setModifyTime(new Date());
        resourceService.updateResourceProperty(resource.getTarget(), oldProperty, resourceProperty.getName(), resourceProperty.getType());
        this.updateById(resourceProperty);
        return new ResponseEntity(HttpStatus.OK);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResponseEntity deleteResourceProperty(Long resourceId, Long id) {
        // 查资源
        Resource resource = resourceService.getById(resourceId);
        if (null == resource) {
            throw new EntityNotFoundException(Resource.class, "id", resourceId);
        }
        ResourceProperty resourceProperty = this.getById(id);
        if (null == resourceProperty) {
            throw new EntityNotFoundException(ResourceProperty.class, "id", id);
        }
        resourceService.deleteResourceProperty(resource.getTarget(), resourceProperty.getName());
        this.removeById(id);
        return new ResponseEntity(HttpStatus.OK);
    }

    /**
     * 初始化属性字段配置
     * @param resourceProperty
     * @return
     */
    private List<SearchConfigDTO> initParams(ResourceProperty resourceProperty) {
        String name = resourceProperty.getName();
        List<SearchConfigDTO> searchConfigDTOS = Lists.newArrayList();
        // 检索结果页配置
        List<SearchFieldsDTO> fields = Lists.newArrayList();
        fields.add(new SearchFieldsDTO().setName(name).setLabel(name).setMaxWords(15).setOrderNumber(1));
        // 统计精炼配置
        List<StatisticsFieldsDTO> statistics = Lists.newArrayList();
        statistics.add(new StatisticsFieldsDTO().setField(name).setTitle(name).setSize(15).setType(0));
        // 详情页配置
        List<DetailFieldsDTO> details = Lists.newArrayList();
        details.add(new DetailFieldsDTO().setName(name).setLabel(name).setOrderNumber(1));
        searchConfigDTOS.add(new SearchConfigDTO()
                .setResourceId(resourceProperty.getResourceId())
                .setFields(fields)
                .setStatistics(statistics)
                .setDetails(details));
        return searchConfigDTOS;
    }
}
