package Domain.Model;

import Data.CategoryDTO;

public class Category {

    private String CategoryName;
    private String Description;
    private int Id;

    public Category(String categoryName, String description) {
        this.CategoryName = categoryName;
        this.Description = description;
    }

    public Category(CategoryDTO category) {
        this.CategoryName = category.categoryName;
        this.Description = category.description;
        this.Id = category.id;
    }

    public Category() {
    }

    public CategoryDTO convertToDTO() {
        CategoryDTO categoryDTO = new CategoryDTO();
        categoryDTO.categoryName = this.CategoryName;
        categoryDTO.description = this.Description;
        categoryDTO.id = this.Id;

        return categoryDTO;
    }

    public String getCategoryName() {
        return this.CategoryName;
    }

    public String getDescription() {
        return this.Description;
    }

    @Override
    public String toString() {
        return this.CategoryName;
    }
}
