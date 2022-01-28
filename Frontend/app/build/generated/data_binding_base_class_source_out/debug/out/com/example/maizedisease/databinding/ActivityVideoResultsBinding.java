// Generated by view binder compiler. Do not edit!
package com.example.maizedisease.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewbinding.ViewBinding;
import androidx.viewbinding.ViewBindings;
import com.example.maizedisease.R;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;

public final class ActivityVideoResultsBinding implements ViewBinding {
  @NonNull
  private final RelativeLayout rootView;

  @NonNull
  public final Button btnMainActivity;

  @NonNull
  public final RecyclerView idFramePredCard;

  private ActivityVideoResultsBinding(@NonNull RelativeLayout rootView,
      @NonNull Button btnMainActivity, @NonNull RecyclerView idFramePredCard) {
    this.rootView = rootView;
    this.btnMainActivity = btnMainActivity;
    this.idFramePredCard = idFramePredCard;
  }

  @Override
  @NonNull
  public RelativeLayout getRoot() {
    return rootView;
  }

  @NonNull
  public static ActivityVideoResultsBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static ActivityVideoResultsBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.activity_video_results, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static ActivityVideoResultsBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.btnMainActivity;
      Button btnMainActivity = ViewBindings.findChildViewById(rootView, id);
      if (btnMainActivity == null) {
        break missingId;
      }

      id = R.id.idFramePredCard;
      RecyclerView idFramePredCard = ViewBindings.findChildViewById(rootView, id);
      if (idFramePredCard == null) {
        break missingId;
      }

      return new ActivityVideoResultsBinding((RelativeLayout) rootView, btnMainActivity,
          idFramePredCard);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}