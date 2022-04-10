package org.me.gcu.labstuff.newtest;
//Karolina Lelito S1821063

import android.accessibilityservice.AccessibilityService;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView itemsTitle;


    private String dResult = "";
    private Button cIncidentsBtn;
    private Button rWorksBtn;
    private Button pRWorksBtn;

    private String url1 = "https://trafficscotland.org/rss/feeds/currentincidents.aspx";
    private String url2 = "https://trafficscotland.org/rss/feeds/roadworks.aspx";
    private String url3 = "https://trafficscotland.org/rss/feeds/plannedroadworks.aspx";


    private ListView lview;
    private EditText search;

    private String cIncTitle[] = {};
    private String pWorkTitle[] = {};
    private String roadTitle[] = {};

    private ArrayList<Item> cIncItem;
    private ArrayList<Item> roadItem;
    private ArrayList<Item> pWorkItem;


    private ArrayList<Item> temp = new ArrayList<>();
    Item i = new Item();
    private itemAdapter adapterr;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        temp.add(i);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (!isNetworkAvailable() == true) {
            new AlertDialog.Builder(this)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setTitle("Internet Connection Alert")
                    .setMessage("Please Check Your Internet Connection")
                    .setPositiveButton("Close", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            finish();
                        }
                    }).show();
        } else if (isNetworkAvailable() == true) {
            Toast.makeText(MainActivity.this,
                    "Welcome", Toast.LENGTH_LONG).show();
        }







        itemsTitle = (TextView) findViewById(R.id.itemsTitle);

        cIncidentsBtn = (Button) findViewById(R.id.cIncidentsBtn);
        cIncidentsBtn.setOnClickListener(this);

        rWorksBtn = (Button) findViewById(R.id.rWorksBtn);
        rWorksBtn.setOnClickListener(this);

        pRWorksBtn = (Button) findViewById(R.id.pRWorksBtn);
        pRWorksBtn.setOnClickListener(this);


        lview = (ListView) findViewById(R.id.itemsList);

        search = (EditText) findViewById(R.id.filter);
        search.setVisibility(View.INVISIBLE);





        adapterr = new itemAdapter(this, temp);

        search.addTextChangedListener(new TextWatcher() {
                                          @Override
                                          public void onTextChanged(CharSequence s, int start, int before, int count) {
                                              System.out.println("Text ["+s+"] - Start ["+start+"] - Before ["+before+"] - Count ["+count+"]");
                                              if (count < before) {

                                                  adapterr.resetData();
                                              }
                                              adapterr.getFilter().filter(s.toString());
                                          }

                                          @Override
                                          public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                                              // ignore
                                          }

                                          @Override
                                          public void afterTextChanged(Editable s) {

                                          }
                                      }
        );

    }
    public boolean isNetworkAvailable() {

        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        if (connectivityManager != null) {


            if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                NetworkCapabilities capabilities = connectivityManager.getNetworkCapabilities(connectivityManager.getActiveNetwork());
                if (capabilities != null) {
                    if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {

                        return true;
                    } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {

                        return true;
                    } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)) {

                        return true;
                    }
                }
            }
        }

        return false;}




    public void setAdapter(String type){
        ArrayAdapter<String> adapter;
        itemAdapter ad;

        if (!"incident".equals(type)) {
            if ("roadworks".equals(type)) {
                Log.e("MyTag", "items r : " + roadItem);

                adapterr = new itemAdapter(
                        MainActivity.this,

                        roadItem);
                lview.setAdapter(adapterr);
            } else if ("planned".equals(type)) {
                Log.e("MyTag", "items P : " + pWorkItem);

                adapterr = new itemAdapter(
                        MainActivity.this,

                        pWorkItem);
                lview.setAdapter(adapterr);
            }
        } else {
            Log.e("MyTag", "items i : " + cIncItem);

            adapterr = new itemAdapter(
                    MainActivity.this,

                    cIncItem);
            lview.setAdapter(adapterr);
        }
    }


    public void onClick(View view) {


        String type;


        int id = view.getId();
        if (id == R.id.cIncidentsBtn) {
            type = "incident";
            itemsTitle.setText("Incidents:");

            if (cIncTitle.length == 0) {

                sProgress(url1, type);
            } else {

                setAdapter(type);
            }
        } else {
            if (id == R.id.rWorksBtn) {
                type = "roadworks";

                itemsTitle.setText("Roadworks:");

                if (roadTitle.length != 0) {

                    setAdapter(type);
                } else {

                    sProgress(url2, type);
                }
            } else if (id == R.id.pRWorksBtn) {
                type = "planned";

                itemsTitle.setText("Planned Roadworks:");

                if (pWorkTitle.length != 0) {

                    setAdapter(type);
                } else {

                    sProgress(url3, type);
                }


            }
        }


    }

    public void sProgress(String url, String type)
    {

        String[] urlAndType = {url, type};


        new downloadTask().execute(urlAndType);


    }

    public ArrayList handler(String data){
//

        RssParse p = new RssParse();
        p.parse(data);



        return p.getApps();
    }

    private class downloadTask extends AsyncTask<String[], Void, ArrayList<Item>> {

        String tType;

        @Override
        public ArrayList<Item> doInBackground(String[]... params) {
            tType = params[0][1];


          return getURLData(params[0][0]);
        }

        @Override
        protected void onPostExecute(ArrayList<Item> result) {
            super.onPostExecute(result);


            if(result.get(0).getTitle() == "No Incidents Found") {
            }else{
                search.setVisibility(View.VISIBLE);
            }

            setAdapter(tType);
        }

        private ArrayList<Item> getURLData(String url)
        {
            URL aurl;
            URLConnection yc;
            BufferedReader in = null;
            String inputLine = "";

            Log.e("MyTag","GETTING DATA FROM " + url);
            Log.e("MyTag","in run");

            try{
                dResult = "";
                Log.e("MyTag","in try");
                aurl = new URL(url);
                yc = aurl.openConnection();
                in = new BufferedReader(new InputStreamReader(yc.getInputStream()));
                Log.e("MyTag","GETTING DATA FROMd " + url);

                Log.e("MyTag", "result:");
                while ((inputLine = in.readLine()) != null){

                    dResult = dResult + inputLine;

                }
                Log.d("MyTag", "result done : " + dResult);
                in.close();
            }
            catch (IOException ae)
            {
                Log.e("MyTag", "ioexception");
            }




            ArrayList<Item> items= handler(dResult);



            String iTitle[];
            int i = 0;
            iTitle = new String [items.size()];
            for (Item o : items){

                iTitle[i] = o.getTitle();
                i++;
            }



            if ("incident".equals(tType)) {
                cIncTitle = iTitle;
                cIncItem = items;
            } else if ("roadworks".equals(tType)) {
                roadTitle = iTitle;
                roadItem = items;
            } else if ("planned".equals(tType)) {
                pWorkTitle = iTitle;
                pWorkItem = items;
            }
            return items;
        }
    }

}