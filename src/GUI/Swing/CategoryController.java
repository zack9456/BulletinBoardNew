package GUI.Swing;

import Domain.DomainFacade;
import Domain.Model.Category;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JTextField;
import javax.swing.JTextPane;

public class CategoryController implements ActionListener {

    private final JTextField textField;
    private final JTextPane textPane;
    
    public CategoryController(JTextField textField, JTextPane textPane){
        this.textField = textField;
        this.textPane = textPane;
    }
    
    @Override
    public void actionPerformed(ActionEvent e){
        DomainFacade.getInstance().createCategory(this.textField.getText(), this.textPane.getText());
        
        Category[] catList = DomainFacade.getInstance().getCategoryList();
        
        textPane.setText(catList[catList.length - 1].getCategoryName());
    }
}
