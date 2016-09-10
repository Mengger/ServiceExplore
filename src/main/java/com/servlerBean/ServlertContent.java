package com.servlerBean;

import java.util.List;

import com.pjo.web.FilterRecord;
import com.pjo.web.ListenerRecord;
import com.pjo.web.ServletRecord;

public class ServlertContent {
	
	private List<FilterRecord> filterList;
	private List<ListenerRecord> listenerList;
	private List<ServletRecord> servletList;
	public List<FilterRecord> getFilterList() {
		return filterList;
	}
	public void setFilterList(List<FilterRecord> filterList) {
		this.filterList = filterList;
	}
	public List<ListenerRecord> getListenerList() {
		return listenerList;
	}
	public void setListenerList(List<ListenerRecord> listenerList) {
		this.listenerList = listenerList;
	}
	public List<ServletRecord> getServletList() {
		return servletList;
	}
	public void setServletList(List<ServletRecord> servletList) {
		this.servletList = servletList;
	}
	
	
}
