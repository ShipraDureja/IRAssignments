package com.lucene;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;

import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopScoreDocCollector;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.QueryBuilder;

public class Main {
	
	public static void main(String[] args) throws IOException {
		String inputPath = null;
		Index indexGivenDirectory = new Index();
		
		if (args.length != 1) {
            System.out.println("Invalid amount of arguments");
        } else {
            inputPath = args[0];
            if (inputPath == null) {
                System.out.println("Incorrect number of parameters");
            }else {
            	//Index
            	int numIndex = indexGivenDirectory.indexFiles(inputPath, new File(inputPath));
            }
        }

		
		
	}
}
