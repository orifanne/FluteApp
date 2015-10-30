package ori.fluteapp;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

public class TunerDrawPanel extends View {
	
	private int pos = 1;
	private int diez = 0;
	private double part = 0.25;
	private Bitmap bm;
	private Bitmap bmDz;
	
	public void setPos(int p) {
		if (pos < 1)
			pos = 1;
		if (pos > 7)
			pos = 7;
		else pos = p;
	}
	
	public void setDies(int p) {
		if ((p == 1) || (p == 0))
			diez = p;
		else diez = 0;
	}
	
	public int getPos() {
		return pos;
	}

	public TunerDrawPanel(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	public TunerDrawPanel(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	public TunerDrawPanel(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		// TODO Auto-generated constructor stub
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
	    paint.setStrokeWidth(5);
	    
	    int w = getWidth();
	    int h = getHeight();
	    int d = (int) h / 8;
	        
	    canvas.drawLine(0, d * 2, w, d * 2, paint);
	    canvas.drawLine(0, d * 3, w, d * 3, paint);
	    canvas.drawLine(0, d * 4, w, d * 4, paint);
	    canvas.drawLine(0, d * 5, w, d * 5, paint);
	    canvas.drawLine(0, d * 6, w, d * 6, paint);

	    bm = BitmapFactory.decodeResource(getResources(),
	    		R.drawable.four);
	    
	    int bh = (int)(d / part);
	    int bw = (bm.getWidth() * bh) / bm.getHeight();
	   
	    bm = Bitmap.createScaledBitmap(bm, bw, bh, false);
	    
	    bmDz = BitmapFactory.decodeResource(getResources(),
	    		R.drawable.diez);		    
		int bhDz = (int)(d * 1.8);
		int bwDz = (bmDz.getWidth() * bhDz) / bmDz.getHeight();		   
		bmDz = Bitmap.createScaledBitmap(bmDz, bwDz, bhDz, false);
	    
	    float x = w / 2 - bw / 2;
	    float y = (float) (h - 0.5 * pos * d - bh);
	    canvas.drawBitmap(bm, x, y, null);
	    
	    if (diez == 1)
	    	canvas.drawBitmap(bmDz, (float) (x + 1.5 * d), (float) (h - 0.5 * d * pos - bhDz), null);
	    
	    if (pos == 1)
	    	canvas.drawLine((float) (x - bw * 0.3), (float) (h - 0.5 * pos * d - 0.5 * d), 
	    			(float) (x + bw * 1.3), (float) (h - 0.5 * pos * d - 0.5 * d), paint);
    }
	
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
	    super.onMeasure(widthMeasureSpec, heightMeasureSpec);

	    int parentWidth = MeasureSpec.getSize(widthMeasureSpec);
	    int parentHeight = MeasureSpec.getSize(heightMeasureSpec);
	    this.setMeasuredDimension(
	            parentWidth / 2, parentHeight / 2);
	}

}