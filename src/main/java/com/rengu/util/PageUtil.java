package com.rengu.util;

import org.springframework.stereotype.Component;

import java.util.Map;
@Component
public class PageUtil {

    // pageNumber: 当前页
    // pageSize: 每页展示条数

    /**
     * 分页
     * @param requestParams 传入的分页参数
     * @return
     */
    public Map<String,Object> separatePage(Map<String,Object> requestParams){
        // 当前页 如果为空默认展示第一页
        Integer pageNumber = requestParams.get("pageNumber") == null ? 1 : Integer.parseInt(requestParams.get("pageNumber").toString());
        // 每页展示数据 如果为空默认每页10条
        Integer pageSize = requestParams.get("pageSize") == null ? 10 : Integer.parseInt(requestParams.get("pageSize").toString());
        pageSize = null == pageSize ? 10 : pageSize;
        // 计算开始页
        int startPage = (pageNumber - 1) * pageSize;
        // 总条数
        int totalCount = Integer.parseInt(requestParams.get("totalCount")+"");
        // 总页数
        int totalPage = (totalCount + pageSize - 1) / pageSize;
        // 上一页
        int prev = pageNumber <= 1 ? 1 : pageNumber - 1;
        // 下一页
        int next = pageNumber >= totalPage ? totalPage : pageNumber + 1;

        // 将参数存入Map
        requestParams.put("pageNumber", pageNumber);     // 当前页
        requestParams.put("pageSize", pageSize);     // 每页展示条数
        requestParams.put("startPage", startPage);   // 开始页
        requestParams.put("totalCount", totalCount); // 总条数
        requestParams.put("totalPage", totalPage);   // 总页数
        requestParams.put("prev", prev);             // 上一页
        requestParams.put("next", next);             // 下一页
        return requestParams;
    }
}
