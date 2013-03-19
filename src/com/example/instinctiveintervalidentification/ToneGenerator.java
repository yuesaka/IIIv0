package com.example.instinctiveintervalidentification;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.util.Log;

public class ToneGenerator {
	private static final String TAG = "ToneGenerator";
	private Generator mGenerator;
	private ScheduledExecutorService mExecutor;

	public ToneGenerator() {

		Log.v(TAG, "Creating ToneGenerator...");

		// mExecutor = Executors.newSingleThreadScheduledExecutor();
		mExecutor = Executors.newScheduledThreadPool(3);
	}

	public void playToneAsync(double freq, int duration) {
		Log.v(TAG, "Starting the Asynchronous Generator thread...");
		mGenerator = new Generator(freq, duration);
		mExecutor.execute(mGenerator);

	}

	public void playTone(double freq, int duration) {
		Log.v(TAG, "Starting the Synchronous Generator thread...");
		Log.v(TAG, "Duration: " + duration);
		mGenerator = new Generator(freq, duration);
		// mExecutor.execute(mGenerator);
		// new Thread(mGenerator).start();
		mGenerator.run();

	}

	/*

	 */

	private class Generator implements Runnable {

		// private AudioTrack mAudioTrack;
		private int duration; // in seconds
		private final int sampleRate = 44100; // 44100 sample rate
		private int numSamples;
		private double sample[];
		private double freqOfTone; // in hz
		private byte generatedSnd[];

		public Generator(double freq, int duration_) {
			duration = duration_;
			freqOfTone = freq;
			numSamples = (duration * sampleRate);
			sample = new double[numSamples];
			generatedSnd = new byte[2 * numSamples];
		}

		public void run() {
			genTone();
			playSound();
		}

		private void genTone() {
			/*
			 * 	case WAVEFORM_SINE:
				fValue = (float) Math.sin(fPeriodPosition * 2.0 * Math.PI);
				break;

				case WAVEFORM_SQUARE:
				fValue = (fPeriodPosition < 0.5F) ? 1.0F : -1.0F;
			 */
			
			// fill out the array
			for (int i = 0; i < numSamples; ++i) {
				double fPeriodPosition = i /(sampleRate/freqOfTone);
				//sample[i] = Math.sin(2 * Math.PI * fPeriodPosition);
				sample[i] = (fPeriodPosition < 0.5) ? 1 : -1;
			}

			// convert to 16bit PCM sound array
			// assume sample buffer is normalized
			int idx = 0;
			for (final double dVal : sample) {
				// scale to maximum amplitude
				final short val = (short) ((dVal * 32767));
				// in 16-bit wav PCM, first byte is the low order byte
				generatedSnd[idx++] = (byte) (val & 0x00ff);
				generatedSnd[idx++] = (byte) ((val & 0xff00) >>> 8);
			}
		}

		private void playSound() {
			final AudioTrack audioTrack = new AudioTrack(
					AudioManager.STREAM_MUSIC, sampleRate,
					AudioFormat.CHANNEL_CONFIGURATION_MONO,
					AudioFormat.ENCODING_PCM_16BIT, generatedSnd.length,
					AudioTrack.MODE_STREAM);
			audioTrack.write(generatedSnd, 0, generatedSnd.length);
			audioTrack.play();
		}

	}
}
