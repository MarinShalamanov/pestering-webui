package is.petabytes.pestering;

import java.util.Date;

public class CompanySentimentDTO {

	private String company;
	private Date date;
	private double sentiment;
	

	public CompanySentimentDTO(String company, Date date2, double sentiment) {
		super();
		this.company = company;
		this.date = date2;
		this.sentiment = sentiment;
	}

	public String getCompany() {
		return company;
	}

	public void setCompany(String company) {
		this.company = company;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public double getSentiment() {
		return sentiment;
	}

	public void setSentiment(double sentiment) {
		this.sentiment = sentiment;
	}

}
