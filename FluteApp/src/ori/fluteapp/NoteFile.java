package ori.fluteapp;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.io.Serializable;

import android.util.Log;

public class NoteFile implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 2690556641213008181L;
	int bpm;
	//числитель размера
	int size1;
	//знаменатель размера
	int size2;
	//число нот в затакте
	int zatact;
	public int[][] notes;
	public static int maxSize = 100;
	public int length = 0;

	public NoteFile() {
		notes = new int[maxSize][3];
		bpm = 120;
		size1 = 4;
		size2 = 4;
	}
	
	public NoteFile(String path) {
		File file = new File(path);
		try {
			Scanner scanner = new Scanner(file);
			if (scanner.hasNextInt())
				bpm = scanner.nextInt();
			else {
				//add exception
			}
			if (scanner.hasNextInt())
				size1 = scanner.nextInt();
			else {
				//add exception
			}
			if (scanner.hasNextInt())
				size2 = scanner.nextInt();
			else {
				//add exception
			}
			if (scanner.hasNextInt())
				zatact = scanner.nextInt();
			else {
				//add exception
			}
			
			notes = new int[maxSize][3];
			int i = 0, j = 0;
			
			for (i = 0; i < maxSize; i++) {
				for (j = 0; j < 3; j++) {
					if (scanner.hasNextInt()) {
						notes[i][j] = scanner.nextInt();
					}
					else {
						break;
					}
				}
				if (!scanner.hasNextInt())
					break;
			}
			
			length = i + 1;
			
			Log.d("File", "Length: " + length);
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
	}

}