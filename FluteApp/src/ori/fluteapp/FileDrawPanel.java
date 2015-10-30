package ori.fluteapp;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Point;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;

public class FileDrawPanel extends View {
	
	private static final String TAG = "FileDrawPanel";
	
	public int error = 0;
	NoteFile file = null;
	//коэффициент масштабирования нот
	double part = 0.25;
	//коэффициент масштабирования скрипичного ключа
	double keyPart = 0.12;
	//горизонтальные координаты нот
	public static int[] coords;
	public static int minNoteWidth;
	//на сколько частей по вертикали делится
	public static int spaceNum = 14;
	public int current = 0;
	int[][] notes;
	
	public FileDrawPanel(Context context) {
		super(context);
		coords = new int[NoteFile.maxSize];
	}

	public FileDrawPanel(Context context, AttributeSet attrs) {
		super(context, attrs);
		coords = new int[NoteFile.maxSize];
	}

	public FileDrawPanel(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		coords = new int[NoteFile.maxSize];
	}
	
	public void setFile(NoteFile file1) {
		file = file1;
	}
	
	@Override
	protected void onDraw(Canvas canvas){
	    super.onDraw(canvas);
	    
	    Paint paint = new Paint();
	    paint.setStyle(Paint.Style.FILL);

	    // закрашиваем холст белым цветом
	    paint.setColor(Color.WHITE);
	    canvas.drawPaint(paint);
	    
	    paint.setColor(Color.BLACK);
	    paint.setStrokeWidth(3);
	    
	    int w = getWidth();
	    int h = getHeight();
	    int d = (int) h / spaceNum;
	        
	    canvas.drawLine(0, h - d * 2, w, h - d * 2, paint);
	    canvas.drawLine(0, h - d * 3, w, h - d * 3, paint);
	    canvas.drawLine(0, h - d * 4, w, h - d * 4, paint);
	    canvas.drawLine(0, h - d * 5, w, h - d * 5, paint);
	    canvas.drawLine(0, h - d * 6, w, h - d * 6, paint);
	    
	    Bitmap bm = BitmapFactory.decodeResource(getResources(),
	    		R.drawable.key);
	    
	    int bh = (int)(d / keyPart);
	    int bw = (bm.getWidth() * bh) / bm.getHeight();
	   
	    //bm = Bitmap.createScaledBitmap(bm, bw, (int) (bh - h * 0.02), false);
	    bm = Bitmap.createScaledBitmap(bm, bw, bh, false);
	    
	    canvas.drawBitmap(bm, 0, h - bh, null);
	       
	    if (file != null) {
	    	
	    	notes = new int[file.length][2];
	    	int[][] signs = new int[23][2];
	    	for (int t = 0; t < 23; t++) {
	    		signs[t][0] = 0;
	    		signs[t][1] = 0;
	    	}
	    	
	    	paint.setColor(Color.BLACK);
	    	
	    	int i = 0;

	    	Matrix matrix = new Matrix();
	    	matrix.preScale(-1.0f, -1.0f);

	    	minNoteWidth = this.getWidth();
	    	
	    	//отрисовать размер
		    paint.setTextSize((float) (d * 2.5));
		    canvas.drawText(file.size1 + "" , bw + d, h - d * 4, paint);
		    canvas.drawText(file.size2 + "" , bw + d, h - d * 2, paint);
	    	   	    
		    Bitmap bm1 = null;
		    Bitmap bm2 = null, bm2f = null;
		    Bitmap bm4 = null, bm4f = null;
		    Bitmap bm8 = null, bm8f = null;
		    Bitmap bm16 = null, bm16f = null;
		    Bitmap bmDz = null, bmBm = null, bmBk = null;
		    Bitmap bmP4 = null, bmP8 = null, bmP16 = null;
		    
		    int bh1 = 0, bw1 = 0;
		    int bh2 = 0, bw2 = 0;
		    int bh4 = 0, bw4 = 0;
		    int bh8 = 0, bw8 = 0, bw8f = 0;
		    int bh16 = 0, bw16 = 0, bw16f = 0;
		    int bhDz = 0, bwDz = 0;
		    int bhBm = 0, bwBm = 0;
		    int bhBk = 0, bwBk = 0;
		    int bhP4 = 0, bwP4 = 0;
		    int bhP8 = 0, bwP8 = 0;
		    int bhP16 = 0, bwP16 = 0;
		    
		    int count1 = 0;
		    int count2 = 0, count2f = 0;
		    int count4 = 0, count4f = 0;
		    int count8 = 0, count8f = 0;
		    int count16 = 0, count16f = 0;
		    int countDz = 0, countBm = 0, countBk = 0;
		    int countP4 = 0, countP8 = 0, countP16 = 0;
		    
		    //координаты отрисовки текущего элемента
		    float x = bw + 2 * d;
		    float y = 0;
		    
		    //длительность текущего такта
		    float j = 0;
		    
		    for (i = 0; i < file.length; i++) {
			    int pos = file.notes[i][0];
			    notes[i][0] = pos;
			    
			    boolean isDiez = false;
			    if ((file.notes[i][2] % 4) == 1) {
			    	signs[pos][0] = 1;
			    	signs[pos][1] = i;
			    	if ((pos == 3) || (pos == 7) || (pos == 10) || (pos == 14) || (pos == 17) || (pos == 21))
			    		notes[i][0] = pos + 1;
			    	else
			    		isDiez = true;
			    }
			    
			    if ((file.notes[i][2] % 4) == 2) {
			    	signs[pos][0] = 2;
			    	signs[pos][1] = i;
			    	//if (pos == 1)
			    		//continue;
			    	notes[i][0] = pos - 1;
			    	if ((pos != 4) || (pos != 8) || (pos != 11) || (pos != 15) || (pos != 18) || (pos != 22))
			    		isDiez = true;
			    }
			    
			    if ((signs[pos][0] == 1) && (signs[pos][1] != i) && ((file.notes[i][2] % 4) != 3)) {
			    	if ((pos == 3) || (pos == 7) || (pos == 10) || (pos == 14) || (pos == 17) || (pos == 21))
			    		notes[i][0] = pos + 1;
			    	else
			    		isDiez = true;
			    }	    	
			    if ((signs[pos][0] == 2) && (signs[pos][1] != i) && ((file.notes[i][2] % 4) != 3)) {
			    	if (pos == 1)
			    		continue;
			    	notes[i][0] = pos - 1;
			    	if ((pos != 4) || (pos != 8) || (pos != 11) || (pos != 15) || (pos != 18) || (pos != 22))
			    		isDiez = true;
			    }
			    
			    if (isDiez)
			    	notes[i][1] = 1;
			    else
			    	notes[i][1] = 0;
			    
			    if (file.notes[i][1] < 0)
			    	notes[i][0] = -1;
  
			    x += d * 3;
			    coords[i] = (int) x;
			    if ((i != 0) && (coords[i] - coords[i-1] < minNoteWidth))
			    	minNoteWidth = coords[i];
			    
			    if (file.notes[i][1] > 0) {
					if (file.notes[i][1] == 1) {
						if (count1 == 0) {
						    bm1 = BitmapFactory.decodeResource(getResources(),
								    		R.drawable.one);
						    bh1 = d;
						    bw1 = (bm1.getWidth() * bh1) / bm1.getHeight();
							bm1 = Bitmap.createScaledBitmap(bm1, bw1, bh1, false);
							count1++;
						}				    	
						y = (float) (h - 0.5 * pos * d - bh1);
						canvas.drawBitmap(bm1, x, y, null);
					}
							    
					if (file.notes[i][1] == 2) {
						if (count2 == 0) {
							bm2 = BitmapFactory.decodeResource(getResources(),
									    		R.drawable.two);
							bh2 = (int)(d / part);
							bw2 = (bm2.getWidth() * bh2) / bm2.getHeight();
							bm2 = Bitmap.createScaledBitmap(bm2, bw2, bh2, false);
							count2++;
						}
												
						if (pos <= 7) {
							y = (float) (h - 0.5 * pos * d - bh2);
							canvas.drawBitmap(bm2, x, y, null);
						 }
						 else {
							 if (count2f == 0) {
								bm2f = Bitmap.createBitmap(bm2, 0, 0, bw2, bh2,	matrix, true);
								count2f++;
							 }
							 y = (float) (h - 0.5 * d * pos - d);
							 canvas.drawBitmap(bm2f, x, y, null);
						 }
					}
						    
					if (file.notes[i][1] == 4) {
						 if (count4 == 0) {
							bm4 = BitmapFactory.decodeResource(getResources(),
								    		R.drawable.four);
							bh4 = (int)(d / part);
							bw4 = (bm4.getWidth() * bh4) / bm4.getHeight();		   
							bm4 = Bitmap.createScaledBitmap(bm4, bw4, bh4, false);
							count4++;
						 }
						 if (pos <= 7) {
							 y = (float) (h - 0.5 * pos * d - bh4);
							 canvas.drawBitmap(bm4, x, y, null);
						 }
						 else {
							 if (count4f == 0) {
								bm4f = Bitmap.createBitmap(bm4, 0, 0, bw4, bh4,	matrix, true);
								count4f++;
							 }
							 y = (float) (h - 0.5 * d * pos - d);
							 canvas.drawBitmap(bm4f, x, y, null);
						 }
					}
						    
					if (file.notes[i][1] == 8) {
					   	if (count8 == 0) {
					   		bm8 = BitmapFactory.decodeResource(getResources(),
								    		R.drawable.eight);		    
					    	bh8 = (int)(d / part);
					    	bw8 = (bm8.getWidth() * bh8) / bm8.getHeight();
							bm8 = Bitmap.createScaledBitmap(bm8, bw8, bh8, false);
							count8++;
						}
												
						if (pos <= 7) {
							y = (float) (h - 0.5 * pos * d - bh8);
							canvas.drawBitmap(bm8, x, y, null);
						 }
						 else {
							 if (count8f == 0) {
								 bm8f = BitmapFactory.decodeResource(getResources(),
								    		R.drawable.eightf);		    
								 bw8f = (bm8f.getWidth() * bh8) / bm8f.getHeight();
								 bm8f = Bitmap.createScaledBitmap(bm8f, bw8f, bh8, false);
								 count8++;
							 }
							 y = (float) (h - 0.5 * d * pos - d);
							 canvas.drawBitmap(bm8f, x, y, null);
						 }
					}
						    
					if (file.notes[i][1] == 16) {
					   	if (count16 == 0) {
					   		bm16 = BitmapFactory.decodeResource(getResources(),
								    		R.drawable.sixteen);		    
						    bh16 = (int)(d / part);
						    bw16 = (bm16.getWidth() * bh16) / bm16.getHeight();		   
						    bm16 = Bitmap.createScaledBitmap(bm16, bw16, bh16, false);
						    count16++;
					   	}
					    					    
					    if (pos <= 7) {
					    	y = (float) (h - 0.5 * pos * d - bh16);
						    canvas.drawBitmap(bm16, x, y, null);
						 }
						 else {
							 if (count16f == 0) {
								 bm16f = BitmapFactory.decodeResource(getResources(),
								    		R.drawable.sixteenf);		    
								 bw16f = (bm16f.getWidth() * bh16) / bm16f.getHeight();
								 bm16f = Bitmap.createScaledBitmap(bm16f, bw16f, bh16, false);
								count16f++;
							 }
							 y = (float) (h - 0.5 * d * pos - d);
							 canvas.drawBitmap(bm16f, x, y, null);
						 }
					}
				    
					//отрисовать дополнительные линейки
					if (pos == 1)
				    	canvas.drawLine((float) (x - d * 0.3), (float) (h - 0.5 * pos * d - 0.5 * d), 
				    			(float) (x + d * 1.5), (float) (h - 0.5 * pos * d - 0.5 * d), paint);
					if (pos > 12) {
						int k;
						for (k = pos; k > 12; k --) {
							if (k % 2 == 1)
								canvas.drawLine((float) (x - d * 0.5), (float) (h - 0.5 * k * d - 0.5 * d), 
						    			(float) (x + d * 1.5), (float) (h - 0.5 * k * d - 0.5 * d), paint);
						}
					}
					
					//отрисовать точку если она есть
					if ((file.notes[i][2] / 4) == 1) {
						x += 1.8 * d;
						if (pos <= 7)
							canvas.drawCircle(x, (float) (h - 0.5 * d * pos - d), 
									(float) (d / 5.0), paint);
						else
							canvas.drawCircle(x, y, d / 5, paint);
					}
					
					//отрисовать диез если он есть
					if ((file.notes[i][2] % 4) == 1) {
						if (countDz == 0) {
							bmDz = BitmapFactory.decodeResource(getResources(),
						    		R.drawable.diez);		    
							bhDz = (int)(d * 1.8);
							bwDz = (bmDz.getWidth() * bhDz) / bmDz.getHeight();		   
							bmDz = Bitmap.createScaledBitmap(bmDz, bwDz, bhDz, false);
							countDz++;
						}
						if ((file.notes[i][2] / 4) == 1)
							x += 0.5 * d;
						else x += 1.8 * d;
						if (pos <= 7)
							canvas.drawBitmap(bmDz, x, (float) (h - 0.5 * d * pos - bhDz), null);
						else
							canvas.drawBitmap(bmDz, x, (float) (y - 0.5 * d), null);
					}
					
					//отрисовать бемоль если он есть
					if ((file.notes[i][2] % 4) == 2) {
						if (countBm == 0) {
							bmBm = BitmapFactory.decodeResource(getResources(),
						    		R.drawable.bemol);		    
							bhBm = (int)(d * 1.8);
							bwBm = (bmBm.getWidth() * bhBm) / bmBm.getHeight();		   
							bmBm = Bitmap.createScaledBitmap(bmBm, bwBm, bhBm, false);
							countBm++;
						}
						if ((file.notes[i][2] / 4) == 1)
							x += 0.5 * d;
						else x += 1.8 * d;
						if (pos <= 7)
							canvas.drawBitmap(bmBm, x, (float) (h - 0.5 * d * pos - bhBm), null);
						else
							canvas.drawBitmap(bmBm, x, (float) (y - 0.5 * d), null);
					}
					
					//отрисовать бекар если он есть
					if ((file.notes[i][2] % 4) == 3) {
						if (countBk == 0) {
							bmBk = BitmapFactory.decodeResource(getResources(),
						    		R.drawable.bekar);		    
							bhBk = (int)(d * 1.8);
							bwBk = (bmBk.getWidth() * bhBk) / bmBk.getHeight();		   
							bmBk = Bitmap.createScaledBitmap(bmBk, bwBk, bhBk, false);
							countBk++;
						}
						if ((file.notes[i][2] / 4) == 1)
							x += 0.5 * d;
						else x += 1.8 * d;
						if (pos <= 7)
							canvas.drawBitmap(bmBk, x, (float) (h - 0.5 * d * pos - bhBk), null);
						else
							canvas.drawBitmap(bmBk, x, (float) (y - 0.5 * d), null);
					}
		    	}
			    
			    else {
			    	if (file.notes[i][1] == -1) {
			    		canvas.drawRect(x, h - d * 5, 
			    				x + d, (float) (h - d * 4.5), paint);
					}
			    	
			    	if (file.notes[i][1] == -2) {
			    		canvas.drawRect(x, (float) (h - d * 4.5), 
			    				x + d, h - d * 4, paint);
					}
			    	
			    	if (file.notes[i][1] == -4) {
				    		if (countP4 == 0) {
								bmP4 = BitmapFactory.decodeResource(getResources(),
							    		R.drawable.p4);		    
								bhP4 = d * 3;
								bwP4 = (bmP4.getWidth() * bhP4) / bmP4.getHeight();		   
								bmP4 = Bitmap.createScaledBitmap(bmP4, bwP4, bhP4, false);
								countP4++;
							}
				    		canvas.drawBitmap(bmP4, x, (float) (h - 5.5 * d), null);
					}
			    	
			    	if (file.notes[i][1] == -8) {
			    		if (countP8 == 0) {
							bmP8 = BitmapFactory.decodeResource(getResources(),
						    		R.drawable.p8);		    
							bhP8 = d * 2;
							bwP8 = (bmP8.getWidth() * bhP8) / bmP8.getHeight();		   
							bmP8 = Bitmap.createScaledBitmap(bmP8, bwP8, bhP8, false);
							countP8++;
						}
			    		canvas.drawBitmap(bmP8, x, h - 5 * d, null);
			    	}
			    	
			    	if (file.notes[i][1] == -16) {
			    		if (countP16 == 0) {
							bmP16 = BitmapFactory.decodeResource(getResources(),
						    		R.drawable.p16);		    
							bhP16 = d * 3;
							bwP16 = (bmP16.getWidth() * bhP16) / bmP16.getHeight();		   
							bmP16 = Bitmap.createScaledBitmap(bmP16, bwP16, bhP16, false);
							countP16++;
						}
			    		canvas.drawBitmap(bmP16, x, h - 5 * d, null);
			    	}
			    }
				   			
				//если это уже не затакт, накапливать длительность данного такта
				if (i > file.zatact - 1) {
				   	j += ((float) file.size2 / Math.abs(file.notes[i][1]));
				   	//если есть точка
				   	if ((file.notes[i][2] / 4) == 1)
				   		j += ((float) file.size2 / Math.abs(file.notes[i][1]) / 2);
				}

				//если затакт закончился на данной ноте, считать длительность такта заполненной
				if (i == file.zatact - 1)
				   	j = file.size1;
				//Log.d(TAG, "j: " + j);
				    
				//если такт закончился, отрисовать тактовую линию
				if (j >= file.size1){
					x += d * 3;
			    	canvas.drawLine(x, h - d * 2, x, h - d * 6, paint);
			    	j = 0;
			    	for (int t = 0; t < 23; t++) {
			    		signs[t][0] = 0;
			    		signs[t][1] = 0;
			    	}
			    }
		    }
		    
		    if (i == 0)
		    	coords[0] = (int) x;
		    
		    //отрисовать курсор
		    if (error == 0)
		    	paint.setColor(Color.GREEN);
		    else paint.setColor(Color.RED);
		    	canvas.drawLine(coords[current] + d / 2, 0, 
			    		coords[current] + d / 2, h, paint);

	    }
    }
	
	//@Override
	  public boolean onTouchEvent(MotionEvent event) {
				
			if (file != null) {
			    float x = event.getX();
			    
			    if (x <= coords[0])
			    	return super.onTouchEvent(event);
			    
			    if (event.getAction() == MotionEvent.ACTION_DOWN) {
			    	int i;
			    	for (i = 0; i < NoteFile.maxSize; i++) {
			    		if (file.notes[i][0] == 0)
			    			break;
			    		if (coords[i] > x) {
			    			current = i - 1;
			    			break;
			    		}
			    	}
			    	if (i > 0)
			    		current = i - 1;
			    	Log.d(TAG, "Current: " + current);
			    }
			}
			
			invalidate();
	    
		    return super.onTouchEvent(event);
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
	    		(width / spaceNum) * 6 * NoteFile.maxSize, height / 4);
	}

}