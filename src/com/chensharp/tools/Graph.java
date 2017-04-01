package com.chensharp.tools;



/**
 * 图
 * @author chen
 *
 */
public class Graph {
	public int[][] matcost;
	public int[][] matbw;
	public int lengths;
	
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
