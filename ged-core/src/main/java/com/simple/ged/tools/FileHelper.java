package com.simple.ged.tools;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.simple.ged.Profile;
import com.simple.ged.models.GedDocumentFile;

/**
 * This class provide tools for managing files
 * 
 * @author xavier
 * 
 */
public final class FileHelper {

	
	private static final Logger logger = LoggerFactory.getLogger(FileHelper.class);
	

	
	/**
	 * Replace \\ by /, to keep unix path like path !
	 */
	public static String forceUnixSeparator(String s) {
        // replace all \ by /
        s = s.replaceAll(Matcher.quoteReplacement("\\"), Matcher.quoteReplacement("/"));

        // replace all // by /
        while (s.contains(Matcher.quoteReplacement("//"))) {
            s = s.replaceAll("//", "/");
        }

        return s;
	}
	
	
	/**
	 * Get relative file path from the absolute path
	 * 
	 * @warning Returns a unix path like (separator : '/')
	 */
	public static String getRelativeFromAbsolutePath(String absolutePath) {
		return forceUnixSeparator(forceUnixSeparator(absolutePath).replaceFirst(forceUnixSeparator(Profile.getInstance().getLibraryRoot()), ""));
	}
	
	
	/**
	 * Copy the file in the library if necessary (if the file is not even in
	 * library)
	 * 
	 * @param file
	 *            The file which need to be copied
	 * 
	 * @param directoryTarget
	 *            Where does I need to copy the file ?
	 * 
	 * @param fileName
	 *            The file name
	 * 
	 * @return The file copied (or not), with relative file path
	 */
	private static GedDocumentFile copyFileIfNecessary(File file, File directoryTarget, String fileName) {

		String fileUnixPath = forceUnixSeparator(file.getAbsolutePath());
		
		if (fileUnixPath.startsWith(Profile.getInstance().getLibraryRoot())) {
			// nothing to copy
			return new GedDocumentFile(fileUnixPath.replaceFirst(Profile.getInstance().getLibraryRoot(), ""));
		}
	
		int mid = fileUnixPath.lastIndexOf(".");
		String extension = fileUnixPath.substring(mid + 1, fileUnixPath.length());
		
		// we need to copy the file
		String fullPath = directoryTarget.getAbsolutePath() + "/" + fileName + "." + extension;
		
		int preadd = 2;	// if the file ever exists, do not erase, additional number
		while (fileExists(fullPath)) {
			fullPath = directoryTarget.getAbsolutePath() + "/" + fileName + " (" + preadd + ")." + extension;
			preadd++;
		}
		
		File target = new File(fullPath);
		try { 
			logger.debug("Copy : " + target.getAbsolutePath());
			copyFile(file, target);
		} catch (IOException e) {
			logger.error("Cannot copy file : " + file.getAbsolutePath() + " to " + target.getAbsolutePath());
		}
		
		return new GedDocumentFile(forceUnixSeparator(target.getAbsolutePath()).replaceFirst(Profile.getInstance().getLibraryRoot(), ""));
	}

	/**
	 * Copy the file in the library if necessary (if the file is not even in
	 * library)
	 * 
	 * @param filesToCopy
	 *            The list of files which need do be copied
	 * 
	 * @param directoryTarget
	 *            Where does I need to copy the file ?
	 * 
	 * @param fileName
	 *            The target file name
	 * 
	 * @return The list of copied files (with relative path)
	 */
	public static List<GedDocumentFile> copyFilesIfNecessary(
			List<File> filesToCopy, File directoryTarget, String fileName) {

		List<GedDocumentFile> l = new ArrayList<>();

		if (filesToCopy.size() <= 1) {
			l.add(copyFileIfNecessary(filesToCopy.get(0), directoryTarget, cleanFileName(fileName)));
		} else {
			int i = 1;
			String cleanedName = cleanFileName(fileName);
			for (File f : filesToCopy) {
				l.add(copyFileIfNecessary(f, directoryTarget, cleanedName + " - " + i));
				i++;
			}
		}

		return l;
	}

