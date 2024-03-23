package net.branium.view.auth;

import android.text.method.PasswordTransformationMethod;
import android.view.View;

import androidx.annotation.NonNull;

public class PasswordMaskTransformation extends PasswordTransformationMethod {
    // tạo đối tượng singleton
    private static final PasswordMaskTransformation instance = new PasswordMaskTransformation();

    private PasswordMaskTransformation() {
    } // ngăn không cho tạo object mới nào khác

    @Override
    public CharSequence getTransformation(CharSequence source, View view) {
        return new PasswordCharSequence(source);
    }

    private class PasswordCharSequence implements CharSequence {
        private final CharSequence source;

        public PasswordCharSequence(CharSequence source) {
            this.source = source;
        }

        @Override
        public int length() {
            return source.length();
        }

        @Override
        public char charAt(int i) {
            return '*';
        }

        @NonNull
        @Override
        public CharSequence subSequence(int start, int end) {
            return source.subSequence(start, end);
        }
    }

    // trả về object duy nhất của lớp mặt nạ
    public static PasswordMaskTransformation getInstance() {
        return instance;
    }
}
