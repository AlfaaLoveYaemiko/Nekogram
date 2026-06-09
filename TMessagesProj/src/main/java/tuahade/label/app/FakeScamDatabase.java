package tuahade.label.app;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Local database untuk menyimpan label FAKE/SCAM yang hanya terlihat lokal (visual only)
 * Tidak ada data yang dikirim ke server
 */
public class FakeScamDatabase {
    private static final String PREF_NAME = "fake_scam_labels_db";
    private SharedPreferences prefs;
    private static FakeScamDatabase instance;

    public static final String LABEL_FAKE = "FAKE";
    public static final String LABEL_SCAM = "SCAM";

    public FakeScamDatabase(Context context) {
        prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }

    public static FakeScamDatabase getInstance(Context context) {
        if (instance == null) {
            instance = new FakeScamDatabase(context);
        }
        return instance;
    }

    /**
     * Simpan label untuk user/channel tertentu
     * @param userId ID user atau channel negatif untuk channel
     * @param label LABEL_FAKE atau LABEL_SCAM, atau null untuk hapus
     */
    public void setLabel(long userId, String label) {
        String key = "user_" + userId;
        if (label == null) {
            prefs.edit().remove(key).apply();
        } else {
            prefs.edit().putString(key, label).apply();
        }
    }

    /**
     * Ambil label untuk user/channel
     * @param userId ID user atau channel negatif untuk channel
     * @return Label (FAKE/SCAM) atau null jika tidak ada
     */
    public String getLabel(long userId) {
        return prefs.getString("user_" + userId, null);
    }

    /**
     * Check apakah user/channel memiliki label
     */
    public boolean hasLabel(long userId) {
        return prefs.contains("user_" + userId);
    }

    /**
     * Hapus label untuk user/channel
     */
    public void removeLabel(long userId) {
        setLabel(userId, null);
    }
}
