package com.rengu.controller;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.metadata.PageList;
import com.baomidou.mybatisplus.extension.api.R;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import com.rengu.entity.EntityModel;
import com.rengu.entity.HostInfoModel;
import com.rengu.entity.vo.EntityRelationship;
import com.rengu.entity.vo.RR;
import com.rengu.entity.vo.ValueAttribute;
import com.rengu.mapper.EntityMapper;
import com.rengu.service.EntityService;
import com.rengu.service.HostInfoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName HostInfoController
 * @Description 数据库表控制器
 * @Author zj
 * @Date 2023/08/02 18:02
 **/
@RestController
@RequestMapping("/host-info-model")
@Api(value = "HostInfoController", tags = {"数据库表控制器"})
	public class HostInfoController {

@Autowired
public HostInfoService hostInfoModelService;
@Autowired
private EntityService entityService;

@RequestMapping(value = "list", method = RequestMethod.GET)
@ApiOperation(value = "展示列表")
@ApiImplicitParams({
		@ApiImplicitParam(value = "页码", name = "pageNum", dataType = "Integer", required = false,example = "1",defaultValue = "1"),
		@ApiImplicitParam(value = "每页条数", name = "pageSize", dataType = "Integer", required = false,example = "10",defaultValue = "10")
})
public RR get(@RequestParam(value = "pageNum") Integer pageNum, @RequestParam(value = "pageSize") Integer pageSize){

		return	RR.success(hostInfoModelService.page(new Page<>(pageNum, pageSize)));

		}
@RequestMapping(value = "queryById", method = RequestMethod.GET)
@ApiOperation(value = "根据Id展示列表")
public RR get(String Id){
		return RR.success(hostInfoModelService.getById(Id));
		}
@RequestMapping(value = "remove", method = RequestMethod.GET)
@ApiOperation(value = "移除")
public RR remove(String Id){
		return RR.success(hostInfoModelService.removeById(Id));
		}
@RequestMapping(value = "saveOrUpdate", method = RequestMethod.POST)
@ApiOperation(value = "保存或更新")
public RR saveOrUpdate(@RequestBody HostInfoModel hostInfoModel){
		return RR.success(hostInfoModelService.saveOrUpdate(hostInfoModel));
		}


	@ApiOperation(value = "测试连接", notes = "测试")
	@PostMapping("/dataTest")
	public RR dataTest(@RequestBody HostInfoModel hostInfo){
		if (hostInfo.getStatus()==1){
			hostInfoModelService.databaseTest(hostInfo);

		}
		if (hostInfo.getStatus()==2){

		}
		return RR.success(hostInfoModelService.databaseTest(hostInfo));
	}

	/**
	 * 分页函数
	 * @param currentPage   当前页数
	 * @param pageSize  每一页的数据条数
//	 * @param list  要进行分页的数据列表
	 * @return  当前页要展示的数据
	 */
	@ApiOperation(value = "连接后分页查询集合", notes = "查询集合")
	@PostMapping("/test-online")
	public RR testOnline(@RequestBody HostInfoModel hostInfo,@RequestParam Integer currentPage, @RequestParam Integer pageSize) {



		List<EntityModel> list = hostInfoModelService.connect(hostInfo);
		if (list==null){
			return null;
		}

//        Entity entity = new Entity();
//
//        List<Entity>  list = iEntityService.selectEntityList(entity);
//        String key =  "entity";
//        redisCache.setCacheList(key,list);

		Page page = new Page();
		int size = list.size();

		if(pageSize > size) {
			pageSize = size;
		}

		// 求出最大页数，防止currentPage越界
		int maxPage = size % pageSize == 0 ? size / pageSize : size / pageSize + 1;

		if(currentPage > maxPage) {
			currentPage = maxPage;
		}

		// 当前页第一条数据的下标
		int curIdx = currentPage > 1 ? (currentPage - 1) * pageSize : 0;

		List pageList = new ArrayList();

		// 将当前页的数据放进pageList
		for(int i = 0; i < pageSize && curIdx + i < size; i++) {
			pageList.add(list.get(curIdx + i));
		}

		page.setCurrent(currentPage).setSize(pageSize).setTotal(list.size()).setRecords(pageList);
		return RR.success(page);



	}

	@ApiOperation(value = "查实体属性", notes = "查实体属性")
	@PostMapping("/findById/{entityId}")
	public R findValue(@PathVariable String entityId){

		List<ValueAttribute> list = hostInfoModelService.findValueAttributesByEntityId(entityId);
		return R.ok(list);
	}

	/**
	 * 查关联属性
	 * @param entityId1
	 * @return
	 */
//	@ApiOperation(value = "查关联实体", notes = "查关联实体")
//	@PostMapping("/entity/{entityId1}")
//	public R getEntityId2ByEntityName(@PathVariable String entityId1) {
//
//		List<EntityRelationship> list = hostInfoModelService.getEntityRelationships(entityId1);
//		return R(list);
//
//	}

		}

