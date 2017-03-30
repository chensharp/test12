package com.cacheserverdeploy.deploy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.chensharp.genetic.GeneticAlgorithmTest;
import com.chensharp.tools.*;

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

public class Deploy
{
	final static int MAX_NODE_LENGTH=1000; //最大节点数目 
	final static byte INF_INT= -1; //无穷大 
	public static int max_node_num=0;
	
    public static DataOpt _dataopt = new DataOpt(); //原始数据
	public static byte [][] dag_cost = new byte [MAX_NODE_LENGTH][MAX_NODE_LENGTH];//网络花费
	public static byte [][] dag_bw = new byte [MAX_NODE_LENGTH][MAX_NODE_LENGTH];//网络带宽
	
	public static SolutionList _solutionlist = new SolutionList();//解的集合
	
	
	
    /*初始化数据
     * 
     */
	private static void initData(String[] graphContent){
//        String[] sourceStrArray = graphContent.split("\\n");
//        for (int i = 0; i < sourceStrArray.length; i++) {
//            System.out.println(sourceStrArray[i]);
//        }
		String[] handStrArray = graphContent[0].split(" ");
		_dataopt.setNetNode_num(Integer.parseInt( handStrArray[0]));
		_dataopt.setLink_num(Integer.parseInt( handStrArray[1]));
		_dataopt.setConsumeNode_num(Integer.parseInt( handStrArray[2]));
		//for (int i = 0; i < sourceStrArray.length; i++)
			//System.out.println(sourceStrArray[i]);
		_dataopt.setDeploy_cost(Integer.parseInt( graphContent[2]));
		//读取links信息
		int tempi=4;
		for (int i = 4; i < graphContent.length; i++) {
			if (graphContent[i].length()==0) {
				tempi = i+1;
				break;
			}
			String[] linkStrArray = graphContent[i].split(" ");
			link _link =new link();
			_link.setStart_node(Integer.parseInt(linkStrArray[0]));
			_link.setEnd_node(Integer.parseInt(linkStrArray[1]));
			_link.setBandwidth(Integer.parseInt(linkStrArray[2]));
			_link.setRent_cost(Integer.parseInt(linkStrArray[3]));
			_dataopt.addLink(_link);
		}
		//读取consumeNode信息
		for (int i = tempi; i < graphContent.length; i++) {
			if (graphContent[i].length()==0) {
				break;
			}
			String[] consumeStrArray = graphContent[i].split(" ");
			consumeNode _consumenode =new consumeNode();
			_consumenode.setNode_id(Integer.parseInt(consumeStrArray[0]));
			_consumenode.setLinking_node(Integer.parseInt(consumeStrArray[1]));
			_consumenode.setBandwidth_cost(Integer.parseInt(consumeStrArray[2]));
			_dataopt.addCosumeNode(_consumenode);
		}
//		for (int i = 0; i < graphContent.length; i++) {
//			if (graphContent[i].length()==0) {
//				graphContent[i]="%";
//			} 
//			System.out.println(graphContent[i]);
//		}
		//String[] sourceStrArray = graphContent.split("\\n");

	}
	/**
	 * 初始化DAG
	 */
	private static void initDAG() {
		//init 0 dag
		for (int i = 0; i < MAX_NODE_LENGTH; i++) {
			for (int j = 0; j < MAX_NODE_LENGTH; j++) {
				dag_cost[i][j]=INF_INT;
				dag_bw[i][j]=INF_INT;
			}
		}
		
		//initdagcost,initdagbw
		for (int i = 0; i < _dataopt.getLink_num(); i++) {
			dag_cost[_dataopt.links.get(i).getStart_node()][_dataopt.links.get(i).getEnd_node()]= 
					(byte) _dataopt.links.get(i).getRent_cost();
			dag_cost[_dataopt.links.get(i).getEnd_node()][_dataopt.links.get(i).getStart_node()]= 
					(byte) _dataopt.links.get(i).getRent_cost();
			
			dag_bw[_dataopt.links.get(i).getStart_node()][_dataopt.links.get(i).getEnd_node()]= 
					(byte) _dataopt.links.get(i).getBandwidth();
			dag_bw[_dataopt.links.get(i).getEnd_node()][_dataopt.links.get(i).getStart_node()]= 
					(byte) _dataopt.links.get(i).getBandwidth();
		}
		
		//求最大的node数目
	    max_node_num=0;
		int end,maxend=0;
		int start, maxstart=0;
		for (int i = 0; i < _dataopt.getLink_num(); i++) {
			end = _dataopt.links.get(i).getEnd_node();
			start = _dataopt.links.get(i).getStart_node();
			if (start>maxstart) {
				maxstart=start;
			}
			if (end>maxend) {
				maxend=end;
			}
		}
		if (maxstart>maxend) {
			max_node_num = maxstart;
		}else {
			max_node_num = maxend;
		}
		
		System.out.println("maxnum = "+max_node_num);
		
	/*	
		//System.out.println(max_node_id);
		//print dag
		for (int i = 0; i <= max_node_id; i++) {
			for (int j = 0; j <= max_node_id; j++) {
				System.out.print(" "+dag_cost[i][j]+" ");
				
			}
			System.out.print("\n");
		}
		System.out.println("*******************************************************************************************");
		// print dag
		for (int i = 0; i <= max_node_id; i++) {
			for (int j = 0; j <= max_node_id; j++) {
				System.out.print(" " + dag_bw[i][j] + " ");

			}
			System.out.print("\n");
		}
*/
	}
	

