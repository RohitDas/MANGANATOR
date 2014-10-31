package manga.anime.com.manganator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by rohitangsu.das on 30/10/14.
 */
public  class DataStore {

    private static String _MANGA_DATA;

    public static void set_MANGA_DATA(String data) {
        _MANGA_DATA = data;
    }

    public static String get_MANGA_DATA() {
        return _MANGA_DATA;
    }

    public  static String get_MANGA_ID(String manga_name)
    {
           int len=0;
           try {
               JSONObject obj = new JSONObject(_MANGA_DATA);
               JSONArray array = obj.getJSONArray("manga");
               while(len<obj.length())
               {
                    if(array.getJSONObject(len).getString("a").equals(manga_name))
                    return array.getJSONObject(len).getString("i");


               }
           }catch(JSONException je)
           {

                je.printStackTrace();
           }
          return null;
    }

}
