package br.com.avfinal.configuration;

import javafx.scene.image.Image;
import javafx.stage.Stage;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import com.sun.javafx.application.PlatformImpl;

@Configuration
@Import({ PersistenceConfig.class })
@ComponentScan(value = { "br.com.avfinal" })
public class AVFinalConfig {

	private Stage stage;

	@Bean
	Stage stagePrimary() {
		PlatformImpl.runAndWait(() -> stage = new Stage());
		stage.getIcons().add(new Image("file:resources/images/AVFinal_32x32.png"));
		return stage;
	}

}
