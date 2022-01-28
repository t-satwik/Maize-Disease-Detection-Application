// Generated by view binder compiler. Do not edit!
package com.example.maizedisease.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewbinding.ViewBinding;
import androidx.viewbinding.ViewBindings;
import com.example.maizedisease.R;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;

public final class ActivityLoginSignUpBinding implements ViewBinding {
  @NonNull
  private final LinearLayout rootView;

  @NonNull
  public final EditText confirmPasswordSignUpET;

  @NonNull
  public final EditText emailSignUPET;

  @NonNull
  public final Button loginBtn;

  @NonNull
  public final EditText passwordLoginET;

  @NonNull
  public final EditText passwordSignUpET;

  @NonNull
  public final Button signUpBtn;

  @NonNull
  public final EditText userNameLoginET;

  @NonNull
  public final EditText userNameSignUpET;

  private ActivityLoginSignUpBinding(@NonNull LinearLayout rootView,
      @NonNull EditText confirmPasswordSignUpET, @NonNull EditText emailSignUPET,
      @NonNull Button loginBtn, @NonNull EditText passwordLoginET,
      @NonNull EditText passwordSignUpET, @NonNull Button signUpBtn,
      @NonNull EditText userNameLoginET, @NonNull EditText userNameSignUpET) {
    this.rootView = rootView;
    this.confirmPasswordSignUpET = confirmPasswordSignUpET;
    this.emailSignUPET = emailSignUPET;
    this.loginBtn = loginBtn;
    this.passwordLoginET = passwordLoginET;
    this.passwordSignUpET = passwordSignUpET;
    this.signUpBtn = signUpBtn;
    this.userNameLoginET = userNameLoginET;
    this.userNameSignUpET = userNameSignUpET;
  }

  @Override
  @NonNull
  public LinearLayout getRoot() {
    return rootView;
  }

  @NonNull
  public static ActivityLoginSignUpBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static ActivityLoginSignUpBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.activity_login_sign_up, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static ActivityLoginSignUpBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.confirmPasswordSignUpET;
      EditText confirmPasswordSignUpET = ViewBindings.findChildViewById(rootView, id);
      if (confirmPasswordSignUpET == null) {
        break missingId;
      }

      id = R.id.emailSignUPET;
      EditText emailSignUPET = ViewBindings.findChildViewById(rootView, id);
      if (emailSignUPET == null) {
        break missingId;
      }

      id = R.id.loginBtn;
      Button loginBtn = ViewBindings.findChildViewById(rootView, id);
      if (loginBtn == null) {
        break missingId;
      }

      id = R.id.passwordLoginET;
      EditText passwordLoginET = ViewBindings.findChildViewById(rootView, id);
      if (passwordLoginET == null) {
        break missingId;
      }

      id = R.id.passwordSignUpET;
      EditText passwordSignUpET = ViewBindings.findChildViewById(rootView, id);
      if (passwordSignUpET == null) {
        break missingId;
      }

      id = R.id.signUpBtn;
      Button signUpBtn = ViewBindings.findChildViewById(rootView, id);
      if (signUpBtn == null) {
        break missingId;
      }

      id = R.id.userNameLoginET;
      EditText userNameLoginET = ViewBindings.findChildViewById(rootView, id);
      if (userNameLoginET == null) {
        break missingId;
      }

      id = R.id.userNameSignUpET;
      EditText userNameSignUpET = ViewBindings.findChildViewById(rootView, id);
      if (userNameSignUpET == null) {
        break missingId;
      }

      return new ActivityLoginSignUpBinding((LinearLayout) rootView, confirmPasswordSignUpET,
          emailSignUPET, loginBtn, passwordLoginET, passwordSignUpET, signUpBtn, userNameLoginET,
          userNameSignUpET);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}
