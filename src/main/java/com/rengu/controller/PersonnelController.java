package com.rengu.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.rengu.entity.PersonnelModel;
import com.rengu.entity.vo.Result;
import com.rengu.service.PersonnelService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @ClassName PersonnelController
 * @Description 评审人员控制器
 * @Author zj
 * @Date 2023/08/04 09:41
 **/
@RestController
@RequestMapping("/personnel-model")
@Api(value = "PersonnelController", tags = {"评审人员控制器"})
public class PersonnelController {

    @Autowired
    public PersonnelService personnelModelService;

//@RequestMapping(value = "list", method = RequestMethod.GET)
//@ApiOperation(value = "展示列表")
//@ApiImplicitParams({
//		@ApiImplicitParam(value = "页码", name = "pageNum", dataType = "Integer", required = false,example = "1",defaultValue = "1"),
//		@ApiImplicitParam(value = "每页条数", name = "pageSize", dataType = "Integer", required = false,example = "10",defaultValue = "10")
//})
//public IPage<PersonnelModel> get(@RequestParam(value = "pageNum") Integer pageNum,@RequestParam(value = "pageSize") Integer pageSize){
//		return	personnelModelService.page(new Page<>(pageNum, pageSize));
//
//		}

    @RequestMapping(value = "queryById", method = RequestMethod.GET)
    @ApiOperation(value = "根据Id展示列表")
    public PersonnelModel get(String Id) {
        return personnelModelService.getById(Id);
    }

    @RequestMapping(value = "remove", method = RequestMethod.GET)
    @ApiOperation(value = "移除")
    public Result remove(String Id) {
        return Result.success(personnelModelService.removeById(Id));
    }

    @RequestMapping(value = "saveOrUpdate", method = RequestMethod.POST)
    @ApiOperation(value = "保存或更新")
    public Result saveOrUpdate(@RequestBody PersonnelModel personnelModel) {
        return Result.success(personnelModelService.saveOrUpdate(personnelModel));
    }


    @RequestMapping(value = "/page", method = RequestMethod.GET)
    @ApiOperation(value = "分页，名字模糊查询")
    public Result page(@RequestParam(value = "pageNum", required = true, defaultValue = "1") Integer pageNum,
                       @RequestParam(value = "pageSize", required = true, defaultValue = "2") Integer pageSize,
                       @RequestParam(value = "name", required = false) String name,
                       @RequestParam(value = "introduction", required = false) String introduction) {


        Page<PersonnelModel> page = personnelModelService.page(pageNum, pageSize, name, introduction);

        return Result.success(page);
    }


}
