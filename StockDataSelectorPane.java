import java.awt.Color;
import java.awt.Container;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.Writer;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Scanner;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class StockDataSelectorPane extends JFrame implements ActionListener {
	
	public static enum Field {
		AFTER_HOURS_CHANGE_REALTIME("c8"), ANNUALIZED_GAIN("g3"), ASK("a0"), ASK_REALTIME("b2"), ASK_SIZE("a5"), AVERAGE_DAILY_VOLUME("a2"), BID("b0"), 
			BID_REALTIME("b3"), BID_SIZE("b6"), BOOK_VALUE_PER_SHARE("b4"), CHANGE("c1"), CHANGE_CHANGE_IN_PERCENT("c0"), CHANGE_FROM_FIFTYDAY_MOVING_AVERAGE("m7"), 
			CHANGE_FROM_TWO_HUNDREDDAY_MOVING_AVERAGE("m5"), CHANGE_FROM_YEAR_HIGH("k4"), CHANGE_FROM_YEAR_LOW("j5"), CHANGE_IN_PERCENT("p2"), CHANGE_IN_PERCENT_REALTIME("k2"), 
			CHANGE_REALTIME("c6"), COMMISSION("c3"), CURRENCY("c4"), DAYS_HIGH("h0"), DAYS_LOW("g0"), DAYS_RANGE("m0"), DAYS_RANGE_REALTIME("m2"), DAYS_VALUE_CHANGE("w1"), 
			DAYS_VALUE_CHANGE_REALTIME("w4"), DIVIDEND_PAY_DATE("r1"), TRAILING_ANNUAL_DIVIDEND_YIELD("d0"), TRAILING_ANNUAL_DIVIDEND_YIELD_IN_PERCENT("y0"), DILUTED_EPS("e0"), 
			EBITDA("j4"), EPS_ESTIMATE_CURRENT_YEAR("e7"), EPS_ESTIMATE_NEXT_QUARTER("e9"), EPS_ESTIMATE_NEXT_YEAR("e8"), EX_DIVIDEND_DATE("q0"), FIFTYDAY_MOVING_AVERAGE("m3"), 
			SHARES_FLOAT("f6"), HIGH_LIMIT("l2"), HOLDINGS_GAIN("g4"), HOLDINGS_GAIN_PERCENT("g1"), HOLDINGS_GAIN_PERCENT_REALTIME("g5"), HOLDINGS_GAIN_REALTIME("g6"), 
			HOLDINGS_VALUE("v1"), HOLDINGS_VALUE_REALTIME("v7"), LAST_TRADE_DATE("d1"), LAST_TRADE_PRICE_ONLY("l1"), LAST_TRADE_REALTIME_WITH_TIME("k1"), LAST_TRADE_SIZE("k3"), 
			LAST_TRADE_TIME("t1"), LAST_TRADE_WITH_TIME("l0"), LOW_LIMIT("l3"), MARKET_CAPITALIZATION("j1"), MARKET_CAP_REALTIME("j3"), MORE_INFO("i0"), NAME("n0"), NOTES("n4"), 
			ONEYR_TARGET_PRICE("t8"), OPEN("o0"), ORDER_BOOK_REALTIME("i5"), PEG_RATIO("r5"), PE_RATIO("r0"), PE_RATIO_REALTIME("r2"), 
			PERCENT_CHANGE_FROM_FIFTYDAY_MOVING_AVERAGE("m8"), PERCENT_CHANGE_FROM_TWO_HUNDREDDAY_MOVING_AVERAGE("m6"), CHANGE_IN_PERCENT_FROM_YEAR_HIGH("k5"), 
			PERCENT_CHANGE_FROM_YEAR_LOW("j6"), PREVIOUS_CLOSE("p0"), PRICE_BOOK("p6"), PRICE_EPS_ESTIMATE_CURRENT_YEAR("r6"), PRICE_EPS_ESTIMATE_NEXT_YEAR("r7"), 
			PRICE_PAID("p1"), PRICE_SALES("p5"), REVENUE("s6"), SHARES_OWNED("s1"), SHARES_OUTSTANDING("j2"), SHORT_RATIO("s7"), STOCK_EXCHANGE("x0"), SYMBOL("s0"), 
			TICKER_TREND("t7"), TRADE_DATE("d2"), TRADE_LINKS("t6"), TRADE_LINKS_ADDITIONAL("f0"), TWO_HUNDREDDAY_MOVING_AVERAGE("m4"), VOLUME("v0"), YEAR_HIGH("k0"), 
			YEAR_LOW("j0"), YEAR_RANGE("w0");
	    
		String value;

	    private Field(String value){ 
	    	this.value = value; 
	    }
	    
	    

	}
	
	private JPanel mainPanel = null;
	private JPanel buttonGrid = null;
	private JLabel dataPriority = null;
	private PrintWriter dataOrderFile = null;
	
	private ArrayList<Field> dataOrder = new ArrayList();
	
	/*public static void main(String[] args){
		JFrame frame = new JFrame("Data Selector");
		frame.setVisible(true);
		frame.setSize(600, 600);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		StockDataSelectorPane selectorPane = new StockDataSelectorPane(frame);
		
		
		  		
	}*/ 
	
	public StockDataSelectorPane() throws FileNotFoundException{
		
		Container contentPane = this.getContentPane();
		
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		this.dataOrderFile = new PrintWriter("dataOrder.txt");
		
		this.dataOrderFile.close();
		
		
		if (this.mainPanel == null) {
  			this.mainPanel = new JPanel();
  			this.mainPanel.setSize(600, 600);
  			this.buttonGrid = new JPanel();
  			this.dataPriority = new JLabel();
		}  			

			JButton[] buttons = new JButton[Field.values().length];
			
			for(int i = 0; i < Field.values().length; i++){
				buttons[i] = new JButton(Controller.makePresentable(Field.values()[i].toString()));
				buttons[i].setActionCommand(Field.values()[i].toString());
			}
			
			

			buttonGrid.setLayout(new GridLayout(22, 4));
			for (JButton button : buttons) {
				button.setFont(new Font("Serif", Font.PLAIN, 10));
				button.addActionListener(this);
				
				buttonGrid.add(button);
				button.setSize(20, 10);
				button.setVisible(true);
			}
			
			dataPriority.setText(" Data Order: ");
				
			mainPanel.add(buttonGrid, "North");
			mainPanel.add(dataPriority);
			contentPane.add(mainPanel);
			
			
			this.pack();
			this.setVisible(true);
	}
	
	
	
	public void printQuoteProperties(){
		try {
  			Scanner reader = new Scanner(new File("QuoteProperties.csv"));
  			Scanner line = null;
  			String name = null;
  			String description = null;
  			String value = null;
  			for (int i = 0; reader.hasNextLine(); i++) {
  				line = new Scanner(reader.nextLine());
  				line.useDelimiter(",");

  				name = null;
  				description = null;
  				value = null;
  				
  				name = line.next();
  				
  				for(int j = 1; j < name.length(); j++){
  					if(isUppercase(name.charAt(j))){
  						name = name.substring(0, j) + "_" + name.substring(j);
  						j++;
  					}
  				}
  				
  				description = line.next();
  				value = line.next();
  				
  				System.out.print(name.toUpperCase() + "(\"" + value + "\"), ");
  			}
  			
  		}
  		catch (IOException e) {
  			e.printStackTrace();
  		}
	}
	
	public boolean isUppercase(char curr){
		boolean check = false;
		
		char[] uppercase = new char[] {'A','B','C','D','E','F','G','H','I','J','K','L','M','N','O','P','Q','R','S','T','U','V','W','X','Y','Z'};
		
		for(int i = 0; i < uppercase.length; i++){
			if(curr == (char) uppercase[i]){
				check = true;
				break;
				
			}
		}
		
		return check;
	}
	
	
	
	public void addField(Field field) {
		if (this.dataOrder.indexOf(field) < 0) {
			this.dataOrder.add(field);
			this.dataPriority.setText(" Data Order: ");
			for (Field currentField : this.dataOrder)
				this.dataPriority.setText(this.dataPriority.getText() + Controller.makePresentable(currentField.toString()) + "\n");
			this.pack();
		}
		
		// edit this so it does the above for the file or write dataOrder to file after clearing the file
		if (this.dataOrder.size() > 0) {
			try {
				this.dataOrderFile = new PrintWriter("dataOrder.txt");
				for(int i = 0; i < this.dataOrder.size(); i++)
					this.dataOrderFile.print(Controller.makePresentable(this.dataOrder.get(i).toString()));
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	
	
	}

  	public void removeField(){
  		if (this.dataOrder.size() > 0) {
  			this.dataOrder.remove(this.dataOrder.size() - 1);
  			
  			this.dataPriority.setText(" Data Order: ");
  			for (Field currentField : this.dataOrder)
  				this.dataPriority.setText(this.dataPriority.getText() + Controller.makePresentable(currentField.toString()) + "\n");
  			this.pack();
  		}
  		
  		if (this.dataOrder.size() > 0) {
			try {
				this.dataOrderFile = new PrintWriter("dataOrder.txt");
				for(int i = 0; i < this.dataOrder.size(); i++)
					this.dataOrderFile.print(Controller.makePresentable(this.dataOrder.get(i).toString()));
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
  	}

  	public void removeField(Field field) {
  		this.dataOrder.remove(field);
  		this.dataPriority.setText(" Data Order: ");
  		for (Field currentField : this.dataOrder)
  			this.dataPriority.setText(this.dataPriority.getText() + Controller.makePresentable(currentField.toString()) + "\n");
  		this.pack();
  		
  		if (this.dataOrder.size() > 0) {
			try {
				this.dataOrderFile = new PrintWriter("dataOrder.txt");
				for(int i = 0; i < this.dataOrder.size(); i++)
					this.dataOrderFile.print(Controller.makePresentable(this.dataOrder.get(i).toString()));
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
  	}
  	
  	public ArrayList<Field> getDataOrder(){
  		return this.dataOrder;
  	}

	@Override
	public void actionPerformed(ActionEvent event) {
		String action = event.getActionCommand();
  		if(this.dataOrder.indexOf(Field.valueOf(action)) < 0){
  			addField(Field.valueOf(action));
  		} else {
  			removeField(Field.valueOf(action));
  		}
		
	}

}
