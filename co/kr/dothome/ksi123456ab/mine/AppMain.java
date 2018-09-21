package co.kr.dothome.ksi123456ab.mine;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Optional;
import java.util.Random;
import java.util.ResourceBundle;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import javafx.application.*;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.*;

public class AppMain extends Application implements Initializable
{
    @Override
    public void start(Stage primaryStage) throws Exception
    {
        stage = primaryStage;
        FXMLLoader loader = new FXMLLoader(getClass().getResource("AppMain.fxml"));
        Pane pane = (Pane)loader.load();
        Scene scene = new Scene(pane);


        stage.setResizable(false);
        primaryStage.setTitle("지뢰찾기");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    void msgBox(String text)
    {
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setContentText(text);
        alert.showAndWait();
    }

    private Stage stage;
    @FXML private Button btnRestart;
    @FXML private Button btnSetting;
    @FXML private Label lvMine;
    @FXML private FlowPane fMap;

    int fdme = 0;
    int getFindMine()
    {
        return fdme;
    }
    void setFindMine(int value)
    {
        fdme = value;
        if (fdme == Setting.Mine_Count && flagcount == 0)
        {
            msgBox("승리!");
            fMap.setDisable(true);
        }
    }

    int flagcount = 0;
    int getFlagCount()
    {
        return flagcount;
    }
    void setFlagCount(int value)
    {
        flagcount = value;
        lvMine.setText("지뢰 : " + new Integer(value).toString() + "개");
    }

    @Override
    public void initialize(URL location , ResourceBundle resources)
    {
        createMap();
        lvMine.setText("지뢰 : " + Setting.Mine_Count + "개");
        btnRestart.setOnMouseClicked(event -> {btnRestartClick();});
        btnSetting.setOnMouseClicked(event -> {btnSettingClick();});
    }

    void createMap()
    {
        setFindMine(0);
        flagcount = Setting.Mine_Count;
        setFlagCount(Setting.Mine_Count);

        //길이 = (지뢰길이 * 지뢰수) + (5 * (지뢰수 + 1))
        int h = (40 * Setting.Map_Length) + (5 *(Setting.Map_Length + 1));
        fMap.setPrefWidth(h);
        fMap.setPrefHeight(h);
        
        try
        {
        Stage sta = (Stage)fMap.getScene().getWindow();
        sta.setWidth(h+20);
        sta.setHeight(h+100);
        } catch (Exception ex) {ex.printStackTrace();}

        fMap.setDisable(false);
        fMap.getChildren().clear();
        for (int i = 1; i <= Math.pow(Setting.Map_Length, 2); i++)
        {
            Button btn = new Button();
            btn.setBackground(new Background(new BackgroundFill(Color.LIGHTGRAY, new CornerRadii(5), new Insets(1))));
            btn.setTextFill(Color.BLACK);
            FlowPane.setMargin(btn, new Insets(0,0,5,5));
            btn.setPrefSize(40, 40);
            btn.setOnMouseClicked(event->{btnClick(event);});
            fMap.getChildren().add(btn);
        }

        Random rnd = new Random();
        ArrayList<Integer> mineList = new ArrayList<Integer>();
        for (int i = 1; i <= Setting.Mine_Count; i++)
        {
            int mPos = 0;
            do
            {
                mPos = rnd.nextInt((int)Math.pow(Setting.Map_Length, 2) - 1) + 1;
            } while (mineList.contains(mPos));
            mineList.add(mPos);

            Button cnt = (Button)fMap.getChildren().toArray()[mPos -1];
            cnt.setTextFill(Color.RED);
        }

        System.out.println("만들어진 지뢰 : " + mineList.toString());
    }

    void btnRestartClick()
    {
        Alert msg = new Alert(AlertType.NONE);
        msg.setContentText("정말로 재시작 할까요?");
        msg.setTitle("재시작");
        msg.getButtonTypes().add(ButtonType.YES);
        msg.getButtonTypes().add(ButtonType.NO);
        msg.showAndWait();
        if (msg.getResult().equals(ButtonType.YES))
            createMap();
    }

    void btnClick(MouseEvent args)
    {
        Object soruce = args.getSource();

        if (true)
        //if (soruce instanceof Button)
        {  //check that the source is really a button
            Button btn = (Button) soruce;  //cast the source to a button
            if (args.getButton().equals(MouseButton.PRIMARY))
            {
                if (btn.getBackground().getImages().size() != 0)
                {
                    return;
                }

                if (btn.getTextFill().equals(Color.RED))
                {
                    for (Node item : fMap.getChildren())
                    {
                        Button ibt = (Button)item;
                        if (ibt.getTextFill().equals(Color.RED))
                        {
                               ibt.setBackground(new Background(new BackgroundFill(Color.RED, new CornerRadii(10), Insets.EMPTY)));
                        }
                    }
                    msgBox("실패");
                    createMap();
                    return;
                }

                int p = fMap.getChildren().indexOf(btn) + 1;
                checkMine(btn , p);
            }
            else if (args.getButton().equals(MouseButton.SECONDARY))
            {
                if (btn.getBackground().getImages().size() == 0)
                {
                    BackgroundImage backgroundImage = new BackgroundImage( new Image( getClass().getResource("flag.png").toExternalForm()), BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT);
                    Background background = new Background(backgroundImage);
                    btn.setBackground(background);

                    setFlagCount(getFlagCount() - 1);
                    if (btn.getTextFill().equals(Color.RED))
                        setFindMine(getFindMine() + 1);
                }
                else
                {
                    btn.setBackground(new Background(new BackgroundFill(Color.LIGHTGRAY, new CornerRadii(5), new Insets(1))));

                    setFlagCount(getFlagCount() + 1);
                    if (btn.getTextFill().equals(Color.RED))
                        setFindMine(getFindMine() - 1);
                }
            }
        }
    }

    ArrayList<Integer> posList = new ArrayList<Integer>();
    void checkMine(int pos)
    {
        Button btn = (Button)fMap.getChildren().toArray()[pos];
        checkMine(btn , ++pos);
    }

    void checkMine(Button btn , int p)
    {
        if (btn.isDisabled() == true)
        {
            msgBox("test");
            return;
        }

        btn.setDisable(true);
        btn.setBackground(new Background(new BackgroundFill(Color.GHOSTWHITE, new CornerRadii(10), Insets.EMPTY)));
        btn.setTextFill(Color.BLACK);

        int c = 0;

        if (p % Setting.Map_Length != 1 && g(p - 1))
            c++;
        if (p % Setting.Map_Length != 0 && g(p + 1))
            c++;
        if (g(p + Setting.Map_Length))
            c++;
        if (g(p - Setting.Map_Length))
            c++;
        if (p % Setting.Map_Length != 1 && g(p - (Setting.Map_Length+1)))
            c++;
        if (p % Setting.Map_Length != 0 && g(p - (Setting.Map_Length-1)))
            c++;
        if (p % Setting.Map_Length != 0 && g(p + (Setting.Map_Length+1)))
            c++;
        if (p % Setting.Map_Length != 1 && g(p + (Setting.Map_Length-1)))
            c++;

        if (c == 0)
        {
            if (p % Setting.Map_Length != 1)
                zcheckMine(p - 1);
            if (p % Setting.Map_Length != 0)
                zcheckMine(p + 1);
            if (!(p + Setting.Map_Length > Math.pow(Setting.Map_Length , 2)))
                zcheckMine(p + Setting.Map_Length);
            if (!(p - Setting.Map_Length < 0))
                zcheckMine(p - Setting.Map_Length);
            if (p % Setting.Map_Length != 1)
                zcheckMine(p - (Setting.Map_Length+1));
            if (p % Setting.Map_Length != 0)
                zcheckMine(p - (Setting.Map_Length-1));
            if (p % Setting.Map_Length != 0)
                zcheckMine(p + (Setting.Map_Length+1));
            if (p % Setting.Map_Length != 1)
                zcheckMine(p + (Setting.Map_Length-1));
        }
        else
        {
            Integer cstr = new Integer(c);
            btn.setText(cstr.toString());
        }
    }

    void zcheckMine(int pos)
    {
        if (pos > Math.pow(Setting.Map_Length, 2) || pos < 1)
            return;

        Button btn = (Button)fMap.getChildren().toArray()[pos-1];
        if (btn.getTextFill().equals(Color.RED) || btn.isDisabled() == true)
            return;
        else
            checkMine(btn , pos);
    }

    boolean g(int ind)
    {
        if (ind < 1)
            return false;

        if (ind > Math.pow(Setting.Map_Length, 2))
            return false;

        Button control = (Button)fMap.getChildren().toArray()[ind-1];
        return control.getTextFill().equals(Color.RED);
    }
    
    void btnSettingClick()
    {
        try
        {
            Stage dialog = new Stage(StageStyle.UTILITY);
            dialog.initModality(Modality.APPLICATION_MODAL);
            dialog.initOwner(stage);
            
            Parent parent = FXMLLoader.load(getClass().getResource("SettingWindow.fxml"));
            Scene scene = new Scene(parent);
            
            dialog.setScene(scene);
            dialog.showAndWait();
            
            if(Setting.isre==true)
                createMap();
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }
   
}
