package ru.onyanov.geni;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;

import java.io.File;
import java.util.ArrayList;

public class Utils {

    protected static final String FIELD_EMAIL_POSITION = "email_position";

    protected static void sendFileToEmail(Context context, File file, String email) {
        Intent emailIntent = new Intent(Intent.ACTION_SEND_MULTIPLE);
        emailIntent.setType("text/plain");

        String subject = context.getString(R.string.email_subject, file.getName());
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, subject);

        // address
        String to[] = {email};
        emailIntent.putExtra(Intent.EXTRA_EMAIL, to);

        // the attachment
        ArrayList<Uri> uriList = new ArrayList<>();
        uriList.add(Uri.fromFile(file));
        emailIntent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, uriList);

        context.startActivity(Intent.createChooser(emailIntent, "Send email"));
    }

    /**
     * Provide array of ids for using photos
     *
     * @return
     */
    protected static int[] getIdArray() {
        return new int[]{76, 77, 78, 79, 81, 82, 85};
    }

    protected static SharedPreferences getPreferences(Context context) {
        return context.getSharedPreferences(context.getApplicationInfo().packageName, context.MODE_PRIVATE);
    }

    public static int getEmail(Context context) {
        return getPreferences(context).getInt(Utils.FIELD_EMAIL_POSITION, 0);
    }

    public static void saveEmail(int position, Context context) {
        SharedPreferences.Editor editor = getPreferences(context).edit();
        editor.putInt(FIELD_EMAIL_POSITION, position);
        editor.commit();
    }
}
