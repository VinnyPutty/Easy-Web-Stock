import java.awt.Color;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Graphics;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;

public class StockPane extends JPanel {

	private ArrayList<Stock> portfolio = null;
	private List<Stock.Field> fields = null; 
	
	public StockPane(ArrayList<Stock> portfolio, List<Stock.Field> fields){
		super();
		this.portfolio = portfolio;
		this.fields = fields;
		this.setSize(600, 200);
		this.setOpaque(true);
		this.setBackground(Color.BLACK);
	}
	
	public void printStockData(){
		this.repaint();
	}
	
	@Override
	public void paint(Graphics g){
		

		
//		this.setBackground(Color.BLACK);
		
		String description = String.format("%7s", new Object[] { "SYMBOL" });
		for (Stock.Field field : fields) {
			if (field == Stock.Field.NAME)
				description = description + String.format("   %-20s", new Object[] { Stock.Field.NAME.toString() });
			else if (field == Stock.Field.PERCENT_CHANGE)
				description = description + String.format("%10s", new Object[] { "%CHANGE" });
			else if (field == Stock.Field.EPS)
				description = description + String.format("%10s", new Object[] { Stock.Field.EPS.toString() });
			else if (field == Stock.Field.PE_RATIO)
				description = description + String.format("%10s", new Object[] { Controller.sterilize(Stock.Field.PE_RATIO.toString()) });
			else if (field == Stock.Field.PE_CURRENT_YEAR)
				description = description + String.format("%17s", new Object[] { Controller.sterilize(Stock.Field.PE_CURRENT_YEAR.toString()) });
			else if (field == Stock.Field.PE_NEXT_YEAR) 
				description = description + String.format("%15s", new Object[] { Controller.sterilize(Stock.Field.PE_NEXT_YEAR.toString()) });
			else if(field == Stock.Field.PRICE)
  				description = description + String.format("%10s", Stock.Field.PRICE.toString());
  			
		}
		
		g.setColor(Color.WHITE);
		g.setFont(new Font("Monospaced", Font.PLAIN, 12));
		
		/*Font dataFont = null;
		
		try {
			dataFont = Font.createFont(Font.TRUETYPE_FONT, new File("Andale Mono.ttf"));
		} catch (FontFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		g.setFont(dataFont.deriveFont((float) 12));*/
		
		int itPos = 20;
		int positionOnViewPane = itPos;
		
		
		g.drawString(description, 0, positionOnViewPane);
		positionOnViewPane += itPos;
		
//		System.out.println(description);
		
		String dashedLine = "";
		
		for (int i = 0; i < 300; i++) {
			dashedLine += "-";
		}
		
		g.setFont(new Font("Monospaced", Font.PLAIN, 12));
		g.drawString(dashedLine, 0, positionOnViewPane);
		positionOnViewPane += itPos;
		
//		for (int i = 0; i < 100; i++) {
//			System.out.print("-");
//		}
		
		
//		System.out.println();
		
//		g.setFont(dataFont.deriveFont((float) 12));
		
		for (Stock stock : portfolio) {
			g.drawString(stock.toString(), 0, positionOnViewPane);
			positionOnViewPane += itPos;
		}
		
//		for (Stock stock : this.portfolio) {
//			System.out.println(stock);
//		}
	
  	}


}
