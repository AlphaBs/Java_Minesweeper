package co.kr.dothome.ksi123456ab.mine;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class GuiSetting implements Initializable
{
    @FXML
    private Button btnOK;
    @FXML
    private TextField txtMine;
    @FXML
    private TextField txtLength;
    
    @Override
    public void initialize(URL location , ResourceBundle resources)
    {
        Setting.isre = false;
        txtMine.setText(Setting.Mine_Count.toString());
        txtLength.setText(Setting.Map_Length.toString());
        btnOK.setOnMouseClicked(event->{BtnOKClick();});
    }
    
    Integer mines = 0;
    Integer blocks = 0;
    
    private void BtnOKClick()
    {
        
        try
        {
            mines = Integer.parseInt(txtMine.getText());
        }
        catch (NumberFormatException ex)
        {
            msgBox("잘못된 숫자입니다.");
            return;
        }
        try
        {
            blocks = Integer.parseInt(txtLength.getText());
        }
        catch (NumberFormatException ex)
        {
            msgBox("잘못된 숫자입니다.");
            return;
        }
        
        if (mines < 5 || mines > 300)
        {
            msgBox("지뢰 갯수는 5 ~ 300 까지 설정할 수 있습니다.");
            return;
        }
        if (blocks < 5 || blocks > 20)
        {
            msgBox("블럭 길이는 5 ~ 20 까지 설정할 수 있습니다.");
            return;
        }
        if ((blocks * blocks) <= mines)
        {
            msgBox("지뢰 갯수가 블럭 갯수보다 큽니다.");
            return;
        }
        
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setContentText("정말로 설정을 저장할까요? 저장을 하면 게임이 초기화됩니다.");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK)
        {            
            Setting.Map_Length = blocks;
            Setting.Mine_Count = mines;
            Setting.isre = true;
            ((Stage)btnOK.getScene().getWindow()).close();
        }
    }
    
    void msgBox(String text)
    {
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setContentText(text);
        alert.showAndWait();
    }
}
