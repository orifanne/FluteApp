package ori.fluteapp;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.net.URISyntaxException;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;
import android.view.View;

public class FileUtils {

	public FileUtils() {
		// TODO Auto-generated constructor stub
	}

	public static String getPath(Context context, Uri uri) throws URISyntaxException {
	    if ("content".equalsIgnoreCase(uri.getScheme())) {
	        String[] projection = { "_data" };
	        Cursor cursor = null;

	        try {
	            cursor = context.getContentResolver().query(uri, projection, null, null, null);
	            int column_index = cursor.getColumnIndexOrThrow("_data");
	            if (cursor.moveToFirst()) {
	                return cursor.getString(column_index);
	            }
	        } catch (Exception e) {
	            // Eat it
	        }
	    }
	    else if ("file".equalsIgnoreCase(uri.getScheme())) {
	        return uri.getPath();
	    }

	    return null;
	} 
	
	public static void save(NoteFile file, String path, EditorActivity activity)
	{
		if ((path == "") || (file == null))
			return;
		try {
			PrintWriter out = new PrintWriter(path);
			out.println(file.bpm);
			out.print(file.size1);
			out.print(' ');
			out.println(file.size2);
			out.println(file.zatact);
			int i, j;
			for (i = 0; i < file.length; i++) {
				for (j = 0; j < 3; j++) {
					out.print(file.notes[i][j]);
					out.print(' ');
				}
				out.println();
			}
			out.flush();
			out.close();
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		if (activity.close_open == 1)
			activity.finish();
	}
}