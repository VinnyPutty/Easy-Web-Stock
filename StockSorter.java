import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import SMG.StockDataSelectorPane.Field;

public class StockSorter implements Comparator<Stock>, ActionListener {
	private ArrayList<Stock.Field> sortPriority = new ArrayList();
	public JPanel bottomPane = new JPanel();
	public JLabel priorityLabel = new JLabel();
	public JPanel mainPanel = null;
  

  /*public StockSorter(){
	  getControlDisplay();
  }*/
  
	public StockSorter(){
		if (this.mainPanel == null) {
  			this.mainPanel = new JPanel();
  			mainPanel.setSize(600, 150);
  			mainPanel.setLocation(0, 450);
  			JPanel buttonGrid = new JPanel();
  			

			JButton[] buttons = new JButton[8];
			buttons[0] = new JButton("Price");
			buttons[0].setActionCommand(Stock.Field.PRICE.toString());
			buttons[1] = new JButton("Symbol");
			buttons[1].setActionCommand(Stock.Field.SYMBOL.toString());
			buttons[2] = new JButton("% Change");
			buttons[2].setActionCommand(Stock.Field.PERCENT_CHANGE.toString());
			buttons[3] = new JButton("Name");
			buttons[3].setActionCommand(Stock.Field.NAME.toString());
			buttons[4] = new JButton("EPS");
			buttons[4].setActionCommand(Stock.Field.EPS.toString());
			buttons[5] = new JButton("PE Ratio");
			buttons[5].setActionCommand(Stock.Field.PE_RATIO.toString());
			buttons[6] = new JButton("PE Current Year");
			buttons[6].setActionCommand(Stock.Field.PE_CURRENT_YEAR.toString());
			buttons[7] = new JButton("PE Next Year");
			buttons[7].setActionCommand(Stock.Field.PE_NEXT_YEAR.toString());

			buttonGrid.setLayout(new GridLayout((int)Math.ceil(buttons.length / 4.0D), 4));
			for (JButton button : buttons) {
				button.addActionListener(this);
				buttonGrid.add(button);
				button.setVisible(true);
			}
			
			this.bottomPane.setBackground(Color.BLACK);
			this.bottomPane.add(this.priorityLabel);
			
			this.priorityLabel.setForeground(Color.WHITE);
			this.priorityLabel.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 14));
			this.priorityLabel.setText(" Sort Priority: ");

			this.mainPanel.add(buttonGrid, "South");
//			this.mainPanel.add(this.priorityLabel, "South");
			
  		}
	}
	
  	public StockSorter(StockDataSelectorPane selectorPane){
  		if (this.mainPanel == null) {
  			this.mainPanel = new JPanel();
  			mainPanel.setSize(600, 150);
  			mainPanel.setLocation(0, 450);
  			JPanel buttonGrid = new JPanel();
  			
  			ArrayList<Field> dataOrder = selectorPane.getDataOrder();
  			
  			JButton[] buttons = new JButton[dataOrder.size()];
  			
  			for(int i = 0; i < buttons.length; i++){
  				buttons[i] = new JButton(Controller.makePresentable(dataOrder.get(i).toString()));
  				buttons[i].setActionCommand(dataOrder.get(i).toString());
  				
  			}

			buttonGrid.setLayout(new GridLayout((int)Math.ceil(buttons.length / 4.0D), 4));
			for (JButton button : buttons) {
				button.addActionListener(this);
				buttonGrid.add(button);
				button.setVisible(true);
			}
			
			this.bottomPane.setBackground(Color.BLACK);
			this.bottomPane.add(this.priorityLabel);
			
			this.priorityLabel.setForeground(Color.WHITE);
			this.priorityLabel.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 14));
			this.priorityLabel.setText(" Sort Priority: ");

			this.mainPanel.add(buttonGrid, "South");