	/**
	 * File copy
	 * 
	 * @see : http://java.developpez.com/sources/?page=fluxFichiers#copieFichier
	 */
	public static void copyFile(File src, File dest) throws IOException {
		FileInputStream fis = new FileInputStream(src);
		FileOutputStream fos = new FileOutputStream(dest);

		java.nio.channels.FileChannel channelSrc = fis.getChannel();
		java.nio.channels.FileChannel channelDest = fos.getChannel();

		channelSrc.transferTo(0, channelSrc.size(), channelDest);

		fis.close();
		fos.close();
	}

	/**
	 * Replace forbidden file char per nothing
	 */
	public static String cleanFileName(String fileName) {
		 char[] forbiddenChars = new char[] {'/', '\\', ':', '*', '?', '"', '<', '>', '|'};
		 for (char c : forbiddenChars) {
			 fileName = fileName.replace(c, ' ');
		 }
		 return fileName;
	}
	
	/**
	 * Delete file (recursive)
	 * 
	 * @see http://java.developpez.com/sources/?page=fluxFichiers#manipFile
	 */
	public static void recursifDelete(File path) throws IOException {
	      if (!path.exists()) {
	    	  throw new IOException("File not found '" + path.getAbsolutePath() + "'");
	      }
	      if (path.isDirectory()) {
	         File[] children = path.listFiles();
	         for (int i=0; children != null && i<children.length; i++) {
	            recursifDelete(children[i]);
	         }
	         if (!path.delete()) {
	        	 throw new IOException( "No delete path '" + path.getAbsolutePath() + "'");
	         }
	      }
	      else if (!path.delete()) { 
	    	  throw new IOException("No delete file '" + path.getAbsolutePath() + "'");
	      }
	         
	   }
	
	
	/**
	 * Move or rename some file
	 */
	public static void move(String source, String destination) {
		File from = new File(source);
		File to   = new File(destination);
		if (! from.renameTo(to)) {
			logger.error("Error while renaming/moving file");
		}
	} 
	
	/**
	 * Does a file ever exists ?
	 */
	public static boolean fileExists(String path) {
		return new File(path).exists();
	}


    /**
     * Extract some resource to the given path
     *
     * @throws Exception
     *                  ... if something wrong append
     */
    public static void extractZipEmbeddedResource(String resourceLocation, String destination) throws Exception {
        byte[] buffer = new byte[1024];

        ZipInputStream zis = new ZipInputStream(FileHelper.class.getResourceAsStream(resourceLocation));
        ZipEntry ze = zis.getNextEntry();

        while(ze != null) {
            String fileName = ze.getName();
            File newFile = new File(destination + "/" + fileName);

            logger.debug("unzip file : {}", newFile.getAbsoluteFile());

            new File(newFile.getParent()).mkdirs();
            FileOutputStream fos = new FileOutputStream(newFile);

            int len;
            while ((len = zis.read(buffer)) > 0) {
                fos.write(buffer, 0, len);
            }

            fos.close();
            ze = zis.getNextEntry();
        }

        zis.closeEntry();
        zis.close();
    }


    /**
     * Extract some resource to the given path
     *
     * @throws Exception
     *                  ... if something wrong append
     */
    public static void extractZip(String resourceLocation, String destination) throws Exception {

        byte[] buffer = new byte[1024];

        try{

            //create output directory is not exists
            File folder = new File(destination);
            if(!folder.exists()){
                folder.mkdir();
            }

            //get the zip file content
            ZipInputStream zis =
                    new ZipInputStream(new FileInputStream(resourceLocation));
            //get the zipped file list entry
            ZipEntry ze = zis.getNextEntry();

            while(ze!=null){

                String fileName = ze.getName();
                File newFile = new File(destination + File.separator + fileName);

                logger.debug("file unzip : "+ newFile.getAbsoluteFile());

                //create all non exists folders
                //else you will hit FileNotFoundException for compressed folder
                new File(newFile.getParent()).mkdirs();

                FileOutputStream fos = new FileOutputStream(newFile);

                int len;
                while ((len = zis.read(buffer)) > 0) {
                    fos.write(buffer, 0, len);
                }

                fos.close();
                ze = zis.getNextEntry();
            }

            zis.closeEntry();
            zis.close();

            logger.debug("Done");

        }catch(IOException e){
            logger.error("Failed to extract zip", e);
        }
    }
}
