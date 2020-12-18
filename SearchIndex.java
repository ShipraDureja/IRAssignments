import java.io.BufferedReader;
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

    public static void searching(String inputPath, String indexPath) throws Exception {

        final Path dataDir = Paths.get(inputPath);
        IndexReader reader = DirectoryReader.open(FSDirectory.open(Paths.get(indexPath)));
        IndexSearcher searcher = new IndexSearcher(reader);
        Analyzer analyzer = new EnglishAnalyzer();

        BufferedReader bReader = new BufferedReader(new InputStreamReader(System.in, StandardCharsets.UTF_8));
        MultiFieldQueryParser queryParser = new MultiFieldQueryParser(new String[]{"contents", "title", "lastmodified"}, analyzer);

        String queryInput = "";
        while (!queryInput.equalsIgnoreCase("q")) {
            System.out.println("Enter query: ");
            queryInput = bReader.readLine();
            if (queryInput == null && queryInput.length() == 0) {
                break;
            }
            Query query = queryParser.parse(queryInput);
            TopDocs docs = searcher.search(query, 10);
            ScoreDoc[] hits = docs.scoreDocs;

            if (hits != null && hits.length > 0) {
                System.out.println("Total" + hits.length + " documents found");
                System.out.println("______________Top Relevant Documents______________ ");
                for (int i = 0; i < hits.length; ++i) {
                    int docId = hits[i].doc;
                    Document doc = searcher.doc(docId);
                    System.out.println("Rank: " + (i + 1) + "\nPath: " + doc.get("path") + " \nLast Modified: " + doc.get("lastmodified") + "\nRelevance Score: " + hits[i].score);
                    String fileLowerCaseName = dataDir.toString().toLowerCase();
                    if (fileLowerCaseName.endsWith(".htm") || fileLowerCaseName.endsWith(".html")) {
                        System.out.println("\nTitle: " + doc.get("title"));
                        System.out.println("\nSummary: " + doc.get("summary"));
                    }
                }
            }
        }
    }
}