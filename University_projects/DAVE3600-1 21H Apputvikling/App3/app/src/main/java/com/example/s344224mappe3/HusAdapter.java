package com.example.s344224mappe3;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.material.chip.Chip;

import java.util.List;

public class HusAdapter extends RecyclerView.Adapter<HusAdapter.HusViewHolder> {
    private Context context;
    private List<Hus> husList;
    private int previousAdapterPosition;
    private OnItemClickListener listener;
    private Hus temp;

    public interface OnItemClickListener {
        void onItemClick(Hus hus, int position);
        void onDeleteClick(Hus hus, int position);
        void onEditClick(Hus hus);
    }

    public HusAdapter(Context context, List<Hus> husList) {
        this.context = context;
        this.husList = husList;
        this.previousAdapterPosition = -1;
    }

    public List<Hus> getHusList() {
        return husList;
    }

    public int getPreviousAdapterPosition() {
        return previousAdapterPosition;
    }

    public void setPreviousAdapterPosition(int previousAdapterPosition) {
        this.previousAdapterPosition = previousAdapterPosition;
    }

    public Hus getTemp() {
        return temp;
    }

    public void setTemp(Hus temp) {
        this.temp = temp;
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public HusViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);
        return new HusViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HusViewHolder holder, int position) {
        Hus hus = husList.get(position);
        holder.husTextView.setText("Hus "+hus.getId());
        holder.adresseTextView.setText(hus.getAdresse());
        holder.antallEtasjerTextView.setText(hus.getAntallEtasjer()+" etasjer");
        holder.koordinaterTextView.setText("Koordinater: "+hus.getKoordinater());
        holder.beskrivelseTextView.setText(hus.getBeskrivelse());


        boolean isExpanded = hus.isExpanded();
        holder.expandableLayout.setVisibility(isExpanded ? View.VISIBLE : View.GONE);

        Animation rotate = AnimationUtils.loadAnimation(context, R.anim.rotate);
        Animation rotateBack = AnimationUtils.loadAnimation(context, R.anim.rotate_back);
        rotate.setFillAfter(true);
        rotateBack.setFillAfter(true);
        holder.arrow.startAnimation(isExpanded ? rotate : rotateBack);
    }

    @Override
    public int getItemCount() {
        return husList.size();
    }

    public Hus getItemByCoordinates(LatLng koord) {
        float lat = (float) koord.latitude;
        float lon = (float) koord.longitude;
        Hus hus = null;
        for (Hus h : husList) {
            String k = h.getKoordinater();
            float lat2 = Float.parseFloat(k.substring(0, k.indexOf(":")));
            float lon2 = Float.parseFloat(k.substring(k.indexOf(":")+1, k.length()));
            if (lat == lat2 && lon == lon2) {
                 hus = h;
                 break;
            }
        }
        return hus;
    }

    public int getItemPositionByCoordinates(LatLng koord) {
        float lat = (float) koord.latitude;
        float lon = (float) koord.longitude;
        int position = -1;
        for (int i = 0; i < husList.size(); i++) {
            String k = husList.get(i).getKoordinater();
            float lat2 = Float.parseFloat(k.substring(0, k.indexOf(":")));
            float lon2 = Float.parseFloat(k.substring(k.indexOf(":")+1, k.length()));
            if (lat == lat2 && lon == lon2) {
                position = i;
                break;
            }
        }
        return position;
    }

    class HusViewHolder extends RecyclerView.ViewHolder {
        TextView husTextView, adresseTextView, antallEtasjerTextView, koordinaterTextView, beskrivelseTextView, arrow;
        ConstraintLayout expandableLayout;
        Chip delete, endre;

        public HusViewHolder(@NonNull View itemView) {
            super(itemView);

            husTextView = itemView.findViewById(R.id.husNr);
            adresseTextView = itemView.findViewById(R.id.adresse);
            antallEtasjerTextView = itemView.findViewById(R.id.antall_etasjer);
            koordinaterTextView = itemView.findViewById(R.id.koordinater);
            beskrivelseTextView = itemView.findViewById(R.id.beskrivelse);
            expandableLayout = itemView.findViewById(R.id.expandable);
            arrow = itemView.findViewById(R.id.arrow);
            delete = itemView.findViewById(R.id.chip_delete);
            endre = itemView.findViewById(R.id.chip_endre);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Hus hus = husList.get(getAdapterPosition());
                    listener.onItemClick(hus, getAdapterPosition());
                }
            });

            delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Hus hus = husList.get(getAdapterPosition());
                    listener.onDeleteClick(hus, getAdapterPosition());
                }
            });

            endre.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Hus hus = husList.get(getAdapterPosition());
                    listener.onEditClick(hus);
                }
            });
        }
    }
}
