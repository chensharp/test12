package com.chensharp.tools;


/**
 * 消费节点信息
 * @author chen
 *
 */
public class consumeNode {
	private int node_id = -1;
	private int linking_node = -1;
	private int bandwidth_cost = -1;
	
	public int getNode_id() {
		return node_id;
	}
	public void setNode_id(int node_id) {
		this.node_id = node_id;
	}
	public int getLinking_node() {
		return linking_node;
	}
	public void setLinking_node(int linking_node) {
		this.linking_node = linking_node;
	}
	public int getBandwidth_cost() {
		return bandwidth_cost;
	}
	public void setBandwidth_cost(int bandwidth_cost) {
		this.bandwidth_cost = bandwidth_cost;
	}
	
	public void print(){
		System.out.println("node_id="+node_id+" linking_node="+linking_node+"  bandwidth_cost="+bandwidth_cost);
	}

}

//0 8 40 // 注：消费节点0，相连网络节点ID为8，视频带宽消耗需求为40 
//1 11 13
//2 22 28
//3 3 45
//4 17 11*/