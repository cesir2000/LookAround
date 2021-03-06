// Generated by view binder compiler. Do not edit!
package es.ucm.fdi.lookaround.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.viewbinding.ViewBinding;
import es.ucm.fdi.lookaround.R;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;

public final class CategoryItemBinding implements ViewBinding {
  @NonNull
  private final CardView rootView;

  @NonNull
  public final CardView cv;

  @NonNull
  public final ImageView imageViewSVGContent;

  @NonNull
  public final ProgressBar progressBar4;

  @NonNull
  public final RelativeLayout relativeLayout;

  @NonNull
  public final RelativeLayout relativeLayout2;

  @NonNull
  public final TextView textViewTitleContent;

  private CategoryItemBinding(@NonNull CardView rootView, @NonNull CardView cv,
      @NonNull ImageView imageViewSVGContent, @NonNull ProgressBar progressBar4,
      @NonNull RelativeLayout relativeLayout, @NonNull RelativeLayout relativeLayout2,
      @NonNull TextView textViewTitleContent) {
    this.rootView = rootView;
    this.cv = cv;
    this.imageViewSVGContent = imageViewSVGContent;
    this.progressBar4 = progressBar4;
    this.relativeLayout = relativeLayout;
    this.relativeLayout2 = relativeLayout2;
    this.textViewTitleContent = textViewTitleContent;
  }

  @Override
  @NonNull
  public CardView getRoot() {
    return rootView;
  }

  @NonNull
  public static CategoryItemBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static CategoryItemBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.category_item, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static CategoryItemBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      CardView cv = (CardView) rootView;

      id = R.id.imageViewSVGContent;
      ImageView imageViewSVGContent = rootView.findViewById(id);
      if (imageViewSVGContent == null) {
        break missingId;
      }

      id = R.id.progressBar4;
      ProgressBar progressBar4 = rootView.findViewById(id);
      if (progressBar4 == null) {
        break missingId;
      }

      id = R.id.relativeLayout;
      RelativeLayout relativeLayout = rootView.findViewById(id);
      if (relativeLayout == null) {
        break missingId;
      }

      id = R.id.relativeLayout2;
      RelativeLayout relativeLayout2 = rootView.findViewById(id);
      if (relativeLayout2 == null) {
        break missingId;
      }

      id = R.id.textViewTitleContent;
      TextView textViewTitleContent = rootView.findViewById(id);
      if (textViewTitleContent == null) {
        break missingId;
      }

      return new CategoryItemBinding((CardView) rootView, cv, imageViewSVGContent, progressBar4,
          relativeLayout, relativeLayout2, textViewTitleContent);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}
