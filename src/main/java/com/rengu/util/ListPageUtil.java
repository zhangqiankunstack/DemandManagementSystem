package com.rengu.util;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ListPageUtil<T> {

    /**
     * 对List进行分页
     *
     * @param requestParams
     * @return
     */
    public Map<String, Object> separatePageList(List<T> dataList, Map<String, Object> requestParams) {
        Integer pageNumber = requestParams.get("pageNumber") == null ? 1 : Integer.parseInt(requestParams.get("pageNumber").toString());
        Integer pageSize = requestParams.get("pageSize") == null ? 10 : Integer.parseInt(requestParams.get("pageSize").toString());
        int totalPage = (dataList.size() + pageSize - 1) / pageSize;
        int prev = pageNumber <= 1 ? 1 : pageNumber - 1;
        int next = pageNumber >= totalPage ? totalPage : pageNumber + 1;
        List<Object> pageList = dataList.stream().skip((pageNumber - 1) * pageSize).limit(pageSize).collect(Collectors.toList());
        requestParams.put("pageNumber", pageNumber);
        requestParams.put("pageSize", pageSize);
        requestParams.put("totalCount", dataList.size());
        requestParams.put("totalPage", totalPage);
        requestParams.put("prev", prev);
        requestParams.put("next", next);
        requestParams.put("resultData", pageList);
        return requestParams;
    }
    //PageHelper.startPage(Integer.parseInt(requestParams.get("pageNumber") == null ? "1" : requestParams.get("pageNumber") + ""),Integer.parseInt(requestParams.get("pageSize") == null ? "10" : requestParams.get("pageSize") + ""));
}
