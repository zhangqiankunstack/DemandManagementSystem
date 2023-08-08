package com.rengu.util;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ListPageUtil<T> {

    /**
     * 对List进行分页
     * @param requestParams
     * @return
     */
    public Map<String,Object> separatePageList(List<T> dataList, Map<String,Object> requestParams){
        // 当前页
        Integer pageNumber = requestParams.get("pageNumber") == null ? 1 : Integer.parseInt(requestParams.get("pageNumber").toString());
        // 每页展示数据
        Integer pageSize = requestParams.get("pageSize") == null ? 10 : Integer.parseInt(requestParams.get("pageSize").toString());

        // 总页数
        int totalPage = (dataList.size() + pageSize - 1) / pageSize;
        // 上一页
        int prev = pageNumber <= 1 ? 1 : pageNumber - 1;
        // 下一页
        int next = pageNumber >= totalPage ? totalPage : pageNumber + 1;
        // 获取每页展示条数
        List<Object> pageList = dataList.stream().skip((pageNumber-1)*pageSize).limit(pageSize).collect(Collectors.toList());
        requestParams.put("pageNumber", pageNumber);     // 当前页
        requestParams.put("pageSize", pageSize);     // 每页展示条数
        requestParams.put("totalCount", dataList.size()); // 总条数
        requestParams.put("totalPage", totalPage);   // 总页数
        requestParams.put("prev", prev);             // 上一页
        requestParams.put("next", next);             // 下一页
        requestParams.put("resultData", pageList);
        return requestParams;
    }

    //PageHelper.startPage(Integer.parseInt(requestParams.get("pageNumber") == null ? "1" : requestParams.get("pageNumber") + ""),Integer.parseInt(requestParams.get("pageSize") == null ? "10" : requestParams.get("pageSize") + ""));
}
