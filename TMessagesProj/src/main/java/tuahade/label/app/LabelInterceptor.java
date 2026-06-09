package tuahade.label.app;

import org.telegram.messenger.MessagesController;
import org.telegram.tgnet.TLRPC;

/**
 * Interceptor untuk apply label saat User/Chat di-fetch dari server
 * Ini memastikan label selalu ditampilkan meskipun setelah refresh
 */
public class LabelInterceptor {
    private static TelegramLabelDatabase database;

    public static void init(android.content.Context context) {
        database = TelegramLabelDatabase.getInstance(context);
    }

    /**
     * Intercept dan apply label ke user object
     */
    public static void interceptUser(TLRPC.User user) {
        if (database != null && user != null) {
            database.applyLabelToUser(user);
        }
    }

    /**
     * Intercept dan apply label ke chat object
     */
    public static void interceptChat(TLRPC.Chat chat) {
        if (database != null && chat != null) {
            database.applyLabelToChat(chat);
        }
    }

    /**
     * Intercept dan apply label ke userFull object (untuk profile)
     */
    public static void interceptUserFull(TLRPC.UserFull userFull) {
        if (database != null && userFull != null) {
            database.applyLabelToUserFull(userFull);
        }
    }

    /**
     * Intercept dan apply label ke chatFull object
     */
    public static void interceptChatFull(TLRPC.ChatFull chatFull) {
        if (database != null && chatFull != null) {
            database.applyLabelToChatFull(chatFull);
        }
    }
}
