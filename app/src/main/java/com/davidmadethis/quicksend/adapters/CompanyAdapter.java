package com.davidmadethis.quicksend.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;

import com.davidmadethis.quicksend.R;
import com.davidmadethis.quicksend.models.Company;

import java.util.List;

/**
 * Created by root on 1/14/17.
 */

public class CompanyAdapter extends RecyclerView.Adapter<CompanyAdapter.ViewHolder> {

    List<Company> companies;

    public CompanyAdapter(List<Company> companies) {
        this.companies = companies;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.content_mail_recycler, parent, false);
        return new CompanyAdapter.ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.inputEmail.setText(companies.get(position).getEmailAddress());
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
            checkBox = (CheckBox) itemView.findViewById(R.id.checkbox);
            editButton = (ImageButton) itemView.findViewById(R.id.edit);
        }
    }
}