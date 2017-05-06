package com.davidmadethis.quicksend.fragments;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.davidmadethis.quicksend.R;

import java.io.File;
import java.net.URISyntaxException;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link CVFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link CVFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CVFragment extends Fragment {
    private final String TAG = this.getClass().getName();
    private OnFragmentInteractionListener mListener;

    public CVFragment() {
        // Required empty public constructor
    }

    public static CVFragment newInstance() {
        CVFragment fragment = new CVFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private ImageView imageView;
    private TextView textView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_cv, container, false);

        Button button = (Button) v.findViewById(R.id.choose_file);
        imageView = (ImageView) v.findViewById(R.id.file_type);
        textView = (TextView) v.findViewById(R.id.file_name);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                chooseFile();
            }
        });
        return v;
    }

    private void chooseFile() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        try {
            startActivityForResult(
                    Intent.createChooser(intent, "Select a File to Upload"),
                    FILE_SELECT_CODE);
        } catch (android.content.ActivityNotFoundException ex) {
            // Potentially direct the user to the Market with a Dialog
            Toast.makeText(getActivity(), "Please install a File Manager.",
                    Toast.LENGTH_SHORT).show();
        }
    }

    private static final int FILE_SELECT_CODE = 100;

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case FILE_SELECT_CODE:
                if (resultCode == RESULT_OK) {
                    // Get the Uri of the selected file
                    Uri uri = data.getData();
                    Log.e(TAG, "File Uri: " + uri.toString());
                    // Get the path
                    String path = null;
                    try {
                        path = getPath(getContext(), uri);
                    } catch (URISyntaxException e) {
                        e.printStackTrace();
                    }

                    setFileLogo(path);

                    Log.e(TAG, "File Path: " + path);

                    // Get the file instance
                    File file = new File(path);
                    textView.setText(file.getName());
                     //Initiate the upload
                }
                break;
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    public static String getPath(Context context, Uri uri) throws URISyntaxException {
        if ("content".equalsIgnoreCase(uri.getScheme())) {
            String[] projection = {"_data"};
            Cursor cursor = null;

            try {
                cursor = context.getContentResolver().query(uri, projection, null, null, null);
                int column_index = cursor.getColumnIndexOrThrow("_data");
                if (cursor.moveToFirst()) {
                    return cursor.getString(column_index);
                }
            } catch (Exception e) {
                // Eat it
            }
        } else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;
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

    public void setFileLogo(String fileLogo) {

        //this.fileLogo = fileLogo;
        String ext = fileLogo.substring(fileLogo.lastIndexOf("."));

        switch (ext.toLowerCase()) {
            case ".png":
                imageView.setImageDrawable(getResources().getDrawable(R.drawable.png));
                break;
            case ".doc":
            case ".docx":
                imageView.setImageDrawable(getResources().getDrawable(R.drawable.doc));
                break;
            case ".pdf":
                imageView.setImageDrawable(getResources().getDrawable(R.drawable.pdf));
                break;
            case "txt":
                imageView.setImageDrawable(getResources().getDrawable(R.drawable.txt));
                break;
             default:
                 imageView.setImageDrawable(getResources().getDrawable(R.drawable.file));
                 break;
        }

    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
