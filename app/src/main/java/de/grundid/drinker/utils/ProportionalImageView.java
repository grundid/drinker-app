package de.grundid.drinker.utils;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;

public class ProportionalImageView extends ImageView {

	public ProportionalImageView(Context context) {
		super(context);
	}

	public ProportionalImageView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		setMeasuredDimension(getMeasuredWidth(), (getMeasuredWidth() * 9) / 16);
	}
}
