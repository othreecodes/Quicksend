package com.davidmadethis.quicksend.fragments;

import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.view.ActionMode;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.Theme;
import com.cocosw.bottomsheet.BottomSheet;
import com.davidmadethis.quicksend.R;
import com.davidmadethis.quicksend.adapters.TemplateAdapter;
import com.davidmadethis.quicksend.models.Template;
import com.davidmadethis.quicksend.util.DividerItemDecorator;
import com.davidmadethis.quicksend.util.RecyclerTouchListener;
import com.davidmadethis.quicksend.util.TemplateStorage;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link TemplatesFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link TemplatesFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TemplatesFragment extends Fragment {

    private OnFragmentInteractionListener mListener;

    public TemplatesFragment() {
        // Required empty public constructor
    }

    public static TemplatesFragment newInstance() {
        TemplatesFragment fragment = new TemplatesFragment();

        return fragment;
    }

    private RecyclerView recyclerView;
    private FloatingActionButton floatingActionButton;
    private TemplateAdapter templateAdapter;
    private List<Template> templates;
    private TemplateStorage storage;
    private ActionMode mode;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_templates, container, false);
        recyclerView = (RecyclerView) v.findViewById(R.id.templates_recycler_view);
        floatingActionButton = (FloatingActionButton) v.findViewById(R.id.fab);

        Snackbar snackbar = Snackbar.make(recyclerView, "You can use {{company}} to represent the company name," +
                " {{name}} to represent your name and {{job}} to represent the position you seek" +
                ". NB: You must have filled this in already in Settings", Snackbar.LENGTH_INDEFINITE)
                .setAction("GOT IT", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                    }
                });


        TextView textView = (TextView) snackbar.getView().findViewById(android.support.design.R.id.snackbar_text);
        textView.setMaxLines(5);
        snackbar.show();
        storage = new TemplateStorage();


        templates = new ArrayList<Template>();
        if (storage.getAll(getContext()) != null) {
            templates.addAll(storage.getAll(getContext()));
        }

        templateAdapter = new TemplateAdapter(getContext(), templates, recyclerView);


        recyclerView.setAdapter(templateAdapter);
        templateAdapter.notifyDataSetChanged();

        recyclerView.setHasFixedSize(false);
        final RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new DividerItemDecorator(getContext(), DividerItemDecorator.VERTICAL_LIST));
        floatingActionButton.setOnClickListener(onClickListener);
        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getContext(), recyclerView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, final int position) {
                new BottomSheet.Builder(getActivity()).title(templates.get(position).getSubject()).sheet(R.menu.template_menu).listener(new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case R.id.delete:
                                //q.toast("Help me!");
                                templates.remove(position);
                                storage.saveAll(getContext(),templates);
                                templateAdapter.notifyDataSetChanged();
                                break;
                            case R.id.edit:
                                break;
                        }
                    }
                }).show();

            }

            @Override
            public void onLongClick(View view, int position) {

            }

        }));
        return v;
    }


    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            LayoutInflater inflater = (LayoutInflater) getActivity().getBaseContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            final View v = inflater.inflate(R.layout.add_template, null);

            new MaterialDialog.Builder(getActivity())
                    .title("Add Mail Template")
                    .customView(v, true)
                    .positiveText(R.string.add)
                    .theme(Theme.LIGHT)
                    .negativeText(R.string.cancel)
                    .onPositive(new MaterialDialog.SingleButtonCallback() {
                        @Override
                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                            EditText subjectEditText = (EditText) v.findViewById(R.id.input_subject);
                            EditText bodyEditText = (EditText) v.findViewById(R.id.input_text);

                            subjectEditText.clearFocus();
                            bodyEditText.clearFocus();

                            Template template = new Template();
                            template.setSubject(subjectEditText.getText().toString());
                            template.setMessage(bodyEditText.getText().toString());

                            templates.add(template);
                            templateAdapter.notifyDataSetChanged();

                            storage.saveAll(getContext(), templates);
                        }
                    }).show();

        }
    };

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
