public class Main {
    public static void main(String[] args) throws Exception {

        String inputPath = "C:\\Users\\Shipra Dureja\\Downloads\\documents";
        String indexPath = "index";
        Indexing.indexing(inputPath, indexPath);
        SearchIndex.searching(inputPath, indexPath);
        /*String inputPath = null;
        String indexPath = "index";
        if (args.length != 1) {
            System.out.println("Invalid arguments");
        } else {
            inputPath = args[0];
            if (inputPath != null) {
                Indexing.main(inputPath, indexPath);
            }try {
                SearchIndex.main(indexPath);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }*/
    }
}

