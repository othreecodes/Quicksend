package com.davidmadethis.quicksend.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.Theme;
import com.davidmadethis.quicksend.R;
import com.davidmadethis.quicksend.adapters.CompanyAdapter;
import com.davidmadethis.quicksend.models.Company;
import com.davidmadethis.quicksend.util.CompanyStorage;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link HomeFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {

    private OnFragmentInteractionListener mListener;

    public HomeFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance() {
        HomeFragment fragment = new HomeFragment();

        return fragment;
    }

    private RecyclerView mailRecyclerView;
    private CompanyAdapter companyAdapter;
    private List<Company> companies;
    private FloatingActionButton fab;
    private CompanyStorage storage;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
//        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.);
        View v = inflater.inflate(R.layout.fragment_home, container, false);
        mailRecyclerView = (RecyclerView) v.findViewById(R.id.mail_recycler_view);
        fab = (FloatingActionButton) v.findViewById(R.id.fab);
        companies = new ArrayList<>();

        storage = new CompanyStorage();

        if (storage.getAll(getContext()) != null) {
            companies.addAll(storage.getAll(getContext()));
        }


        companyAdapter = new CompanyAdapter(companies);
        mailRecyclerView.setAdapter(companyAdapter);
        companyAdapter.notifyDataSetChanged();

        mailRecyclerView.setHasFixedSize(false);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        mailRecyclerView.setLayoutManager(layoutManager);


        fab.setOnClickListener(fabListener);
        return v;
    }


    private View.OnClickListener fabListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            LayoutInflater inflater = (LayoutInflater) getActivity().getBaseContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            final View v = inflater.inflate(R.layout.add_company, null);

            new MaterialDialog.Builder(getActivity())
                    .title("Add Company")
                    .customView(v, true)
                    .positiveText(R.string.add)
                    .theme(Theme.LIGHT)
                    .negativeText(R.string.cancel)
                    .onPositive(new MaterialDialog.SingleButtonCallback() {
                        @Override
                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                            EditText nameEditText = (EditText) v.findViewById(R.id.input_name);
                            EditText email = (EditText) v.findViewById(R.id.input_email);

                            nameEditText.clearFocus();
                            email.clearFocus();

                            Company company = new Company();
                            company.setEmailAddress(email.getText().toString());
                            company.setCompanyName(nameEditText.getText().toString());

                            companies.add(company);
                            companyAdapter.notifyDataSetChanged();

                            storage.saveAll(getContext(),companies);
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

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
