package com.rengu.entity.vo;

import lombok.Data;

import java.util.List;

@Data
public class ReviewVo {

    private String name;

    private String sponsor;

    private List<String> ids;
}
