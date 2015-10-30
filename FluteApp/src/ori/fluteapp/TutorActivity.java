package ori.fluteapp;

import java.net.URISyntaxException;

import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Point;
import android.view.Display;
import android.view.Menu;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.TextView;
import android.widget.Toast;

public class TutorActivity extends Activity implements ScrollViewListener {

	private static final int FILE_SELECT_CODE = 0;
	private static final String TAG = "TutorActivity";
	private NoteFile file = null;
	String path = "";
	FingeringDrawPanel panel1;
	FileDrawPanel panel;
	TextView text;
	TunerTask tunerTask;
	int width;
	boolean teaching = false;
	boolean playing = false;
	Player player;
	PlayTask playTask;

	class TunerTask extends AsyncTask<Void, Void, Void> {

		private static final String TAG = "TunerTask";
		int[] a;

		@Override
		protected Void doInBackground(Void... params) {
			Tuner tuner = new Tuner();
			tuner.initRecorder();
			int i = 0;
			a = new int[2];
			while (true) {
				if (isCancelled()) {
					tuner.relRecorder();
					return null;
				}
				a = tuner.getNoteId();
				publishProgress();
				i++;
			}
		}

		@Override
		protected void onProgressUpdate(Void... params) {
			super.onProgressUpdate(params);

			Log.d(TAG, "WHAT: " + panel1.notes[panel.current][0] + " " +
			 panel1.notes[panel.current][1]);
			Log.d(TAG, "With WHAT: " + a[0] + " " + a[1]);

			int rest1 = panel1.notes[panel.current][0] % 11;
			int rest2 = a[0] % 11;
			
			if ((rest1 == rest2) && (panel1.notes[panel.current][1] == a[1])) {
				panel.error = 0;
				if (panel.current < file.length) {
					panel.current++;
					ObservableScrollView scrollView1 = (ObservableScrollView) findViewById(R.id.scrollview1);
					scrollView1.scrollTo(FileDrawPanel.coords[panel.current] - (width / 2), 0);
				} else
					tunerTask.cancel(true);
			} else {
				panel.error = 1;
				// Log.d(TAG, "ERROR!!!!!!");
			}
			panel.postInvalidate();
		}

	}

	@Override
	protected void onDestroy() {
		player.relPlayer();
		super.onDestroy();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_tutor);
		player = new Player();
		player.initPlayer();

		ObservableScrollView scrollView1 = (ObservableScrollView) findViewById(R.id.scrollview1);
		scrollView1.setScrollViewListener(this);
		ObservableScrollView scrollView2 = (ObservableScrollView) findViewById(R.id.scrollview2);
		scrollView2.setScrollViewListener(this);

		Intent intent = getIntent();
		if (intent.hasExtra("file")) {
			file = (NoteFile) intent.getSerializableExtra("file");

			TextView textBpm = (TextView) findViewById(R.id.textView1);
			textBpm.setText(file.bpm + "bpm");

			panel = (FileDrawPanel) findViewById(R.id.fileDrawPanel);
			panel.setFile(file);
			panel.invalidate();

			panel1 = (FingeringDrawPanel) findViewById(R.id.fingeringDrawPanel);
			panel1.setFile(file);
			panel1.invalidate();
		}

		if (intent.hasExtra("path")) {
			path = intent.getStringExtra("path");

			TextView textName = (TextView) findViewById(R.id.textView2);
			textName.setText(path);
		}

