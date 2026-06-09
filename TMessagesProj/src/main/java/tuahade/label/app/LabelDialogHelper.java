package tuahade.label.app;

import android.app.AlertDialog;
import android.content.Context;

import org.telegram.messenger.MessagesController;
import org.telegram.tgnet.TLRPC;
import org.telegram.ui.ActionBar.BaseFragment;

/**
 * Helper untuk integrate label system ke ProfileActivity
 * Menampilkan dialog untuk label user/channel
 */
public class LabelDialogHelper {
    private Context context;
    private long userId;
    private int currentAccount;
    private TelegramLabelDatabase database;
    private Runnable onLabelChanged;

    public LabelDialogHelper(Context context, long userId, int currentAccount, Runnable onLabelChanged) {
        this.context = context;
        this.userId = userId;
        this.currentAccount = currentAccount;
        this.onLabelChanged = onLabelChanged;
        this.database = TelegramLabelDatabase.getInstance(context);
    }

    /**
     * Show dialog untuk label user/channel
     */
    public void showLabelDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(context, AlertDialog.THEME_DEVICE_DEFAULT_DARK);
        builder.setTitle("Mark User/Channel");

        String[] options = {
                "🚩 Mark as SCAM",
                "🏷️ Mark as FAKE",
                "✓ Remove Label",
                "Cancel"
        };

        builder.setItems(options, (dialog, which) -> {
            switch (which) {
                case 0:
                    markAsScam();
                    break;
                case 1:
                    markAsFake();
                    break;
                case 2:
                    removeLabel();
                    break;
            }
        });

        builder.show();
    }

    /**
     * Mark sebagai SCAM
     */
    private void markAsScam() {
        database.setAsScam(userId);
        applyLabelToUI();
        showToast("Marked as SCAM");
    }

    /**
     * Mark sebagai FAKE
     */
    private void markAsFake() {
        database.setAsFake(userId);
        applyLabelToUI();
        showToast("Marked as FAKE");
    }

    /**
     * Remove label
     */
    private void removeLabel() {
        database.removeLabel(userId);
        applyLabelToUI();
        showToast("Label removed");
    }

    /**
     * Apply label ke UI dengan update TLRPC objects
     */
    private void applyLabelToUI() {
        MessagesController controller = MessagesController.getInstance(currentAccount);

        // Update User object jika ada
        TLRPC.User user = controller.getUser(userId);
        if (user != null) {
            database.applyLabelToUser(user);
        }

        // Update Chat object jika ada (untuk channel/group)
        TLRPC.Chat chat = controller.getChat(-userId);
        if (chat != null) {
            database.applyLabelToChat(chat);
        }

        // Refresh UI
        if (onLabelChanged != null) {
            onLabelChanged.run();
        }
    }

    /**
     * Show toast
     */
    private void showToast(String message) {
        android.widget.Toast.makeText(context, message, android.widget.Toast.LENGTH_SHORT).show();
    }
}
