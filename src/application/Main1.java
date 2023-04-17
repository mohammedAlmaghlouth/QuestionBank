//
//  Done By: Mohamamd AlSafran & Mohammad AlMaghlouth
//  Course : ICS 108
//  Title  : Question Bank 
//  Desc.  : This program is written with a JavaFX program to create, modify, delete and display
//           questions. It uses ListView, Buttons, Labels, TextView, and RadioButton
//           It also read/write to binary file using Object Stream. A calss for Questions has  been
//			 created too. Read/Write/set/initializing buttons Mehtods have been created as well.  
//
//
package application;

import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

public class Main1 extends Application {

	int row_index = 0;
	String tempAnswer = "";

	// Define Array with Objects Elements
	List<Questions> questionsArray = new ArrayList<>();

	// Define ListView
	ListView<String> QSlistv = new ListView<>();

	// Labels to Display
	Label labelQus = new Label();
	Label labelCorrect = new Label();
	Label labelAns1 = new Label();
	Label labelAns2 = new Label();
	Label labelAns3 = new Label();
	Label labelAns4 = new Label();

	// TextFields to read the Question Information
	TextField inputQus = new TextField();
	TextField inputAns1 = new TextField();
	TextField inputAns2 = new TextField();
	TextField inputAns3 = new TextField();
	TextField inputAns4 = new TextField();

	// Buttons for the display Screen
	Button display = new Button("Press Here to Start The Programe");
	Button create = new Button("Create");
	Button edit = new Button("  Edit  ");
	Button delete = new Button("Delete");
	Button exit = new Button("Save & Exit");
	Button cancel = new Button("Cancel");
	Button save = new Button(" Save ");
	Button conf = new Button("Confirm");

	// Buttons for the Questions Screen
	RadioButton RadioAnsA = new RadioButton("    A.");
	RadioButton RadioAnsB = new RadioButton("    B.");
	RadioButton RadioAnsC = new RadioButton("    C.");
	RadioButton RadioAnsD = new RadioButton("    D.");

