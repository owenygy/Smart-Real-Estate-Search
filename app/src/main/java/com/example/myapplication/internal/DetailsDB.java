package com.example.myapplication.internal;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

/**
 * This class provides operations to query the rent information.
 */
public class DetailsDB {

    public enum Feature {
        RENT,
        BEDROOM,
        GARAGE,
        LOCATION,
        HIGHLIGHTS
    }

    public enum Operation {
        LT,
        GT,
        EQ;

        public Operation reverse() {
            switch (this) {
                case EQ:
                    return EQ;
                case GT:
                    return LT;
                case LT:
                    return GT;
                default:
                    throw new IllegalArgumentException();
            }
        }
    }

    private RBTree<Integer, Details> treeRent;
    private RBTree<Integer, Details> treeBedroom;
    private RBTree<Integer, Details> treeGarage;
    private RBTree<String, Details> treeLocation;
    private RBTree<String, Details> treeHighlights;

    private static DetailsDB instance = null;

    public static DetailsDB getInstance(InputStream inputStream) {
        if (instance == null) {
            instance = new DetailsDB(inputStream);
        }
        return instance;
    }

    private DetailsDB(InputStream inputStream) {

        // step 1: read data from file
        List<Details> detailsList = new ArrayList<>();
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        try {
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.parse(inputStream);
            doc.getDocumentElement().normalize();

            // assume that there are six attributes:
            // 'id', 'rent', 'bedroom', 'garage', 'location', 'highlights'
            NodeList nodeList = doc.getElementsByTagName("id");
            for (int i = 0; i < nodeList.getLength(); i++) {
                Node node = nodeList.item(i);
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element element = (Element) node;
                    int rent = Integer.parseInt(element.getElementsByTagName("rent").item(0).getTextContent());
                    int bedroom = Integer.parseInt(element.getElementsByTagName("bedroom").item(0).getTextContent());
                    int garage = Integer.parseInt(element.getElementsByTagName("garage").item(0).getTextContent());
                    String location = element.getElementsByTagName("location").item(0).getTextContent();
                    String highlights = element.getElementsByTagName("highlights").item(0).getTextContent();
                    detailsList.add(new Details(i, rent, bedroom, location, garage, highlights));
                }
            }
        } catch (ParserConfigurationException | SAXException | IOException e) {
            e.printStackTrace();
        }

