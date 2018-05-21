package Domain;

import Data.DataFacade;
import Data.DatabaseUtil;
import Data.BulletinDTO;
import Data.BulletinHistoryDTO;
import Data.CategoryDTO;
import Domain.Model.Bulletin;
import Domain.Model.BulletinHistory;
import Domain.Model.Category;

public class DomainFacade {

    private static DomainFacade instance = null;

    protected DomainFacade() {
    }

    public static DomainFacade getInstance() {
        if (instance == null) {
            instance = new DomainFacade();
        }
        return instance;
    }

    public void initiateProgram() {
        DatabaseUtil db = new DatabaseUtil();
        if (!db.databaseExisting()) {
            db.createDatabase();
        }
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

    public void createBulletin(String subject, String body, Category category, Boolean highPriority) {
        PriorityFactory pf = new PriorityFactory();
        IPriorityDecider priorityDecider = pf.getPriority(highPriority);

        Bulletin newBulletin = priorityDecider.createBulletin(subject, body, category);

        newBulletin = new Bulletin((BulletinDTO) DataFacade.getInstance().save(newBulletin.convertToDTO()));

        createInitialBulletinHistory(newBulletin);
    }

    public Category[] getCategoryList() {
        return DomainFacade.getInstance().getCategoryArray();
    }

    private Category[] getCategoryArray() {
        Category cat = new Category();

        CategoryDTO[] catDTOArray = (CategoryDTO[]) DataFacade.getInstance().getAll(cat.convertToDTO());
        Category[] categoryArray = new Category[catDTOArray.length];
        int i = 0;

        for (CategoryDTO catDTO : catDTOArray) {
            categoryArray[i] = new Category(catDTO);
            i++;
        }

        return categoryArray;
    }

    public void createInitialBulletinHistory(Bulletin bulletin) {
        BulletinHistory bulletinHistory = new BulletinHistory(bulletin, States.NEW);
        bulletinHistory.setId(Integer.MAX_VALUE);

        DataFacade.getInstance().save(bulletinHistory.convertToDTO());
    }

    public void createBulletinHistory(int bulletinId, String state) {
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

        for (BulletinHistoryDTO bHistoryDTO : bulletinHistoryDTO) {
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

    public BulletinHistory[] getBulletinHistoryArray(int bulletinId) {
        Bulletin bulletin = new Bulletin();
        bulletin.setId(bulletinId);

        return getBulletinHistory(bulletin);
    }

    public Bulletin[] getBulletinArray() {
        return getBulletins();
    }

    public void createCategory(String categoryName, String categoryDescription) {
        Category cat = new Category(categoryName, categoryDescription);

        DataFacade.getInstance().save(cat.convertToDTO());
    }
}
