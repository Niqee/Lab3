package ua.kpi.comsys.iv7105.lab3;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Build;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Field;

import ua.kpi.comsys.iv7105.lab3.model.Movie;

public class MoveDetail extends AppCompatActivity {

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_move_detail);

        Bundle b = getIntent().getExtras();
        String text = b.getString("text");
        String idStr = text.split("\n")[2].split("=")[1];

        int id = getResId(idStr, R.raw.class);

        Movie movie = new Movie();

        String jsonString = "";
        try (InputStream is = this.getResources().openRawResource(id)) {
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            jsonString = reader.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            JSONObject movieObj = new JSONObject(jsonString);

            movie = new Movie(
                        movieObj.getString("Title"),
                        movieObj.getString("Year"),
                        movieObj.getString("imdbID"),
                        movieObj.getString("Type"),
                        movieObj.getString("Poster"));
            movie.setRated(movieObj.getString("Rated"));
            movie.setReleased(movieObj.getString("Released"));
            movie.setRuntime(movieObj.getString("Runtime"));
            movie.setGenre(movieObj.getString("Genre"));
        } catch (JSONException e) {
            e.printStackTrace();
        }


        TextView textView = findViewById(R.id.textView);
        textView.setText(movie.fullInfo());
    }

    public static int getResId(String resName, Class<?> c) {

        try {
            Field idField = c.getDeclaredField(resName);
            return idField.getInt(idField);
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }
}