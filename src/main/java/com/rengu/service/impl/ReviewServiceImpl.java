package com.rengu.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rengu.entity.*;
import com.rengu.entity.vo.EntityQueryVo;
import com.rengu.entity.vo.FourClasses;
import com.rengu.entity.vo.ValueAttribute;
import com.rengu.mapper.*;
import com.rengu.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * @ClassName ReviewServiceImpl
 * @Description 流程表服务接口实现
 * @Author zj
 * @Date 2023/08/08 13:30
 **/
@Service
		public class ReviewServiceImpl extends ServiceImpl<ReviewMapper, ReviewModel>implements ReviewService {

	@Autowired
	private EntityHistoryMapper entityHistoryMapper;
	@Autowired
	private OpinionService opinionService;
	@Autowired
	private RelationshipMapper relationshipMapper;
	@Autowired
	private EntityMapper entityMapper;
	@Autowired
	private ReviewEntityMapper reviewEntityMapper;
	@Autowired
	private ValueMapper valueMapper;
	@Autowired
	private EntityHistoryService entityHistoryService;
	@Autowired
	private EntityService entityService;
	@Autowired
	private AttributeHistoryService attributeHistoryService;
	@Autowired
	private ValueHistoryService valueHistoryService;
	@Autowired
	private RelationshipHistoryService relationshipHistoryService;
	/**
	 * 保存类型，看增量还是变更
	 * @param name
	 * @param sponsor
	 * @param list
	 * @return
	 */
	@Override
	public ReviewModel add(String name, String sponsor, List<String> list) {
		ReviewModel reviewModel = new ReviewModel();
		reviewModel.setName(name);
		reviewModel.setSponsor(sponsor);
		reviewModel.setStatus(0);

		List<EntityHistoryModel> entityHistoryModels = entityHistoryMapper.selectBatchIds(list);


		if (entityHistoryModels !=null){
			reviewModel.setType("变更");
		}
	    reviewModel.setType("增量");
		baseMapper.insert(reviewModel);
		return reviewModel;
	}

	/**
	 * 分页前的组合查询
	 * @param name
	 * @param type
	 * @param status
	 * @return
	 */
	@Override
	public List<ReviewModel> findBy(String name,String type,Integer status) {

		QueryWrapper<ReviewModel> queryWrapper = new QueryWrapper<>();
		if (name != null && !name.isEmpty()) {
			queryWrapper.like("name", name);
		}
		if (type != null && !type.isEmpty()) {
			queryWrapper.eq("type", type);
		}
		if (status != null ) {
			queryWrapper.eq("status", status);
		}

		return baseMapper.selectList(queryWrapper);

	}

	/**
	 * 审核
	 * @param ids
	 * @return
	 */
	@Override
	public Boolean goBy(List<String> ids) {

		QueryWrapper<RelationshipModel> queryWrapper = new QueryWrapper<>();
		queryWrapper.in("entity_id1", ids)
				.or()
				.in("entity_id2", ids);
		List<RelationshipModel> relationshipModelList = relationshipMapper.selectList(queryWrapper);
		if (relationshipModelList != null){
			return true;
		}




		List<EntityModel> entityModels = entityMapper.selectBatchIds(ids);
		for (EntityModel entityModel : entityModels) {
			if (entityModel.getEntityId().equals(ids)){

			}
		}


		return false;

	}

	@Override
	public Boolean updateStatusById(Integer id, Integer status) {
		ReviewModel reviewModel = new ReviewModel();
		reviewModel.setStatus(status);
		UpdateWrapper<ReviewModel> updateWrapper = new UpdateWrapper<>();
		updateWrapper.eq("id", id);

		int updateCount = baseMapper.update(reviewModel, updateWrapper);
		if (updateCount<0){
			return false;
		}
		return true;
	}


	/**
	 * 审核
	 * @param entityIds
	 * @param name
	 * @param sponsor
	 * @return
	 */

	@Override
	public Object findEntitiesWithoutRelationship(List<String> entityIds, String name, String sponsor) {
        // 查询与给定实体ID相关的关系列表
		List<RelationshipModel> relationshipList = relationshipMapper.getRelationshipsByEntityIds(entityIds);
		// 用于存储相关的实体ID

		List<String> entityS = new ArrayList<>();

		for (RelationshipModel relationship : relationshipList) {
			if (entityIds.contains(relationship.getEntityId1()) || entityIds.contains(relationship.getEntityId2())) {
				entityS.add(relationship.getEntityId1());
				entityS.add(relationship.getEntityId2());
			}
		}

//		Set<String> relatedEntityIds = new HashSet<>();
//
//		// 遍历关系列表，将相关的实体ID添加到relatedEntityIds集合中
//		for (RelationshipModel relationship : relationshipList) {
//			relatedEntityIds.add(relationship.getEntityId1());
//			relatedEntityIds.add(relationship.getEntityId2());
//		}

		List<String> entitiesWithoutRelationship = new ArrayList<>();


		// 判断是否存在关联关系
		boolean hasRelationship = true;
		for (String entityId : entityIds) {
			if (!entityS.contains(entityId)) {
				entitiesWithoutRelationship.add(entityId);
				// 如果实体ID不在relatedEntityIds集合中，则说明没有关联关系
				hasRelationship = false;

			}
		}

		if (hasRelationship) {
			// 存在关联关系，返回"成功"字符串

//			ReviewServiceImpl reviewService = new ReviewServiceImpl();
//			ReviewModel reviewModel  = reviewService.add(name, sponsor, entityIds);

			ReviewModel reviewModel  = s(name, sponsor, entityIds);

			for (String entityId : entityIds) {
				ReviewEntityModel reviewEntityModel = new ReviewEntityModel();
				reviewEntityModel.setReviewId(reviewModel.getId());
				reviewEntityModel.setEntityId(entityId);
				reviewEntityMapper.insert(reviewEntityModel);
			}
			Map<String, Object> resultMap = new HashMap<>();
			resultMap.put("entities", null);
			resultMap.put("hasRelationship", true);
			return resultMap;

		} else {
			// 不存在关联关系，查询对应的EntityModel实体列表并返回
			// 存在没有关联关系的实体，返回这些实体及关联关系状态为false
			List<EntityModel> entities = entityMapper.selectBatchIds(entitiesWithoutRelationship);

			Map<String, Object> resultMap = new HashMap<>();
			resultMap.put("entities", entities);
			resultMap.put("hasRelationship", false);
			return resultMap;
		}
	}






	private ReviewModel s(String name, String sponsor, List<String> list) {
		ReviewModel reviewModel = new ReviewModel();
		reviewModel.setName(name);
		reviewModel.setSponsor(sponsor);
		reviewModel.setStatus(0);

		List<EntityHistoryModel> entityHistoryModels = entityHistoryMapper.selectBatchIds(list);


		if (entityHistoryModels !=null){
			reviewModel.setType("变更");
		}
		reviewModel.setType("增量");
		baseMapper.insert(reviewModel);
		return reviewModel;
	}

//	----






	@Override
	public List<EntityModel> findStart(Integer id ) {

		List<String> entity_ids = reviewEntityMapper.selectByReviewId(id);

		List<EntityModel> entityModels = entityMapper.selectBatchIds(entity_ids);
		

		return entityModels;



	}

	@Override
	public List<ValueAttribute> getAttributeAndValue(String entityId,String attributeName, String value) {


		return valueMapper.getAttributeAndValue(entityId,attributeName,value);
	}

	@Override
	public List<FourClasses> giveSome() {
		return null;
	}

	@Override
	public Object saveStatusAndReview(Integer id, Integer status,List<OpinionModel> dataList) {
		ReviewModel reviewModel = new ReviewModel();
		reviewModel.setStatus(status);
		UpdateWrapper<ReviewModel> updateWrapper = new UpdateWrapper<>();
		updateWrapper.eq("id", id);

		int updateCount = baseMapper.update(reviewModel, updateWrapper);
		if (updateCount<0){
			return false;
		}
		opinionService.batchInsert(dataList);

		List<String> entity_ids = reviewEntityMapper.selectByReviewId(id);
		entityHistoryService.copyDataToEntityHistory(entity_ids);
		EntityQueryVo entityQueryVo = entityService.queryEntities(entity_ids);

		List<String> attributeIdList = entityQueryVo.getAttributeIdList();
		attributeHistoryService.copyDataToAttributeHistory(attributeIdList);

		List<String> valueIdList = entityQueryVo.getValueIdList();
		valueHistoryService.copyDataToValueHistory(valueIdList);

		List<String> relationshipIdList = entityQueryVo.getRelationshipIdList();
		relationshipHistoryService.copyDataToRelationshipHistory(relationshipIdList);

		return true;
	}


}
