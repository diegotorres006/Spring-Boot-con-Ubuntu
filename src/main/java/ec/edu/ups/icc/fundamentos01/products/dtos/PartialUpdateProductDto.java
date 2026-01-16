package ec.edu.ups.icc.Springboot01.products.dtos;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;

public class PartialUpdateProductDto {

    @Size(min = 3, max = 100)
    public String name;

    @Size(max = 255)
    public String description;

    @Min(value = 0)
    public Double price; 
}
