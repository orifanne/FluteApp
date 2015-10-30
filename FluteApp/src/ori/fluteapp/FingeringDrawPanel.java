package ori.fluteapp;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;

public class FingeringDrawPanel extends View {
	
	private static final String TAG = "TutorActivity";
	
	int[][] notes;
	
	NoteFile file = null;
	//id инструмента
	public int instrument = 1;
		
	public FingeringDrawPanel(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	public FingeringDrawPanel(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	public FingeringDrawPanel(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		// TODO Auto-generated constructor stub
	}
	
	public void setFile(NoteFile file1) {
		file = file1;
	}
	
	public void setInstrunent(int instrument1) {
		instrument = instrument1;
	}
	
	@Override
	protected void onDraw(Canvas canvas){
	    super.onDraw(canvas);
	    
	    Paint paint = new Paint();
	    paint.setStyle(Paint.Style.FILL);

	    // закрашиваем холст белым цветом
	    paint.setColor(Color.WHITE);
	    canvas.drawPaint(paint);
	    
	    int w = getWidth();
	    int h = getHeight();
	        
		//число отверстий в инструменте
	    int holesNum = 0;
	    
	    // формируем строку запроса
		String queryGetHolesNum = "SELECT " + WindTutorialDatabase.intInstrumentId + ", "
				+ WindTutorialDatabase.intNumOfHoles + " FROM " + WindTutorialDatabase.tblInstruments + 
				" WHERE " + WindTutorialDatabase.intInstrumentId + "=" + instrument;
		// скармливаем строку с запросом методу rawQuery()
		Cursor cursor1 = MainActivity.sqdb.rawQuery(queryGetHolesNum, null);
		// перебираем в цикле все строки выборки
		while (cursor1.moveToNext()) {
			int id = cursor1.getInt(cursor1.getColumnIndex(WindTutorialDatabase.intInstrumentId));
			String strHolesNum = cursor1.getString(cursor1.getColumnIndex(WindTutorialDatabase.intNumOfHoles));
			// конвертируем полученную строку в число отверстий
			holesNum = Integer.parseInt(strHolesNum.toString());
			//Log.i("LOG_TAG", "ROW:  Instrument = " + id + " NumOfHoles = " + holesNum);
		}
		cursor1.close();
	    
	    //диаметр кругов
	    int d = (int) Math.min(FileDrawPanel.minNoteWidth * 0.7, 
	    		h / ((holesNum + 1) * 0.25 + holesNum)) / 2;
	    //отступ между кругами
	    int sp = (h - d * holesNum) / (holesNum + 1);
	    
	    //строка с аппликатурой
	    String app = "";
	    
	    if (file != null) {
	    	notes = new int[file.length][2];
	    	int i, j, g = 0;
	    	int[][] signs = new int[23][2];
	    	for (int t = 0; t < 23; t++) {
	    		signs[t][0] = 0;
	    		signs[t][1] = 0;
	    	}
	    	for (i = 0; i < file.length; i++) {
			    int pos = file.notes[i][0];
			    boolean isDiez = false;
			    if ((file.notes[i][2] % 4) == 1) {
			    	signs[pos][0] = 1;
			    	signs[pos][1] = i;
			    	if ((pos == 3) || (pos == 7) || (pos == 10) || (pos == 14) || (pos == 17) || (pos == 21))
			    		pos++;
			    	else
			    		isDiez = true;
			    }
			    if ((file.notes[i][2] % 4) == 2) {
			    	signs[pos][0] = 2;
			    	signs[pos][1] = i;
			    	if (pos == 1)
			    		continue;
			    	pos--;
			    	if ((pos != 4) || (pos != 8) || (pos != 11) || (pos != 15) || (pos != 18) || (pos != 22))
			    		isDiez = true;
			    }

			    if ((signs[pos][0] == 1) && (signs[pos][1] != i) && ((file.notes[i][2] % 4) != 3)) {
			    	if ((pos == 3) || (pos == 7) || (pos == 10) || (pos == 14) || (pos == 17) || (pos == 21))
			    		pos++;
			    	else
			    		isDiez = true;
			    }	    	
			    if ((signs[pos][0] == 2) && (signs[pos][1] != i) && ((file.notes[i][2] % 4) != 3)) {
			    	if (pos == 1)
			    		continue;
			    	pos--;
			    	if ((pos != 4) || (pos != 8) || (pos != 11) || (pos != 15) || (pos != 18) || (pos != 22))
			    		isDiez = true;
			    }
			    
			    notes[i][0] = pos;
			    if (isDiez)
			    	notes[i][1] = 1;
			    else
			    	notes[i][1] = 0;

			    if (file.notes[i][1] < 0) {
			    	notes[i][0] = -1;
			    	continue;
			    }
			    
			    paint.setColor(Color.BLACK);
			    
			    // 1) получаем алфафитно-цифровое название ноты из БД,
			    // т.е. название ноты, которую будем искать в таблице аппликатур, напр., С4 или С4d
			    
			    String noteSig = ""; // C4, D4, E4, ... (без диезов) - лежит в поле txtNoteCode таблице Нот
				String diezSig = ""; // будет символ "d", если нота является диезовой
					
				// формируем строку запроса
				String query_get_note = "SELECT " + WindTutorialDatabase.txtNoteCode
						+ " FROM " + WindTutorialDatabase.tblNotes
						+ " WHERE " + WindTutorialDatabase.intNoteId + "=" + pos;
				// скармливаем строку с запросом методу rawQuery()
				Cursor cursor2 = MainActivity.sqdb.rawQuery(query_get_note, null);
				// перебираем в цикле все строки выборки
				while (cursor2.moveToNext()) {
					noteSig = cursor2.getString(cursor2.getColumnIndex(WindTutorialDatabase.txtNoteCode));
					//Log.d("LOG_TAG", "      pos = "  + pos + " isDiez = " + isDiez +" note = " + noteSig);
					if (isDiez == true) diezSig = "d";
					else diezSig = "";
					//Log.d("LOG_TAG", "      finalNoteName = " + noteSig + diezSig);
				}
				cursor2.close();
				
				// 2) по найденному названию ноты получаем соотв. поле с аппликатурой 
				//    и записываем в переменную app
				String query_get_fingering = "SELECT " + WindTutorialDatabase.intInstrumentId + ", "
						+ " intFingering" + noteSig + diezSig // сразу формируем строку => обращение к классу не треб-ся
						+ " FROM " + WindTutorialDatabase.tblInstruments
						+ " WHERE " + WindTutorialDatabase.intInstrumentId + "=" + instrument;
				// скармливаем строку с запросом методу rawQuery()
				Cursor cursor3 = MainActivity.sqdb.rawQuery(query_get_fingering, null);
				// перебираем в цикле все строки выборки
				while (cursor3.moveToNext()) {
					app = cursor3.getString(cursor3.getColumnIndex("intFingering" + noteSig + diezSig));
					//Log.d("LOG_TAG", "      app = " + app);
					//Log.i("LOG_TAG", "------------------------------------------");
				}
				cursor3.close();
				
		    	if (app.equals("!"))
		    		continue;
			    
			    for (j = 0; j < holesNum; j++) {
			    			    	
			    	if ((app.charAt(j) == '0') || (app.charAt(j) == '2')) {
			    		paint.setStyle(Paint.Style.STROKE);
			    		canvas.drawCircle(FileDrawPanel.coords[i] + d / 2, (float) 
			    		h - (j + 1) * sp - j * d, (float) d, paint);
			    	}
			    	
			    	if (app.charAt(j) == '1') {
			    		paint.setStyle(Paint.Style.FILL);
			    		canvas.drawCircle(FileDrawPanel.coords[i] + d / 2, (float) 
			    		h - (j + 1) * sp - j * d, (float) d, paint);
			    	}
			    	
			    	if (app.charAt(j) == '2') {
			    		paint.setStyle(Paint.Style.FILL);
			    		RectF oval = new RectF();
			    		oval.set(FileDrawPanel.coords[i] - d / 2, 
			    				h - (j + 1) * sp - j * d - d, 
			    				(float) (FileDrawPanel.coords[i] + d * 1.5),
			    				h - (j + 1) * sp - j * d + d);
			    		canvas.drawArc(oval, 45, 180, true, paint);
			    	}
			    }
			    
			    //если это уже не затакт, накапливать длительность данного такта
				if (i > file.zatact - 1) {
				   	g += ((float) file.size2 / Math.abs(file.notes[i][1]));
				   	//если есть точка
				   	if ((file.notes[i][2] / 4) == 1)
				   		g += ((float) file.size2 / Math.abs(file.notes[i][1]) / 2);
				}

				//если затакт закончился на данной ноте, считать длительность такта заполненной
				if (i == file.zatact - 1)
				   	g = file.size1;
				    
				//если такт закончился, обнулить счетчик
				if (g >= file.size1){
					for (int t = 0; t < 23; t++) {
			    		signs[t][0] = 0;
			    		signs[t][1] = 0;
			    	}
			    	g = 0;
			    }
	    	}
	    }
    }
	
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
	    super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	    
	    Context ctx = this.getContext();
	    WindowManager wm = (WindowManager) ctx.getSystemService(Context.WINDOW_SERVICE);
	    Display display = wm.getDefaultDisplay();
	    Point size = new Point();
	    display.getSize(size);
	    int width = size.x;
	    int height = size.y;

	    this.setMeasuredDimension(
	    		(width / FileDrawPanel.spaceNum) * 6 * NoteFile.maxSize, height / 3);
	}

}