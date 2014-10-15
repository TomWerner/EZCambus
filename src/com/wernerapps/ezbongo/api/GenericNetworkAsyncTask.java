package com.wernerapps.ezbongo.api;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.nio.charset.Charset;

import org.json.JSONException;
import org.json.JSONObject;

import android.os.AsyncTask;
import android.util.Log;

import com.wernerapps.ezbongo.api.NetworkCall.OnNetworkRequestCompletedListener;

public class GenericNetworkAsyncTask extends AsyncTask<String, String, JSONObject>
{
    private OnNetworkRequestCompletedListener onRequestCompletedListener;

    public GenericNetworkAsyncTask(OnNetworkRequestCompletedListener onRequestCompletedListener)
    {
        this.onRequestCompletedListener = onRequestCompletedListener;
    }
    
    @Override
    protected JSONObject doInBackground(String... params)
    {
        String url = params[0];
        JSONObject result = readJsonFromUrl(url);
        onRequestCompletedListener.networkRequestComplete(result);
        return result;
    }
    
    /**
     * This method creates a JSON object from data returned by an HTTP GET
     * request to the given url.
     * 
     * @param url
     *            The url to request data from
     * @return JSONObject of the data returned by the url
     * @throws IOException
     * @throws JSONException
     */
    private JSONObject readJsonFromUrl(String url)
    {
        try
        {
            InputStream is = new URL(url).openStream();
            BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
            String jsonText = readAll(rd);
            JSONObject json = new JSONObject(jsonText);
            is.close();
            return json;
        }
        catch (JSONException e)
        {
            e.printStackTrace();
            Log.e(APIManager.class.getName(), "JSON Error", e);
        }
        catch (IOException e)
        {
            Log.e(APIManager.class.getName(), "IOException Error", e);
        }
        finally
        {
        }
        return null;
    }

    private String readAll(Reader rd) throws IOException
    {
        StringBuilder sb = new StringBuilder();
        int cp;
        while ((cp = rd.read()) != -1)
        {
            sb.append((char) cp);
        }
        return sb.toString();
    }

}
