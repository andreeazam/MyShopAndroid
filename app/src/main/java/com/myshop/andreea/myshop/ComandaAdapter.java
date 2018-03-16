package com.myshop.andreea.myshop;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

public class ComandaAdapter extends BaseAdapter {

    private Context context = null;
    private List<Comanda> data = null;
    private LayoutInflater layoutInflater = null;

    private static class ViewHolder {
        TextView numarComandaTextView, valoareComandaTextView, statusComandaTextView;
    };

    public ComandaAdapter(Context context, List<Comanda> data) {
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
        ViewHolder viewHolder;
        Comanda comanda = data.get(position);
        if (convertView == null) {
            customView = layoutInflater.inflate(R.layout.comanda, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.numarComandaTextView = (TextView) customView.findViewById(R.id.numar_comanda_text_view);
            viewHolder.valoareComandaTextView = (TextView) customView.findViewById(R.id.valoare_comanda_text_view);
            viewHolder.statusComandaTextView = (TextView) customView.findViewById(R.id.status_comanda_text_view);
            customView.setTag(viewHolder);
        } else {
            customView = convertView;
        }
        viewHolder = (ViewHolder)customView.getTag();
        viewHolder.numarComandaTextView.setText(String.valueOf(comanda.getNumarComanda()));
        viewHolder.valoareComandaTextView.setText(String.valueOf(comanda.getValoareComanda()));
        viewHolder.statusComandaTextView.setText(String.valueOf(comanda.getTextStatus()));

        return customView;
    }
}
