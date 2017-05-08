package com.davidmadethis.quicksend.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.davidmadethis.quicksend.R;
import com.davidmadethis.quicksend.models.Template;

import java.util.List;


/**
 * Created by root on 5/8/17.
 */

public class TemplateAdapter extends RecyclerView.Adapter<TemplateAdapter.ViewHolder> {

    private Context mContext;
    private List<Template> mTemplates;
    private RecyclerView mRecyclerView;

    public TemplateAdapter(Context context, List<Template> templates, RecyclerView recyclerView) {
        this.mContext = context;
        this.mTemplates = templates;
        this.mRecyclerView = recyclerView;

    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.content_templates, parent, false);
        return new TemplateAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(TemplateAdapter.ViewHolder holder, int position) {
        holder.subjectTextView.setText(mTemplates.get(position).getSubject());
        holder.textView.setText(mTemplates.get(position).getMessage());
    }

    @Override
    public int getItemCount() {
        return mTemplates.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView subjectTextView;
        private TextView textView;

        public ViewHolder(View itemView) {
            super(itemView);
            subjectTextView = (TextView) itemView.findViewById(R.id.subject);
            textView = (TextView) itemView.findViewById(R.id.text);

        }


    }

}
