package ua.kpi.comsys.iv7105.lab3;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TabHost;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import org.json.*;

import ua.kpi.comsys.iv7105.lab3.model.Movie;

public class MainActivity extends AppCompatActivity {

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setTitle("Lab3");

        TabHost tabHost = findViewById(R.id.tabHost);

        tabHost.setup();

        TabHost.TabSpec tabSpec = tabHost.newTabSpec("tag1");

        tabSpec.setContent(R.id.linearLayout);
        tabSpec.setIndicator("Movies");
        tabHost.addTab(tabSpec);

        tabSpec = tabHost.newTabSpec("tag2");
        tabSpec.setContent(R.id.linearLayout2);
        tabSpec.setIndicator("Songs");
        tabHost.addTab(tabSpec);

        tabSpec = tabHost.newTabSpec("tag3");
        tabSpec.setContent(R.id.linearLayout3);
        tabSpec.setIndicator("Weapons");
        tabHost.addTab(tabSpec);

        tabHost.setCurrentTab(0);

        // End tab

        String jsonString = "";
        try (InputStream is = this.getResources().openRawResource(R.raw.movies_list)) {
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            jsonString = reader.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }

        List<Movie> movies = new LinkedList<>();
        try {
            JSONObject obj = new JSONObject(jsonString);

            int size = obj.getJSONArray("Search").length();
            for (int i = 0; i < size; i++) {
                JSONObject movieObj = obj.getJSONArray("Search").getJSONObject(i);

                movies.add(new Movie(
                        movieObj.getString("Title"),
                        movieObj.getString("Year"),
                        movieObj.getString("imdbID"),
                        movieObj.getString("Type"),
                        movieObj.getString("Poster")));
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        List<String> moviesRepr = new LinkedList<>();
        for (Movie m : movies) {
            moviesRepr.add(m.toString());
        }

        final ListView listView = findViewById(R.id.list_view_id);
        final StableArrayAdapter adapter = new StableArrayAdapter(this,
                android.R.layout.simple_list_item_1, moviesRepr);
        listView.setAdapter(adapter);
    }

    private class StableArrayAdapter extends ArrayAdapter<String> {

        HashMap<String, Integer> mIdMap = new HashMap<String, Integer>();

        public StableArrayAdapter(Context context, int textViewResourceId,
                                  List<String> objects) {
            super(context, textViewResourceId, objects);
            for (int i = 0; i < objects.size(); ++i) {
                mIdMap.put(objects.get(i), i);
            }
        }

        @Override
        public long getItemId(int position) {
            String item = getItem(position);
            return mIdMap.get(item);
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }
    }
}