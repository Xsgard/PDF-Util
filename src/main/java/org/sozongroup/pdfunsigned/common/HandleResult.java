package org.sozongroup.pdfunsigned.common;

import javafx.scene.control.Alert;
import lombok.Data;

/**
 * @Author Xsgard
 * @Description 处理结果
 * @Date 2024/12/25 11:27
 * @Version 1.0
 */
@Data
public class HandleResult {
    private int code;

    private Alert.AlertType alertType;

    private String title;

    private String message;

    public HandleResult(int code, Alert.AlertType alertType, String title, String message) {
        this.code = code;
        this.alertType = alertType;
        this.title = title;
        this.message = message;
    }

    public static HandleResult success(String title, String message) {
        return new HandleResult(200, Alert.AlertType.INFORMATION, title, message);
    }

    public static HandleResult error(String title, String message) {
        return new HandleResult(500, Alert.AlertType.ERROR, title, message);
    }
}
