package com.cdk8s.code.gen.dto;

import lombok.Data;

import java.util.List;

@Data
public class EnumEntity {

	private String upperAttrName;
	private List<EnumItemEntity> enumItemEntityList;

}
