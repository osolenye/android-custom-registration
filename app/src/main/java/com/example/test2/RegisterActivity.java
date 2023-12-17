package com.example.test2;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ProgressBar;
//import android.widget.RadioButton;
//import android.widget.RadioGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;

public class RegisterActivity extends AppCompatActivity {
    private EditText editTextRegisterFullName, editTextRegisterEmail, editTextRegisterMobile, editTextRegisterDoB,
            editTextRegisterPwd, editTextRegisterConfirmPwd;
    private ProgressBar progressBar;
    private RadioGroup radioGroupRegisterGender;
    private RadioButton radioButtonRegisterGenderSelected;
    private DatePickerDialog picker;
    private static final String TAG= "RegisterActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

//        getSupportActionBar().setTitle("Register");

        Toast.makeText(RegisterActivity.this, "You cen register now", Toast.LENGTH_SHORT).show();

        /**View - занимает прямоугольную область на экране  отвечает за рисование и обработку.
         * View - базовый класс виджетов, которые используются для создания интерактивных компонентов UI(кнопок, текстовых полей)
         * findViewById - это метод, который находит по присвоенному ему id. Также возвращает экземпляр View, который затем преобразуется в целевой класс*/

        progressBar = findViewById(R.id.progressBar);
        editTextRegisterFullName = findViewById(R.id.edittext_register_fullName);
        editTextRegisterEmail = findViewById(R.id.edittext_register_email);
        editTextRegisterMobile = findViewById(R.id.edittext_register_mobile);
        editTextRegisterDoB = findViewById(R.id.edittext_register_dob);
        editTextRegisterPwd = findViewById(R.id.edittext_register_password);
        editTextRegisterConfirmPwd = findViewById(R.id.edittext_register_confirm_password);

        //clearCheck() - для очистки всех отмеченных переключателей при запуске или возобновлении активности
        //RadioButton for gender
        radioGroupRegisterGender = findViewById(R.id.radio_group_register_gender);
        radioGroupRegisterGender.clearCheck();

        //Setting up DatePicker on EditText
        editTextRegisterDoB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar calendar = Calendar.getInstance();
                int day = calendar.get(Calendar.DAY_OF_MONTH);
                int month = calendar.get(Calendar.MONTH);
                int year = calendar.get(Calendar.YEAR);

                //Date picker dialog
                picker = new DatePickerDialog(RegisterActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        editTextRegisterDoB.setText(dayOfMonth + "/" + month + "/" + year);
                    }
                }, year, month, day);
                picker.show();
            }
        });

        /**Создали кнопку зарегаться, создали экземпляр OnClickListener* и переопределили метод OnClick
         *Назначили OnClickListener нашей кнопке с помощью  в наших фрагментах/действиях метода onCreate.
         *Когда юзер нажимает кнопку, вызывается функция onClick назначенного OnClickListener.*/

        Button buttonRegister = findViewById(R.id.button_register);
        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                int selectedGenderId = radioGroupRegisterGender.getCheckedRadioButtonId();
