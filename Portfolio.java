import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.Timer;

public class Portfolio implements ActionListener {
	private ArrayList<Stock> portfolio = new ArrayList();
	private List<Stock.Field> fields = Controller.getFields();
	
	private StockSorter sorter = null;
	private Timer updateTimer = new Timer(Controller.updateInterval, this);
	private JFrame application = new JFrame("Stock Monitor");
	public StockPane stockPane = new StockPane(this.portfolio, this.fields);

	private Industry industry = null;

	public Portfolio(){
		JButton pauseButton = new JButton("Start");
		pauseButton.setActionCommand("RESUME");
		pauseButton.addActionListener(this);
		
		this.sorter = new StockSorter();
		
		this.sorter.mainPanel.add(pauseButton, "North");
		
//		this.application.getContentPane().add(pauseButton, "North");
		
		
		this.application.getContentPane().add(this.stockPane, "Center");
	
		
		this.application.getContentPane().add(this.sorter.mainPanel, "North");
		this.application.getContentPane().add(this.sorter.bottomPane, "South");
		
		
		this.application.getContentPane().setBackground(Color.BLACK);
		this.application.setSize(800, 600);
		this.application.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.application.setVisible(true);
		
	}

	public Portfolio(String file){
		this();
		try {
			this.industry = new Industry(file);
			
			Scanner fileReader = new Scanner(new File(file));
			if (fileReader.hasNextLine()) {
				String line = fileReader.next();
				if (line.equals("Sector:"))
					fileReader.nextLine();
				else {
					this.portfolio.add(new Stock(line));
				}
			}
			if (fileReader.hasNextLine()) {
				String line = fileReader.next();
				if (line.equals("Industry:"))
					fileReader.nextLine();
				else {
					this.portfolio.add(new Stock(line));
				}
			}
			while (fileReader.hasNextLine())
				this.portfolio.add(new Stock(fileReader.nextLine()));
      
		}
		catch (FileNotFoundException e) {
			System.err.println("Could not open symbols.txt!");
			e.printStackTrace();
		}
		
	}

	public Portfolio(String file, StockDataSelectorPane selectorPane){
		JButton pauseButton = new JButton("Start");
		pauseButton.setActionCommand("RESUME");
		pauseButton.addActionListener(this);
		
		this.sorter = new StockSorter(selectorPane);
		
		this.sorter.mainPanel.add(pauseButton, "North");
		
//		this.application.getContentPane().add(pauseButton, "North");
		this.application.getContentPane().add(this.stockPane, "Center");
		this.application.getContentPane().add(this.sorter.mainPanel, "North");
		this.application.getContentPane().add(this.sorter.bottomPane, "South");
		
		this.application.setSize(800, 600);
		this.application.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.application.setVisible(true);
		
		try {
			this.industry = new Industry(file);
			
			Scanner fileReader = new Scanner(new File(file));
			if (fileReader.hasNextLine()) {
				String line = fileReader.next();
				if (line.equals("Sector:"))
					fileReader.nextLine();
				else {
					this.portfolio.add(new Stock(line));
				}
			}
			if (fileReader.hasNextLine()) {
				String line = fileReader.next();
				if (line.equals("Industry:"))
					fileReader.nextLine();
				else {
					this.portfolio.add(new Stock(line));
				}
			}
			while (fileReader.hasNextLine())
				this.portfolio.add(new Stock(fileReader.nextLine()));
      
		}
		catch (FileNotFoundException e) {
			System.err.println("Could not open symbols.txt!");
			e.printStackTrace();
		}
		
	}

  public void addStock(String symbol) {
    for (Stock stock : this.portfolio) {
      if (stock.getSymbol().equals(symbol)) {
        return;
      }
    }
    this.portfolio.add(new Stock(symbol));
  }

  public void removeStock(String symbol) {
    for (int i = 0; i < this.portfolio.size(); i++)
      if (((Stock)this.portfolio.get(i)).getSymbol().equals(symbol)) {
        this.portfolio.remove(i);
        i--;
      }
  }

