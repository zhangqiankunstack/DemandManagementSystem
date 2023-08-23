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

    private List<ValueHistoryModel> valueHistoryModelList;

    private List<RelationshipHistoryModel> links;

    private List<EntityHistoryModel> entityHistoryModelList;

    private List<AttributeHistoryModel> attributeHistoryModelList;
}
