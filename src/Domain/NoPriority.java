package Domain;

import Domain.Model.Bulletin;
import Domain.Model.Category;

public class NoPriority implements IPriorityDecider {

    @Override
    public Bulletin createBulletin(String subject, String body, Category category) {
        return new Bulletin(subject, body, category, Priority.NORMAL);
    }
}
