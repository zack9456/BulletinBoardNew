package GUI.Swing;

import Domain.DomainFacade;

public class Main {

    public static void main(String[] args) {
        DomainFacade.getInstance().initiateProgram();
        
        StartingScreen ss = new StartingScreen();
        ss.setTableColumnsAndRows();
        ss.addTableListener();
    }
}
