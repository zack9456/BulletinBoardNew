package Domain;

import Domain.Model.Bulletin;
import Domain.Model.Category;

public interface IPriorityDecider
{
    public Bulletin createBulletin(String subject, String body, Category category);
}
