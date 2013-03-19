package com.example.instinctiveintervalidentification;

import android.os.Handler;

public class IntervalPlayer {
	private static final String TAG = "IntervalPlayer";
	private static ToneGenerator mToneGenerator;
	private int noteLength;// in ms

	public IntervalPlayer(int noteLength_) {
		mToneGenerator = new ToneGenerator();
		noteLength = noteLength_;
	}

	public void playInterval(final Note baseNote, final int interval,
			final int direction) {

		// new Thread(new Runnable() {
		// public void run() {
		// Log.v(TAG, "before first tone!");
		// mToneGenerator.playTone(baseNote.getFrequency(), noteLength);
		// Log.v(TAG, "after first tone!");
		//
		// if (direction == 0) {// Up
		// mToneGenerator.playTone(
		// Note.fromInt(baseNote.getValue() + interval)
		// .getFrequency(), noteLength);
		// } else {// Down
		// mToneGenerator.playTone(
		// Note.fromInt(baseNote.getValue() - interval)
		// .getFrequency(), noteLength);
		// }
		// }
		// }).start();

		mToneGenerator.playTone(baseNote.getFrequency(), noteLength);

		Handler handler = new Handler();
		handler.postDelayed(new Runnable() {
			public void run() {
				if (direction == 0) {// Up
					mToneGenerator.playTone(
							Note.fromInt(baseNote.getValue() + interval)
									.getFrequency(), noteLength);
				} else {// Down
					mToneGenerator.playTone(
							Note.fromInt(baseNote.getValue() - interval)
									.getFrequency(), noteLength);
				}
			}
		}, 1001);

	}

}
