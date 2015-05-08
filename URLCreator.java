import java.util.*;
import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;

public class URLCreator {
	
	static ArrayList<Stock> stocks = new ArrayList<Stock>();

	public static void main(String[] args) throws Exception {
		Scanner sc = new Scanner( new File( "symbols1.txt" ) );
		// companies
		int numberOfCompanies = sc.nextInt();
		String[] companies = new String[numberOfCompanies];
		for( int i = 0; i < numberOfCompanies; i++ )
		{
			companies[i] = sc.next();
		}
		// info fields
		int numberOfFields = sc.nextInt();
		String[] fields = new String[numberOfFields];
		for( int i = 0; i < numberOfFields; i++ )
		{
			fields[i] = sc.next();
		}
		
		// print it out
		String company = "";
		String field = "";
		for( int i = 0; i < companies.length; i++ )
		{
			company += companies[i] + "+";
		}
		for( int j = 0; j < fields.length; j++ )
		{
			field += fields[j];
		}
		System.out.println( "http://download.finance.yahoo.com/d/" + "quotes.csv?s=" + company.substring(0, company.length() - 1 ) +
				"&f=" + field + "=.csv" );
		
		try {
  			Scanner reader = new Scanner(new URL("http://download.finance.yahoo.com/d/"
  					+ "quotes.csv?s=" + company.substring(0, company.length() - 1 ) +
  					"&f=" + field + "=.csv").openStream());
  			
  			Scanner line = null;
  			for (int i = 0; (i < companies.length) && (reader.hasNextLine()); i++) {
  				line = new Scanner(reader.nextLine());
  				line.useDelimiter(",");

  				String next = null;
  				String symbol = null;
  				String name = null;
  				double percentChange = (0.0D / 0.0D);
  				double peRatio = (0.0D / 0.0D);
  				double peRatioCurrentYear = (0.0D / 0.0D);
  				double peRatioNextYear = (0.0D / 0.0D);
  				double eps = (0.0D / 0.0D);
  				double price = (0.0D / 0.0D);
  				for (int j = 0; (j < fields.length) && (line.hasNext()); j++) {
  					next = line.next();
  					if (fields[j] == Stock.Field.SYMBOL.value)
  						symbol = removeQuotes(next);
  					else if (fields[j] == Stock.Field.NAME.value)
  						name = removeQuotes(next);
  					else if (fields[j] == Stock.Field.PERCENT_CHANGE.value)
  						try {
  							percentChange = Double.valueOf(removeQuotes(next)).doubleValue();
  						} catch (NumberFormatException localNumberFormatException) {
  						}
  					else if (fields[j] == Stock.Field.EPS.value)
  						try {
  							eps = Double.valueOf(removeQuotes(next)).doubleValue();
  						} catch (NumberFormatException localNumberFormatException1) {
  						}
  					else if (fields[j] == Stock.Field.PE_RATIO.value)
  						try {
  							peRatio = Double.valueOf(removeQuotes(next)).doubleValue();
  						} catch (NumberFormatException localNumberFormatException2) {
  						}
  					else if (fields[j] == Stock.Field.PE_CURRENT_YEAR.value)
  						try {
  							peRatioCurrentYear = Double.valueOf(removeQuotes(next)).doubleValue();
  						} catch (NumberFormatException localNumberFormatException3) {
  						}
  					else if (fields[j] == Stock.Field.PE_NEXT_YEAR.value)
  						try {
  							peRatioNextYear = Double.valueOf(removeQuotes(next)).doubleValue();
  						}
  						catch (NumberFormatException localNumberFormatException4){
  						}
  					else if (fields[j] == Stock.Field.PRICE.value)
  						try { price = Double.valueOf(removeQuotes(next)).doubleValue();}
  						catch (NumberFormatException localNumberFormatException4){}
  				}
  				Stock stock = null;
  				for (int j = 0; j < companies.length; j++) {
  					if (( companies[j] ).equals(symbol)) {
  						stock = new Stock(symbol);
  						stock.setName(name);
  						stock.setPercentChange(percentChange);
  						stock.setEarningsPerShare(eps);
  						stock.setPeRatio(peRatio);
  						stock.setPeRatioCurrentYear(peRatioCurrentYear);
  						stock.setPeRatioNextYear(peRatioNextYear);
  						stock.setPrice(price);
  						stocks.add(stock);
  						break;
  					}
  				}
  				
  				printPortfolioConsole(fields);
  			}
  			
  		}
  		catch (MalformedURLException e) {
  			e.printStackTrace();
  		}
  		catch (IOException e) {
  			e.printStackTrace();
  		}
  		try {
  			Runtime.getRuntime().exec("clear");
  		}
  		catch (IOException e) {
  			e.printStackTrace();
  		}
		
	}
	
	public static String removeQuotes(String string) {
	    if ((string == null) || (string.length() == 0)) {
	      return string;
	    }

	    int start = string.charAt(0) == '"' ? 1 : 0;
	    int end = string.charAt(string.length() - 1) == '"' ? string.length() - 1 : string.length();
	    String result = "";
	    for (int i = start; i < end; i++) {
	      if (string.charAt(i) != '%') {
	        result = result + string.charAt(i);
	      }
	    }
	    return result;
	  }
	
	public static void printPortfolioConsole(String[] fields){
  		String description = String.format("%7s", new Object[] { "SYMBOL" });
  		for (String field : fields) {
  			if (field == Stock.Field.NAME.value)
  				description = description + String.format("   %-20s", new Object[] { Stock.Field.NAME.toString() });
  			else if (field == Stock.Field.PERCENT_CHANGE.value)
  				description = description + String.format("%10s", new Object[] { "% CHANGE" });
  			else if (field == Stock.Field.EPS.value)
  				description = description + String.format("%10s", new Object[] { Stock.Field.EPS.toString() });
  			else if (field == Stock.Field.PE_RATIO.value)
  				description = description + String.format("%10s", new Object[] { Controller.sterilize(Stock.Field.PE_RATIO.toString()) });
  			else if (field == Stock.Field.PE_CURRENT_YEAR.value)
  				description = description + String.format("%17s", new Object[] { Controller.sterilize(Stock.Field.PE_CURRENT_YEAR.toString()) });
  			else if (field == Stock.Field.PE_NEXT_YEAR.value) {
  				description = description + String.format("%15s", new Object[] { Controller.sterilize(Stock.Field.PE_NEXT_YEAR.toString()) });
  			} else if(field == Stock.Field.PRICE.value){
  				description = description + String.format("%10f", Stock.Field.PRICE.toString());
  			}
  		}
  		
  		System.out.println(description);
  		for (int i = 0; i < 100; i++) {
  			System.out.print("-");
  		}
  		System.out.println();
  		for (Stock stock : stocks) {
  			System.out.println(stock);
  		}
  		System.out.println();
  	}

}
