package org.me.gcu.labstuff.newtest;
//Karolina Lelito S1821063

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;


public class ItemActivity extends AppCompatActivity {
    String typeOfInc;
    String geo1;
    TextView titleA;
    TextView timeP;
    TextView descA;
    TextView descA2;
    TextView geoA;






    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item);

        Intent intent = getIntent();
        titleA = findViewById(R.id.titleA);
        descA = findViewById(R.id.descA);
        geoA = findViewById(R.id.geoA);
        descA2 = findViewById(R.id.descA2);
        timeP = findViewById(R.id.timeP);



        String title = intent.getExtras().getString("title");
        String dat = intent.getExtras().getString("dat");
        String d = intent.getExtras().getString("desc");
        Boolean incident = intent.getExtras().getBoolean("inc");
        String description = "";
        String description2 = "";
        String description3 = "";


        if (incident == true){typeOfInc = "i";}
        else{
            if(d.contains("Works:")){
                typeOfInc = "works";
            }if(d.contains("TYPE :")){
                typeOfInc = "type";
            }if(d.contains("Delay Information")){
                typeOfInc = "delay";
            }
        }

        String desc[];
        if ("works".equals(typeOfInc)) {
            desc = d.split("Works:|Traffic Management:|Diversion Information:");
            Log.e("MyTag", "desc[0] -> " + desc[1]);
            description = "Works: " + desc[1];
            if (desc.length > 2) {
                Log.e("MyTag", "desc[1] -> " + desc[2]);
                description2 = "Traffic management: " + desc[2];
            }
            if (desc.length > 3) {
                Log.e("MyTag", "desc[2] -> " + desc[3]);
                description3 = "Diversion Information: " + desc[3];
            }
        } else {
            if ("type".equals(typeOfInc)) {
                desc = d.split("TYPE :|Lane Closures :|Location :");
                description = "Type:" + desc[1];
                if (desc.length > 2) {
                    Log.e("MyTag", "desc[2] -> " + desc[2]);
                    description2 = "Location: " + desc[2];
                }
                if (desc.length > 3) {
                    Log.e("MyTag", "desc[3] -> " + desc[3]);
                    description3 = "Lane CLosures: " + desc[3];
                }
            } else if ("delay".equals(typeOfInc)) {
                desc = d.split("Delay Information");
                description = "Delay Information " + desc[1];
            } else if ("i".equals(typeOfInc)) {
                description = "Description: " + d;
            }
        }
        geo1 = intent.getExtras().getString("geo");

        titleA.setText(title);
        descA.setText(description);
        geoA.setText(geo1);

        if (incident == false) {
            String col = intent.getExtras().getString("col");


            timeP.setText(dat);

        }

    }

}

