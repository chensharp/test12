package com.chensharp.tools;

import java.util.ArrayList;

/*
 * 解集合
 */
public class SolutionList {
	//保存全部解
	public ArrayList<solution> Solutionlist = new ArrayList<solution>();
	public int solistIndex;//解的序号
	
	
	public SolutionList() {
		// TODO Auto-generated constructor stub
		solistIndex = 0;
	}
	
	/**
	 * 增加一个解
	 * @return
	 */
	public int addSolution() {
		
		solution so = new solution(solistIndex);
		Solutionlist.add(so);
		solistIndex++;
		return solistIndex;
	}
	
	public void checkallSolution() {
		
	}
	
	
	
}
