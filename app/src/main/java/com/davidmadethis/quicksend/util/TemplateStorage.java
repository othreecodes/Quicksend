package com.davidmadethis.quicksend.util;

import android.content.Context;
import android.content.SharedPreferences;

import com.davidmadethis.quicksend.models.Template;
import com.google.gson.Gson;

import java.util.Arrays;
import java.util.List;

/**
 * Created by root on 5/7/17.
 */

public class TemplateStorage {

    private static final String PREFS_NAME = "templates";
    private static final String DATA = "data";


    public void saveAll(Context context, List<Template> templates) {

        SharedPreferences settings;
        SharedPreferences.Editor editor;

        settings = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        editor = settings.edit();

        Gson gson = new Gson();
        String jsontemplates = gson.toJson(templates);

        editor.putString(DATA, jsontemplates);
        editor.commit();
        editor.apply();

    }

    public List<Template> getAll(Context context) {
        SharedPreferences settings;
        List<Template> templates;

        settings = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);

        if (settings.contains(DATA)) {
            String json = settings.getString(DATA, null);
            Gson gson = new Gson();
            Template[] templates1 = gson.fromJson(json, Template[].class);

            templates = Arrays.asList(templates1);
        } else {
            return null;
        }

        return templates;
    }


}