//			this.mainPanel.add(this.priorityLabel, "South");
			
  		}
  	}
  
  
  	public StockSorter(ArrayList<Stock.Field> sortPriority) {
  		this();
  		if (sortPriority != null)
  			this.sortPriority = sortPriority;
  	}
  	
  	public int compare(Stock stock1, Stock stock2){
  		int comparison = 0;
  		for (int i = 0; (i < this.sortPriority.size()) && (comparison == 0); i++) {
  			Stock.Field currentField = this.sortPriority.get(i);
  			if (currentField == Stock.Field.SYMBOL)
  				comparison = stock1.getSymbol().compareToIgnoreCase(stock2.getSymbol());
  			else if (currentField == Stock.Field.NAME)
  				comparison = stock1.getName().compareToIgnoreCase(stock2.getName());
  			else if (currentField == Stock.Field.PERCENT_CHANGE)
  				comparison = compareDouble(stock1.getPercentChange(), stock2.getPercentChange());
  			else if (currentField == Stock.Field.EPS)
  				comparison = compareDouble(stock1.getEarningsPerShare(), stock2.getEarningsPerShare());
  			else if (currentField == Stock.Field.PE_RATIO)
  				comparison = compareDouble(stock2.getPeRatio(), stock1.getPeRatio());
  			else if (currentField == Stock.Field.PE_CURRENT_YEAR)
  				comparison = compareDouble(stock2.getPeRatioCurrentYear(), stock1.getPeRatioCurrentYear());
  			else if (currentField == Stock.Field.PE_NEXT_YEAR)
  				comparison = compareDouble(stock2.getPeRatioNextYear(), stock1.getPeRatioNextYear());
  			else if(currentField == Stock.Field.PRICE)
  				comparison = compareDouble(stock1.getPrice(), stock2.getPrice());
  			
  		}
  		return comparison;
  	}

  	public void addField(Stock.Field field) {
  		if (this.sortPriority.indexOf(field) < 0) {
  			this.sortPriority.add(field);
  			this.priorityLabel.setText(" Sort Priority: ");
  			for (Stock.Field currentField : this.sortPriority)
  				if(sortPriority.indexOf(currentField) < sortPriority.size() - 1)
  					this.priorityLabel.setText(this.priorityLabel.getText() + Controller.sterilize(currentField.toString()) + ", " + "\n");
  				else
  					this.priorityLabel.setText(this.priorityLabel.getText() + Controller.sterilize(currentField.toString()) + "\n");
  		}
  	}

  	public void removeField(){
  		if (this.sortPriority.size() > 0) {
  			this.sortPriority.remove(this.sortPriority.size() - 1);
  			
  			this.priorityLabel.setText(" Sort Priority: ");
  			for (Stock.Field currentField : this.sortPriority)
  				if(sortPriority.indexOf(currentField) < sortPriority.size() - 1)
  					this.priorityLabel.setText(this.priorityLabel.getText() + Controller.sterilize(currentField.toString()) + ", " + "\n");
  				else
  					this.priorityLabel.setText(this.priorityLabel.getText() + Controller.sterilize(currentField.toString()) + "\n");
  			
  		}
  	}

  public void removeField(Stock.Field field) {
    this.sortPriority.remove(field);
    this.priorityLabel.setText(" Sort Priority: ");
		for (Stock.Field currentField : this.sortPriority)
			if(sortPriority.indexOf(currentField) < sortPriority.size() - 1)
				this.priorityLabel.setText(this.priorityLabel.getText() + Controller.sterilize(currentField.toString()) + ", " + "\n");
			else
				this.priorityLabel.setText(this.priorityLabel.getText() + Controller.sterilize(currentField.toString()) + "\n");
  }

  	public JPanel getControlDisplay(){
  		if (this.mainPanel == null) {
  			this.mainPanel = new JPanel();
  			JPanel buttonGrid = new JPanel();

  			JButton[] buttons = new JButton[8];
  			buttons[0] = new JButton("Remove");
  			buttons[0].setActionCommand("REMOVE");
  			buttons[1] = new JButton("Symbol");
  			buttons[1].setActionCommand(Stock.Field.SYMBOL.toString());
  			buttons[2] = new JButton("% Change");
  			buttons[2].setActionCommand(Stock.Field.PERCENT_CHANGE.toString());
  			buttons[3] = new JButton("Name");
  			buttons[3].setActionCommand(Stock.Field.NAME.toString());
  			buttons[4] = new JButton("EPS");
  			buttons[4].setActionCommand(Stock.Field.EPS.toString());
  			buttons[5] = new JButton("PE Ratio");
  			buttons[5].setActionCommand(Stock.Field.PE_RATIO.toString());
  			buttons[6] = new JButton("PE Current Year");
  			buttons[6].setActionCommand(Stock.Field.PE_CURRENT_YEAR.toString());
  			buttons[7] = new JButton("PE Next Year");
  			buttons[7].setActionCommand(Stock.Field.PE_NEXT_YEAR.toString());

  			buttonGrid.setLayout(new GridLayout((int) Math.ceil(buttons.length / 4.0D), 4));
  			for (JButton button : buttons) {
  				button.addActionListener(this);
  				buttonGrid.add(button);
  			}

  			this.mainPanel.add(buttonGrid, "Center");
  			this.mainPanel.add(this.priorityLabel, "South");
  		}
  		return this.mainPanel;
  	}

  	/*public void actionPerformed(ActionEvent event){
  		String action = event.getActionCommand().toUpperCase();
  		if (action.equals("REMOVE"))
  			removeField();
  		else if(this.sortPriority.indexOf(Stock.Field.valueOf(action)) < 0){
  			addField(Stock.Field.valueOf(action));
  		} else {
  			removeField(Stock.Field.valueOf(action));
  		}
  	}*/
  	
  	public void actionPerformed(ActionEvent event){
  		String action = event.getActionCommand().toUpperCase();
  		if(this.sortPriority.indexOf(Stock.Field.valueOf(action)) < 0){
  			addField(Stock.Field.valueOf(action));
  		} else {
  			removeField(Stock.Field.valueOf(action));
  		}
  	}

  private static int compareDouble(double num1, double num2)
  {
    if (Double.compare(num1, (0.0D / 0.0D)) == 0)
      return -2147483647;
    if (Double.compare(num2, (0.0D / 0.0D)) == 0) {
      return 2147483647;
    }
    double difference = num2 - num1;
    return (int)(Math.abs(difference) > 0.0D ? difference / Math.abs(difference) : 0.0D);
  }


	
}