import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.Set;

public class TagExtractor extends JFrame {
    private JTextArea resultArea;
    private File textFile;
    private File stopWordsFile;
    private Map<String, Integer> tagMap;

    public TagExtractor() {
        setTitle("Tag Extractor");
        setSize(600, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Text Area
        resultArea = new JTextArea();
        resultArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(resultArea);
        add(scrollPane, BorderLayout.CENTER);

        // Buttons
        JPanel buttonPanel = new JPanel();
        JButton openTextFileBtn = new JButton("Open Text File");
        JButton openStopWordsBtn = new JButton("Open Stop Words File");
        JButton extractBtn = new JButton("Extract Tags");
        JButton saveBtn = new JButton("Save Tags to File");

        openTextFileBtn.addActionListener(this::openTextFile);
        openStopWordsBtn.addActionListener(this::openStopWordsFile);
        extractBtn.addActionListener(this::extractTags);
        saveBtn.addActionListener(this::saveTags);

        buttonPanel.add(openTextFileBtn);
        buttonPanel.add(openStopWordsBtn);
        buttonPanel.add(extractBtn);
        buttonPanel.add(saveBtn);

        add(buttonPanel, BorderLayout.NORTH);
    }

    private void openTextFile(ActionEvent e) {
        textFile = chooseFile();
        if (textFile != null) {
            JOptionPane.showMessageDialog(this, "Selected Text File: " + textFile.getName());
        }
    }

    private void openStopWordsFile(ActionEvent e) {
        stopWordsFile = chooseFile();
        if (stopWordsFile != null) {
            JOptionPane.showMessageDialog(this, "Selected Stop Words File: " + stopWordsFile.getName());
        }
    }

    private void extractTags(ActionEvent e) {
        if (textFile == null || stopWordsFile == null) {
            JOptionPane.showMessageDialog(this, "Please select both text and stop word files.");
            return;
        }

        try {
            Set<String> stopWords = TagProcessor.loadStopWords(stopWordsFile);
            tagMap = TagProcessor.extractTags(textFile, stopWords);

            StringBuilder sb = new StringBuilder();
            tagMap.forEach((word, freq) -> sb.append(word).append(": ").append(freq).append("\n"));
            resultArea.setText(sb.toString());

        } catch (IOException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error processing files.");
        }
    }

    private void saveTags(ActionEvent e) {
        if (tagMap == null || tagMap.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No tags to save.");
            return;
        }

        JFileChooser chooser = new JFileChooser();
        int returnVal = chooser.showSaveDialog(this);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File saveFile = chooser.getSelectedFile();
            try {
                TagProcessor.saveTagsToFile(tagMap, saveFile);
                JOptionPane.showMessageDialog(this, "Tags saved to: " + saveFile.getName());
            } catch (IOException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error saving file.");
            }
        }
    }

    private File chooseFile() {
        JFileChooser chooser = new JFileChooser();
        int returnVal = chooser.showOpenDialog(this);
        return (returnVal == JFileChooser.APPROVE_OPTION) ? chooser.getSelectedFile() : null;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new TagExtractor().setVisible(true));
    }
}
