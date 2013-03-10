package org.bejug.tictactoe.client;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

/**
 *
 */
public class TicTacToeView extends View {

    public static final int LINE_STROKE_WIDTH = 5;
    public static final int X_O_STROKE_WIDTH = 10;
    private Paint mPaint;

    public TicTacToeView(Context context) {
        this(context, null);
    }

    public TicTacToeView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TicTacToeView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        init();
    }

    private void init() {
        mPaint = new Paint();
        mPaint.setAntiAlias(true);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        mPaint.setColor(Color.BLACK);
        canvas.drawRect(0, 0, canvas.getWidth(), canvas.getHeight(), mPaint);

        final int size = getBoardSize();
        final int horizontalOffset = getHorizontalOffset();
        final int verticalOffset = getVerticalOffset();
        final int cellSize = getCellSize();

        mPaint.setColor(Color.DKGRAY);
        mPaint.setStrokeWidth(LINE_STROKE_WIDTH);
        for (int columnLine = 0; columnLine < 2; columnLine++) {
            int cx = horizontalOffset + ( (columnLine+1) * cellSize );
            canvas.drawLine(cx, verticalOffset, cx, verticalOffset + size, mPaint);
        }
        for (int rowLine = 0; rowLine < 2; rowLine++) {
            int cy = verticalOffset + ( (rowLine + 1) *  cellSize);
            canvas.drawLine(horizontalOffset, cy, horizontalOffset + size, cy, mPaint);
        }
        int inset = (int) (cellSize * 0.1);

        mPaint.setColor(Color.WHITE);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(X_O_STROKE_WIDTH);
    }

    private int getBoardSize() {
        final int sizeToCheck = (getWidth() < getHeight()) ? getWidth() : getHeight();
        return (int) (sizeToCheck * 0.8);
    }

    private int getHorizontalOffset() {
        return ( getWidth() / 2 ) - ( getBoardSize() / 2 );
    }

    private int getVerticalOffset() {
        return ( getHeight() / 2 ) - ( getBoardSize() / 2 );
    }

    private int getCellSize() {
        return ( getBoardSize() / 3 );
    }

    /*

    protected void onDraw(Canvas canvas) {
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setColor(Color.BLACK);
        canvas.drawRect(0,0,canvas.getWidth(),canvas.getHeight(), paint);

        int size = getSize();
        int offsetX = getOffsetX();
        int offsetY = getOffsetY();
        int lineSize = getLineSize();

        paint.setColor(Color.DKGRAY);
        paint.setStrokeWidth( 5 );
        for( int col = 0; col < 2; col++ ) {
            int cx = offsetX + ( ( col + 1 ) * lineSize );
            canvas.drawLine(cx, offsetY, cx, offsetY + size, paint);
        }
        for( int row = 0; row < 2; row++ ) {
            int cy = offsetY + ( ( row + 1 ) * lineSize );
            canvas.drawLine(offsetX, cy, offsetX + size, cy, paint);
        }
        int inset = (int) ( (float)lineSize * 0.1 );

        paint.setColor(Color.WHITE);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth( 10 );
        for( int x = 0; x < 3; x++ ) {
            for( int y = 0; y < 3; y++ ) {
                Rect r = new Rect( ( offsetX + ( x * lineSize ) ) + inset,
                        ( offsetY + ( y * lineSize ) ) + inset,
                        ( ( offsetX + ( x * lineSize ) ) + lineSize ) - inset,
                        ( ( offsetY + ( y * lineSize ) ) + lineSize ) - inset );
                if ( GameService.getInstance().positions[ x ][ y ] == 1 ) {
                    canvas.drawCircle( ( r.right + r.left ) / 2,
                            ( r.bottom + r.top ) / 2,
                            ( r.right - r.left ) / 2, paint);
                }
                if ( GameService.getInstance().positions[ x ][ y ] == 2 ) {
                    canvas.drawLine( r.left, r.top, r.right, r.bottom, paint);
                    canvas.drawLine( r.left, r.bottom, r.right, r.top, paint);
                }
            }
        }
    }*/
}
