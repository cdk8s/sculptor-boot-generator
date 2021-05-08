package com.cdk8s.code.gen.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@NoArgsConstructor
@Data
public class ResponseBaseEntity implements Serializable {

	private int code;
	private boolean isSuccess;
	private String msg;
	private long timestamp;
	private Object data;
}
