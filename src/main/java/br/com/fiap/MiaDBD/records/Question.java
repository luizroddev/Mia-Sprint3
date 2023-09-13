package br.com.fiap.MiaDBD.records;

public class Question {
    private String app;
    private String text;

    private Long userId;

    private Integer taskId;

    public String getApp() {
        return app;
    }

    public String getText() {
        return text;
    }

    public Long getUserId() {
        return userId;
    }

    public Integer getTaskId() {
        return taskId;
    }
}

