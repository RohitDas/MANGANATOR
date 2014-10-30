package manga.anime.com.manganator;



/* MANGANATER is an android application wherein you could read manga to begin with */
/* This App makes url request to mangareader to get all the information about the latest animes. */


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
import android.widget.Button;
import android.util.Log;
import java.io.IOException;
import java.io.InputStream;
import android.content.Intent;

public class MyActivity extends Activity {
    int lock = 0;
    String result = "";
    InputStream in = null ;
    JSONObject  jsonObject = null ;
    String[] anime_list= new String[20000];
     Button button ;
    Intent intent;
    Bundle bundle = new Bundle();
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my);
        final ListView _manga_list = (ListView)findViewById(R.id.listview);
        addListeneronButton();
        Log.e("STATUS",MangaAsync.Status.FINISHED.toString());
        /* Wait for MangaAsync to finish */

    }

    public void addListeneronButton()
    {
        button = (Button)findViewById(R.id.button1);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              new MangaAsync().execute();

            }
        });
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.my, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private class MangaAsync extends AsyncTask<String,Integer,String> {

        protected String  doInBackground(String... params) {
            // TODO Auto-generated method stub
            get_Data();
            return null;

        }
        protected void onPostExecute(String results){
            Toast.makeText(getApplicationContext(), "command sent", Toast.LENGTH_LONG).show();
            bundle.putString("Json_Manga",result);

           intent  = new Intent(MyActivity.this,Manga_List_Activity.class);

            intent.putExtras(bundle);
            startActivity(intent);

        }



    }

    /* Post Method */
    public void get_Data()
    {
        try {
            HttpClient client = new DefaultHttpClient();
            HttpGet get_request = new HttpGet("http://www.mangaeden.com/api/list/0/");
            HttpResponse response = client.execute(get_request);
            HttpEntity entity = response.getEntity();
            in =  entity.getContent();

        } catch(Exception e)
        {
            Log.e("Error","err");
        }
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            StringBuilder sb = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
            in.close();
            result = sb.toString();
            Log.e("Result",result);
        } catch(Exception e) {
            Log.e("Error","error");
        }




    }


}
