/**
 * 
 */
package com.seuksa.distributed.other;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * @author sok.pongsametrey
 *
 */
public class ReadTextMain {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		long start1 = System.nanoTime();
		String filename = "D:/temp/test.txt";
		try {
//			System.out.println("PrintWriter with StringBuilder ");
//			PrintWriter pw = new PrintWriter(filename);
//			StringBuilder sb = new StringBuilder();
//			for (int j = 1000; j < 1040; j++)
//				sb.append(j).append(' ');
//			String outLine = sb.toString();
//			for (int i = 0; i < 1000 * 1000; i++)
//				pw.println(outLine);
//			pw.close();
//			long time1 = System.nanoTime() - start1;
//			System.out.printf("> Took %f seconds to write%n", time1 / 1e9);
//
//			{
//				System.out.println("FileReader when read text.");
//				long start = System.nanoTime();
//				FileReader fr = new FileReader(filename);
//				char[] buffer = new char[1024 * 1024];
//				while (fr.read(buffer) > 0) {
//					
//				}
//					
//				fr.close();
//				long time = System.nanoTime() - start;
//				System.out.printf(
//						"> Took %f seconds to read text as fast as possible%n",
//						time / 1e9);
//			}
//			{
//				System.out.println("BufferedReader with line.split");
//				long start = System.nanoTime();
//				BufferedReader br = new BufferedReader(new FileReader(
//						filename));
//				String line;
//				while ((line = br.readLine()) != null) {
//					String[] words = line.split(" ");
//				}
//				br.close();
//				long time = System.nanoTime() - start;
//				System.out.printf("> Took %f seconds to read lines and split%n",
//						time / 1e9);
//			}
			
			{
				System.out.println("BufferedReader with Pattern");
				long start = System.nanoTime();
				BufferedReader br = new BufferedReader(new FileReader(
						filename));
				String line;
				Pattern splitSpace = Pattern.compile(" ");
				String[] words = null ;
				while ((line = br.readLine()) != null) {
					words = splitSpace.split(line, 0);
				}
				System.out.println(words.length);
				br.close();
				long time = System.nanoTime() - start;
				System.out
						.printf("> Took %f seconds to read lines and split (precompiled)%n",
								time / 1e9);
			}
			{
				System.out.println("BufferedReader with substring");
				long start = System.nanoTime();
				BufferedReader br = new BufferedReader(new FileReader(
						filename));
				String line;
				List<String> words = new ArrayList<String>();

				while ((line = br.readLine()) != null) {
					words.clear();
					int pos = 0, end;
					while ((end = line.indexOf(' ', pos)) >= 0) {
						words.add(line.substring(pos, end));
						pos = end + 1;
					}
				}
				System.out.println("> " + words.size());
				
				br.close();
				long time = System.nanoTime() - start;
				System.out
						.printf("> Took %f seconds to read lines and break using indexOf%n",
								time / 1e9);
			}
			
			
//			{
//				System.out.println("FileChannel with StringTokenizer");
//				long start = System.nanoTime();
//				Charset charset = Charset.forName("UTF-8");
//			    CharsetDecoder decoder = charset.newDecoder();
//				File FILE = new File(filename);  
//			    FileChannel fileChannel = new RandomAccessFile(FILE, "r").getChannel();  
//			    			    
//				ByteBuffer buffer = ByteBuffer.allocate(64);
//				
//			    String line; 
//			    List<String> words = new ArrayList<String>();
//			    int ind = 1;
//			    
//			    int bytesRead = fileChannel.read(buffer);
//	            while (bytesRead != -1) {
//	                buffer.flip();
//	                buffer.flip();
//				    CharBuffer cb = decoder.decode(buffer);			    
//				    BufferedReader br = new BufferedReader(new CharArrayReader(cb.array()));
//				    while ((line = br.readLine()) != null) {
////				      	StringTokenizer st = new StringTokenizer(line);
////						while (st.hasMoreTokens()) {
////							words.add(st.nextToken());
////						}
//				    	int pos = 0, end;
//						while ((end = line.indexOf(' ', pos)) >= 0) {
//							words.add(line.substring(pos, end));
//							pos = end + 1;
//						}
//						ind++;
//				     }				    
//	                buffer.clear();
//	                br.close();
//	                bytesRead = fileChannel.read(buffer);
//	            }
//			    
//			    fileChannel.close();
//			    
//			    long time = System.nanoTime() - start;
//			    System.out.printf("> Took %f seconds to read lines and break using StringTokenizer%n",
//						time / 1e9);
//			}
			
//			{
//				System.out.println("FastFileReader with StringTokenizer");
//				long start = System.nanoTime();
//				FastFileReader fileReader = new FastFileReader(filename);
//				List<String> words = new ArrayList<String>();
//				for (String line : fileReader) {
//					System.out.println(line);
//					StringTokenizer st = new StringTokenizer(line);
//					while (st.hasMoreTokens()) {
//						words.add(st.nextToken());
//					}
//				}
//				long time = System.nanoTime() - start;
//			    System.out.printf("> Took %f seconds to read lines and break using indexOf%n",
//						time / 1e9);
//				   
//			}

			
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * @return
	 * @throws IOException
	 */
	private static int readSizeInfo(FileChannel channel) throws IOException {
		ByteBuffer sizeBuffer = ByteBuffer.allocate(4);
		channel.read(sizeBuffer);
		sizeBuffer.rewind();
		int skipBytes = sizeBuffer.getInt();
		return skipBytes;
	}
	
}