  	public void updatePortfolio(){
  		URLConnection connection = null;
  		try {
  			this.industry.updateIndustry();
  			
  			connection = new URL(generateURL()).openConnection();
  			connection.setRequestProperty("Accept-Charset", "UTF-8");
  			InputStream response = connection.getInputStream();

  			Scanner reader = new Scanner(new URL(generateURL()).openStream());
  			Scanner line = null;
  			for (int i = 0; (i < this.portfolio.size()) && (reader.hasNextLine()); i++) {
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
  				for (int j = 0; (j < this.fields.size()) && (line.hasNext()); j++) {
  					next = line.next();
  					if (this.fields.get(j) == Stock.Field.SYMBOL)
  						symbol = removeQuotes(next);
  					else if (this.fields.get(j) == Stock.Field.NAME)
  						name = removeQuotes(next);
  					else if (this.fields.get(j) == Stock.Field.PERCENT_CHANGE)
  						try { percentChange = Double.parseDouble(removeQuotes(next)); } 
  						catch (NumberFormatException localNumberFormatException) {}
  					else if (this.fields.get(j) == Stock.Field.EPS)
  						try {
  							eps = Double.parseDouble(removeQuotes(next));
  						} catch (NumberFormatException localNumberFormatException1) {
  						}
  					else if (this.fields.get(j) == Stock.Field.PE_RATIO)
  						try {
  							peRatio = Double.parseDouble(removeQuotes(next));
  						} catch (NumberFormatException localNumberFormatException2) {
  						}
  					else if (this.fields.get(j) == Stock.Field.PE_CURRENT_YEAR)
  						try {
  							peRatioCurrentYear = Double.parseDouble(removeQuotes(next));
  						} catch (NumberFormatException localNumberFormatException3) {
  						}
  					else if (this.fields.get(j) == Stock.Field.PE_NEXT_YEAR)
  						try { peRatioNextYear = Double.parseDouble(removeQuotes(next)); }
  						catch (NumberFormatException localNumberFormatException4){}
  					else if (this.fields.get(j) == Stock.Field.PRICE)
  						try { price = Double.parseDouble(removeQuotes(next)); }
  						catch (NumberFormatException localNumberFormatException4){}
  				}
  				Stock stock = null;
  				for (int j = 0; j < this.portfolio.size(); j++) {
  					if (((Stock)this.portfolio.get(j)).getSymbol().equals(symbol)) {
  						stock = (Stock)this.portfolio.get(j);
  						stock.setName(name);
  						stock.setPercentChange(percentChange);
  						stock.setEarningsPerShare(eps);
  						stock.setPeRatio(peRatio);
  						stock.setPeRatioCurrentYear(peRatioCurrentYear);
  						stock.setPeRatioNextYear(peRatioNextYear);
  						stock.setPrice(price);
  						break;
  					}
  				}
  			}
  			Collections.sort(this.portfolio, this.sorter);
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

  	public void printPortfolioConsole(){
  		String description = String.format("%7s", new Object[] { "SYMBOL" });
  		for (Stock.Field field : this.fields) {
  			if (field == Stock.Field.NAME)
  				description = description + String.format("   %-20s", new Object[] { Stock.Field.NAME.toString() });
  			else if (field == Stock.Field.PERCENT_CHANGE)
  				description = description + String.format("%10s", new Object[] { "% CHANGE" });
  			else if (field == Stock.Field.EPS)
  				description = description + String.format("%10s", new Object[] { Stock.Field.EPS.toString() });
  			else if (field == Stock.Field.PE_RATIO)
  				description = description + String.format("%10s", new Object[] { Controller.sterilize(Stock.Field.PE_RATIO.toString()) });
  			else if (field == Stock.Field.PE_CURRENT_YEAR)
  				description = description + String.format("%17s", new Object[] { Controller.sterilize(Stock.Field.PE_CURRENT_YEAR.toString()) });
  			else if (field == Stock.Field.PE_NEXT_YEAR) {
  				description = description + String.format("%15s", new Object[] { Controller.sterilize(Stock.Field.PE_NEXT_YEAR.toString()) });
  			} else if(field == Stock.Field.PRICE){
  				description = description + String.format("%10f", Stock.Field.PRICE.toString());
  			}
  		}
  		
  		System.out.println(description);
  		for (int i = 0; i < 100; i++) {
  			System.out.print("-");
  		}
  		System.out.println();
  		for (Stock stock : this.portfolio) {
  			System.out.println(stock);
  		}
  		System.out.println();
  	}
  	
  	public void printPortfolioWindow(){
  		this.stockPane.printStockData();
  	}

  	public void actionPerformed(ActionEvent event) {
  		if (event.getSource() == this.updateTimer) {
  			updatePortfolio();
//  			printPortfolioConsole();
  			printPortfolioWindow();
  		} else if (event.getActionCommand().equals("PAUSE")) {
  			this.updateTimer.stop();
  			((JButton)event.getSource()).setText("Resume");
  			((JButton)event.getSource()).setActionCommand("RESUME");
  		} else if (event.getActionCommand().equals("RESUME")) {
  			this.updateTimer.start();
  			((JButton)event.getSource()).setText("Pause");
  			((JButton)event.getSource()).setActionCommand("PAUSE");
  		}
  	}

  	public String generateURL() {
  		return "http://download.finance.yahoo.com/d/quotes.csv?" + generateTickerString() + "&" + generateFormatString() + "&" + "e=.csv";
  	}
  	
  	public String generateFormatString() {
  		String format = null;
  		if (this.fields.size() > 0) {
  			format = "f=";
  			for (Stock.Field field : this.fields) {
  				format = format + field.value;
  			}
  		}
  		return format;
  	}

  public String generateTickerString() {
    String tickerString = null;
    if (this.portfolio.size() > 0) {
      tickerString = "s=" + ((Stock)this.portfolio.get(0)).getSymbol();
      for (int i = 1; i < this.portfolio.size(); i++) {
        tickerString = tickerString + "+" + ((Stock)this.portfolio.get(i)).getSymbol();
      }
    }
    return tickerString;
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
}
