package org.sozongroup.pdfunsigned.app;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.sozongroup.pdfunsigned.common.HandleResult;
import org.sozongroup.pdfunsigned.util.PdfToImageAndBack;
import org.sozongroup.pdfunsigned.util.PdfUnsignedUtils;

import java.io.File;

/**
 * @Author Xsgard
 * @Description TODO
 * @Date 2024/12/25 10:36
 * @Version 1.0
 */

public class PdfUploadApp extends Application {
    @Override
    public void start(Stage primaryStage) {
        // 创建文本框用于显示文件路径
        TextField filePathField = new TextField();
        filePathField.setPromptText("选择一个文件...");
        filePathField.setEditable(false);
        filePathField.setStyle("-fx-background-color: #ffffff; -fx-border-radius: 8px; -fx-padding: 10px; -fx-border-color: #cccccc;");

        // 创建选择文件按钮
        Button chooseFileButton = new Button("选择文件");
        // 修复按钮悬停效果的样式
        chooseFileButton.setStyle("-fx-background-color: linear-gradient(to right, #4CAF50, #45A049);  -fx-text-fill: white; -fx-font-size: 14px; -fx-padding: 10px 20px; -fx-border-radius: 8px;");
        chooseFileButton.setOnMouseEntered(e -> chooseFileButton.setStyle("-fx-background-color: #45A049;  -fx-text-fill: white; -fx-font-size: 14px; -fx-padding: 10px 20px; -fx-border-radius: 8px;"));
        chooseFileButton.setOnMouseExited(e -> chooseFileButton.setStyle("-fx-background-color: linear-gradient(to right, #4CAF50, #45A049);  -fx-text-fill: white; -fx-font-size: 14px; -fx-padding: 10px 20px; -fx-border-radius: 8px;"));

        // 创建提交按钮
        Button submitButton = new Button("执行");
        submitButton.setStyle("-fx-background-color: linear-gradient(to right, #2196F3, #1E88E5); -fx-text-fill: white; -fx-font-size: 14px; -fx-padding: 10px 20px; -fx-border-radius: 8px;");
        submitButton.setOnMouseEntered(e -> submitButton.setStyle("-fx-background-color: #1E88E5;  -fx-text-fill: white; -fx-font-size: 14px; -fx-padding: 10px 20px; -fx-border-radius: 8px;"));
        submitButton.setOnMouseExited(e -> submitButton.setStyle("-fx-background-color: linear-gradient(to right, #2196F3, #1E88E5); -fx-text-fill: white; -fx-font-size: 14px; -fx-padding: 10px 20px; -fx-border-radius: 8px;"));
        // 创建文件选择器
        FileChooser fileChooser = new FileChooser();

        // 选择文件按钮事件
        chooseFileButton.setOnAction(event -> {
            File selectedFile = fileChooser.showOpenDialog(primaryStage);
            if (selectedFile != null) {
                filePathField.setText(selectedFile.getAbsolutePath());
            } else {
                filePathField.setText("未选择文件");
            }
        });

        // 单选框：选择解密模式
        ToggleGroup decryptModeGroup = new ToggleGroup();
        RadioButton mode1 = new RadioButton("模式1（去除PDF签名）");
        mode1.setFont(Font.font("Arial", 14));
        mode1.setToggleGroup(decryptModeGroup);
        mode1.setSelected(true); // 默认选中模式1

        RadioButton mode2 = new RadioButton("模式2（转为图片模式）");
        mode2.setFont(Font.font("Arial", 14));
        mode2.setToggleGroup(decryptModeGroup);

        // 提交按钮事件
        submitButton.setOnAction(event -> {
            String filePath = filePathField.getText();
            if (filePath.isEmpty() || filePath.equals("未选择文件")) {
                showAlert(Alert.AlertType.WARNING, "警告", "请先选择文件后再提交！");
            } else {
                RadioButton selectedMode = (RadioButton) decryptModeGroup.getSelectedToggle();
                String mode = selectedMode.getText();
                System.out.println(mode);

                if (mode.contains("模式1")) {
                    //去除PDF的签名，授予完全权限
                    HandleResult r = PdfUnsignedUtils.removePDFSignature(filePath);
                    showAlert(r.getAlertType(), r.getTitle(), r.getMessage());
                }
                if (mode.contains("模式2")) {
                    //PDF转为图片再转PDF
                    HandleResult r = PdfToImageAndBack.handlePDF(filePath);
                    showAlert(r.getAlertType(), r.getTitle(), r.getMessage());
                }
                filePathField.setText("未选择文件");
            }
        });

        // 创建标题
        Label titleLabel = new Label("PDF解密工具");
        titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 28));
        titleLabel.setTextFill(new LinearGradient(0, 0, 1, 0, true, null,
                new Stop(0, Color.DARKSLATEBLUE), new Stop(1, Color.DARKCYAN)));

        // 文件选择布局
        HBox fileSelectionBox = new HBox(10, filePathField, chooseFileButton);
        fileSelectionBox.setAlignment(Pos.CENTER);

        // 解密模式布局
        VBox modeBox = new VBox(10, new Label("选择执行模式："), mode1, mode2);
        modeBox.setAlignment(Pos.CENTER_LEFT);
        modeBox.setStyle("-fx-padding: 10; -fx-border-color: #cccccc; -fx-border-radius: 8px; -fx-background-color: #ffffff; -fx-background-radius: 8px;");

        // 主布局
        VBox root = new VBox(30, titleLabel, fileSelectionBox, modeBox, submitButton);
        root.setAlignment(Pos.CENTER);
        root.setStyle("-fx-padding: 30; -fx-background-color: #f9f9f9; -fx-border-radius: 12px;");
        root.setPadding(new Insets(20));

        // 设置场景
        Scene scene = new Scene(root, 600, 400);
        primaryStage.setTitle("PDF解密工具");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    // 弹窗工具方法
    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
