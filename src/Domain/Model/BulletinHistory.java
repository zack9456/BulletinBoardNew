package Domain.Model;

import java.sql.Timestamp;

import Data.BulletinHistoryDTO;

public class BulletinHistory {

    private int Id;
    private Timestamp Created;
    private String CurrentState;
    private Bulletin Bulletin;
    private Integer CurrentBulletinHistory;

    public BulletinHistory(Bulletin bulletin, String state) {
        this.Created = new Timestamp(System.currentTimeMillis());
        this.CurrentState = state;
        this.Bulletin = bulletin;
    }

    public BulletinHistory(BulletinHistoryDTO bulletinHistoryDTO) {
        this.Created = bulletinHistoryDTO.created;
        this.CurrentState = bulletinHistoryDTO.currentState;
        this.Id = bulletinHistoryDTO.id;
        this.Bulletin = new Bulletin(bulletinHistoryDTO.bulletinDTO);
        this.CurrentBulletinHistory = bulletinHistoryDTO.currentBulletinHistory;
    }

    public BulletinHistory() {
    }

    public BulletinHistoryDTO convertToDTO() {
        BulletinHistoryDTO bulletinHistoryDTO = new BulletinHistoryDTO();
        bulletinHistoryDTO.created = this.Created;
        bulletinHistoryDTO.currentState = this.CurrentState;
        bulletinHistoryDTO.id = this.Id;
        bulletinHistoryDTO.bulletinDTO = this.Bulletin.convertToDTO();
        if (this.CurrentBulletinHistory != null) {
            bulletinHistoryDTO.currentBulletinHistory = this.CurrentBulletinHistory;
        } else {
            bulletinHistoryDTO.currentBulletinHistory = null;
        }

        return bulletinHistoryDTO;
    }

    public String getStatus() {
        return this.CurrentState;
    }

    public Timestamp getDate() {
        return this.Created;
    }

    public Integer getCurrentBulletinHistory() {
        return this.CurrentBulletinHistory;
    }

    public void setBulletin(Bulletin bulletin) {
        this.Bulletin = bulletin;
    }

    public void setId(int bhId) {
        this.Id = bhId;
    }
}
