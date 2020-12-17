package com.lucene;

import java.io.File;
import java.io.FileReader;
import java.io.BufferedReader;
import org.apache.lucene.index.DirectoryReader;
import java.io.IOException;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.en.EnglishAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

public class Index {

	 public static int indexFiles(String indexPath, File dataDir) throws IOException {
		 
		  Directory index = FSDirectory.open(Paths.get(indexPath));
		  
		  Analyzer analyzer = new EnglishAnalyzer();
	  
		  IndexWriterConfig indexWriterConfig = new IndexWriterConfig(analyzer);
		  indexWriterConfig.setOpenMode(IndexWriterConfig.OpenMode.CREATE);
		  
		  IndexWriter indexWriter = new IndexWriter(index, indexWriterConfig);
		  
		  indexDirectory(indexWriter, dataDir,analyzer);
		  
		  int numIndexed = indexWriter.maxDoc();
		  indexWriter.close();

	      return numIndexed;

	  }
	  
	  public static void indexDirectory(IndexWriter indexWriter, File dataDir) {
		  
		  File[] files = dataDir.listFiles();
		  
		  for(int i =0; i<files.length; i++) {
			  File file = files[i];
			  
			  if(file.isDirectory()) {
				  indexDirectory(indexWriter, file, analyzer);
			  }
			  
			  else {
				  try {
					indexFileWithIndexWriter(indexWriter, file);
				} catch (IOException e) {
					e.printStackTrace();
				}
			  }
		  }
  
		  
	  }
	  
	  public static void indexFileWithIndexWriter(IndexWriter indexWriter, File file) throws IOException {
		  
		  if(file.isHidden() || file.isDirectory() || !file.canRead() || !file.exists()) {
			  return;
		  }
		  
		  if(file.getName().endsWith(".txt") || file.getName().endsWith(".html") || file.getName().endsWith(".htm")) {
			  
			  
			  BufferedReader  reader = new BufferedReader (new FileReader(file));
			  StringBuilder stringBuilder = new StringBuilder();
			  
			  String line = null;
			  String ls = System.getProperty("line.separator");
			  while ((line = reader.readLine()) != null) {
			  	stringBuilder.append(line);
			  	stringBuilder.append(ls);
			  }

			  stringBuilder.deleteCharAt(stringBuilder.length() - 1);
			  reader.close();
			  
			  String docContents = stringBuilder.toString();
			  
			  System.out.println("Indexing File:...." + file.getCanonicalPath());
			  Document doc = new Document();
			  doc.add(new TextField("DocName", file.getName(), Field.Store.YES));
			  doc.add(new TextField("DocContents",docContents, Field.Store.YES ));
			  doc.add(new TextField("DocPath", file.getCanonicalPath(), Field.Store.YES));
			 
			  indexWriter.addDocument(doc);
		  }
		  
		  else {
			  return;
		  }
	  
		  
		    
		  
	  }
}
