package tuahade.label.app;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.view.Gravity;
import android.widget.FrameLayout;
import android.widget.TextView;

import org.telegram.messenger.AndroidUtilities;
import org.telegram.ui.ActionBar.Theme;

/**
 * Custom view untuk menampilkan label FAKE/SCAM di profile
 */
public class FakeScamLabelView extends FrameLayout {
    private TextView labelTextView;
    private String labelType;

    public FakeScamLabelView(Context context, String label) {
        super(context);
        this.labelType = label;
        init();
    }

    private void init() {
        labelTextView = new TextView(getContext());
        labelTextView.setText(labelType);
        labelTextView.setTextColor(Color.WHITE);
        labelTextView.setTextSize(12);
        labelTextView.setTypeface(android.graphics.Typeface.DEFAULT_BOLD);
        labelTextView.setGravity(Gravity.CENTER);
        labelTextView.setPadding(
                AndroidUtilities.dp(8),
                AndroidUtilities.dp(4),
                AndroidUtilities.dp(8),
                AndroidUtilities.dp(4)
        );

        // Set background berdasarkan label type
        GradientDrawable drawable = new GradientDrawable();
        drawable.setCornerRadius(AndroidUtilities.dp(4));
        
        if (labelType.equals(FakeScamDatabase.LABEL_FAKE)) {
            // Yellow untuk FAKE
            drawable.setColor(Color.parseColor("#FFB90F"));
        } else if (labelType.equals(FakeScamDatabase.LABEL_SCAM)) {
            // Red untuk SCAM
            drawable.setColor(Color.parseColor("#FF4444"));
        }
        
        labelTextView.setBackground(drawable);
        addView(labelTextView, new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.WRAP_CONTENT,
                FrameLayout.LayoutParams.WRAP_CONTENT,
                Gravity.CENTER
        ));
    }

    public String getLabelType() {
        return labelType;
    }
}
