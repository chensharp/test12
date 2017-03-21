package com.chensharp.tools;

import java.util.ArrayList;  
import java.util.List;  
/**
 * 数据操作类
 * @author chen
 */
public class DataOpt {

	private int netNode_num = -1;//网络节点数目
	private int link_num = -1;//链路数目
	private int consumeNode_num = -1;//消费节点
	private int deploy_cost = -1;//服务器部署成本
	public  List<link> links = new ArrayList<link>();//链路集合
	public  List<consumeNode> consumeNodes = new ArrayList<consumeNode>();//消费节点集合
	
	private int links_count=0;//用来统计总数，分配link的id
	
	public DataOpt() {
		links_count=0;
		
	}
	
	
	public int getNetNode_num() {
		return netNode_num;
	}
	public void setNetNode_num(int netNode_num) {
		this.netNode_num = netNode_num;
	}
	public int getLink_num() {
		return link_num;
	}
	public void setLink_num(int link_num) {
		this.link_num = link_num;
	}
	public int getConsumeNode_num() {
		return consumeNode_num;
	}
	public void setConsumeNode_num(int consumeNode_num) {
		this.consumeNode_num = consumeNode_num;
	}
	public int getDeploy_cost() {
		return deploy_cost;
	}
	public void setDeploy_cost(int deploy_cost) {
		this.deploy_cost = deploy_cost;
	}
	
	
	/*
	 * 插入一个link
	 */
	public void addLink(link _link) {
		_link.setLink_id(links_count);
		links_count++;
		this.links.add(_link);
	}
	
	/*
	 * 插入一个consumeNode
	 */
	public void addCosumeNode(consumeNode _consumeNode) {
		this.consumeNodes.add(_consumeNode);
	}

	public void print(){
		System.out.println("netNode_num="+netNode_num+" link_num="+link_num+"  consumeNode_num="+consumeNode_num+" deploy_cost="+deploy_cost);
		for(int i = 0; i < links.size(); i++)  
        {  
            links.get(i).print();   
        }  
		for(int i = 0; i < consumeNodes.size(); i++)  
        {  
			consumeNodes.get(i).print();   
        }  
		
		
	}
	
	
	
}

/*
输入文件示例（参照第一节中的用例）：
28 45 12 // 注：28个网络节点，45条链路，12个消费节点

100 // 注：服务器部署成本为100

0 16 8 2 // 注：链路起始节点为0，链路终止节点为16 ，总带宽为8，单位网络租用费为2
0 26 13 2
0 9 14 2
0 8 36 2
(以下省略若干行网络节点信息)
0 8 40 // 注：消费节点0，相连网络节点ID为8，视频带宽消耗需求为40 
1 11 13
2 22 28
3 3 45
4 17 11*/