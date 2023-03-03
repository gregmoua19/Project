package Project;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.graphics.shading.RadialShadingContext;
import org.apache.pdfbox.text.PDFTextStripper;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import java.util.*;
import java.util.regex.Pattern;
import java.util.regex.Matcher;
import java.io.*;
import java.net.NetPermission;

public class first_project{
  public static void main(String[] args) throws IOException{
    //readPDF_createTxtFile();
    ArrayList<String> names = create_name_array();
    ArrayList<String> check_numbers = new ArrayList<>();
    ArrayList<ArrayList<Integer>> array2D = new ArrayList<ArrayList<Integer>>();
    
    // System.out.println(names.toString());
    check_numbers.add("^1\\.?\\s*Employment\\s+and\\s+earnings\\s$");
    check_numbers.add("^2\\.?\\s*\\(a\\)\\s+Support\\s+linked\\s+to\\s+an\\s+MP\\s+but\\s+received\\s+by\\s+a\\s+local\\s+party\\s+organisation\\s+or\\s+indirectly\\s$");
    check_numbers.add("^2\\.?\\s*\\(b\\)\\s+Any\\s+other\\s+support\\s+not\\s+included\\s+in\\s+Category\\s+2\\(a\\)\\s$");
    check_numbers.add("^3\\.?\\s*Gifts,\\s+benefits\\s+and\\s+hospitality\\s+from\\s+UK\\s+sources\\s$");
    check_numbers.add("^4\\.?\\s*Visits\\s+outside\\s+the\\s+UK\\s");
    check_numbers.add("^5\\.?\\s*Gifts\\s+and\\s+benefits\\s+from\\s+sources\\s+outside\\s+the\\s+UK\\s$");
    check_numbers.add("^6\\.?\\s*Land\\s+and\\s+property\\s+portfolio:\\s+\\(i\\)\\s+value\\s+over\\s+£100,000\\s+and/or\\s+\\(ii\\)\\s+giving\\s+rental\\s+income\\s$");
    check_numbers.add("^7\\.?\\s*\\(i\\)\\s+Shareholdings:\\s+over\\s+15%\\s+of\\s+issued\\s+share\\s+capital\\s$");
    check_numbers.add("^7\\.?\\s*\\(ii\\)\\s+Other\\s+shareholdings,\\s+valued\\s+at\\s+more\\s+than\\s+£70,000\\s$");
//7. (ii) Other shareholdings, valued at more than £70,000
    check_numbers.add("^8\\.?\\s*Miscellaneous\\s$");
    check_numbers.add("^9\\.?\\s*Family\\s+members\\s+employed\\s+and\\s+paid\\s+from\\s+parliamentary\\s+expenses\\s$");
    check_numbers.add("^Nil\\s$");
    check_numbers.add("^10\\.?\\s*Family\\s+members\\s+engaged\\s+in\\s+lobbying\\s+the\\s+public\\s+sector\\s+on\\s+behalf\\s+of\\s+a\\s+third\\s+party\\s+or\\s$");

    String [] types = {"1. Employment and earnings",
         "2. (a) Support linked to an MP but received by a local party organisation or indirectly via a central party organisation",
         "2. (b) Any other support not included in Category 2(a)",
         "3. Gifts, benefits and hospitality from UK sources", 
         "4. Visits outside the UK",
         "5. Gifts and benefits from sources outside the UK",
         "6. Land and property portfolio: (i) value over £100,000 and/or (ii) giving rental income of over £10,000 a year",
         "7. (i) Shareholdings: over 15% of issued share capital",
         "7. (ii) Other shareholdings, valued at more than £70,000",
         "8. Miscellaneous",
         "9. Family members employed and paid from parliamentary expenses",
         "10. Family members engaged in lobbying the public sector on behalf of a third party or client",
         "Nil"};

    String [][] sorting = new String [check_numbers.size()+1][names.size()+1];
    int counter = 0;
    for(int i = 1; i < types.length;i++) {
      sorting[i][0] = types[counter];
      counter++;
    }

    counter = 0;
    for(int i = 1; i < names.size();i++){
      sorting[0][i] = names.get(counter);
      counter++;
    }
    //System.out.println(Arrays.deepToString(sorting));
    // System.out.println(names.toString());
    File file = new File("text.txt");
    FileReader reader = new FileReader(file);
    BufferedReader bufferedReader = new BufferedReader(reader);
    Scanner pdfScanner = new Scanner(bufferedReader);
    String test_temp = "";
    int colIndex = 0;
    int rowIndex = 0;
    for(int i = 0; i < 5;i++){
      pdfScanner.nextLine();
    }
    while(pdfScanner.hasNextLine()){
      String current_line = pdfScanner.nextLine();
      // System.out.println("currentLine: " + current_line);
      test_temp = "";
      String name = "";
      // check if the current line matches with the name expressions
      for (int i = 0; i < names.size(); i++) {
          Pattern pattern = Pattern.compile(names.get(i));
          Matcher matcher = pattern.matcher(current_line);
          
          //if it matches it breaks out
          if(matcher.matches()){
            // System.out.println(pattern);
            //System.out.println("col index is now " + i);

            name = current_line;
            colIndex = i+1;
            //go to next line
            current_line = pdfScanner.nextLine();
        
            break;
          } 
      }
      
      if(name.length() > 0){
        //System.out.println("name: " + name);
      }
      
      //name found go to nextline
      // current_line = pdfScanner.nextLine();
      for(int i = 0; i < check_numbers.size();i++){

          Pattern pattern = Pattern.compile(check_numbers.get(i));
          Matcher matcher = pattern.matcher(current_line);
          //check for numbers exp

          if(matcher.matches()){
            //System.out.println("new number: " + current_line);
            rowIndex = i+1;
            //System.out.println("row index is now " + i);
            //go to next line
            current_line = pdfScanner.nextLine();
            
            test_temp = "";
          }
      }
      if(sorting[rowIndex][colIndex] == null) {
        sorting[rowIndex][colIndex] ="";  
      }
      sorting[rowIndex][colIndex] += current_line;
      
      //System.out.println(test_temp);
    }   
    pdfScanner.close();
    bufferedReader.close();

    for(int i = 0; i < sorting.length;i++) {
      //System.out.println("We are at " + sorting[i][0] +"\n\n\n\n\n");
      for(int j = 0; j < sorting[i].length;j++) {
        if (sorting[i][j] == null) {
          sorting[i][j] = "";
          sorting[i][j] = i + " does not exist";
        }
        if(j == sorting[i].length-150) {
          System.out.println(sorting[i][j]);
        }
      }
    }
    //System.out.print(Arrays.deepToString(sorting));

    // System.out.println(Arrays.toString(temp_array));
    // System.out.println(names.toString());
  }

  public static void readPDF_createTxtFile() throws IOException{
    File file = new File("Information.pdf");
    PDDocument document = PDDocument.load(file);
    PDFTextStripper pdfStripper = new PDFTextStripper();
    String text = pdfStripper.getText(document);
    System.out.println(text);
    FileWriter writer = new FileWriter("text.txt");
    writer.write(text);
    writer.close();

    document.close();
  }

  public static ArrayList<String> create_name_array() throws IOException{
    
    ArrayList<String> regular_expression = new ArrayList<>();
    File file = new File("names.txt");
    FileReader reader = new FileReader(file);
    BufferedReader bufferedReader = new BufferedReader(reader);
    Scanner pdfScanner = new Scanner(bufferedReader);
    while(pdfScanner.hasNextLine()){
      String firstName = pdfScanner.next();
      String lastName = pdfScanner.next();
      regular_expression.add(".*"+lastName+"[^A-Za-z0-9_]+.*"+firstName+".*");
      // System.out.println(firstName+ " " + lastName);
     
      pdfScanner.nextLine();
      
    }
    pdfScanner.close();
    return regular_expression;
  }

  
}