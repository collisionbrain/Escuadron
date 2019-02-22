package escuadron_cannabico.registroseguimiento.adapter;

import android.text.InputFilter;
import android.text.Spanned;

/**
 * Created by hugo on 18/08/18.
 */

public class InpuTextFilters {

    public static InputFilter filtroSoloLetras = new InputFilter() {
        public CharSequence filter(CharSequence source, int start, int end,
                                   Spanned dest, int dstart, int dend) {
            for (int i = start; i < end; i++) {
                String st = "" + source.charAt(i);
                if (!Character.isLetter(source.charAt(i)) && !(st.equals(" "))) {

                    return "";
                }
            }
            return null;
        }
    };

    public static InputFilter filtroLetrasNumeros = new InputFilter() {
        public CharSequence filter(CharSequence source, int start, int end,
                                   Spanned dest, int dstart, int dend) {
            for (int i = start; i < end; i++) {
                String st = "" + source.charAt(i);
                if (!Character.isLetterOrDigit(source.charAt(i))) {

                    return "";
                }
            }
            return null;
        }
    };
    public static InputFilter filtroLetrasNumerosEspacio = new InputFilter() {
        public CharSequence filter(CharSequence source, int start, int end,
                                   Spanned dest, int dstart, int dend) {
            for (int i = start; i < end; i++) {
                String st = "" + source.charAt(i);
                if (!Character.isLetterOrDigit(source.charAt(i)) && !(st.equals(" "))) {

                    return "";
                }
            }
            return null;
        }
    };
    public static InputFilter filtroCorreo = new InputFilter() {
        public CharSequence filter(CharSequence source, int start, int end,
                                   Spanned dest, int dstart, int dend) {
            for (int i = start; i < end; i++) {
                String st = "" + source.charAt(i);
                if (!Character.isLetterOrDigit(source.charAt(i)) && !(st.equals("@") || st.equals("_") || st.equals("-") || st.equals("."))) {

                    return "";
                }
            }
            return null;
        }
    };
    public static InputFilter filtroNumeros = new InputFilter() {
        public CharSequence filter(CharSequence source, int start, int end,
                                   Spanned dest, int dstart, int dend) {
            for (int i = start; i < end; i++) {
                String st = "" + source.charAt(i);
                if (!Character.isDigit(source.charAt(i)) && !(st.equals("-"))) {

                    return "";
                }
            }
            return null;
        }
    };

}


