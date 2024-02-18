package com.example.myapplication;

import com.example.myapplication.internal.TokenTJH;
import com.example.myapplication.internal.TokenizerTJH;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class TokenizerTest {
    private static TokenizerTJH tokenizer ;

    @Test(timeout=1000)
    public void testTokenType() {
        tokenizer = new TokenizerTJH();
        tokenizer.setBuffer("rent=200");

        //check the type of the first token
        assertEquals("wrong token type", TokenTJH.Type.Rent,tokenizer.next().type);

    }
    @Test(timeout=1000)
    public void testTokenValue() {
        tokenizer = new TokenizerTJH();
        tokenizer.setBuffer("rent=200");

        //check the type of the first token
        assertEquals("wrong token type", "rent",tokenizer.next().token);
    }

    @Test(timeout=1000)
    public void testTokenBoolean() throws Exception {
        tokenizer = new TokenizerTJH();
        tokenizer.setBuffer("rent=300");
        tokenizer.takeNext();

        //check the type of the first token
        assertEquals("wrong token type", "=",tokenizer.takeNext().token);
    }

    @Test(timeout=1000)
    public void testTokenValue2() throws Exception {
        tokenizer = new TokenizerTJH();
        tokenizer.setBuffer("rent=300");
        tokenizer.takeNext();
        tokenizer.takeNext();

        //check the type of the first token
        assertEquals("wrong token type", "300",tokenizer.takeNext().token);
    }

    @Test(timeout=1000)
    public void testTokenValue3() throws Exception {
        tokenizer = new TokenizerTJH();
        tokenizer.setBuffer("highlights=sds");

        //check the type of the first token
        assertEquals("wrong token type", "highlights",tokenizer.next().token);
    }

    @Test(timeout=1000)
    public void testTokenOtherBoolean() throws Exception {
        tokenizer = new TokenizerTJH();
        tokenizer.setBuffer("bedroom >= sds");
        tokenizer.takeNext();

        //check the type of the first token
        assertEquals("wrong token type", ">=",tokenizer.next().token);
    }
}
