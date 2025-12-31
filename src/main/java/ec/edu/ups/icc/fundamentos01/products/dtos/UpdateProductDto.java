package ec.edu.ups.icc.fundamentos01.products.dtos;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class UpdateProductDto {

    @NotBlank(message = "El nombre es obligatorio")
    @Size(min = 3, max = 100)
    public String name;

    @Size(max = 255)
    public String description;

    @NotNull(message = "El precio es obligatorio")
    @Min(value = 0)
    public double price;
}