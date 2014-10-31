package manga.anime.com.manganator;


import android.app.Activity;
import android.content.Intent;
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
import android.widget.EditText;
import android.text.TextWatcher;
import android.text.Editable;
import java.util.Locale;
/**
 * Created by ROHIT on 10/19/2014.
 */
public class Manga_List_Activity extends Activity {
    int lock = 0;
    String result_manga = "";
    InputStream in = null;
    JSONObject jsonObject = null;
    String[] anime_list = new String[500];
    String[] anime_list_status = new String[500];
    String manga_string;
    EditText editText;
    String item_id;
    ArrayAdapter<String>  _manga_adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_intent);
        final ListView _manga_list = (ListView) findViewById(R.id.listview);
        editText = (EditText)findViewById(R.id.inputSearch);
        manga_string = DataStore.get_MANGA_DATA();

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

                anime_list[iterate]= manga_array.getJSONObject(iterate).getString("a");
                anime_list_status[iterate] = manga_array.getJSONObject(iterate).getString("s");
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


       _manga_adapter = new ArrayAdapter<String>(this, R.layout.list_item, R.id.manga_text,anime_list);

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
                //Start a new Activity.
                item_id = DataStore.get_MANGA_ID(itemValue);
                new MangaAsync().execute();
                /* Make an  Http Request */


            }

        });
        editText.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
                // When user changed the Text
                Manga_List_Activity.this._manga_adapter.getFilter().filter(cs);
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
                                          int arg3) {
                // TODO Auto-generated method stub

            }

            @Override
            public void afterTextChanged(Editable arg0) {
                // TODO Auto-generated method stub
                String text = editText.getText().toString().toLowerCase(Locale.getDefault());
                 _manga_adapter.getFilter().filter(text);
            }
        });

    }
    private class MangaAsync extends AsyncTask<String,Integer,String> {

        protected String doInBackground(String... params) {
            // TODO Auto-generated method stub
            get_Data();
            return null;

        }

        protected void onPostExecute(String results) {
            Toast.makeText(getApplicationContext(), "command sent", Toast.LENGTH_LONG).show();
            //  DataStore.set_MANGA_DATA(result);
            // intent  = new Intent(MyActivity.this,Manga_List_Activity.class);
            // startActivity(intent);

        }
    }
        public void get_Data() {
            try {
                HttpClient client = new DefaultHttpClient();
                HttpGet get_request = new HttpGet("http://www.mangaeden.com/api/manga/" + item_id);
                HttpResponse response = client.execute(get_request);
                HttpEntity entity = response.getEntity();
                in = entity.getContent();

            } catch (Exception e) {
                Log.e("Error", "err");
            }
            try {
                BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                StringBuilder sb = new StringBuilder();
                String line = null;
                while ((line = reader.readLine()) != null) {
                    sb.append(line + "\n");
                }
                in.close();
                result_manga = sb.toString();
                Log.e("Result", result_manga);
            } catch (Exception e) {
                Log.e("Error", "error");
            }
        }
}