//                radioGroupRegisterGender = findViewById(selectedGenderId);
                int selectedGenderId = radioGroupRegisterGender.getCheckedRadioButtonId();
                radioButtonRegisterGenderSelected = findViewById(selectedGenderId);


                //Obtain the entered data
                String textFullName = editTextRegisterFullName.getText().toString();
                String textEmail = editTextRegisterEmail.getText().toString();
                String textMobile = editTextRegisterMobile.getText().toString();
                String textDoB = editTextRegisterDoB.getText().toString();
                String textPwd = editTextRegisterPwd.getText().toString();
                String textConfirmPwd = editTextRegisterConfirmPwd.getText().toString();
                String textGender; //Невозможно получить значение, прежде чем проверять, выбрана ли какая-либо кнопка или нет.

                if (TextUtils.isEmpty(textFullName)) {
                    Toast.makeText(RegisterActivity.this, "Please enter your full name", Toast.LENGTH_LONG).show();
                    editTextRegisterFullName.setError("Full Name is required");
                    editTextRegisterFullName.requestFocus();
                }else if (TextUtils.isEmpty(textEmail)) {
                    Toast.makeText(RegisterActivity.this, "Please enter your email", Toast.LENGTH_LONG).show();
                    editTextRegisterEmail.setError("Email is required");
                    editTextRegisterEmail.requestFocus();
                }else if (!Patterns.EMAIL_ADDRESS.matcher(textEmail).matches()) {
                    Toast.makeText(RegisterActivity.this, "Please re-enter your email", Toast.LENGTH_LONG).show();
                    editTextRegisterEmail.setError("Valid email is required");
                    editTextRegisterEmail.requestFocus();
                }else if (TextUtils.isEmpty(textDoB)) {
                    Toast.makeText(RegisterActivity.this, "Please enter your date of birth", Toast.LENGTH_LONG).show();
                    editTextRegisterEmail.setError("Date of Birth is required");
                    editTextRegisterEmail.requestFocus();
                }else if (radioGroupRegisterGender.getCheckedRadioButtonId() == -1) {
                    Toast.makeText(RegisterActivity.this, "Please select your gender", Toast.LENGTH_LONG).show();
                    editTextRegisterEmail.setError("Gender is required");
                    editTextRegisterEmail.requestFocus();
                }else if (TextUtils.isEmpty(textMobile)) {
                    Toast.makeText(RegisterActivity.this, "Please enter your mobile no.", Toast.LENGTH_LONG).show();
                    editTextRegisterEmail.setError("Mobile No. is required");
                    editTextRegisterEmail.requestFocus();
                }else if (textMobile.length() != 10) {
                    Toast.makeText(RegisterActivity.this, "Please re-enter your mobile no.", Toast.LENGTH_LONG).show();
                    editTextRegisterEmail.setError("Mobile no. should be 10 digits");
                    editTextRegisterEmail.requestFocus();
                }else if (TextUtils.isEmpty(textPwd)) {
                    Toast.makeText(RegisterActivity.this, "Please enter your password", Toast.LENGTH_LONG).show();
                    editTextRegisterEmail.setError("Password is required");
                    editTextRegisterEmail.requestFocus();
                }else if (textPwd.length() < 6) {
                    Toast.makeText(RegisterActivity.this, "Password should be at least 6 digits", Toast.LENGTH_LONG).show();
                    editTextRegisterEmail.setError("Password is weak");
                    editTextRegisterEmail.requestFocus();
                }else if (TextUtils.isEmpty(textConfirmPwd)) {
                    Toast.makeText(RegisterActivity.this, "Please confirm your password", Toast.LENGTH_LONG).show();
                    editTextRegisterEmail.setError("Password Confirmation is required");
                    editTextRegisterEmail.requestFocus();
                    //Clear the entered passwords
                    editTextRegisterPwd.clearComposingText();
                    editTextRegisterConfirmPwd.clearComposingText();
                }else {
                    textGender = radioButtonRegisterGenderSelected.getText().toString();
                    progressBar.setVisibility(View.VISIBLE);
                    registerUser(textFullName, textEmail, textGender, textDoB, textConfirmPwd, textMobile, textPwd);

                }

            }
        });


    }
    //Register user using the credentials given
    private void registerUser(String textFullName, String textEmail, String textGender, String textDoB, String textConfirmPwd, String textMobile, String textPwd) {
        FirebaseAuth auth = FirebaseAuth.getInstance();

        //Create user profile
        auth.createUserWithEmailAndPassword(textEmail, textPwd).addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){

                    FirebaseUser firebaseUser = auth.getCurrentUser();

                    //Update display name of user
                    UserProfileChangeRequest profileChangeRequest = new UserProfileChangeRequest.Builder().setDisplayName(textFullName).build();
                    firebaseUser.updateProfile(profileChangeRequest);

                    //Enter User Data into the Firebase Realtime Database
                    ReadWriteUserDetails writeUserDetails = new ReadWriteUserDetails(textDoB, textMobile, textGender, firebaseUser.getEmail(), textFullName);

                    //Extracting User reference from Database for "Registered Users"
                    DatabaseReference referenceProfile = FirebaseDatabase.getInstance().getReference("Registered users");

                    referenceProfile.child(firebaseUser.getUid()).setValue(writeUserDetails).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                //Send verification Email
                                firebaseUser.sendEmailVerification();

                                Toast.makeText(RegisterActivity.this, "User registered successfully. Please verify your email",
                                        Toast.LENGTH_LONG).show();
                                //Open user profile after successful registration
                                /*FLAG_ACTIVITY_CLEAR_TOP чаще всего используется вместе с FLAG_ACTIVITY_NEW_TASK.
                                При совместном использовании эти флаги определяют местонахождение существующего действия в другой задаче
                                и помещают его в положение, в котором оно может реагировать на намерение.*/
//                                Intent intent = new Intent(RegisterActivity.this, UserProfileActivity.class);
//                                //To Prevent User from returning back to Register Activity on pressing back Button after registration
//                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
//                                startActivity(intent);
//                                finish(); // to close register activity

                            }else{
                                Toast.makeText(RegisterActivity.this, "User registered failed. Please try again",
                                        Toast.LENGTH_LONG).show();
                            }
                            progressBar.setVisibility(View.GONE);

                        }
                    });




                } else {
                    try {
                        throw task.getException();
                    }catch (FirebaseAuthWeakPasswordException e){
                        editTextRegisterPwd.setError("Your password is too weak. ");
                        editTextRegisterPwd.requestFocus();
                    }catch(FirebaseAuthInvalidCredentialsException e){
                        editTextRegisterPwd.setError("Your email is invalid or already in use. ");
                        editTextRegisterPwd.requestFocus();
                    }catch(FirebaseAuthUserCollisionException e){
                        editTextRegisterPwd.setError("User is already registered whit this email. Use another email.");
                        editTextRegisterPwd.requestFocus();
                    }catch(Exception e){
                        Log.e(TAG, e.getMessage());
                        Toast.makeText(RegisterActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();

                    }
                    progressBar.setVisibility(View.GONE);

                }
            }
        });
    }
}