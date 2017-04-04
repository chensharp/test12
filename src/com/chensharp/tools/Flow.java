package com.chensharp.tools;



/**
 * 流，一条流，
 * @author chen
 *
 */
public class Flow {
	final  int INF_INT= 100000; //无穷大 
	
	
	public int start_id;//服务节点                                    自动赋值
	public int end_id;//消费节点相连接的普通节点id   自动赋值
	
	public int consumer_id;//消费节点的id，0,1,2,3,4  手动赋值  ！！！！！！！！！
	
	public int sumcost;//总费用                      调用函数赋值
	public int bandwidth;//提供的带宽            调用函数赋值
	
	private String toStringpath;//转化为string版路径，
	private int[] path;//下标路径 
	
	public void printInfo() {
		System.out.println("flow= "+toStringpath+" cost="+sumcost+" bandwidth="+bandwidth);
	}
	
	
	/*
	 * 构造函数
	 */
	public Flow(String _path) {
		toStringpath = _path;
		String[] strings = _path.split("-");
		path = new int[strings.length];
		for (int i = 0; i < strings.length; i++) {
			path[i] = Integer.parseInt(strings[i]);
		}
		start_id = path[0];
		end_id = path[strings.length-1];
		
		sumcost =0;
		bandwidth =0;
		
	}
	
	public Flow(int[] _path) {
		int n = _path.length;
		toStringpath = new String();
		toStringpath = String.valueOf(_path[0]);
		for (int i = 1; i < n; i++) {
			toStringpath= toStringpath + "-" + String.valueOf(_path[i]);
		}
		start_id = _path[0];
		end_id = _path[_path.length-1];
		
		sumcost =0;
		bandwidth =0;
	}
	
	public String toStringPath () { return toStringpath; }//获得路径
	public int[]  toIntPath() { return path;  }
	
	/**
	 * 计算路径的cost
	 * @param graph 要求是原图，未经修改的，
	 * @return
	 */
	public int calculaCost( Graph _graph) {
		int minbw = GetminBw( _graph);  //传原图进去，
		//System.out.println("minbw = "+minbw);
		int node1,node2,cost;
		int sum = 0;
		for(int i=0;i<path.length-1;i++){
			node1 = path[i];
			node2 = path[i+1];
			cost = _graph.matcost[node1][node2];
			sum +=( cost * minbw ); 
		}
		//System.out.println("sum = "+sum);
		sumcost = sum;
		return sum;
	}
	
	/**
	 * 该函数只考虑合法路径，
	 * 
	 * 得到路径上最小的带宽,如果返回10000，则说明路径不通
	 * @param path
	 * @param _graph
	 * @return
	 */
	public int GetminBw( Graph _graph) {
		// 计算最小带宽 minbw
		int node1, node2, bw,cost;
		int minbw = 10000;// 最小带宽
		
		//printMatrix(_graph.matbw, "matbw");
		//printMatrix(_graph.matcost, "matcost");
		
		for (int i = 0; i < path.length - 1; i++) {
			node1 = path[i];
			node2 = path[i + 1];
			bw = _graph.matbw[node1][node2];
			//cost = _graph.matcost[node1][node2];
			//System.out.println("bw="+bw+" cost="+cost+" node1="+node1 +" node2="+node2 );
			if ((bw != INF_INT) && (minbw > bw) && (bw > 0)  ) {//&&(cost<INF_INT)
				minbw = bw;
			}
		}
		bandwidth = minbw;
		return minbw;
	}
	

	/**
	 * 把String做处理，转为int[]
	 * @param _s
	 * @return
	 */
	public int[] converPathtoInt(String _s) {
		String[] strings = _s.split("-");
		int[] returnss = new int[strings.length];
		for (int i = 0; i < strings.length; i++) {
			returnss[i] = Integer.parseInt(strings[i]);
		}
		return returnss;
	}
	
	
	
}
