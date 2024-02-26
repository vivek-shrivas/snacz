package com.example.snacz;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.auth.AuthResult;


import java.util.concurrent.TimeUnit;

public class OtpVerificationActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private String verificationId;
    private String phoneNumber;
    private EditText otpEditText;
    private Button verifyButton;
    private TextView resendOtp;
    private TextView countdownTimerText;

    private String userName;
    private static final long COUNTDOWN_INTERVAL = 1000; // 1 second
    private static final long COUNTDOWN_TIME = 60000; // 60 seconds
    private CountDownTimer countDownTimer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.otp_verification);

        mAuth = FirebaseAuth.getInstance();

        otpEditText = findViewById(R.id.otpEditText);
        verifyButton = findViewById(R.id.verifyButton);
        resendOtp = findViewById(R.id.resendOtp);
        countdownTimerText = findViewById(R.id.countDownTimer);

        phoneNumber = getIntent().getStringExtra("phoneNumber");
        userName = getIntent().getStringExtra("userName");
        verificationId = getIntent().getStringExtra("verificationId");
        startCountdownTimer();
        verifyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String otp = otpEditText.getText().toString().trim();
                if (otp.length() == 6) {
                    PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, otp);
                    signInWithPhoneAuthCredential(credential);
                } else {
                    String message = "Enter Valid OTP";
                    Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                }
            }
        });

        resendOtp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isValidPhoneNumber(phoneNumber)) {
                    if (!phoneNumber.startsWith("+91")) {
                        phoneNumber = "+91" + phoneNumber;
                    }
                    // Resend OTP by calling the verification method again
                    startPhoneNumberVerification(phoneNumber);
                    Toast.makeText(getApplicationContext(), "OTP Resent", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(), "Invalid phone number", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)

                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Authentication successful, proceed with your app logic
                            // For example, get the UID of the authenticated user
                            String uid = mAuth.getCurrentUser().getUid();

                            SharedPreferencesManager.getInstance(getApplicationContext()).saveAuthToken(uid);

                            SharedPreferencesManager.getInstance(getApplicationContext()).savePhoneNumber(phoneNumber);

                            SharedPreferencesManager.getInstance(getApplicationContext()).saveUserName(userName);
                            // Navigate to the main activity
                            startActivity(new Intent(OtpVerificationActivity.this, MainActivity.class));
                            finish();
                        } else {
                            // Authentication failed
                            // Handle the failure, e.g., display an error message
                            Toast.makeText(getApplicationContext(), "Authentication failed", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void startPhoneNumberVerification(String phoneNumber) {
        if (!phoneNumber.startsWith("+91")) {
            phoneNumber = "+91" + phoneNumber;
        }

        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(FirebaseAuth.getInstance())
                        .setPhoneNumber(phoneNumber)
                        .setTimeout(60L, TimeUnit.SECONDS)
                        .setActivity(this)
                        .setCallbacks(new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                            @Override
                            public void onCodeSent(@NonNull String verificationId,
                                                   @NonNull PhoneAuthProvider.ForceResendingToken token) {
                                // The SMS verification code has been sent to the provided phone number
                                // Save the verification ID and resend token, if needed
                                // You can pass this verificationId to the OTP verification activity
                                // for later use when the user enters the OTP.
                                Toast.makeText(OtpVerificationActivity.this, "OTP Sent", Toast.LENGTH_SHORT).show();

                                // Disable the resend button and start the countdown
                                setResendButtonEnabled(false);
                                startCountdownTimer();
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
                                Toast.makeText(OtpVerificationActivity.this, "Failed to verify phone number", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .build();

        PhoneAuthProvider.verifyPhoneNumber(options);
    }

    private void setResendButtonEnabled(boolean enabled) {
        resendOtp.setEnabled(enabled);
        resendOtp.setVisibility(enabled ? View.VISIBLE : View.GONE);
        countdownTimerText.setVisibility(enabled ? View.GONE : View.VISIBLE);
    }

    public void startCountdownTimer() {
        countDownTimer = new CountDownTimer(COUNTDOWN_TIME, COUNTDOWN_INTERVAL) {
            @Override
            public void onTick(long millisUntilFinished) {
                long secondsRemaining = TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) % 60;
                countdownTimerText.setText(getString(R.string.resend_code_in, secondsRemaining));
            }

            @Override
            public void onFinish() {
                // Enable the resend button when the countdown is finished
                setResendButtonEnabled(true);
                countdownTimerText.setText("");  // Clear the text here
            }
        };

        countDownTimer.start();  // Start the countdown timer
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Cancel the countdown timer to avoid leaks
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
    }

    private boolean isValidPhoneNumber(String phoneNumber) {
        // Implement your phone number validation logic
        // For example, you can check if it's not empty and has a valid format
        return phoneNumber != null && !phoneNumber.isEmpty() && phoneNumber.trim().length() == 13;
    }
}
