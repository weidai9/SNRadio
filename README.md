SNRadio
=======
Cloud Computing & Big Data Final Project
Wei Dai (wd2248), Youhan Wang (yw2663), Yijun Wang (YW2676)
An Android app to collect and classify the user’s news feed from Facebook

Tools Used
==========
Jsoup, Facebook Android SDK, AWS Android SDK

Front End App
=============
The project is in "SocialNetworkRadio" directory.

We developed the app based on Android 21 API.  We used Facebook Graph API to enable users to login with their Facebook account, and ask for read_stream authentication, then use the following request to read the news feed:
new Request(session, "/me/home",params, HttpMethod.GET,
    new Request.Callback() {
        public void onCompleted(Response response) {
            /* handle the result */
        } } ).executeAsync();

Then we put the news feed data into a ListView based on BaseAdapter.

The news data is acquired from the Dynamo database using AWS Android SDK. We use Cognito to get access to the database, then use scan mapper to get all the news data from the database.

We implemented the radio feature by leveraging Android TextToSpeech engine, which can receive String data and speak it out.

Data Collecting Daemon
======================
The project is in "ServerNews" directory.

The data collecting daemon is running on EBS and will check news update every three minutes.

Currently, we divide the news into three categories, sports news, politics news and tech news (We could easily expand the categories, adding more classes). These three parts are similar, in the report, I will only explain the sport category since it is the clearest one.

We use Jsoup library and regular expression to do the news fetching work.  One of the sports website that we fetch is http://www.nba.com. We fetch its headline and put it into our dynamo database.

To fetch the website data, we have to analyze the structure of the website code (This is where most work is done). For the NBA website. The headline structure is somehow like:
	<div id = ”nbaNewsTabArea”>
		< class = “nbaEachNewsTbl”>
			<a href = “xxx”>  // here is the news link
			<class = “nbaNewsInfo”>
        </>
	</div>
It is actually much harder because we have to get rid of a lot of redundant or useless information.
Using Jsoup, we get the title, news info, news link “xxx”, and use the link to get more information( The text of the news). To get the text with out the tag, we have to use regular expression to remove all the tags:
text = text.replaceAll("</p>", "\n");
		text = text.replaceAll("<[^<>]*>", "");
This way, we can get the text of the news.
The other websites information are also fetched this way. In the future, we might put all the regular expressions into one xml which is easier to manage. And also, we plan to add a listener on each website which can notify us when the structure of the websites change.

We also use the class DBhelper to put all these information into dynamoDB.(This class is used in TwitterMap assignment)
