package com.example.contacts2021;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.backendless.Backendless;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;

public class ContactInfo extends AppCompatActivity {
    TextView tv_Char,tv_Name;
    ImageView ivCall,ivMail,ivDelete,ivEdit;
    EditText etNAME,etMAIL,etNUMBER;
    Button btnSUBMIT;

    private View mProgressView;
    private View mLoginFormView;
    private TextView tvLoad;
    Boolean edit = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_info);


        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);
        tvLoad = findViewById(R.id.tvLoad);

        tv_Char=(TextView)findViewById(R.id.tv_Char);
        tv_Name=(TextView)findViewById(R.id.tv_Name);
        ivCall=(ImageView)findViewById(R.id.ivCall);
        ivMail=(ImageView)findViewById(R.id.ivMail);
        ivEdit=(ImageView)findViewById(R.id.ivEdit);
        ivDelete=(ImageView)findViewById(R.id.ivDelete);
        etNAME=(EditText)findViewById(R.id.etNAME);
        etMAIL=(EditText)findViewById(R.id.etMAIL);
        etNUMBER=(EditText)findViewById(R.id.etNUMBER);
        btnSUBMIT=(Button)findViewById(R.id.btnSUBMIT);

        etNAME.setVisibility(View.GONE);
        etMAIL.setVisibility(View.GONE);
        etNUMBER.setVisibility(View.GONE);
        btnSUBMIT.setVisibility(View.GONE);

        final int index =getIntent().getIntExtra("index",0);

        etNAME.setText(ApplicationClass.contacts.get(index).getName());
        etMAIL.setText(ApplicationClass.contacts.get(index).getEmail());
        etNUMBER.setText(ApplicationClass.contacts.get(index).getNumber());

        tv_Char.setText(ApplicationClass.contacts.get(index).getName().charAt(0)+"");
        tv_Name.setText(ApplicationClass.contacts.get(index).getName());

        //OnClick Properties

        ivCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String uri = "tel: "+ ApplicationClass.contacts.get(index).getNumber();
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse(uri));
                startActivity(intent);

            }
        });

        ivMail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/html");
                intent.putExtra(Intent.EXTRA_EMAIL,ApplicationClass.contacts.get(index).getEmail());
                startActivity(Intent.createChooser(intent,"Send mail to "
                        +ApplicationClass.contacts.get(index).getName()));
            }
        });

        ivDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final AlertDialog.Builder dialog = new AlertDialog.Builder(ContactInfo.this);
                dialog.setTitle("Do you want to delete this contact?");
                dialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        showProgress(true);
                        tvLoad.setText("Deleting contact...Please Wait.....");

                        Backendless.Persistence.of(Contact.class).remove(ApplicationClass.contacts.get(index), new AsyncCallback<Long>() {
                            @Override
                            public void handleResponse(Long response) {
                                ApplicationClass.contacts.remove(index);
                                showProgress(false);
                                Toast.makeText(ContactInfo.this,"Contact Successfully Deleted",Toast.LENGTH_SHORT).show();
                                setResult(RESULT_OK);
                                ContactInfo.this.finish();
                            }

                            @Override
                            public void handleFault(BackendlessFault fault) {

                            }
                        });
                    }
                });
                dialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

                dialog.show();

            }
        });

        ivEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edit= !edit;
                if (edit){
                    etNAME.setVisibility(View.VISIBLE);
                    etMAIL.setVisibility(View.VISIBLE);
                    etNUMBER.setVisibility(View.VISIBLE);
                    btnSUBMIT.setVisibility(View.VISIBLE);
                }
                else {
                    etNAME.setVisibility(View.GONE);
                    etMAIL.setVisibility(View.GONE);
                    etNUMBER.setVisibility(View.GONE);
                    btnSUBMIT.setVisibility(View.GONE);

                }

            }
        });

        btnSUBMIT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (etNAME.getText().toString().trim().isEmpty()||etMAIL.getText().toString().trim().isEmpty()||etNUMBER.getText().toString().trim().isEmpty()){
                    Toast.makeText(ContactInfo.this,"Please enter all the fields",Toast.LENGTH_SHORT).show();

                }
                else {
                    ApplicationClass.contacts.get(index).setName(etNAME.getText().toString().trim());
                    ApplicationClass.contacts.get(index).setEmail(etMAIL.getText().toString().trim());
                    ApplicationClass.contacts.get(index).setNumber(etNUMBER.getText().toString().trim());

                    Backendless.Persistence.save(ApplicationClass.contacts.get(index), new AsyncCallback<Contact>() {
                        @Override
                        public void handleResponse(Contact response) {

                            tv_Char.setText(ApplicationClass.contacts.get(index).getName().toUpperCase().charAt(0)+"");
                            tv_Name.setText(ApplicationClass.contacts.get(index).getName());
                            Toast.makeText(ContactInfo.this,"Contact Successfully Updated",Toast.LENGTH_SHORT).show();

                        }

                        @Override
                        public void handleFault(BackendlessFault fault) {
                            Toast.makeText(ContactInfo.this,"Error: "+fault.getMessage(),Toast.LENGTH_SHORT).show();
                        }
                    });
                }

            }
        });


    }

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