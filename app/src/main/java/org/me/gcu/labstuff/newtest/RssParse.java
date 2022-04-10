package org.me.gcu.labstuff.newtest;


import android.util.Log;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;
import java.io.StringReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;



public class RssParse {

    private ArrayList<Item> apps;



    public RssParse() {
        this.apps = new ArrayList<>();
    }

    private Date[] getDates(String[] parts){


        Date newSDate = null;
        Date newEDate = null;

        try{
            //string dates
            String startsDate = parts[0].substring(12);
            String endsDate = parts[1].substring(10);

            //first format
            SimpleDateFormat sdf = new SimpleDateFormat("EEE, d MMMMM yyyy - HH:mm", Locale.ENGLISH);
            Date sd = sdf.parse(startsDate);
            Date ed = sdf.parse(endsDate);

            //reformat
            sdf.applyPattern("dd/MM/yy HH:mm");
            String startDate = sdf.format(sd);
            String endDate = sdf.format(ed);
            newSDate = sdf.parse(startDate);
            newEDate = sdf.parse(endDate);

        } catch (ParseException e){
            e.printStackTrace();
        }

        return new Date[]{newSDate, newEDate};
    };



    public ArrayList<Item> getApps() {
        return apps;
    }

    public boolean parse(String xmlData) {
        boolean status = true;
        Item currentTraffItem = null;
        boolean inEntry = false;
        String textValue = "";

        try {

            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            factory.setNamespaceAware(true);
            XmlPullParser xpp = factory.newPullParser();
            xpp.setInput(new StringReader(xmlData));
            int eventType = xpp.getEventType();


            while(eventType != XmlPullParser.END_DOCUMENT) {
                String tagName = xpp.getName();
                if (eventType == XmlPullParser.START_TAG) {
                    if ("item".equalsIgnoreCase(tagName)) {
                        inEntry = true;
                        currentTraffItem = new Item();
                     //   Log.d(TAG, "parse: NEW ITEM");
                    }
                } else if (eventType == XmlPullParser.TEXT) {
                    textValue = xpp.getText();
                } else if (eventType == XmlPullParser.END_TAG) {
                    if (inEntry) {
                        if ("item".equalsIgnoreCase(tagName)) {
                            apps.add(currentTraffItem);
                            inEntry = false;
                        } else if ("title".equalsIgnoreCase(tagName)) {
                            currentTraffItem.setTitle(textValue);
                        } else if ("description".equalsIgnoreCase(tagName)) {
                            if (textValue.contains("Start Date:")) {
                                String[] parts = textValue.split("<br />");
                                Date dates[] = getDates(parts);

                                Log.d("MyTag", "" + parts[0]);
                                Log.d("MyTag", "" + parts[1]);


                                String dat1 = dates[0].toString().substring(0, 10) + " - " + dates[1].toString().substring(0, 10);
                                String dat2 = dates[0].toString().substring(0, 23) + " - " + dates[1].toString().substring(0, 23);

                                currentTraffItem.setDesc(parts[2]);
                                currentTraffItem.setStartDate(dates[0]);
                                currentTraffItem.setEndDate(dates[1]);
                                currentTraffItem.setDat1(dat1);
                                currentTraffItem.setDat2(dat2);

                                currentTraffItem.setInc(false);

                            } else {
                                currentTraffItem.setInc(true);
                                currentTraffItem.setDesc(textValue);
                            }
                        } else if ("link".equalsIgnoreCase(tagName)) {
                            currentTraffItem.setLink(textValue);
                        } else if ("point".equalsIgnoreCase(tagName)) {
                            Log.d("MyTag", "GEO TAG: " + textValue);
                            currentTraffItem.setGeorss(textValue);
                        }
                    }
                }
                eventType = xpp.next();

            }
            Log.d("ParseIncidents", "new items: ");
            for (Item app: apps) {

            }

        } catch(Exception e) {
            status = false;
            e.printStackTrace();
        }
        return status;
    }



}