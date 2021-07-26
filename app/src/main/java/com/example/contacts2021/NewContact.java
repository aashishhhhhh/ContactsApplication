package com.example.contacts2021;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.backendless.Backendless;
import com.backendless.BackendlessUser;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;

public class NewContact extends AppCompatActivity {

    private View mProgressView;
    private View mLoginFormView;
    private TextView tvLoad,EmailNamee;

    EditText et_Name,etNumber,et_Email;
    Button btn_NewContact;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String value = getIntent().getStringExtra("Toast");
        Toast.makeText(NewContact.this,value,Toast.LENGTH_SHORT).show();
        setContentView(R.layout.activity_new_contact);
        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);
        tvLoad = findViewById(R.id.tvLoad);
       // EmailNamee=findViewById(R.id.EmailName);


        et_Name=(EditText) findViewById(R.id.et_Name);
        etNumber=(EditText) findViewById(R.id.etNumber);
        et_Email=(EditText) findViewById(R.id.et_Email);
        btn_NewContact=(Button) findViewById(R.id.btn_NewContact);

        btn_NewContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(et_Name.getText().toString().isEmpty()||et_Email.getText().toString().isEmpty()||et_Email.getText().toString().isEmpty()){
                    Toast.makeText(NewContact.this,"Please enter all the fields",Toast.LENGTH_SHORT).show();

                }

                else {
                    String name = et_Name.getText().toString().trim();
                    String email = et_Email.getText().toString().trim();
                    String number = etNumber.getText().toString().trim();

                    Contact contact = new Contact();
                    contact.setName(name);
                    contact.setEmail(email);
                    contact.setNumber(number);
                    contact.setUserEmail(ApplicationClass.user.getEmail());

                    showProgress(true);
                    tvLoad.setText("Creating new contact......please wait");

                    Backendless.Persistence.save(contact, new AsyncCallback<Contact>() {
                        @Override
                        public void handleResponse(Contact response) {
                            Toast.makeText(NewContact.this,"New Contact Saved Successfully",Toast.LENGTH_SHORT).show();
                            et_Name.setText("");
                            et_Email.setText("");
                            etNumber.setText("");
                            showProgress(false);
                        }

                        @Override
                        public void handleFault(BackendlessFault fault) {
                           Toast.makeText(NewContact.this,"Error:"+fault.getMessage(),Toast.LENGTH_SHORT).show();
                           showProgress(false);
                        }
                    });
                }

            }
        });
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });

            tvLoad.setVisibility(show ? View.VISIBLE : View.GONE);
            tvLoad.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    tvLoad.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            tvLoad.setVisibility(show ? View.VISIBLE : View.GONE);
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }


}