package com.cacheserverdeploy.deploy;

import com.chensharp.genetic.GeneticAlgorithmTest;
import com.chensharp.tools.DataOpt;
import com.chensharp.tools.consumeNode;
import com.chensharp.tools.link;

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
    public static DataOpt _dataopt = new DataOpt(); 
	
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
    	//_dataopt.print();
    	//GeneticAlgorithmTest test = new GeneticAlgorithmTest();  
        //test.caculte();  
    	
    	
    	
        return new String[]{"17","\r\n","0 8 0 20"};
    }

}
