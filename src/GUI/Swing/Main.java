package GUI.Swing;

import Data.DataFacade;
import Domain.DomainFacade;

public class Main 
{
    @SuppressWarnings("unused")
	public static void main(String[] args) 
    {
        DomainFacade domf = DomainFacade.getInstance();
        DataFacade dataF = DataFacade.getInstance();
        domf.initiateProgram();
    }
}
