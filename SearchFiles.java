	  
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
	  
