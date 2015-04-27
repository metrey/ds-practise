/**
 * 
 */
package edu.rupp.search.words.server;

import java.sql.SQLException;
import java.util.List;
import java.util.StringTokenizer;

import edu.rupp.search.words.server.dao.SEWordFinderDAO;
import edu.rupp.search.words.server.vo.ParamVO;
import edu.rupp.search.words.shared.vo.MapReduceVO;

/**
 * @author sok.pongsametrey
 *
 */
public class WordFinderSeverThread implements Runnable {

	List<String> lstLines = null;
	SEWordFinderDAO dao = null;
	ParamVO paramVO = null;
	
	public WordFinderSeverThread (List<String> lstLines, ParamVO paramVO) {
		this.lstLines = lstLines;
		this.paramVO = paramVO;
		this.dao = paramVO.getWordFinderDao();
	}
	
	public void run() {
		String threadName = Thread.currentThread().getName();   
        
		if (this.lstLines == null || this.lstLines.size() == 0) return;
        
        System.out.println("STARTING " + threadName + " with " + this.lstLines.size() + " lines");
        MapReduceVO mapReduceVO = new MapReduceVO();
        for (String line : this.lstLines) {
        	this.mapReducer(line, mapReduceVO);
        }
		try {
			this.dao.saveWord(mapReduceVO);
		} catch (SQLException e) {
			System.out.println("SQLException [" + threadName + "]: " + e.getMessage());
			e.printStackTrace();
		}
	}
	/**
	 * 
	 * @param line
	 * @param mapReduceVo
	 */
	private void mapReducer (String line, MapReduceVO mapReduceVo) {
//		Pattern pattern = Pattern.compile(" ");
//		String[] split = pattern.split (line, 0);
		
		StringTokenizer token = new StringTokenizer(line);
        while(token.hasMoreTokens()) {
            String word = token.nextToken();
            if (word == null || "".equals(word.trim())) continue;
            
            Integer count = mapReduceVo.get(word.trim());

            if(count==null){
                count = 0;
            }
            mapReduceVo.put(word, count+1);            
        }
	}

}
