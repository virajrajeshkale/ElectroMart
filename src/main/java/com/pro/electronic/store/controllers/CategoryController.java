package com.pro.electronic.store.controllers;

import com.pro.electronic.store.dtos.*;
import com.pro.electronic.store.services.CategoryService;
import com.pro.electronic.store.services.FileService;
import com.pro.electronic.store.services.ProductSerivce;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;

@RestController
@RequestMapping("/categories")
public class CategoryController {

    @Autowired
    private ProductSerivce productSerivce;

    @Autowired
    private CategoryService categoryService;
    @Autowired
    private FileService fileService;

    @Value("${category.items.image.path}")
    private String ImagePath;

    //create
    @PostMapping
    public ResponseEntity<CategoryDto> categoryResponseEntity(@Valid @RequestBody CategoryDto categoryDto) {
        CategoryDto category = categoryService.Create(categoryDto);
        return new ResponseEntity<>(category, HttpStatus.CREATED);
    }

    //update
    @PutMapping("/{categoryId}")
    public ResponseEntity<CategoryDto> categoryUpdate(@RequestBody CategoryDto categoryDto, @PathVariable("categoryId") String categoryId) {
        CategoryDto update = categoryService.Update(categoryDto, categoryId);
        return new ResponseEntity<>(update, HttpStatus.OK);
    }


    //delete
    @DeleteMapping("/{categoryId}")
    public ResponseEntity<ApiResponseMessage> categoryDelete(@PathVariable("categoryId") String categoryId) {
        categoryService.Delete(categoryId);
        ApiResponseMessage responseMessage = ApiResponseMessage.builder().message("Category Deleted..!").success(true).status(HttpStatus.OK).build();
        return new ResponseEntity<>(responseMessage, HttpStatus.OK);
    }

    //getsingle
    @GetMapping("/{categoryId}")
    public ResponseEntity<CategoryDto> getSingle(@PathVariable("categoryId") String categoryId) {
        CategoryDto categoryDto = categoryService.getSingle(categoryId);
        return new ResponseEntity<>(categoryDto, HttpStatus.FOUND);
    }


    //getall

    @GetMapping
    public ResponseEntity<PageableResponse<CategoryDto>> getAll(
            @RequestParam(value = "pageNumber", defaultValue = "0", required = false) int pageNumber,
            @RequestParam(value = "pageSize", defaultValue = "10", required = false) int pageSize,
            @RequestParam(value = "sortBy", defaultValue = "title", required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = "asc", required = false) String sortDir) {

        PageableResponse<CategoryDto> pageableResponse = categoryService.getAll(pageNumber, pageSize, sortBy, sortDir);
        return new ResponseEntity<>(pageableResponse, HttpStatus.OK);
    }


    //image file upload

    @PostMapping("/image/{categoryId}")
    public ResponseEntity<ImageResponse> uploadCategoryImage(@RequestParam("Image") MultipartFile Image, @PathVariable String categoryId) throws IOException {
        String imageName = fileService.uploadFile(Image, ImagePath);

        //update user image name
        CategoryDto category = categoryService.getSingle(categoryId);
        category.setCoverImage(imageName);
        CategoryDto update = categoryService.Update(category, categoryId);
//        UserDto user = userService.GetUserById(userId);
        //     user.setProfile_pic_name(imageName);
        //      UserDto updatedUser = userService.UpdateUser(user, userId);
        ImageResponse imageResponse = ImageResponse.builder().imageName(imageName).message("Image Uploaded SuccessFully..!!").success(true).status(HttpStatus.CREATED).build();
        return new ResponseEntity<>(imageResponse, HttpStatus.CREATED);
    }


//serve user image

    @GetMapping("/image/{userId}")
    public void ServeCategoryImage(@PathVariable String userId, HttpServletResponse response) throws IOException {
        CategoryDto categoryDto = categoryService.getSingle(userId);
        InputStream resource = fileService.getResource(ImagePath, categoryDto.getCoverImage());
        response.setContentType(MediaType.IMAGE_JPEG_VALUE);
        StreamUtils.copy(resource, response.getOutputStream());
    }


//create product with category

    @PostMapping("/{categoryId}/products")
    public ResponseEntity<ProductDto> createProductWithCategory(
            @PathVariable("categoryId") String categoryId,
            @RequestBody ProductDto productDto
    ) {

        ProductDto produtWithCategory = productSerivce.createWithCategory(productDto, categoryId);
        return new ResponseEntity<>(produtWithCategory, HttpStatus.CREATED);
    }

    //update category of product
@PutMapping("/{categoryId}/products/{productId}")
    public  ResponseEntity<ProductDto> updateCategoryOfProduct(
            @PathVariable("productId") String productId,@PathVariable("categoryId") String categoryId
    ){
        ProductDto productDto = productSerivce.updateCategory(productId, categoryId);
        return  new ResponseEntity<>(productDto,HttpStatus.OK);
    }

    //get products list of same category
    @GetMapping("/{categoryId}/products")
    public  ResponseEntity<PageableResponse<ProductDto>> getAllProductOfCategory(
        @PathVariable("categoryId") String categoryId,
        @RequestParam(value = "pageNumber", defaultValue = "0", required = false) int pageNumber,
        @RequestParam(value = "pageSize", defaultValue = "10", required = false) int pageSize,
        @RequestParam(value = "sortBy", defaultValue = "title", required = false) String sortBy,
        @RequestParam(value = "sortDir", defaultValue = "asc", required = false) String sortDir
    ){
        PageableResponse<ProductDto> allProductOfCategory = productSerivce.getAllProductOfCategory(categoryId,pageNumber,pageSize,sortBy,sortDir);
            return  new ResponseEntity<>(allProductOfCategory,HttpStatus.OK);
    }
}