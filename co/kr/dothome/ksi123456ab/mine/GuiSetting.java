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
            msgBox("�߸��� �����Դϴ�.");
            return;
        }
        try
        {
            blocks = Integer.parseInt(txtLength.getText());
        }
        catch (NumberFormatException ex)
        {
            msgBox("�߸��� �����Դϴ�.");
            return;
        }
        
        if (mines < 5 || mines > 300)
        {
            msgBox("���� ������ 5 ~ 300 ���� ������ �� �ֽ��ϴ�.");
            return;
        }
        if (blocks < 5 || blocks > 20)
        {
            msgBox("�� ���̴� 5 ~ 20 ���� ������ �� �ֽ��ϴ�.");
            return;
        }
        if ((blocks * blocks) <= mines)
        {
            msgBox("���� ������ �� �������� Ů�ϴ�.");
            return;
        }
        
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setContentText("������ ������ �����ұ��? ������ �ϸ� ������ �ʱ�ȭ�˴ϴ�.");
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
