package com.chensharp.tools;

import java.awt.RenderingHints.Key;
import java.util.HashMap;

/**
 * 图
 * @author chen
 *
 */
public class Graph {
	public int[][] matcost;
	public int[][] matbw;
	public int lengths;
	
	public HashMap<String, String> consumerBw = new HashMap<String,String>(); //存储key=nodeid , value=bwneed;
	
	public Graph(int length) {
		// TODO Auto-generated constructor stub
		matcost = new int[length][length];
		matbw = new int[length][length];
		lengths = length;
	}
	
	/**
	 * 深复制
	 * @param _graph
	 */
	public void reInit(Graph _graph) {
		matcost = arraysCopy(_graph.matcost);
		matbw = arraysCopy(_graph.matbw);
		lengths = matcost.length;
	}
	
	/**
	 * 初始化consumerBw
	 * @param _graph
	 */
	public void InitConsumer(DataOpt _dDataOpt) {
		int n = _dDataOpt.consumeNodes.size();
		int nodeid,bw;
		for (int i = 0; i < n; i++) {
			nodeid = _dDataOpt.consumeNodes.get(i).getLinking_node();
			bw = _dDataOpt.consumeNodes.get(i).getBandwidth_cost();
			consumerBw.put(String.valueOf(nodeid), String.valueOf(bw));
		}
	}
	
	/**
	 * 更新需求
	 * @param nodeid
	 * @param needbw
	 */
	public void UpdateConsumer(int nodeid , int needbw) {
		String str = String.valueOf(nodeid);
		consumerBw.remove(str);//移除该nodeid
		consumerBw.put(str, String.valueOf(needbw));
	}
	
	
	/**
	 * 拷贝数组二维
	 * @param newmat
	 * @return
	 */
	private int[][] arraysCopy(int[][] newmat) {
		int n = newmat.length;
		int[][] result= new int[n][n];
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < n; j++) {
				result[i][j] = newmat[i][j];
			}
		}
		return result;
	}
	
}
