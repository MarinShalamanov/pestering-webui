package is.petabytes.pestering;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.google.cloud.datastore.Datastore;
import com.google.cloud.datastore.DatastoreOptions;
import com.google.cloud.datastore.Entity;
import com.google.cloud.datastore.Query;
import com.google.cloud.datastore.QueryResults;

public class GoogleCloudConnector {
	private static final String COMPANY_SENTIMENT_TIMEPOINT_KIND = "CompanySentimentTimePoint";
	private static final String COMPANY_NAME = "CompanyName";
	private static final String DATE = "Date";
	private static final String SENTIMENT = "Sentiment";
	private static final String SENTIMENT_SUM = "SentimentSum";
	private static final String SENTIMENT_COUNT = "SentimentCount";

	// DATE FORMATTER
	final SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

	final Datastore datastore;

	public GoogleCloudConnector() {
		datastore = DatastoreOptions.getDefaultInstance().getService();
	}

	
	public List<CompanySentimentDTO> getAll() {
		Query<Entity> query = Query.newEntityQueryBuilder()
			    .setKind(COMPANY_SENTIMENT_TIMEPOINT_KIND)
			    .build();
		QueryResults<Entity> res = datastore.run(query);
		
		List<CompanySentimentDTO> all = new ArrayList<>();
		while (res.hasNext()) {
			  Entity entity = res.next();
			  
			  String company = entity.getString(COMPANY_NAME);
			  Date date = entity.getDateTime(DATE).toDate();
			  double sentiment = entity.getDouble(SENTIMENT);
			  
			  System.out.println(company + date + sentiment);
			  
			  CompanySentimentDTO companySentimentDTO = new CompanySentimentDTO(company, date, sentiment);
			  all.add(companySentimentDTO);
		}
		
		return all;
	}

}