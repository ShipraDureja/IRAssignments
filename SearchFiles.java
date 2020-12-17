import java.io.File;
import java.util.*;
import java.io.IOException;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.en.EnglishAnalyzer;
import java.nio.file.Paths;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TopScoreDocCollector;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;	

public class Index {	  

          public static void searchIndex(String indexPath, String queryTerms, int hits ) throws IOException, ParseException {
		  

		  Directory index = FSDirectory.open(Paths.get(indexPath));
		  
		  IndexReader indexReader = DirectoryReader.open(index);
		  IndexSearcher indexSearcher = new IndexSearcher(indexReader);
		  
		  TopScoreDocCollector collector = TopScoreDocCollector.create(hits);
		  

		  Analyzer analyzer = new EnglishAnalyzer();
	  
	          QueryParser queryParser = new QueryParser("DocContents",analyzer); 
	          Query query = queryParser.parse(queryTerms);

	          indexSearcher.search(query, collector);
			  
		  ScoreDoc[] scoreHits =collector.topDocs().scoreDocs;
			  
		  for(int j=0; j<scoreHits.length; j++) {
        
			  int docID = scoreHits[j].doc;
			  Document d = indexSearcher.doc(docID);
			  System.out.println(d.get("DocName"));
		  
		  }
		  
		  System.out.println("Found:" + scoreHits.length);

		  
		  analyzer.close();

		  indexReader.close();

		  
	  }
}
