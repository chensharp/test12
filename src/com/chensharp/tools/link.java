package com.chensharp.tools;

/**
 * 保存单条链路信息
 * @author chen
 *
 */
public class link {

	private int link_id = -1;
	private int start_node = -1;
	private int end_node = -1;
	private int bandwidth = -1;
	private int rent_cost = -1;
	
	public link() {
	}
	
	public int getEnd_node() {
		return end_node;
	}
	public void setEnd_node(int end_node) {
		this.end_node = end_node;
	}
	public int getStart_node() {
		return start_node;
	}
	public void setStart_node(int start_node) {
		this.start_node = start_node;
	}
	public int getBandwidth() {
		return bandwidth;
	}
	public void setBandwidth(int bandwidth) {
		this.bandwidth = bandwidth;
	}
	public int getRent_cost() {
		return rent_cost;
	}
	public void setRent_cost(int rent_cost) {
		this.rent_cost = rent_cost;
	}
	
	public int getLink_id() {
		return link_id;
	}
	public void setLink_id(int link_id) {
		this.link_id = link_id;
	}
	
	public void print(){
		System.out.println("link_id="+link_id+" startnode="+start_node+" endnode="+end_node+"  bandwidth="+bandwidth+" rent_cost="+rent_cost);
	}
	
}

//0 16 8 2 // 注：链路起始节点为0，链路终止节点为16 ，总带宽为8，单位网络租用费为2
//0 26 13 2
//0 9 14 2
//0 8 36 2