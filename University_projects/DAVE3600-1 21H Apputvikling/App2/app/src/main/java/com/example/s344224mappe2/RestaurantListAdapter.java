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

import java.util.List;

public class RestaurantListAdapter extends BaseAdapter{
    private AdapterCallback callback;
    private List<Restaurant> list;
    private LayoutInflater layoutInflater;
    private Context context;
    private MyDialog dialog;

    public RestaurantListAdapter(Context context, List<Restaurant> list) {
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
    public Restaurant getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        RestaurantViewHolder holder;

        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.list_item, null);
            holder = new RestaurantViewHolder();

            holder.icon = (ImageView) convertView.findViewById(R.id.delete_item);
            holder.id = (TextView) convertView.findViewById(R.id.text_id);
            holder.navn = (TextView) convertView.findViewById(R.id.text1);
            holder.adresse = (TextView) convertView.findViewById(R.id.text2);
            holder.telefon = (TextView) convertView.findViewById(R.id.text3);
            holder.type = (TextView) convertView.findViewById(R.id.text4);

            convertView.setTag(holder);
            holder.icon.setTag(position);
        }
        else {
            holder = (RestaurantViewHolder) convertView.getTag();
        }

        holder.icon.setImageResource(R.drawable.ic_baseline_delete_24);
        holder.id.setText(Long.toString(list.get(position).get_id()));
        holder.navn.setText(list.get(position).getNavn());
        holder.adresse.setText("  "+list.get(position).getAdresse());
        holder.telefon.setText("  "+list.get(position).getTelefon());
        holder.type.setText("  "+list.get(position).getType());

        holder.icon.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                FragmentActivity activity = (FragmentActivity)(context);
                FragmentManager fm = activity.getSupportFragmentManager();
                dialog = new MyDialog(
                        "Slett restaurant?",
                        "Ja, slett",
                        "Nei"
                );
                dialog.setDialogClickListener(new MyDialog.DialogClickListener() {
                    @Override
                    public void onYesClick() {
                        String t = (String) holder.id.getText();
                        for (int i = 0; i < list.size(); i++) {
                            Restaurant r = list.get(i);
                            if (r.get_id() == Long.parseLong(t)) {
                                list.remove(r);
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
                dialog.show(fm, "Slett Restaurant");


            }
        });

        return convertView;
    }

    static class RestaurantViewHolder {
        ImageView icon;
        TextView id;
        TextView navn;
        TextView adresse;
        TextView telefon;
        TextView type;
    }
}
