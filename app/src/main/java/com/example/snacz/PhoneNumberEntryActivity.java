package com.example.snacz;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthProvider;
import androidx.annotation.NonNull;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.FirebaseException; // Add this import
import java.util.concurrent.TimeUnit;

public class PhoneNumberEntryActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private EditText mobileNumberEditText;
    private Button requestOtpButton;
    private String phoneNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_signup);

        mAuth = FirebaseAuth.getInstance();

        mobileNumberEditText = findViewById(R.id.mobileNumberEditText);
        requestOtpButton = findViewById(R.id.verifyButton);

        requestOtpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                phoneNumber = mobileNumberEditText.getText().toString().trim();
                if (isValidPhoneNumber(phoneNumber)) {
                    if (!phoneNumber.startsWith("+91")) {
                        phoneNumber = "+91" + phoneNumber;
                        startPhoneNumberVerification(phoneNumber);
                    }
                }
                else{
                    String message="Enter valid Phone number";
                    Toast.makeText(getApplicationContext(),message,Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

//check if the phone number is valid or not
    private static boolean isValidPhoneNumber(String phoneNumber) {
        // Null check and then check if the trimmed string contains only digits and has a length of 10
        return phoneNumber != null  && phoneNumber.trim().length() == 10;
    }


    private void startPhoneNumberVerification(String phoneNumber) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneNumber,        // Phone number to verify
                60,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                this,               // Activity (for callback binding)
                new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                    @Override
                    public void onCodeSent(@NonNull String verificationId,
                                           @NonNull PhoneAuthProvider.ForceResendingToken token) {
                        // The SMS verification code has been sent to the provided phone number
                        // Save the verification ID and resend token, if needed
                        // You can pass this verificationId to the OTP verification activity
                        // for later use when the user enters the OTP.
                        Intent intent = new Intent(PhoneNumberEntryActivity.this, OtpVerificationActivity.class);
                        intent.putExtra("verificationId", verificationId);
                        intent.putExtra("phoneNumber",phoneNumber);
                        startActivity(intent);
                    }


                    @Override
                    public void onVerificationCompleted(@NonNull PhoneAuthCredential credential) {
                        // This callback will be invoked in two situations:
                        // 1 - Instant verification. In some cases, the phone number can be instantly verified without needing to send or enter a verification code.
                        // 2 - Auto-retrieval. On some devices, Google Play services can automatically detect the incoming verification SMS and perform verification without user action.
                        // You can proceed to signInWithPhoneAuthCredential here if needed.
                    }

                    @Override
                    public void onVerificationFailed(@NonNull FirebaseException e) {
                        // Failed to verify the phone number
                        // Handle the failure, e.g., display an error message
                    }
                });
    }
}
