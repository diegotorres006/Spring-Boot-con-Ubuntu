package ec.edu.ups.icc.Springboot01.products.controllers;

import ec.edu.ups.icc.Springboot01.products.dtos.*;
import ec.edu.ups.icc.Springboot01.products.services.ProductService;
import ec.edu.ups.icc.Springboot01.security.models.UserDetailsImpl;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    // --- CONSULTAS ---
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<ProductResponseDto>> findAll() {
        return ResponseEntity.ok(productService.findAll());
    }

    @GetMapping("/paginated")
    public ResponseEntity<Page<ProductResponseDto>> findAllPaginated(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id,asc") String[] sort) {
        return ResponseEntity.ok(productService.findAllPaged(page, size, sort));
    }

    // --- ESCRITURA CON OWNERSHIP (Práctica 13) ---
    @PostMapping
    public ResponseEntity<ProductResponseDto> create(
            @Valid @RequestBody CreateProductDto dto,
            @AuthenticationPrincipal UserDetailsImpl currentUser) {
        return ResponseEntity.ok(productService.create(dto, currentUser));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductResponseDto> update(
            @PathVariable int id, 
            @Valid @RequestBody UpdateProductDto dto,
            @AuthenticationPrincipal UserDetailsImpl currentUser) {
        return ResponseEntity.ok(productService.update(id, dto, currentUser));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> delete(
            @PathVariable int id,
            @AuthenticationPrincipal UserDetailsImpl currentUser) {
        return ResponseEntity.ok(productService.delete(id, currentUser));
    }
}