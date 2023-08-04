package com.rengu.service.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rengu.entity.*;
import com.rengu.entity.vo.EntityRelationship;
import com.rengu.entity.vo.ValueAttribute;
import com.rengu.mapper.EntityMapper;
import com.rengu.mapper.HostInfoMapper;
import com.rengu.mapper.ValueAttributeMapper;
import com.rengu.service.*;
import com.rengu.util.RedisTemplateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;

/**
 * @ClassName HostInfoServiceImpl
 * @Description 数据库表服务接口实现
 * @Author zj
 * @Date 2023/08/02 18:02
 **/
@Service
		public class HostInfoServiceImpl extends ServiceImpl<HostInfoMapper, HostInfoModel> implements HostInfoService {

	@Autowired
	private EntityService entityService;
	@Autowired
	private ValueAttributeMapper valueAttributeMapper;
	@Autowired
	private EntityMapper entityMapper;
	@Autowired
	private  RedisTemplateUtil redisTemplateUtil;
	@Autowired
	private RelationshipService relationshipService;
	@Autowired
	private ValueService valueService;
	@Autowired
	private AttributeService attributeService;

	@Override
	public boolean databaseTest(HostInfoModel hostInfo) {
		String databaseUrl = "jdbc:mysql://" + hostInfo.getHostIp() + ":" + hostInfo.getPort() + "/" + hostInfo.getNewDatabase() + "?serverTimezone=GMT";
		try (Connection connection = DriverManager.getConnection(databaseUrl, hostInfo.getUsername(), hostInfo.getPassword())) {
			if (connection != null) {
				System.out.println("Database connection successful!");

				// 执行其他数据库操作
				// ...
				connection.close();
				return true;
			}
		} catch (SQLException e) {
			System.out.println("Database connection error: " + e.getMessage());
		}
		return false;
	}


	/**
	 * 连接后查询
	 *
	 * @param hostInfo
	 * @return
	 */
	@Override
	public List<EntityModel> connect(HostInfoModel hostInfo) {
		String databaseUrl = "jdbc:mysql://" + hostInfo.getHostIp() + ":" + hostInfo.getPort() + "/" + hostInfo.getNewDatabase() + "?serverTimezone=GMT";
		try (Connection connection = DriverManager.getConnection(databaseUrl, hostInfo.getUsername(), hostInfo.getPassword())) {
			if (connection != null) {
				System.out.println("连接成功");

				// 执行其他数据库操作
				// ...
				List<EntityModel> list = entityService.list();

				System.out.println(list);
				return list;


			}
		} catch (SQLException e) {
			System.out.println("Database connection error: " + e.getMessage());
		}

		return null;
	}

	/**
	 * 查询数据
	 *
	 * @param entityId
	 * @return
	 */
	@Override
	public List<ValueAttribute> findValueAttributesByEntityId(String entityId) {
		List<ValueAttribute> valueAttributeList = valueAttributeMapper.findValueAttributesByEntityId(entityId);

		return valueAttributeList;

	}

	@Override
	public List<EntityRelationship> getEntityRelationships(String entityId1) {
		List<EntityRelationship> list = entityMapper.getEntityRelationships(entityId1);

			boolean entityRelationship = redisTemplateUtil.lSet("entityRelationship", list, 6000);

			return list;

	}

	@Override
	public void insertAll(String id) {
		List<EntityModel> list = entityService.list();
		for (EntityModel entityModel :list) {
			if (entityModel.getEntityId()==id){
				entityService.save(entityModel);

			}
		}
		List<RelationshipModel> list1 = relationshipService.list();
		for (RelationshipModel relationshipModel :list1) {
			if (relationshipModel.getEntityId1()==id){
				relationshipService.save(relationshipModel);

			}
		}
		List<ValueModel> list2 = valueService.list();
		for (ValueModel valueModel :list2) {
			if (valueModel.getEntityId()==id){
				valueService.save(valueModel);

			}
		}
		List<AttributeModel> list3 = attributeService.list();
		for (AttributeModel attributeModel :list3) {
			if (attributeModel.getAttributeId()==id){
				attributeService.save(attributeModel);
//这里的添加不符合
			}
		}

	}
}