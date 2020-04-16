package com.jgroen.juliangroenstudenttracker.features.term;

import android.content.Context;
import android.icu.text.SimpleDateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.jgroen.juliangroenstudenttracker.R;

import java.util.Calendar;
import java.util.List;

public class TermAdapter extends RecyclerView.Adapter<TermAdapter.TermViewHolder> {

    private final LayoutInflater inflater;
    private final Context context;
    private List<TermEntity> terms;

    private AdapterCallback callback;

    public interface AdapterCallback {
        void onItemClicked(TermEntity term);
        void onItemLongClicked(TermEntity term);
    }

    public TermAdapter(Context context, AdapterCallback callback) {
        this.callback = callback;
        inflater = LayoutInflater.from(context);
        this.context = context;
    }

    class TermViewHolder extends RecyclerView.ViewHolder {
        private final TextView termListTitle;
        private final TextView termListStartDate;
        private final TextView termListEndDate;

        private TermViewHolder(View itemView) {
            super(itemView);

            termListTitle = itemView.findViewById(R.id.termListTitle);
            termListStartDate = itemView.findViewById(R.id.termListStartDate);
            termListEndDate = itemView.findViewById(R.id.termListEndDate);

        }

    }

    @NonNull
    @Override
    public TermViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = inflater.inflate(R.layout.term_list_item, parent, false);
        return new TermViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull TermViewHolder holder, int position) {

        if (terms != null) {

            TermEntity current = terms.get(position);
            Calendar cal = Calendar.getInstance();

            cal.setTime(current.getTermStartDate());
            String startDate = context.getResources().getString(R.string.term_list_item_start_date,
                    new SimpleDateFormat("MMM dd yyyy").format(cal.getTime()));

            cal.setTime(current.getTermEndDate());
            String endDate = context.getResources().getString(R.string.term_list_item_end_date,
                    new SimpleDateFormat("MMM dd yyyy").format(cal.getTime()));

            holder.termListTitle.setText(current.getTermTitle());
            holder.termListStartDate.setText(startDate);
            holder.termListEndDate.setText(endDate);

        } else {

            holder.termListTitle.setText("N/A");
            holder.termListStartDate.setText("N/A");
            holder.termListEndDate.setText("N/A");

        }

        holder.itemView.setOnClickListener(view -> {
            if (callback != null)
                callback.onItemClicked(terms.get(position));
        });

        holder.itemView.setOnLongClickListener(view -> {
            if (callback != null)
                callback.onItemLongClicked(terms.get(position));
            return true;
        });

    }

    public void setTerms(List<TermEntity> termList) {
        terms = termList;
        notifyDataSetChanged();
    }

    public TermEntity getCurrentTerm(int position) {

        return terms.get(position);

    }

    @Override
    public int getItemCount() {
        if (terms != null)
            return terms.size();
        else return 0;
    }

}
