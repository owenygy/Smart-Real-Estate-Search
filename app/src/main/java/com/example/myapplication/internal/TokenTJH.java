package com.example.myapplication.internal;

/**
 * Token class to save extracted token from tokenizer. Each token has its
 * surface form saved in {@code token} and type saved in {@code type} which is
 * one of the predefined type in Type enum.
 * <p>
 * Rent: String Bedroom: String Location: String Garage: String Highlights:
 * String
 *
 * @author Ruoxing Liao
 * @uid u6965141
 */
public class TokenTJH {

    public String token;
    public Type type;

    public TokenTJH(String token, Type type) {
        this.token = token;
        this.type = type;
    }

    public String token() {
        return token;
    }

    public Type type() {
        return type;
    }

    public enum Type {
        UNKNOWN,

        // Features
        Rent,
        Bedroom,
        Location,
        Garage,
        Highlights,

        // Operation
        GT,
        EQ,
        LT,

        // String / Digits
        Lit;

        public DetailsDB.Feature parseFeature() {
            switch (this) {
                case Rent:
                    return DetailsDB.Feature.RENT;
                case Bedroom:
                    return DetailsDB.Feature.BEDROOM;
                case Garage:
                    return DetailsDB.Feature.GARAGE;
                case Location:
                    return DetailsDB.Feature.LOCATION;
                case Highlights:
                    return DetailsDB.Feature.HIGHLIGHTS;
                default:
                    throw new IllegalArgumentException();
            }
        }

        public DetailsDB.Operation parseOperation() {
            switch (this) {
                case EQ:
                    return DetailsDB.Operation.EQ;
                case GT:
                    return DetailsDB.Operation.GT;
                case LT:
                    return DetailsDB.Operation.LT;
                default:
                    throw new IllegalArgumentException();
            }
        }
    }

}

