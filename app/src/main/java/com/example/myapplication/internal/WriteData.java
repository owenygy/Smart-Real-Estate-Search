package com.example.myapplication.internal;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

public class WriteData {
    public ArrayList<Details> details;

    public WriteData(ArrayList<Details> details) {
        this.details = details;
    }

    public void saveToXMLFile(File file) throws TransformerException {
        // TODO: Implement this function yourself. The specific hierarchy is up to you,
        // but it must be in an XML format and should match the load function.
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        try {
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document d = db.newDocument();
            Element rootElement = d.createElement("details");
            d.appendChild(rootElement);
            for (Details detail : details) {
                Element detailId = d.createElement("id");
                rootElement.appendChild(detailId);

                Element rent = d.createElement("rent");
                rent.appendChild(d.createTextNode(String.valueOf(detail.rent)));
                detailId.appendChild(rent);

                Element bedroom = d.createElement("bedroom");
                bedroom.appendChild(d.createTextNode(String.valueOf(detail.bedroom)));
                detailId.appendChild(bedroom);

                Element location = d.createElement("location");
                location.appendChild(d.createTextNode(detail.location));
                detailId.appendChild(location);

                Element garage = d.createElement("garage");
                garage.appendChild(d.createTextNode(String.valueOf(detail.garage)));
                detailId.appendChild(garage);

                Element highlights = d.createElement("highlights");
                highlights.appendChild(d.createTextNode(detail.highlights));
                detailId.appendChild(highlights);
            }
            Transformer transformer = TransformerFactory.newInstance().newTransformer();
            transformer.setOutputProperty(OutputKeys.ENCODING, "utf-8");
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            DOMSource source = new DOMSource(d);
            StreamResult result = new StreamResult(file);
            transformer.transform(source, result);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static void main(String[] args) throws TransformerException {
        WriteData data = new WriteData(new ArrayList<>());

        int id;
        List<String> locations = Arrays.asList("Clean", "Beautiful", "Cheap", "near ANU", "near UC", "near railway", "Market", "Casino");
        List<String> highlights = Arrays.asList("Belconnen", "City", "Lawson", "Fyshwick", "Braddon", "Turner", "Dickson", "Downer", "Bruce", "Aranda", "Wakanda");

        for (int i = 0; i <= 1000; i++) {
            id = i;
            Random random = new Random();
            int rent = random.nextInt(600) + 100;
            int bedroom = random.nextInt(8) + 1;
            int garage = random.nextInt(2) + 0;
            int indexLocation = random.nextInt(locations.size());
            String location = locations.get(indexLocation);
            int indexHigh = random.nextInt(highlights.size());
            String highlight = highlights.get(indexHigh);
            Details detail = new Details(id, rent, bedroom, location, garage, highlight);
            data.details.add(detail);
        }
        File file = new File("data.xml");
        data.saveToXMLFile(file);
    }

}
