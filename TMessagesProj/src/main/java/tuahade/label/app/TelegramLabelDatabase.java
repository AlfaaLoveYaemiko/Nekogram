package tuahade.label.app;

import android.content.Context;
import android.content.SharedPreferences;

import org.telegram.tgnet.TLRPC;

/**
 * Database untuk menyimpan label FAKE/SCAM lokal
 * Dengan memanipulasi field is_scam/is_fake di TLRPC.User dan TLRPC.Chat
 * Sehingga tampilan badge akan SAMA PERSIS seperti Telegram official
 */
public class TelegramLabelDatabase {
    private static final String PREF_NAME = "telegram_label_db";
    private SharedPreferences prefs;
    private static TelegramLabelDatabase instance;

    public TelegramLabelDatabase(Context context) {
        prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }

    public static TelegramLabelDatabase getInstance(Context context) {
        if (instance == null) {
            instance = new TelegramLabelDatabase(context);
        }
        return instance;
    }

    /**
     * Set user/channel sebagai SCAM (menggunakan field is_scam asli Telegram)
     * @param userId ID user atau -channelId
     */
    public void setAsScam(long userId) {
        prefs.edit().putBoolean("scam_" + userId, true).apply();
    }

    /**
     * Set user/channel sebagai FAKE (menggunakan field is_fake asli Telegram)
     * @param userId ID user atau -channelId
     */
    public void setAsFake(long userId) {
        prefs.edit().putBoolean("fake_" + userId, true).apply();
    }

    /**
     * Check apakah user/channel adalah SCAM
     */
    public boolean isScam(long userId) {
        return prefs.getBoolean("scam_" + userId, false);
    }

    /**
     * Check apakah user/channel adalah FAKE
     */
    public boolean isFake(long userId) {
        return prefs.getBoolean("fake_" + userId, false);
    }

    /**
     * Remove label
     */
    public void removeLabel(long userId) {
        prefs.edit()
                .remove("scam_" + userId)
                .remove("fake_" + userId)
                .apply();
    }

    /**
     * Apply label ke TLRPC.User object
     * Ini akan inject field is_scam/is_fake sehingga UI akan menampilkan badge Telegram official
     */
    public void applyLabelToUser(TLRPC.User user) {
        if (user == null) return;
        if (isScam(user.id)) {
            user.is_scam = true;
        }
        if (isFake(user.id)) {
            user.is_fake = true;
        }
    }

    /**
     * Apply label ke TLRPC.Chat object
     */
    public void applyLabelToChat(TLRPC.Chat chat) {
        if (chat == null) return;
        long chatId = chat.id;
        if (chat.megagroup || chat.gigagroup) {
            chatId = -chatId; // Convert to negative for supergroups
        }
        if (isScam(chatId)) {
            chat.is_scam = true;
        }
        if (isFake(chatId)) {
            chat.is_fake = true;
        }
    }

    /**
     * Apply label ke TLRPC.UserFull object
     */
    public void applyLabelToUserFull(TLRPC.UserFull userFull) {
        if (userFull == null || userFull.user == null) return;
        applyLabelToUser(userFull.user);
    }

    /**
     * Apply label ke TLRPC.ChatFull object
     */
    public void applyLabelToChatFull(TLRPC.ChatFull chatFull) {
        if (chatFull == null || chatFull.chat == null) return;
        applyLabelToChat(chatFull.chat);
    }
}
