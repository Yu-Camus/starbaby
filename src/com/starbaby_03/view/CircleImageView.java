package com.starbaby_03.view;

//import android.content.Context;  
//import android.graphics.Bitmap;  
//import android.graphics.Bitmap.Config;  
//import android.graphics.Canvas;  
//import android.graphics.Paint;  
//import android.graphics.PorterDuff.Mode;  
//import android.graphics.PorterDuffXfermode;  
//import android.graphics.Rect;  
//import android.graphics.drawable.BitmapDrawable;  
//import android.graphics.drawable.Drawable;  
//import android.util.AttributeSet;  
//import android.widget.ImageView;  
//  
///** 
// * Ô²ÐÎµÄImageview 
// * @since 2012-11-02 
// *  
// * @author bingyang.djj 
// *  
// */  
//public class CircleImageView extends ImageView {  
//    private Paint paint = new Paint();  
//  
//    public CircleImageView(Context context) {  
//        super(context);  
//    }  
//  
//    public CircleImageView(Context context, AttributeSet attrs) {  
//        super(context, attrs);  
//    }  
//  
//    public CircleImageView(Context context, AttributeSet attrs, int defStyle) {  
//        super(context, attrs, defStyle);  
//    }  
//  
//    @Override  
//    protected void onDraw(Canvas canvas) {  
//  
//        Drawable drawable = getDrawable();  
//        if (null != drawable) {  
//            Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();  
//            Bitmap b = toRoundCorner(bitmap, 14);  
//            final Rect rect = new Rect(0, 0, b.getWidth(), b.getHeight());  
//            paint.reset();  
//            canvas.drawBitmap(b, rect, rect, paint);  
//  
//        } else {  
//            super.onDraw(canvas);  
//        }  
//    }  
//  
//    private Bitmap toRoundCorner(Bitmap bitmap, int pixels) {  
//        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),  
//                bitmap.getHeight(), Config.ARGB_8888);  
//        Canvas canvas = new Canvas(output);  
//          
//        final int color = 0xff424242;  
//        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());  
//        paint.setAntiAlias(true);  
//        canvas.drawARGB(0, 0, 0, 0);  
//        paint.setColor(color);  
//        int x = bitmap.getWidth();  
//        canvas.drawCircle(x / 2, x / 2, x / 2, paint);  
//        paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));  
//        canvas.drawBitmap(bitmap, rect, rect, paint);  
//        return output;  
//    }  
//}  
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.Path;
import android.util.AttributeSet;
import android.widget.ImageView;

public class CircleImageView extends ImageView {

	public CircleImageView(Context context) {
		super(context);
	}
	
	public CircleImageView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	
	public CircleImageView(Context context, AttributeSet attrs,
			int defStyle) {
		super(context, attrs, defStyle);
	}

	@Override
	protected void onDraw(Canvas canvas) {		
		Path clipPath = new Path();
//		
		int w = this.getWidth();
		int h = this.getHeight();
		clipPath.addCircle(w/2, h/2, w/2, Path.Direction.CW);   		
		canvas.clipPath(clipPath);	
		canvas.setDrawFilter(new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG|Paint.FILTER_BITMAP_FLAG));
		super.onDraw(canvas);
	}
}
