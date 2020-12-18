import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

public class Main {
	public static void main(String args[]){
		String inputPath = null;
		String indexPath = "index";
		boolean performIndexing = true;
		//Check the amount of arguments passed by the user in the console
		if (args.length != 1) {
            System.out.println("Invalid amount of arguments. To execute the file use java -jar IR_P01.jar [path to document folder]");
        } else {
        	//First argument is the path to the document folder
            inputPath = args[0];
            //In case the empty string is included it checks whether the user wants to use the current folder as 
            //the document folder. In case it is not desired finish the execution 
            if (inputPath.equals("")) {
                System.out.println("Do you want to use the current folder as the document folder? [Y/N]");
                String UserInput;
                BufferedReader bReader = new BufferedReader(new InputStreamReader(System.in, StandardCharsets.UTF_8));
                try {
                	UserInput = bReader.readLine();
					if(!UserInput.equalsIgnoreCase("y")) {
						performIndexing = false;
						System.out.println("To run the project with a different path please use java -jar IR_P01.jar [path to document folder]");
					}
				} catch (IOException e) {
					System.out.println(e.getMessage());
				}
            } 
            try {
            	if (performIndexing) {
                	//Index input folder
                	Indexing.indexing(inputPath, indexPath);
                	//Query over indexed files
                	SearchIndex.searching(indexPath);
            	}
    		} catch (Exception e) {
    			System.out.println("An error was encounter during indexing process. Error:" + e.getMessage());
    		}
        }
		
		
	}
}