import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Scanner;

public class Industry {
	public static final String baseURL = "http://biz.yahoo.com/p/csv/";
	private int sectorIndex = 0;
	private String industryName = "";
	private double industryPE = (0.0D / 0.0D);
	
	public Industry(String file) throws FileNotFoundException {
		Scanner fileReader = new Scanner(new File(file));
	    if (fileReader.hasNextLine()) {
	    	Scanner line = new Scanner(fileReader.nextLine());
	    	if (line.next().equals("Sector:")) {
	    		String sector = line.next();
	    		for (int i = 0; i < Stock.sectors.length; i++) {
	    			if (Stock.sectors[i].equals(sector)) {
	    				this.sectorIndex = (i + 1);
	    			}
	    		}
	    		line = new Scanner(fileReader.nextLine());
	    		if (line.next().equals("Industry:")) {
	    			this.industryName = line.nextLine();
	    			this.industryName = this.industryName.substring(1, this.industryName.length());
	    		}
	    	}
	    }
	    updateIndustry();
	}

	public void updateIndustry(){
		try {
			URLConnection connection = new URL(generateURL()).openConnection();
			connection.setRequestProperty("Accept-Charset", "UTF-8");
			InputStream response = connection.getInputStream();
			Scanner reader = new Scanner(response);
			Scanner line = new Scanner(reader.nextLine());
			line.useDelimiter(",");
			int peIndex = 0;

			for (int i = 0; line.hasNext(); i++) {
				if (line.next().equals("\"P/E\"")) {
					peIndex = i;
				}
			}
			while (reader.hasNextLine()) {
				line = new Scanner(reader.nextLine());
				line.useDelimiter(",");
				if (Portfolio.removeQuotes(line.next()).equals(this.industryName)) {
					for (int i = 0; i < peIndex; i++)
						line.next();
					try {
						this.industryPE = Double.valueOf(line.next()).doubleValue();
					}
					catch (NumberFormatException localNumberFormatException){
						//empty
					}
				}
			}
		}
		catch (MalformedURLException e){
			e.printStackTrace();
		}
		catch (IOException e){
			e.printStackTrace();
		}
	}

	private String generateURL() {
		return "http://biz.yahoo.com/p/csv/" + this.sectorIndex + "conameu" + ".csv";
	}

	public int getSectorIndex() {
		return this.sectorIndex;
	}

	public String getIndustryName() {
		return this.industryName;
	}

	public double getIndustryPE() {
		return this.industryPE;
	}
}