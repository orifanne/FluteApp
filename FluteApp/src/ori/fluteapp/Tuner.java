package ori.fluteapp;

import android.os.Bundle;
import android.util.Log;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import org.apache.commons.math3.transform.FastFourierTransformer;
import org.apache.commons.math3.complex.Complex;
import org.apache.commons.math3.transform.DftNormalization;
import org.apache.commons.math3.transform.TransformType;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.Cursor;
//import android.util.Log;


public class Tuner {
	
	private static final String TAG = "Tuner";
		
        private static final int RECORDER_BPP = 16;
        private static final int RECORDER_SAMPLERATE = 44100;
        private static final int RECORDER_CHANNELS = AudioFormat.CHANNEL_IN_MONO;
        private static final int RECORDER_AUDIO_ENCODING = AudioFormat.ENCODING_PCM_16BIT;
		private static final int disFreq = 44100;
		private int iFreq = 265;
		private AudioRecord recorder;
        private int bufferSize = 8192;
		private int addon = 10; //разница частот нужная для определения нижней границы снизу
		private int addon2 = 120; //для границы сверху

		private double chank[][] = new double[2][bufferSize];
		private byte data[] = new byte[bufferSize];
        private float freq; // ? DOUBLE => FLOAT (В БД ХРАНЯТСЯ FLOAT)
	
		
		// Перед getChank()
		public void initRecorder(){
			bufferSize = AudioRecord.getMinBufferSize(RECORDER_SAMPLERATE, RECORDER_CHANNELS,RECORDER_AUDIO_ENCODING);
			Log.d(TAG, "***********************MinBufferSize: " + bufferSize);
			int size = (int) Math.ceil(Math.log(bufferSize) / Math.log(2));
			Log.d(TAG, "***********************BufferSize: " + bufferSize);
			bufferSize = (int) Math.pow(2, size);
			recorder = new AudioRecord(MediaRecorder.AudioSource.MIC,
                                                RECORDER_SAMPLERATE, RECORDER_CHANNELS,RECORDER_AUDIO_ENCODING, bufferSize);
		}
		// При завершении работы с тюнером
		public void relRecorder(){
			recorder.release();
            recorder = null;
		}
		public int[] getNoteId(){
			//needs rework
			//bufferSize = AudioRecord.getMinBufferSize(RECORDER_SAMPLERATE,RECORDER_CHANNELS,RECORDER_AUDIO_ENCODING);
			getChank();
			freq = getFreq();
			Log.d("TAG", "freq = " + freq);
			
			/* ===============================================freq===================================================== */
			
			int[] idUnder = new int[2]; // не надо private, они и так определены в пределах метода getNoteId()
			int[] idOver = new int[2];
			
			float freqUnder = 0, freqOver = 0;
			boolean flagUnder = false, flagOver = false;
			int noteId = 0; // id ноты (высчитывается на каждой итерации while ниже)
			
			// формируем строку запроса: id ноты, ее частоты, частота ее диеза (для всех нот из БД)
			String queryGetNotesFreq = "SELECT " + WindTutorialDatabase.intNoteId + ", " + WindTutorialDatabase.fltFreq 
					+ ", " + WindTutorialDatabase.fltFreqOfDies + " FROM " + WindTutorialDatabase.tblNotes;
			// скармливаем строку с запросом методу rawQuery().
			// экземпляр БД создается на всю программу Единственный, в MainActivity.java. 
			// здесь идет только обращение к нему:
			Cursor cursor = MainActivity.sqdb.rawQuery(queryGetNotesFreq, null);
			// перебираем в цикле все строки выборки
			while (cursor.moveToNext()) {
				noteId = cursor.getInt(cursor.getColumnIndex(WindTutorialDatabase.intNoteId));
				float noteFreq = cursor.getFloat(cursor.getColumnIndex(WindTutorialDatabase.fltFreq));
				float noteFreqOfDies = cursor.getFloat(cursor.getColumnIndex(WindTutorialDatabase.fltFreqOfDies));
								
				// исключение: freq совпало с каким-то значением в БД
				if (freq == noteFreq) {
					freqUnder = noteFreq; freqOver = noteFreq;
					flagUnder = true; flagOver = true;
					idUnder[0] = noteId; idUnder[1] = 0; // совпадение НЕ с диезом  
					idOver[0] = noteId; idOver[1] = 0; // совпадение НЕ с диезом 
					break;
				}
				if (freq == noteFreqOfDies) {
					freqUnder = noteFreqOfDies;	freqOver = noteFreqOfDies;
					flagUnder = true; flagOver = true;
					idUnder[0] = noteId; idUnder[1] = 1; // совпадение с диезом  
					idOver[0] = noteId; idOver[1] = 1; // совпадение с диезом 
					break;
				}
				// исключение: freq < всех частот в БД
				if (freq < noteFreq && flagUnder == false) {
					freqUnder = 0; freqOver = noteFreq;
					flagUnder = false; flagOver = true;
					idUnder[0] = 0; idUnder[1] = 0; // нижней границы нет
					idOver[0] = noteId; idOver[1] = 0; // верняя граница - первая нота в БД (С4)
					break;
				}
				// ищем ближайшую нижнюю границу
				if (noteFreq < freq) {
					if (noteFreqOfDies < freq && noteFreqOfDies != 0) {
						freqUnder = noteFreqOfDies;
						idUnder[0] = noteId; idUnder[1] = 1; // нижняя граница - диез-нота с текущим Id
					}
					else {
						freqUnder = noteFreq;
						idUnder[0] = noteId; idUnder[1] = 0; // нижняя граница - НЕ диез-нота с текущим Id
					}
					flagUnder = true;
				}
				// ищем ближайшую верхнюю границу
				if (freq < noteFreq && flagOver == false) {
					freqOver = noteFreq;
					flagOver = true;
					idOver[0] = noteId; idOver[1] = 0; // верняя граница - НЕ диез-нота с текущим Id
					break;
				}
				if (freq < noteFreqOfDies && flagOver == false) {
					freqOver = noteFreqOfDies;
					flagOver = true;
					idOver[0] = noteId; idOver[1] = 1; // верняя граница - диез-нота с текущим Id
					break;
				}
			}
			cursor.close();
			
			// если вышли с такими флагами, - это сигнал исключения: freq > всех частот из БД
			if (flagUnder == true && flagOver == false) {
				freqOver = 0;
				idUnder[0] = noteId; idUnder[1] = 0; // нижняя граница - последняя нота в БД (D7)
				idOver[0] = 0; idOver[1] = 0; // верней границы нет
			}
			
			// выходные данные для отладки
			//Log.d("LOG_TAG", "freqUnder = " + freqUnder + "  freqOver = " + freqOver);
			//Log.d("LOG_TAG", "idUnder[] = " + idUnder[0] + " , " + idUnder[1]);
			//Log.d("LOG_TAG", "idOver[] = " + idOver[0] + " , " + idOver[1]);
			/* ======================================================================================================== */
			
			//System.out.println("Freq = "+Double.toString(freq));
				
				//System.out.println(Integer.toString(freq));
				/*while ((freq > noteID[i]) & (i<60)){
						i++;
				}
				if((noteID[i] - freq) < (freq - noteID[i-1])){ID = i;} else { ID = i-1;}*/
			if(freqUnder == freqOver) {
				return idUnder;
			} else {
				if(freqUnder == 0) {
					//addon norm ideya :)
					if(freqOver>freq + addon) {
						idUnder[0] = -1;
						return idUnder;
					} else {
						return idOver;
					}
				} else {
					if(freqOver == 0) {
						if(freqUnder < freq + addon2) {
							idUnder[0] = -1;
							return idUnder;
						} else {
							return idUnder;
						}
					} else {
						if(Math.log(freqOver) - Math.log(freq) > Math.log(freq) - Math.log(freqUnder)) {
							return idUnder;
						} else {
							return idOver;
						}
					}
				}
			}
				
		}       
		