		Context ctx = this.getBaseContext();
		WindowManager wm = (WindowManager) ctx.getSystemService(Context.WINDOW_SERVICE);
		Display display = wm.getDefaultDisplay();
		Point size = new Point();
		display.getSize(size);
		width = size.x;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.tutor, menu);
		return true;
	}

	public void onScrollChanged(ObservableScrollView scrollView, int x, int y, int oldx, int oldy) {
		ObservableScrollView scrollView1 = (ObservableScrollView) findViewById(R.id.scrollview1);
		ObservableScrollView scrollView2 = (ObservableScrollView) findViewById(R.id.scrollview2);

		if (scrollView == scrollView1) {
			scrollView2.scrollTo(x, y);
		} else if (scrollView == scrollView2) {
			scrollView1.scrollTo(x, y);
		}
	}

	private void showFileChooser() {
		Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
		intent.setType("*/*");
		intent.addCategory(Intent.CATEGORY_OPENABLE);

		try {
			startActivityForResult(Intent.createChooser(intent, "Select a File to Upload"), FILE_SELECT_CODE);
		} catch (android.content.ActivityNotFoundException ex) {
			// Potentially direct the user to the Market with a Dialog
			Toast.makeText(this, "Please install a File Manager.", Toast.LENGTH_SHORT).show();
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		case FILE_SELECT_CODE:
			if (resultCode == RESULT_OK) {
				// Get the Uri of the selected file
				Uri uri = data.getData();
				// Log.d(TAG, "File Uri: " + uri.toString());
				// Get the path
				try {
					path = FileUtils.getPath(this, uri);
				} catch (URISyntaxException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				// Log.d(TAG, "File Path: " + path);

				// Get the file instance
				file = new NoteFile(path);

				TextView textBpm = (TextView) findViewById(R.id.textView1);
				textBpm.setText(file.bpm + "bpm");

				TextView textName = (TextView) findViewById(R.id.textView2);
				textName.setText(path);

				panel = (FileDrawPanel) findViewById(R.id.fileDrawPanel);
				panel.setFile(file);
				panel.invalidate();

				panel1 = (FingeringDrawPanel) findViewById(R.id.fingeringDrawPanel);
				panel1.setFile(file);
				panel1.invalidate();
			}
			break;
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	public void mainMenuClick(View v) {
		if (playing)
			return;
		finish();
	}

	public void fileClick(View v) {
		if (playing)
			return;
		showFileChooser();
	}

	public void editorClick(View v) {
		if (playing)
			return;
		Intent intent = new Intent(this, EditorActivity.class);
		intent.putExtra("file", file);
		intent.putExtra("path", path);
		startActivity(intent);
		finish();
	}

	public void instrClick(View v) {
		if ((file == null) || (playing))
			return;
		text = (TextView) findViewById(R.id.instrText);
		AlertDialog dialog = InstrDialogScreen.getDialog(this);
		dialog.show();
	}

	public void playClick(View v) {
		if (file == null)
			return;
		if (!playing) {
			playing = true;
			playTask = new PlayTask();
			playTask.execute();
		} else {
			playing = false;
		}
	}

	public void tutorClick(View v) {
		if ((file == null) || (playing))
			return;

		if (!teaching) {
			tunerTask = new TunerTask();
			tunerTask.execute();

			teaching = true;
			Button but = (Button) findViewById(R.id.button3);
			but.setText("Стоп");
		} else {
			tunerTask.cancel(true);
			teaching = false;
			Button but = (Button) findViewById(R.id.button3);
			but.setText("Обучение");
			panel.error = 0;
			panel.invalidate();
		}
	}

	class PlayTask extends AsyncTask<Void, Void, Void> {

		@Override
		protected Void doInBackground(Void... params) {
			FileDrawPanel panel = (FileDrawPanel) findViewById(R.id.fileDrawPanel);
			player.playFreq(0, 1000);
			Cursor cursor = null;
			for (int i = panel.current; i < file.length; i++) {
				if (!playing)
					break;
				int id = panel.notes[panel.current][0];
				int isDiez = panel.notes[panel.current][1];
				if (id < 0) {
					player.playFreq(0, (60000 / file.bpm) * 4 / Math.abs(file.notes[i][1]));
				} else {
					float freq = 0; // вот эту переменную надо инициализировать
									// из запроса к БД,
									// там должна быть частота ноты (по id и
									// isDiez)
					/*
					 * =========================================================
					 * ===================================
					 */
					// вытаскиваем ноту с нужным id
					String queryGetNoteFreq = "SELECT " + WindTutorialDatabase.intNoteId + ", "
							+ WindTutorialDatabase.fltFreq + ", " + WindTutorialDatabase.fltFreqOfDies + " FROM "
							+ WindTutorialDatabase.tblNotes + " WHERE " + WindTutorialDatabase.intNoteId + "=" + id;
					// устанавливам курсор на выборку
					cursor = MainActivity.sqdb.rawQuery(queryGetNoteFreq, null);
					// работаем с записью
					while (cursor.moveToNext()) {
						float noteFreq = cursor.getFloat(cursor.getColumnIndex(WindTutorialDatabase.fltFreq));
						float noteFreqOfDies = cursor
								.getFloat(cursor.getColumnIndex(WindTutorialDatabase.fltFreqOfDies));
						// в зависимости от является нота диезом или нет,
						// берется соответствующая поле (частота)
						if (isDiez == 0)
							freq = noteFreq;
						else
							freq = noteFreqOfDies;
					}

					/*
					 * =========================================================
					 * ===================================
					 */
					player.playFreq(freq, (60000 / file.bpm) * 4 / file.notes[i][1]);
				}
				panel.current++;
				panel.postInvalidate();
				HorizontalScrollView scrollView1 = (HorizontalScrollView) findViewById(R.id.scrollview1);
				scrollView1.scrollTo(FileDrawPanel.coords[panel.current] - (width / 2), 0);
			}
			panel.current--;
			playing = false;
			cursor.close();
			return null;
		}
	}

}