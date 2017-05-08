package com.davidmadethis.quicksend.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.davidmadethis.quicksend.R;
import com.davidmadethis.quicksend.adapters.TemplateAdapter;

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

        return v;
    }

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
