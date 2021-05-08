package com.cdk8s.code.gen.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;


@NoArgsConstructor
@Data
public class ResponsePageBaseEntity implements Serializable {
	private int code;
	private boolean isSuccess;
	private String msg;
	private long timestamp;
	private DataBean data;

	@NoArgsConstructor
	@Data
	public static class DataBean {
		private int total;
		private int pageNum;
		private int pageSize;
		private int size;
		private int startRow;
		private int endRow;
		private int pages;
		private int prePage;
		private int nextPage;
		private boolean isFirstPage;
		private boolean isLastPage;
		private boolean hasPreviousPage;
		private boolean hasNextPage;
		private int navigatePages;
		private int navigateFirstPage;
		private int navigateLastPage;
		private List<Object> list;
		private List<Integer> navigatepageNums;

		public boolean getIsFirstPage() {
			return isFirstPage;
		}

		public void setIsFirstPage(boolean isFirstPage) {
			isFirstPage = isFirstPage;
		}

		public boolean getIsLastPage() {
			return isLastPage;
		}

		public void setIsLastPage(boolean isLastPage) {
			isLastPage = isLastPage;
		}
	}
}
