import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.en.EnglishAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryparser.classic.MultiFieldQueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.FSDirectory;


/** Simple command-line based search. */
public class SearchIndex {

    public static void searching(String indexPath) throws Exception {
        try{
            IndexReader reader = DirectoryReader.open(FSDirectory.open(Paths.get(indexPath)));
            IndexSearcher searcher = new IndexSearcher(reader);
            Analyzer analyzer = new EnglishAnalyzer();

            BufferedReader bReader = new BufferedReader(new InputStreamReader(System.in, StandardCharsets.UTF_8));
            MultiFieldQueryParser queryParser = new MultiFieldQueryParser(new String[]{"contents", "title", "lastmodified"}, analyzer);

            String queryInput = "";
            int flag = 0;
            int num_hits = 10;
            while (flag!=1) {
                System.out.println("\nEnter query: ");
                queryInput = bReader.readLine();
                if (queryInput == null && queryInput.length() == 0) {
                    break;
                }
                System.out.println("Enter number of top docs:");
                Query query = queryParser.parse(queryInput);
                TopDocs docs = searcher.search(query, num_hits);
                ScoreDoc[] hits = docs.scoreDocs;
                if (hits != null && hits.length > 0) {
                    System.out.println("Total" + hits.length + " documents found");
                    System.out.println("______________Top " + hits.length + " Relevant Documents______________ ");
                    for (int i = 0; i < hits.length; ++i) {
                        int docId = hits[i].doc;
                        Document doc = searcher.doc(docId);
                        String filepath = doc.get("path");
                        System.out.println("\nRank: " + (i + 1) + "\nPath: " + filepath + " \nLast Modified: " + doc.get("lastmodified") + "\nRelevance Score: " + hits[i].score);
                        File file = new File(filepath);
                        String filename = file.getName();
                        if (filename.endsWith(".htm") || filename.endsWith(".html")) {
                            System.out.println("Title: " + doc.get("title"));
                            System.out.println("Summary: " + doc.get("summary"));
                        }
                    }
                } else
                    System.out.println("No results found\n");
                System.out.println("______________________________________________________\n\n");

                System.out.println("Do you want to continue searching?");
                System.out.print("Press y to continue:  ");
                String UserInput = bReader.readLine();
                if (UserInput.equalsIgnoreCase("y"))
                    flag = 0;
                else
                    flag = 1;
            }

        }
        catch (Exception e) {
            System.out.println("Error in Search Index Class");
            e.printStackTrace();
        }
    }


}