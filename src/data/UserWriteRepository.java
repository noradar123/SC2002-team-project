package data;
import entity.User;

public interface UserWriteRepository { 
    void save(User user);
}
