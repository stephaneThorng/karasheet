package io.sthorng.presenter.ui;

import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;

import java.io.IOException;

@SpringBootApplication
@ComponentScan(basePackages = {"io.sthorng.*"})
public class Application extends javafx.application.Application {
    private ConfigurableApplicationContext springContext;

    public static void main(String[] args) {
        launch(Application.class, args);
    }

    @Override
    public void init() {
        this.springContext = SpringApplication.run(Application.class);
    }

    @Override
    public void stop() {
        this.springContext.close();
        Platform.exit();
    }

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("main-view.fxml"));
        fxmlLoader.setControllerFactory(springContext::getBean);
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("Karasheet v1");
        stage.setScene(scene);
        stage.show();
    }
}