package com.rengu.entity.vo;

import com.rengu.entity.AttributeHistoryModel;
import com.rengu.entity.EntityHistoryModel;
import com.rengu.entity.RelationshipHistoryModel;
import com.rengu.entity.ValueHistoryModel;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Data
@Getter
@Setter
public class ToJson {

    private List<EntityHistoryModel> entityHistorys;

    private List<AttributeHistoryModel> attributeHistory;

    private List<ValueHistoryModel> valueHistory;


    private List<RelationshipHistoryModel> links;



}
