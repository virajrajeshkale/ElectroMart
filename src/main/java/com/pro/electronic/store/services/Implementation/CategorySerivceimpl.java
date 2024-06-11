package com.pro.electronic.store.services.Implementation;

import com.pro.electronic.store.dtos.CategoryDto;
import com.pro.electronic.store.dtos.PageableResponse;
import com.pro.electronic.store.entites.Category;
import com.pro.electronic.store.exceptions.ResourceNotFoundException;
import com.pro.electronic.store.helper.PageableHelper;
import com.pro.electronic.store.repositories.CategoryRepository;
import com.pro.electronic.store.services.CategoryService;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.UUID;

@Service
public class CategorySerivceimpl implements CategoryService {

    Logger  logger = LoggerFactory.getLogger(CategorySerivceimpl.class);

    @Autowired
    private  CategoryRepository categoryRepository;

    //for conversion dto to entity or vice versa
    @Autowired
    private ModelMapper modelMapper;

    @Value("${category.items.image.path}")
    private  String imagePath;
    @Override
    public CategoryDto Create(CategoryDto categoryDto) {
        //Create Auto generate if
        String randomId = UUID.randomUUID().toString();
        categoryDto.setCategoryId(randomId);

        Category category = modelMapper.map(categoryDto, Category.class);
        Category save = categoryRepository.save(category);
        return modelMapper.map(save,CategoryDto.class);
    }

    @Override
    public CategoryDto Update(CategoryDto categoryDto, String userId) {
        Category category = categoryRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User Not Found"));
        category.setTitle(categoryDto.getTitle());
        category.setDescription(categoryDto.getDescription());
        category.setCoverImage(categoryDto.getCoverImage());

        Category updated = categoryRepository.save(category);
      return   modelMapper.map(updated,CategoryDto.class);

    }

    @Override
    public void Delete(String userId) {

        //delete catrgory image
        //delete user profile image
        Category category = categoryRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User not Found for Given id..!"));
        String imagepath = imagePath+category.getCoverImage();

        try
        {
            Path path = Paths.get(imagepath);
            Files.delete(path);
        } catch (NoSuchFileException e) {
            logger.info("Image File Not Found for give User..!!");
            e.printStackTrace();
        } catch (IOException ex)
        {
            ex.printStackTrace();
        }

        categoryRepository.delete(category);
    }

    @Override
    public PageableResponse<CategoryDto> getAll(int pageNumber,int pageSize,String sortBy,String sortDir) {

        Sort sort = (sortDir.equalsIgnoreCase("desc"))    ?  (Sort.by(sortBy).descending()):(Sort.by(sortBy).ascending());

        Pageable pageable = PageRequest.of(pageNumber,pageSize,sort);
        Page<Category> page = categoryRepository.findAll(pageable);
        return PageableHelper.getPageableResponse(page, CategoryDto.class);
    }

    @Override
    public CategoryDto getSingle(String userId) {

        Category category = categoryRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("Given Category id user is Not Found..!!"));
        return modelMapper.map(category,CategoryDto.class);

    }
}
