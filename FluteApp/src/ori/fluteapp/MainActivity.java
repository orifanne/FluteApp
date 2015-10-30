package ori.fluteapp;

import android.os.Bundle;
import android.database.sqlite.SQLiteDatabase;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;

public class MainActivity extends Activity {
	static WindTutorialDatabase sqh; // единственный экземпляр на всю программу
	static SQLiteDatabase sqdb; // единственный экземпляр на всю программу
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		// Инициализируем наш класс-обёртку
        sqh = new WindTutorialDatabase(this);
        
        // База нам нужна для записи и чтения
        sqdb = sqh.getWritableDatabase();
	}
	
	//@Override
/*	public void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		// закрываем соединения с базой данных
		sqdb.close();
		sqh.close();
	}*/

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	public void tunerClick(View v)
	{
	    Intent intent = new Intent(this, TunerActivity.class);
	    startActivity(intent);
	}
	
	public void editorClick(View v)
	{
	    Intent intent = new Intent(this, EditorActivity.class);
	    startActivity(intent);
	}
	
	public void tutorClick(View v)
	{
	    Intent intent = new Intent(this, TutorActivity.class);
	    startActivity(intent);
	}
	
	public void exitClick(View v)
	{
	    finish();
	}

}