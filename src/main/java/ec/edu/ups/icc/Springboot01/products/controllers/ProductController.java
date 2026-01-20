package ec.edu.ups.icc.Springboot01.products.controllers;

import ec.edu.ups.icc.Springboot01.products.dtos.*;
import ec.edu.ups.icc.Springboot01.products.services.ProductService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Slice;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    // 1. Page Response (Paginación Completa)
    // Evidencia 9.2: GET /api/products?page=0&size=5
    @GetMapping
    public ResponseEntity<Page<ProductResponseDto>> findAllPaged(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id,asc") String[] sort) {
        return ResponseEntity.ok(productService.findAllPaged(page, size, sort));
    }

    // 2. Slice Response (Paginación Ligera)
    // Evidencia 9.2: GET /api/products/slice?page=0&size=5
    @GetMapping("/slice")
    public ResponseEntity<Slice<ProductResponseDto>> findAllSlice(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id,asc") String[] sort) {
        return ResponseEntity.ok(productService.findAllSlice(page, size, sort));
    }

    // 3. Filtros + Paginación
    // Evidencia 9.2: GET /api/products/search?name=laptop&page=0&size=3
    @GetMapping("/search")
    public ResponseEntity<Page<ProductResponseDto>> findWithFilters(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) Double minPrice,
            @RequestParam(required = false) Double maxPrice,
            @RequestParam(required = false) Long categoryId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id,asc") String[] sort) {
        return ResponseEntity.ok(productService.findWithFiltersPaged(name, minPrice, maxPrice, categoryId, page, size, sort));
    }

    // --- Otros Endpoints ---

    @PostMapping
    public ResponseEntity<ProductResponseDto> create(@RequestBody CreateProductDto dto) {
        return ResponseEntity.ok(productService.create(dto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductResponseDto> findOne(@PathVariable int id) {
        return ResponseEntity.ok(productService.findOne(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductResponseDto> update(@PathVariable int id, @RequestBody UpdateProductDto dto) {
        return ResponseEntity.ok(productService.update(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> delete(@PathVariable int id) {
        return ResponseEntity.ok(productService.delete(id));
    }

    // Se mantiene la validación de nombre solicitada
    @PostMapping("/validate-name")
    public ResponseEntity<Boolean> validateName(@RequestBody ValidateProductNameDto dto) {
        return ResponseEntity.ok(productService.validateName(dto.id, dto.name));
    }
}