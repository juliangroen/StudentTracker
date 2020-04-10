package com.jgroen.juliangroenstudenttracker.features.term;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.icu.text.SimpleDateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.jgroen.juliangroenstudenttracker.R;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class TermAdapter extends RecyclerView.Adapter<TermAdapter.TermViewHolder> {

    class TermViewHolder extends RecyclerView.ViewHolder {
        private final TextView termListTitle;
        private final TextView termListStartDate;
        private final TextView termListEndDate;

        private TermViewHolder(View itemView) {
            super(itemView);

            termListTitle = itemView.findViewById(R.id.termListTitle);
            termListStartDate = itemView.findViewById(R.id.termListStartDate);
            termListEndDate = itemView.findViewById(R.id.termListEndDate);

            /*itemView.setOnClickListener(view -> {
                int position = getAdapterPosition();
                final TermEntity current = terms.get(position);
                // Intent intent = new Intent(context, TermAddEditActivity.class);
                Intent intent = new Intent(context, TermDetailsActivity.class);
                intent.putExtra(TermAddEditActivity.EXTRA_TERM_ID, current.getTermID());
                intent.putExtra(TermAddEditActivity.EXTRA_TERM_TITLE, current.getTermTitle());
                intent.putExtra(TermAddEditActivity.EXTRA_TERM_START_DATE, current.getTermStartDate().getTime());
                intent.putExtra(TermAddEditActivity.EXTRA_TERM_END_DATE, current.getTermEndDate().getTime());
                context.startActivity(intent);
            });*/

        }

    }

    private final TermViewModel termViewModel;
    private final LayoutInflater inflater;
    private final Context context;
    private List<TermEntity> terms;

    public TermAdapter(Context context, TermViewModel termViewModel) {
        this.termViewModel = termViewModel;
        inflater = LayoutInflater.from(context);
        this.context = context;
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
            final TermEntity current = terms.get(position);
            // Intent intent = new Intent(context, TermAddEditActivity.class);
            Intent intent = new Intent(context, TermDetailsActivity.class);
            intent.putExtra(TermAddEditActivity.EXTRA_TERM_ID, current.getTermID());
            intent.putExtra(TermAddEditActivity.EXTRA_TERM_TITLE, current.getTermTitle());
            intent.putExtra(TermAddEditActivity.EXTRA_TERM_START_DATE, current.getTermStartDate().getTime());
            intent.putExtra(TermAddEditActivity.EXTRA_TERM_END_DATE, current.getTermEndDate().getTime());
            context.startActivity(intent);

        });

        holder.itemView.setOnLongClickListener(view -> {

            //termViewModel.delete(terms.get(position));
            String[] options = {"Delete Term", "Cancel"};

            new AlertDialog.Builder(context)
                    .setTitle("Choose an option")
                    .setItems(options, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if (which == 0) {
                                termViewModel.delete(terms.get(position));
                            }
                        }
                    }).show();
            return true;
        });

    }

    public void setTerms(List<TermEntity> termList) {
        terms = termList;
        notifyDataSetChanged();
    }

    public void removeTerm(TermViewModel termViewModel) {

    }


    @Override
    public int getItemCount() {
        if (terms != null)
            return terms.size();
        else return 0;
    }



}
