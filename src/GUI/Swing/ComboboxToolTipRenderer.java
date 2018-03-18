package GUI.Swing;

import javax.swing.*;
import java.awt.*;
import java.util.List;

//lite gui-kod för att få snyggare presentation som hittades på stackoverflow
//iom att det endast är presentation och ingen logik känns det ok att låna den
@SuppressWarnings("serial")
public class ComboboxToolTipRenderer extends DefaultListCellRenderer {

    List<String> tooltips;

    @Override
    public Component getListCellRendererComponent(JList<?> list, Object value,
            int index, boolean isSelected, boolean cellHasFocus) {
        JComponent comp = (JComponent) super.getListCellRendererComponent(list,
                value, index, isSelected, cellHasFocus);

        if (-1 < index && null != value && null != tooltips) {
            list.setToolTipText(tooltips.get(index));
        }
        return comp;
    }

    public void setTooltips(List<String> tooltips) {
        this.tooltips = tooltips;
    }
}
