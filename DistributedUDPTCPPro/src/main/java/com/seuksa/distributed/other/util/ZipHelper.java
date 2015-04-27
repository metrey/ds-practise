package com.seuksa.distributed.other.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Enumeration;
import java.util.LinkedList;
import java.util.List;
import java.util.zip.CRC32;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import org.apache.xerces.impl.dv.util.Base64;


/**
 * 
 * @author sok.pongsametrey
 *
 */
public class ZipHelper {
    
    public static void CreateZipFile(String path){
        
    }
    
    /**
     * This the compress xml file request and respone 
     * @param filename
     * @param zipFileName
     */
    public static void doZip(String filename, String zipFileName) throws Exception {
        
        File f = new File(filename);        
        FileInputStream fis = null;
        ZipOutputStream zipOutputStream = null;
        try {
            if (f.exists()) {
                int size = (int)f.length();
                byte[] buf = new byte[size];
                fis = new FileInputStream(f.getAbsolutePath());                
                fis.read(buf, 0, buf.length);
                CRC32 crc = new CRC32();                
                zipOutputStream = new ZipOutputStream(new FileOutputStream(zipFileName));
                zipOutputStream.setLevel(6);
                ZipEntry entry = new ZipEntry(f.getName());
                entry.setSize(buf.length);
                crc.reset();
                crc.update(buf);
                entry.setCrc(crc.getValue());
                zipOutputStream.putNextEntry(entry);
                zipOutputStream.write(buf, 0, buf.length);
                zipOutputStream.finish();
             } else {
                System.out.println(">> Input file :  " + f.getPath() + "/" + f.getName() + " Not exists .");
            }
        } catch (FileNotFoundException fNotFound) {
            throw new Exception(">> Error during compressing file ", fNotFound);
        } catch (IOException ioe) {
            throw new Exception(">> Error during compressing file ", ioe);
        } catch (Exception e) {
            throw new Exception(">> Error during compressing file ", e);
        } 
            finally {
            try {
                if (zipOutputStream != null) {
                    zipOutputStream.close();    
                }
                if (fis != null) {
                    fis.close();
                }
                
                boolean success = f.delete();
                if (!success){          
                    System.out.println("File : " + f.getName() + " Deletion failed.");
                  }else{
                    System.out.println("File : " + f.getName() + " deleted. ");          
                  }                
                
            } catch (IOException e) {
               throw new Exception(">> Error during closing Zip output Stream / deleting file . ",e);
            }
        }       
    }
    
    public static void doUnzip (String zipFilename) {
    	Enumeration entries;
    	ZipFile zipFile;
    	if(zipFilename == null) {
    	System.err.println("Usage: Unzip zipfile");
    	return;
    	}
    	try {
    	zipFile = new ZipFile(zipFilename);
    	entries = zipFile.entries();
    	while(entries.hasMoreElements()) {
    	ZipEntry entry = (ZipEntry)entries.nextElement();
    	if(entry.isDirectory()) {
    		// Assume directories are stored parents first then children.
    		System.err.println("Extracting directory: " + entry.getName());
    		// This is not robust, just for demonstration purposes.
    		(new File(entry.getName())).mkdir();
    		continue;
    	}
    	System.err.println("Extracting file: " + entry.getName());
    	copyInputStream(zipFile.getInputStream(entry),
    	new BufferedOutputStream(new FileOutputStream(entry.getName())));
    	}
    	zipFile.close();
    	} catch (IOException ioe) {
    	System.err.println("Unhandled exception:");
    	ioe.printStackTrace();
    	return;
    	}
    }

