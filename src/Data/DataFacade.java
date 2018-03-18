package Data;

public class DataFacade {

    private final CategoryBroker catBroker;
    private final BulletinBroker bulBroker;
    private final BulletinHistoryBroker bHisBroker;
    private static DataFacade instance = null;

    protected DataFacade() {
        catBroker = new CategoryBroker();
        bulBroker = new BulletinBroker();
        bHisBroker = new BulletinHistoryBroker();
    }

    public static DataFacade getInstance() {
        if (instance == null) {
            instance = new DataFacade();
        }
        return instance;
    }

    public DTO getSingle(DTO dto) {
        DTO returnDTO = null;

        if (dto instanceof CategoryDTO) {
            returnDTO = catBroker.getSingle(dto);
        } else if (dto instanceof BulletinDTO) {
            returnDTO = bulBroker.getSingle(dto);
        } else if (dto instanceof BulletinHistoryDTO) {
            BulletinHistoryDTO bHistoryDTO = (BulletinHistoryDTO) dto;
            BulletinDTO bulletinDTO = (BulletinDTO) getSingle(bHistoryDTO.bulletinDTO);

            returnDTO = bHisBroker.getSingle(bulletinDTO);
        }

        return returnDTO;
    }

    public DTO[] getAll(DTO dto) {
        DTO[] returnArray = null;

        if (dto instanceof CategoryDTO) {
            returnArray = catBroker.getAll(dto, returnArray);
        } else if (dto instanceof BulletinDTO) {
            returnArray = bulBroker.getAll(dto, returnArray);
        } else if (dto instanceof BulletinHistoryDTO) {
            BulletinHistoryDTO bHistoryDTO = (BulletinHistoryDTO) dto;
            DTO bulletin = (BulletinDTO) getSingle(bHistoryDTO.bulletinDTO);

            returnArray = bHisBroker.getAll(bulletin, returnArray);
        }

        return returnArray;
    }

    //iom att jag har 2 sparfunktioner för olika lägen för historiken kör jag en check på en till variabel för att avgöra läget
    //id skapas upp av databasen när objektet sparas, så det är ingenting som kommer att påverka någon logik
    public DTO save(DTO dto) {
        DTO returnDTO = null;

        if (dto instanceof CategoryDTO) {
            catBroker.save(dto);
        } else if (dto instanceof BulletinDTO) {
            returnDTO = bulBroker.save(dto);
        } else if (dto instanceof BulletinHistoryDTO && dto.id == Integer.MAX_VALUE) {
            bHisBroker.save(dto);
        } else if (dto instanceof BulletinHistoryDTO) {
            BulletinHistoryDTO bHistoryDTO = (BulletinHistoryDTO) dto;
            DTO currentLatestBH = getSingle(bHistoryDTO);

            DTO newBulletinHistoryDTO = bHisBroker.save(bHistoryDTO);

            bHisBroker.updateSecondLatestHistory(currentLatestBH, newBulletinHistoryDTO);
        }

        return returnDTO;
    }
}
