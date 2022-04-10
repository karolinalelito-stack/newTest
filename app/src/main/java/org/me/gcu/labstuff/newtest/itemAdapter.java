package org.me.gcu.labstuff.newtest;
//Karolina Lelito S1821063

import android.content.Context;
import android.content.Intent;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;


public class itemAdapter extends ArrayAdapter<Item> implements Filterable {
    public ArrayList<Item> trafficItems;
    public ArrayList<Item> tItems1;
    private Context cntxt;
    private Boolean incs = false;
    private Filter filter;



    private static LayoutInflater inflater = null;

    public itemAdapter (Context context, ArrayList<Item> _items) {
        super(context, 0, _items);
        cntxt = context;
        this.trafficItems = _items;
        this.tItems1 = _items;

    }



    public Item getItems(int position) {
        return tItems1.get(position);
    }

//    @Override
//    public int getCount() {
//        return tItems1.size();
//    }
//
//    @Override
//    public long getItemId(int position) {
//        return tItems1.get(position).hashCode();
//    }

    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {
        Item ite = getItems(position);

        ItemHolder iHolder = new ItemHolder();

        if (convertView == null) {
            Log.d("MyTag", "No Filter");
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.my_list_view, parent, false);
            TextView iName = (TextView) convertView.findViewById(R.id.iName);
            TextView iDat = (TextView) convertView.findViewById(R.id.iDat);
            LinearLayout click= (LinearLayout) convertView.findViewById(R.id.click);

            iHolder.clickable = click  ;
            iHolder.iTitleView = iName;
            iHolder.iDatView = iDat;
//                holder.dateView= tvDate;

            iName.setText(ite.getTitle());
            iDat.setText(ite.getDat1());


            incs = ite.getIncs();
            if (incs == false){

            }
            convertView.setTag(iHolder);
        }else{
            iHolder = (ItemHolder) convertView.getTag();
        }

        final Item t = tItems1.get(position);


        TextView tv =  iHolder.iTitleView;
        tv.setText(t.getTitle());
        if (incs == false) {
            iHolder.iDatView.setText(t.getDat1());

        }else {
            iHolder.iDatView.setText("");
        }
        iHolder.clickable.setOnClickListener(v -> {

            Intent i = new Intent(cntxt, ItemActivity.class);

            i.putExtra("title", t.getTitle());
            i.putExtra("desc", t.getDesc());
            i.putExtra("inc", t.getIncs());
            i.putExtra("pub", t.getPubDate());
            i.putExtra("geo", t.getGeorss());

            if(incs == false) {
              //  i.putExtra("dur", Long.toString(t.getDuration()) + " hrs");
                i.putExtra("dat", t.getDat2());
                //i.putExtra("col", t.getColour());
            }
            cntxt.startActivity(i);
        });
        return convertView;
    }


    public void resetData() {
        tItems1 = trafficItems;
    }

    private static class ItemHolder {
        public TextView iTitleView;
        public TextView iDatView;
        public TextView iDurView;
        public LinearLayout clickable;
    }

    @Override
    public Filter getFilter() {

        Log.e("MyTag"," filter ");
        if (filter == null)
            filter = new myFilter();


        return filter;

    }

    private class myFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            Log.e("MyTag"," filterin ");
            FilterResults results = new FilterResults();

            if (constraint == null || constraint.length() == 0) {
                Log.e("MyTag"," .. ");


                ArrayList<Item> ar = new ArrayList<Item>(trafficItems);

                results.values = ar;
                results.count = ar.size();


            }
            else {

                ArrayList<Item> newIList = new ArrayList<Item>();

                for (Item p : trafficItems) {
                    if ((p.getTitle().toUpperCase().contains(constraint.toString().toUpperCase())) | (p.getDat1().toUpperCase().contains(constraint.toString().toUpperCase()))) {

                        newIList.add(p);
                    }
                }

                results.values = newIList;
                results.count = newIList.size();
            }



            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint,
                                      FilterResults results) {
            switch (results.count) {
                case 0:
                    notifyDataSetInvalidated();
                    break;
                default:
                    tItems1 = (ArrayList<Item>) results.values;
                    notifyDataSetChanged();
                    break;
            }
        }
    }

}




