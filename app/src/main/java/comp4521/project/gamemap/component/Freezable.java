package comp4521.project.gamemap.component;

public interface Freezable {
    default void freeze() {}
    default void unfreeze() {}
}
