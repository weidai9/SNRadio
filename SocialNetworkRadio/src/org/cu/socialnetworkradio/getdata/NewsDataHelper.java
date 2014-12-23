package org.cu.socialnetworkradio.getdata;

import java.util.ArrayList;

import android.content.Context;

import com.amazonaws.auth.CognitoCachingCredentialsProvider;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBMapper;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBScanExpression;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.PaginatedScanList;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;

public class NewsDataHelper {
	private AmazonDynamoDBClient ddb = null;
    private Context context;
    
    public NewsDataHelper(Context context) {
    	this.context = context;
    	CognitoCachingCredentialsProvider cognitoProvider = new CognitoCachingCredentialsProvider(
    		    context, // get the context for the current activity
    		    "256659443981",
    		    "us-east-1:1ca265c4-22e5-42a4-a933-f9011fc2df90",
    		    "arn:aws:iam::256659443981:role/Cognito_SNSRadioUnauth_DefaultRole",
    		    "arn:aws:iam::256659443981:role/Cognito_SNSRadioAuth_DefaultRole",
    		    Regions.US_EAST_1
    	);
    	ddb = new AmazonDynamoDBClient(cognitoProvider);
    }
    
    public ArrayList<NbaNewsInfo> getNbaNewsInfoList() {
    	DynamoDBMapper mapper = new DynamoDBMapper(ddb);
    	DynamoDBScanExpression scanExpression = new DynamoDBScanExpression();
    	 PaginatedScanList<NbaNewsInfo> result = mapper.scan(
                 NbaNewsInfo.class, scanExpression);
    	 ArrayList<NbaNewsInfo> resultlist = new ArrayList<NbaNewsInfo>();
    	 for (NbaNewsInfo news : result) {
    		 resultlist.add(news);
    	 }
    	 return resultlist;
    }
    
    public ArrayList<TechNewsInfo> getTechNewsInfoList() {
    	DynamoDBMapper mapper = new DynamoDBMapper(ddb);
    	DynamoDBScanExpression scanExpression = new DynamoDBScanExpression();
    	 PaginatedScanList<TechNewsInfo> result = mapper.scan(
                 TechNewsInfo.class, scanExpression);
    	 ArrayList<TechNewsInfo> resultlist = new ArrayList<TechNewsInfo>();
    	 for (TechNewsInfo news : result) {
    		 resultlist.add(news);
    	 }
    	 return resultlist;
    }
    
    public ArrayList<UsNewsInfo> getUsNewsInfoList() {
    	DynamoDBMapper mapper = new DynamoDBMapper(ddb);
    	DynamoDBScanExpression scanExpression = new DynamoDBScanExpression();
    	 PaginatedScanList<UsNewsInfo> result = mapper.scan(
                 UsNewsInfo.class, scanExpression);
    	 ArrayList<UsNewsInfo> resultlist = new ArrayList<UsNewsInfo>();
    	 for (UsNewsInfo news : result) {
    		 resultlist.add(news);
    	 }
    	 return resultlist;
    }
}
