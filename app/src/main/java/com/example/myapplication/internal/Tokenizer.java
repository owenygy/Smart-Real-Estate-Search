package com.example.myapplication.internal;

public class Tokenizer {
    public String _buffer;        //save text
    public MyToken currentToken;    //save token extracted from next()

    public Tokenizer(String _buffer, MyToken currentToken) {
        this._buffer = _buffer;
        this.currentToken = currentToken;
    }

    public void next() {
        String[] buffer = _buffer.split(";");
        if (_buffer.isEmpty()) {
            currentToken = null;    // if there's no string left, set currentToken null and return
            return;
        }
        String term = buffer[0].trim().toLowerCase();
//        term.trim();
//        term.toLowerCase();
        if (term.contains("rent")) {
            String range = "";
            if (term.contains(">=") || term.contains("greater than or equal to")) {
                range = ">=";
            } else if (term.contains("<=") || term.contains("less than or equal to")) {
                range = "<=";
            } else if (term.contains("=") || term.contains("equal")) {
                range = "=";
            } else if (term.contains(">") || term.contains("greater than")) {
                range = ">";
            } else if (term.contains("<") || term.contains("less than")) {
                range = "<";
            }

            Integer firstIntIndex = null;
            for (int i = 0; i < term.length(); i++) {
                if (Character.isDigit(term.charAt(i))) {
                    firstIntIndex = i;
                    break;
                }
            }
            StringBuilder toString = new StringBuilder(Character.toString(term.charAt(firstIntIndex)));
            for (int j = firstIntIndex + 1; j < term.length() && Character.isDigit(term.charAt(j)); j++) {
                toString.append(term.charAt(j));
            }
            String result = "rent" + range + toString;
            currentToken = new MyToken(result, MyToken.Type.Rent);
        }
        if (term.contains("bedroom")) {
            Integer firstIntIndex = null;
            for (int i = 0; i < term.length(); i++) {
                if (Character.isDigit(term.charAt(i))) {
                    firstIntIndex = i;
                    break;
                }
            }
            StringBuilder toString = new StringBuilder(Character.toString(term.charAt(firstIntIndex)));
            for (int j = firstIntIndex + 1; j < term.length() && Character.isDigit(term.charAt(j)); j++) {
                toString.append(term.charAt(j));
            }
            String result = "bedroom" + "=" + toString;
            currentToken = new MyToken(result, MyToken.Type.Bedroom);
        }
        if (term.contains("location")) {
            String result = "";
            if (term.contains("=")) {
                result = "location=" + term.substring(11);
            } else if (term.contains("is")) {
                result = "location=" + term.substring(12);
            } else if (term.contains("are")) {
                result = "location=" + term.substring(13);
            }
            currentToken = new MyToken(result, MyToken.Type.Highlights);
        }
        if (term.contains("garbage")) {
            Integer firstIntIndex = null;
            for (int i = 0; i < term.length(); i++) {
                if (Character.isDigit(term.charAt(i))) {
                    firstIntIndex = i;
                    break;
                }
            }
            StringBuilder toString = new StringBuilder(Character.toString(term.charAt(firstIntIndex)));
            for (int j = firstIntIndex + 1; j < term.length() && Character.isDigit(term.charAt(j)); j++) {
                toString.append(term.charAt(j));
            }
            String result = "garbage" + "=" + toString;
            currentToken = new MyToken(result, MyToken.Type.Garage);
        }
        if (term.contains("highlights")) {
            String result = "";
            if (term.contains("=")) {
                result = "highlights=" + term.substring(13);
            } else if (term.contains("is")) {
                result = "highlights=" + term.substring(14);
            } else if (term.contains("are")) {
                result = "highlights=" + term.substring(15);
            }
            currentToken = new MyToken(result, MyToken.Type.Highlights);
        }
        int length = term.length() + 1;
        if (buffer.length > 1) {
            _buffer = _buffer.substring(length);
        } else {
            _buffer = "";
        }
    }

    public MyToken current() {
        return this.currentToken;
    }

    public boolean hasNext() {
//        return _buffer != "";
        return !_buffer.equals("");
    }
}

