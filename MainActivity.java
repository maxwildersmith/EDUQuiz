package com.example.quiz.eduquiz;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity {

    public static final String TAG = MainActivity.class.getSimpleName();

    private EditText input;
    private Button button;
    private TextView output;
    private String search;
    private ArrayList<Wiki> wikis;
    private WikiFragment frag;
    public static final String BASE_URL="http://www.wikia.com/api/v1/Wikis/ByString/?string=" ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        wikis = new ArrayList<Wiki>();
        frag = new WikiFragment();
        FragmentManager fm = getSupportFragmentManager();
        if(fm.findFragmentByTag("FamilyFragment")== null)
            fm.beginTransaction()
                    .add(R.id.frame, frag, "FamilyFragment")
                    .commit();

        input = (EditText)findViewById(R.id.input);
        button = (Button)findViewById(R.id.go);
        output = ((TextView)findViewById(R.id.data));

        String s = "";
        String out = "";


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                wikis.clear();
                String in = "";
                if(input.getText().length()==0){
                    Toast.makeText(MainActivity.this, "Enter Text.", Toast.LENGTH_LONG).show();
                    return;
                }
                in = input.getText().toString();
                String s="";
                try {
                    String out=new PersonSearch().execute(BASE_URL+in, null,"").get();
                    JSONObject jsonO;
                    try {
                        jsonO = new JSONObject(out);
                        s="";
                        JSONArray json = jsonO.getJSONArray("items");
                        for(int i=0;i<json.length();i++)
                            if(json.getJSONObject(i).optString("language").equals("en"))
                                wikis.add(new Wiki(json.getJSONObject(i).optString("name"),json.getJSONObject(i).optString("domain")));
                        //s+=json.getJSONObject(i).optString("name")+"\n";
                        frag.populateList(wikis);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    output.setText(s);


                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }





            }});



//        try {
//            InputStream in = getAssets().open("data.json");
//            BufferedReader read =  new BufferedReader(new InputStreamReader(in));
//            String line;
//            while((line=read.readLine())!= null)
//                s+=line;
//        } catch (IOException e) {
//            e.printStackTrace();
//            Log.e(TAG, "onCreate: blah blah");
//            Toast.makeText(MainActivity.this, "ARGGHHHH!!!!", Toast.LENGTH_SHORT).show();
//        }




    }

    public class PersonSearch extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {
            try {
                URL url = new URL(urls[0]);
                URLConnection connection = url.openConnection();

                String out="";

                InputStream in = connection.getInputStream();
                BufferedReader read =  new BufferedReader(new InputStreamReader(in));
                String line;
                while((line=read.readLine())!= null)
                    out+=line;
                Log.e("asdf",out);
                return out;

            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

    }



}
