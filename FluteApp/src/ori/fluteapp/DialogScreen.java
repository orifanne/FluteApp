package ori.fluteapp;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;

public class DialogScreen {

	static EditorActivity act = null;
	
	public DialogScreen() {
		// TODO Auto-generated constructor stub
	}
	
	final static int DIALOG_NEW = 0;
	final static int DIALOG_OPEN = 1;
	final static int DIALOG_CLOSE = 2;
	final static int DIALOG_TUTOR = 3;
	
    public static AlertDialog getDialog(EditorActivity activity, int ID) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        act = activity;
        switch(ID) {
        case DIALOG_NEW:
            builder.setMessage("Вы хотите сохранить предыдущий файл?")
            .setCancelable(false)
            .setPositiveButton("Да",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog,
                                int id) {
                        	act.save();
                        	act.createNew();
                        }
                    })
            .setNeutralButton("Отмена",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog,
                                int id) {
                            dialog.cancel();
                        }
                    })
            .setNegativeButton("Нет",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog,
                                int id) {
                        	act.createNew();
                        }
                    });
            return builder.create();
        case DIALOG_OPEN:
            builder.setMessage("Вы хотите сохранить предыдущий файл?")
            .setCancelable(false)
            .setPositiveButton("Да",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog,
                                int id) {
                        	act.save();
                        	act.showFileChooser();
                        }
                    })
            .setNeutralButton("Отмена",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog,
                                int id) {
                            dialog.cancel();
                        }
                    })
            .setNegativeButton("Нет",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog,
                                int id) {
                       		act.showFileChooser();
                        }
                    });
            return builder.create();
            
        case DIALOG_CLOSE:
            builder.setMessage("Вы хотите сохранить предыдущий файл?")
            .setCancelable(false)
            .setPositiveButton("Да",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog,
                                int id) {
                        	act.close_open = 1;
                        	act.save();
                        	//act.finish();
                        }
                    })
            .setNeutralButton("Отмена",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog,
                                int id) {
                            dialog.cancel();
                        }
                    })
            .setNegativeButton("Нет",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog,
                                int id) {
                        	act.finish();
                        }
                    });
            return builder.create();
            
        case DIALOG_TUTOR:
            builder.setMessage("Вы хотите сохранить предыдущий файл?")
            .setCancelable(false)
            .setPositiveButton("Да",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog,
                                int id) {
                        	act.save();
                        	Intent intent = new Intent(act, TutorActivity.class);
                			intent.putExtra("file", act.file);
                			intent.putExtra("path", act.path);
                			act.startActivity(intent);
                			act.finish();
                        }
                    })
            .setNeutralButton("Отмена",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog,
                                int id) {
                            dialog.cancel();
                        }
                    })
            .setNegativeButton("Нет",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog,
                                int id) {
                        	Intent intent = new Intent(act, TutorActivity.class);
                			intent.putExtra("file", act.file);
                			intent.putExtra("path", act.path);
                			act.startActivity(intent);
                			act.finish();
                        }
                    });
            return builder.create();
        default:
            return null;
        }		
    }

}