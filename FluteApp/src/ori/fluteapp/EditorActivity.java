package ori.fluteapp;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.net.URISyntaxException;

import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Point;
import android.util.Log;
import android.view.Display;
import android.view.Menu;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

public class EditorActivity extends Activity {

	private static final int FILE_SELECT_CODE = 0;
	private static final String TAG = "EditorActivity";
	NoteFile file = null;
	private historyTypes historyType = historyTypes.NO_CH;
	private int[] historyUn;
	private int[] historyRe;
	private boolean canRedo = false;
	private boolean canUndo = false;
	private boolean wereChanges = false;
	
	TextView textName;

	private enum historyTypes {
		CH_NOTE_POS, CH_NOTE_LEN, CH_SIGN, DEL_NOTE, DEL_SIGN, ADD_NOTE, CH_ZATACT, CH_SIZE, CH_BPM, NO_CH
	}

	String path = "";
	int open_save = 0;
	public int close_open = 0;
	Player player;
	boolean playing = false;
	int width;
	PlayTask playTask;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_editor);
		historyUn = new int[4];
		historyRe = new int[4];
		final TextView bpm = (TextView) findViewById(R.id.textBpm);
		SeekBar seek = (SeekBar) findViewById(R.id.seekBar);
		seek.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
			@Override
			public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
				if (file != null) {
					bpm.setText(String.valueOf(progress + 30) + "bpm");
					file.bpm = progress + 30;
					wereChanges = true;
					textName.setText(path + " (не сохранен)");
				}
			}

			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub
				// mTextValue.setText(String.valueOf(seekBar.getProgress()));

			}
		});
		
		textName = (TextView) findViewById(R.id.textName);

		Intent intent = getIntent();
		if (intent.hasExtra("file")) {
			file = (NoteFile) intent.getSerializableExtra("file");

			bpm.setText(file.bpm + "bpm");

			FileDrawPanel panel = (FileDrawPanel) findViewById(R.id.fileDrawPanel);
			panel.setFile(file);
			panel.invalidate();
						
			if (intent.hasExtra("path")) {
				path = intent.getStringExtra("path");

				textName.setText(path);
			}
		}

		else {
			createNew();
		}

		player = new Player();
		player.initPlayer();

		Context ctx = this.getBaseContext();
		WindowManager wm = (WindowManager) ctx.getSystemService(Context.WINDOW_SERVICE);
		Display display = wm.getDefaultDisplay();
		Point size = new Point();
		display.getSize(size);
		width = size.x;
	}

	@Override
	protected void onDestroy() {
		player.relPlayer();
		super.onDestroy();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.editor, menu);
		return true;
	}

	void showFileChooser() {
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
				Log.d(TAG, "***************open_save: " + open_save);

				if (open_save != 2) {

					file = new NoteFile(path);

					TextView textBpm = (TextView) findViewById(R.id.textBpm);
					textBpm.setText(file.bpm + "bpm");
					SeekBar seek = (SeekBar) findViewById(R.id.seekBar);
					seek.setProgress(file.bpm);

					textName.setText(path);

					FileDrawPanel panel = (FileDrawPanel) findViewById(R.id.fileDrawPanel);
					panel.setFile(file);
					panel.invalidate();
					wereChanges = false;
				}

				else {
					save();
					open_save = 0;
				}
			}
			break;
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	public void mainMenuClick(View v) {
		if (playing)
			return;
		close_open = 1;
		if ((file != null) && (wereChanges == true)) {
			AlertDialog dialog = DialogScreen.getDialog(this, DialogScreen.DIALOG_CLOSE);
			dialog.show();
		} else
			finish();
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
		if ((file == null) || (path == "") || playing)
			return;

		if ((file != null) && (wereChanges == true)) {
			AlertDialog dialog = DialogScreen.getDialog(this, DialogScreen.DIALOG_TUTOR);
			dialog.show();
		} else {
			Intent intent = new Intent(this, TutorActivity.class);
			intent.putExtra("file", file);
			intent.putExtra("path", path);
			startActivity(intent);
			finish();
		}
	}

	public void openClick(View v) {
		if (playing)
			return;
		if ((file != null) && (wereChanges == true)) {
			AlertDialog dialog = DialogScreen.getDialog(this, DialogScreen.DIALOG_OPEN);
			dialog.show();
		} else
			showFileChooser();
	}

	public void save() {
		open_save = 2;
		if ((path != "") && (file != null))
			FileUtils.save(file, path, this);
		if ((path == "") && (file != null)) {
			showFileChooser();
			FileUtils.save(file, path, this);
		}
		textName.setText(path);
		historyType = historyTypes.NO_CH;
		wereChanges = false;
	}

	void createNew() {
		file = new NoteFile();
		path = "";
		textName.setText("(не сохранен)");
		FileDrawPanel panel = (FileDrawPanel) findViewById(R.id.fileDrawPanel);
		panel.setFile(file);
		panel.current = 0;
		panel.invalidate();
		wereChanges = false;
	}

	public void saveClick(View v) {
		if (playing)
			return;
		save();
	}

	public void newClick(View v) {
		if (playing)
			return;
		if ((file != null) && (wereChanges == true)) {
			AlertDialog dialog = DialogScreen.getDialog(this, DialogScreen.DIALOG_NEW);
			dialog.show();
		} else
			createNew();
	}

	public void updownClick(View v) {
		if (playing)
			return;
		if (file == null)
			return;

		ImageButton b = (ImageButton) v;
		FileDrawPanel panel = (FileDrawPanel) findViewById(R.id.fileDrawPanel);

		if (b.getId() == R.id.imageButtonUp) {
			if ((file.notes[panel.current][0] >= 22) || (file.notes[panel.current][1] < 0))
				return;
			historyUn[1] = file.notes[panel.current][0];
			file.notes[panel.current][0]++;
			historyRe[1] = file.notes[panel.current][0];
		}

		if (b.getId() == R.id.imageButtonDown) {
			if ((file.notes[panel.current][0] <= 1) || (file.notes[panel.current][1] < 0))
				return;
			historyUn[1] = file.notes[panel.current][0];
			file.notes[panel.current][0]--;
			historyRe[1] = file.notes[panel.current][0];
		}

		historyType = historyTypes.CH_NOTE_POS;
		historyUn[0] = panel.current;
		historyRe[0] = panel.current;
		panel.invalidate();
		canRedo = false;
		canUndo = true;
		wereChanges = true;
		textName.setText(path + " (не сохранен)");

		/*
		 * if (((file.notes[panel.current][0] == 1) ||
		 * (file.notes[panel.current][0] == 4) || (file.notes[panel.current][0]
		 * == 8) || (file.notes[panel.current][0] == 11) ||
		 * (file.notes[panel.current][0] == 15) || (file.notes[panel.current][0]
		 * == 18) || (file.notes[panel.current][0] == 22)) &&
		 * (file.notes[panel.current][2] % 4 == 2)) file.notes[panel.current][2]
		 * -= 2;
		 * 
		 * if (((file.notes[panel.current][0] == 3) ||
		 * (file.notes[panel.current][0] == 7) || (file.notes[panel.current][0]
		 * == 10) || (file.notes[panel.current][0] == 14) ||
		 * (file.notes[panel.current][0] == 17) || (file.notes[panel.current][0]
		 * == 21)) && (file.notes[panel.current][2] % 4 == 1))
		 * file.notes[panel.current][2] -= 1;
		 */
	}

	public void chNoteClick(View v) {
		if (playing)
			return;
		if (file == null)
			return;

		RadioButton edit = (RadioButton) findViewById(R.id.rad_edit);
		RadioButton paste = (RadioButton) findViewById(R.id.rad_paste);

		ImageButton b = (ImageButton) v;
		FileDrawPanel panel = (FileDrawPanel) findViewById(R.id.fileDrawPanel);

		if (edit.isChecked()) {
			historyUn[0] = panel.current;
			historyUn[1] = file.notes[panel.current][1];
			switch (b.getId()) {
			case R.id.imageButtonOne:
				file.notes[panel.current][1] = 1;
				break;
			case R.id.imageButtonTwo:
				file.notes[panel.current][1] = 2;
				break;
			case R.id.imageButtonFour:
				file.notes[panel.current][1] = 4;
				break;
			case R.id.imageButtonEight:
				file.notes[panel.current][1] = 8;
				break;
			case R.id.imageButtonSixteen:
				file.notes[panel.current][1] = 16;
				break;
			case R.id.imageButtonPOne:
				file.notes[panel.current][1] = -1;
				break;
			case R.id.imageButtonPTwo:
				file.notes[panel.current][1] = -2;
				break;
			case R.id.imageButtonPFour:
				file.notes[panel.current][1] = -4;
				break;
			case R.id.imageButtonPEight:
				file.notes[panel.current][1] = -8;
				break;
			case R.id.imageButtonPSixteen:
				file.notes[panel.current][1] = -16;
				break;
			}
			historyType = historyTypes.CH_NOTE_LEN;
			historyRe[0] = panel.current;
			historyRe[1] = file.notes[panel.current][1];
		}

		if (paste.isChecked()) {
			if (file.length == (NoteFile.maxSize + 1))
				return;
			int i;
			for (i = file.length; i > (panel.current); i--) {
				file.notes[i][0] = file.notes[i - 1][0];
				file.notes[i][1] = file.notes[i - 1][1];
				file.notes[i][2] = file.notes[i - 1][2];
			}
			historyUn[0] = panel.current;
			historyRe[0] = panel.current;
			file.notes[panel.current][0] = 1;
			file.notes[panel.current][2] = 0;

			switch (b.getId()) {
			case R.id.imageButtonOne:
				file.notes[panel.current][1] = 1;
				break;
			case R.id.imageButtonTwo:
				file.notes[panel.current][1] = 2;
				break;
			case R.id.imageButtonFour:
				file.notes[panel.current][1] = 4;
				break;
			case R.id.imageButtonEight:
				file.notes[panel.current][1] = 8;
				break;
			case R.id.imageButtonSixteen:
				file.notes[panel.current][1] = 16;
				break;
			case R.id.imageButtonPOne:
				file.notes[panel.current][1] = -1;
				break;
			case R.id.imageButtonPTwo:
				file.notes[panel.current][1] = -2;
				break;
			case R.id.imageButtonPFour:
				file.notes[panel.current][1] = -4;
				break;
			case R.id.imageButtonPEight:
				file.notes[panel.current][1] = -8;
				break;
			case R.id.imageButtonPSixteen:
				file.notes[panel.current][1] = -16;
				break;
			}
			historyRe[1] = file.notes[panel.current][1];
			file.length++;
			historyType = historyTypes.ADD_NOTE;
		}
		canRedo = false;
		canUndo = true;
		wereChanges = true;
		textName.setText(path + " (не сохранен)");
		panel.invalidate();
	}

	public void addSignClick(View v) {
		if (playing)
			return;
		if (file == null)
			return;

		FileDrawPanel panel = (FileDrawPanel) findViewById(R.id.fileDrawPanel);
		if (file.notes[panel.current][1] < 0)
			return;
		historyUn[0] = panel.current;
		historyUn[1] = file.notes[panel.current][2];
		ImageButton b = (ImageButton) v;

		switch (b.getId()) {
		case R.id.imageButtonDiez:
			/*
			 * if ((file.notes[panel.current][0] == 3) ||
			 * (file.notes[panel.current][0] == 7) ||
			 * (file.notes[panel.current][0] == 10) ||
			 * (file.notes[panel.current][0] == 14) ||
			 * (file.notes[panel.current][0] == 17) ||
			 * (file.notes[panel.current][0] == 21)) return;
			 */
			file.notes[panel.current][2] = (file.notes[panel.current][2] / 4) * 4 + 1;
			break;
		case R.id.imageButtonBemol:
			/*
			 * if ((file.notes[panel.current][0] == 1) ||
			 * (file.notes[panel.current][0] == 4) ||
			 * (file.notes[panel.current][0] == 8) ||
			 * (file.notes[panel.current][0] == 11) ||
			 * (file.notes[panel.current][0] == 15) ||
			 * (file.notes[panel.current][0] == 18) ||
			 * (file.notes[panel.current][0] == 22)) return;
			 */
			file.notes[panel.current][2] = (file.notes[panel.current][2] / 4) * 4 + 2;
			break;
		case R.id.imageButtonBekar:
			file.notes[panel.current][2] = (file.notes[panel.current][2] / 4) * 4 + 3;
			break;
		case R.id.imageButtonDote:
			file.notes[panel.current][2] = (file.notes[panel.current][2] % 4) + 4;
			break;
		}
		historyType = historyTypes.CH_SIGN;
		panel.invalidate();
		historyRe[0] = panel.current;
		historyRe[1] = file.notes[panel.current][2];
		canRedo = false;
		canUndo = true;
		wereChanges = true;
		textName.setText(path + " (не сохранен)");
	}

	public void delNoteClick(View v) {
		if (playing)
			return;
		if (file == null)
			return;

		if (file.length == 0)
			return;

		FileDrawPanel panel = (FileDrawPanel) findViewById(R.id.fileDrawPanel);
		historyRe[0] = panel.current;
		historyUn[1] = file.notes[panel.current][0];
		historyUn[2] = file.notes[panel.current][1];
		historyUn[3] = file.notes[panel.current][2];
		int i;
		for (i = panel.current; i < (file.length - 1); i++) {
			file.notes[i][0] = file.notes[i + 1][0];
			file.notes[i][1] = file.notes[i + 1][1];
			file.notes[i][2] = file.notes[i + 1][2];
		}

		file.notes[file.length - 1][0] = 0;
		file.notes[file.length - 1][1] = 0;
		file.notes[file.length - 1][2] = 0;
		file.length--;

		historyUn[0] = panel.current;
		if ((file.length == panel.current) && (panel.current > 0))
			panel.current--;

		Log.d(TAG, "Length: " + file.length);
		historyType = historyTypes.DEL_NOTE;
		panel.invalidate();
		canRedo = false;
		canUndo = true;
		wereChanges = true;
		textName.setText(path + " (не сохранен)");
	}

	public void delSignClick(View v) {
		if (playing)
			return;
		if (file == null)
			return;

		FileDrawPanel panel = (FileDrawPanel) findViewById(R.id.fileDrawPanel);
		if (file.notes[panel.current][1] < 0)
			return;
		historyUn[0] = panel.current;
		historyUn[1] = file.notes[panel.current][2];
		file.notes[panel.current][2] = 0;
		historyType = historyTypes.DEL_SIGN;
		panel.invalidate();
		historyRe[0] = panel.current;
		historyRe[1] = file.notes[panel.current][2];
		canRedo = false;
		canUndo = true;
		wereChanges = true;
		textName.setText(path + " (не сохранен)");
	}

	public void zatactClick(View v) {
		if (playing)
			return;
		if (file == null)
			return;

		FileDrawPanel panel = (FileDrawPanel) findViewById(R.id.fileDrawPanel);
		historyUn[0] = file.zatact;
		ImageButton b = (ImageButton) v;
		if ((b.getId() == R.id.imageButtonMinusZatact) && (file.zatact > 0))
			file.zatact--;
		if ((b.getId() == R.id.imageButtonPlusZatact) && (file.zatact < (file.length - 1)))
			file.zatact++;
		historyType = historyTypes.CH_ZATACT;
		Log.d(TAG, "historyType: " + historyType);
		panel.invalidate();
		historyRe[0] = file.zatact;
		canRedo = false;
		canUndo = true;
		wereChanges = true;
		textName.setText(path + " (не сохранен)");
	}

	public void chSizeClick(View v) {
		if (playing)
			return;
		if (file == null)
			return;

		historyUn[0] = file.size1;
		historyUn[1] = file.size2;

		ImageButton b = (ImageButton) v;
		FileDrawPanel panel = (FileDrawPanel) findViewById(R.id.fileDrawPanel);

		switch (b.getId()) {
		case R.id.imageButtonSize6_8:
			file.size1 = 6;
			file.size2 = 8;
			break;
		case R.id.imageButtonSize2_2:
			file.size1 = 2;
			file.size2 = 2;
			break;
		case R.id.imageButtonSize2_4:
			file.size1 = 2;
			file.size2 = 4;
			break;
		case R.id.imageButtonSize2_8:
			file.size1 = 2;
			file.size2 = 8;
			break;
		case R.id.imageButtonSize3_2:
			file.size1 = 3;
			file.size2 = 2;
			break;
		case R.id.imageButtonSize3_4:
			file.size1 = 3;
			file.size2 = 4;
			break;
		case R.id.imageButtonSize3_8:
			file.size1 = 3;
			file.size2 = 8;
			break;
		case R.id.imageButtonSize4_2:
			file.size1 = 4;
			file.size2 = 2;
			break;
		case R.id.imageButtonSize4_4:
			file.size1 = 4;
			file.size2 = 4;
			break;
		case R.id.imageButtonSize6_4:
			file.size1 = 6;
			file.size2 = 4;
			break;
		}
		historyRe[0] = file.size1;
		historyRe[1] = file.size2;
		historyType = historyTypes.CH_SIZE;
		panel.invalidate();
		canRedo = false;
		canUndo = true;
		wereChanges = true;
		textName.setText(path + " (не сохранен)");
	}

	public void undoClick(View v) {
		if (playing)
			return;
		if ((file == null) || (canUndo == false))
			return;

		FileDrawPanel panel = (FileDrawPanel) findViewById(R.id.fileDrawPanel);

		switch (historyType) {
		case CH_ZATACT:
			file.zatact = historyUn[0];
			canRedo = true;
			// Log.d(TAG, "********zatact: " + file.zatact);
			break;
		case DEL_NOTE:
			panel.current = historyUn[0];
			RadioButton paste = (RadioButton) findViewById(R.id.rad_paste);
			paste.setChecked(true);
			ImageButton b = (ImageButton) findViewById(R.id.imageButtonOne);
			chNoteClick(b);
			file.notes[panel.current][0] = historyUn[1];
			file.notes[panel.current][1] = historyUn[2];
			file.notes[panel.current][2] = historyUn[3];
			historyType = historyTypes.DEL_NOTE;
			canRedo = true;
			canUndo = false;
			break;
		case ADD_NOTE:
			panel.current = historyUn[0];
			ImageButton d = (ImageButton) findViewById(R.id.imageButtonDelNote);
			delNoteClick(d);
			historyType = historyTypes.ADD_NOTE;
			canUndo = false;
			canRedo = true;
			break;
		case CH_NOTE_LEN:
			file.notes[historyUn[0]][1] = historyUn[1];
			canRedo = true;
			break;
		case CH_NOTE_POS:
			file.notes[historyUn[0]][0] = historyUn[1];
			canRedo = true;
			break;
		// эти две возможно стоит объединить
		case CH_SIGN:
			file.notes[historyUn[0]][2] = historyUn[1];
			canRedo = true;
			break;
		case DEL_SIGN:
			file.notes[historyUn[0]][2] = historyUn[1];
			canRedo = true;
			break;
		case CH_SIZE:
			file.size1 = historyUn[0];
			file.size2 = historyUn[1];
			canRedo = true;
			break;
		case CH_BPM:
			canRedo = true;
			break;
		default:
			break;

		}
		panel.invalidate();
	}

	public void redoClick(View v) {
		if (playing)
			return;
		if ((file == null) || (canRedo == false))
			return;

		FileDrawPanel panel = (FileDrawPanel) findViewById(R.id.fileDrawPanel);

		switch (historyType) {
		case CH_ZATACT:
			file.zatact = historyRe[0];
			canRedo = false;
			// Log.d(TAG, "********zatact: " + file.zatact);
			break;
		case DEL_NOTE:
			panel.current = historyRe[0];
			ImageButton d = (ImageButton) findViewById(R.id.imageButtonDelNote);
			delNoteClick(d);
			canRedo = false;
			break;
		case ADD_NOTE:
			panel.current = historyRe[0];
			RadioButton paste = (RadioButton) findViewById(R.id.rad_paste);
			paste.setChecked(true);
			ImageButton b = null;
			switch (historyRe[1]) {
			case 1:
				b = (ImageButton) findViewById(R.id.imageButtonOne);
				break;
			case 2:
				b = (ImageButton) findViewById(R.id.imageButtonTwo);
				break;
			case 4:
				b = (ImageButton) findViewById(R.id.imageButtonFour);
				break;
			case 8:
				b = (ImageButton) findViewById(R.id.imageButtonEight);
				break;
			case 16:
				b = (ImageButton) findViewById(R.id.imageButtonSixteen);
				break;
			case -1:
				b = (ImageButton) findViewById(R.id.imageButtonPOne);
				break;
			case -2:
				b = (ImageButton) findViewById(R.id.imageButtonPTwo);
				break;
			case -4:
				b = (ImageButton) findViewById(R.id.imageButtonPFour);
				break;
			case -8:
				b = (ImageButton) findViewById(R.id.imageButtonPEight);
				break;
			case -16:
				b = (ImageButton) findViewById(R.id.imageButtonPSixteen);
				break;
			}
			chNoteClick(b);
			canRedo = false;
			break;
		case CH_NOTE_LEN:
			file.notes[historyRe[0]][1] = historyRe[1];
			canRedo = false;
			break;
		case CH_NOTE_POS:
			file.notes[historyRe[0]][0] = historyRe[1];
			canRedo = false;
			break;
		// эти две возможно стоит объединить
		case CH_SIGN:
			file.notes[historyRe[0]][2] = historyRe[1];
			canRedo = false;
			break;
		case DEL_SIGN:
			file.notes[historyRe[0]][2] = historyRe[1];
			canRedo = false;
			break;
		case CH_SIZE:
			file.size1 = historyRe[0];
			file.size2 = historyRe[1];
			canRedo = false;
			break;
		case CH_BPM:
			canRedo = false;
			break;
		default:
			break;

		}
		panel.invalidate();
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
				HorizontalScrollView scrollView1 = (HorizontalScrollView) findViewById(R.id.horizontalScrollView1);
				scrollView1.scrollTo(FileDrawPanel.coords[panel.current] - (width / 2), 0);
			}
			panel.current--;
			playing = false;
			cursor.close();
			return null;

		}
	}

}