package com.chensharp.tools;

import java.util.ArrayList;

/**
 * 一个解
 * @author chen
 *
 */
public class solution {
	public int solutionID;//解的ID
	public ArrayList<Node> route = new ArrayList<Node>();//保存路径中节点id
	public int Bandwidth;//提供带宽
	public boolean finished;//标记是否完成
	public int route_index;//路径计数，给节点序号。
	
	public solution(int sID) {
		// TODO Auto-generated constructor stub
		route_index = 0;
		finished = false;
		solutionID = sID;
	}
	public void addNode(Node _node){
		
		_node.index = route_index;
		route_index++;
		route.add(_node);
	}
}
