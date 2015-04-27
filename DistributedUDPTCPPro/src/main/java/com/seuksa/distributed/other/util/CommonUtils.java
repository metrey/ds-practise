/**
 * 
 */
package com.seuksa.distributed.other.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.sql.Clob;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.lang.StringUtils;


/**
 * @author sok.pongsametrey
 * @version $Revision: 1.5 $
 */
public class CommonUtils {
	//private static Charset charset = Charset.forName("UTF-8");
    //private static CharsetDecoder decoder = charset.newDecoder();

	/**
     * 
     * @param fileName s
     * @param data s1
	 * @throws IOException 
     */
    public static void writeToFile(String fileName, String data) throws IOException {
        BufferedWriter bufferedWriter = null;
        try {
            bufferedWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(fileName), "UTF-8"));
            bufferedWriter.write(data);
            bufferedWriter.flush();
        } catch (IOException ioEx) {
            throw ioEx;
        } finally {
            try {
                bufferedWriter.close();
            } catch (IOException ioEx) {
            	throw ioEx;
            }
        }
    }
    /**
     * 
     * @param clb
     * @return
     * @throws IOException
     * @throws SQLException
     */
    public static String clobStringConversion(Clob clb) throws IOException, SQLException
    {
    	if (clb == null) return  "";
            
    	StringBuffer str = new StringBuffer();
    	String strng;
              
    
    	BufferedReader bufferRead = new BufferedReader(clb.getCharacterStream());
   
    	while ((strng = bufferRead.readLine())!=null) {
    		str.append(strng);
    	}
   
    	return str.toString();
    }
    
    /**
     * 
     * @param filepath
     * @return
     * @throws Exception 
     */
    public static synchronized FileChannel openFileChannelOutputStream (String filepath) throws Exception {
    	FileOutputStream fileOutputStream = null;
        FileChannel fileChannel = null;
        
        try {
			fileOutputStream = new FileOutputStream(filepath, true);
			fileChannel = fileOutputStream.getChannel();
		} catch (FileNotFoundException e) {
			throw e;
		} catch (Exception e) {
			throw e;
		} finally {
			if (fileOutputStream != null) {
				//fileOutputStream.close();
			}
		}
        
        return fileChannel;
    }
    
    /**
     * Writing into file
     * @param fileChannel
     * @param inputStr
     * @throws Exception 
     */
    public static synchronized void writeFileChannelString (FileChannel fileChannel, String inputStr) throws Exception {
    	
    	if (StringUtils.isEmpty(inputStr)) {
    		return;    		
    	}
    	ByteBuffer byteBuffer = null;
    	int length = inputStr.length();
    	
    	try {
    		long fileSize = fileChannel.size(); 
    		byteBuffer = ByteBuffer.allocate(length * 4);
    		System.out.println("File size before write: " + fileSize + " - String length: " + length );
    		
    		byteBuffer.clear();
    		byteBuffer.put(inputStr.getBytes("utf-8"));
        	byteBuffer.flip();
			fileChannel.write(byteBuffer);    		
		} catch (IOException e) {
			throw e;
		} catch (Exception e) {
			throw e;
		}
    }
    
    /**
     * Closing file channel
     * @param fileChannel
     * @throws IOException
     */
    public static void closeFileChannelStream (FileChannel fileChannel) throws IOException {
    	if (fileChannel != null) {
    		fileChannel.close();
    	}
    }
    
    /**
     * 
     * @param dt
     * @param format
     * @return
     * @throws ParseException
     */
    public static Date validateDate (String dt, String format) throws ParseException {
    	SimpleDateFormat df = new SimpleDateFormat(format);
    	Date myDate = null;
    	myDate = df.parse(dt);
    	
    	return myDate;
    }
    
    /**
	 * 
	 * @param value
	 * @return
	 */
	public static long getLongValue (String value) {
		if (StringUtils.isEmpty(value)) {
			value = "0";
		}
		
		return Long.valueOf(value).longValue();
	}
	
	/**
	 * 
	 * @param value
	 * @return
	 */
	public static int getIntValue (String value) {
		if (StringUtils.isEmpty(value)) {
			value = "0";
		}
		
		return Integer.valueOf(value).intValue();
	}

}
