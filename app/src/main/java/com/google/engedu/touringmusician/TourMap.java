/* Copyright 2016 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.engedu.touringmusician;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Iterator;

public class TourMap extends View {

    private Bitmap mapImage;
    private CircularLinkedList list = new CircularLinkedList();
    private String insertMode = "Add";

    public TourMap(Context context) {
        super(context);
        mapImage = BitmapFactory.decodeResource(
                getResources(),
                R.drawable.map);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawBitmap(mapImage, 0, 0, null);
        Paint pointPaint = new Paint();
        pointPaint.setColor(Color.BLACK);
        Paint linePaint = new Paint();
        linePaint.setColor(Color.MAGENTA);
        linePaint.setStrokeWidth(5);
        Point prev = null;
        for (Point p : list) {
            Log.d("onDraw", "drawCircle");
            canvas.drawCircle(p.x, p.y, 20, pointPaint);
            if (prev != null) {
                Log.d("onDraw", "drawLine");
                canvas.drawLine(prev.x, prev.y, p.x, p.y, linePaint);
            }
            prev = p;
        }
        /**
         **
         **  YOUR CODE GOES HERE
         **
         **/
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                Log.d("onTouchEvent", "New point");
                Point p = new Point((int) event.getX(), (int)event.getY());
                if (insertMode.equals("Closest")) {
                    list.insertNearest(p);
                } else if (insertMode.equals("Smallest")) {
                    list.insertSmallest(p);
                } else {
                    Log.d("onTouchEvent", "insertBeginning called");
                    list.insertBeginning(p);
                }
                TextView message = (TextView) ((Activity) getContext()).findViewById(R.id.game_status);
                Log.d("onTouchEvent", "Message " + message);
                if (message != null) {
                    Log.d("onTouchEvent", "Message text updated");
                    message.setText(String.format("Tour length is now %.2f", list.totalDistance()));
                }
                Log.d("onTouchEvent", "Invalidate");
                invalidate();
                return true;
        }
        return super.onTouchEvent(event);
    }

    public void reset() {
        list.reset();
        invalidate();
    }

    public void setInsertMode(String mode) {
        insertMode = mode;
    }
}
