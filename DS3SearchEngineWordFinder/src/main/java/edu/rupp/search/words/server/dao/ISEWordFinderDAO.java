/**
 * 
 */
package edu.rupp.search.words.server.dao;

import java.sql.SQLException;

import edu.rupp.search.words.shared.vo.FileTransferVO;
import edu.rupp.search.words.shared.vo.MapReduceVO;



/**
 * @author sok.pongsametrey
 *
 */
public interface ISEWordFinderDAO {
	/** Save file */
	public int saveFile(FileTransferVO fileVO) throws SQLException;
	
	public int saveWord (MapReduceVO mapVO) throws SQLException;
	
	public MapReduceVO searchWord (String word) throws SQLException;
	
	public static final String SQL_FILE_INS = String.valueOf(new StringBuffer()
			.append("INSERT INTO td_file (filename, from_ip, to_ip, is_loaded, dt_cre, dt_upd) VALUES (?, ?, ?, ?, ?, ?) ")
			);
	
	public static final String SQL_FILE_UPD = String.valueOf(new StringBuffer()
			.append("UPDATE td_file SET is_loaded = ?, dt_upd = ? WHERE filename = ? ")
			);
	public static final String SQL_WORD_INS = String.valueOf(new StringBuffer()
			.append("INSERT INTO td_word (word, counter) VALUES (?, ?) ")
		);
	public static final String SQL_WORD_UPD = String.valueOf(new StringBuffer()
			.append("UPDATE td_word SET counter=? WHERE word = ?")
		);
	public static final String SQL_WORD_SEL = String.valueOf(new StringBuffer()
			.append("SELECT word, sum(counter) NB_FOUND FROM td_word WHERE upper(word) = upper(?) GROUP BY word ")
		); 
}
