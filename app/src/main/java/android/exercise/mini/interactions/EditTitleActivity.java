package android.exercise.mini.interactions;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import static java.lang.Math.max;
import static java.lang.Math.min;

public class EditTitleActivity extends AppCompatActivity {
    private boolean isEditing = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_title);

        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

        // find all views
        FloatingActionButton fabStartEdit = findViewById(R.id.fab_start_edit);
        FloatingActionButton fabEditDone = findViewById(R.id.fab_edit_done);
        TextView textViewTitle = findViewById(R.id.textViewPageTitle);
        EditText editTextTitle = findViewById(R.id.editTextPageTitle);

        // setup - start from static title with "edit" button
        fabStartEdit.setVisibility(View.VISIBLE);
        fabEditDone.setVisibility(View.GONE);
        fabStartEdit.setAlpha(1f);
        fabEditDone.setAlpha(0f);

        textViewTitle.setText("Page title here");
        textViewTitle.setVisibility(View.VISIBLE);
        editTextTitle.setText("Page title here");
        editTextTitle.setVisibility(View.GONE);

        // handle clicks on "start edit"
        fabStartEdit.setOnClickListener(v -> {
            isEditing = true;

            animateOut(fabStartEdit);
            animateIn(fabEditDone);

            editTextTitle.setText(textViewTitle.getText());
            textViewTitle.setVisibility(View.GONE);
            editTextTitle.setVisibility(View.VISIBLE);
            editTextTitle.requestFocus();
            editTextTitle.setSelection(editTextTitle.getText().length());
            // open keyboard
            imm.showSoftInput(editTextTitle, InputMethodManager.SHOW_IMPLICIT);
        });

        // handle clicks on "done edit"
        fabEditDone.setOnClickListener(v -> {
            stopEditing(true);
        });
    }


    /***
     * BACK button was clicked
     */
    @Override
    public void onBackPressed() {
        if (isEditing) {
            stopEditing(false);
        } else {
            super.onBackPressed();
        }
    }

    /***
     * switch to the static textView, close keyboard and animate the FABs.
     * @param saveChanges true iff need to save changes made to the editText.
     */
    private void stopEditing(boolean saveChanges) {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        FloatingActionButton fabStartEdit = findViewById(R.id.fab_start_edit);
        FloatingActionButton fabEditDone = findViewById(R.id.fab_edit_done);
        TextView textViewTitle = findViewById(R.id.textViewPageTitle);
        EditText editTextTitle = findViewById(R.id.editTextPageTitle);

        isEditing = false;
        if (saveChanges) {
            textViewTitle.setText(editTextTitle.getText());
        } else {
            editTextTitle.setText(textViewTitle.getText());
        }

        animateIn(fabStartEdit);
        animateOut(fabEditDone);

        // make sure keyboard is closed
        editTextTitle.requestFocus();
        imm.hideSoftInputFromWindow(textViewTitle.getWindowToken(), 0);

        editTextTitle.setVisibility(View.GONE);
        textViewTitle.setVisibility(View.VISIBLE);
    }

    private void animateOut(View view) {
        float slideRightLength = getResources().getDimension(R.dimen.EditFabSlideRight);

        view.animate()
                .withStartAction(() -> view.setClickable(false))
                .alpha(0f)
                .translationXBy(slideRightLength)
                .setDuration(1000L)
                .withEndAction(() -> {
                    view.setVisibility(View.GONE);
                    view.setTranslationX(view.getTranslationX() - slideRightLength);
                    view.setClickable(true);
                })
                .start();
    }

    private void animateIn(View view) {
        view.animate()
                .withStartAction(() -> {
                    view.setClickable(false);
                    view.setVisibility(View.VISIBLE);
                })
                .alpha(1f)
                .setDuration(1000L)
                .withEndAction(() -> {
                    view.setClickable(true);
                })
                .start();
    }

}