    /**
     * 
     * @param in
     * @param out
     * @throws IOException
     */
	public static final void copyInputStream(InputStream in, OutputStream out)
			throws IOException {
		byte[] buffer = new byte[1024];
		int len;
		while ((len = in.read(buffer)) >= 0)
			out.write(buffer, 0, len);
		in.close();
		out.close();
	}

    
    /**
     * This the compress request and response to zip file 
     * @param filename
     * @param zipFileName
     */
    public static void doStringToZip(String filename, String zipFileName,byte [] buf) throws Exception {
        ZipOutputStream zipOutputStream = null;
        try {
            CRC32 crc = new CRC32();
            zipOutputStream = new ZipOutputStream(new FileOutputStream(zipFileName));
            zipOutputStream.setLevel(6);
            ZipEntry entry = new ZipEntry(filename);
            entry.setSize(buf.length);
            crc.reset();
            crc.update(buf);
            entry.setCrc(crc.getValue());
            zipOutputStream.putNextEntry(entry);
            zipOutputStream.write(buf, 0, buf.length);
            zipOutputStream.finish();

        } catch (FileNotFoundException fNotFound) {
            throw new Exception(">> Error during compressing file ", fNotFound);
        } catch (IOException ioe) {
            throw new Exception(">> Error during compressing file ", ioe);
        } catch (Exception e) {
            throw new Exception(">> Error during compressing file ", e);
        } finally {
            try {
                if (zipOutputStream != null) {
                    zipOutputStream.close();
                }
            } catch (IOException e) {
                throw new Exception(
                        ">> Error during closing Zip output Stream / deleting file . ", e);
            }
        }
    }
    /**
     * Reads a file storing intermediate data into an array.
     * 
     * @param file
     *            the file to be read
     * @return a file data
     */
    public static byte[] read2Array(String file) throws Exception {        
        InputStream in = null;
        byte[] out = new byte[0];

        try {
            if (FileUtils.isExistFiles(file).booleanValue()) {

                in = new BufferedInputStream(new FileInputStream(file));

                // the length of a buffer can vary
                int bufLen = 20000 * 1024;
                byte[] buf = new byte[bufLen];
                byte[] tmp = null;
                int len = 0;

                while ((len = in.read(buf, 0, bufLen)) != -1) {
                    // extend array
                    tmp = new byte[out.length + len];

                    // copy data
                    System.arraycopy(out, 0, tmp, 0, out.length);
                    System.arraycopy(buf, 0, tmp, out.length, len);
                    out = tmp;
                    tmp = null;
                }
            }
        } catch (Exception e) {
            StringBuffer err = new StringBuffer(">> Error durring execute method read2Array ");
            System.out.println(err.toString());
            throw new Exception(err.toString(), e);
        } finally {
            // always close the stream
            if (in != null) {
                try {
                    in.close();
                } catch (Exception e) {
                    StringBuffer err = new StringBuffer(">> Error durring closing buffer stream ");
                    System.out.println(err.toString());
                    throw new Exception(err.toString(), e);
                }
            }
        }
        return out;
    }
    /**
     * This transform the input data after compressing to
     * Basse 64 character
     * @param zipFileName
     * @return
     */
    public static String transformedInputDataToBase64Character(String zipFileName) throws Exception{
        String strEncode = null;
        byte[] fileToByte = null;
        try {
            if (FileUtils.isExistFiles(zipFileName).booleanValue()) {
                fileToByte = ZipHelper.read2Array(zipFileName);
            } else {
                System.out.println("Zip file : " + zipFileName + " input not exits ");
            }
            
        } catch (Exception e1) {
            StringBuffer msg = new StringBuffer(">> Error durring execute method transformedInputDataToBase64Character.");
            System.out.println(msg.toString());
            throw new Exception(msg.toString(),e1);            
        }        
        strEncode = Base64.encode(fileToByte);
        return strEncode;
    }
    
    public static List<String> decode64CharacterAndUnzipFile (String base64String, String serverDir) throws Exception {
    	
    	byte[] decodedByte = Base64.decode(base64String);
    	if (decodedByte == null
    			|| decodedByte.length == 0) {
    		throw new Exception("Base64 found no any byte");
    	}
//        ZipOutputStream zos = new ZipOutputStream(new BufferedOutputStream(new FileOutputStream(zipPath + "/" + zipfilename)));  
//        //now create the entry in zip file  
//          
//        ZipEntry entry = new ZipEntry(zipfilename);
//        entry.setSize(decodedByte.length);
//        zos.putNextEntry(entry);  
//        zos.write(decodedByte);
//        zos.closeEntry();
//        zos.close();
        
        ZipInputStream zipStream = new ZipInputStream(new ByteArrayInputStream(decodedByte));
        ZipEntry entry = null;
        List<String> lstEntries = new LinkedList<String>();
        while ((entry = zipStream.getNextEntry()) != null) {
            String entryName = entry.getName();            
            FileOutputStream out = new FileOutputStream(serverDir + "/" + entryName);
            byte[] buf = new byte[4096];
            int bytesRead = 0;
            while ((bytesRead = zipStream.read(buf)) != -1) {
                out.write(buf, 0, bytesRead);
            }
            out.close();
            zipStream.closeEntry();
            lstEntries.add(entryName);
        }
        zipStream.close(); 

        
        return lstEntries;
    }
    
    
}
