/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package run;

import controller.SocketHandler;

import view.LoginView;
import view.RegisterView;
import view.ListView;

/**
 *
 * @author hieun
 */
public class ClientRun {

    public enum SceneName {
        LOGIN,
        REGISTER,
        LIST,
    }
    // scenes
    public static LoginView loginView;
    public static RegisterView registerView;
    public static ListView listView;

    // controller 
    public static SocketHandler socketHandler;

    public ClientRun() {
        socketHandler = new SocketHandler();
        initScene();
        
        //connect server
        new Thread(() -> {
            // call controller
            String result = ClientRun.socketHandler.connect("localhost", 2000);

            // check result
            if (result.equals("success")) {
                openScene(SceneName.LOGIN);
            } else {
                String failedMsg = result.split(";")[1];
                System.out.println(failedMsg);
            }
        }).start();
    }
    
    public void initScene() {
        loginView = new LoginView();
        registerView = new RegisterView();
        listView = new ListView();
    }
    
    public static void openScene(SceneName sceneName) {
        if (null != sceneName) {
            switch (sceneName) {
                case LOGIN:
                    loginView.setVisible(true);
                    break;
                case REGISTER:
                    registerView.setVisible(true);
                    break;
                case LIST:
                    if (listView == null) {
                        listView = new ListView();
                    }
                    listView.loadUserList();
                    listView.setVisible(true);
                    break;
                default:
                    break;
            }
        }
    }
    
    public static void closeScene(SceneName sceneName) {
        if (null != sceneName) {
            switch (sceneName) {
                case LOGIN:
                    loginView.dispose();
                    break;
                case REGISTER:
                    registerView.dispose();
                    break;
                case LIST:
                    listView.dispose();
                    break;
                default:
                    break;
            }
        }
    }
    
    public static void closeAllScene() {
        loginView.dispose();
    }

    public static void main(String[] args) {
        new ClientRun();

    }
}