		private void getChank(){
			recorder.startRecording();
            recorder.read(data, 0, bufferSize);
            recorder.stop();
        	for (int i = 0; i < bufferSize; i++){
				chank[0][i] = (double)data[i];
				chank[1][i] = 0;
			}
	    }
                
        private float getFreq(){
			//FastFourierTransformer transformer = new FastFourierTransformer(DftNormalization.STANDARD);
			FastFourierTransformer.transformInPlace(chank, DftNormalization.STANDARD, TransformType.FORWARD);
			//for (int i = 0; i < bufferSize; i++){
			//	System.out.println(Double.toString(chank[0][i])+" "+Double.toString(chank[1][i]));
			//}
			double maxAmp;
			double amp1,amp2,amp3,amp;
			int supIndex = 100;
			amp1 = Math.sqrt(chank[0][supIndex] * chank[0][supIndex] + chank[1][supIndex] * chank[1][supIndex]);
			amp2 = Math.sqrt(chank[0][supIndex+1] * chank[0][supIndex+1] + chank[1][supIndex+1] * chank[1][supIndex+1]);
			amp3 = Math.sqrt(chank[0][supIndex+2] * chank[0][supIndex+2] + chank[1][supIndex+2] * chank[1][supIndex+2]);
			amp = maxAmp = amp1 + amp2 + amp3;
			int index = supIndex + 2;
			
			int loops = 600;
			for (int j = supIndex + 3; j < loops; j++) {    
				amp1 = amp2;
				amp2 = amp3;
				amp3 = Math.sqrt(chank[0][j] * chank[0][j] + chank[1][j] * chank[1][j]);
				amp = amp - amp1 + amp3;
				
				if (maxAmp < amp){
					maxAmp = amp;
					index = j;
				}
			}

			float dFreq = (float) index*RECORDER_SAMPLERATE/bufferSize;
			
			return dFreq;
		}
		
		public void genChank(){
		    // генерация массива с чистой нотой
			for(int i=0; i < bufferSize; i++){
				chank[0][i] = 10*(Math.sin(2*Math.PI*iFreq*i/disFreq));
				chank[1][i] = 0;
				//System.out.println(Double.toString(chank[0][i]));
			}
       
		}
}