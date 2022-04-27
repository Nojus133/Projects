package com.example.s344224mappe2;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class BestillingerFragment extends Fragment {
    private static BestillingerFragmentListener listener;
    private List<Bestilling> alleBestillinger;
    private BestillingListAdapter adapter;
    private ListView lv;
    private TextView emptyText;
    private FloatingActionButton fab;

    private void initParams(View v) {
        fab = (FloatingActionButton) v.findViewById(R.id.floating_action_button);
        emptyText = (TextView) v.findViewById(android.R.id.empty);
        lv = (ListView) v.findViewById(R.id.liste1);
        alleBestillinger = getArguments().getParcelableArrayList("alleBestillinger");
        adapter = new BestillingListAdapter(getContext(), alleBestillinger);
    }

    public interface BestillingerFragmentListener {
        void onListItemClick(Bestilling bestilling);
        void onFabClick(String tag);
        void onDeleteItemClick(Long position, String tag);
    }

    public void onAttach(Context context) {
        super.onAttach(context);
        Activity activity;
        activity = (Activity) context;
        try {
            listener = (BestillingerFragmentListener) activity;
            System.out.println("satt lytter");
        }
        catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + "må implementere BestillingerFragmentListener");
        }
    }

    public BestillingerFragment() {}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_restauranter, container, false);
        initParams(v);

        emptyText.setText(R.string.empty_list_bestillinger);
        lv.setEmptyView(emptyText);
        lv.setAdapter(adapter);

        adapter.setAdapterCallbackListener(new BestillingListAdapter.AdapterCallback() {
            @Override
            public void deleteItem(int position) {
                long pos = position;
                listener.onDeleteItemClick(pos, "bestillinger");
            }
        });

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Bestilling item = adapter.getItem(i);
                listener.onListItemClick(item);
            }
        });

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onFabClick("bestillinger");
            }
        });

        return v;
    }
}
