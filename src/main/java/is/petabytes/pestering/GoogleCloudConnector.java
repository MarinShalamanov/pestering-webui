package is.petabytes.pestering;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.cloud.datastore.Datastore;
import com.google.cloud.datastore.DatastoreOptions;
import com.google.cloud.datastore.Entity;
import com.google.cloud.datastore.Query;
import com.google.cloud.datastore.QueryResults;

public class GoogleCloudConnector {
	private static final String COMPANY_SENTIMENT_TIMEPOINT_KIND = "CompanySentimentTimePointDemo";
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

	static List<CompanySentimentDTO> cached = null;
	
	
	public List<CompanySentimentDTO> getAll() {	
		if (cached != null) {
			return cached;
		}
		
		Query<Entity> query = Query.newEntityQueryBuilder()
			    .setKind(COMPANY_SENTIMENT_TIMEPOINT_KIND)
			    .build();
		QueryResults<Entity> res = datastore.run(query);
		
		
		Map<String, Double> perCompanySum = new HashMap<String, Double>();
		Map<String, Integer> perCompanyCount = new HashMap<String, Integer>();
		
		while (res.hasNext()) {
			  Entity entity = res.next();
			  
			  String company = entity.getString(COMPANY_NAME);
			  Date date = entity.getDateTime(DATE).toDate();
			  double sentiment = entity.getDouble(SENTIMENT);
			  
			  if(!perCompanySum.containsKey(company)) {
				  perCompanySum.put(company, 0.0);
				  perCompanyCount.put(company, 0);
			  }
			  
			  perCompanySum.put(company, sentiment + perCompanySum.get(company));
			  perCompanyCount.put(company, 1 + perCompanyCount.get(company));
			  
			  
			  System.out.println(company + date + sentiment);
		}
		
		List<CompanySentimentDTO> all = new ArrayList<>();
		
		for(String company : perCompanyCount.keySet()) {
			double sentiment = perCompanySum.get(company) / perCompanyCount.get(company);
			CompanySentimentDTO companySentimentDTO = new CompanySentimentDTO(company, new Date(), sentiment); 
			all.add(companySentimentDTO);
		}
		cached = all;
		
		return all;
	}

}