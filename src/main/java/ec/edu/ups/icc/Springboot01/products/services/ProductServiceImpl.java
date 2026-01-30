package ec.edu.ups.icc.Springboot01.products.services;

import ec.edu.ups.icc.Springboot01.products.dtos.*;
import ec.edu.ups.icc.Springboot01.products.entities.ProductEntity;
import ec.edu.ups.icc.Springboot01.products.repositories.ProductRepository;
import ec.edu.ups.icc.Springboot01.security.models.UserDetailsImpl;
import ec.edu.ups.icc.Springboot01.users.repositories.UserRepository;
import ec.edu.ups.icc.Springboot01.categories.repositories.CategoryRepository;
import ec.edu.ups.icc.Springboot01.users.entities.UserEntity;
import ec.edu.ups.icc.Springboot01.categories.entities.CategoryEntity;
import ec.edu.ups.icc.Springboot01.products.mappers.ProductMapper;

import org.springframework.data.domain.*;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepo;
    private final UserRepository userRepo;
    private final CategoryRepository categoryRepo;

    public ProductServiceImpl(ProductRepository productRepo, UserRepository userRepo, CategoryRepository categoryRepo) {
        this.productRepo = productRepo;
        this.userRepo = userRepo;
        this.categoryRepo = categoryRepo;
    }

    @Override
    @Transactional
    public ProductResponseDto create(CreateProductDto dto, UserDetailsImpl currentUser) {
        // Obtenemos el owner directamente del token por seguridad
        UserEntity owner = userRepo.findById(currentUser.getId())
                .orElseThrow(() -> new RuntimeException("Usuario autenticado no encontrado"));
        
        Set<CategoryEntity> categories = new HashSet<>();
        if (dto.categoryIds != null) {
            for (Long catId : dto.categoryIds) {
                categoryRepo.findById(catId).ifPresent(categories::add);
            }
        }

        ProductEntity entity = new ProductEntity();
        entity.setName(dto.name);
        entity.setPrice(dto.price);
        entity.setDescription(dto.description);
        entity.setOwner(owner); // Asignación automática de propiedad
        entity.setCategories(categories);
        entity.setDeleted(false);
        
        return ProductMapper.toResponse(productRepo.save(entity));
    }

    @Override
    @Transactional
    public ProductResponseDto update(int id, UpdateProductDto dto, UserDetailsImpl currentUser) {
        ProductEntity entity = productRepo.findById((long) id)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));
        
        validateOwnership(entity, currentUser); // VALIDACIÓN PRÁCTICA 13

        entity.setName(dto.name);
        entity.setPrice(dto.price);
        entity.setDescription(dto.description);
        return ProductMapper.toResponse(productRepo.save(entity));
    }

    @Override
    @Transactional
    public ProductResponseDto partialUpdate(int id, PartialUpdateProductDto dto, UserDetailsImpl currentUser) {
        ProductEntity entity = productRepo.findById((long) id)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));
        
        validateOwnership(entity, currentUser);

        if (dto.name != null) entity.setName(dto.name);
        if (dto.price != null) entity.setPrice(dto.price);
        return ProductMapper.toResponse(productRepo.save(entity));
    }

    @Override
    @Transactional
    public Object delete(int id, UserDetailsImpl currentUser) {
        ProductEntity entity = productRepo.findById((long) id)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));
        
        validateOwnership(entity, currentUser);

        entity.setDeleted(true); // Se mantiene el borrado lógico
        productRepo.save(entity);
        return new Object() { public String message = "Producto eliminado por su dueño o administrador"; };
    }

    // ============== VALIDACIÓN DE PROPIEDAD (OWNERSHIP) ==============

    private void validateOwnership(ProductEntity product, UserDetailsImpl currentUser) {
        // 1. ADMIN y MODERATOR se saltan la validación
        if (hasAnyRole(currentUser, "ROLE_ADMIN", "ROLE_MODERATOR")) {
            return;
        }

        // 2. USER solo puede modificar sus propios productos
        if (!product.getOwner().getId().equals(currentUser.getId())) {
            throw new AccessDeniedException("No tienes permiso sobre este recurso ajeno");
        }
    }

    private boolean hasAnyRole(UserDetailsImpl user, String... roles) {
        for (String role : roles) {
            for (GrantedAuthority authority : user.getAuthorities()) {
                if (authority.getAuthority().equals(role)) return true;
            }
        }
        return false;
    }

    // ============== MÉTODOS DE CONSULTA (SIN CAMBIOS) ==============

    @Override
    public List<ProductResponseDto> findAll() {
        return productRepo.findAll().stream()
                .filter(p -> !p.isDeleted()) // Filtro de borrado lógico
                .map(ProductMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public ProductResponseDto findOne(int id) {
        return productRepo.findById((long) id)
                .filter(p -> !p.isDeleted())
                .map(ProductMapper::toResponse)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado o eliminado"));
    }

    @Override
    public Page<ProductResponseDto> findAllPaged(int page, int size, String[] sort) {
        Pageable pageable = createPageable(page, size, sort);
        return productRepo.findAll(pageable).map(ProductMapper::toResponse);
    }

    @Override
    public Slice<ProductResponseDto> findAllSlice(int page, int size, String[] sort) {
        Pageable pageable = createPageable(page, size, sort);
        return productRepo.findBy(pageable).map(ProductMapper::toResponse);
    }

    @Override
    public Page<ProductResponseDto> findWithFiltersPaged(String name, Double minPrice, Double maxPrice, Long categoryId, int page, int size, String[] sort) {
        Pageable pageable = createPageable(page, size, sort);
        return productRepo.findAllWithFilters(name, minPrice, maxPrice, categoryId, pageable).map(ProductMapper::toResponse);
    }

    @Override
    public boolean validateName(Integer id, String name) {
        return name != null && !name.trim().isEmpty();
    }

    private Pageable createPageable(int page, int size, String[] sort) {
        Sort.Direction direction = (sort.length == 2 && sort[1].equalsIgnoreCase("desc")) ? Sort.Direction.DESC : Sort.Direction.ASC;
        return PageRequest.of(page, size, Sort.by(direction, sort[0]));
    }
}