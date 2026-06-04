package com.example.myapplication;

import android.content.Context;
import android.graphics.*;
import android.util.AttributeSet;
import android.view.View;

public class TrashCanView extends View {

    private int fillPercent = 50; // 기본값 50%
    private Paint paintFill = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Paint paintBorder = new Paint(Paint.ANTI_ALIAS_FLAG);

    public TrashCanView(Context context, AttributeSet attrs) {
        super(context, attrs);
        paintBorder.setStyle(Paint.Style.STROKE);
        paintBorder.setStrokeWidth(8f);
        paintBorder.setColor(Color.parseColor("#333333"));
        paintFill.setStyle(Paint.Style.FILL);
        paintFill.setPathEffect(new CornerPathEffect(15f));
    }

    // 퍼센트 설정 메서드 (외부에서 호출)
    public void setFillPercent(int percent) {
        if (percent < 0) percent = 0;
        if (percent > 100) percent = 100;

        // 5단계로 변환
        if (percent == 0) {
            this.fillPercent = 0;
        } else if (percent <= 40) {
            this.fillPercent = 25;
        } else if (percent <= 60) {
            this.fillPercent = 50;
        } else if (percent <= 99) {
            this.fillPercent = 75;
        } else {
            this.fillPercent = 100;
        }

        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int w = getWidth();
        int h = getHeight();

        // 휴지통 모양 좌표 (사다리꼴)
        float topLeft     = w * 0.15f;
        float topRight    = w * 0.85f;
        float bottomLeft  = w * 0.22f;
        float bottomRight = w * 0.78f;
        float top         = h * 0.05f;
        float bottom      = h * 0.95f;

        // 색상 결정 (퍼센트에 따라)
        if (fillPercent == 0) {
            paintFill.setColor(Color.parseColor("#B7E36A")); // 색 상관없음 (안 보임)
        } else if (fillPercent <= 50) {
            paintFill.setColor(Color.parseColor("#B7E36A")); // 연두
        } else if (fillPercent <= 75) {
            paintFill.setColor(Color.parseColor("#FFC73B")); // 주황
        } else {
            paintFill.setColor(Color.parseColor("#F44336")); // 빨강
        }

        // 채워진 높이 계산
        float fillHeight = (bottom - top) * (fillPercent / 100f);
        float fillTop = bottom - fillHeight;

        // 채우기 그리기 (사다리꼴 형태로)
        float ratio = (fillTop - top) / (bottom - top);
        float padding = 12f; // 테두리와의 간격
        float fillLeft  = topLeft  + (bottomLeft  - topLeft)  * ratio + padding;
        float fillRight = topRight + (bottomRight - topRight) * ratio - padding;

        Path fillPath = new Path();
        fillPath.moveTo(fillLeft, fillTop);
        fillPath.lineTo(fillRight, fillTop);
        fillPath.lineTo(bottomRight - padding, bottom - padding);
        fillPath.lineTo(bottomLeft  + padding, bottom - padding);
        fillPath.close();
        canvas.drawPath(fillPath, paintFill);

        // 휴지통 외곽선 그리기
        Path borderPath = new Path();
        borderPath.moveTo(topLeft, top);
        borderPath.lineTo(topRight, top);
        borderPath.lineTo(bottomRight, bottom);
        borderPath.lineTo(bottomLeft, bottom);
        borderPath.close();
        canvas.drawPath(borderPath, paintBorder);
    }
}