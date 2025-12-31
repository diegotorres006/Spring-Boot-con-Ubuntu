package ec.edu.ups.icc.fundamentos01.products.services;

import ec.edu.ups.icc.fundamentos01.exception.domain.ConflictException;
import ec.edu.ups.icc.fundamentos01.exception.domain.NotFoundException;
import ec.edu.ups.icc.fundamentos01.products.dtos.*;
import ec.edu.ups.icc.fundamentos01.products.mappers.ProductMapper;
import ec.edu.ups.icc.fundamentos01.products.models.Product;
import ec.edu.ups.icc.fundamentos01.products.repositories.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository repository;

    public ProductServiceImpl(ProductRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<ProductResponseDto> findAll() {
        return repository.findAll().stream()
                .map(Product::fromEntity)
                .map(ProductMapper::toResponse)
                .toList();
    }

    @Override
    public Object findOne(int id) {
        return repository.findById((long) id)
                .map(Product::fromEntity)
                .map(ProductMapper::toResponse)
                .orElseThrow(() -> new NotFoundException("Producto no encontrado con ID: " + id));
    }

    @Override
    public ProductResponseDto create(CreateProductDto dto) {
        // REGLA DE NEGOCIO: No permitir nombres duplicados
        if (repository.existsByName(dto.name)) {
            throw new ConflictException("Ya existe un producto con el nombre: " + dto.name);
        }

        return Optional.of(dto)
                .map(Product::fromDto)
                .map(Product::toEntity)
                .map(repository::save)
                .map(Product::fromEntity)
                .map(ProductMapper::toResponse)
                .orElseThrow(() -> new RuntimeException("Error creando el producto"));
    }

    @Override
    public Object update(int id, UpdateProductDto dto) {
        return repository.findById((long) id)
                .map(Product::fromEntity)
                .map(p -> p.update(dto))
                .map(Product::toEntity)
                .map(repository::save)
                .map(Product::fromEntity)
                .map(ProductMapper::toResponse)
                .orElseThrow(() -> new NotFoundException("Producto no encontrado para actualizar"));
    }

    @Override
    public Object partialUpdate(int id, PartialUpdateProductDto dto) {
        return repository.findById((long) id)
                .map(Product::fromEntity)
                .map(p -> p.partialUpdate(dto))
                .map(Product::toEntity)
                .map(repository::save)
                .map(Product::fromEntity)
                .map(ProductMapper::toResponse)
                .orElseThrow(() -> new NotFoundException("Producto no encontrado para actualizar"));
    }

    @Override
    public Object delete(int id) {
        repository.findById((long) id).ifPresentOrElse(
                repository::delete,
                () -> { throw new NotFoundException("Producto no encontrado para eliminar"); }
        );
        return new Object() { public String message = "Deleted successfully"; };
    }
}