    /**
     * chart2 - 引入bandwidth的最短路径
     * @param weight
     * @param bandwidth
     * @param start
     * @return  返回start点到所有点的最短路径的string[],其中【1】是start到1的路径，用-隔开。  
     */
	public static String[] Dijsktra12(byte[][] weight, byte[][] bandwidth, int start, int _maxnode) {
		// 接受一个有向图的权重矩阵，和一个起点编号start（从0编号，顶点存在数组中）
		int n = _maxnode; // 顶点个数
		//int[] shortPath = new int[n]; // 存放从start到其他各点的最短路径长度

		String[] path = new String[n]; // 存放从start到其他各点的最短路径的字符串表示
		for (int i = 0; i < n; i++) {
			path[i] = new String(start + "-" + i);
		}
		int[] visited = new int[n]; //标记当前该顶点的最短路径是否已经求出,1表示已求出

		// 初始化，第一个顶点求出
		//shortPath[start] = 0;
		visited[start] = 1;

		// 要加入n-1个顶点
		for (int count = 1; count <= n-1; count++) {
			int k = -1; // 选出一个距离初始顶点start最近的未标记顶点
			int dmin = Integer.MAX_VALUE;//寻找最小的节点i
			for (int i = 0; i < n; i++) {
				if (visited[i] == 0 && weight[start][i] < dmin ) {//&& bandwidth[start][i] > 0
					dmin = weight[start][i];
					k = i;
				}
			}
			//System.out.println("k=" + k);

			// 将新选出的顶点标记为已求出最短路径，且到start的最短路径就是dmin
			//shortPath[k] = dmin;
			visited[k] = 1;

			// 以k为中间点，修正从start到未访问各点的距离
			for (int i = 0; i < n; i++) {
				// System.out.println("k="+k);
				if (visited[i] == 0 && weight[start][k] + weight[k][i] < weight[start][i]) {
					weight[start][i] = (byte) (weight[start][k] + weight[k][i]);
					path[i] = path[k] + "-" + i;
				}
			}
		}
		return path;
	}
	
	
    /**
     * 
     * @param weight
     * @param start
     * @return  返回start点到所有点的最短路径的string[],其中【1】是start到1的路径，用-隔开。  
     */
	public static String[] Dijsktra1(byte[][] weight, int start, int _maxnode) {
		// 接受一个有向图的权重矩阵，和一个起点编号start（从0编号，顶点存在数组中）
		// 返回一个int[] 数组，表示从start到它的最短路径长度
		int n = _maxnode; // 顶点个数
		int[] shortPath = new int[n]; // 存放从start到其他各点的最短路径

		String[] path = new String[n]; // 存放从start到其他各点的最短路径的字符串表示
		for (int i = 0; i < n; i++) {
			path[i] = new String(start + "-" + i);
		}
		int[] visited = new int[n]; //标记当前该顶点的最短路径是否已经求出,1表示已求出

		// 初始化，第一个顶点求出
		shortPath[start] = 0;
		visited[start] = 1;

		// 要加入n-1个顶点
		for (int count = 1; count <= n-1; count++) {
			int k = -1; // 选出一个距离初始顶点start最近的未标记顶点
			int dmin = Integer.MAX_VALUE;//寻找最小的节点i
			for (int i = 0; i < n; i++) {
				if (visited[i] == 0 && weight[start][i] < dmin) {
					dmin = weight[start][i];
					k = i;
				}
			}
			//System.out.println("k=" + k);

			// 将新选出的顶点标记为已求出最短路径，且到start的最短路径就是dmin
			shortPath[k] = dmin;
			visited[k] = 1;

			// 以k为中间点，修正从start到未访问各点的距离
			for (int i = 0; i < n; i++) {
				// System.out.println("k="+k);
				if (visited[i] == 0 && weight[start][k] + weight[k][i] < weight[start][i]) {
					weight[start][i] = (byte) (weight[start][k] + weight[k][i]);
					path[i] = path[k] + "-" + i;
				}
			}
		}
		return path;
	}
	/**
	 * 把Dijsktra1函数的返回值做处理，转为int[]
	 * @param _s
	 * @return
	 */
	public static int[] converPathtoInt(String _s) {
		String[] strings = _s.split("-");
		int[] returnss = new int[strings.length];
		for (int i = 0; i < strings.length; i++) {
			returnss[i] = Integer.parseInt(strings[i]);
		}
		return returnss;
	}
	

