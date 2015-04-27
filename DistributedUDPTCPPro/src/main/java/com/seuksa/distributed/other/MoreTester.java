/**
 * 
 */
package com.seuksa.distributed.other;

import java.io.BufferedReader;
import java.io.CharArrayReader;
import java.io.FileInputStream;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

/**
 * @author sok.pongsametrey
 *
 */
public class MoreTester {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
//			String filename = "D:/tmp/test.txt";
//			String zipFile = filename + ".zip";
//			ZipHelper.doZip(filename, zipFile);
//	        String inputDateBase64Character = ZipHelper.transformedInputDataToBase64Character(zipFile);
//	        System.out.println(inputDateBase64Character);
//			String zipFile2 = "D:/temp";
//			List<String> lstFiles = ZipHelper.decode64CharacterAndUnzipFile(inputDateBase64Character, zipFile2);
//			for (String file : lstFiles) {
//				System.out.println(file);
//			}
//			
//			// reader
//			Charset charset = Charset.forName("UTF-8");
//		    CharsetDecoder decoder = charset.newDecoder();
//			File FILE = new File("D:/temp/test.txt");  
//		    FileChannel fileChannel = new FileInputStream(FILE).getChannel();  
//		        
//		    //MappedByteBuffer mbb = fc.map(FileChannel.MapMode.READ_ONLY, 0, fc.size());  
//		    ByteBuffer buffer = ByteBuffer.allocate((int)fileChannel.size());		    
//		    fileChannel.read(buffer);  
//		    buffer.flip();
//		    CharBuffer cb = decoder.decode(buffer);  
//		    BufferedReader br = new BufferedReader(new CharArrayReader(cb.array()));  
//		    String line;  
//		    while ((line = br.readLine()) != null) {  
//		      System.out.println(" FileChannel : "+line);  
//		     }  
			
			CharBuffer chBuff = getFileContents("D:/temp/test.txt");
			
			BufferedReader br = new BufferedReader(new CharArrayReader(chBuff.array()));
		    
		    String line; 
		    List<String> words = new ArrayList<String>();
		    int ind = 1;
		    while ((line = br.readLine()) != null) {
		    	//System.out.println(ind + " " + line);
//		      	StringTokenizer st = new StringTokenizer(line);
//				while (st.hasMoreTokens()) {
//					words.add(st.nextToken());
//				}		
				ind++;
		     }
		    br.close();
		    
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	static CharBuffer getFileContents(String fileName) throws Exception {
		FileInputStream fin = new FileInputStream(fileName);
		FileChannel fch = fin.getChannel();

		// map the contents of the file into ByteBuffer
		ByteBuffer byteBuff = fch.map(FileChannel.MapMode.READ_ONLY, 0, fch.size());

		// convert ByteBuffer to CharBuffer
		// CharBuffer chBuff = Charset.defaultCharset().decode(byteBuff);
		CharBuffer chBuff = Charset.forName("UTF-8").decode(byteBuff);
		return chBuff;
	    }

}
