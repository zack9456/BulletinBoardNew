package Domain.Model;

import Data.BulletinDTO;

public class Bulletin {

    private int Id;
    private String Body;
    private String Subject;
    private Category Category;
    private String Priority;

    public Bulletin(String subject, String body, Category category, String priority) {
        this.Body = body;
        this.Subject = subject;
        this.Category = category;
        this.Priority = priority;
    }

    public Bulletin(BulletinDTO bulletin) {
        this.Body = bulletin.body;
        this.Subject = bulletin.subject;
        this.Category = new Category(bulletin.categoryDTO);
        this.Priority = bulletin.priority;
        this.Id = bulletin.id;
    }

    public Bulletin() {
    }

    public BulletinDTO convertToDTO() {
        BulletinDTO bulletinDTO = new BulletinDTO();
        bulletinDTO.body = this.Body;
        if (this.Category != null) {
            bulletinDTO.categoryDTO = this.Category.convertToDTO();
        }
        bulletinDTO.id = this.Id;
        bulletinDTO.priority = this.Priority;
        bulletinDTO.subject = this.Subject;

        return bulletinDTO;
    }

    public void setId(int bulletinId) {
        this.Id = bulletinId;
    }

    public String getId() {
        return Integer.toString(this.Id);
    }

    public String getBody() {
        return this.Body;
    }

    public String getSubject() {
        return this.Subject;
    }

    public String getCategoryName() {
        return this.Category.getCategoryName();
    }

    public String getPriority() {
        return this.Priority;
    }
}
