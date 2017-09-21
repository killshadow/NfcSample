package com.ppy.nfclib;

import android.app.Activity;
import android.content.Intent;
import android.nfc.NfcAdapter;
import android.nfc.Tag;

import java.io.IOException;

/**
 * Created by ZP on 2017/9/20.
 * NFC管理类，外层调用只需要与本类交互
 */

public class NfcCardReaderManager implements INfcCardReader{

    private Activity mActivity;
    private CardReader mCardReader;
    private boolean enableSound = true;


    private NfcCardReaderManager(Builder builder) {
        mActivity = builder.mActivity;
        mCardReader = builder.mCardReader;
        if (builder.mCardReader == null) {
            mCardReader = CardReaderFactory.productCardReader(mActivity);
        }
        enableSound = builder.enableSound;
        mCardReader.enablePlatformSound(enableSound);
    }


    @Override
    public void onStart(Activity activity) {

    }

    @Override
    public void onResume() {
        mCardReader.enableCardReader();
    }

    @Override
    public void onPause() {
        mCardReader.disableCardReader();
    }

    @Override
    public void onDestroy() {
        if (mCardReader != null) {
            mCardReader = null;
            mActivity = null;
        }
    }
    @Override
    public void onNewIntent(Intent intent) {
        if (intent != null) {
            Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
            mCardReader.dispatchTag(tag);
        }
    }

    @Override
    public String sendData(byte[] data) throws IOException {
        return Util.ByteArrayToHexString(mCardReader.transceive(data));
    }

    @Override
    public String sendData(String hexData) throws IOException {
        return sendData(Util.HexStringToByteArray(hexData));
    }

    @Override
    public byte[] tranceive(byte[] data) throws IOException {
        return mCardReader.transceive(data);
    }

    @Override
    public byte[] tranceive(String hexData) throws IOException {
        return tranceive(Util.HexStringToByteArray(hexData));
    }

    public void setOnCardOperatorListener(CardOperatorListener listener) {
        mCardReader.setOnCardOperatorListener(listener);
    }

    @Override
    public boolean isCardConnected() {
        return mCardReader.isCardConnected();
    }

    public static final class Builder {
        private Activity mActivity;
        private CardReader mCardReader;
        private boolean enableSound;

        public Builder() {
        }

        public Builder(NfcCardReaderManager copy) {
            this.mActivity = copy.mActivity;
            this.mCardReader = copy.mCardReader;
            this.enableSound = copy.enableSound;
        }

        public Builder mActivity(Activity val) {
            mActivity = val;
            return this;
        }

        public Builder mCardReader(CardReader val) {
            mCardReader = val;
            return this;
        }

        public Builder enableSound(boolean val) {
            enableSound = val;
            return this;
        }

        public NfcCardReaderManager build() {
            return new NfcCardReaderManager(this);
        }
    }
}