	/**
	 * 获取节点信息（出度，带宽信息，）
	 */
	private static void GetNodeInfo() {
		//计算出度和带宽
		int sumbw = 0;
		int degree = 0;
		for (int i = 0; i < max_node_num; i++) {
			sumbw = 0;
			degree = 0;
			for (int j = 0; j < max_node_num; j++) {
				if (dag_cost[i][j]!=INF_INT) {
					sumbw += dag_bw[i][j];
					degree ++;
				}
			}
			System.out.println(i+" node: sumbw= "+sumbw+" degree= "+degree );
		}
		
		String[] result = Dijsktra1(dag_cost, 0, max_node_num);

		for (int i = 0; i < max_node_num; i++){
			System.out.println("从" + 0 + "出发到" + i + "的最短路径为：" + result[i]);
		/*	int[] temp = converPathtoInt(result[i]);
			for (int j = 0; j < temp.length; j++) {
				System.out.print(" "+temp[j]);
			}
			System.out.print("\n");*/
		}
	
	}
	
	
	private void GetLinks( ) {
		//
	}
	
	
	
	
	/**
     * 你需要完成的入口
     * <功能详细描述>
     * @param graphContent 用例信息文件
     * @return [参数说明] 输出结果信息
     * @see [类、类#方法、类#成员]
     */
    public static String[] deployServer(String[] graphContent)
    {
    	initData(graphContent);
    	
    	/**do your work here**/
    	_dataopt.print();
    	initDAG();
    	//GeneticAlgorithmTest test = new GeneticAlgorithmTest();  
        //test.caculte();  
    	GetNodeInfo();
    	//searchDeployed();
    	
    	
		return new String[] { "17", "\r\n", "0 8 0 20" };
	}
    
    /**
     * 搜索最优解的过程
     */
    public static void searchDeployed() {
		/*
		 *首先假设所需视频流量的节点数目 n = 1 或2，如果不能找到可行解，便继续增加数目；
		 *从带有消费节点的节点开始，进行生长，直至获得一个解，保存此状态的带宽占用和费用
		 *
		 *单个节点生长过程：
		 *一条链路上流量的大小由该链路上最小的带宽决定。
		 *
		 *
		 *一、顺序解法： 限定server的数目， 问题分解为已知server数目（1，2，3，n）的求解过程，从高到低不断消减数目，直至无解，
		 *初始等于消费节点的数目，每个迭代减少一个步长，输出结果，
		 *    1，求解已知server数目为n时（n<=m  comsumer num ）,一个可行解，
		 *      1.1：已知有m个consumer ，分解为单个consumer到server节点的生长，？
		 *
		 *
		 *
		 *
		 */
    	
	}

}
