package com.rengu.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.rengu.entity.*;
import com.rengu.entity.vo.FourClasses;
import com.rengu.entity.vo.ReviewVo;
import com.rengu.entity.vo.ValueAttribute;
import com.rengu.service.CriterionService;
import com.rengu.service.OpinionService;
import com.rengu.service.PersonnelService;
import com.rengu.service.ReviewService;
import com.rengu.util.ListPageUtil;
import com.rengu.util.ResultUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @ClassName ReviewController
 * @Description 流程表控制器
 * @Author zj
 * @Date 2023/08/08 13:30
 **/
@RestController
@RequestMapping("/review-model")
@Api(value = "ReviewController", tags = {"流程表控制器"})
	public class ReviewController {

	@Autowired
	public ReviewService reviewModelService;
	@Autowired
	private CriterionService criterionService;

	@Autowired
	private PersonnelService personnelService;

	@Autowired
	private OpinionService opinionService;

	@RequestMapping(value = "list", method = RequestMethod.GET)
	@ApiOperation(value = "展示列表")
	@ApiImplicitParams({
			@ApiImplicitParam(value = "页码", name = "pageNum", dataType = "Integer", required = false, example = "1", defaultValue = "1"),
			@ApiImplicitParam(value = "每页条数", name = "pageSize", dataType = "Integer", required = false, example = "10", defaultValue = "10")
	})
	public IPage<ReviewModel> get(@RequestParam(value = "pageNum") Integer pageNum, @RequestParam(value = "pageSize") Integer pageSize) {
		return reviewModelService.page(new Page<>(pageNum, pageSize));

	}

	@RequestMapping(value = "queryById", method = RequestMethod.GET)
	@ApiOperation(value = "根据Id展示列表")
	public ReviewModel get(String Id) {
		return reviewModelService.getById(Id);
	}

	@RequestMapping(value = "remove", method = RequestMethod.GET)
	@ApiOperation(value = "移除")
	public boolean remove(String Id) {
		return reviewModelService.removeById(Id);
	}

	@RequestMapping(value = "saveOrUpdate", method = RequestMethod.POST)
	@ApiOperation(value = "保存或更新")
	public boolean saveOrUpdate(@RequestBody ReviewModel reviewModel) {
		return reviewModelService.saveOrUpdate(reviewModel);
	}




	@RequestMapping(value = "save", method = RequestMethod.POST)
	@ApiOperation(value = "只保存评审")
	public Result save(@RequestBody ReviewVo reviewVo) {
		String name = reviewVo.getName();
		String sponsor =reviewVo.getSponsor();
		List<String> ids = reviewVo.getIds();

		return ResultUtils.build(reviewModelService.add(name,sponsor,ids));

	}






	@ApiOperation(value = "分页组合查询流程表")
	@GetMapping("/test-list")
	public Result testOnline(@RequestParam Integer pageNumber,
							 @RequestParam Integer pageSize,
							 @RequestParam(required = false) String name,
							 @RequestParam(required = false) String type,
							 @RequestParam(required = false) Integer status) {
		List<ReviewModel> list = reviewModelService.findBy(name,type,status);
		Map<String, Object> requestParams = new HashMap<>();
		requestParams.put("pageNumber",pageNumber);
		requestParams.put("pageSize",pageSize);
		new ListPageUtil<ReviewModel>().separatePageList(list,requestParams);
		return ResultUtils.build(requestParams);
	}



	@PostMapping("/updateStatus")
	@ApiOperation(value = "修改评审状态")
	public Result updateStatus(@RequestParam Integer id,@RequestParam Integer status) {

		return ResultUtils.build(reviewModelService.updateStatusById(id, status));

	}


	@PostMapping("/audit")
	@ApiOperation(value = "点击提交保存评审")
	public Result audit(@RequestBody ReviewVo reviewVo) {
		String name = reviewVo.getName();
		String sponsor =reviewVo.getSponsor();
		List<String> ids = reviewVo.getIds();

		return ResultUtils.build(reviewModelService.findEntitiesWithoutRelationship(ids,name,sponsor));

	}



	@ApiOperation(value = "分页组合查询开始评审的entity")
	@GetMapping("/start")
	public Result start(@RequestParam Integer pageNumber,
							 @RequestParam Integer pageSize,
							 @RequestParam Integer id) {
		List<EntityModel> list = reviewModelService.findStart(id);
		Map<String, Object> requestParams = new HashMap<>();
		requestParams.put("pageNumber",pageNumber);
		requestParams.put("pageSize",pageSize);
		new ListPageUtil<EntityModel>().separatePageList(list,requestParams);
		return ResultUtils.build(requestParams);
	}



	@ApiOperation(value = "分页组合查询开始评审的entity的属性以及对应的值")
	@GetMapping("/findByEntityId")
	public Result findByEntityId(@RequestParam Integer pageNumber,
						@RequestParam Integer pageSize,
						@RequestParam String ids,
						@RequestParam(required = false) String entityName,
						@RequestParam(required = false) String entityType) {
		List<ValueAttribute> list = reviewModelService.getAttributeAndValue(ids, entityName, entityType);
		Map<String, Object> requestParams = new HashMap<>();
		requestParams.put("pageNumber",pageNumber);
		requestParams.put("pageSize",pageSize);
		new ListPageUtil<ValueAttribute>().separatePageList(list,requestParams);
		return ResultUtils.build(requestParams);
	}



	@ApiOperation(value = "四个不同的集合")
	@GetMapping("/findFour")
	public Result findFour(Integer id) {

		FourClasses fourClasses = new FourClasses();
		List<CriterionModel> listC = criterionService.list();
		List<PersonnelModel> listP = personnelService.list();
		ReviewModel reviewModelServiceById = reviewModelService.getById(id);
		List<OpinionModel> opinionServiceByReviewId = opinionService.findByReviewId(id);

		fourClasses.setCriterionModelList(listC);
		fourClasses.setPersonnelModelList(listP);
		fourClasses.setReviewModelList(reviewModelServiceById);
		fourClasses.setOpinionModelList(opinionServiceByReviewId);

		return ResultUtils.build(fourClasses);
	}


	@PostMapping("/saveStatusAndReview")
	@ApiOperation(value = "点击保存修改状态，批量增加")
	public Result saveStatusAndReview(@RequestParam Integer id,@RequestParam Integer status,@RequestBody List<OpinionModel> opinionList) {
//		reviewModelService.updateStatusById(id, status);
//		opinionService.batchInsert(opinionList);
		reviewModelService.saveStatusAndReview(id,status,opinionList);
		return ResultUtils.build("ok");

	}




}