package tuahade.label.app;

import android.app.AlertDialog;
import android.content.Context;
import android.view.Menu;
import android.view.MenuItem;

import org.telegram.messenger.AndroidUtilities;
import org.telegram.ui.ActionBar.BaseFragment;
import org.telegram.ui.ProfileActivity;

/**
 * Helper class untuk mengintegrasikan label system ke ProfileActivity
 */
public class ProfileLabelHelper {
    private Context context;
    private long dialogId;
    private BaseFragment fragment;
    private FakeScamDatabase database;

    public ProfileLabelHelper(Context context, long dialogId, BaseFragment fragment) {
        this.context = context;
        this.dialogId = dialogId;
        this.fragment = fragment;
        this.database = FakeScamDatabase.getInstance(context);
    }

    /**
     * Tambah menu item untuk label ke ActionBar
     */
    public void addLabelMenuItems(Menu menu) {
        MenuItem labelItem = menu.add(Menu.NONE, R.id.label_menu_item, 0, "Label");
        labelItem.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        labelItem.setIcon(R.drawable.ic_flag_24);
    }

    /**
     * Handle menu item click
     */
    public boolean handleMenuItemClick(MenuItem item) {
        if (item.getItemId() == R.id.label_menu_item) {
            showLabelDialog();
            return true;
        }
        return false;
    }

    /**
     * Show dialog untuk memilih label
     */
    private void showLabelDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(context, AlertDialog.THEME_DEVICE_DEFAULT_DARK);
        builder.setTitle("Label User/Channel");

        String[] options = {"🟨 Mark as FAKE", "🔴 Mark as SCAM", "❌ Remove Label", "Cancel"};
        builder.setItems(options, (dialog, which) -> {
            if (which == 0) {
                setLabel(FakeScamDatabase.LABEL_FAKE);
            } else if (which == 1) {
                setLabel(FakeScamDatabase.LABEL_SCAM);
            } else if (which == 2) {
                removeLabel();
            }
            // which == 3 adalah Cancel
        });

        builder.show();
    }

    /**
     * Set label untuk user/channel
     */
    private void setLabel(String label) {
        database.setLabel(dialogId, label);
        showToast(label + " label telah ditambahkan");
    }

    /**
     * Remove label
     */
    private void removeLabel() {
        database.removeLabel(dialogId);
        showToast("Label telah dihapus");
    }

    /**
     * Show toast message
     */
    private void showToast(String message) {
        android.widget.Toast.makeText(context, message, android.widget.Toast.LENGTH_SHORT).show();
    }

    /**
     * Get current label untuk user/channel
     */
    public String getCurrentLabel() {
        return database.getLabel(dialogId);
    }

    /**
     * Check apakah user/channel memiliki label
     */
    public boolean hasLabel() {
        return database.hasLabel(dialogId);
    }
}
