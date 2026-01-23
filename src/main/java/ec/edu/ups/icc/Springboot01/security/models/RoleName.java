package ec.edu.ups.icc.Springboot01.security.models;

public enum RoleName {
    ROLE_USER("Usuario estándar con permisos básicos"),
    ROLE_ADMIN("Administrador con permisos completos"),
    ROLE_MODERATOR("Moderador con permisos intermedios");

    private final String description;

    // Constructor del enum para asignar la descripción
    RoleName(String description) {
        this.description = description;
    }

    // Método que el DataInitializer estaba buscando
    public String getDescription() {
        return description;
    }
}