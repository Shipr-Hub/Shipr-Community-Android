package io.github.yhdesai.makertoolbox.ssh;

import android.content.Context;
import android.text.InputType;
import android.text.Layout;
import android.text.Selection;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputConnection;
import android.view.inputmethod.InputConnectionWrapper;

/**
 * EditText class for simulating an SSH shell terminal.
 * Main features of this class, compared to base EditText,
 * is the inability to delete (backspace) characters from
 * lines above the bottom line, as in a terminal.
 */
public class SshEditText extends android.support.v7.widget.AppCompatEditText {

    private String mlastInput;

    private String mPrompt = "";

    /**
     * First Constructor
     *
     * @param context
     */
    public SshEditText(Context context) {
        super(context);
        setup();
    }

    /**
     * Second Constructor
     *
     * @param context
     * @param attrs
     */
    public SshEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        setup();
    }

    /**
     * Third constructor
     *
     * @param context
     * @param attrs
     * @param defStyle
     */
    public SshEditText(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setup();
    }

    private void setup() {
        this.setRawInputType(InputType.TYPE_CLASS_TEXT);
        this.setImeOptions(EditorInfo.IME_ACTION_GO);
        this.setTextSize(12f);
    }

    @Override
    protected void onSelectionChanged(int s, int e) {
        //force selection to end
        setSelection(this.length());
    }

    /**
     * Returns the last line of user input.
     *
     * @return string input
     */
    public String getLastInput() {
        synchronized (this) {
            String rez = mlastInput;
            mlastInput = null;
            return rez;
        }
    }

    /**
     * Peeks the last line of user input.
     *
     * @return string input
     */
    public String peekLastInput() {
        synchronized (this) {
            return new String(mlastInput);
        }
    }


    /**
     * Sets the last input member variable to s.
     *
     * @param s
     */
    public void AddLastInput(String s) {
        synchronized (this) {
            if (mlastInput == null) {
                mlastInput = "";
            }
            mlastInput = s;
        }
    }


    /**
     * Gets the current line the cursor is on.
     *
     * @return Current line, -1 if none.
     */
    private int getCurrentCursorLine() {
        int selectionStart = Selection.getSelectionStart(this.getText());
        Layout layout = this.getLayout();

        if (!(selectionStart == -1)) {
            return layout.getLineForOffset(selectionStart);
        }

        return -1;
    }

    /**
     * Returns true if currently on a new line with no
     * input on the line.
     *
     * @return True if new line, false otherwise.
     */
    private boolean isNewLine() {

        int i = this.getText().toString().toCharArray().length;
        if (i == 0)
            return true;

        char s = this.getText().toString().toCharArray()[i - 1];
        return s == '\n' || s == '\r';

    }

    public synchronized String getPrompt() {
        return mPrompt;
    }

    /**
     * Sets the prompt string. If parameter is null, then the prompt parameter
     * will also be null.
     *
     * @param prompt
     */
    public synchronized void setPrompt(String prompt) {
        mPrompt = prompt;
    }

    @Override
    public InputConnection onCreateInputConnection(EditorInfo outAttrs) {
        return new SshConnectionWrapper(super.onCreateInputConnection(outAttrs),
                true);
    }

    private class SshConnectionWrapper extends InputConnectionWrapper {


        SshConnectionWrapper(InputConnection target, boolean mutable) {
            super(target, mutable);
        }

        @Override
        public boolean sendKeyEvent(KeyEvent event) {
            if (event.getKeyCode() == KeyEvent.KEYCODE_DEL) {
                if (isNewLine()) {
                    return false;

                } else if (getCurrentCursorLine() < getLineCount() - 1) {
                    return false;
                }
            }
            return super.sendKeyEvent(event);
        }

        @Override
        public boolean deleteSurroundingText(int beforeLength, int afterLength) {

            if (isNewLine()) {
                return false;

            } else if (getCurrentCursorLine() < getLineCount() - 1) {
                return false;
            } else {
                return super.deleteSurroundingText(beforeLength, afterLength);
            }
        }
    }
}
