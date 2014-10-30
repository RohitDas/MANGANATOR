package manga.anime.com.manganator;


import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView;
import android.net.Uri;
import android.widget.ArrayAdapter;
import android.widget.MediaController;
import android.widget.VideoView;
import android.widget.ListView;
import android.view.View;
import android.widget.Toast;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HttpContext;
import org.json.JSONArray;
import org.json.JSONObject;
import android.util.Log;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by ROHIT on 10/19/2014.
 */
public class Manga_List_Activity extends Activity {
    int lock = 0;
    String result = "";
    InputStream in = null;
    JSONObject jsonObject = null;
    String[] anime_list = new String[20000];

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_intent);
        final ListView _manga_list = (ListView) findViewById(R.id.listview);
        Bundle bundle = getIntent().getExtras();
        String manga_string = bundle.getString("Json_Manga");
        if(manga_string == null)
        {
            Log.e("null","null");
        }
        Log.e("Manga String",manga_string);
        try
        {
            jsonObject = new JSONObject(manga_string);
            JSONArray manga_array = jsonObject.getJSONArray("manga");
            int end = jsonObject.getInt("end");
            Log.d("End",Integer.toString(end));
            /* Put into the List */
            int len = manga_array.length();
            Log.e("Length",Integer.toString(len));
            int iterate=0;

            while(iterate<len)
            {

                anime_list[iterate]=manga_array.getJSONObject(iterate).getString("a");
                iterate++;
            }

        }
        catch(Exception e)
        {
            e.printStackTrace();
        }

        /* Wait for MangaAsync to finish */

        String[] _manga_ = new String[]{
                "Naruto Shippuden",
                "Fairy Tail",
                "Bleach",
                "One Piece",
                "Attack on the titans"

        };


        final ArrayAdapter<String> _manga_adapter = new ArrayAdapter<String>(this, R.layout.list_item, R.id.manga_text,anime_list);
        _manga_list.setAdapter(_manga_adapter);


        _manga_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                // ListView Clicked item index
                int itemPosition = position;

                // ListView Clicked item value
                String itemValue = (String) _manga_list.getItemAtPosition(position);

                // Show Alert
                Toast.makeText(getApplicationContext(),
                        "Position :" + itemPosition + "  ListItem : " + itemValue, Toast.LENGTH_LONG)
                        .show();

            }

        });

    }
}
