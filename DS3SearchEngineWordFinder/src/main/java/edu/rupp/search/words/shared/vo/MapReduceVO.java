/**
 * 
 */
package edu.rupp.search.words.shared.vo;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * @author sok.pongsametrey
 *
 */
public class MapReduceVO implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1991054328724900122L;

	private Map<String, Integer> mapReduce;
	/**
	 * 
	 */
	public MapReduceVO () {
		mapReduce = new HashMap<String, Integer>();
	}
	/**
	 * 
	 * @param key
	 * @param value
	 */
	public void addValue (String key) {
		Integer valueTmp = this.mapReduce.get(key);
		
		if (valueTmp == null) valueTmp = 0;
		
		int newValue = valueTmp + 1;
		this.mapReduce.remove(key);
		
		this.mapReduce.put(key, newValue);
	}
	/**
	 * 
	 * @param key
	 * @param value
	 */
	public void put (String key, Integer value) {
		this.mapReduce.put(key, value);
	}
	
	public Integer get (String key) {
		return this.mapReduce.get(key);
	}
	/**
	 * 
	 * @param inputMap
	 */
	public void reduce (Map<String, Integer> inputMap) {
		for (String word : inputMap.keySet()) {
			Integer value = inputMap.get(word);
			if (value == null) value = 0;
			
			Integer oldValue = this.mapReduce.get(word);
			if (oldValue == null) oldValue = 0;
			
			this.mapReduce.put(word, value + oldValue);
		}
	}
	/**
	 * 
	 * @return
	 */
	public Map<String, Integer> getMapReduce () {
		return this.mapReduce;
	}
	/**
	 * 
	 * @param key
	 * @return
	 */
	public Integer getValue (String key) {
		return this.mapReduce.get(key);
	}
	
	public String toString () {
		StringBuffer str = new StringBuffer();
		
		for (String word : this.mapReduce.keySet()) {
			Integer counter = this.mapReduce.get(word);
			str.append(word).append("\t").append(counter).append("\n");
		}
		return String.valueOf(str);
	}
}
