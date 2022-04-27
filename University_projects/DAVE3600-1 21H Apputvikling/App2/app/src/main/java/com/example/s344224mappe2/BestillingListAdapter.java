package com.example.s344224mappe2;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class BestillingListAdapter extends BaseAdapter{
    private AdapterCallback callback;
    private List<Bestilling> list;
    private LayoutInflater layoutInflater;
    private Context context;
    private MyDialog dialog;

    public BestillingListAdapter(Context context, List<Bestilling> list) {
        this.context = context;
        this.layoutInflater = LayoutInflater.from(context);
        this.list = list;
    }

    public interface AdapterCallback {
        void deleteItem(int position);
    }

    public void setAdapterCallbackListener(AdapterCallback listener) {
        callback = listener;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Bestilling getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        BestillingViewHolder holder;

        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.list_item3, null);
            holder = new BestillingViewHolder();

            holder.icon = (ImageView) convertView.findViewById(R.id.delete_item3);
            holder.id = (TextView) convertView.findViewById(R.id.text_bestilling_id);
            holder.restaurantNavn = (TextView) convertView.findViewById(R.id.text_bestilling_navn);
            holder.dato = (TextView) convertView.findViewById(R.id.text_bestilling_dato);
            holder.tidspunkt = (TextView) convertView.findViewById(R.id.text_bestilling_tid);
            holder.antallPersoner = (TextView) convertView.findViewById(R.id.text_bestilling_personer);
            holder.adresse = (TextView) convertView.findViewById(R.id.text_bestilling_adresse);

            convertView.setTag(holder);
            holder.icon.setTag(position);
        }
        else {
            holder = (BestillingViewHolder) convertView.getTag();
        }

        holder.icon.setImageResource(R.drawable.ic_baseline_delete_24);
        holder.id.setText(Long.toString(list.get(position).get_id()));
        holder.restaurantNavn.setText(list.get(position).getRestaurant().getNavn());
        holder.dato.setText("  "+ new SimpleDateFormat("dd.MM.yyyy").format(list.get(position).getDato()));
        holder.tidspunkt.setText("  "+ new SimpleDateFormat("HH:mm").format(list.get(position).getTidspunkt()));
        holder.antallPersoner.setText("  "+(list.get(position).getVenner().size()+1));
        holder.adresse.setText(" "+ list.get(position).getRestaurant().getAdresse());

        holder.icon.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                FragmentActivity activity = (FragmentActivity)(context);
                FragmentManager fm = activity.getSupportFragmentManager();
                dialog = new MyDialog(
                        "Slett bestilling?",
                        "Ja, slett",
                        "Nei"
                );
                dialog.setDialogClickListener(new MyDialog.DialogClickListener() {
                    @Override
                    public void onYesClick() {
                        String t = (String) holder.id.getText();
                        for (int i = 0; i < list.size(); i++) {
                            Bestilling b = list.get(i);
                            if (b.get_id() == Long.parseLong(t)) {
                                list.remove(b);
                                break;
                            }
                        }
                        callback.deleteItem(Integer.parseInt(t));
                        notifyDataSetChanged();
                    }

                    @Override
                    public void onNoClick() {

                    }
                });
                dialog.show(fm, "Slett Bestilling");


            }
        });

        return convertView;
    }

    static class BestillingViewHolder {
        ImageView icon;
        TextView id;
        TextView restaurantNavn;
        TextView dato;
        TextView tidspunkt;
        TextView antallPersoner;
        TextView adresse;
    }
}
