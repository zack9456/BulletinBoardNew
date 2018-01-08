package Domain;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JComboBox;
import javax.swing.table.DefaultTableModel;

import Data.DataFacade;
import Data.DatabaseUtil;
import Data.BulletinDTO;
import Data.BulletinHistoryDTO;
import Data.CategoryDTO;
import Domain.Model.Bulletin;
import Domain.Model.BulletinHistory;
import Domain.Model.Category;
import GUI.Swing.ComboboxToolTipRenderer;
import GUI.Swing.StartingScreen;

public class DomainFacade {
	private static DomainFacade instance = null;
	
	protected DomainFacade() {
	}
	
	public static DomainFacade getInstance() {
		if(instance == null) {
			instance = new DomainFacade();
		}
		return instance;
	}
	
    public void initiateProgram(){
    	DatabaseUtil db = new DatabaseUtil();
    	if(!db.databaseExisting())
    	{
    		db.createDatabase();
    	}
    	
        StartingScreen ss = new StartingScreen();
        ss.setTableColumnsAndRows();
        ss.addTableListener();
    }
    
    public String getBulletinStatus(int bulletinId) {
    	BulletinHistory bHistory = new BulletinHistory();
    	Bulletin bulletin = new Bulletin();
    	bulletin.setId(bulletinId);
    	bHistory.setBulletin(bulletin);
    	
    	BulletinHistoryDTO bHistoryDTO = (BulletinHistoryDTO) DataFacade.getInstance().getSingle(bHistory.convertToDTO());
    	
    	bHistory = new BulletinHistory(bHistoryDTO);
    	
    	return bHistory.getStatus();
    }
    
    public void createBulletin(String subject, String body, Category category, Boolean highPriority){
        PriorityFactory pf = new PriorityFactory();
        IPriorityDecider priorityDecider = pf.getPriority(highPriority);
        
        Bulletin newBulletin = priorityDecider.createBulletin(subject, body, category);
        
        newBulletin = new Bulletin((BulletinDTO) DataFacade.getInstance().save(newBulletin.convertToDTO()));
        
        createInitialBulletinHistory(newBulletin);
    }
    
    public JComboBox<Category> getCategoryList(JComboBox<Category> categoryList) {
    	 Category[] categoryArray = DomainFacade.getInstance().getCategoryArray();
         ComboboxToolTipRenderer renderer = new ComboboxToolTipRenderer();
         categoryList.setRenderer(renderer);
         List<String> tooltips = new ArrayList<>();
         
         for (Category category : categoryArray)
         {
             categoryList.addItem(category);
             tooltips.add(category.getDescription());
         }
         
         renderer.setTooltips(tooltips);
         
         return categoryList;
    }
    
    private Category[] getCategoryArray(){
    	Category cat = new Category();
    	
    	CategoryDTO[] catDTOArray = (CategoryDTO[]) DataFacade.getInstance().getAll(cat.convertToDTO());
    	Category[] categoryArray = new Category[catDTOArray.length];
    	int i = 0;
    	
        for (CategoryDTO catDTO : catDTOArray){
        	categoryArray[i] = new Category(catDTO);
        	i++;
        }
        
        return categoryArray;
    }
    
    public void createInitialBulletinHistory(Bulletin bulletin){
    	BulletinHistory bulletinHistory = new BulletinHistory(bulletin, States.NEW);
    	bulletinHistory.setId(Integer.MAX_VALUE);
        
    	DataFacade.getInstance().save(bulletinHistory.convertToDTO());
    }
    
    public void createBulletinHistory(int bulletinId, String state){
    	Bulletin bulletin = new Bulletin();
    	bulletin.setId(bulletinId);
    	
    	Bulletin selectedBulletin = new Bulletin((BulletinDTO) DataFacade.getInstance().getSingle(bulletin.convertToDTO()));
    	
    	BulletinHistory bulletinHistory = new BulletinHistory(selectedBulletin, state);
        
    	DataFacade.getInstance().save(bulletinHistory.convertToDTO());
    }
    
    private BulletinHistory[] getBulletinHistory(Bulletin bulletin) {
    	BulletinHistory bHistory = new BulletinHistory();
    	bHistory.setBulletin(bulletin);
    	
    	BulletinHistoryDTO[] bulletinHistoryDTO = (BulletinHistoryDTO[]) DataFacade.getInstance().getAll(bHistory.convertToDTO());
    	BulletinHistory[] bulletinHistory = new BulletinHistory[bulletinHistoryDTO.length];
    	int i = 0;
    	
    	for (BulletinHistoryDTO bHistoryDTO: bulletinHistoryDTO) {
    		bulletinHistory[i] = new BulletinHistory(bHistoryDTO);
    		i++;
    	}
    	
    	return bulletinHistory;
    }
    
    private Bulletin[] getBulletins() {
    	Bulletin bulletin = new Bulletin();
    	BulletinDTO[] bulletinDTOArr = (BulletinDTO[]) DataFacade.getInstance().getAll(bulletin.convertToDTO());
    	Bulletin[] bulletins = new Bulletin[bulletinDTOArr.length];
    	int i = 0;
    	
    	for (BulletinDTO bulletinDTO : bulletinDTOArr) {
    		bulletins[i] = new Bulletin(bulletinDTO);
    		i++;
    	}
    	
    	return bulletins;
    }

	public DefaultTableModel updateHistoryTable(DefaultTableModel historyDTM, int bulletinId) {
    	Bulletin bulletin = new Bulletin();
    	bulletin.setId(bulletinId);
    	
		if (historyDTM.getRowCount() > 0) {
            for (int i = historyDTM.getRowCount() - 1; i > -1; i--) {
            	historyDTM.removeRow(i);
            }
        }
        
        for (BulletinHistory bHistory: getBulletinHistory(bulletin))
        {
        	historyDTM.addRow(new Object[]{
        			bHistory.getStatus(),
        			 new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(bHistory.getDate())
        	}
            );
        }
		
        return historyDTM;
	}
	
	public DefaultTableModel addBulletinsToTableModel(DefaultTableModel bulletinDTM) {
        for (Bulletin bulletin: getBulletins())
        {
        	bulletinDTM.addRow(new Object[]{
                bulletin.getId(),
                bulletin.getPriority(), 
                bulletin.getCategoryName(), 
                bulletin.getSubject(), 
                bulletin.getBody()}
            );
        }
        
        return bulletinDTM;
	}

	public void createCategory(String categoryName, String categoryDescription) {
		Category cat = new Category(categoryName, categoryDescription);
		
		DataFacade.getInstance().save(cat.convertToDTO());
	}
}
