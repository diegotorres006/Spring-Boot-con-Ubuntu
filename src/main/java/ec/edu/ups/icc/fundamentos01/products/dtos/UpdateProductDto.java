package ec.edu.ups.icc.fundamentos01.products.dtos;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Size;
import java.util.Set;

public class UpdateProductDto {

    @Size(min = 3, max = 150, message = "El nombre debe tener entre 3 y 150 caracteres")
    public String name;

    @DecimalMin(value = "0.0", inclusive = false, message = "El precio debe ser mayor a 0")
    public Double price;

    @Size(max = 500, message = "La descripción no puede exceder los 500 caracteres")
    public String description;


    public Set<Long> categoryIds; 
}