package ori.fluteapp;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class WindTutorialDatabase extends SQLiteOpenHelper {

	// константы для конструктора
	private static final String DATABASE_NAME = "wind_tutorial_database.db";
	private static final int DATABASE_VERSION = 4;

	public WindTutorialDatabase(Context context) {
		// конструктор суперкласса
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}
		
	// константы для таблицы tblInstruments
	public static final String tblInstruments = "tblInstruments";
	public static final String intInstrumentId = "intInstrumentId";
	public static final String txtInstrumentName = "txtInstrumentName";
	public static final String intNumOfHoles = "intNumOfHoles";
		
	public static final String intFingeringC4 = "intFingeringC4";
	public static final String intFingeringC4d = "intFingeringC4d";
	public static final String intFingeringD4 = "intFingeringD4";
	public static final String intFingeringD4d = "intFingeringD4d";
	public static final String intFingeringE4 = "intFingeringE4";
	public static final String intFingeringF4 = "intFingeringF4";
	public static final String intFingeringF4d = "intFingeringF4d";
	public static final String intFingeringG4 = "intFingeringG4";
	public static final String intFingeringG4d = "intFingeringG4d";
	public static final String intFingeringA4 = "intFingeringA4";
	public static final String intFingeringA4d = "intFingeringA4d";
	public static final String intFingeringB4 = "intFingeringB4";
	
	public static final String intFingeringC5 = "intFingeringC5";
	public static final String intFingeringC5d = "intFingeringC5d";
	public static final String intFingeringD5 = "intFingeringD5";
	public static final String intFingeringD5d = "intFingeringD5d";
	public static final String intFingeringE5 = "intFingeringE5";
	public static final String intFingeringF5 = "intFingeringF5";
	public static final String intFingeringF5d = "intFingeringF5d";
	public static final String intFingeringG5 = "intFingeringG5";
	public static final String intFingeringG5d = "intFingeringG5d";
	public static final String intFingeringA5 = "intFingeringA5";
	public static final String intFingeringA5d = "intFingeringA5d";
	public static final String intFingeringB5 = "intFingeringB5";
	
	public static final String intFingeringC6 = "intFingeringC6";
	public static final String intFingeringC6d = "intFingeringC6d";
	public static final String intFingeringD6 = "intFingeringD6";
	public static final String intFingeringD6d = "intFingeringD6d";
	public static final String intFingeringE6 = "intFingeringE6";
	public static final String intFingeringF6 = "intFingeringF6";
	public static final String intFingeringF6d = "intFingeringF6d";
	public static final String intFingeringG6 = "intFingeringG6";
	public static final String intFingeringG6d = "intFingeringG6d";
	public static final String intFingeringA6 = "intFingeringA6";
	public static final String intFingeringA6d = "intFingeringA6d";
	public static final String intFingeringB6 = "intFingeringB6";
	
	public static final String intFingeringC7 = "intFingeringC7";
	public static final String intFingeringC7d = "intFingeringC7d";
	public static final String intFingeringD7 = "intFingeringD7";
	
	private static final String SQL_CREATE_TBL_INSTRUMENTS = "CREATE TABLE " 
			+ tblInstruments + " (" 
			+ intInstrumentId + " INTEGER PRIMARY KEY, " 
			+ txtInstrumentName + " TEXT, "
			+ intNumOfHoles + " INTEGER, "
			+ intFingeringC4 + " TEXT, " // int... = text ...))) redo!
			+ intFingeringC4d + " TEXT, "
			+ intFingeringD4 + " TEXT, "
			+ intFingeringD4d + " TEXT, "
			+ intFingeringE4 + " TEXT, "
			+ intFingeringF4 + " TEXT, "
			+ intFingeringF4d + " TEXT, "
			+ intFingeringG4 + " TEXT, "
			+ intFingeringG4d + " TEXT, "
			+ intFingeringA4 + " TEXT, "
			+ intFingeringA4d + " TEXT, "
			+ intFingeringB4 + " TEXT, "
			
			+ intFingeringC5 + " TEXT, "
			+ intFingeringC5d + " TEXT, "
			+ intFingeringD5 + " TEXT, "
			+ intFingeringD5d + " TEXT, "
			+ intFingeringE5 + " TEXT, "
			+ intFingeringF5 + " TEXT, "
			+ intFingeringF5d + " TEXT, "
			+ intFingeringG5 + " TEXT, "
			+ intFingeringG5d + " TEXT, "
			+ intFingeringA5 + " TEXT, "
			+ intFingeringA5d + " TEXT, "
			+ intFingeringB5 + " TEXT, "
			
			+ intFingeringC6 + " TEXT, "
			+ intFingeringC6d + " TEXT, "
			+ intFingeringD6 + " TEXT, "
			+ intFingeringD6d + " TEXT, "
			+ intFingeringE6 + " TEXT, "
			+ intFingeringF6 + " TEXT, "
			+ intFingeringF6d + " TEXT, "
			+ intFingeringG6 + " TEXT, "
			+ intFingeringG6d + " TEXT, "
			+ intFingeringA6 + " TEXT, "
			+ intFingeringA6d + " TEXT, "
			+ intFingeringB6 + " TEXT, "
			
			+ intFingeringC7 + " TEXT, "
			+ intFingeringC7d + " TEXT, "
			+ intFingeringD7 + " TEXT)";

		// константы для таблицы tblNotes
		public static final String tblNotes = "tblNotes";
		public static final String intNoteId = "intNoteId";
		public static final String fltFreq = "fltFreq";
		public static final String txtNoteCode = "txtNoteCode";
		public static final String fltFreqOfDies = "fltFreqOfDies";
		
		private static final String SQL_CREATE_TBL_NOTES = "CREATE TABLE " 
				+ tblNotes + " (" 
				+ intNoteId + " INTEGER PRIMARY KEY, " 
				+ fltFreq + " REAL, "
				+ txtNoteCode + " TEXT, "
				+ fltFreqOfDies + " REAL)"; 
			
		// константы для таблицы tblNoteLengths 
		public static final String tblNoteLengths = "tblNoteLengths";
		public static final String intLengthId = "intLengthId";
		public static final String fltMultiplier = "fltMultiplier";
		
		private static final String SQL_CREATE_TBL_NOTE_LENGTH = "CREATE TABLE " 
				+ tblNoteLengths + " (" 
				+ intLengthId + " INTEGER PRIMARY KEY, " 
				+ fltMultiplier + " REAL)";
		
		// константы для таблицы tblTimeSignature 
		public static final String tblTimeSignature = "tblTimeSignature";
		public static final String intTimeSignatureId = "intTimeSignatureId";
		public static final String intTimeSignature1 = "intTimeSignature1";
		public static final String intTimeSignature2 = "intTimeSignature2";
		
		private static final String SQL_CREATE_TBL_TIME_SIGNATURE = "CREATE TABLE " 
				+ tblTimeSignature + " (" 
				+ intTimeSignatureId + " INTEGER PRIMARY KEY, " 
				+ intTimeSignature1 + " INTEGER, "
				+ intTimeSignature2 + " INTEGER)";
	
	// константы, используемые при обновлении версии базы данных
	private static final String SQL_DELETE_TBL_INSTRUMENTS = "DROP TABLE IF EXISTS "
			+ tblInstruments;
	private static final String SQL_DELETE_TBL_NOTES = "DROP TABLE IF EXISTS "
			+ tblNotes;
	private static final String SQL_DELETE_TBL_NOTE_LENGTH = "DROP TABLE IF EXISTS "
			+ tblNoteLengths;
	private static final String SQL_DELETE_TBL_TIME_SIGNATURE = "DROP TABLE IF EXISTS "
			+ tblTimeSignature;

	// ВСТАВКА ДАННЫХ В БАЗУ ДАННЫХ
	
	/* ТАБЛИЦА ИНСТРУМЕНТОВ */
	String insert_data_into_tblInstruments_id1 = "INSERT INTO "
			+ WindTutorialDatabase.tblInstruments + " VALUES " 
			+ "(1, 'Блокфлейта сторй C (немецкая система)', 8, " 
			+ "'11111111', '11111112', '11111110', '11111120', '11111100', '11111000', '11110111', " 
			+ "'11110000', '11101120', '11100000', '11011000', '11000000', '10100000', '01100000', "
			+ "'00100000', '00111110', '21111100', '21111000', '21110101', '21110000', '21110111', " 
			+ "'21100000', '21101110', '21101100', '21001100', '21011000', '21011012', '21010000', " 
			+ "'20110120', '21101100', '20101100', '21001000', '01010011', '01010100', '01100001', " 
			+ "'!', '!', '!', '!')";
						
	String insert_data_into_tblInstruments_id2 = "INSERT INTO "
			+ WindTutorialDatabase.tblInstruments + " VALUES "
			+ "(2, 'Тинвистл строй D', 6, " 
			+ "'!', '!', '111111', '111112', '111110', '111120', '111100', '111000', '112000', '110000', " 
			+ "'101111', '101111', '011000', '000000', '011111', '111112', '111110', '111120', '111100', " 
			+ "'111000', '110110', '110000', '101000', '101000', '011111', '000000', '!', '!', '!', '!', '!', '!', " 
			+ "'!', '!', '!', '!', '!', '!', '!')";
	
	/* ТАБЛИЦА НОТ */
	String insert_data_into_tblNotes_id1 = "INSERT INTO " 
			+	WindTutorialDatabase.tblNotes + " VALUES (1, 261.63, 'C4', 277.18)";
	String insert_data_into_tblNotes_id2 = "INSERT INTO " 
			+	WindTutorialDatabase.tblNotes + " VALUES (2, 293.66, 'D4', 311.13)";
	String insert_data_into_tblNotes_id3 = "INSERT INTO " 
			+	WindTutorialDatabase.tblNotes + " VALUES (3, 329.63, 'E4', 0)";
	String insert_data_into_tblNotes_id4 = "INSERT INTO " 
			+	WindTutorialDatabase.tblNotes + " VALUES (4, 349.23, 'F4', 369.99)";
	String insert_data_into_tblNotes_id5 = "INSERT INTO " 
			+	WindTutorialDatabase.tblNotes + " VALUES (5, 392.00, 'G4', 415.30)";
	String insert_data_into_tblNotes_id6 = "INSERT INTO " 
			+	WindTutorialDatabase.tblNotes + " VALUES (6, 440.00, 'A4', 466.16)";
	String insert_data_into_tblNotes_id7 = "INSERT INTO " 
			+	WindTutorialDatabase.tblNotes + " VALUES (7, 493.88, 'B4', 0)";

	String insert_data_into_tblNotes_id8 = "INSERT INTO " 
			+	WindTutorialDatabase.tblNotes + " VALUES (8, 523.25, 'C5', 554.36)";
	String insert_data_into_tblNotes_id9 = "INSERT INTO " 
			+	WindTutorialDatabase.tblNotes + " VALUES (9, 587.32, 'D5', 622.26)";
	String insert_data_into_tblNotes_id10 = "INSERT INTO " 
			+	WindTutorialDatabase.tblNotes + " VALUES (10, 659.26, 'E5', 0)";
	String insert_data_into_tblNotes_id11 = "INSERT INTO " 
			+	WindTutorialDatabase.tblNotes + " VALUES (11, 698.46, 'F5', 739.98)";
	String insert_data_into_tblNotes_id12 = "INSERT INTO " 
			+	WindTutorialDatabase.tblNotes + " VALUES (12, 784.00, 'G5', 830.60)";
	String insert_data_into_tblNotes_id13 = "INSERT INTO " 
			+	WindTutorialDatabase.tblNotes + " VALUES (13, 880.00, 'A5', 932.32)";
	String insert_data_into_tblNotes_id14 = "INSERT INTO " 
			+	WindTutorialDatabase.tblNotes + " VALUES (14, 987.75, 'B5', 0)";

	String insert_data_into_tblNotes_id15 = "INSERT INTO " 
			+	WindTutorialDatabase.tblNotes + " VALUES (15, 1046.50, 'C6', 1108.70)";
	String insert_data_into_tblNotes_id16= "INSERT INTO " 
			+	WindTutorialDatabase.tblNotes + " VALUES (16, 1174.60, 'D6', 1244.50)";
	String insert_data_into_tblNotes_id17 = "INSERT INTO " 
			+	WindTutorialDatabase.tblNotes + " VALUES (17, 1318.50, 'E6', 0)";
	String insert_data_into_tblNotes_id18 = "INSERT INTO " 
			+	WindTutorialDatabase.tblNotes + " VALUES (18, 1396.90, 'F6', 1480.00)";
	String insert_data_into_tblNotes_id19 = "INSERT INTO " 
			+	WindTutorialDatabase.tblNotes + " VALUES (19, 1568.00, 'G6', 1661.20)";
	String insert_data_into_tblNotes_id20 = "INSERT INTO " 
			+	WindTutorialDatabase.tblNotes + " VALUES (20, 1720.00, 'A6', 1864.60)";
	String insert_data_into_tblNotes_id21 = "INSERT INTO " 
			+	WindTutorialDatabase.tblNotes + " VALUES (21, 1975.50, 'B6', 0)";

	String insert_data_into_tblNotes_id22 = "INSERT INTO " 
			+	WindTutorialDatabase.tblNotes + " VALUES (22, 2093.00, 'C7', 2217.40)";
	String insert_data_into_tblNotes_id23 = "INSERT INTO " 
			+	WindTutorialDatabase.tblNotes + " VALUES (23, 2349.20, 'D7', 0)";
	
	/* ТАБЛИЦА ДЛИТЕЛЬНОСТЕЙ НОТ */
	String insert_data_into_tblNoteLengths_id1 = "INSERT INTO " + 
			WindTutorialDatabase.tblNoteLengths + " VALUES (1, 	16)";
	String insert_data_into_tblNoteLengths_id2 = "INSERT INTO " + 
			WindTutorialDatabase.tblNoteLengths + " VALUES (2, 	8)";
	String insert_data_into_tblNoteLengths_id3 = "INSERT INTO " + 
			WindTutorialDatabase.tblNoteLengths + " VALUES (3, 	4)";
	String insert_data_into_tblNoteLengths_id4 = "INSERT INTO " + 
			WindTutorialDatabase.tblNoteLengths + " VALUES (4, 	2)";
	String insert_data_into_tblNoteLengths_id5 = "INSERT INTO " + 
			WindTutorialDatabase.tblNoteLengths + " VALUES (5, 	1)";
	
	/* ТАБЛИЦА РАЗМЕРОВ */
	String insert_data_into_tblTimeSignature_id1 = "INSERT INTO " + 
			WindTutorialDatabase.tblTimeSignature + " VALUES (1, 	2, 2)";
	String insert_data_into_tblTimeSignature_id2 = "INSERT INTO " + 
			WindTutorialDatabase.tblTimeSignature + " VALUES (2, 	2, 4)";
	String insert_data_into_tblTimeSignature_id3 = "INSERT INTO " + 
			WindTutorialDatabase.tblTimeSignature + " VALUES (3, 	2, 8)";
	String insert_data_into_tblTimeSignature_id4 = "INSERT INTO " + 
			WindTutorialDatabase.tblTimeSignature + " VALUES (4, 	3, 2)";
	String insert_data_into_tblTimeSignature_id5 = "INSERT INTO " + 
			WindTutorialDatabase.tblTimeSignature + " VALUES (5, 	3, 4)";
	String insert_data_into_tblTimeSignature_id6 = "INSERT INTO " + 
			WindTutorialDatabase.tblTimeSignature + " VALUES (6, 	3, 8)";
	String insert_data_into_tblTimeSignature_id7 = "INSERT INTO " + 
			WindTutorialDatabase.tblTimeSignature + " VALUES (7, 	4, 4)";
	String insert_data_into_tblTimeSignature_id8 = "INSERT INTO " + 
			WindTutorialDatabase.tblTimeSignature + " VALUES (8, 	4, 2)";
	String insert_data_into_tblTimeSignature_id9 = "INSERT INTO " + 
			WindTutorialDatabase.tblTimeSignature + " VALUES (9, 	6, 4)";
	String insert_data_into_tblTimeSignature_id10 = "INSERT INTO " + 
			WindTutorialDatabase.tblTimeSignature + " VALUES (10, 	6, 8)";
	String insert_data_into_tblTimeSignature_id11 = "INSERT INTO " + 
			WindTutorialDatabase.tblTimeSignature + " VALUES 	(11, 	9, 8)";
	String insert_data_into_tblTimeSignature_id12 = "INSERT INTO " + 
			WindTutorialDatabase.tblTimeSignature + " VALUES (12, 	12, 8)";
	
	@Override
	public void onCreate(SQLiteDatabase db) {
		// создаем таблицы с полями
	    Log.d("LOG_TAG", "--- onCreate database ---");
		db.execSQL(SQL_CREATE_TBL_INSTRUMENTS);
		db.execSQL(SQL_CREATE_TBL_NOTES);
		db.execSQL(SQL_CREATE_TBL_NOTE_LENGTH);
		db.execSQL(SQL_CREATE_TBL_TIME_SIGNATURE);
		Log.d("LOG_TAG", "database is created");
		
		// вставляем строки в таблицы
	    Log.d("LOG_TAG", "--- Inserting data into database ---");
	    
		db.execSQL(insert_data_into_tblInstruments_id1); db.execSQL(insert_data_into_tblInstruments_id2);
		
		db.execSQL(insert_data_into_tblNotes_id1); db.execSQL(insert_data_into_tblNotes_id2); db.execSQL(insert_data_into_tblNotes_id3);
		db.execSQL(insert_data_into_tblNotes_id4); db.execSQL(insert_data_into_tblNotes_id5); db.execSQL(insert_data_into_tblNotes_id6);
		db.execSQL(insert_data_into_tblNotes_id7); db.execSQL(insert_data_into_tblNotes_id8); db.execSQL(insert_data_into_tblNotes_id9);
		db.execSQL(insert_data_into_tblNotes_id10); db.execSQL(insert_data_into_tblNotes_id11); db.execSQL(insert_data_into_tblNotes_id12);
		db.execSQL(insert_data_into_tblNotes_id13); db.execSQL(insert_data_into_tblNotes_id14); db.execSQL(insert_data_into_tblNotes_id15);
		db.execSQL(insert_data_into_tblNotes_id16); db.execSQL(insert_data_into_tblNotes_id17); db.execSQL(insert_data_into_tblNotes_id18);
		db.execSQL(insert_data_into_tblNotes_id19); db.execSQL(insert_data_into_tblNotes_id20); db.execSQL(insert_data_into_tblNotes_id21);
		db.execSQL(insert_data_into_tblNotes_id22); db.execSQL(insert_data_into_tblNotes_id23);
		
		db.execSQL(insert_data_into_tblNoteLengths_id1); db.execSQL(insert_data_into_tblNoteLengths_id2);
		db.execSQL(insert_data_into_tblNoteLengths_id3); db.execSQL(insert_data_into_tblNoteLengths_id4);
		db.execSQL(insert_data_into_tblNoteLengths_id5);
		
		db.execSQL(insert_data_into_tblTimeSignature_id1); db.execSQL(insert_data_into_tblTimeSignature_id2);
		db.execSQL(insert_data_into_tblTimeSignature_id3); db.execSQL(insert_data_into_tblTimeSignature_id4);
		db.execSQL(insert_data_into_tblTimeSignature_id5); db.execSQL(insert_data_into_tblTimeSignature_id6);
		db.execSQL(insert_data_into_tblTimeSignature_id7); db.execSQL(insert_data_into_tblTimeSignature_id8);
		db.execSQL(insert_data_into_tblTimeSignature_id9); db.execSQL(insert_data_into_tblTimeSignature_id10);
		db.execSQL(insert_data_into_tblTimeSignature_id11); db.execSQL(insert_data_into_tblTimeSignature_id12);
		
		Log.d("LOG_TAG", "rows inserted");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		Log.w("LOG_TAG", "Обновление базы данных с версии " + oldVersion
				+ " до версии " + newVersion + ", которое удалит все старые данные...");
		// Удаляем предыдущую базу данных при обновлении
		db.execSQL(SQL_DELETE_TBL_INSTRUMENTS);
		db.execSQL(SQL_DELETE_TBL_NOTES);
		db.execSQL(SQL_DELETE_TBL_NOTE_LENGTH);
		db.execSQL(SQL_DELETE_TBL_TIME_SIGNATURE);
		// Создаём новый экземпляр базы данных
		onCreate(db);
		Log.w("LOG_TAG", "...обновление успешно завершено.");
	}

}