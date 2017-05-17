package com.davidmadethis.quicksend.fragments;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.RingtoneManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.NotificationCompat;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.Theme;
import com.davidmadethis.quicksend.MainActivity;
import com.davidmadethis.quicksend.R;
import com.davidmadethis.quicksend.adapters.CompanyAdapter;
import com.davidmadethis.quicksend.models.Company;
import com.davidmadethis.quicksend.models.Template;
import com.davidmadethis.quicksend.util.CompanyStorage;
import com.davidmadethis.quicksend.util.QPreferences;
import com.davidmadethis.quicksend.util.TemplateStorage;
import com.stephentuso.welcome.WelcomeHelper;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.AuthenticationFailedException;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link HomeFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {

    private static String dataEmail;
    SharedPreferences preferences;
    private OnFragmentInteractionListener mListener;
    private RecyclerView mailRecyclerView;
    private CompanyAdapter companyAdapter;
    private List<Company> companies;
    private FloatingActionButton fab;
    private CompanyStorage storage;
    private AppCompatButton sendButton;
    private List<String> emailsToSend;
    private String username;
    private String password;
    private View.OnClickListener fabListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            LayoutInflater inflater = (LayoutInflater) getActivity().getBaseContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            final View v = inflater.inflate(R.layout.add_company, null);

            EditText email = (EditText) v.findViewById(R.id.input_email);
            email.setText(dataEmail);
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
                            dataEmail = null;
                            storage.saveAll(getContext(), companies);
                        }
                    }).show();

        }
    };

    public HomeFragment() {
        // Required empty public constructor
    }

    public static HomeFragment newInstance(String email) {
        HomeFragment fragment = new HomeFragment();
        dataEmail = email;
        return fragment;
    }

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
        sendButton = (AppCompatButton) v.findViewById(R.id.mail_button);
        companies = new ArrayList<>();
        preferences = PreferenceManager.getDefaultSharedPreferences(this.getContext());

        if (preferences.getBoolean("tips", true)) {
            Snackbar snackbar = Snackbar.make(mailRecyclerView, "Visit Settings to update your personal details and App preferences", Snackbar.LENGTH_INDEFINITE)
                    .setAction("GOT IT", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                        }
                    });
            final TextView textView = (TextView) snackbar.getView().findViewById(android.support.design.R.id.snackbar_text);
            textView.setMaxLines(10);
            snackbar.show();
        }
        emailsToSend = new ArrayList<>();
        storage = new CompanyStorage();

        if (storage.getAll(getContext()) != null) {
            companies.addAll(storage.getAll(getContext()));
        }

        companyAdapter = new CompanyAdapter(companies, mailRecyclerView);
        mailRecyclerView.setAdapter(companyAdapter);
        companyAdapter.notifyDataSetChanged();

        mailRecyclerView.setHasFixedSize(false);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        mailRecyclerView.setLayoutManager(layoutManager);


        fab.setOnClickListener(fabListener);

        if(dataEmail!=null){
            fab.performClick();
        }
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                emailsToSend.clear();

                for (Company com : companyAdapter.getCompaniesToSend()) {
                    if (com.isShouldSend()) {
                        emailsToSend.add(com.getEmailAddress());
                    }
                }


                Log.e("ddd", String.valueOf(emailsToSend.size()));

                if (emailsToSend.isEmpty()) {
                    Toast.makeText(getContext(), "You did not select any email", Toast.LENGTH_LONG)
                            .show();
                    return;
                }

                TemplateStorage storage = new TemplateStorage();
                final List<Template> templates = storage.getAll(getContext());
                if (templates==null) {
                    Toast.makeText(getContext(), "No templates available." +
                            "Add one in the templates tab", Toast.LENGTH_LONG)
                            .show();
                    return;
                } else if(templates.size() < 1){
                    Toast.makeText(getContext(), "No templates available." +
                            "Add one in the templates tab", Toast.LENGTH_LONG)
                            .show();
                    return;
                }


                new MaterialDialog.Builder(getContext())
                        .title("Choose Template")
                        .items(templates)
                        .itemsCallbackSingleChoice(-1, new MaterialDialog.ListCallbackSingleChoice() {
                            @Override
                            public boolean onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {


                                Authenticate(templates.get(which), companyAdapter.getCompaniesToSend());
                                return true;
                            }
                        })
                        .positiveText("SEND")
                        .show();


            }
        });
        return v;
    }

    private MaterialDialog Authenticate(final Template template, final List<Company> companies) {

        LayoutInflater inflater = (LayoutInflater) getActivity().getBaseContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View v = inflater.inflate(R.layout.content_login, null);

        EditText nameEditText = (EditText) v.findViewById(R.id.input_email);
        nameEditText.setText(preferences.getString("email", ""));

        return new MaterialDialog.Builder(getActivity())
                .title("Authentication Required")
                .customView(v, true)
                .positiveText("Authenticate")
                .theme(Theme.DARK)
                .negativeText(R.string.cancel)
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        EditText nameEditText = (EditText) v.findViewById(R.id.input_email);
                        EditText email = (EditText) v.findViewById(R.id.input_password);

                        nameEditText.clearFocus();
                        email.clearFocus();


                        username = nameEditText.getText().toString();
                        password = email.getText().toString();
                        preferences.edit().putString("email", username).apply();

                        for (Company company : companies) {
                            new SendEmail(template, username, password, company).execute();
                        }


                        Toast.makeText(getContext(), "Emails will be sent in the background, " +
                                "Check Statusbar for progress", Toast.LENGTH_LONG)
                                .show();
                    }
                }).show();


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

    public boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            return true;
        }
        return false;
    }




    private Properties getGmailSettings() {

        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

        return props;
    }

    private Properties getOutlookSettings() {

        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp-mail.outlook.com");
        props.put("mail.smtp.port", "587");
        return props;
    }


    private Properties getYahooSettings() {


        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.mail.yahoo.com");
        props.put("mail.smtp.port", "587");

        return props;
    }

    String filterTemplate(String template, Company company) {

        return template.replace("{{company}}", company.getCompanyName())
                .replace("{{name}}", preferences.getString("name", ""))
                .replace("{{job}}", preferences.getString("position", ""))
                .replace("\n", "<br>");


    }


    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    class SendEmail extends AsyncTask<String, Void, String> {

        Template template;
        String email;
        String password;
        Company company;
        NotificationCompat.Builder mBuilder;
        int notificationId;
        NotificationManager mNotifyManager;
        private Exception exception;

        public SendEmail(Template template, String email, String password, Company company) {
            this.template = template;
            this.email = email;
            this.password = password;
            this.company = company;

        }

        public NotificationCompat.Builder showNotification() {

            Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            notificationId = (int) new Date().getTime();
            mNotifyManager = (NotificationManager) getActivity().getSystemService(Context.NOTIFICATION_SERVICE);
            mBuilder = new NotificationCompat.Builder(getActivity());
            mBuilder.setContentTitle("Sending mail to " + this.company.getCompanyName())
                    .setContentText("Sending...")
                    .setSound(defaultSoundUri)
                    .setSmallIcon(R.drawable.ic_stat_name);

            mBuilder.setProgress(100, 100, true);
            // Displays the progress bar for the first time.
            mNotifyManager.notify(notificationId, mBuilder.build());

            return mBuilder;
        }

        protected String doInBackground(String... urls) {

            final String email = this.email;
            final String password = this.password;
            Properties properties = null;

            if (email.toLowerCase().endsWith("gmail.com")) {
                properties = getGmailSettings();
            } else if (email.toLowerCase().endsWith("outlook.com")) {
                properties = getOutlookSettings();
            } else if (email.toLowerCase().endsWith("yahoo.com")) {
                properties = getYahooSettings();
            } else {
                return "nosupport";
            }


            Session session = Session.getInstance(properties,
                    new javax.mail.Authenticator() {
                        protected PasswordAuthentication getPasswordAuthentication() {
                            return new PasswordAuthentication(email, password);
                        }
                    });


            try {
                NotificationCompat.Builder builder = showNotification();
                Message message = new MimeMessage(session);
                message.setFrom(new InternetAddress(email));

                message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(company.getEmailAddress()));
                message.setSubject(filterTemplate(template.getSubject(), company));


                BodyPart messageBody = new MimeBodyPart();


                String sendvia = "";
                if (preferences.getBoolean("sendvia", true)) {
                    sendvia = "<a href=\"http://www.github.com/othreecodes/quicksend\">Sent Via QuickSend</a>";
                }
                messageBody.setContent(filterTemplate(template.getMessage() + "\n\n" + sendvia, company), "text/html");


                Multipart multipart = new MimeMultipart();
                multipart.addBodyPart(messageBody);

                if (preferences.getBoolean("cv", true)) {
                    File cv;
                    try {
                        QPreferences preferences = new QPreferences(getContext());
                        cv = new File(preferences.getCV_LOCATION());
                        MimeBodyPart FileBodyPart = new MimeBodyPart();
                        FileBodyPart = new MimeBodyPart();
                        String file = cv.getAbsolutePath();
                        String fileName = cv.getName();
                        DataSource source = new FileDataSource(preferences.getCV_LOCATION());

                        FileBodyPart.setDataHandler(new DataHandler(source));
                        FileBodyPart.setFileName(fileName);

                        multipart.addBodyPart(FileBodyPart);
                    } catch (Exception e) {
                        return "";
                    }

                }
                message.setContent(multipart);
                Transport.send(message);


                Intent intent = new Intent(getActivity(), MainActivity.class);

                PendingIntent pIntent = PendingIntent.getActivity(getActivity(), (int) System.currentTimeMillis(), intent, 0);

                Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

                NotificationCompat.Builder n = builder
                        .setContentTitle("Your mail to " + company.getCompanyName() + " was successfully sent")
                        .setContentText("Mail Successfully sent")
                        .setSmallIcon(R.drawable.ic_stat_name)
                        .setContentIntent(pIntent)
                        .setProgress(0, 0, false)
                        .setSound(defaultSoundUri)
                        .setAutoCancel(true);


                mNotifyManager.notify(notificationId, n.build());
            } catch (AuthenticationFailedException e) {


                Intent intent = new Intent(getActivity(), MainActivity.class);


                PendingIntent pIntent = PendingIntent.getActivity(getActivity(), (int) System.currentTimeMillis(), intent, 0);

                Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                Notification n = mBuilder
                        .setContentTitle("Your mail to " + company.getCompanyName() + " was not sent")
                        .setContentText("Authentication Failed: Email or password Incorrect")
                        .setSmallIcon(R.drawable.ic_stat_name)
                        .setContentIntent(pIntent)
                        .setSound(defaultSoundUri)
                        .addAction(R.drawable.ic_send_black_24dp, "Report", pIntent)
                        .setProgress(0, 0, false)
                        .setAutoCancel(false)
                        .build();


                mNotifyManager.notify(notificationId, n);

            } catch (MessagingException e) {

                Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);

                String aEmailList[] = {"contact@davidmadethis.com"};

                emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, aEmailList);
                emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Error From Quicksend");

                emailIntent.setType("plain/text");
                emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, e.toString());

                startActivity(emailIntent);


                PendingIntent pIntent = PendingIntent.getActivity(getActivity(), (int) System.currentTimeMillis(), emailIntent, 0);

                Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                Notification n = mBuilder
                        .setContentTitle("Your mail to " + company.getCompanyName() + " was not sent")
                        .setContentText(e.getMessage())
                        .setSmallIcon(R.drawable.ic_stat_name)
                        .setContentIntent(pIntent)
                        .setSound(defaultSoundUri)
                        .addAction(R.drawable.ic_send_black_24dp, "Report", pIntent)
                        .setProgress(0, 0, false)
                        .setAutoCancel(false)
                        .build();


                mNotifyManager.notify(notificationId, n);
            }

            return "";

        }

        protected void onPostExecute(String data) {

            if (data.equals("nosupport")) {
                Toast.makeText(getContext(), "We Currently do not support your email provider ", Toast.LENGTH_LONG)
                        .show();
            }
        }
    }


}
