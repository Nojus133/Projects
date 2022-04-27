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

public class VennListAdapter extends BaseAdapter{
    private AdapterCallback callback;
    private List<Venn> list;
    private LayoutInflater layoutInflater;
    private Context context;
    private MyDialog dialog;

    public VennListAdapter(Context context, List<Venn> list) {
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
    public Venn getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        VennViewHolder holder;

        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.list_item2, null);
            holder = new VennViewHolder();

            holder.icon = (ImageView) convertView.findViewById(R.id.delete_item3);
            holder.id = (TextView) convertView.findViewById(R.id.text_venn_id);
            holder.navn = (TextView) convertView.findViewById(R.id.text_bestilling_navn);
            holder.telefon = (TextView) convertView.findViewById(R.id.text_bestilling_dato);

            convertView.setTag(holder);
            holder.icon.setTag(position);
        }
        else {
            holder = (VennViewHolder) convertView.getTag();
        }

        holder.icon.setImageResource(R.drawable.ic_baseline_delete_24);
        holder.id.setText(Long.toString(list.get(position).get_id()));
        holder.navn.setText(list.get(position).getFirstName()+" "+list.get(position).getLastName());
        holder.telefon.setText("  "+list.get(position).getTelefon());

        holder.icon.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                FragmentActivity activity = (FragmentActivity)(context);
                FragmentManager fm = activity.getSupportFragmentManager();
                dialog = new MyDialog(
                        "Slett venn?",
                        "Ja, slett",
                        "Nei"
                );
                dialog.setDialogClickListener(new MyDialog.DialogClickListener() {
                    @Override
                    public void onYesClick() {
                        String t = (String) holder.id.getText();
                        for (int i = 0; i < list.size(); i++) {
                            Venn v = list.get(i);
                            if (v.get_id() == Long.parseLong(t)) {
                                list.remove(v);
                                break;
                            }
                        }
                        callback.deleteItem(Integer.parseInt(t));
                        notifyDataSetChanged();
                    }
                    @Override
                    public void onNoClick() { return;}
                });
                dialog.show(fm, "Slett venn");
            }
        });
        return convertView;
    }

    static class VennViewHolder {
        ImageView icon;
        TextView id;
        TextView navn;
        TextView telefon;
    }
}
