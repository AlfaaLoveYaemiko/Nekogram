package tuahade.label.app;

import org.telegram.messenger.MessagesController;
import org.telegram.tgnet.TLRPC;
import java.util.ArrayList;

/**
 * Patch untuk MessagesController
 * Apply label ke semua user/chat yang ter-cache
 */
public class LabelCachePatcher {
    private static TelegramLabelDatabase database;

    public static void init(android.content.Context context) {
        database = TelegramLabelDatabase.getInstance(context);
    }

    /**
     * Apply label ke semua cached users
     */
    public static void patchAllUsers(MessagesController controller) {
        if (database == null || controller == null) return;
        
        try {
            // Get all users dari MessagesController
            java.util.HashMap<Integer, TLRPC.User> users = controller.getUsers();
            if (users != null) {
                for (TLRPC.User user : users.values()) {
                    database.applyLabelToUser(user);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Apply label ke semua cached chats
     */
    public static void patchAllChats(MessagesController controller) {
        if (database == null || controller == null) return;
        
        try {
            // Get all chats dari MessagesController
            java.util.HashMap<Integer, TLRPC.Chat> chats = controller.getChats();
            if (chats != null) {
                for (TLRPC.Chat chat : chats.values()) {
                    database.applyLabelToChat(chat);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
