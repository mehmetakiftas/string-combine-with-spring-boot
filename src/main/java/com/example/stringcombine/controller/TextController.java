package com.example.stringcombine.controller;

import com.example.stringcombine.model.TextModel;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.ArrayList;
import java.util.List;

@Controller
public class TextController {
    List texts = new ArrayList();
    List dbtexts = new ArrayList();

    @GetMapping("/")
        // Home Page Get Request
    String get(Model model) {
        model.addAttribute("welcome", "Home Page");
        texts.clear();
        dbtexts.clear();
        return "index";
    }

    @PostMapping("/")
        // Home Page Post Request
    String post(@ModelAttribute TextModel text) {
        texts.add(text.getText());
        dbtexts.add(text.getText());
        return "index";
    }

    @PostMapping("/combine")
        // Combine Button
    String combine(Model model) {
        long start = 0, end = 0, time = 0;

        // Starting time
        start = System.nanoTime();

        // Text ArrayList -> Array Operations
        Object[] objectArray = texts.toArray();
        String[] textsArray = new String[objectArray.length]; // eklenecek[][]

        String sentence = "";
        String addedSentence = "";

        for (int i = 0; i < objectArray.length; i++) {
            textsArray[i] = (String) objectArray[i];
        }

        // Descriptions
        String[][] matrix = new String[40][50];
        ArrayList text1 = new ArrayList<>();
        ArrayList text2 = new ArrayList<>();
        ArrayList text3 = new ArrayList<>();
        ArrayList text4 = new ArrayList<>();
        ArrayList text5 = new ArrayList<>();
        ArrayList text6 = new ArrayList<>();
        ArrayList combined = new ArrayList<>();
        int a = 0, b = 0, c = 0, leek = 0;

        // Reset Operations
        text1.clear();
        text2.clear();
        text3.clear();
        text4.clear();
        text5.clear();
        text6.clear();

        // Splitting a sentence into words and storing them in an array
        for (int i = 0; i < textsArray.length; i++) {
            textsArray[i] = textsArray[i].toLowerCase();
            matrix[i] = textsArray[i].split(" ");
        }

        // Finding Commons
        while (a < textsArray.length - 1) {
            text1.clear();
            text2.clear();

            for (int i = 0; i < matrix[a].length; i++) {
                text1.add(matrix[a][i]);
            }

            for (int i = 0; i < matrix[a + 1].length; i++) {
                text2.add(matrix[a + 1][i]);
            }

            for (int i = 0; i < text1.size(); i++) {
                for (int j = 0; j < text2.size(); j++) {
                    if (text1.get(i).toString().length() > text2.get(j).toString().length()) {
                        if (text1.get(i).toString().equals(text2.get(j).toString()) == true) {
                            text3.add(text1.get(i).toString());
                        }
                    } else {
                        if (text2.get(j).toString().equals(text1.get(i).toString()) == true) {
                            text3.add(text2.get(j).toString());
                        }
                    }
                }
            }
            a++;
        }


        // If there are "no" commons, enters the 'if'.
        // If there are commons, continues from the 'else'.
        if (text3.size() == 0) {
            text1.clear();
            text2.clear();
            text3.clear();
            System.out.println(":)");
            sentence = "Text could not be combined!";
            dbtexts.add(sentence);
            model.addAttribute("wheezy", "The sentence could not be combined because there were no commons.");
        } else {
            text1.clear();
            text2.clear();

            for (int i = 0; i < matrix[0].length; i++) {
                text1.add(matrix[0][i]);
            }
            c = 0;
            int counter = 0;

            // Getting the part of the first sentence before the common word.
            while (true) {
                for (int i = 0; i < text1.size(); i++) {
                    b = 0;
                    for (int j = 0; j < text3.size(); j++) {
                        if (text1.get(i).toString().equals(text3.get(j).toString()) == true) {
                            c++;
                        } else {
                            b++;
                        }
                    }

                    if (c == 0) {
                        text4.add(text1.get(i).toString());
                    }
                }
                counter++;

                if (c != 0 || counter == 1) {
                    break;
                }
            }

            text1.clear();
            text2.clear();

            for (int i = 0; i < matrix[textsArray.length - 1].length; i++) {
                text1.add(matrix[textsArray.length - 1][i]);
            }

            c = 0;
            int counter2 = 0;

            // Getting the part of the last sentence after the common word.
            while (true) {
                if (counter2 == 0) {
                    for (int i = text1.size() - 1; i >= 0; i--) {
                        b = 0;
                        for (int j = text3.size() - 1; j >= 0; j--) {
                            if (text1.get(i).toString().equals(text3.get(j).toString()) == true) {
                                c++;
                            } else {
                                b++;
                            }
                        }
                        if (c == 0) {
                            text6.add(text1.get(i).toString());
                        } else {
                            break;
                        }

                    }
                } else {
                    break;
                }
                counter2++;
            }

            // Converting the value we obtained in reverse order in the upper 'while' loop to its normal state.
            for (int i = text6.size() - 1; i >= 0; i--) {
                text5.add(text6.get(i).toString());
            }

            // Cleaning the same common words to avoid repetition.
            while (true) {
                leek = 0;
                for (int i = 0; i < text3.size(); i++) {
                    if (text3.size() == 1) {
                        break;
                    } else {
                        for (int j = i + 1; j < text3.size(); j++) {
                            if (text3.get(i).toString().equals(text3.get(j).toString()) == true) {
                                text3.remove(j);
                                leek++;
                            }
                        }
                    }
                }

                if (leek == 0) {
                    break;
                }
            }

            combined.clear();

            // Reassembling the words separated into arrays back into a sentence.
            for (int i = 0; i < text4.size(); i++) {
                combined.add(text4.get(i));
            }
            for (int i = 0; i < text3.size(); i++) {
                combined.add(text3.get(i));
            }
            for (int i = 0; i < text5.size(); i++) {
                combined.add(text5.get(i));
            }

            // Creating a sentence by adding spaces between the words taken from an ArrayList.
            for (int i = 0; i < combined.size(); i++) {
                addedSentence = combined.get(i).toString() + " ";
                sentence = sentence + addedSentence;
            }

            // Adding the sentence to the ArrayList created for the database.
            dbtexts.add(sentence);

            combined.clear();

            model.addAttribute("yopierre", "Combined sentence: " + sentence);
        }

        // Terminating and calculating duration
        end = System.nanoTime();
        time = end - start;

        // Printing nanoseconds to the screen.
        model.addAttribute("metroboomin", "Elapsed Time: " + time + " nanoseconds");

        return "combine";
    }

    @GetMapping("/save")
        // "Save" button to save to the database
    String save() {
        // ArrayList -> Array Operations
        Object[] objectArray = dbtexts.toArray();
        String[] dbArray = new String[objectArray.length];
        for (int i = 0; i < dbtexts.size(); i++) {
            dbArray[i] = (String) objectArray[i];
        }

        // Magic of connecting to MongoDB database
        MongoClientURI connectionString = new MongoClientURI("mongodb://localhost:27017");
        MongoClient mongoClient = new MongoClient(connectionString);
        MongoDatabase database = mongoClient.getDatabase("sentences");

        MongoCollection<Document> collection = database.getCollection("texts");
        Document document = new Document();

        // Adding texts like text1, text2...
        for (int i = 0; i < dbArray.length - 1; i++) {
            document.append("text" + (i + 1), dbArray[i]);
        }

        // Adding sentence as the last element of the database
        document.append("sentence", dbArray[(dbArray.length) - 1]);
        collection.insertOne(document);

        // Closing the database connection
        mongoClient.close();

        dbtexts.clear();

        return "save";
    }
}
