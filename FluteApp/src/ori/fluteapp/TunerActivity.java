package ori.fluteapp;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.database.Cursor;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.TableRow;
import android.widget.TextView;

public class TunerActivity extends Activity {
	
	private static final String TAG = "TunerActivity";
	TunerTask tunerTask;
	TextView text;
	TunerDrawPanel panel;
	
	class TunerTask extends AsyncTask<Void, String, Void> {
		
		private static final String TAG = "TunerTask";
		int [] a;

		@Override
		protected Void doInBackground(Void... params) {
			Tuner tuner = new Tuner();
			tuner.initRecorder();
			int i = 0;
			a = new int [2];
			while (true) {
				if (isCancelled()) {
					tuner.relRecorder();
					return null;
				}
				a = tuner.getNoteId();
				//if (a[0] > 0) {
				//	Log.d(TAG, "***********************NOTE: " + a[0] + "(" + i + ")");
					//Log.d(TAG, "***********************ISDIEZ: " + a[1] + "(" + i + ")");
				//}
					
					/* ============================================================================================ */	
					// вх. данные: id ноты ( a[0] ) и isDiez ( a[1] )
						
					String noteSig = ""; // C4, D4, E4, ... (без диезов) - лежит в поле txtNoteCode таблице Нот
					String diezSig = ""; // будет символ "d", если нота является диезовой
							
					// формируем строку запроса
					String query_get_note = "SELECT " + WindTutorialDatabase.txtNoteCode
							+ " FROM " + WindTutorialDatabase.tblNotes
							+ " WHERE " + WindTutorialDatabase.intNoteId + "=" + a[0];
					// скармливаем строку с запросом методу rawQuery()
					Cursor cursor = MainActivity.sqdb.rawQuery(query_get_note, null);
					// перебираем в цикле все строки выборки
						while (cursor.moveToNext()) {
						noteSig = cursor.getString(cursor.getColumnIndex(WindTutorialDatabase.txtNoteCode));
						//Log.d("LOG_TAG", "***********************a[0] = "  + a[0] + " a[1] = " + a[1]);
						if (a[1] == 1) diezSig = "d";
						else diezSig = ""; // !!!!
						//Log.d("LOG_TAG", "***********************NoteCode= " + noteSig + diezSig);
					}
					cursor.close();
					
					// алфавитно-цифровая запись ноты на выходе
					String NoteCode = noteSig + diezSig;
					/* ============================================================================================ */
					publishProgress(NoteCode);
					i++;
			}
		}

		@Override
		protected void onProgressUpdate(String... params) {
			super.onProgressUpdate(params);
			text.setText(params[0]);
			panel.setDies(a[1]);
			panel.setPos(a[0] % 7);
			panel.invalidate();
		}

	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_tuner);
		
		//View panel = findViewById(R.id.tunerDrawPanel);
		//TableRow row = (TableRow)findViewById(R.id.tableRow2);
		//int w = row.getWidth();
		//int h = row.getHeight();
		//LayoutParams params = new LayoutParams((int) w / 4, h);
		//panel.setLayoutParams(params);
		
		panel = (TunerDrawPanel) findViewById(R.id.tunerDrawPanel);
		TableRow row = (TableRow)findViewById(R.id.tableRow2);
		int w = (int) row.getWidth() / 2;
		panel.setLeft(w);
		
		text = (TextView)findViewById(R.id.noteText);

		tunerTask = new TunerTask();
		tunerTask.execute();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.tuner, menu);
		return true;
	}
	
	public void mainMenuClick(View v)
	{
	    finish();
	    tunerTask.cancel(true);
	}

}