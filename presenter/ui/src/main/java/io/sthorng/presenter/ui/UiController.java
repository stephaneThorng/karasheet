package io.sthorng.presenter.ui;

import io.sthorng.controller.DocumentController;
import io.sthorng.domain.exception.ValidationException;
import io.sthorng.domain.usecase.document.boundary.GenerateDocumentInput;
import io.sthorng.domain.usecase.document.boundary.GenerateDocumentOuput;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

@Slf4j
@Component
public class UiController {
    public static final FileChooser.ExtensionFilter DOCX_FILES = new FileChooser.ExtensionFilter("Docx Files", "*.docx");
    @Autowired
    private DocumentController controller;
    @FXML
    private Label templateLabel;
    @FXML
    private Button templateButton;
    @FXML
    private TextField templateTextField;

    @FXML
    private Label sourceLabel;
    @FXML
    private Button sourceButton;
    @FXML
    private TextField sourceTextField;

    @FXML
    private Label destinationLabel;
    @FXML
    private Button destinationButton;
    @FXML
    private TextField destinationTextField;

    @FXML
    private Button generateButton;

    @FXML
    private void initialize() {
        templateLabel.setText("Template");
        templateTextField.setDisable(true);
        templateTextField.setPrefWidth(300d);
        Path path = Paths.get("");
        templateTextField.setText(path.toAbsolutePath() + "\\" + "template.docx");

        FileChooser templateFileChooser = new FileChooser();
        templateFileChooser.setTitle("Sélectionnez un template");
        templateFileChooser.getExtensionFilters().add(DOCX_FILES);
        templateButton.setOnAction(e -> {
            File selectedFile = templateFileChooser.showOpenDialog(null);
            if (selectedFile != null) {
                templateTextField.setText(selectedFile.getAbsolutePath());
            }
        });

        sourceLabel.setText("Source");
        sourceTextField.setDisable(true);
        sourceTextField.setPrefWidth(300d);

        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle("Sélectionnez une source");
        sourceButton.setOnAction(e -> {
            File selectedFile = directoryChooser.showDialog(null);
            if (selectedFile != null) {
                sourceTextField.setText(selectedFile.getAbsolutePath());
            }
        });

        destinationLabel.setText("Destination");
        destinationTextField.setDisable(true);
        destinationTextField.setPrefWidth(300d);
        destinationTextField.setText(path.toAbsolutePath() + "\\" + "output.docx");

        FileChooser destinationFileChooser = new FileChooser();
        directoryChooser.setTitle("Sélectionnez une destination");
        destinationFileChooser.getExtensionFilters().add(DOCX_FILES);
        destinationButton.setOnAction(e -> {
            File selectedFile = destinationFileChooser.showSaveDialog(null);
            if (selectedFile != null) {
                destinationTextField.setText(selectedFile.getAbsolutePath());
            }
        });

        generateButton.setOnAction(e -> onClickGenerate());
    }

    protected void onClickGenerate() {
        generateButton.setDisable(true);
        if (StringUtils.isAnyBlank(templateTextField.getText(),
                destinationTextField.getText(),
                sourceTextField.getText())) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Génération du docuement");
            alert.setHeaderText("Il faut tout remplir !");

            alert.showAndWait();
        } else {


            try {
                GenerateDocumentOuput response = generateDocument(destinationTextField.getText(), templateTextField.getText(), sourceTextField.getText());
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Génération du docuement");
                alert.setHeaderText(response.getStatus());
                alert.showAndWait();
            } catch (ValidationException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Génération du docuement");
                alert.setHeaderText(e.getMessage());
                alert.showAndWait();
            }
        }
        generateButton.setDisable(false);
    }

    private GenerateDocumentOuput generateDocument(String output, String template, String input) throws ValidationException {
        GenerateDocumentInput filename = new GenerateDocumentInput(output, template, input);
        return controller.generateDocument(filename);
    }
}