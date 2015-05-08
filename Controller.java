import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

import javax.swing.JFrame;

public class Controller {
	public static final String baseURL = "http://download.finance.yahoo.com/d/quotes.csv";
	@SuppressWarnings("unchecked")
	private static ArrayList<Stock.Field> fields = new ArrayList(Arrays.asList(new Stock.Field[] { Stock.Field.SYMBOL, Stock.Field.PRICE, Stock.Field.PERCENT_CHANGE, Stock.Field.PE_RATIO, Stock.Field.PE_CURRENT_YEAR, Stock.Field.PE_NEXT_YEAR, Stock.Field.EPS, Stock.Field.NAME }));
	public static final int updateInterval = 1500;

	public static void main(String[] args){
	
		/*Scanner input = new Scanner(System.in);
    	
    	System.out.print("Update interval(milliseconds): ");
    	updateInterval = input.nextInt();
    	System.out.print("File name: ");
    	Portfolio portfolio = new Portfolio(input.nextLine());*/
		
//		StockDataSelectorPane selectorPane = new StockDataSelectorPane();
		
//		selectorPane.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
    	
		Portfolio portfolio = new Portfolio("symbols.txt");
	}
	
	

	public static void addField(Stock.Field field){
		if (fields.indexOf(field) < 0)
		fields.add(field);
	}

	public static List<Stock.Field> getFields(){
		return new ArrayList(fields);
	}
	
	public static String sterilize(String unclean){
		String clean = unclean;
		while(clean.contains("_")){
			clean = clean.replace("_", " ");
		}
		return clean;
	}
	
	public static String makePresentable(String s){
		s = s.toLowerCase();
		
		s = sterilize(s);
		
		s = Character.toUpperCase(s.charAt(0)) + s.substring(1);
		
		
		for(int i = 1; i < s.length(); i++){
			if(s.charAt(i) == '_' || s.charAt(i) == ' '){
				s = s.substring(0, i + 1) + Character.toUpperCase(s.charAt(i + 1)) + s.substring(i + 2);
			}
		}
		
		while(s.indexOf("Eps") >= 0 || s.indexOf("Pe ") >= 0 || s.indexOf("Peg ") >= 0 || s.indexOf("Oneyr") >= 0 || s.indexOf("Hundredday") >= 0 || s.indexOf("Fiftyday") >= 0){
			if(s.indexOf("Eps") >= 0)
				s = s.replaceFirst("Eps", "EPS");
			else if(s.indexOf("Pe ") >= 0)
				s = s.replaceFirst("Pe ", "PE ");
			else if(s.indexOf("Peg ") >= 0)
				s = s.replaceFirst("Peg ", "PEG ");
			else if(s.indexOf("Oneyr") >= 0)
				s = s.replaceFirst("Oneyr", "One Year");
			else if(s.indexOf("Hundredday") >= 0)
				s = s.replaceFirst("Hundredday", "Hundred-Day");
			else if(s.indexOf("Fiftyday") >= 0)
				s = s.replaceFirst("Fiftyday", "Fifty-Day");
			
		}
		
		
		
		return s;
	}
}
