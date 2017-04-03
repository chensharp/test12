package com.cacheserverdeploy.deploy;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.omg.CORBA.Current;

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
	//数据定义区，跨函数数据调用
	final static int MAX_NODE_LENGTH=1000; //最大节点数目 
	final static int INF_INT= 100000; //无穷大 
	public static int max_node_num = 0;
	
    public static DataOpt _dataopt = new DataOpt(); //原始数据
	//public static int [][] dag_cost ;//网络花费
	//public static int [][] dag_bw ;//网络带宽
	
	public static Graph dag_graph;//图
	
	public static SolutionList _solutionlist = new SolutionList();//解的集合
	
	public static ArrayList<Flow> flows = new ArrayList<Flow>();
	
	
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
		max_node_num++;
		System.out.println("max_node_num = "+max_node_num);
		
		//初始化网络矩阵
		//dag_cost = new int [max_node_num][max_node_num];//网络花费
		//dag_bw = new int [max_node_num][max_node_num];//网络带宽
		dag_graph = new Graph(max_node_num);
		
		//init 0 dag
		for (int i = 0; i < max_node_num; i++) {
			for (int j = 0; j < max_node_num; j++) {
				dag_graph.matcost [i][j]=INF_INT;
				dag_graph.matbw [i][j]=0;//带宽初始化为0
			}
		}
		
		//initdagcost,initdagbw
		for (int i = 0; i < _dataopt.getLink_num(); i++) {
			dag_graph.matcost[_dataopt.links.get(i).getStart_node()][_dataopt.links.get(i).getEnd_node()]= 
					_dataopt.links.get(i).getRent_cost();
			dag_graph.matcost[_dataopt.links.get(i).getEnd_node()][_dataopt.links.get(i).getStart_node()]= 
					 _dataopt.links.get(i).getRent_cost();
			
			dag_graph.matbw[_dataopt.links.get(i).getStart_node()][_dataopt.links.get(i).getEnd_node()]= 
					 _dataopt.links.get(i).getBandwidth();
			dag_graph.matbw[_dataopt.links.get(i).getEnd_node()][_dataopt.links.get(i).getStart_node()]= 
					 _dataopt.links.get(i).getBandwidth();
		}
		
		
		//printMatrix(dag_cost, "dagcost");
		//printMatrix(dag_bw, "dagbw");
