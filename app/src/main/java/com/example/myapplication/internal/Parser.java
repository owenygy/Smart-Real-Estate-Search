package com.example.myapplication.internal;

import java.util.ArrayList;

public class Parser {
    Tokenizer myTokenizer;
    public ArrayList<String> arrayList;

    public Parser(Tokenizer myTokenizer, ArrayList<String> arrayList) {
        this.myTokenizer = myTokenizer;
        this.arrayList = arrayList;
    }

    public void parse() {
        while (myTokenizer.hasNext()) {
            myTokenizer.next();
            arrayList.add(myTokenizer.currentToken._token);
        }
    }
}
