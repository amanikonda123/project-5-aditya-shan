public interface Plant extends ExecutableEntity {
    String STUMP_KEY = "stump";
    int getHealth();
    void setHealth(int health);
}
