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

    @ChangeSet(order = "003", id = "003_init_products", author = "softi", runAlways = true)
    public void initProducts(ProductRepository productRepository) {
        Product tempProductIPhone14 = new Product();
        tempProductIPhone14.setName("IPhone 14");
        tempProductIPhone14.setCategoryIds(List.of(categoryPhones.getId()));
        tempProductIPhone14.setIsActive(true);
        productIPhone14 = productRepository.save(tempProductIPhone14);

        Product tempProductGooglePixel7 = new Product();
        tempProductGooglePixel7.setName("Google Pixel 7");
        tempProductGooglePixel7.setCategoryIds(List.of(categoryPhones.getId()));
        tempProductGooglePixel7.setIsActive(true);
        productGooglePixel7 = productRepository.save(tempProductGooglePixel7);
    }
}
