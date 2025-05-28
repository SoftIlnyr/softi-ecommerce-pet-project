package com.softi.productservice.changelog;

import com.github.cloudyrock.mongock.ChangeLog;
import com.github.cloudyrock.mongock.ChangeSet;
import com.mongodb.client.MongoDatabase;
import com.softi.productservice.models.Category;
import com.softi.productservice.models.Product;
import com.softi.productservice.repositories.CategoryRepository;
import com.softi.productservice.repositories.ProductRepository;

import java.util.List;

@ChangeLog(order = "001")
public class InitChangelog {

    private Category categoryElectronics;

    private Category categoryPhones;

    private Product productIPhone14;

    private Product productGooglePixel7;

    @ChangeSet(order = "001", id = "001_drop_database", author = "softi", runAlways = true)
    public void dropDatabase(MongoDatabase db) {
        db.drop();
    }

    @ChangeSet(order = "002", id = "002_init_categories", author = "softi", runAlways = true)
    public void initCategories(CategoryRepository categoryRepository) {
        Category tempCategoryElectronics = new Category();
        tempCategoryElectronics.setName("Electronics");
        categoryElectronics = categoryRepository.save(tempCategoryElectronics);

        Category tempCategoryPhones = new Category();
        tempCategoryPhones.setName("Phones");
        tempCategoryPhones.setParentCategory(categoryElectronics);
        categoryPhones = categoryRepository.save(tempCategoryPhones);
    }
}
