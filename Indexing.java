import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Date;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.en.EnglishAnalyzer;
import org.apache.lucene.document.*;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig.OpenMode;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.jsoup.Jsoup;
import java.text.SimpleDateFormat;

public class Indexing {
	/*
	 * Method that has as input inputPath and indexPath. It creates a index for all of the files in the inputPath and all 
	 * subfolders. 
	 * */
    public static void indexing(String inputPath, String indexPath) {

        final Path dataDir = Paths.get(inputPath);
        Date start = new Date();
        try {
        	Directory index = FSDirectory.open(Paths.get(indexPath));
            Analyzer analyzer = new EnglishAnalyzer();
            IndexWriterConfig indexWriterConfig = new IndexWriterConfig(analyzer);
            indexWriterConfig.setOpenMode(OpenMode.CREATE);
            //Create an indexWriter with the english analyzer
            IndexWriter indexWriter = new IndexWriter(index, indexWriterConfig);
            //Call a function to index the documents inside the given folder using the created indexWriter 
            indexDirectory(indexWriter, dataDir);
            //Close the index
            indexWriter.close();
            //Print the amount of time spent on the index
            Date end = new Date();
            System.out.println(end.getTime() - start.getTime() + " total milliseconds");
        } catch (IOException e) {
            System.out.println("Error in Indexing Class: ");
            System.out.println(e.getMessage());
            //e.printStackTrace();
            //System.out.println(e.getMessage());
        }

    }

    /*
	 * Method that iterates over the folder given for the user for indexing and check whether the files inside the folder and 
	 * subfolders are text or html files (.txt,.html,.htm). 
	 * If the files are accepted then it calls the function indexDocument to index the file. Otherwise, It will just display a message 
	 * */
    public static void indexDirectory(final IndexWriter indexWriter, Path dataDir) throws IOException {
        if (Files.isDirectory(dataDir)) {
            Files.walkFileTree(dataDir, new SimpleFileVisitor<Path>() {
                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                    String fileLowerCaseName = file.toString().toLowerCase();
                    if (fileLowerCaseName.endsWith(".txt") || fileLowerCaseName.endsWith(".html") || fileLowerCaseName.endsWith(".htm")) {
                        try {
                            indexDocument(indexWriter, file, attrs.lastModifiedTime().toMillis());
                        } catch (IOException ignore) {
                            // don't index files that can't be read.
                        }
                    } else {
                        System.out.println("This type of file will not be indexed" + file.toString());
                    }
                    return FileVisitResult.CONTINUE;
                }
            });
        } else {
            indexDocument(indexWriter, dataDir, Files.getLastModifiedTime(dataDir).toMillis());
        }
    }

    /** Indexes a single document */
    public static void indexDocument(IndexWriter writer, Path dataDir, long lastModified) throws IOException {
        try (InputStream stream = Files.newInputStream(dataDir)) {
            Document doc = new Document();

            String fileLowerCaseName = dataDir.toString().toLowerCase();
            //In case of html files. It uses the jSoup library for parse title and summary fields
            if (fileLowerCaseName.endsWith(".htm") || fileLowerCaseName.endsWith(".html")) {
                String file = new String(Files.readAllBytes(dataDir));
                org.jsoup.nodes.Document jSoupdoc = Jsoup.parse(file, "UTF-8");

                String title = String.valueOf(jSoupdoc.title());
                doc.add(new StringField("title", title, Field.Store.YES));
                String summary = String.valueOf(jSoupdoc.select("summary").text());
                doc.add(new StringField("summary", summary, Field.Store.YES));
            }
            try {
            	//For both text files and html files, it includes in the Index the path, content and last modified date
                Field pathField = new StringField("path", dataDir.toString(), Field.Store.YES);
                doc.add(pathField);

                String lastMod = new SimpleDateFormat("dd MMM yyyy HH:mm:ss ").format(lastModified);
                doc.add(new StringField("lastmodified", lastMod, Field.Store.YES));
                doc.add(new TextField("contents", new BufferedReader(new InputStreamReader(stream, StandardCharsets.UTF_8))));

                System.out.println("adding " + dataDir);
                writer.addDocument(doc);
            }
            catch(Exception e) {
                System.out.println("Error in Indexing Class - in indexDocument module");
                e.printStackTrace();
                System.out.println(e.getMessage());

                }
            }
        }
}

