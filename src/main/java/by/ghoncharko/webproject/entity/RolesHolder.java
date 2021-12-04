package by.ghoncharko.webproject.entity;


public final class RolesHolder {
    private RolesHolder() {

    }

    public static final Role CLIENT = new Role.Builder().
            withId(1).withRoleName("client").build();
    public static final Role PHARMACY = new Role.Builder().
            withId(2).withRoleName("pharmacy").build();
    public static final Role DOCTOR = new Role.Builder().
            withId(3).withRoleName("doctor").build();
    public static final Role ADMIN = new Role.Builder().
            withId(4).withRoleName("admin").build();
    public static final Role UNAUTHORIZED = new Role.Builder().
            withId(5).withRoleName("unauthorized").build();
}
