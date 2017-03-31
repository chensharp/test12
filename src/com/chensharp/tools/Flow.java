package com.chensharp.tools;



/**
 * 流，一条流，
 * @author chen
 *
 */
public class Flow {
	
	public int start_id;
	public int end_id;
	
	private String toStringpath;//转化为string版路径，
	private int[] path;//下标路径 
	
	public Flow(String _path) {
		toStringpath = _path;
		String[] strings = _path.split("-");
		path = new int[strings.length];
		for (int i = 0; i < strings.length; i++) {
			path[i] = Integer.parseInt(strings[i]);
		}
		start_id = path[0];
		end_id = path[strings.length-1];
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
	}
	
	public String toStringPath () { return toStringpath; }
	public int[]  toIntPath() { return path;  }
	
}
