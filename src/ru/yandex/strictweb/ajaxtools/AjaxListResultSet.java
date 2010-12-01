package ru.yandex.strictweb.ajaxtools;

import java.util.List;

import ru.yandex.strictweb.ajaxtools.annotation.Presentable;

@Presentable
public class AjaxListResultSet<E> {
	List<E> list;
	long count;
	boolean searchable;
	
	public AjaxListResultSet(List list, long count) {
		this.list = list;
		this.count = count;
		this.searchable = false;
	}

	public AjaxListResultSet(List list, long count, boolean searchable) {
		this.list = list;
		this.count = count;
		this.searchable = searchable;
	}
	
	public long getCount() {
		return count;
	}

	public void setCount(long count) {
		this.count = count;
	}

	public List<E> getList() {
		return list;
	}

	public void setList(List list) {
		this.list = list;
	}

	public boolean getSearchable() {
		return searchable;
	}

	public void setSearchable(boolean searchable) {
		this.searchable = searchable;
	}
}
