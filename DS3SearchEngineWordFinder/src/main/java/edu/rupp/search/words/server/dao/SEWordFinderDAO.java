/**
 * 
 */
package edu.rupp.search.words.server.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

import edu.rupp.search.words.common.dao.AbstractDAO;
import edu.rupp.search.words.shared.vo.FileTransferVO;
import edu.rupp.search.words.shared.vo.MapReduceVO;


/**
 * @author sok.pongsametrey
 *
 */
public class SEWordFinderDAO extends AbstractDAO implements ISEWordFinderDAO{

	private static final int BATCHES_TO_EXEC = 500;
	@Override
	public void createDBModel() throws SQLException {
		try {
			this.getConnection();
		    
		    this.getStatement().executeUpdate("create table if not exists td_file (file_id integer PRIMARY KEY AUTOINCREMENT, filename string, from_ip String, to_ip String, is_loaded integer, dt_cre date, dt_upd date)");
		    this.getStatement().executeUpdate("create table if not exists td_word (word_id integer PRIMARY KEY AUTOINCREMENT, word string, counter integer)");

		} catch (SQLException e) {
			System.out.println("ERROR: " + e.getMessage());
			throw e;
		} finally {
			this.close();
		}
		System.out.println("DB Model created for database: " + this.dbName + "!");		
	}

	/**
	 * 
	 */
	public int saveFile(FileTransferVO fileVO) throws SQLException {
		PreparedStatement ps = null;
		int nbReturn = -1;
		try {
			System.out.println("> SQL saveFile: " + SQL_FILE_INS);
			ps = this.getConnection().prepareStatement(SQL_FILE_INS);
			ps.setString(1, fileVO.getFilename());
			ps.setString(2, fileVO.getClientIP());
			ps.setString(3, this.serverIPAddress);
			ps.setInt(4, 1);
			ps.setDate(5, new java.sql.Date(new Date().getTime()));
			ps.setDate(6, new java.sql.Date(new Date().getTime()));
			
			nbReturn = ps.executeUpdate();	
			
		} catch (SQLException e) {
			throw e;
		} finally {
			if (ps != null) {
				ps.close();
			}
			this.close();
			System.out.println(nbReturn + " file inserted. ");
		}
				
		return nbReturn;
	}

	/**
	 * 
	 */
	public int saveWord(MapReduceVO mapVO) throws SQLException {
		PreparedStatement ps = null;
		int nbReturn = 0;
		int counter = 0;
		try {
			System.out.println("> SQL saveFile: " + SQL_WORD_INS);
			ps = this.getConnection().prepareStatement(SQL_WORD_INS);
			
			for (String word : mapVO.getMapReduce().keySet()) {
				Integer occur = mapVO.getMapReduce().get(word);
				//ps.clearParameters();
				ps.setString(1, word);
				ps.setInt(2, occur);	
				ps.addBatch();
				counter++;
				
				if (counter != 0 && counter % BATCHES_TO_EXEC == 0) {
					ps.executeBatch();
					nbReturn += counter;
					this.getConnection().commit();
					counter = 0;
				}
			}
			if (counter > 0) {
				ps.executeBatch();
				nbReturn += counter;
			}
		} catch (SQLException e) {
			throw e;
		} finally {
			if (ps != null) {
				ps.close();
			}
			this.close();
			System.out.println(nbReturn + " words inserted. ");
		}
				
		return nbReturn;
	}

	/**
	 * 
	 */
	public MapReduceVO searchWord(String word) throws SQLException {
		
		MapReduceVO mapReduceVO = new MapReduceVO();
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			System.out.println("> SQL searchWord: " + SQL_WORD_SEL);
			ps = this.getConnection().prepareStatement(SQL_WORD_SEL);
			ps.setString(1, word);
			rs = ps.executeQuery();
			while (rs.next()) {
				mapReduceVO.put(rs.getString("WORD"), rs.getInt("NB_FOUND"));
			}
						
		} catch (SQLException e) {
			throw e;
		} finally {
			if (rs != null) rs.close();
			this.close();
			System.out.println("# Found word(s): " + mapReduceVO.getMapReduce().size());
		}
		return mapReduceVO;
	}

}
