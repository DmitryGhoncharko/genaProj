package by.ghoncharko.webproject.entity;

import java.util.Objects;

/**
 * Entity class Role
 *
 * @author Dmitry Ghoncharko
 */
public class Role implements Entity {
    private Integer id;
    private String roleName;

    private Role() {

    }

    public Integer getId() {
        return id;
    }

    public String getRoleName() {
        return roleName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Role role = (Role) o;
        return Objects.equals(id, role.id) &&
                Objects.equals(roleName, role.roleName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, roleName);
    }

    @Override
    public String toString() {
        return "Role{" +
                "id=" + id +
                ", roleName='" + roleName + '\'' +
                '}';
    }

    public static class Builder {
        private final Role newRole;

        public Builder() {
            newRole = new Role();
        }

        public Builder withId(Integer id) {
            newRole.id = id;
            return this;
        }

        public Builder withRoleName(String roleName) {
            newRole.roleName = roleName;
            return this;
        }

        public Role build() {
            return newRole;
        }
    }
}
