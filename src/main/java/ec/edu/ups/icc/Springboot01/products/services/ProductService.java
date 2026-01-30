package ec.edu.ups.icc.Springboot01.products.services;

import ec.edu.ups.icc.Springboot01.products.dtos.*;
import ec.edu.ups.icc.Springboot01.security.models.UserDetailsImpl;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Slice;
import java.util.List;

public interface ProductService {
    // CRUD con Ownership
    ProductResponseDto create(CreateProductDto dto, UserDetailsImpl currentUser);
    ProductResponseDto update(int id, UpdateProductDto dto, UserDetailsImpl currentUser);
    ProductResponseDto partialUpdate(int id, PartialUpdateProductDto dto, UserDetailsImpl currentUser);
    Object delete(int id, UserDetailsImpl currentUser);

    // Consultas y Paginación
    List<ProductResponseDto> findAll();
    ProductResponseDto findOne(int id);
    Page<ProductResponseDto> findAllPaged(int page, int size, String[] sort);
    Slice<ProductResponseDto> findAllSlice(int page, int size, String[] sort);
    Page<ProductResponseDto> findWithFiltersPaged(String name, Double minPrice, Double maxPrice, Long categoryId, int page, int size, String[] sort);
    
    boolean validateName(Integer id, String name);
}