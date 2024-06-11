package com.pro.electronic.store.controllers;

import com.pro.electronic.store.dtos.*;
import com.pro.electronic.store.services.FileService;
import com.pro.electronic.store.services.ProductSerivce;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
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
@RequestMapping("/products")
public class ProductController {

    @Autowired
    private ProductSerivce productSerivce;

    @Value("${products.image.path}")
    private String Imagepath;

    @Autowired
    private FileService fileService;

    //create
    @PostMapping
    ResponseEntity<ProductDto> create(@Valid @RequestBody ProductDto productDto) {
        ProductDto product = productSerivce.create(productDto);
        return new ResponseEntity<>(product, HttpStatus.CREATED);
    }

    //delete
    @DeleteMapping("/{productId}")
    public ResponseEntity<ApiResponseMessage> delete(@PathVariable String productId) {
        productSerivce.delete(productId);
        ApiResponseMessage responseMessage = ApiResponseMessage.builder().message("Product Deleted Successfully..!").status(HttpStatus.OK).success(true).build();
        return new ResponseEntity<>(responseMessage, HttpStatus.OK);
    }


    //update
    @PutMapping("/{productId}")
    public ResponseEntity<ProductDto> update(@PathVariable String productId, @RequestBody ProductDto productDto) {
        ProductDto update = productSerivce.update(productDto, productId);
        return new ResponseEntity<>(update, HttpStatus.OK);
    }

    //get single
    @GetMapping("/{productId}")
    public ResponseEntity<ProductDto> getSingle(@PathVariable String productId) {
        ProductDto singleProduct = productSerivce.getSingleProduct(productId);
        return new ResponseEntity<>(singleProduct, HttpStatus.FOUND);
    }

    //getAll

    @GetMapping
    public ResponseEntity<PageableResponse<ProductDto>> getAll(
            @RequestParam(value = "pageNumber", defaultValue = "0", required = false) int pageNumber,
            @RequestParam(value = "pageSize", defaultValue = "10", required = false) int pageSize,
            @RequestParam(value = "sortBy", defaultValue = "title", required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = "asc", required = false) String sortDir
    ) {
        PageableResponse<ProductDto> allProduct = productSerivce.getAllProduct(pageNumber, pageSize, sortBy, sortDir);
        return new ResponseEntity<>(allProduct, HttpStatus.OK);
    }

    //getLive
    @GetMapping("/live")
    public ResponseEntity<PageableResponse<ProductDto>> getAllLive(
            @RequestParam(value = "pageNumber", defaultValue = "0", required = false) int pageNumber,
            @RequestParam(value = "pageSize", defaultValue = "10", required = false) int pageSize,
            @RequestParam(value = "sortBy", defaultValue = "title", required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = "asc", required = false) String sortDir
    ) {
        PageableResponse<ProductDto> allProduct = productSerivce.liveProductList(pageNumber, pageSize, sortBy, sortDir);
        return new ResponseEntity<>(allProduct, HttpStatus.OK);
    }

    //searchall

    @GetMapping("/search/{subTitle}")
    public ResponseEntity<PageableResponse<ProductDto>> searchAll(@PathVariable String subTitle,
                                                                  @RequestParam(value = "pageNumber", defaultValue = "0", required = false) int pageNumber,
                                                                  @RequestParam(value = "pageSize", defaultValue = "10", required = false) int pageSize,
                                                                  @RequestParam(value = "sortBy", defaultValue = "title", required = false) String sortBy,
                                                                  @RequestParam(value = "sortDir", defaultValue = "asc", required = false) String sortDir
    ) {
        PageableResponse<ProductDto> allProduct = productSerivce.searchByTitle(subTitle, pageNumber, pageSize, sortBy, sortDir);
        return new ResponseEntity<>(allProduct, HttpStatus.OK);
    }


//upload image

    @PostMapping("/image/{productId}")
    public ResponseEntity<ImageResponse> uploadProductImage(
            @RequestParam("productImage") MultipartFile image,
            @PathVariable String productId) throws IOException {
        //upload image
        String fileName = fileService.uploadFile(image, Imagepath);
        ProductDto productdto= productSerivce.getSingleProduct(productId);
        productdto.setProductImage(fileName);

        ProductDto updated = productSerivce.update(productdto, productId);
        ImageResponse response = ImageResponse.builder().imageName(updated.getProductImage()).message("Image uploaded successfully..!").status(HttpStatus.OK).success(true).build();
        return  new ResponseEntity<>(response,HttpStatus.CREATED);
    }

    //serve image

    @GetMapping("/image/{productId}")
    public  void ServeCategoryImage (@PathVariable String productId, HttpServletResponse response) throws IOException {
        ProductDto productDto = productSerivce.getSingleProduct(productId);
        InputStream resource = fileService.getResource(Imagepath, productDto.getProductImage());
        response.setContentType(MediaType.IMAGE_JPEG_VALUE);
        StreamUtils.copy(resource,response.getOutputStream());
    }

}