	@Override
	public void start(Stage primaryStage) throws IOException, ClassNotFoundException {

		setButtonFormat();

		// Set the Logo for KFUPM
		Image img = new Image("KFUPM.png");
		ImageView imgV = new ImageView();
		imgV.setImage(img);
		imgV.setFitHeight(100);
		imgV.setFitWidth(100);
		VBox paneForImage = new VBox();
		paneForImage.getChildren().add(imgV);
		paneForImage.setAlignment(Pos.TOP_RIGHT);

		Text PanerText = new Text("QuestionsBank ICS108 :  By M. AlSafran & M. AlMaghlouth");
		PanerText.setFill(Color.BLACK);
		PanerText.setFont(Font.font("Times New Roman", FontWeight.BOLD, FontPosture.REGULAR, 30));

		BorderPane paneOuter = new BorderPane();
		paneOuter.setBottom(display);
		paneOuter.setAlignment(display, Pos.TOP_CENTER);
		paneOuter.setCenter(PanerText);
		paneOuter.setAlignment(PanerText, Pos.CENTER);
		paneOuter.setStyle("-fx-background-color: 	LIGHTGOLDENRODYELLOW;");
		paneOuter.setLeft(paneForImage);

		Scene outerScene = new Scene(paneOuter, 1000, 500);
		BorderPane paneForMasterScene = new BorderPane();
		Scene masterScene = new Scene(paneForMasterScene, 1100, 500);

		primaryStage.setScene(outerScene); // Display the first Screen
		primaryStage.setTitle("QusetionBank");
		primaryStage.show();
		primaryStage.setOnCloseRequest(e -> {
			Platform.exit();
		}); // Exit when pressed X

		PanerText.setOnMouseMoved(paneText -> {
			PanerText.setFill(Color.RED);
		});

		display.setOnAction(eDisplay -> {

			// Read objects from file and write it to array
			ReadFromObjectFile();
			primaryStage.setOnCloseRequest(e -> {
				WritToObjectFile();
			}); // when pressed X write data to file

			// Copy the Data from the Array to the ListView
			for (row_index = 0; row_index < questionsArray.size(); row_index++)
				QSlistv.getItems().add(questionsArray.get(row_index).QuestionText);
			row_index--;

			// Setup the ListView with displaying the information on side
			QSlistv.setPrefSize(450, 10000);
			QSlistv.getSelectionModel().selectedItemProperty().addListener(ov -> {
				int Qus_index = QSlistv.getSelectionModel().getSelectedIndex();
				int Qus_number = Qus_index + 1;
				labelQus.setText(" Q" + Qus_number + ".  " + questionsArray.get(Qus_index).QuestionText);
				labelQus.setFont(Font.font("Times New Roman", FontWeight.BOLD, FontPosture.REGULAR, 20));
				labelAns1.setText("    A.  " + questionsArray.get(Qus_index).Choice1);
				labelAns1.setFont(Font.font("Times New Roman", FontWeight.NORMAL, FontPosture.REGULAR, 20));
				labelAns2.setText("    B.  " + questionsArray.get(Qus_index).Choice2);
				labelAns2.setFont(Font.font("Times New Roman", FontWeight.NORMAL, FontPosture.REGULAR, 20));
				labelAns3.setText("    C.  " + questionsArray.get(Qus_index).Choice3);
				labelAns3.setFont(Font.font("Times New Roman", FontWeight.NORMAL, FontPosture.REGULAR, 20));
				labelAns4.setText("    D.  " + questionsArray.get(Qus_index).Choice4);
				labelAns4.setFont(Font.font("Times New Roman", FontWeight.NORMAL, FontPosture.REGULAR, 20));
				labelCorrect.setText("The Correct Answer is : " + questionsArray.get(Qus_index).Ans);
				labelCorrect.setFont(Font.font("Times New Roman", FontWeight.BOLD, FontPosture.REGULAR, 20));
				labelCorrect.setTextFill(Color.RED);
				tempAnswer = "";

			});
			// VBOX Pane for listView
			VBox paneForListv = new VBox();
			paneForListv.getChildren().addAll(new ScrollPane(QSlistv));

			// HOBOX pane for Buttons at the bottom
			HBox paneForButtons = new HBox(30);
			paneForButtons.setAlignment(Pos.CENTER);
			paneForButtons.setSpacing(30);
			paneForButtons.getChildren().addAll(create, edit, delete, exit);

			// Pane for display text on the Center
			GridPane paneForDisplyQus = new GridPane();
			paneForDisplyQus.setAlignment(Pos.TOP_LEFT);
			paneForDisplyQus.setPadding(new Insets(20, 20, 20, 20));
			paneForDisplyQus.setHgap(6);
			paneForDisplyQus.setVgap(6);
			paneForDisplyQus.add(labelQus, 0, 0);
			labelQus.setWrapText(true);
			paneForDisplyQus.add(labelAns1, 0, 1);
			labelAns1.setWrapText(true);
			paneForDisplyQus.add(labelAns2, 0, 2);
			labelAns2.setWrapText(true);
			paneForDisplyQus.add(labelAns3, 0, 3);
			labelAns3.setWrapText(true);
			paneForDisplyQus.add(labelAns4, 0, 4);
			labelAns4.setWrapText(true);
			paneForDisplyQus.setVgap(10);
			paneForDisplyQus.add(labelCorrect, 0, 6);
			paneForDisplyQus.setStyle("-fx-border-width: 4px; -fx-border-color: black");

			// Master Border Pane
			paneForMasterScene.setLeft(paneForListv);
			paneForMasterScene.setBottom(paneForButtons);
			paneForMasterScene.setCenter(paneForDisplyQus);
			paneForMasterScene.setStyle("-fx-background-color: LIGHTGOLDENRODYELLOW;");

			// Create a scene and place it in the stage
			primaryStage.setResizable(true);
			primaryStage.setScene(masterScene);
		});

//----------------------------------------------------------------------------------
//  CREATE A NEW RECORD ON THE LISTVIEW AND THE ARRAYLIST
//----------------------------------------------------------------------------------
		create.setOnAction(ecreate -> {

			clearRadioButtons();

			Text textCreate = new Text("CREATE A NEW QUSTION AND CHOSE THE ANSWER");
			textCreate.setFont(Font.font("Times New Roman", FontWeight.BOLD, FontPosture.REGULAR, 16));

			HBox paneForSaveCancelInCreate = new HBox(10);
			paneForSaveCancelInCreate.setAlignment(Pos.BOTTOM_CENTER);
			paneForSaveCancelInCreate.getChildren().addAll(save, cancel);

			GridPane paneForCreate = new GridPane();
			paneForCreate.setAlignment(Pos.CENTER);
			paneForCreate.setPadding(new Insets(20, 20, 20, 20));
			paneForCreate.setHgap(5.5);
			paneForCreate.setVgap(5.5);

			inputQus.setPrefColumnCount(20);
			paneForCreate.add(new Label("Write a New Qustion: "), 0, 0);
			paneForCreate.add(inputQus, 1, 0);
			paneForCreate.add(RadioAnsA, 0, 1);
			paneForCreate.add(inputAns1, 1, 1);
			paneForCreate.add(RadioAnsB, 0, 2);
			paneForCreate.add(inputAns2, 1, 2);
			paneForCreate.add(RadioAnsC, 0, 3);
			paneForCreate.add(inputAns3, 1, 3);
			paneForCreate.add(RadioAnsD, 0, 4);
			paneForCreate.add(inputAns4, 1, 4);
			paneForCreate.setStyle("-fx-border-width: 4px; -fx-border-color: black");

			BorderPane paneForDisplayCreation = new BorderPane();
			paneForDisplayCreation.setCenter(paneForCreate);
			paneForDisplayCreation.setTop(textCreate);
			paneForDisplayCreation.setAlignment(textCreate, Pos.CENTER);
			paneForDisplayCreation.setBottom(paneForSaveCancelInCreate);
			paneForDisplayCreation.setStyle("-fx-background-color: LIGHTGOLDENRODYELLOW;");

			Scene sceneForCreate = new Scene(paneForDisplayCreation, 500, 500);
			primaryStage.setResizable(true);
			primaryStage.setScene(sceneForCreate);

			// CLICKING SAVE BUTTON, COPY THE DATA FROM THE TEXTVIEW TO THE ARRAY
			save.setOnAction(SaveCreate -> {
				if (!(inputQus.getText().isBlank() || inputAns1.getText().isBlank() || inputAns2.getText().isBlank()
						|| inputAns3.getText().isBlank() || inputAns4.getText().isBlank() || tempAnswer == "")) {
					row_index++;
					Questions Qus_tmp = new Questions();
					Qus_tmp.QuestionText = inputQus.getText();
					Qus_tmp.Ans = tempAnswer;
					Qus_tmp.Choice1 = inputAns1.getText();
					Qus_tmp.Choice2 = inputAns2.getText();
					Qus_tmp.Choice3 = inputAns3.getText();
					Qus_tmp.Choice4 = inputAns4.getText();
					questionsArray.add(Qus_tmp);
					QSlistv.getItems().add(inputQus.getText());
				} else {
					NotValidEntries("Empty Questtion Error", "You Must Enter a Valid Question");
				}
				primaryStage.setScene(masterScene); // Display the Scene
			});
			cancel.setOnAction(CancelCreate -> {
				primaryStage.setScene(masterScene);
			});
		});
//----------------------------------------------------------------------------------
//  EDIT BUTTON, MODIFY THE DATA IN BOTH THE ARRAYLIST AND THE DISPLAY
//----------------------------------------------------------------------------------
		edit.setOnAction(eedit -> {
			int Qus_index = QSlistv.getSelectionModel().getSelectedIndex(); // Read the index

			clearRadioButtons();

			// Display the Question Records and the Radi Button Selection
			inputQus.setText(questionsArray.get(Qus_index).QuestionText);
			inputAns1.setText(questionsArray.get(Qus_index).Choice1);
			inputAns2.setText(questionsArray.get(Qus_index).Choice2);
			inputAns3.setText(questionsArray.get(Qus_index).Choice3);
			inputAns4.setText(questionsArray.get(Qus_index).Choice4);

			if (questionsArray.get(Qus_index).Ans.equalsIgnoreCase("A")) {
				RadioAnsA.setSelected(true);
				tempAnswer = "A";
			} else if (questionsArray.get(Qus_index).Ans.equalsIgnoreCase("B")) {
				RadioAnsB.setSelected(true);
				tempAnswer = "B";
			} else if (questionsArray.get(Qus_index).Ans.equalsIgnoreCase("C")) {
				RadioAnsC.setSelected(true);
				tempAnswer = "C";
			} else {
				RadioAnsD.setSelected(true);
				tempAnswer = "D";
			}

			Text textEdit = new Text("MODIFY A QUSTION");
			textEdit.setFont(Font.font("Times New Roman", FontWeight.BOLD, FontPosture.REGULAR, 16));

			HBox paneForSaveCancelEdit = new HBox(10);
			paneForSaveCancelEdit.setAlignment(Pos.BOTTOM_CENTER);
			paneForSaveCancelEdit.getChildren().addAll(save, cancel);

			GridPane paneForEdit = new GridPane();
			paneForEdit.setAlignment(Pos.CENTER);
			paneForEdit.setPadding(new Insets(20, 20, 20, 20));
			paneForEdit.setHgap(5.5);
			paneForEdit.setVgap(5.5);

			// Read the Modified Question Data
			inputQus.setPrefColumnCount(25);
			paneForEdit.add(new Label("Edit the Qustion: "), 0, 0);
			paneForEdit.add(inputQus, 1, 0);
			paneForEdit.add(RadioAnsA, 0, 1);
			paneForEdit.add(inputAns1, 1, 1);
			paneForEdit.add(RadioAnsB, 0, 2);
			paneForEdit.add(inputAns2, 1, 2);
			paneForEdit.add(RadioAnsC, 0, 3);
			paneForEdit.add(inputAns3, 1, 3);
			paneForEdit.add(RadioAnsD, 0, 4);
			paneForEdit.add(inputAns4, 1, 4);
			paneForEdit.setStyle("-fx-border-width: 4px; -fx-border-color: black");

			BorderPane paneForDisplayEdit = new BorderPane();
			paneForDisplayEdit.setCenter(paneForEdit);
			paneForDisplayEdit.setTop(textEdit);
			paneForDisplayEdit.setAlignment(textEdit, Pos.CENTER);
			paneForDisplayEdit.setBottom(paneForSaveCancelEdit);
			paneForDisplayEdit.setStyle("-fx-background-color: LIGHTGOLDENRODYELLOW;");

			Scene sceneForCreate = new Scene(paneForDisplayEdit, 500, 500);
			primaryStage.setResizable(true);
			primaryStage.setScene(sceneForCreate);

			// SAVE ACTION, TO SAVE THE DATA ON TO THE ARRAY
			save.setOnAction(SaveEdit -> {
				if (!(inputQus.getText().isBlank() || inputAns1.getText().isBlank() || inputAns2.getText().isBlank()
						|| inputAns3.getText().isBlank() || inputAns4.getText().isBlank())) {
					questionsArray.get(Qus_index).QuestionText = inputQus.getText();
					questionsArray.get(Qus_index).Ans = tempAnswer;
					questionsArray.get(Qus_index).Choice1 = inputAns1.getText();
					questionsArray.get(Qus_index).Choice2 = inputAns2.getText();
					questionsArray.get(Qus_index).Choice3 = inputAns3.getText();
					questionsArray.get(Qus_index).Choice4 = inputAns4.getText();

					// Update the ListView with the new entry
					QSlistv.getSelectionModel().clearSelection(Qus_index);
					QSlistv.getItems().remove(Qus_index);
					QSlistv.getItems().add(Qus_index, questionsArray.get(Qus_index).QuestionText);
				} else {
					NotValidEntries("Update Questtion Error", "You Must Update With Valid Entries");
				}
				primaryStage.setScene(masterScene);
			});

			// CLICKING CANCEL BUTTON, RETURN TO THE ORIGINAL SCREEN
			cancel.setOnAction(CancelEdit -> {
				primaryStage.setScene(masterScene);
			});
		});
//----------------------------------------------------------------------------------
//  DELETE BUTTON, REMOVE THE RECORD FROM BOTH LISTVIEW AND ARRAYLIST
//----------------------------------------------------------------------------------
		delete.setOnAction(eDelete -> {
			BorderPane paneForDeletion = new BorderPane();

			Text textHeader = new Text("DELETION SCREEN");
			textHeader.setFont(Font.font("Times New Roman", FontWeight.BOLD, FontPosture.REGULAR, 16));

			StackPane paneForText1 = new StackPane();
			Text text1 = new Text("CONFIRM DELETION  ? ");
			text1.setFill(Color.RED);
			text1.setFont(Font.font("Times New Roman", FontWeight.BOLD, FontPosture.ITALIC, 16));
			paneForText1.getChildren().add(text1);
			paneForText1.setStyle("-fx-border-width: 4px; -fx-border-color: black");

			HBox paneForConfCancelDelete = new HBox(10);
			paneForConfCancelDelete.setAlignment(Pos.BOTTOM_CENTER);
			paneForConfCancelDelete.getChildren().addAll(conf, cancel);

			paneForDeletion.setBottom(paneForConfCancelDelete);
			paneForDeletion.setTop(textHeader);
			paneForDeletion.setAlignment(textHeader, Pos.CENTER);
			paneForDeletion.setCenter(paneForText1);
			paneForDeletion.setStyle("-fx-background-color: LIGHTGOLDENRODYELLOW;");

			Scene sceneForDelet = new Scene(paneForDeletion, 500, 500);
			primaryStage.setScene(sceneForDelet);
			conf.setOnAction(ConfirmDelete -> { // Perform the delete action

				if (QSlistv.getItems().size() > 1) {

					int Qus_index = QSlistv.getSelectionModel().getSelectedIndex();

					QSlistv.getItems().remove(Qus_index);
					row_index--; // Change the row_index to the new size
					questionsArray.remove(Qus_index);
				} else {
					NotValidEntries("Delete Questtion Error", "You Can Only Update the Last Record in the File");
				}

				primaryStage.setScene(masterScene);

			});
			cancel.setOnAction(CancelDelete -> { // Cancel the delete action
				primaryStage.setScene(masterScene);
			});
		});

//----------------------------------------------------------------------------------
//  EXIT BUTTON, CALL ANOTHER METHOD TO WRITE THE DATA FROM THE ARRAYLIST TO FILE
//----------------------------------------------------------------------------------
		exit.setOnAction(eExit -> {
			// BorderPane paneForDeletion = new BorderPane();

			WritToObjectFile();

			try {
				TimeUnit.SECONDS.sleep(1); // Wait 2 Seconds to exit
				Platform.exit(); // Exit the Program
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		});
	}

// ----------------------------------------------------------------------------------
// MAIN PROGRAM  
//----------------------------------------------------------------------------------

	public static void main(String[] args) {
		launch(args);
	}

// ----------------------------------------------------------------------------------
// SET BUTTONS PADDING
//----------------------------------------------------------------------------------
	public void setButtonFormat() {
		display.setPrefHeight(50);
		create.setPrefHeight(20);
		edit.setPrefHeight(20);
		delete.setPrefHeight(20);
		exit.setPrefHeight(20);
		save.setPrefHeight(20);
		conf.setPrefHeight(20);
		cancel.setPrefHeight(20);

		display.setPrefWidth(1000);
		create.setPrefWidth(100);
		edit.setPrefWidth(100);
		delete.setPrefWidth(100);
		exit.setPrefWidth(100);
		save.setPrefWidth(100);
		cancel.setPrefWidth(100);
		conf.setPrefWidth(100);

	}

// ----------------------------------------------------------------------------------
// CLEAR RADIOBUTTONS METHOD 
//----------------------------------------------------------------------------------
	public void clearRadioButtons() {
		RadioAnsA.setSelected(false);
		RadioAnsB.setSelected(false);
		RadioAnsC.setSelected(false);
		RadioAnsD.setSelected(false);

		inputQus.setText("");
		inputAns1.setText("");
		inputAns2.setText("");
		inputAns3.setText("");
		inputAns4.setText("");
		tempAnswer = "";

		ToggleGroup group = new ToggleGroup();
		RadioAnsA.setToggleGroup(group);
		RadioAnsB.setToggleGroup(group);
		RadioAnsC.setToggleGroup(group);
		RadioAnsD.setToggleGroup(group);
		RadioAnsA.setOnAction(ansA -> {
			tempAnswer = "A";
		});
		RadioAnsB.setOnAction(ansB -> {
			tempAnswer = "B";
		});
		RadioAnsC.setOnAction(ansC -> {
			tempAnswer = "C";
		});
		RadioAnsD.setOnAction(ansD -> {
			tempAnswer = "D";
		});

	}

// ----------------------------------------------------------------------------------
// METHOD TO DISPLAY ERROR MESSAGES 
//----------------------------------------------------------------------------------
	public static void NotValidEntries(String title, String message) {
		Stage window = new Stage();
		window.initModality(Modality.APPLICATION_MODAL);
		window.setTitle(title);
		window.setMinHeight(250);

		Label label = new Label(message);

		Button closeButton = new Button("Close the window");
		closeButton.setOnAction(e -> window.close());
		VBox layout = new VBox(10);
		layout.getChildren().addAll(label, closeButton);
		layout.setAlignment(Pos.CENTER);
		layout.setSpacing(40);

		Scene scene = new Scene(layout, 400, 150);
		window.setScene(scene);
		window.showAndWait();
	}

// ----------------------------------------------------------------------------------
// READ QUESTIONS RECORD FORM THE OBJECT FILE
//----------------------------------------------------------------------------------
	public void ReadFromObjectFile() {
		try {
			ObjectInputStream inObj = new ObjectInputStream(new FileInputStream("c:\\tmp\\QuestionsBank.dat"));
			row_index = 0;
			try {
				while (true) {
					Questions InQus = new Questions();
					InQus = (Questions) inObj.readObject();
					questionsArray.add(InQus);
					row_index++;
				}
			} catch (EOFException | ClassNotFoundException e) {
			}
			inObj.close();
		} catch (IOException e) {
			System.out.println("Please Check the Input File Name");
			Platform.exit();
		} finally {
		}
	}

// ----------------------------------------------------------------------------------
// WRITE THE QUESTIONS FROM ARRAYLIST TO THE OBJECTFILE
//----------------------------------------------------------------------------------
	public void WritToObjectFile() {
		try {
			FileOutputStream f = new FileOutputStream(new File("c:\\tmp\\QuestionsBank.dat"));
			ObjectOutputStream outObj = new ObjectOutputStream(f);

			// Write objects to file
			for (int i = 0; i <= row_index; i++) {
				Questions OutQ = new Questions();
				OutQ = questionsArray.get(i);
				if ((OutQ.QuestionText != "") && (OutQ.QuestionText != null))
					outObj.writeObject((Questions) OutQ);
			}
			outObj.close();
			f.close(); // setOnCloseRequest()
		} catch (IOException e) {
			System.out.println("Write not Completed");
		}
	}
}

//----------------------------------------------------------------------------------
// CREATE A QUESTIONS CLASS 
//----------------------------------------------------------------------------------

class Questions implements Serializable {
	String QuestionText, Ans, Choice1, Choice2, Choice3, Choice4;

	/** Construct a question with blank defaults */
	Questions() {
		this.QuestionText = "";
		this.Ans = "";
		this.Choice1 = "";
		this.Choice2 = "";
		this.Choice3 = "";
		this.Choice4 = "";

	}

	Questions(String newQus, String newAns, String newChs1, String newChs2, String newChs3, String newChs4) {
		this.QuestionText = newQus;
		this.Ans = newAns;
		this.Choice1 = newChs1;
		this.Choice2 = newChs2;
		this.Choice3 = newChs3;
		this.Choice4 = newChs4;
	}

	String getQuestions() {
		return QuestionText;
	}

	String getAns() {
		return Ans;
	}

	String getChoice1() {
		return Choice1;
	}

	String getChoice2() {
		return Choice2;
	}

	String getChoice3() {
		return Choice3;
	}

	String getChoice4() {
		return Choice4;
	}

}