        // step 2: create RBTree for each attribute
        treeRent = new RBTree<>(); // rent as key
        treeBedroom = new RBTree<>(); // bedroom as key
        treeGarage = new RBTree<>(); // garage as key
        treeLocation = new RBTree<>(); // location as key
        treeHighlights = new RBTree<>(); // highlights as key
        for (Details e : detailsList) {
            treeRent.insert(e.rent, e);
            treeBedroom.insert(e.bedroom, e);
            treeGarage.insert(e.garage, e);
            treeLocation.insert(e.location, e);
            treeHighlights.insert(e.highlights, e);
        }
    }

    /**
     * Deletes specified {@code Details} in the database.
     * <p>
     * Note that we use equals() internally to find the details to be deleted.
     *
     * @param e details to be deleted
     * @return deleted {@code Details}, or null if the specified details does not exist in the database
     * @throws IOException inconsistency appears in the internal tree
     */
    public Details delete(Details e) throws IOException {
        Details deleted;
        if (allEquals((deleted = treeRent.delete(e.rent, e)),
                treeBedroom.delete(e.bedroom, e),
                treeGarage.delete(e.garage, e),
                treeLocation.delete(e.location, e),
                treeHighlights.delete(e.highlights, e))
        ) {
            return deleted;
        } else {
            throw new IOException("Internal tree inconsistency");
        }
    }

    /**
     * Adds specified {@code Details} to the database.
     * <p>
     * Note that we use equals() internally to ensure the details to be added.
     *
     * @param e details to be added
     */
    public void add(Details e) {
        // No need to verify the return values of insert()
        treeRent.insert(e.rent, e);
        treeBedroom.insert(e.bedroom, e);
        treeGarage.insert(e.garage, e);
        treeLocation.insert(e.location, e);
        treeHighlights.insert(e.highlights, e);
    }

    private boolean allEquals(Details... x) {
        for (int i = 1; i < x.length; i++)
            // Here we simply compare reference of object to ensure that we are adding/deleting the same object
            if (x[0] != x[i]) return false;
        return true;
    }

    private Set<Details> query(Feature ft, String from, String to) {
        Set<Details> result = new HashSet<>();
        int fromNum = 0, toNum = 0;
        try {
            fromNum = Integer.parseInt(from);
            toNum = Integer.parseInt(to);
        } catch (NumberFormatException ignored) {
        }
        switch (ft) {
            case RENT:
                treeRent.subTree(fromNum, toNum).forEachAscending((k, v) -> result.add(v));
                return result;
            case BEDROOM:
                treeBedroom.subTree(fromNum, toNum).forEachAscending((k, v) -> result.add(v));
                return result;
            case GARAGE:
                treeGarage.subTree(fromNum, toNum).forEachAscending((k, v) -> result.add(v));
                return result;
            case LOCATION:
                treeLocation.subTree(from, to, new StringComparator()).forEachAscending((k, v) -> result.add(v));
                return result;
            case HIGHLIGHTS:
                treeHighlights.subTree(from, to, new StringComparator()).forEachAscending((k, v) -> result.add(v));
                return result;
            default:
                throw new IllegalArgumentException("Unhandled feature");
        }
    }

    @SafeVarargs
    private static <E> Set<E> intersection(Set<E>... sets) {
        for (int i = 1; i < sets.length; i++) {
            sets[0].retainAll(sets[i]);
        }
        return sets[0];
    }

    /**
     * Evaluates specified condition and returns set of details which matches the specified condition.
     *
     * @param condition condition
     * @return set of details which matches the specified condition
     * @throws IllegalArgumentException incorrect format of the condition
     */
    public Set<Details> eval(String condition) throws Exception {
        TokenizerTJH tokenizer = new TokenizerTJH();
        tokenizer.setBuffer(condition);
        List<TokenTJH> tokens = new ArrayList<>();
        while (tokenizer.hasNext()) {
            tokens.add(tokenizer.takeNext());
        }

        if (tokens.size() == 3) {
            // Condition is in the form: A < B
            TokenTJH lhs = tokens.get(0);
            TokenTJH op = tokens.get(1);
            TokenTJH rhs = tokens.get(2);
            return eval(lhs, op, rhs);
        } else if (tokens.size() == 5) {
            // Condition is in the form: A < B < C

            // First half
            TokenTJH lhs = tokens.get(0);
            TokenTJH op = tokens.get(1);
            TokenTJH rhs = tokens.get(2);
            Set<Details> firstHalf = eval(lhs, op, rhs);

            // Second half
            lhs = rhs;
            op = tokens.get(3);
            rhs = tokens.get(4);
            Set<Details> secondHalf = eval(lhs, op, rhs);

            // Return the intersection
            return intersection(firstHalf, secondHalf);
        } else {
            throw new Exception("Unhandled number of tokens");
        }
    }

    private Set<Details> eval(TokenTJH lhs, TokenTJH op, TokenTJH rhs) throws IllegalArgumentException {
        if (lhs.type == TokenTJH.Type.Lit) {
            // Case 1 : feature is on RHS
            return eval(rhs.type.parseFeature(), op.type.parseOperation().reverse(), lhs.token);
        } else if (rhs.type == TokenTJH.Type.Lit) {
            // Case 2 : feature is on LHS
            return eval(lhs.type.parseFeature(), op.type.parseOperation(), rhs.token);
        } else {
            // Case 3 : either side has no given feature
            throw new IllegalArgumentException("Either side has no given feature");
        }
    }

    private Set<Details> eval(DetailsDB.Feature ft, DetailsDB.Operation op, String val) {
        switch (op) {
            case LT:
                return query(ft, "0", val);
            case GT:
                return query(ft, val, String.valueOf(Integer.MAX_VALUE));
            case EQ:
                return query(ft, val, val);
            default:
                throw new IllegalArgumentException("Unhandled operation");
        }
    }

    private static class StringComparator implements Comparator<String> {
        @Override
        public int compare(String o1, String o2) {
            if (o1.toLowerCase().contains(o2.toLowerCase()) ||
                    o2.toLowerCase().contains(o1.toLowerCase())) {
                return 0;
            }
            return 233;

        }
    }

}