/**/
	}
	
	/**
	 * 打印矩阵
	 */
	private static void printMatrix(int[][] mat, String name) {
		System.out.println("---------------开始打印矩阵:"+name+"----------------------------");
		// print 
		for (int i = 0; i < mat.length; i++) {
			for (int j = 0; j < mat.length; j++) {
				if (mat[i][j]==INF_INT) {
					System.out.print(" # ");
				}else{
					System.out.print(" "+ mat[i][j] + " ");
				}
			}
			System.out.print("\n");
		}
	}

	public static String getDIJPath(int[][] weight,int start,int end) {
		String[] str = Dijsktra1(weight, start);
		return str[end];
	}
	
    /**
     * 
     * @param weight
     * @param start
     * @return  返回start点到所有点的最短路径的string[],其中【1】是start到1的路径，用-隔开。  
     */
	public static String[] Dijsktra1(int[][] weight, int start) {
		// 接受一个有向图的权重矩阵，和一个起点编号start（从0编号，顶点存在数组中）
		// 返回一个int[] 数组，表示从start到它的最短路径长度
		int n = weight.length; // 顶点个数
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
					int res =0;
					if(weight[start][k]==INF_INT || weight[k][i]==INF_INT){//如果二者有一个是无穷大，即为不可达，结果也为不可达
						res = INF_INT;
					}else {
						res = (weight[start][k] + weight[k][i]);
					}
					weight[start][i] = res;
					
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
		int sumbwOut = 0;
		int sumbwIn = 0;
		int degreeOut = 0;
		int degreeIn = 0;
		for (int i = 0; i < max_node_num; i++) {
			sumbwOut = 0;
			sumbwIn = 0;
			degreeOut = 0;
			for (int j = 0; j < max_node_num; j++) {
				if (dag_graph.matbw[i][j]!=0) {
					sumbwOut += dag_graph.matbw[i][j];
					degreeOut ++;
				}
			}
			degreeIn = 0;
			for (int j = 0; j < max_node_num; j++) {
				if (dag_graph.matbw[j][i]!=0) {
					sumbwIn += dag_graph.matbw[j][i];
					degreeIn ++;
				}
			}
			System.out.println(i+" node: sumbwOut= "+sumbwOut+" degreeOut= "+degreeOut+ "sumbwIn= "+sumbwIn+" degreeIn= "+degreeIn );
		}

		//复制带宽，
		Graph _graph_1 = new Graph(max_node_num);
		_graph_1.reInit(dag_graph);
		_graph_1.InitConsumer(_dataopt);
		
		//GetFlows(4,38,_graph_1);
		//printMatrix(_graph_1.matbw, "matbw");
		//printMatrix(_graph_1.matcost, "matcost");
		
		
		//_graph_1.matbw= arraysCopy(dag_bw);
		//_graph_1.matcost= arraysCopy(dag_cost);
		//Graph _graph_2 = new Graph(max_node_num);
		//_graph_2.reInit(_graph_1);
		
		//System.out.println(_graph_1.consumerBw.get("34"));
		
		
		//_graph_2.consumerBw.put(, value);
	//	String[] result = Dijsktra1(_graph_1.matcost, 4);

		//for (int i = 0; i < max_node_num; i++){
	//		System.out.println("从" + 0 + "出发到" + i + "的最短路径为：" + result[i]);
			/*int[] temp = converPathtoInt(result[i]);
			for (int j = 0; j < temp.length; j++) {
				System.out.print(" "+temp[j]);
			}*/
		//	System.out.print("\n");
		//}
		//计算下总需求带宽
		/*
		int sumneed =0;
		for (int i = 0; i < _dataopt.consumeNodes.size(); i++) {
			sumneed += _dataopt.consumeNodes.get(i).getBandwidth_cost();
		}
		System.out.println("总需求 = "+sumneed);
		*/
		//printMatrix(dag_bw, "原图:");
		//printMatrix(matbw_1, "test1:");
		
		//String[] result_1 = Dijsktra1(matcost_1, 4);
		//int[] temp = converPathtoInt(result_1[30]);
		//for (int j = 0; j < temp.length; j++) {
	//		System.out.print(" "+temp[j]);
	//	}
	//	System.out.print("\n");
		
		//for (int i = 0; i < max_node_num; i++){
		//	System.out.println("从" + 0 + "出发到" + i + "的最短路径为：" + result_1[i]);
			/*int[] temp = converPathtoInt(result[i]);
			for (int j = 0; j < temp.length; j++) {
				System.out.print(" "+temp[j]);
			}*/
		//	System.out.print("\n");
		//}
		
		//String res = getDIJPath(_graph_2.matcost, 4,44);
		//System.out.println(res);
		
	}
	
	/**
	 * 拷贝数组二维
	 * @param newmat
	 * @return
	 */
	private static int[][] arraysCopy(int[][] newmat) {
		int n = newmat.length;
		int[][] result= new int[n][n];
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < n; j++) {
				result[i][j] = newmat[i][j];
			}
		}
		return result;
	}

	/**
	 * 得到路径上最小的带宽,如果返回10000，则说明路径不通
	 * @param path
	 * @param _graph
	 * @return
	 */
	private static int GetminBw(int[] path, Graph _graph) {
		// 计算最小带宽 minbw
		int node1, node2, bw;
		int minbw = 10000;// 最小带宽
		
		//printMatrix(_graph.matbw, "matbw");
		//printMatrix(_graph.matcost, "matcost");
		
		for (int i = 0; i < path.length - 1; i++) {
			node1 = path[i];
			node2 = path[i + 1];
			bw = _graph.matbw[node1][node2];
			//System.out.println("bw="+bw+" node1="+node1 +" node2="+node2 );
			if ((bw != INF_INT) && (minbw > bw) && (bw > 0)) {
				minbw = bw;
			}
		}
		return minbw;
	}
	
	
	
	/**
	 * 扣除带宽,输入bw矩阵，返回扣除的bw矩阵，如果bw降为0，将cost矩阵置为inf
	 * @return
	 */
	private static Graph Deduction(int[] path,Graph _graph) {
		//计算最小带宽 minbw
		int node1,node2,bw;
		int minbw= GetminBw(path, _graph);//最小带宽
		int tempdw;
		for(int i=0;i<path.length-1;i++){
			node1 = path[i];
			node2 = path[i+1];
			bw = _graph.matbw[node1][node2];
			if ( ( bw != INF_INT )&&( bw > 0 ) ) {
				tempdw = bw - minbw;
				if (tempdw<=0) {//结果小于等于0
					tempdw = 0;
					//修改cost矩阵，置为不可达
					_graph.matcost[node1][node2] = INF_INT;
				}
				System.out.println("修改的新bw： "+tempdw+" node1="+node1+" node2="+node2+" bw="+bw);//输出
				_graph.matbw[node1][node2] = tempdw;//更新bw
			}
		}
		return _graph;
	}
	

	/**
	 * 生成
	 * @param startid 起始点，为服务器节点
	 * @param endid  终止点，为消费者节点
	 * @param matcost  cost花费矩阵，当值为-1，表示流量为空。matbw 带宽矩阵，为0表示无带宽。
	 * @param 
	 */
	private static void GetFlows(int[] startid, int[] endid,Graph _graph) {
		/*
		 * 
		 * 缺少如何保存输出的路径解的代码部分，，
		 * 
		 */
		ArrayList<String> flowStr = new ArrayList<String>();//保存路径
		
		Graph _graph_1 = new Graph(_graph.lengths);
		_graph_1.reInit(_graph);//新图数据
		_graph_1.InitConsumer(_dataopt);
		
		String resultStr= new String();
		int[] resultInt;
		int minbw,conBw;
		
		int starti=0;
		int endi=0;
		for(;;){
			
			resultStr = getDIJPath(_graph_1.matcost, startid[starti], endid[endi]);//得到路径，
			
			System.out.println("（循环开始） 产生路径："+resultStr);//输出
			
			resultInt = converPathtoInt(resultStr);
			minbw = GetminBw(resultInt, _graph_1);//得到提供的带宽
			System.out.println("路径提供带宽："+minbw);
			
			if(minbw >=10000){//如果路径产生失败，说明没有足够的路径到达该终端，
				//说明该起点到该终点不能产生路径，则退出并更换起点，终点不变。
				starti++;//更换下一个startnode
				if (starti >= startid.length) {//使用完了节点，准备输出无解。
					
				}
				
				continue;
			}else {//正常路径，
				
				
				
			}
			
			
			//获得消费者带宽需求
			conBw = Integer.parseInt( _graph.consumerBw.get(String.valueOf( endid[endi]  ) )  );
			System.out.println("消费者带宽需求："+conBw);
			
			int Current ;
			if (conBw >= minbw) {//如果需求 大于 提供
				//更新消费需求，剪去提供的。
				Current = conBw - minbw;//计算出
				_graph_1.UpdateConsumer(endid[endi],Current);//更新
				
			}else {//需求小于提供，满足，则停止继续产生。
				//更新消费需求，remove掉，
				_graph_1.consumerBw.remove(String.valueOf(endid[endi] ));
			
				//break;
				//更换下一个endid，
				endi++;
			}
			Deduction(resultInt, _graph_1);//扣减带宽
			//保存路径，这里的保存路径才是有效路径，
			flowStr.add(resultStr);
		}

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
