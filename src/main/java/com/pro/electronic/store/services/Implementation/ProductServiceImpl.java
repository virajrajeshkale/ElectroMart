package com.pro.electronic.store.services.Implementation;

import com.pro.electronic.store.dtos.PageableResponse;
import com.pro.electronic.store.dtos.ProductDto;
import com.pro.electronic.store.entites.Category;
import com.pro.electronic.store.entites.Product;
import com.pro.electronic.store.exceptions.ResourceNotFoundException;
import com.pro.electronic.store.helper.PageableHelper;
import com.pro.electronic.store.repositories.CategoryRepository;
import com.pro.electronic.store.repositories.ProductRepository;
import com.pro.electronic.store.services.ProductSerivce;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ProductServiceImpl implements ProductSerivce {

    @Autowired
    private  ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Value("${products.image.path}")
    private  String ImagePath;
    @Autowired
    private ModelMapper modelMapper;
    @Override
    public ProductDto create(ProductDto productDto) {

        //generate product id
        Product product = modelMapper.map(productDto, Product.class);
        String id = UUID.randomUUID().toString();
        product.setProductId(id);

        //add product date
        product.setAddedDate(new Date());

        Product saveproduct = productRepository.save(product);
        return modelMapper.map(saveproduct,ProductDto.class);
    }

    @Override
    public void delete(String productId) {
        Product product = productRepository.findById(productId).orElseThrow(() -> new ResourceNotFoundException("Given Product id Product is Not Found..!"));

        String imagepath = ImagePath+product.getProductImage();

        try
        {
            Path path = Paths.get(imagepath);
            Files.delete(path);
        } catch (NoSuchFileException e) {

            e.printStackTrace();
        } catch (IOException ex)
        {
            ex.printStackTrace();
        }

        productRepository.delete(product);

    }

    @Override
    public ProductDto update(ProductDto productDto, String productId) {
        Product product = productRepository.findById(productId).orElseThrow(() -> new ResourceNotFoundException("Product is not found for given id..!!"));
        product.setTitle(productDto.getTitle());
        product.setDescription(productDto.getDescription());
        product.setLive(productDto.isLive());
        product.setPrice(productDto.getPrice());
        product.setDiscountedPrice(productDto.getDiscountedPrice());
        product.setQuantity(productDto.getQuantity());
        product.setStock(productDto.getStock());
        product.setProductImage(productDto.getProductImage());

        Product updatedproduct = productRepository.save(product);
        return modelMapper.map(updatedproduct,ProductDto.class);
    }

    @Override
    public PageableResponse<ProductDto> getAllProduct(int pageNumber, int pageSize, String sortBy, String sortDir) {
        Sort sort = (sortDir.equalsIgnoreCase("desc")?(Sort.by(sortBy).descending()):(Sort.by(sortBy).ascending()));
        Pageable pageable = PageRequest.of(pageNumber,pageSize,sort);
        Page<Product> page = productRepository.findAll(pageable);
        return PageableHelper.getPageableResponse(page,ProductDto.class);
    }


    @Override
    @GetMapping("/{productId}")
    public ProductDto getSingleProduct(@PathVariable String productId) {
        Product product = productRepository.findById(productId).orElseThrow(() -> new ResourceNotFoundException("Product not found for given user Id..!"));
        return modelMapper.map(product,ProductDto.class);
    }

    @Override
    public  PageableResponse<ProductDto>  searchByTitle(String subTitle, int pageNumber, int pageSize, String sortBy, String sortDir) {
        Sort sort = (sortDir.equalsIgnoreCase("desc")?(Sort.by(sortBy).descending()):(Sort.by(sortBy).ascending()));
        Pageable pageable = PageRequest.of(pageNumber,pageSize,sort);
        Page<Product> page = productRepository.findByTitleContaining(subTitle,pageable);
        return PageableHelper.getPageableResponse(page,ProductDto.class);

    }

    @Override
    public  PageableResponse<ProductDto>  liveProductList(int pageNumber, int pageSize, String sortBy, String sortDir) {

        Sort sort = (sortDir.equalsIgnoreCase("desc")?(Sort.by(sortBy).descending()):(Sort.by(sortBy).ascending()));
        Pageable pageable = PageRequest.of(pageNumber,pageSize,sort);
        Page<Product> live_product = productRepository.findByLiveTrue(pageable);
        return PageableHelper.getPageableResponse(live_product,ProductDto.class);


    }

    @Override
    public ProductDto createWithCategory(ProductDto productDto, String categoryId) {

        Category category = categoryRepository.findById(categoryId).orElseThrow(() -> new ResourceNotFoundException("Category Not found for given id..!!"));
//        productDto.setCategory(category);
        Product product = modelMapper.map(productDto, Product.class);
        String id = UUID.randomUUID().toString();
        product.setProductId(id);

        //add product date
        product.setAddedDate(new Date());
        product.setCategory(category);
        Product saveproduct;
        saveproduct = productRepository.save(product);
        return modelMapper.map(saveproduct,ProductDto.class);

    }

    @PutMapping
    @Override
    public ProductDto updateCategory(String productId, String categoryId) {
        Product product = productRepository.findById(productId).orElseThrow(() -> new ResourceNotFoundException("For give id Product is Not found..!!"));
        Category category = categoryRepository.findById(categoryId).orElseThrow(() -> new ResourceNotFoundException("category of given id not found"));
        product.setCategory(category);
        Product savedproduct = productRepository.save(product);

        return modelMapper.map(savedproduct,ProductDto.class);
    }

    //method to get product of same category
    @Override
    public PageableResponse<ProductDto> getAllProductOfCategory(String categoryId,int pageNumber,int pageSize,String sortBy,String sortDir) {
        Category category = categoryRepository.findById(categoryId).orElseThrow(() -> new ResourceNotFoundException("For given id  Category is not found..!"));
        Sort sort = (sortDir.equalsIgnoreCase("desc")?(Sort.by(sortBy).descending()):(Sort.by(sortBy).ascending()));
        Pageable pageable = PageRequest.of(pageNumber,pageSize,sort);
        Page<Product> byCategory = productRepository.findByCategory(category,pageable);
        return PageableHelper.getPageableResponse(byCategory,ProductDto.class);
    }
}
