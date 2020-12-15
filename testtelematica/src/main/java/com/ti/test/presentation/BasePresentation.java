package com.ti.test.presentation;

import java.util.Scanner;

public abstract class BasePresentation {

    protected Scanner scanner;

    public void setScanner(Scanner scanner) {
        this.scanner = scanner;
    }

    public void present() {
        //to give extra space for each presentation
        System.out.println();
        showContent();
        endPresentation();
    }

    public void endPresentation() {
        System.out.println();
        System.out.println("========================================");
        showEndingContent();
    }

    protected abstract void showContent();

    protected abstract void showEndingContent();
}
