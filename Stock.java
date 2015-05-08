import java.util.List;

public class Stock {
	public static final String[] sectors = { "Basic_Materials", "Congolmerates", "Consumer Goods", "Financial", "Healthcare", "Industrial_Goods", "Services", "Technology", "Utilities" };

	private String name = "";
	private String symbol = "";
	private double percentChange = (0.0D / 0.0D);
	private double earningsPerShare = (0.0D / 0.0D);
	private double peRatio = (0.0D / 0.0D);
	private double peRatioCurrentYear = (0.0D / 0.0D);
	private double peRatioNextYear = (0.0D / 0.0D);
	private double price = (0.0D / 0.0D);
  
	public Stock(String symbol){
		this.symbol = symbol;
	}
	public Stock(String symbol, String name){
		this(symbol);
		this.name = name;
	}
	
	public String toString(){
		List<Field> fields = Controller.getFields();
		String description = String.format("%7s", new Object[] { this.symbol });
		for (Stock.Field field : fields) {
			if (field == Stock.Field.NAME)
				description = description + String.format("   %-20s", new Object[] { this.name });
			else if (field == Stock.Field.PERCENT_CHANGE)
				description = description + String.format("%10.3f", new Object[] { Double.valueOf(this.percentChange) });
			else if (field == Stock.Field.EPS)
				description = description + String.format("%10.3f", new Object[] { Double.valueOf(this.earningsPerShare) });
			else if (field == Stock.Field.PE_RATIO)
				description = description + String.format("%10.3f", new Object[] { Double.valueOf(this.peRatio) });
			else if (field == Stock.Field.PE_CURRENT_YEAR)
				description = description + String.format("%17.3f", new Object[] { Double.valueOf(this.peRatioCurrentYear) });
			else if (field == Stock.Field.PE_NEXT_YEAR) 
				description = description + String.format("%15.3f", new Object[] { Double.valueOf(this.peRatioNextYear) });
			else if(field == Stock.Field.PRICE)
				description = description + String.format("%10.3f", new Object[] { Double.valueOf(this.price) });
		}
		return description;
	}
	
	/*public String toString(){
		return String.format("%7s", this.symbol) + String.format("%10.3f", Double.valueOf(this.percentChange)) + 
		String.format("%10.2f", Double.valueOf(this.peRatio)) + String.format("%17.2f", new Object[] { Double.valueOf(this.peRatioCurrentYear) }) + 
		String.format("%15.2f", new Object[] { Double.valueOf(this.peRatioNextYear) }) + String.format("%7.2f", this.earningsPerShare) + 
		String.format("   %-20s", this.name);
	}*/

	public String getSymbol(){
		return this.symbol;
	}
	public double getPercentChange(){
		return this.percentChange;
	}
	public void setPercentChange(double percentChange){
		if (Double.compare(percentChange, (0.0D / 0.0D)) != 0)
			this.percentChange = percentChange;
	}
	
	public String getName(){
		return this.name;
	}
	public void setName(String name){
		if (name != null)
			this.name = name;
	}

	public double getEarningsPerShare(){
		return this.earningsPerShare;
	}
	public void setEarningsPerShare(double earningsPerShare){
		if (Double.compare(earningsPerShare, (0.0D / 0.0D)) != 0)
    	this.earningsPerShare = earningsPerShare;
	}

	public double getPeRatio(){
		return this.peRatio;
	}
	public void setPeRatio(double peRatio){
		if (Double.compare(peRatio, (0.0D / 0.0D)) != 0)
			this.peRatio = peRatio;
	}

	public double getPeRatioCurrentYear(){
		return this.peRatioCurrentYear;
	}
	public void setPeRatioCurrentYear(double peRatioCurrentYear){
		if (Double.compare(peRatioCurrentYear, (0.0D / 0.0D)) != 0)
			this.peRatioCurrentYear = peRatioCurrentYear;
	}

	public double getPeRatioNextYear(){
		return this.peRatioNextYear;
	}
	public void setPeRatioNextYear(double peRatioNextYear){
		if (Double.compare(peRatioNextYear, (0.0D / 0.0D)) != 0)
			this.peRatioNextYear = peRatioNextYear;
	}
	
	public double getPrice(){
		return this.price;
	}
	public void setPrice(double price){
		if (Double.compare(price, (0.0D / 0.0D)) != 0)
			this.price = price;
	}

	public static enum Field {
    NAME("n"), SYMBOL("s"), PERCENT_CHANGE("p2"), EPS("e"), PE_RATIO("r"), PE_CURRENT_YEAR("r6"), PE_NEXT_YEAR("r7"), PRICE("l1");

    String value;

    private Field(String value) { this.value = value; }

	}
}