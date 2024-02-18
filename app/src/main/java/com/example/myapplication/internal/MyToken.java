package com.example.myapplication.internal;

/**
 * Token class to save extracted token from tokenizer.
 * Each token has its surface form saved in {@code _token}
 * and type saved in {@code _type} which is one of the predefined type in Type enum.
 * Rent: String
 * Bedroom: String
 * Location: String
 * Garage: String
 * Highlights: String
 *
 * @author Ruoxing Liao
 * @uid u6965141
 */
class MyToken {
    public enum Type {UNKNOWN, Rent, Bedroom, Location, Garage, Highlights}


    public String _token = "";
    public Type _type = Type.UNKNOWN;

    public MyToken(String _token, Type type) {
        this._token = _token;
        this._type = type;
    }

    public String token() {
        return _token;
    }

    public MyToken.Type type() {
        return _type;
    }
}

