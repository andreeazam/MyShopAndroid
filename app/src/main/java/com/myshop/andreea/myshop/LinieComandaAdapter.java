package com.myshop.andreea.myshop;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Andreea on 5/24/2017.
 */

public class LinieComandaAdapter extends BaseAdapter {

    private Context context = null;
    private List<LinieComanda> data = null;
    private LayoutInflater layoutInflater = null;

    private static class ViewHolder {
        TextView denumireProdusTextView, cantitateTextView, pretProdusTextView, lccValoareTextView;
    };

    public LinieComandaAdapter(Context context, List<LinieComanda> data) {
        this.context = context;
        this.data = data;

        layoutInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View customView;
        LinieComandaAdapter.ViewHolder viewHolder;
        LinieComanda linieComanda = data.get(position);
        if (convertView == null) {
            customView = layoutInflater.inflate(R.layout.liniecomanda, parent, false);
            viewHolder = new LinieComandaAdapter.ViewHolder();
            viewHolder.denumireProdusTextView = (TextView) customView.findViewById(R.id.denumire_Produs_text_view);
            viewHolder.cantitateTextView = (TextView) customView.findViewById(R.id.cantitate_text_view);
            viewHolder.pretProdusTextView = (TextView) customView.findViewById(R.id.pret_Produs_text_view);
            viewHolder.lccValoareTextView = (TextView) customView.findViewById(R.id.lcc_Valoare_text_view);

            customView.setTag(viewHolder);
        } else {
            customView = convertView;
        }
        viewHolder = (LinieComandaAdapter.ViewHolder)customView.getTag();
        viewHolder.denumireProdusTextView.setText(String.valueOf(linieComanda.getDenumireProdus()));
        viewHolder.cantitateTextView.setText(String.valueOf(linieComanda.getCantitate()));
        viewHolder.pretProdusTextView.setText(String.valueOf(linieComanda.getPretProdus()));
        viewHolder.lccValoareTextView.setText(String.valueOf(linieComanda.getLccValoare()));

        return customView;
    }

}
