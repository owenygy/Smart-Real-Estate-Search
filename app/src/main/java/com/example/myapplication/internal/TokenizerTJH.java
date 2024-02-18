package com.example.myapplication.internal;

public class TokenizerTJH {

    private String buffer;

    public TokenizerTJH() {
        setBuffer("");
    }

    public TokenizerTJH(String buffer) {
        setBuffer(buffer);
    }

    public void setBuffer(String buffer) {
        this.buffer = buffer;
    }

    // Method to extract next getToken
    private TokenResult nextToken() {
        buffer = buffer.trim(); // Remove whitespace and convert case
        String testBuffer = buffer.toLowerCase(); // Test on this string

        if (buffer.isEmpty()) {
            return null;
        }

        char firstChar = testBuffer.charAt(0);
        if (Character.isDigit(firstChar)) {
            // Case 1: 0
            if (firstChar == 0) {
                return new TokenResult("0", 1, TokenTJH.Type.Lit);
            }

            // Case 2:
            StringBuilder builder = new StringBuilder();
            int count;
            for (count = 0; ; count++) {
                if (count >= testBuffer.length()) break; // In case of NPE
                char c = testBuffer.charAt(count);
                if (Character.isDigit(c)) {
                    builder.append(c);
                } else {
                    break;
                }
            }
            String result = builder.toString();
            return new TokenResult(result, count, TokenTJH.Type.Lit);
        }

        // Attributes
        String x;
        if (testBuffer.startsWith(x = "rent")) {
            return new TokenResult(x, x.length(), TokenTJH.Type.Rent);
        }
        if (testBuffer.startsWith(x = "bedroom") || testBuffer.startsWith(x = "bed")) {
            return new TokenResult(x, x.length(), TokenTJH.Type.Bedroom);
        }
        if (testBuffer.startsWith(x = "location") || testBuffer.startsWith(x = "loc")) {
            return new TokenResult(x, x.length(), TokenTJH.Type.Location);
        }
        if (testBuffer.startsWith(x = "garage") || testBuffer.startsWith(x = "grg")) {
            return new TokenResult(x, x.length(), TokenTJH.Type.Garage);
        }
        if (testBuffer.startsWith(x = "highlights") || testBuffer.startsWith(x = "hl")) {
            return new TokenResult(x, x.length(), TokenTJH.Type.Highlights);
        }

        // Quoted string
        if (testBuffer.startsWith(x = "\"")) {
            int endIndex = testBuffer.indexOf('"', 1);
            if (endIndex == -1) {
                return null; // unbalanced quotes, just return null to propagate the exception
            }
            String quote = buffer.substring(1, endIndex);
            return new TokenResult(quote, quote.length() + 2, TokenTJH.Type.Lit);
        }

        // Operations
        if (testBuffer.startsWith(x = "less than or equal to") ||
                testBuffer.startsWith(x = "less than") ||
                testBuffer.startsWith(x = "<=") ||
                testBuffer.startsWith(x = "<")) {
            return new TokenResult(x, x.length(), TokenTJH.Type.LT);
        }
        if (testBuffer.startsWith(x = "greater than or equal to") ||
                testBuffer.startsWith(x = "greater than") ||
                testBuffer.startsWith(x = ">=") ||
                testBuffer.startsWith(x = ">")) {
            return new TokenResult(x, x.length(), TokenTJH.Type.GT);
        }
        if (testBuffer.startsWith(x = "are") ||
                testBuffer.startsWith(x = "is") ||
                testBuffer.startsWith(x = "=")) {
            return new TokenResult(x, x.length(), TokenTJH.Type.EQ);
        }

        return null;

    }

    // Return the next token in the buffer (without removing it)
    public TokenTJH next() {
        TokenResult tokenResult = nextToken();
        if (tokenResult != null) {
            return new TokenTJH(tokenResult.data, tokenResult.type);
        } else {
            return null;
        }

    }

    // Return the next token and remove it from the buffer
    public TokenTJH takeNext() throws Exception {
        TokenResult nextResult = nextToken();
        if (nextResult == null) {
            throw new Exception("Next token is null");
        }
        String data = nextResult.data;
        TokenTJH.Type type = nextResult.type;
        int length = nextResult.length;

        buffer = buffer.substring(length);
        return new TokenTJH(data, type);
    }

    // Return whether is another token to parse in the buffer
    public boolean hasNext() {
        return next() != null;
    }

    // Inner class to encapsulate token and buffer information
    private static class TokenResult {

        final String data;
        final int length;
        final TokenTJH.Type type;

        TokenResult(String data, int length, TokenTJH.Type type) {
            this.data = data;
            this.length = length;
            this.type = type;
        }

    }

}