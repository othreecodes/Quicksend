package com.davidmadethis.quicksend.util;

import android.content.Context;
import android.content.SharedPreferences;

import com.davidmadethis.quicksend.models.Company;
import com.google.gson.Gson;

import java.util.Arrays;
import java.util.List;

/**
 * Created by root on 5/7/17.
 */

public class CompanyStorage {

    private static final String PREFS_NAME = "companies";
    private static final String DATA = "data";


    public void saveAll(Context context, List<Company> companies) {

        SharedPreferences settings;
        SharedPreferences.Editor editor;
        settings = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        editor = settings.edit();

        Gson gson = new Gson();
        String jsonCompanies = gson.toJson(companies);

        editor.putString(DATA, jsonCompanies);
        editor.commit();
        editor.apply();

    }

    public List<Company> getAll(Context context) {
        SharedPreferences settings;
        List<Company> companies;

        settings = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);

        if (settings.contains(DATA)) {
            String json = settings.getString(DATA, null);
            Gson gson = new Gson();
            Company[] companies1 = gson.fromJson(json, Company[].class);

            companies = Arrays.asList(companies1);
        } else {
            return null;
        }

        return companies;
    }


}
