package com.davidmadethis.quicksend.adapters;

import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;

import com.davidmadethis.quicksend.R;
import com.davidmadethis.quicksend.models.Company;
import com.davidmadethis.quicksend.util.CompanyStorage;

import java.util.List;

/**
 * Created by root on 1/14/17.
 */

public class CompanyAdapter extends RecyclerView.Adapter<CompanyAdapter.ViewHolder> {

    private List<Company> companies;
    private RecyclerView recyclerView;

    private CompanyStorage storage;

    public CompanyAdapter(List<Company> companies, RecyclerView recyclerView) {
        this.recyclerView = recyclerView;
        this.companies = companies;
        storage = new CompanyStorage();
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.content_mail_recycler, parent, false);
        return new CompanyAdapter.ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.inputEmail.setText(companies.get(position).getEmailAddress());
        holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                companies.get(position).setShouldSend(b);
                if (b)
                    Snackbar.make(recyclerView, companies.get(position).getCompanyName() + " selected", Snackbar.LENGTH_SHORT)
                            .show();

            }
        });

        holder.editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String removed = companies.get(position).getEmailAddress();
                companies.remove(companies.get(position));
                storage.saveAll(recyclerView.getContext(), companies);
                recyclerView.getAdapter().notifyDataSetChanged();
                Snackbar.make(recyclerView, removed+ " Removed", Snackbar.LENGTH_SHORT)
                        .show();

            }
        });

    }

    @Override
    public int getItemCount() {
        return companies.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {


        EditText inputEmail;
        CheckBox checkBox;
        ImageButton editButton;

        public ViewHolder(View itemView) {
            super(itemView);

            inputEmail = (EditText) itemView.findViewById(R.id.input_email);
            inputEmail.setKeyListener(null);
            checkBox = (CheckBox) itemView.findViewById(R.id.checkbox);
            editButton = (ImageButton) itemView.findViewById(R.id.edit);
        }
    }

}