
package com.wernerapps.ezbongo.api;
import org.json.JSONObject;

public abstract class NetworkCall
{
    public NetworkCall(String url)
    {
        // Handle how the data should be received
        OnNetworkRequestCompletedListener listener = new OnNetworkRequestCompletedListener()
        {
            @Override
            public void networkRequestComplete(JSONObject networkResult)
            {
                parseJSONResults(networkResult);
            }
        };

        // Start the request
        new GenericNetworkAsyncTask(listener).execute(url);        
    }
    
    public abstract void parseJSONResults(JSONObject networkCallResult);
    
    public interface OnNetworkRequestCompletedListener
    {
        public void networkRequestComplete(JSONObject networkResult);
    }
}
