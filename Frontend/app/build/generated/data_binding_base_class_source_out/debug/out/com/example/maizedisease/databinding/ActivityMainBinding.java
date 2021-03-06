// Generated by view binder compiler. Do not edit!
package com.example.maizedisease.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewbinding.ViewBinding;
import androidx.viewbinding.ViewBindings;
import com.camerakit.CameraKitView;
import com.example.maizedisease.R;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;

public final class ActivityMainBinding implements ViewBinding {
  @NonNull
  private final LinearLayout rootView;

  @NonNull
  public final Button btnMap;

  @NonNull
  public final Button btnPastData2;

  @NonNull
  public final CameraKitView camera;

  @NonNull
  public final ImageButton imageButton;

  @NonNull
  public final ImageButton imageView;

  @NonNull
  public final Button recordVideoBtn;

  private ActivityMainBinding(@NonNull LinearLayout rootView, @NonNull Button btnMap,
      @NonNull Button btnPastData2, @NonNull CameraKitView camera, @NonNull ImageButton imageButton,
      @NonNull ImageButton imageView, @NonNull Button recordVideoBtn) {
    this.rootView = rootView;
    this.btnMap = btnMap;
    this.btnPastData2 = btnPastData2;
    this.camera = camera;
    this.imageButton = imageButton;
    this.imageView = imageView;
    this.recordVideoBtn = recordVideoBtn;
  }

  @Override
  @NonNull
  public LinearLayout getRoot() {
    return rootView;
  }

  @NonNull
  public static ActivityMainBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static ActivityMainBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.activity_main, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static ActivityMainBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.btnMap;
      Button btnMap = ViewBindings.findChildViewById(rootView, id);
      if (btnMap == null) {
        break missingId;
      }

      id = R.id.btnPastData2;
      Button btnPastData2 = ViewBindings.findChildViewById(rootView, id);
      if (btnPastData2 == null) {
        break missingId;
      }

      id = R.id.camera;
      CameraKitView camera = ViewBindings.findChildViewById(rootView, id);
      if (camera == null) {
        break missingId;
      }

      id = R.id.imageButton;
      ImageButton imageButton = ViewBindings.findChildViewById(rootView, id);
      if (imageButton == null) {
        break missingId;
      }

      id = R.id.imageView;
      ImageButton imageView = ViewBindings.findChildViewById(rootView, id);
      if (imageView == null) {
        break missingId;
      }

      id = R.id.recordVideoBtn;
      Button recordVideoBtn = ViewBindings.findChildViewById(rootView, id);
      if (recordVideoBtn == null) {
        break missingId;
      }

      return new ActivityMainBinding((LinearLayout) rootView, btnMap, btnPastData2, camera,
          imageButton, imageView, recordVideoBtn);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}
