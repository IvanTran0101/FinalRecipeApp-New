package vn.edu.tdtu.anhminh.myapplication.UI.CustomView;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class PieChartView extends View {
    private Paint paint;
    private Paint textPaint; // New paint for text
    private RectF rectF;
    private float protein = 0, carbs = 0, fat = 0;

    private final int COLOR_PROTEIN = 0xFF4CAF50; // Green
    private final int COLOR_CARBS = 0xFFFFC107;   // Yellow
    private final int COLOR_FAT = 0xFFF44336;     // Red

    // Labels to display
    private final String LABEL_PROTEIN = "Protein";
    private final String LABEL_CARBS = "Carbs";
    private final String LABEL_FAT = "Fat";

    public PieChartView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        // 1. Existing paint for shapes
        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(5f);

        // 2. New paint setup for Text
        textPaint = new Paint();
        textPaint.setAntiAlias(true);
        textPaint.setColor(Color.DKGRAY); // Dark gray text against colored background
        textPaint.setTextSize(40f); // Set a reasonable text size (you might want to make this dynamic based on view size later)
        textPaint.setTextAlign(Paint.Align.CENTER); // Center text horizontally around the drawn point
        textPaint.setFakeBoldText(true); // Make bold to stand out

        rectF = new RectF();
    }

    public void setData(double p, double c, double f) {
        this.protein = (float) p;
        this.carbs = (float) c;
        this.fat = (float) f;
        invalidate(); // Redraw
    }

    @Override
    protected void onDraw(@NonNull Canvas canvas) {
        super.onDraw(canvas);

        float width = getWidth();
        float height = getHeight();
        // Ensure padding so the thick border doesn't get cut off
        float padding = 30f;
        float diameter = Math.min(width, height) - (padding * 2);
        float left = (width - diameter) / 2;
        float top = (height - diameter) / 2;

        rectF.set(left, top, left + diameter, top + diameter);

        // Calculate center points and radius for text positioning later
        float centerX = left + (diameter / 2);
        float centerY = top + (diameter / 2);
        float radius = diameter / 2;

        float total = protein + carbs + fat;

        // Draw standard circle style (Wireframe look) outer border
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(8f);
        paint.setColor(Color.BLACK);
        canvas.drawOval(rectF, paint);

        // If data exists, draw the slices and text inside
        if (total > 0) {
            paint.setStyle(Paint.Style.FILL); // Switch to fill for slices

            float angleProtein = (protein / total) * 360;
            float angleCarbs = (carbs / total) * 360;
            float angleFat = (fat / total) * 360;

            float currentAngle = -90; // Start at top

            // --- Draw Protein Slice & Label ---
            paint.setColor(COLOR_PROTEIN);
            canvas.drawArc(rectF, currentAngle, angleProtein, true, paint);
            drawLabel(canvas, LABEL_PROTEIN, currentAngle, angleProtein, centerX, centerY, radius);
            currentAngle += angleProtein;

            // --- Draw Carbs Slice & Label ---
            paint.setColor(COLOR_CARBS);
            canvas.drawArc(rectF, currentAngle, angleCarbs, true, paint);
            drawLabel(canvas, LABEL_CARBS, currentAngle, angleCarbs, centerX, centerY, radius);
            currentAngle += angleCarbs;

            // --- Draw Fat Slice & Label ---
            paint.setColor(COLOR_FAT);
            canvas.drawArc(rectF, currentAngle, angleFat, true, paint);
            drawLabel(canvas, LABEL_FAT, currentAngle, angleFat, centerX, centerY, radius);

            // Re-draw thinner wireframe border on top for clean look
            paint.setStyle(Paint.Style.STROKE);
            paint.setColor(Color.BLACK);
            paint.setStrokeWidth(5f);
            canvas.drawOval(rectF, paint);
        }
    }

    /**
     * Helper method to calculate position and draw the text label.
     */
    private void drawLabel(Canvas canvas, String label, float startAngle, float sweepAngle, float centerX, float centerY, float radius) {
        // If the slice is too small, don't draw text, it will look messy.
        // 15 degrees is an arbitrary threshold, adjust as needed.
        if (sweepAngle < 15) {
            return;
        }

        // 1. Find the middle angle of the slice
        float middleAngle = startAngle + (sweepAngle / 2);

        // 2. Determine how far out from center to place text. 0.65 (65%) places it nicely within the slice.
        float textRadius = radius * 0.65f;

        // 3. Convert polar coordinates (angle & radius) to Cartesian coordinates (x & y)
        // Math.cos/sin expect radians, so convert the degrees.
        float x = (float) (centerX + textRadius * Math.cos(Math.toRadians(middleAngle)));
        float y = (float) (centerY + textRadius * Math.sin(Math.toRadians(middleAngle)));

        // Adjust Y slightly down to center text vertically based on its size (heuristic)
        float verticalAdjustment = textPaint.getTextSize() / 3;

        // 4. Draw the text
        canvas.drawText(label, x, y + verticalAdjustment, textPaint);
    }
}