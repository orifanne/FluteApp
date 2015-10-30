package ori.fluteapp;

import android.media.AudioTrack;
import android.media.AudioFormat;
import android.media.AudioManager;

public class Player {
		
    private static final int RECORDER_BPP = 16;
	private int BUFFERSIZE = 8000;
    private static final int SAMPLERATE = 8000;
    private static final int CHANNEL = AudioFormat.CHANNEL_OUT_MONO;
    private static final int AUDIO_ENCODING = AudioFormat.ENCODING_PCM_16BIT;
    public byte buffer[] = new byte[BUFFERSIZE];
	private static final int STREAM = AudioManager.STREAM_MUSIC;
	private float freq;
	private int time; //time in ms
	private AudioTrack Track;
	public void initPlayer(){
		Track = new AudioTrack(STREAM, SAMPLERATE, CHANNEL,AUDIO_ENCODING, BUFFERSIZE, AudioTrack.MODE_STREAM);
	}
	public void relPlayer(){
		Track.release();
    }
	
	public void playFreq(float inFreq, int inTime) {
		time = inTime;
		freq = inFreq;
		genChank();
		int noteLoops = inTime / 1000;
		for (int i = 0; i < noteLoops; i++) {
			Track.write(buffer, 0, BUFFERSIZE);
			Track.play();
		}
		double noteRest = BUFFERSIZE*(inTime / 1000.0 - noteLoops);
		int restBuffer = (int) noteRest;
		Track.write(buffer, 0, restBuffer);
		Track.play();
		//}
		//Track.play();
		//Track.flush();
		
	}
	public void genChank(){
	    // генерация массива с чистой нотой
		for(int i=0; i < buffer.length; i++){
		buffer[i] = (byte) (Byte.MAX_VALUE*(Math.sin(2*Math.PI*freq*i/SAMPLERATE)));
		}
      
	  }
}