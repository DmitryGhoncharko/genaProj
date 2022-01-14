package by.ghoncharko.webproject.entity;

import java.util.Objects;

/**
 * Entity class Role
 *
 * @author Dmitry Ghoncharko
 */
public final class Role implements Entity {
    private final Integer id;
    private final String roleName;

    private Role(Builder builder) {
        id = builder.id;
        roleName = builder.roleName;
    }

    public Integer getId() {
        return id;
    }

    public String getRoleName() {
        return roleName;
    }

    public static class Builder {
        private Integer id;
        private String roleName;


        public Builder withId(Integer id) {
            this.id = id;
            return this;
        }

        public Builder withRoleName(String roleName) {
            this.roleName = roleName;
            return this;
        }

        public Role build() {
            return new Role(this);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Role role = (Role) o;

        if (!Objects.equals(id, role.id)) return false;
        return Objects.equals(roleName, role.roleName);
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (roleName != null ? roleName.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Role{" +
                "id=" + id +
                ", roleName='" + roleName + '\'' +
                '}';
    }
}
