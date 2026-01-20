package ec.edu.ups.icc.Springboot01.shared.dtos;

import jakarta.validation.constraints.*;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

public class PageableDto {
    @Min(value = 0)
    private int page = 0;
    @Min(value = 1) @Max(value = 100)
    private int size = 10;
    private String[] sort = {"id"};

    public PageableDto() {}
    public int getPage() { return page; }
    public void setPage(int page) { this.page = page; }
    public int getSize() { return size; }
    public void setSize(int size) { this.size = size; }
    public String[] getSort() { return sort; }
    public void setSort(String[] sort) { this.sort = sort; }

    public Pageable toPageable() {
        Sort sortObj = Sort.by("id");
        if (sort != null && sort.length > 0) {
            Sort.Order[] orders = new Sort.Order[sort.length];
            for (int i = 0; i < sort.length; i++) {
                String[] parts = sort[i].split(",");
                orders[i] = (parts.length > 1 && "desc".equalsIgnoreCase(parts[1])) 
                    ? Sort.Order.desc(parts[0]) : Sort.Order.asc(parts[0]);
            }
            sortObj = Sort.by(orders);
        }
        return PageRequest.of(page, size, sortObj);
    }
}