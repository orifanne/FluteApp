package ori.fluteapp;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.util.Log;
import android.widget.TextView;

public class InstrDialogScreen {
	
	static TutorActivity act;

	public InstrDialogScreen() {
		// TODO Auto-generated constructor stub
	}
	
    public static AlertDialog getDialog(TutorActivity activity) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        act = activity;
	            builder.setTitle("Выберите инструмент");
	            //String data[] = { "one", "two", "three", "four" };
	            builder.setCancelable(true);
	            
	            // количество инструментов
	            int InstrNum = 0;
	            
	            // формируем строку запроса
	    		String queryCountInstr = "SELECT * FROM " + WindTutorialDatabase.tblInstruments;
	    		// скармливаем строку с запросом методу rawQuery()
	    		Cursor cursor1 = MainActivity.sqdb.rawQuery(queryCountInstr, null);
	    		// перебираем в цикле все строки выборки
	    		/*while (cursor1.moveToNext()) {
	    			InstrNum = cursor1.getInt(cursor1.getColumnIndex(WindTutorialDatabase.intInstrumentId));
	    		}*/
	    		InstrNum = cursor1.getCount();
	    		Log.d("LOG_TAG", "ROWS:  " + InstrNum);
	    		cursor1.close();
	            
	            // массив названий инструментов
	            final String Names[] = new String[InstrNum];
	            for (int i = 0; i < InstrNum; i++)
	            	Names[i] = new String();
	            // название инструмента на каждой итерации
	            String strInstrName = "";
	            
	    	    // формируем строку запроса
	    		String queryGetInstrNames = "SELECT " + WindTutorialDatabase.txtInstrumentName +
	    				" FROM " + WindTutorialDatabase.tblInstruments;
	    		// скармливаем строку с запросом методу rawQuery()
	    		Cursor cursor = MainActivity.sqdb.rawQuery(queryGetInstrNames, null);
	    		// перебираем в цикле все строки выборки
	    		for (int i = 0; i < InstrNum; i++) {
	    			cursor.moveToNext();
	    			strInstrName = cursor.getString(cursor.getColumnIndex(WindTutorialDatabase.txtInstrumentName));
	    			Names[i] = strInstrName;
	    			Log.d("LOG_TAG", "ROW:  Names[" + i + "] = " + Names[i]);
	    			
	    		}
	    		
				cursor.close();
	            
	            builder.setItems(Names, new DialogInterface.OnClickListener() {
	            public void onClick(DialogInterface dialog, int item) {
		                act.text.setText(Names[item]);
		                act.panel1.instrument = item + 1;
		                //Log.d("LOG_TAG", "ITEM: " + item);
		                act.panel1.invalidate();
		            }
		        });
            return builder.create();	
    }

}