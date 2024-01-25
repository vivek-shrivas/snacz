package com.example.snacz;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;


public class OtpVerificationActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private String verificationId;

    private EditText otpEditText;
    private Button verifyButton;
    private TextView resendOtp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.otp_verification);

        mAuth = FirebaseAuth.getInstance();

        otpEditText = findViewById(R.id.otpEditText);
        verifyButton = findViewById(R.id.verifyButton);
        resendOtp = findViewById(R.id.resendOtp);

        verificationId = getIntent().getStringExtra("verificationId");

        verifyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String otp = otpEditText.getText().toString().trim();
                if (!otp.isEmpty()) {
                    PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, otp);
                    signInWithPhoneAuthCredential(credential);
                }
            }
        });

        resendOtp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Implement code to resend OTP
                // You may want to start the phone number verification process again
            }
        });
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        if (task.isSuccessful()) {
                            // Authentication successful, proceed with your app logic
                            // For example, navigate to the main activity
                            startActivity(new Intent(OtpVerificationActivity.this, MainActivity.class));
                            finish();
                        } else {
                            // Authentication failed
                            // Handle the failure, e.g., display an error message
                        }
                    }
                });
    }
}

