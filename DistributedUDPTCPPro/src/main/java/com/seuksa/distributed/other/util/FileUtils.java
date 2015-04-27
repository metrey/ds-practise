package com.seuksa.distributed.other.util;

import java.io.File;

/**
 * 
 * @author sok.pongsametrey
 *
 */
public class FileUtils {
    
   
    
    
    /**
     * This checked is directory or not
     * @param dir
     * @return
     */
    public static Boolean isDirectory(String fileName)
    {
      File outPutFile = new File(fileName);
      if(outPutFile.isDirectory()){
          return Boolean.TRUE;
      } else {
          return Boolean.FALSE;         
      }
    }
    
    public static final Boolean isExistFiles(String fileName){
        File file = new File(fileName);
        if(file.exists()){
            return Boolean.TRUE;
        } else {
            return Boolean.FALSE;         
        }
    }
    /**
     * This is deleted xml file after compressing
     * @param dir
     * @param fileName
     */
    public static void deletefile(String fileName){
        File f1 = new File(fileName);
        
        if (f1.exists()) {
            if (f1.isFile()){                
                boolean success = f1.delete();
                if (!success){          
                  System.out.println("File : " + fileName + " Deletion failed.");
                }else{
                	System.out.println("File : " + fileName + " deleted. ");          
                }                
            }
        }
        
      }
}
