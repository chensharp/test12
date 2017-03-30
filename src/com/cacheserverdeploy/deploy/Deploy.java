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
    	
    	searchDeployed();
    	
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
