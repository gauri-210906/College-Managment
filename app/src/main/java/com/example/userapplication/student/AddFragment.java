package com.example.userapplication.student;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;

import com.example.userapplication.R;
import com.example.userapplication.common.Urls;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;


public class AddFragment extends Fragment {

    EditText etDate, etTitle, etName,etYear,etDescription;

    AppCompatButton acbtnSubmit;

    ProgressDialog progressDialog;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    @SuppressLint({"MissingInflatedId",})
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_add, container, false);

        etDate = view.findViewById(R.id.etStudentAddFragmentDate);
        etTitle = view.findViewById(R.id.etStudentAddFragmentTitle);
        etDescription = view.findViewById(R.id.etStudentAddFragmentDescription);
        etName = view.findViewById(R.id.etStudentAddFragmentName);
        etYear = view.findViewById(R.id.etStudentAddFragmentYear);
        acbtnSubmit = view.findViewById(R.id.acbtnStudentAddFragmentSubmit);

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        editor = sharedPreferences.edit();


        acbtnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (etName.getText().toString().isEmpty()) {
                    etName.setError("Please enter your name");
                } else if (etDate.getText().toString().isEmpty()) {
                    etDate.setError("Please enter date");
                }  else if (etTitle.getText().toString().isEmpty()) {
                    etTitle.setError("Please enter title");
                }  else if (etYear.getText().toString().isEmpty()) {
                    etYear.setError("Please enter your year");
                }
                else {

                    progressDialog = new ProgressDialog(getActivity());
                    progressDialog.setTitle("Please wait...");
                    progressDialog.setMessage("Submitting your response");
                    progressDialog.setCanceledOnTouchOutside(false);
                    progressDialog.show();

                    submitResponse();

                }
            }
        });




        return view;
    }

    private void submitResponse() {

        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();

        params.put("date",etDate.getText().toString());
        params.put("title",etTitle.getText().toString());
        params.put("name",etName.getText().toString());
        params.put("year",etYear.getText().toString());
        params.put("description",etDescription.getText().toString());


        client.post(Urls.addStudentResponseWebService,params, new JsonHttpResponseHandler(){

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);

                progressDialog.dismiss();

                try {
                    String status = response.getString("success");
                    if (status.equals("1")){


                        AlertDialog.Builder ad = new AlertDialog.Builder(getActivity());

                        LayoutInflater inflater = getLayoutInflater();
                        View dialogView = inflater.inflate(R.layout.alert_dialogbox, null);

                        ad.setView(dialogView);

                        ad.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        }).create().show();




                    }

                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                progressDialog.dismiss();
                Toast.makeText(getActivity(), "Server Error", Toast.LENGTH_SHORT).show();
            }

        });




    }

    private void showAlertDialogBox() {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        // Inflate custom layout
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.alert_dialogbox, null);

        // Set the custom layout in the dialog
        builder.setView(dialogView);


       /* // Optionally set title or message
        builder.setTitle("Custom Dialog")
                .setMessage("This is a custom dialog with an image.");

        // Find the image view from the layout if you need to change the image dynamically
        ImageView imageView = dialogView.findViewById(R.id.imageView);
        imageView.setImageResource(R.drawable.your_image); */



        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        // Create and show the alert dialog
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}